package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.TableCellRendererProvider;
import com.sun.javafx.collections.ImmutableObservableList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public abstract class ColumnCustomizableTable extends JTable {

    private final ColumnHeaderProvider columnHeaderProvider;
    private final List<TableColumn> allColumns = new ArrayList<>();
    private List<Integer> lockedColumnIndex = new ArrayList<>();
    private RowHeaderTable rowHeaderTable;
    private final boolean hasRowNumber;
    private JScrollPane tableScrollPane;
    private ColumnHeaderCellRenderer headerCellRenderer;

    abstract public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colDataModelIndex);
    public ColumnCustomizableTable(boolean hasRowNumber, ColumnHeaderProvider columnHeaderProvider) {
        this.columnHeaderProvider = columnHeaderProvider;
        this.hasRowNumber = hasRowNumber;
        this.setAutoCreateColumnsFromModel(true);
    }
    public ColumnHeaderProvider getColumnHeaderProvider() {
        return columnHeaderProvider;
    }
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        getRowHeaderTable().setRowHeight(rowHeight);
    }
    @Override
    public void setIntercellSpacing(Dimension intercellSpacing) {
        super.setIntercellSpacing(intercellSpacing);
        getRowHeaderTable().setIntercellSpacing(intercellSpacing);
    }
    @Override
    public void setRowHeight(int row,int rowHeight) {
        super.setRowHeight(row,rowHeight);
        getRowHeaderTable().setRowHeight(row,rowHeight);
    }
    @Override
    public void setShowHorizontalLines(boolean showHorizontalLines) {
        super.setShowHorizontalLines(showHorizontalLines);
        getRowHeaderTable().setShowHorizontalLines(showHorizontalLines);
    }
    @Override
    public void setGridColor(Color gridColor) {
        super.setGridColor(gridColor);
        getRowHeaderTable().setGridColor(gridColor);
    }
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        getRowHeaderTable().setFont(font);
    }
    @Override
    public void setRowMargin(int rowMargin) {
        super.setRowMargin(rowMargin);
        getRowHeaderTable().setRowMargin(rowMargin);
    }
    @Override
    public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
        if ( null == headerCellRenderer) {
            TableCellRendererProvider tableCellRendererProvider = (row,col)-> {
                int colDataModelIndex = ColumnCustomizableTable.this.convertColumnIndexToModel(col) + lockedColumnIndex.size();
                return getUserCellRenderer(row,colDataModelIndex);
            };
            headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, columnHeaderProvider);
        }
        return headerCellRenderer;
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
            rowHeaderTable.createDefaultColumnModel();
        }
        return rowHeaderTable;
    }
    protected RowHeaderTable createNewRowHeaderTable() {
        return new RowHeaderTable(this,hasRowNumber);
    }

    /**
     * boundaryIndex is not included in locked column
     * @param boundaryIndex
     */
    public void removeLockedColumnIndex(int boundaryIndex) {
        Integer[] arr = new Integer[boundaryIndex];
        for (int i = 0; i < boundaryIndex; i++) {
            arr[i] = i;
        }
        removeLockedColumnIndex(arr);
    }
    public TableColumn getColumnFromDataModel(int colModelIndex) {
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
    public final void createDefaultColumnsFromModel() {
        //don't override this method.
        super.createDefaultColumnsFromModel();
    }
}
