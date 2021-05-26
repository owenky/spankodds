package com.sia.client.ui;

import com.sia.client.ui.ColumnAdjustPreparer.AdjustRegion;

import java.util.List;

public class ColumnAdjusterManager {

    private final ColumnCustomizableTable<?> mainTable;
    private TableColumnAdjuster mainTableColumnAdjuster;
    private TableColumnAdjuster rowHeaderTableColumnAdjuster;
    private final ColumnAdjustPreparer mainTablePreparer;
    private final ColumnAdjustPreparer rowHeaderTablePreparer;

    public ColumnAdjusterManager(ColumnCustomizableTable<?> mainTable) {
        this.mainTable = mainTable;
        this.mainTablePreparer = new ColumnAdjustPreparer(mainTable);
        this.rowHeaderTablePreparer = new ColumnAdjustPreparer(mainTable.getRowHeaderTable());
    }
    public void clear() {
        this.getMainTableColumnAdjuster().clear();
        mainTablePreparer.clear();
        rowHeaderTablePreparer.clear();
    }
    public void adjustColumns() {
        List<AdjustRegion> mainTableAdjustRegions = mainTablePreparer.getAdjustRegions();
        for(AdjustRegion region:mainTableAdjustRegions) {
            getMainTableColumnAdjuster().adjustColumns(region);
        }

        List<AdjustRegion> rowHeaderAdjustRegions = rowHeaderTablePreparer.getAdjustRegions();
        for(AdjustRegion region: rowHeaderAdjustRegions) {
            getRowHeaderTableColumnAdjuster().adjustColumns(region);
        }

        if ( rowHeaderAdjustRegions.size() > 0) {
            RowHeaderTable<?> rowHeaderTable = mainTable.getRowHeaderTable();
            rowHeaderTable.optimizeSize();
        }
    }
    public void adjustColumnsOnRows(Integer ... gameIds) {
        getMainTableColumnAdjuster().adjustColumnsOnRow(gameIds);
        getRowHeaderTableColumnAdjuster().adjustColumnsOnRow(gameIds);
    }
    private TableColumnAdjuster getMainTableColumnAdjuster() {
        if ( null == mainTableColumnAdjuster) {
            mainTableColumnAdjuster = new TableColumnAdjuster(mainTable,mainTable.getMarginProvider());
        }
        return mainTableColumnAdjuster;
    }
    private TableColumnAdjuster getRowHeaderTableColumnAdjuster() {
        if ( null == rowHeaderTableColumnAdjuster) {
            rowHeaderTableColumnAdjuster = new TableColumnAdjuster(mainTable.getRowHeaderTable(),mainTable.getMarginProvider());
        }
        return rowHeaderTableColumnAdjuster;
    }
}
