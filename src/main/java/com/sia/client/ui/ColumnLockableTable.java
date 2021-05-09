package com.sia.client.ui;

import com.sun.javafx.collections.ImmutableObservableList;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.List;

public class ColumnLockableTable extends JTable {

    private final List<TableColumn> allColumns = new ArrayList<>();
    private List<Integer> lockedColumnIndex = new ArrayList<>();

    public ColumnLockableTable() {
        this.setAutoCreateColumnsFromModel(true);
    }

    public void removeLockedColumnIndex(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        removeLockedColumnIndex(arr);
    }
    public TableColumn getColumnFromModel(int colModelIndex) {
        return allColumns.get(colModelIndex);
    }
    public void removeLockedColumnIndex(Integer... lockedColumnIndexArr) {

        lockedColumnIndex = new ImmutableObservableList<>(lockedColumnIndexArr);
    }

    public List<Integer> getLockedColumns() {
        return lockedColumnIndex;
    }

    public void createUnlockedColumns() {

        // Remove any current columns
        TableColumnModel cm = getColumnModel();
        while (cm.getColumnCount() > 0) {
            cm.removeColumn(cm.getColumn(0));
        }

        for (TableColumn tc : allColumns) {
            if (null != lockedColumnIndex && !lockedColumnIndex.contains(tc.getModelIndex())) {
                cm.addColumn(tc);
            }
        }


    }

    @Override
    public void addColumn(TableColumn tc) {
        super.addColumn(tc);
        tc.setModelIndex(allColumns.size());
        //placed after super
        allColumns.add(tc);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
//        if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW && null != allColumns) {
//            allColumns.clear();
//            TableColumnModel tcm = this.getColumnModel();
//            if (0 == tcm.getColumnCount()) {
//                createDefaultColumnsFromModel();
//                tcm = this.getColumnModel();
//            }
//            for (int i = 0; i < tcm.getColumnCount(); i++) {
//                TableColumn tc = tcm.getColumn(i);
//                tc.setModelIndex(allColumns.size());
//                allColumns.add(tc);
//            }
//
//        }
    }


    @Override
    public void createDefaultColumnsFromModel() {
        //don't override this method.
        super.createDefaultColumnsFromModel();
    }
}
