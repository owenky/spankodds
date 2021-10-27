package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnCustomizableDataModel.LtdSrhStruct;
import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.KeyedObject;
import com.sia.client.model.MarginProvider;
import com.sia.client.model.TableCellRendererProvider;
import com.sia.client.model.TableSection;
import com.sun.javafx.collections.ImmutableObservableList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ColumnCustomizableTable<V extends KeyedObject> extends JTable implements ColumnAdjuster {

    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final int instanceIndex;
    private List<Integer> lockedColumnIndex = new ArrayList<>();
    private RowHeaderTable<V> rowHeaderTable;
    private final boolean hasRowNumber;
    private JScrollPane tableScrollPane;
    private ColumnAdjusterManager columnAdjusterManager;
    private ColumnHeaderCellRenderer headerCellRenderer;
    private TableColumnHeaderManager<V> tableColumnHeaderManager;
    private int userDefinedRowMargin;
    private MarginProvider marginProvider;
    private boolean needToCreateColumnModel = true;
    private TableModelListener tableChangedListener;
    private Map<Integer, Component> oldHeaderMap = new HashMap<>();

    abstract public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colDataModelIndex);
    public ColumnCustomizableTable(boolean hasRowNumber, ColumnCustomizableDataModel<V> tm) {
        super(tm,new CustomizableTableColumnModel());
        this.hasRowNumber = hasRowNumber;
        this.setAutoCreateColumnsFromModel(true);
        instanceIndex = instanceCounter.addAndGet(1);
        setName(ColumnCustomizableTable.class.getSimpleName()+":"+instanceIndex);
        ToolTipManager.sharedInstance().registerComponent(this);
    }
    @Override
    public String getToolTipText(MouseEvent e) {
       return Utils.getTableCellToolTipText(this,e);
    }
    public MarginProvider getMarginProvider() {
        if ( null == marginProvider) {
            marginProvider = ()-> new Dimension(getUserDefinedColumnMargin(),getUserDefinedRowMargin());
        }
        return marginProvider;
    }
    public void configHeaderRow() {
        configHeaderRow(0,getRowCount()-1,true);
    }
    public void configHeaderRow(int firstRow,int lastRow,boolean toSetRowHeight) {

        ColumnCustomizableDataModel<V> model = getModel();
        ColumnHeaderProperty columnHeaderProperty = model.getColumnHeaderProperty();

        for(int rowViewIndex=firstRow;rowViewIndex<=lastRow;rowViewIndex++) {
            //remove group header if the row has it.
            Component oldHeaderComp = oldHeaderMap.get(rowViewIndex);
            if ( null != oldHeaderComp) {
                this.remove(oldHeaderComp);
            }
            int rowModelIndex = convertRowIndexToModel(rowViewIndex);
            int rowHeight;
            Object headerValue = model.getGameGroupHeader(rowModelIndex);
            if ( null == headerValue) {
//                rowHeight = getRowHeight();
                LtdSrhStruct<V> section = getModel().getLinesTableData(rowModelIndex);
                if ( null != section && null != section.linesTableData) {
                    rowHeight = section.linesTableData.getRowHeight();
                } else {
                    rowHeight = SiaConst.NormalRowheight;
                }
            } else {
                rowHeight = columnHeaderProperty.getColumnHeaderHeight();
                Component newGroupHeader = getTableColumnHeaderManager().drawColumnHeaderOnViewIndex(this,rowViewIndex,String.valueOf(headerValue));
                this.oldHeaderMap.put(rowViewIndex,newGroupHeader);
            }
            if ( toSetRowHeight) {
                setRowHeight(rowViewIndex, rowHeight);
            }
        }
    }
    public TableColumnHeaderManager<V> getTableColumnHeaderManager() {
        if ( null == tableColumnHeaderManager) {
            tableColumnHeaderManager = new TableColumnHeaderManager<>(this);
        }
        return tableColumnHeaderManager;
    }
    @Override
    public JTable table() {
        return this;
    }
    @Override
    public void adjustColumn(int column) {
        getColumnAdjusterManager().getMainTableColumnAdjuster().adjustColumn(column);
    }
    public void adjustRowHeaderColumn(int column) {
        getColumnAdjusterManager().getRowHeaderTableColumnAdjuster().adjustColumn(column);
    }
    public void adjustColumns() {
        getColumnAdjusterManager().adjustColumns();
    }
    public void adjustColumnsOnRows(Integer ... gameIds) {
        getColumnAdjusterManager().adjustColumnsOnRows(gameIds);
    }
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        getRowHeaderTable().setRowHeight(rowHeight);
        //jtable::setRowHeight() set rowModel to null, need to config row height after this call.
        configHeaderRow();
    }
    @Override
    public void setRowHeight(int rowViewIndex,int rowHeight) {
        getRowHeaderTable().setRowHeight(rowViewIndex,rowHeight);
        super.setRowHeight(rowViewIndex,rowHeight);
    }
    @Override
    public int getRowHeight() {
        return super.getRowHeight();
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
            headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, getModel().getColumnHeaderProperty());
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
    public final RowHeaderTable<V> getRowHeaderTable() {
        if ( null == rowHeaderTable) {
            rowHeaderTable = createNewRowHeaderTable();
//            rowHeaderTable.createDefaultColumnModel();
        }
        return rowHeaderTable;
    }
    protected RowHeaderTable<V> createNewRowHeaderTable() {
        return new RowHeaderTable<>(this,hasRowNumber);
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
        return getModel().getAllColumns().get(colModelIndex);
    }
    public void removeLockedColumnIndex(Integer... lockedColumnIndexArr) {

        lockedColumnIndex = new ImmutableObservableList<>(lockedColumnIndexArr);
        needToCreateColumnModel = true;
    }
    public void addGameLine(TableSection<V> gameLine) {
        getModel().addGameLine(gameLine);
    }
    public void addGameLine(int index,TableSection<V> gameLine) {
        getModel().addGameLine(index,gameLine);
    }
    public TableSection<V> getLinesTableData(int row) {
        return getModel().getLinesTableData(row).linesTableData;
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

        List<TableColumn> allColumns = getModel().getAllColumns();
        for (TableColumn tc : allColumns) {
            if (null != lockedColumnIndex && !lockedColumnIndex.contains(tc.getModelIndex())) {
                cm.addColumn(tc);
            }
        }
        needToCreateColumnModel = false;

    }

    @Override
    public void addColumn(TableColumn tc) {
//        super.addColumn(tc);
//        tc.setModelIndex(allColumns.size());
//        //placed after super
//        allColumns.add(tc);
//        needToCreateColumnModel = true;
        throw new IllegalArgumentException("This method not supported.");
    }
    @Override
    public Object getValueAt(int row,int col) {
        return super.getValueAt(row,col);
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        //to prevent this method called from constructor.super, need condition null != rowHeaderTable
        if ( isShowing()) {
            if ((e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW  ) && null != rowHeaderTable) {
                //super method discard row model, need to re-config row height
                configHeaderRow();
            }
            if (null != tableChangedListener) {
                tableChangedListener.tableChanged(e);
            }
        }
    }
    @Override
    public CustomizableTableColumnModel getColumnModel() {
        return (CustomizableTableColumnModel)super.getColumnModel();
    }
    @Override
    public ColumnCustomizableDataModel<V> getModel() {
        TableModel model = super.getModel();
        if ( ! (model instanceof ColumnCustomizableDataModel) ){
            throw new IllegalStateException("Expecting ColumnCustomizableDataModel");
        }
        return (ColumnCustomizableDataModel<V>)model;
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
            createUnlockedColumns();
        }
    }
    public ColumnAdjusterManager getColumnAdjusterManager() {
        if ( null == columnAdjusterManager) {
            columnAdjusterManager = new ColumnAdjusterManager(this);
        }
        return columnAdjusterManager;
    }
}
