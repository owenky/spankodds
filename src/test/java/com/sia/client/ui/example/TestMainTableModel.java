package com.sia.client.ui.example;

import javax.swing.table.AbstractTableModel;

public class TestMainTableModel extends AbstractTableModel {

    String data[] = createDataPrefix();
    String headers[] = createColumnTitles();

    public int getRowCount() {
        return RowHeaderTableTest.rowCount;
    }

    public int getColumnCount() {
        return data.length;
    }

    // Synthesize some entries using the data values and the row number.
    public Object getValueAt(int row, int col) {
        return data[col] + row;
    }

    public String getColumnName(int col) {
        return headers[col];
    }

    private static String[] createDataPrefix() {
        String[] dataPrefix = new String[RowHeaderTableTest.colCount];
        for (int i = 1; i < RowHeaderTableTest.colCount; i++) {
            dataPrefix[i] = "col" + i;
        }
        dataPrefix[0] = "";
        return dataPrefix;
    }

    private static String[] createColumnTitles() {
        String[] columns = new String[RowHeaderTableTest.colCount];
        for (int i = 1; i < RowHeaderTableTest.colCount; i++) {
            columns[i] = "Column " + i;
        }
        columns[0] = "Row #";
        return columns;
    }
}
