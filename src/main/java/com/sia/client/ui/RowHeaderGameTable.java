package com.sia.client.ui;

import com.sia.client.model.Game;

import javax.swing.*;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        ListSelectionModel listSelectionModel = mainTable.getSelectionModel();
        setSelectionModel(listSelectionModel);
        this.addMouseListener(GameTableMouseListener.instance());
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
