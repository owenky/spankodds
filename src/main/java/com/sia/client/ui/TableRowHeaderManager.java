package com.sia.client.ui;

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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sia.client.config.Utils.log;

public class TableRowHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener {

    private static final Color DefaultHeaderColor = Color.BLACK;
    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnLockableTable mainTable;
    private final Color titleColor;
    private final Font titleFont;
    private final int headerHeight;
    private boolean isMainTableFirstShown = false;
    private List<ColumnHeaderStruct> rowHeaderList;

    public TableRowHeaderManager(ColumnLockableTable mainTable,Color titleColor,Font titleFont,int headerHeight) {
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
    }
    public void setColumnHeaderList(List<ColumnHeaderStruct> rowHeaderList) {
        this.rowHeaderList = rowHeaderList;
    }
    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }
    @Override
    public void componentMoved(final ComponentEvent e) {

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

    @Override
    public void componentShown(final ComponentEvent e) {
        drawColumnHeaders();
    }

    @Override
    public void componentHidden(final ComponentEvent e) {
        drawColumnHeaders();
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
    public void componentResized(final ComponentEvent e) {
        drawColumnHeaders();
    }
    @Override
    public void tableChanged(final TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            drawColumnHeaders();
        }
    }
    private void drawColumnHeaders() {
        if ( null != rowHeaderList) {
            for(ColumnHeaderStruct struct: rowHeaderList) {
                drawColumnHeader(struct);
            }
        }
    }
    private void drawColumnHeader(ColumnHeaderStruct struct) {
        int rowViewIndex = mainTable.convertRowIndexToView(struct.headerModelIndex);
//TODO: debug
        if (rowViewIndex < 10) {
            log("header at row view index:" + rowViewIndex + ", header=" +struct.headerString);
        }
//END OF debug TODO
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(struct.headerString, header-> makeColumnHeaderComp(mainTable,header,titleColor,titleFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, headerHeight);
    }
    public static JComponent makeColumnHeaderComp(ColumnLockableTable jtable, String gameGroupHeader, Color titleColor, Font titleFont) {

        JPanel jPanel = new JPanel();
        jPanel.setBackground(titleColor);
        BorderLayout bl = new BorderLayout();
        bl.setVgap(3);
        jPanel.setLayout(bl);
        jPanel.setBackground(DefaultHeaderColor);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);
        jPanel.add(BorderLayout.CENTER, titleLabel);
//        jtable.getCoordinateContainer().add(jPanel);
//        jtable.getRootPane().getContentPane().add(jPanel);
        jtable.add(jPanel);
//        SwingUtilities.getRootPane(jtable).getLayeredPane().add(jPanel, 1);

        return jPanel;
    }
    public static void layOutColumnHeader(int rowViewIndex, ColumnLockableTable mainTable, JComponent header, int headerHeight) {

        Rectangle r1 = mainTable.getCellRect(rowViewIndex, 0, true);
        JComponent tableContainer = mainTable.getCoordinateContainer();

        int x1 = 0;
        int y1 = (int) r1.getY();
        int tableWidth = mainTable.getWidth();
        int tableParentWidth = tableContainer.getWidth();
        int width = Math.min(tableWidth, tableParentWidth);
        mainTable.setRowHeight(rowViewIndex, headerHeight);
        mainTable.getRowHeaderTable().setRowHeight(rowViewIndex, headerHeight);
        header.setBounds(x1, y1, width, headerHeight);
        if ( null != mainTable.getRowHeaderTable() ) {
            drawHeaderOnRowHeaderTable(mainTable.getRowHeaderTable(),rowViewIndex,headerHeight);
        }
    }
    private static void drawHeaderOnRowHeaderTable(RowHeaderTable rhTable, int rowViewIndex,int headerHeight) {

        Rectangle r1 = rhTable.getCellRect(rowViewIndex, 0, true);
        int x1 = 0;
        int y1 = (int) r1.getY();
        int width = rhTable.getWidth();


        JPanel panel = new JPanel();
        rhTable.add(panel);
        panel.setBackground(DefaultHeaderColor);
        panel.setBounds(x1, y1, width, headerHeight);
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
