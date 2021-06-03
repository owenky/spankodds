package com.sia.client.ui;

import javax.swing.JTable;

public interface ColumnAdjuster {
    void adjustColumn(int columnIndex);
    JTable table();
}
