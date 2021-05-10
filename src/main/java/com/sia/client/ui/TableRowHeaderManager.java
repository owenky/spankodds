package com.sia.client.ui;

import com.sia.client.config.SiaConst;

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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TableRowHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private static final Color DefaultHeaderColor = SiaConst.DefaultHeaderColor;
    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnLockableTable mainTable;
    private final Color titleColor;
    private final Font titleFont;
    private final int headerHeight;
    private boolean isMainTableFirstShown = false;
    private Supplier<List<ColumnHeaderStruct>> rowHeaderListSppr;
    private int lastValue = -1;

    public TableRowHeaderManager(ColumnLockableTable mainTable, Color titleColor, Font titleFont, int headerHeight) {
        this.mainTable = mainTable;
        this.titleColor = titleColor;
        this.titleFont = titleFont;
        this.headerHeight = headerHeight;

    }

    public void installListeners() {
        mainTable.addHierarchyListener(this);
        mainTable.getColumnModel().addColumnModelListener(this);
        mainTable.getParent().addComponentListener(this);
        mainTable.getModel().addTableModelListener(this);
        mainTable.getTableScrollPane().getHorizontalScrollBar().addAdjustmentListener(this);

    }

    public void setColumnHeaderList(Supplier<List<ColumnHeaderStruct>> rowHeaderListSppr) {
        this.rowHeaderListSppr = rowHeaderListSppr;
    }

    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainTable && !isMainTableFirstShown && mainTable.isShowing()) {
                isMainTableFirstShown = true;
                drawColumnHeaders();
            }
        }
    }

    private void drawColumnHeaders() {
        drawColumnHeaders(0);
    }

    private void drawColumnHeaders(int diffByScroll) {
        List<ColumnHeaderStruct> rowHeaderList = rowHeaderListSppr.get();
        if (null != rowHeaderList) {
            for (ColumnHeaderStruct struct : rowHeaderList) {
                drawColumnHeader(struct, diffByScroll);
            }
        }
    }

    private void drawColumnHeader(ColumnHeaderStruct struct, int diffByScroll) {
        int rowViewIndex = mainTable.convertRowIndexToView(struct.headerModelIndex);
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(struct.headerString, header -> makeColumnHeaderComp(mainTable, header, titleColor, titleFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, headerHeight, diffByScroll);
    }

    public static JComponent makeColumnHeaderComp(ColumnLockableTable jtable, String gameGroupHeader, Color titleColor, Font titleFont) {

        JPanel jPanel = new JPanel();
        jPanel.setBackground(titleColor);
        BorderLayout bl = new BorderLayout();
        bl.setVgap(1);
        jPanel.setLayout(bl);
        jPanel.setBackground(DefaultHeaderColor);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);
        jPanel.add(BorderLayout.CENTER, titleLabel);
        jtable.add(jPanel);

        return jPanel;
    }

    public static void layOutColumnHeader(int rowViewIndex, ColumnLockableTable mainTable, JComponent header, int headerHeight, int diffByScroll) {
        Rectangle r1 = mainTable.getCellRect(rowViewIndex, 0, true);

        int x1 = 0;
        int y1 = (int) r1.getY();
        int width = (int)header.getPreferredSize().getWidth();
        mainTable.setRowHeight(rowViewIndex, headerHeight);
        mainTable.getRowHeaderTable().setRowHeight(rowViewIndex, headerHeight);
        header.setBounds(x1+diffByScroll*3, y1, width, headerHeight);

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
        drawColumnHeaders();
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
        drawColumnHeaders();
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
            drawColumnHeaders();
        }
    }

    @Override
    public void adjustmentValueChanged(final AdjustmentEvent evt) {
        Adjustable source = evt.getAdjustable();
        if (evt.getValueIsAdjusting()) {
            return;
        }
        int type = evt.getAdjustmentType();
        int value = evt.getValue();
        if (lastValue < 0) {
            lastValue = value;
            return;
        }
        int diff = value - lastValue;
        lastValue = value;
        System.out.println("value =" + evt.getValue() + " diff=" + diff);
        drawColumnHeaders(diff);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ColumnHeaderStruct {
        public final String headerString;
        public final int headerModelIndex;

        public ColumnHeaderStruct(String headerString, int headerModelIndex) {
            this.headerString = headerString;
            this.headerModelIndex = headerModelIndex;
        }
    }
}
