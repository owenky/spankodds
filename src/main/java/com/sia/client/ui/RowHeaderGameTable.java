package com.sia.client.ui;

import com.sia.client.model.Game;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
        ListSelectionModel listSelectionModel = mainTable.getSelectionModel();
        setSelectionModel(listSelectionModel);
        this.addMouseListener(GameTableMouseListener.instance());
        listSelectionModel.addListSelectionListener(GameTableMouseListener.instance());
    }
    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        ShortCut.disableShortCut();
        return super.prepareEditor(editor,row,column);
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        TableUtils.updateRowSelection(getMainTable(),e);
    }
    // force ctrl key down when mouse click a row by setting toggle=true
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if ( ! this.isCellEditable(rowIndex,columnIndex)) {
            super.changeSelection(rowIndex, columnIndex, true, extend);
        }
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return TableUtils.isCellEditable(this,row,col);
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
