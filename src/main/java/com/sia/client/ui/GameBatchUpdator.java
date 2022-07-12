package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.ui.control.MainScreen;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameBatchUpdator implements TableModelListener {

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
    public void flush(TableModelEvent e) {
        if ( null != e) {
            addUpdateEvent(e);
            forcing = true;
        }

        checkToUpdate();
    }
    /**
     * this method must be run in EDT -- 2021-11-16
     */
    private synchronized void checkToUpdate() {
        if ( AppController.isReadyForMessageProcessing()) {
            long now = System.currentTimeMillis();
            if (SiaConst.DataRefreshRate < (now - lastUpdateTime) || forcing) {
                for (TableModelEvent e : pendingUpdateEvents) {
                    ColumnCustomizableDataModel<?> model = (ColumnCustomizableDataModel<?>) e.getSource();
                    SpankyWindow spankyWindow = SpankyWindow.findSpankyWindow(model.getSpankyWindowConfig().getWindowIndex());
                    if ( null != spankyWindow && null != spankyWindow.getSportsTabPane()) { //when user close a window, null scenario might happen -- 2021-11-25
                        Component selectedComp = spankyWindow.getSportsTabPane().getSelectedComponent();
                        if (selectedComp instanceof MainScreen) {
                            if (selectedComp.isShowing()) {
                                model.fireTableChanged(e);
                            }
                        }
                    }
                }
Utils.consoleLogPeek("In GameBatchUpdator, accumulateCnt="+accumulateCnt+", updated row count="+updatedRowCnt+", ago="+(now-lastUpdateTime)+", processing time="+(System.currentTimeMillis()-now)+", forcing="+forcing);
                pendingUpdateEvents.clear();
                pendingUpdatedRowModelIndexSet.clear();
                accumulateCnt = 0;
                updatedRowCnt = 0;

                lastUpdateTime = now;
                forcing = false;
            }
        }
    }

    /**
     * this method is called only when TableUtils.toRebuildCache(event) is false, so no need to check TableUtils.toRebuildCache(event) condition. -- 2021-11-16
     */
    public synchronized void addUpdateEvent(TableModelEvent event) {
        if ( null == event) {
            return;
        }
        accumulateCnt++;
        int firstRow = event.getFirstRow();
        int lastRow = event.getLastRow();
        List<int[]> newUpdateRegions = getNewUpdateRegions(firstRow,lastRow,pendingUpdatedRowModelIndexSet);
        if ( null != newUpdateRegions ) {
            for (int[] newRegion : newUpdateRegions) {
                TableModelEvent e = new TableModelEvent((TableModel) event.getSource(), newRegion[0], newRegion[1], event.getColumn(), event.getType());
                pendingUpdateEvents.add(e);
                updatedRowCnt += newRegion[1] - newRegion[0] + 1;
            }
            for (int index = firstRow; index <= lastRow; index++) {
                pendingUpdatedRowModelIndexSet.add(index);
            }
        } else {
            pendingUpdateEvents.add(event);
            updatedRowCnt++;
        }
        if ( TableModelEvent.INSERT == event.getType() || TableModelEvent.DELETE == event.getType()) {
            //insert or delete event must be executed immediately to ensure future model index calculated correctly -- 06/30/2022
            forcing= true;
            checkToUpdate();
        }
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        lastUpdateTime = System.currentTimeMillis();

    }
    static List<int[]> getNewUpdateRegions(int firstRow,int lastRow,Set<Integer>pendingUpdatedRowModelIndexSet) {

        List<int[]> result = new ArrayList<>();
        if ( firstRow < 0 || lastRow < 0 || Integer.MAX_VALUE == lastRow) {
           return null;
        }

        int[] region = createNewRegion();
        for (int index=firstRow; index<=lastRow; index++) {
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
