package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        ListSelectionModel listSelectionModel = mainTable.getSelectionModel();
        setSelectionModel(listSelectionModel);
        this.addMouseListener(GameTableMouseListener.instance());
        listSelectionModel.addListSelectionListener(GameTableMouseListener.instance());
    }
    public boolean isNoteColumn(int colViewIndex) {
        TableColumn tc = getColumnModel().getColumn(colViewIndex);
        return SiaConst.NodeColumnHeader.equals(tc.getHeaderValue());
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        TableUtils.updateRowSelection(getMainTable(),e);
    }
    // force ctrl key down when mouse click a row by setting toggle=true
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex,columnIndex,true,extend);
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
