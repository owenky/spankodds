package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.MainGameTableModel;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MainGameTable extends JTable {

    private static final int SoccerRowHeight = 60;
    private static final int NormalRowHeight = 30;
    private TableColumnAdjuster tableColumnAdjuster;


    public MainGameTable() {
    }
    @Override
    public MainGameTableModel createDefaultDataModel() {
        return new MainGameTableModel();
    }
    @Override
    public MainGameTableModel getModel() {
        return (MainGameTableModel)super.getModel();
    }
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (getModel().isSoccer()) {
            return new LineRenderer(SiaConst.SoccerStr);
        } else {
            return new LineRenderer();
        }
    }
    public void addGameLine(LinesTableData gameLine) {
        getModel().addGameLine(gameLine);
    }
    public LinesTableData getLinesTableData(int row) {
        return getModel().getLinesTableData(row).linesTableData;
    }
    public void optimizeRowHeights() {
        if (getModel().isSoccer()) {
            setRowHeight(SoccerRowHeight);
        } else {
            setRowHeight(NormalRowHeight);
        }

    }
    public String getName() {
        return getModel().getName();
    }
    public void setName(String name) {
        getModel().setName(name);
    }
    public void adjustColumns(boolean includeHeaders) {
        getTableColumnAdjuster().adjustColumns(includeHeaders);
    }
    public void adjustColumn(int col) {
        getTableColumnAdjuster().adjustColumn(col);
    }
    private TableColumnAdjuster getTableColumnAdjuster() {
        if ( null == tableColumnAdjuster) {
            tableColumnAdjuster = new TableColumnAdjuster(this);
            this.getModel().addTableModelListener(e -> {
                tableColumnAdjuster.adjustColumn(e.getColumn());
            });
        }
        return tableColumnAdjuster;
    }
}
