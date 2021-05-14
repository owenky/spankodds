package com.sia.client.ui;

import javax.swing.table.DefaultTableColumnModel;

public class CustomizableTableColumnModel extends DefaultTableColumnModel {

    private int userDefinedColumnMargin;

    @Override
    public void setColumnMargin(int columnMargin) {
        //column margin should remain 0 for game group header drawing.
        this.userDefinedColumnMargin = columnMargin;
    }
    public int getUserDefinedColumnMargin() {
        return userDefinedColumnMargin;
    }
}
