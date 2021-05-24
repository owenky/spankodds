package com.sia.client.ui.example;

import com.sia.client.ui.ColumnHeaderCellRenderer;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TestMainTable extends JTable {

    private TableCellRenderer mainTableCellRenderer;
    public TestMainTable(TableModel tm, TableColumnModel cm) {
        super(tm,cm);
    }
    @Override
    public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
        if ( null==mainTableCellRenderer) {
            mainTableCellRenderer = new TestMainTableCellRenderer();
        }
        return mainTableCellRenderer;
    }
}
