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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class TableColumnHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener, AdjustmentListener {

    private static final Color DefaultHeaderColor = SiaConst.DefaultHeaderColor;
    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable mainTable;
    private final Color titleColor;
    private final Font titleFont;
    private final int headerHeight;
    private final Set<ColumnHeaderStruct> columnHeaderStructSet = new HashSet<>();
    private boolean isMainTableFirstShown = false;
    private Supplier<List<ColumnHeaderStruct>> rowHeaderListSppr;

    public TableColumnHeaderManager(ColumnCustomizableTable mainTable, Color titleColor, Font titleFont, int headerHeight) {
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
                mainTable.adjustColumns(true);
                drawColumnHeaders();
                isMainTableFirstShown = true;
            }
        }
    }

    private void drawColumnHeaders() {
        List<ColumnHeaderStruct> newRowHeaderList = rowHeaderListSppr.get();
        if ( shouldDrawColumnHeader(newRowHeaderList)) {
            restoreColumnHeaderHeight(newRowHeaderList);
            columnHeaderStructSet.clear();
            columnHeaderStructSet.addAll(newRowHeaderList);
            drawColumnHeaders(0);
        }
    }

    private void drawColumnHeaders(int diffByScroll) {
        List<ColumnHeaderStruct> rowHeaderList = rowHeaderListSppr.get();
        if (null != rowHeaderList) {
            for (ColumnHeaderStruct struct : rowHeaderList) {
                drawColumnHeader(struct, diffByScroll);
            }
        }
    }
    private boolean shouldDrawColumnHeader(List<ColumnHeaderStruct> newRowHeaderList) {
        boolean status = columnHeaderStructSet.containsAll(newRowHeaderList);
        if ( status ) {
            status = columnHeaderStructSet.size()==newRowHeaderList.size();
        }
        return ! status;
    }
    private void restoreColumnHeaderHeight(List<ColumnHeaderStruct> newRowHeaderList) {
        for(ColumnHeaderStruct struct: newRowHeaderList) {
            int rowViewIndex = mainTable.convertRowIndexToView(struct.headerModelIndex);
            mainTable.setRowHeight(rowViewIndex, mainTable.getRowHeight());
        }
    }
    private void drawColumnHeader(ColumnHeaderStruct struct, int diffByScroll) {
        int rowViewIndex = mainTable.convertRowIndexToView(struct.headerModelIndex);
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(struct.headerString, header -> makeColumnHeaderComp(mainTable, header, titleFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, headerHeight, diffByScroll);
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
        int value = evt.getValue();
        drawColumnHeaders(value);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class ColumnHeaderStruct {
        public final String headerString;
        public final int headerModelIndex;

        public ColumnHeaderStruct(String headerString, int headerModelIndex) {
            this.headerString = headerString;
            this.headerModelIndex = headerModelIndex;
        }
        @Override
        public int hashCode() {
            return headerString.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final ColumnHeaderStruct that = (ColumnHeaderStruct) o;
            return headerModelIndex == that.headerModelIndex && Objects.equals(headerString, that.headerString);
        }
    }
}
