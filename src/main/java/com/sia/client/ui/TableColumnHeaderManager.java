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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class TableColumnHeaderManager<V extends KeyedObject> implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable<V> mainTable;
    private final AtomicBoolean isMainTableFirstShown = new AtomicBoolean(false);
    private boolean isAdjustingColumn = false;
    private final Set<Object> drawnHeaderValues = new HashSet<>();
    private int horizontalScrollBarAdjustmentValue;

    public TableColumnHeaderManager(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
//        mainTable.getModel().addTableModelListener(this);
        mainTable.setTableChangedListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);
        mainTable.getTableScrollPane().getVerticalScrollBar().addAdjustmentListener(this);
    }
    public boolean isColumnHeaderDrawn(Object columnHeaderValue) {
        return drawnHeaderValues.contains(columnHeaderValue);
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && !isMainTableFirstShown.get() && mainTable.isShowing()) {
                Utils.checkAndRunInEDT(() -> {
                    configRowHeight();
                    adjustComumns();
                    invokeDrawColumnHeaders();
                    isMainTableFirstShown.set(true);
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
        ColumnHeaderProvider<V> columnHeaderProvider = mainTable.getModel().getColumnHeaderProvider();
        for(int rowViewIndex=0;rowViewIndex<mainTable.getRowCount();rowViewIndex++) {
            int rowModelIndex = mainTable.convertRowIndexToModel(rowViewIndex);
            int rowHeight;
            if ( null == columnHeaderProvider.getColumnHeaderAt(rowModelIndex)) {
                rowHeight = mainTable.getRowHeight();
            } else {
                rowHeight = columnHeaderProvider.getColumnHeaderHeight();
            }
            mainTable.setRowHeight(rowViewIndex, rowHeight);
        }
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
        if ( isMainTableFirstShown.get() ) {
            invokeDrawColumnHeaders();
        }
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
        if ( isMainTableFirstShown.get() && ! isAdjustingColumn ) {
            drawnHeaderValues.clear();
        }
        isAdjustingColumn = false;
    }

    @Override
    public void componentMoved(final ComponentEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void componentShown(final ComponentEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
        invokeDrawColumnHeaders();
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        if (TableUtils.toRebuildCache(e) ) {
            adjustComumns();
            configRowHeight();
            invokeDrawColumnHeaders();
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        if ( isMainTableFirstShown.get() ) {
            if ( ! evt.getValueIsAdjusting()) {
                adjustComumns();
            }

            if ( evt.getSource() == mainTable.getTableScrollPane().getHorizontalScrollBar()) {
                horizontalScrollBarAdjustmentValue = evt.getValue();
                invokeDrawColumnHeaders();
            }

        }
    }
}
