package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
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
import java.awt.Adjustable;
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
import java.util.Map;

public class TableColumnHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable mainTable;
    private boolean isMainTableFirstShown = false;
    private boolean isAdjustingColumn = false;
    private final ColumnHeaderProvider columnHeaderProvider;
    private ColumnHeaderProperty columnHeaderProperty;

    public TableColumnHeaderManager(ColumnCustomizableTable mainTable,ColumnHeaderProvider columnHeaderProvider) {
        this.mainTable = mainTable;
        this.columnHeaderProvider = columnHeaderProvider;
    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
//        mainTable.getModel().addTableModelListener(this);
        mainTable.setTableChangedListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);

    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && !isMainTableFirstShown && mainTable.isShowing()) {
                columnHeaderProperty = columnHeaderProvider.get();
                adjustComumns();
                drawColumnHeaders();
                isMainTableFirstShown = true;
            }
        }
    }
    private void adjustComumns() {
        isAdjustingColumn = true;
        mainTable.adjustColumns(true);
    }
    private void drawColumnHeaders() {
        drawColumnHeaders(0);
    }

    private void drawColumnHeaders(int diffByScroll) {
        if (null != columnHeaderProperty) {
            columnHeaderProperty.rowIndexToHeadValueMap.forEach((key, value) -> drawColumnHeader(key, String.valueOf(value), diffByScroll));
        }
    }
    private void restoreRowHeight() {
        for(int i=0;i<mainTable.getRowCount();i++) {
            mainTable.setRowHeight(i, mainTable.getRowHeight());
        }
    }
    private void drawColumnHeader(int rowModelIndex, String headerValue,int diffByScroll) {
        int rowViewIndex = mainTable.convertRowIndexToView(rowModelIndex);
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(headerValue, header -> makeColumnHeaderComp(mainTable, header, columnHeaderProperty.headerFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, columnHeaderProperty.columnHeaderHeight, diffByScroll);
    }

    public static JComponent makeColumnHeaderComp(ColumnCustomizableTable jtable, String gameGroupHeader, Font titleFont) {

        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        BorderLayout bl = new BorderLayout();
//        bl.setVgap(1);
        jPanel.setLayout(bl);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);
        jPanel.add(BorderLayout.CENTER, titleLabel);
        jtable.add(jPanel);

        return jPanel;
    }

    public static void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable mainTable, JComponent header, int headerHeight, int diffByScroll) {
        Rectangle r1 = mainTable.getCellRect(rowViewIndex-1, 0, true);
        int x1 = 0;
        int y1 = (int) (r1.getY() + r1.getHeight());
        int width = (int)header.getPreferredSize().getWidth();
        mainTable.setRowHeight(rowViewIndex, headerHeight);
        header.setBounds(x1+diffByScroll, y1, width, headerHeight);

    }
    @Override
    public void columnAdded(final TableColumnModelEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void columnRemoved(final TableColumnModelEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void columnMoved(final TableColumnModelEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void columnMarginChanged(final ChangeEvent e) {
        if ( isMainTableFirstShown ) {
            drawColumnHeaders();
        }
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
        if ( isMainTableFirstShown && ! isAdjustingColumn ) {
            drawColumnHeaders();
        }
        isAdjustingColumn = false;
    }

    @Override
    public void componentMoved(final ComponentEvent e) {

    }

    @Override
    public void componentShown(final ComponentEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            restoreRowHeight();
            columnHeaderProperty = columnHeaderProvider.get();
            drawColumnHeaders();
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        if ( isMainTableFirstShown ) {
            Adjustable source = evt.getAdjustable();
            if (evt.getValueIsAdjusting()) {
                return;
            }
            int value = evt.getValue();
            if ( value != 0) {
                drawColumnHeaders(value);
            }
        }
    }
}
