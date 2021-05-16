package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;

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

public class TableColumnHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable mainTable;
    private boolean isMainTableFirstShown = false;
    private boolean isAdjustingColumn = false;
    private final Set<Object> drawnHeaderValues = new HashSet<>();
    private ColumnHeaderProperty columnHeaderProperty;
    private int horizontalScrollBarAdjustmentValue;

    public TableColumnHeaderManager(ColumnCustomizableTable mainTable) {
        this.mainTable = mainTable;
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
//        mainTable.getModel().addTableModelListener(this);
        mainTable.setTableChangedListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);
    }
    public boolean isColumnHeaderDrawn(Object columnHeaderValue) {
        return drawnHeaderValues.contains(columnHeaderValue);
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && !isMainTableFirstShown && mainTable.isShowing()) {
                adjustComumns();
                invokeDrawColumnHeaders();
                isMainTableFirstShown = true;
            }
        }
    }
    private ColumnHeaderProperty getColumnHeaderProperty() {
        if ( null == columnHeaderProperty) {
            //don't use mainTable.getColumnHeaderProvider().get() to obtain columnHeaderProperty because each time this call minght return different rowIndexToHeadValueMap
            columnHeaderProperty = mainTable.getColumnHeaderProvider().get();
        }
        return columnHeaderProperty;
    }
    private void adjustComumns() {
        isAdjustingColumn = true;
        mainTable.adjustColumns(true);
    }
    private void invokeDrawColumnHeaders() {
//        drawColumnHeaders(0);
        drawnHeaderValues.clear();
        mainTable.repaint();
    }

//    private void drawColumnHeaders(int diffByScroll) {
//        if (null != columnHeaderProperty) {
//            columnHeaderProperty.rowIndexToHeadValueMap.forEach((key, value) -> drawColumnHeaderOnModelIndex(key, String.valueOf(value), diffByScroll));
//        }
//    }
    private void configRowHeight() {
        Map<Integer,Object> map = getColumnHeaderProperty().rowIndexToHeadValueMap;
        for(int rowViewIndex=0;rowViewIndex<mainTable.getRowCount();rowViewIndex++) {
            int rowModelIndex = mainTable.convertRowIndexToModel(rowViewIndex);
            int rowHeight;
            if ( null == map.get(rowModelIndex)) {
                rowHeight = mainTable.getRowHeight();
            } else {
                rowHeight = getColumnHeaderProperty().columnHeaderHeight;
            }
            mainTable.setRowHeight(rowViewIndex, rowHeight);
        }
    }
//    private void drawColumnHeaderOnModelIndex(int rowModelIndex, String headerValue, int diffByScroll) {
//        int rowViewIndex = mainTable.convertRowIndexToView(rowModelIndex);
//        drawColumnHeaderOnViewIndex(rowViewIndex,headerValue,diffByScroll);
//    }
    public void drawColumnHeaderOnViewIndex(ColumnHeaderProperty columnHeaderProperty, int rowViewIndex, Object headerValue) {
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(String.valueOf(headerValue), header -> makeColumnHeaderComp(mainTable, header,columnHeaderProperty.headerForeground, columnHeaderProperty.headerFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, columnHeaderProperty.columnHeaderHeight, horizontalScrollBarAdjustmentValue);
        drawnHeaderValues.add(headerValue);
    }
    private static JComponent makeColumnHeaderComp(ColumnCustomizableTable jtable, String gameGroupHeader, Color headerForeGround, Font titleFont) {
        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        BorderLayout bl = new BorderLayout();
//        bl.setVgap(1);
        jPanel.setLayout(bl);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(headerForeGround);
        jPanel.add(BorderLayout.CENTER, titleLabel);
        jtable.add(jPanel);

        return jPanel;
    }

    private static void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable mainTable, JComponent header, int headerHeight, int diffByScroll) {
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
        if ( isMainTableFirstShown ) {
            invokeDrawColumnHeaders();
        }
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
        if ( isMainTableFirstShown && ! isAdjustingColumn ) {
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
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            columnHeaderProperty = mainTable.getColumnHeaderProvider().get();
            configRowHeight();
            invokeDrawColumnHeaders();
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        if ( isMainTableFirstShown ) {
            if (evt.getValueIsAdjusting()) {
                return;
            }
            int newValue = evt.getValue();
            if ( newValue ==horizontalScrollBarAdjustmentValue ) {
                return;
            }
            horizontalScrollBarAdjustmentValue = evt.getValue();
            invokeDrawColumnHeaders();

        }
    }
}