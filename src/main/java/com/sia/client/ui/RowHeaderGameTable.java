package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.ui.comps.NodeCellEditor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;

public class RowHeaderGameTable extends RowHeaderTable<Game>{

    private final TableCellEditor tableCellEditor = new NodeCellEditor();
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
    // force ctrl key down when mouse click a row by setting toggle=true
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex,columnIndex,true,extend);
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return TableUtils.isCellEditable(this,row,col);
    }
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return tableCellEditor;
    }
    public int getWindowIndex() {
        return ((MainGameTable)getMainTable()).getWindowIndex();
    }
}
