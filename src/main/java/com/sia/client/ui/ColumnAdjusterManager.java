package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.ColumnAdjustPreparer.AdjustRegion;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.sia.client.config.Logger.consoleLogPeek;

public class ColumnAdjusterManager {

    private final ColumnCustomizableTable<?> mainTable;
    private TableColumnAdjuster mainTableColumnAdjuster;
    private TableColumnAdjuster rowHeaderTableColumnAdjuster;
    private final ColumnAdjustPreparer mainTablePreparer;
    private final ColumnAdjustPreparer rowHeaderTablePreparer;
    private long lastAdjustingTime = 0;
    private int accumulatedCnt = 0;
    private static final int initialCapicity = 50;
    private final List<AdjustRegion> mainTableAdjustRegions = new ArrayList<>(initialCapicity);
    private final List<AdjustRegion> rowHeaderTableAdjustRegions = new ArrayList<>(initialCapicity);
    private final static ColumnAdjusterFlusher COLUMN_ADJUSTER_FLUSHER = new ColumnAdjusterFlusher();
    private final static Timer flushScheduler = new Timer(SiaConst.ColumnWidthRefreshRate,COLUMN_ADJUSTER_FLUSHER);

    static {
        flushScheduler.start();
    }

    public ColumnAdjusterManager(ColumnCustomizableTable<?> mainTable) {
        this.mainTable = mainTable;
        this.mainTablePreparer = new ColumnAdjustPreparer(mainTable);
        this.rowHeaderTablePreparer = new ColumnAdjustPreparer(mainTable.getRowHeaderTable());
        COLUMN_ADJUSTER_FLUSHER.addColumnAdjusterManager(this);
    }
    public void removeFromScheduler() {
        COLUMN_ADJUSTER_FLUSHER.rmColumnAdjusterManager(this);
    }
    public void clear() {
        this.getMainTableColumnAdjuster().clear();
        this.getRowHeaderTableColumnAdjuster().clear();
        mainTablePreparer.clear();
        rowHeaderTablePreparer.clear();
        clearAdjustRegions();
    }
    public synchronized void adjustColumns() {
        long now = System.currentTimeMillis();
        if (SiaConst.ColumnWidthRefreshRate <= (now-lastAdjustingTime)) {
            if ( 0 == mainTableAdjustRegions.size()) {
                accumulateRegions();
            }

            for (AdjustRegion region : mainTableAdjustRegions) {
                getMainTableColumnAdjuster().adjustColumns(region);
            }

            for (AdjustRegion region : rowHeaderTableAdjustRegions) {
                getRowHeaderTableColumnAdjuster().adjustColumns(region);
            }

            if (rowHeaderTableAdjustRegions.size() > 0) {
                RowHeaderTable<?> rowHeaderTable = mainTable.getRowHeaderTable();
                rowHeaderTable.optimizeSize();
            }
            long timeConsumed = System.currentTimeMillis()-now;
//            consoleLogPeek("ColumnAdjusterManager::adjustColumns, update "+ mainTableAdjustRegions.size()+" regions took " +timeConsumed+", accumulatedCnt="+accumulatedCnt+", ago="+(now-lastAdjustingTime)+", table name="+mainTable.getName());
            lastAdjustingTime = now;
            accumulatedCnt = 0;
            clearAdjustRegions();
        } else {
            accumulateRegions();
        }
    }
    private void accumulateRegions() {
        mainTableAdjustRegions.addAll(mainTablePreparer.getAdjustRegions());
        rowHeaderTableAdjustRegions.addAll(rowHeaderTablePreparer.getAdjustRegions());
        accumulatedCnt++;
    }
    private synchronized void clearAdjustRegions() {
        mainTableAdjustRegions.clear();
        rowHeaderTableAdjustRegions.clear();
    }
    public synchronized void adjustColumnsOnRows(Integer ... rowViewIndice) {
        for(int ind: rowViewIndice) {
            AdjustRegion mainTableAdjustRegion = new AdjustRegion(ind,ind,0,mainTable.getColumnCount()-1);
            AdjustRegion rowHeaderTableAdjustRegion = new AdjustRegion(ind,ind,0,mainTable.getRowHeaderTable().getColumnCount()-1);
            mainTableAdjustRegions.add(mainTableAdjustRegion);
            rowHeaderTableAdjustRegions.add(rowHeaderTableAdjustRegion);
            accumulatedCnt++;
        }
//        getMainTableColumnAdjuster().adjustColumnsOnRow(rowViewIndice);
//        getRowHeaderTableColumnAdjuster().adjustColumnsOnRow(rowViewIndice);
    }
    public TableColumnAdjuster getMainTableColumnAdjuster() {
        if ( null == mainTableColumnAdjuster) {
            mainTableColumnAdjuster = new TableColumnAdjuster(mainTable,mainTable.getMarginProvider());
        }
        return mainTableColumnAdjuster;
    }
    public TableColumnAdjuster getRowHeaderTableColumnAdjuster() {
        if ( null == rowHeaderTableColumnAdjuster) {
            rowHeaderTableColumnAdjuster = new TableColumnAdjuster(mainTable.getRowHeaderTable(),mainTable.getMarginProvider());
        }
        return rowHeaderTableColumnAdjuster;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class ColumnAdjusterFlusher implements ActionListener {

        private final List<ColumnAdjusterManager> columnAdjusterManagerList = new ArrayList<>();
        @Override
        public void actionPerformed(final ActionEvent e) {
            if ( AppController.isReadyForMessageProcessing()) {
                for (ColumnAdjusterManager manager : columnAdjusterManagerList) {
                    if (manager.mainTable.isShowing()) {
                        manager.adjustColumns();
                    }
                }
            }
        }
        public void addColumnAdjusterManager(ColumnAdjusterManager manager) {
            columnAdjusterManagerList.add(manager);
        }
        public void rmColumnAdjusterManager(ColumnAdjusterManager manager) {
            columnAdjusterManagerList.remove(manager);
            consoleLogPeek("remove manager "+manager+" from ColumnAdjusterFlusher");
        }
    }
}
