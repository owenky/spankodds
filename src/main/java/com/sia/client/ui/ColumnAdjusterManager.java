package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.ColumnAdjustPreparer.AdjustRegion;

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

    public ColumnAdjusterManager(ColumnCustomizableTable<?> mainTable) {
        this.mainTable = mainTable;
        this.mainTablePreparer = new ColumnAdjustPreparer(mainTable);
        this.rowHeaderTablePreparer = new ColumnAdjustPreparer(mainTable.getRowHeaderTable());
    }
    public void clear() {
        this.getMainTableColumnAdjuster().clear();
        this.getRowHeaderTableColumnAdjuster().clear();
        mainTablePreparer.clear();
        rowHeaderTablePreparer.clear();
        clearAdjustRegions();
    }
    public void adjustColumns() {
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
            consoleLogPeek("TableColumnAdjuster::adjustColumns, update "+ mainTableAdjustRegions.size()+" regions took " +timeConsumed+", accumulatedCnt="+accumulatedCnt+", ago="+(now-lastAdjustingTime)+", table name="+mainTable.getName());
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
    private void clearAdjustRegions() {
        mainTableAdjustRegions.clear();
        rowHeaderTableAdjustRegions.clear();
    }
    public void adjustColumnsOnRows(Integer ... gameIds) {
        getMainTableColumnAdjuster().adjustColumnsOnRow(gameIds);
        getRowHeaderTableColumnAdjuster().adjustColumnsOnRow(gameIds);
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
}
