package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.KeyedObject;
import com.sia.client.model.MqMessageProcessor;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import static com.sia.client.config.Utils.debug;

public class TableColumnHeaderManager<V extends KeyedObject> implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final ColumnCustomizableTable<V> mainTable;
    private boolean isAdjustingColumn = false;
    private int horizontalScrollBarAdjustmentValue=Integer.MIN_VALUE;
    private final PropertyChangeListener rowHeightConfigListener;
    private final ColumnHeaderDrawer<V> columnHeaderDrawer;
    private final Map<ColumnAdjuster, ResizingProperties> tableResizingProp = new HashMap<>();
    //flag to turn on/off auto adjusting feature on column width narrowing. if it is true, when user narrow a column, the column is auto adjusted to the width that just fit data.
    //otherwise, column width won't auto adjusted unless the column with does not fit data. --05/25/2021
    private static boolean autoAdjustOnNarrowing = false;

    public TableColumnHeaderManager(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
        columnHeaderDrawer = new ColumnHeaderDrawer<>();
        rowHeightConfigListener = (e)-> reconfigHeaderRow();
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.addComponentListener(this);
        mainTable.getRowHeaderTable().addComponentListener(this);
        mainTable.getRowHeaderTable().getColumnModel().addColumnModelListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
        mainTable.addTableChangedListener(this);
        mainTable.addTableChangedListener(MqMessageProcessor.getInstance());
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);
        mainTable.getTableScrollPane().getVerticalScrollBar().addAdjustmentListener(this);
        //after sorting, rowModel is set to null, need to re-configure row height
        mainTable.addPropertyChangeListener("rowSorter", rowHeightConfigListener);
        //detect column dragging
        mainTable.getTableHeader().addMouseListener( new MouseAdapter() {
            public void mouseReleased(MouseEvent arg0) {
                adjustColumnOnColumnDraging(mainTable,arg0.getPoint(),false);

        }});
        mainTable.getRowHeaderTable().getTableHeader().addMouseListener( new MouseAdapter() {
            public void mouseReleased(MouseEvent arg0) {
                //set adjustOnWidening to adjust row header width -- 06/25/2021
                adjustColumnOnColumnDraging(mainTable.getRowHeaderTable(),arg0.getPoint(),true);

        }});
    }
    public ColumnHeaderDrawer<V> getColumnHeaderDrawer() {
        return columnHeaderDrawer;
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && mainTable.isShowing()) {
                reconfigHeaderRow();
                debug("TableColumnHeaderManager::hierarchyChanged --Why need configHeaderRow here.");
                adjustComumns();
            }
        }
    }
    private void adjustComumns() {
        Utils.ensureEdtThread();
        if ( ! isAdjustingColumn ) {
            mainTable.adjustColumns();
            isAdjustingColumn = false;
        }
    }
    private void invokeDrawColumnHeaders() {
        mainTable.repaint();
    }
    private void reconfigHeaderRow() {
       mainTable.reconfigHeaderRow();
    }
    private void reconfigHeaderRow(int firstRow, int lastRow) {
        mainTable.reconfigHeaderRow(firstRow,lastRow,false);
    }
    public Component drawColumnHeaderOnViewIndex(ColumnCustomizableTable<V> table,int rowViewIndex, String headerValue) {
        return columnHeaderDrawer.drawOnViewIndex(table,rowViewIndex,headerValue,horizontalScrollBarAdjustmentValue);
    }

    @Override
    public void columnAdded(final TableColumnModelEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void columnRemoved(final TableColumnModelEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void columnMoved(final TableColumnModelEvent e) {
        if ( e.getFromIndex() != e.getToIndex()) {
            invokeDrawColumnHeaders();
            TableUtils.saveTableColumnPreference(mainTable);
        }
    }

    @Override
    public void columnMarginChanged(final ChangeEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }
    @Override
    public void componentResized(final ComponentEvent e) {
        //don't call adjustColumns() in componentResized(). because adjustColumns() will invoke componentResized in return. 05/27/2021
        Object source = e.getSource();
        if ( source instanceof ColumnAdjuster) {
            ColumnAdjuster columnAdjuster = (ColumnAdjuster)source;
            ResizingProperties resizingProperties = tableResizingProp.computeIfAbsent(columnAdjuster,(k)->new ResizingProperties());
            resizingProperties.widthBeforeResized = resizingProperties.width;
            resizingProperties.width = columnAdjuster.table().getWidth();
        }
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void componentShown(final ComponentEvent e) {
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        if (TableUtils.toRebuildCache(e) ) {
            mainTable.getColumnAdjusterManager().clear();
            if (mainTable.isShowing()) {
                adjustComumns();
                reconfigHeaderRow();
                invokeDrawColumnHeaders();
            }
        }
    }
    private int horizontalScrollBarAdjustmentValue_old = -10000;
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        if ( mainTable.isShowing() ) {
            if ( ! evt.getValueIsAdjusting() && Integer.MIN_VALUE != horizontalScrollBarAdjustmentValue) {
                adjustComumns();
            }

            if ( evt.getSource() == mainTable.getTableScrollPane().getHorizontalScrollBar()) {
                horizontalScrollBarAdjustmentValue = evt.getValue();
                if ( topWindowResized()) {
                    reconfigHeaderRow();
                } else {
                    if ( 2 <  Math.abs(horizontalScrollBarAdjustmentValue-horizontalScrollBarAdjustmentValue_old)) {
                        drawHeaderinVisibleRegion();
                    }
                }
                horizontalScrollBarAdjustmentValue_old = horizontalScrollBarAdjustmentValue;
            }

        }
    }
    private int oldWindowWidth=-1;
    private boolean topWindowResized() {
        boolean resized = false;
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainTable);
        if ( oldWindowWidth > 0) {
           resized = oldWindowWidth != topFrame.getWidth();
        }
        oldWindowWidth = topFrame.getWidth();
        return resized;
    }
    private void drawHeaderinVisibleRegion() {
        Rectangle visibleRect = mainTable.getVisibleRect();
        int x = (int) visibleRect.getX();
        int width = (int) visibleRect.getWidth();
        int y = (int) visibleRect.getY();
        int height = (int) visibleRect.getHeight();

        Point p0 = new Point(x, y);
        Point p1 = new Point(x + width, y + height);
        int firstRow = mainTable.rowAtPoint(p0);
        int lastRow = mainTable.rowAtPoint(p1);

        if ( 0 > lastRow ) {
            lastRow = mainTable.getRowCount()-1;
        }
        if ( 0 > firstRow) {
            firstRow = 0;
        }
//        reconfigHeaderRow(firstRow,lastRow);
        for(int rowViewIndex=firstRow;rowViewIndex<=lastRow;rowViewIndex++) {
            int rowModelIndex=mainTable.convertRowIndexToModel(rowViewIndex);
            String headerValue = mainTable.getModel().getGameGroupHeader(rowModelIndex);
            drawColumnHeaderOnViewIndex(mainTable, rowViewIndex, headerValue);
        }
    }
    private void adjustColumnOnColumnDraging(ColumnAdjuster columnAdjuster,Point mouseLocation,boolean adjustOnWidening){
        JTable table = columnAdjuster.table();
        ResizingProperties resizingProperties = tableResizingProp.get(columnAdjuster);
        int column = table.columnAtPoint(mouseLocation);
        if ( column < 0) {
            return;
        }
        if (  null != resizingProperties && resizingProperties.widthBeforeResized > resizingProperties.width) {
            //narrow width
            TableColumn tc = table.getColumnModel().getColumn(column);

            if ( autoAdjustOnNarrowing) {
                tc.setPreferredWidth(0);
            }
            columnAdjuster.adjustColumn(column);
            //sometimes users drag on column but mainTable.columnAtPoint(mouseLocation) returns right next column, to be safe, adjust both of them.
            column --;
            if ( 0 <= column) {
                TableColumn tc2 = table.getColumnModel().getColumn(column);
                if ( autoAdjustOnNarrowing) {
                    tc2.setPreferredWidth(0);
                }
                columnAdjuster.adjustColumn(column);
            }

        } else if ( adjustOnWidening) {
            columnAdjuster.adjustColumn(column);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class ResizingProperties {
        private int widthBeforeResized;
        private int width;
    }
}
