package com.sia.client.ui.example;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TestRHTable extends JTable {

    private TestHeaderTableCellRenderer testHeaderTableCellRenderer;
    public TestRHTable(TableModel tm, TableColumnModel cm) {
        super(tm,cm);
    }
    @Override
    public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
        if ( null == testHeaderTableCellRenderer) {
            testHeaderTableCellRenderer = new TestHeaderTableCellRenderer();
        }
        return testHeaderTableCellRenderer;
    }
}
