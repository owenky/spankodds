package com.sia.client.ui;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;
import com.sia.client.model.MarginProvider;
import com.sia.client.model.TableCellRendererProvider;
import com.sun.javafx.collections.ImmutableObservableList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ColumnCustomizableTable extends JTable implements ColumnHeaderDrawer {

    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final int instanceIndex;
    private final ColumnHeaderProvider columnHeaderProvider;
    private final List<TableColumn> allColumns = new ArrayList<>();
    private List<Integer> lockedColumnIndex = new ArrayList<>();
    private RowHeaderTable rowHeaderTable;
    private final boolean hasRowNumber;
    private JScrollPane tableScrollPane;
    private ColumnAdjusterManager columnAdjusterManager;
    private ColumnHeaderCellRenderer headerCellRenderer;
    private TableColumnHeaderManager tableColumnHeaderManager;
    private int userDefinedRowMargin;
    private MarginProvider marginProvider;
    private boolean needToCreateColumnModel = true;
    private TableModelListener tableChangedListener;

    abstract public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colDataModelIndex);
    @Override
    abstract public ColumnCustomizableDataModel createDefaultDataModel();
    public ColumnCustomizableTable(boolean hasRowNumber, ColumnHeaderProvider columnHeaderProvider) {
        this.columnHeaderProvider = columnHeaderProvider;
        this.hasRowNumber = hasRowNumber;
        this.setAutoCreateColumnsFromModel(true);
        instanceIndex = instanceCounter.addAndGet(1);
        setName(ColumnCustomizableTable.class.getSimpleName()+":"+instanceIndex);
    }
    public MarginProvider getMarginProvider() {
        if ( null == marginProvider) {
            marginProvider = ()-> new Dimension(getUserDefinedColumnMargin(),getUserDefinedRowMargin());
        }
        return marginProvider;
    }
    public ColumnHeaderProvider getColumnHeaderProvider() {
        return columnHeaderProvider;
    }
    public TableColumnHeaderManager getTableColumnHeaderManager() {
        if ( null == tableColumnHeaderManager) {
            tableColumnHeaderManager = new TableColumnHeaderManager(this);
        }
        return tableColumnHeaderManager;
    }
    public void adjustColumns(boolean includeHeaders) {
        getColumnAdjusterManager().adjustColumns(includeHeaders);
    }
    public void adjustColumnsOnRows(Integer ... gameIds) {
        getColumnAdjusterManager().adjustColumnsOnRows(gameIds);
    }
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        getRowHeaderTable().setRowHeight(rowHeight);
    }
    @Override
    public void setRowHeight(int rowViewIndex,int rowHeight) {
        super.setRowHeight(rowViewIndex,rowHeight);
        getRowHeaderTable().setRowHeight(rowViewIndex,rowHeight);
    }
    @Override
    public void setRowMargin(int rowMargin) {
        //table row margin should remain 0 for game group header drawing.
        this.userDefinedRowMargin = rowMargin;
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
    public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
        if ( null == headerCellRenderer) {
            TableCellRendererProvider tableCellRendererProvider = (row,col)-> {
                int colDataModelIndex = ColumnCustomizableTable.this.convertColumnIndexToModel(col) + lockedColumnIndex.size();
                return getUserCellRenderer(row,colDataModelIndex);
            };
            headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, columnHeaderProvider,getMarginProvider());
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
//            rowHeaderTable.createDefaultColumnModel();
        }
        return rowHeaderTable;
    }
    protected RowHeaderTable createNewRowHeaderTable() {
        return new RowHeaderTable(this,hasRowNumber);
    }
    public int getUserDefinedRowMargin() {
        return userDefinedRowMargin;
    }
    public int getUserDefinedColumnMargin() {
        return this.getColumnModel().getUserDefinedColumnMargin();
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
    public void setTableChangedListener(TableModelListener tableChangedListener) {
        this.tableChangedListener = tableChangedListener;
    }
    public TableColumn getColumnFromDataModel(int colModelIndex) {
        return allColumns.get(colModelIndex);
    }
    public void removeLockedColumnIndex(Integer... lockedColumnIndexArr) {

        lockedColumnIndex = new ImmutableObservableList<>(lockedColumnIndexArr);
        needToCreateColumnModel = true;
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
        needToCreateColumnModel = false;

    }

    @Override
    public void addColumn(TableColumn tc) {
        super.addColumn(tc);
        tc.setModelIndex(allColumns.size());
        //placed after super
        allColumns.add(tc);
        needToCreateColumnModel = true;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        if ( null != tableChangedListener) {
            tableChangedListener.tableChanged(e);
        }
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
    public CustomizableTableColumnModel getColumnModel() {
        return (CustomizableTableColumnModel)super.getColumnModel();
    }
    @Override
    public CustomizableTableColumnModel createDefaultColumnModel() {
        return new CustomizableTableColumnModel();
    }
    @Override
    public ColumnCustomizableDataModel getModel() {
        return (ColumnCustomizableDataModel)super.getModel();
    }
    @Override
    public final void createDefaultColumnsFromModel() {
        //copied from super.createDefaultColumnsFromModel();
        if ( ! needToCreateColumnModel) {
            return;
        }

        TableModel m = getModel();
        if (m != null) {
            // Remove any current columns
            TableColumnModel cm = getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
            }

//            // Create new columns from the data model info
//            for (int i = 0; i < m.getColumnCount(); i++) {
//                TableColumn newColumn = new TableColumn(i) ;
//                addColumn(newColumn);
//            }
            createUnlockedColumns();
        }
    }
    @Override
    public boolean isColumnHeaderDrawn(Object columnHeaderValue) {
        return getTableColumnHeaderManager().isColumnHeaderDrawn(columnHeaderValue);
    }
    @Override
    public void drawColumnHeaderOnViewIndex(ColumnHeaderProperty columnHeaderProperty, int rowViewIndex, Object columnHeaderValue){
        getTableColumnHeaderManager().drawColumnHeaderOnViewIndex(columnHeaderProperty,rowViewIndex,columnHeaderValue);
    }
    private ColumnAdjusterManager getColumnAdjusterManager() {
        if ( null == columnAdjusterManager) {
            columnAdjusterManager = new ColumnAdjusterManager(this);
            columnAdjusterManager.setColumnHeaderIncluded(true);
        }
        return columnAdjusterManager;
    }
}
