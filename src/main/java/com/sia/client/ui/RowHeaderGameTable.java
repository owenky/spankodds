package com.sia.client.ui;

import com.sia.client.model.Game;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        this.addMouseListener(new MainGameTable.MouseClickListener(getWindowIndex()));
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
