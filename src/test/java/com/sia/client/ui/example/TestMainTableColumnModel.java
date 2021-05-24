package com.sia.client.ui.example;

// Create a column model for the main table. This model ignores the first
// column added and sets a minimum width of 150 pixels for all others.

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class TestMainTableColumnModel extends DefaultTableColumnModel {
    boolean first = true;

    public void addColumn(TableColumn tc) {
        // Drop the first column, which will be the row header.
        if (first) {
            first = false;
            return;
        }
        tc.setMinWidth(150);  // Just for looks, really...
        super.addColumn(tc);
    }

}
