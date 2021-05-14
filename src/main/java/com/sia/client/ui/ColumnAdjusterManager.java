package com.sia.client.ui;

public class ColumnAdjusterManager {

    private final ColumnCustomizableTable mainTable;
    private TableColumnAdjuster mainTableColumnAdjuster;
    private TableColumnAdjuster rowHeaderTableColumnAdjuster;

    public ColumnAdjusterManager(ColumnCustomizableTable mainTable) {
        this.mainTable = mainTable;
    }
    public void setColumnHeaderIncluded(boolean toInclude) {
        getMainTableColumnAdjuster().setColumnHeaderIncluded(toInclude);
        getRowHeaderTableColumnAdjuster().setColumnHeaderIncluded(toInclude);
    }
    public void adjustColumns(boolean includeHeaders) {
        getMainTableColumnAdjuster().adjustColumns(includeHeaders);
        getRowHeaderTableColumnAdjuster().adjustColumns(includeHeaders);
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
