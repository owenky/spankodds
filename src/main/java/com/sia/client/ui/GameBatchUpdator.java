package com.sia.client.ui;

import com.sia.client.config.Logger;
import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;

import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameBatchUpdator {

    private final Timer flushingScheduler;
    private long lastUpdateTime = 0;
    private List<TableModelEvent> pendingUpdateEvents = new ArrayList<>(20);
    private Set<Integer> pendingUpdatedRowModelIndexSet = new HashSet<>();
    private int accumulateCnt = 0;
    private int updatedRowCnt = 0;
    private boolean forcing = false;

    public static GameBatchUpdator instance() {
        return LazyInitHolder.instance;
    }
    private GameBatchUpdator() {
        ActionListener al = e->this.checkToUpdate();
        flushingScheduler = new Timer(SiaConst.DataRefreshRate,al);
        flushingScheduler.start();
    }
    public void flush() {
        flush(null);
    }
    public void flush(TableModelEvent e) {
        if ( null != e) {
            addUpdateEvent(e);
        }
        forcing = true;
        checkToUpdate();
    }
    /**
     * this method must be run in EDT -- 2021-11-16
     */
    private void checkToUpdate() {
        long now = System.currentTimeMillis();
        if ( SiaConst.DataRefreshRate< (now-lastUpdateTime) || forcing) {
            for (TableModelEvent e : pendingUpdateEvents) {
                ColumnCustomizableDataModel<?> model = (ColumnCustomizableDataModel<?>)e.getSource();
                model.fireTableChanged(e);
            }
Logger.consoleLogPeek("In GameBatchUpdator, accumulateCnt="+accumulateCnt+", updated row count="+updatedRowCnt+", ago="+(now-lastUpdateTime)+", processing time="+(System.currentTimeMillis()-now)+", forcing="+forcing);
            pendingUpdateEvents.clear();
            pendingUpdatedRowModelIndexSet.clear();
            accumulateCnt=0;
            updatedRowCnt = 0;

            lastUpdateTime = now;
            forcing = false;
        }
    }

    /**
     * this method is called only when TableUtils.toRebuildCache(event) is false, so no need to check TableUtils.toRebuildCache(event) condition. -- 2021-11-16
     */
    public void addUpdateEvent(TableModelEvent event) {
        if ( event.getFirstRow() < 0 || event.getFirstRow() <0 ) {
            addToPendingUpdateEvents(event);
            flush();
        }
        addToPendingUpdateEvents(event);
    }
    private void addToPendingUpdateEvents(TableModelEvent event) {
        accumulateCnt++;
        int firstRow = event.getFirstRow();
        int lastRow = event.getLastRow();
        List<int[]> newUpdateRegions = getNewUpdateRegions(firstRow,lastRow,pendingUpdatedRowModelIndexSet);
        for(int [] newRegion: newUpdateRegions) {
            TableModelEvent e = new TableModelEvent((TableModel)event.getSource(),newRegion[0],newRegion[1],event.getColumn(),event.getType());
            pendingUpdateEvents.add(e);
            updatedRowCnt += newRegion[1]-newRegion[0]+1;
        }
        for(int index=firstRow;index<=lastRow;index++) {
            pendingUpdatedRowModelIndexSet.add(index);
        }
    }
    static List<int[]> getNewUpdateRegions(int firstRow,int LastRow,Set<Integer>pendingUpdatedRowModelIndexSet) {

        List<int[]> result = new ArrayList<>();
        int[] region = createNewRegion();
        for (int index=firstRow; index<=LastRow; index++) {
            if ( pendingUpdatedRowModelIndexSet.contains(index)) {
                if ( 0 <= region[0]) {
                    region[1]=index-1;
                    region = createNewRegion();
                }
            } else {
                if ( 0 > region[0]) {
                    region[0]=index;
                    result.add(region);
                }
                region[1]=index;
            }
        }
        return result;
    }
    static int [] createNewRegion() {
        return new int [] {-1,-1};
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final GameBatchUpdator instance = new GameBatchUpdator();
    }
}
