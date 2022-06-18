package com.sia.client.ui;

import com.sia.client.model.Game;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        setSelectionModel(mainTable.getSelectionModel());
        this.addMouseListener(new GameTableMouseListener(getWindowIndex()));
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
