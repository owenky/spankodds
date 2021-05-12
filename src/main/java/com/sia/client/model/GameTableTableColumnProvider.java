package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.MainGameTable;

public class GameTableTableColumnProvider implements ColumnHeaderProvider {

    private MainGameTable mainGameTable;
    private ColumnHeaderProperty columnHeaderProperty;

    public GameTableTableColumnProvider() {
    }
    public void setMainGameTable(MainGameTable mainGameTable) {
        this.mainGameTable = mainGameTable;
    }
    @Override
    public ColumnHeaderProperty get() {
        if ( null == columnHeaderProperty) {
            columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor,SiaConst.GameGroupHeaderHeight,
                    mainGameTable.getColumnHeaderRowViewIndex());
        }
        return columnHeaderProperty;
    }
}
