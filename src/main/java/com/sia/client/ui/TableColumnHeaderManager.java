package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.KeyedObject;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeListener;

public class TableColumnHeaderManager<V extends KeyedObject> implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final ColumnCustomizableTable<V> mainTable;
    private boolean isAdjustingColumn = false;
    private int horizontalScrollBarAdjustmentValue=Integer.MIN_VALUE;
    private final PropertyChangeListener rowHeightConfigListener;
    private final ColumnHeaderDrawer<V> columnHeaderDrawer;

    public TableColumnHeaderManager(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
        columnHeaderDrawer = new ColumnHeaderDrawer<>(mainTable);
        rowHeightConfigListener = (e)-> mainTable.configHeaderRow();
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.addComponentListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
        mainTable.setTableChangedListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);
        mainTable.getTableScrollPane().getVerticalScrollBar().addAdjustmentListener(this);
        //after sorting, rowModel is set to null, need to re-configure row height
        mainTable.addPropertyChangeListener("rowSorter", rowHeightConfigListener);
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && mainTable.isShowing()) {
                Utils.checkAndRunInEDT(() -> {
                    configHeaderRow();
                    adjustComumns();
                });
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
    private void configHeaderRow() {
       mainTable.configHeaderRow();
    }
    public void drawColumnHeaderOnViewIndex(int rowViewIndex, Object headerValue) {
        columnHeaderDrawer.drawOnViewIndex(rowViewIndex,headerValue,horizontalScrollBarAdjustmentValue);
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
        invokeDrawColumnHeaders();
    }

    @Override
    public void columnMarginChanged(final ChangeEvent e) {
//        if ( isMainTableShown.get() ) {
          if ( mainTable.isShowing()) {
            invokeDrawColumnHeaders();
        }
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
//        if ( mainTable.isShowing() && ! isAdjustingColumn ) {
//            drawnHeaderValues.clear();
//            adjustComumns();
//        }
//        isAdjustingColumn = false;
        //don't call adjustColumns() in componentResized(). because adjustColumns() will invoke componentResized in return. 05/27/2021
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void componentShown(final ComponentEvent e) {
//        invokeDrawColumnHeaders();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
//        invokeDrawColumnHeaders();
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        if (TableUtils.toRebuildCache(e) ) {
            mainTable.getColumnAdjusterManager().clear();
            if (mainTable.isShowing()) {
                adjustComumns();
                configHeaderRow();
                invokeDrawColumnHeaders();
            }
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        if ( mainTable.isShowing() ) {
            if ( ! evt.getValueIsAdjusting() && Integer.MIN_VALUE != horizontalScrollBarAdjustmentValue) {
                adjustComumns();
            }

            if ( evt.getSource() == mainTable.getTableScrollPane().getHorizontalScrollBar()) {
                horizontalScrollBarAdjustmentValue = evt.getValue();
                drawHeaderinVisibleRegion();
            }

        }
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
        mainTable.configHeaderRow(firstRow,lastRow,false);
    }
}
