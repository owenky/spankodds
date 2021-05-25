package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.KeyedObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TableColumnHeaderManager<V extends KeyedObject> implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable<V> mainTable;
//    private final AtomicBoolean isMainTableShown = new AtomicBoolean(false);
    private boolean isAdjustingColumn = false;
    private final Set<Object> drawnHeaderValues = new HashSet<>();
    private int horizontalScrollBarAdjustmentValue=Integer.MIN_VALUE;
    private final PropertyChangeListener rowHeightConfigListener;

    public TableColumnHeaderManager(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
        rowHeightConfigListener = (e)-> mainTable.configRowHeight();
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.addComponentListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
//        mainTable.getModel().addTableModelListener(this);
        mainTable.setTableChangedListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);
        mainTable.getTableScrollPane().getVerticalScrollBar().addAdjustmentListener(this);
        //after sorting, rowModel is set to null, need to re-configure row height
        mainTable.addPropertyChangeListener("rowSorter", rowHeightConfigListener);
    }
    public boolean isColumnHeaderDrawn(Object columnHeaderValue) {
        return drawnHeaderValues.contains(columnHeaderValue);
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
//            if (source == mainTable && !isMainTableShown.get() && mainTable.isShowing()) {
            if (source == mainTable && mainTable.isShowing()) {
                Utils.checkAndRunInEDT(() -> {
                    configRowHeight();
                    adjustComumns();
                    invokeDrawColumnHeaders();
//                    isMainTableShown.set(true);
                });
            }
        }
    }
    private void adjustComumns() {
        Utils.ensureEdtThread();
        if ( ! isAdjustingColumn ) {
            mainTable.adjustColumns(true);
            isAdjustingColumn = false;
        }
    }
    private void invokeDrawColumnHeaders() {
        drawnHeaderValues.clear();
        mainTable.repaint();
    }
    private void configRowHeight() {
       mainTable.configRowHeight();
    }
    public void drawColumnHeaderOnViewIndex(int rowViewIndex, Object headerValue) {
        ColumnHeaderProvider<V> columnHeaderProvider = mainTable.getModel().getColumnHeaderProvider();
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(String.valueOf(headerValue), header -> makeColumnHeaderComp(mainTable, header,columnHeaderProvider.getHeaderForeground()
                , columnHeaderProvider.getHeaderFont()));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, columnHeaderProvider.getColumnHeaderHeight(), horizontalScrollBarAdjustmentValue);
        drawnHeaderValues.add(headerValue);
    }
    private static <V extends KeyedObject> JComponent makeColumnHeaderComp(ColumnCustomizableTable<V> jtable, String gameGroupHeader, Color headerForeGround, Font titleFont) {
        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        BorderLayout bl = new BorderLayout();
        jPanel.setLayout(bl);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(headerForeGround);
        jPanel.add(BorderLayout.CENTER, titleLabel);
        jtable.add(jPanel);

        return jPanel;
    }

    private static <V extends KeyedObject> void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable<V> mainTable, JComponent header, int headerHeight, int diffByScroll) {
        Rectangle r1 = mainTable.getCellRect(rowViewIndex-1, 0, true);
        int x1 = 0;
        int y1 = (int) (r1.getY() + r1.getHeight());
        int width = (int)header.getPreferredSize().getWidth();
        mainTable.setRowHeight(rowViewIndex, headerHeight);
        header.setBounds(x1+diffByScroll, y1, width, headerHeight);

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
//        if ( isMainTableShown.get() && ! isAdjustingColumn ) {
        if ( mainTable.isShowing() && ! isAdjustingColumn ) {
            drawnHeaderValues.clear();
            adjustComumns();
        }
        isAdjustingColumn = false;
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
            if (mainTable.isShowing()) {
                adjustComumns();
                configRowHeight();
                invokeDrawColumnHeaders();
            }
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
//        if ( isMainTableShown.get() ) {
        if ( mainTable.isShowing() ) {
            if ( ! evt.getValueIsAdjusting() && Integer.MIN_VALUE != horizontalScrollBarAdjustmentValue) {
                adjustComumns();
            }

            if ( evt.getSource() == mainTable.getTableScrollPane().getHorizontalScrollBar()) {
                horizontalScrollBarAdjustmentValue = evt.getValue();
                invokeDrawColumnHeaders();
            }

        }
    }
}
