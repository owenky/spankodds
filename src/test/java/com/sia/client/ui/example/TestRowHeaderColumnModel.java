package com.sia.client.ui.example;

// Create a column model that will serve as our row header table. This model
// picks a maximum width and stores only the first column.

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class TestRowHeaderColumnModel extends DefaultTableColumnModel {
    boolean first = true;

    public void addColumn(TableColumn tc) {
        if (first) {
            tc.setMaxWidth(tc.getPreferredWidth());
            super.addColumn(tc);
            first = false;
        }
        // Drop the rest of the columns; this is the header column only.
    }

}
