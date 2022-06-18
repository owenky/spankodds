package com.sia.client.ui;

import com.sia.client.model.Game;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        ListSelectionModel listSelectionModel = mainTable.getSelectionModel();
        setSelectionModel(listSelectionModel);
        this.addMouseListener(GameTableMouseListener.instance());
        listSelectionModel.addListSelectionListener(GameTableMouseListener.instance());
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        TableUtils.updateRowSelection(getMainTable(),e);
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
