package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.MainGameTable;

public class GameTableTableColumnProvider implements ColumnHeaderProvider {

    private MainGameTable mainGameTable;

    public GameTableTableColumnProvider() {
    }
    public void setMainGameTable(MainGameTable mainGameTable) {
        this.mainGameTable = mainGameTable;
    }
    @Override
    public ColumnHeaderProperty get() {
        ColumnHeaderProperty columnHeaderProperty;
        columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor,SiaConst.DefaultHeaderFontColor, SiaConst.DefaultHeaderFont, SiaConst.GameGroupHeaderHeight,
                mainGameTable.getRowModelIndex2GameGroupHeaderMap());
        return columnHeaderProperty;
    }
}
