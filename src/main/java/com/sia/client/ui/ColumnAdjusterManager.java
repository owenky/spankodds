package com.sia.client.ui;

import javax.swing.JComponent;

import static com.sia.client.config.Utils.log;

public class ColumnAdjusterManager {

    private final ColumnCustomizableTable<?> mainTable;
    private TableColumnAdjuster mainTableColumnAdjuster;
    private TableColumnAdjuster rowHeaderTableColumnAdjuster;

    public ColumnAdjusterManager(ColumnCustomizableTable<?> mainTable) {
        this.mainTable = mainTable;
    }
    public void setColumnHeaderIncluded(boolean toInclude) {
        getMainTableColumnAdjuster().setColumnHeaderIncluded(toInclude);
        getRowHeaderTableColumnAdjuster().setColumnHeaderIncluded(toInclude);
    }
    public void adjustColumns(boolean includeHeaders) {
        getMainTableColumnAdjuster().adjustColumns(includeHeaders);
        int prefWidthOfCols = getRowHeaderTableColumnAdjuster().adjustColumns(includeHeaders);
log("in ColumnAdjusterManager, prefWidthOfCols="+prefWidthOfCols+", old prefwidth="+mainTable.getRowHeaderTable().getPreferredSize().getWidth());
        JComponent rowHeaderTable = mainTable.getRowHeaderTable();
        rowHeaderTable.getParent().setPreferredSize(rowHeaderTable.getPreferredSize());
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
