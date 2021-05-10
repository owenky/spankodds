package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sun.javafx.collections.ImmutableObservableList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColumnLockableTable extends JTable {

    private static final String ColumnHeaderCellIden = SiaConst.GameGroupHeaderIden;
    private final List<TableColumn> allColumns = new ArrayList<>();
    private List<Integer> lockedColumnIndex = new ArrayList<>();
    private RowHeaderTable rowHeaderTable;
    private final boolean hasRowNumber;
    private final Color headerBackground;
    private JScrollPane tableScrollPane;
    private ColumnHeaderCellRenderer headerCellRenderer;

    public ColumnLockableTable(boolean hasRowNumber) {
        this.hasRowNumber = hasRowNumber;
        this.setAutoCreateColumnsFromModel(true);
        this.headerBackground = SiaConst.DefaultHeaderColor;
    }
    @Override
    public final TableCellRenderer getCellRenderer(int row, int column) {
        if ( null == headerCellRenderer) {
            headerCellRenderer = new ColumnHeaderCellRenderer(this::getUserCellRenderer,headerBackground,ColumnHeaderCellIden);
        }
        return headerCellRenderer;
    }
    protected TableCellRenderer getUserCellRenderer(int row, int column) {
        return super.getCellRenderer(row, column);
    }
    public JScrollPane getTableScrollPane() {
        if ( null == tableScrollPane) {
            tableScrollPane = new JScrollPane(this);
        }
        return tableScrollPane;
    }
    public boolean hasRowNumber() {
        return hasRowNumber;
    }
    public final RowHeaderTable getRowHeaderTable() {
        if ( null == rowHeaderTable) {
            rowHeaderTable = createNewRowHeaderTable();
        }
        return rowHeaderTable;
    }
    protected RowHeaderTable createNewRowHeaderTable() {
        return new RowHeaderTable(this,hasRowNumber);
    }
    public void removeLockedColumnIndex(int lastIndex) {
        Integer[] arr = new Integer[lastIndex+1];
        for (int i = 0; i <= lastIndex; i++) {
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
