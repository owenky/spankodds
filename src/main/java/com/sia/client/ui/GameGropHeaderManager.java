package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.MainGameTableModel.BlankGameStruct;

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

public class GameGropHeaderManager implements HierarchyListener, TableColumnModelListener, ComponentListener, TableModelListener {

    public static final Color DefaultTitleColor = new Color(0, 0, 128);
    private static final Color DefaultHeaderColor = Color.BLACK;
    public static final Font DefaultTitleFont = new Font("Verdana", Font.BOLD, 11);
    private final MainGameTable mainGameTable;
    private final Map<String, JComponent> gameGroupHeaderComponents = new HashMap<>();
    private boolean isMainTableFirstShown = false;
    private Color titleColor = new Color(0, 0, 128);
    private Font titleFont = new Font("Verdana", Font.BOLD, 11);
    private int headerHeight;

    public GameGropHeaderManager(MainGameTable mainGameTable) {
        this(mainGameTable, DefaultTitleColor, DefaultTitleFont, SiaConst.GameGroupHeaderHeight);
    }

    public GameGropHeaderManager(MainGameTable mainGameTable, Color titleColor, Font titleFont, int headerHeight) {
        this.mainGameTable = mainGameTable;
        this.titleColor = titleColor;
        this.titleFont = titleFont;
        this.headerHeight = headerHeight;
    }
    public void installListeners() {
        mainGameTable.addHierarchyListener(this);
        mainGameTable.getColumnModel().addColumnModelListener(this);
        mainGameTable.getParent().addComponentListener(this);
        mainGameTable.getModel().addTableModelListener(this);
    }

    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if (source == mainGameTable && !isMainTableFirstShown && mainGameTable.isShowing()) {
                isMainTableFirstShown = true;
                drawGameLineTitles();
            }
        }
    }

    private void drawGameLineTitles() {
        MainGameTableModel model = mainGameTable.getModel();
        List<BlankGameStruct> blankGameIndex = model.getBlankGameIdIndex();
        for (BlankGameStruct struct : blankGameIndex) {
            drawGameLineTitle(struct);
        }
    }

    private void drawGameLineTitle(BlankGameStruct struct) {
        int rowViewIndex = mainGameTable.convertRowIndexToView(struct.tableRowModelIndex);
//TODO: debug
        mainGameTable.getModel().setHeaderInstalled(true);
        if (rowViewIndex < 10) {
            log("header at row view index:" + rowViewIndex + ", header=" + struct.linesTableData.getGameGroupHeader());
        }
//END OF debug TODO
        JComponent headerComponent = gameGroupHeaderComponents.computeIfAbsent(struct.linesTableData.getGameGroupHeader(), header->makeGameGroupHeaderComp(mainGameTable,header,titleColor,titleFont));
        layOutGameGroupHeader(rowViewIndex, mainGameTable, headerComponent, headerHeight);
    }

    public static JComponent makeGameGroupHeaderComp(ColumnLockableTable jtable, String gameGroupHeader,Color titleColor,Font titleFont) {

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
    public static void layOutGameGroupHeader(int rowViewIndex, ColumnLockableTable mainTable, JComponent header, int headerHeight) {

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
        ///
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
    @Override
    public void columnAdded(final TableColumnModelEvent e) {
        drawGameLineTitles();
    }

    @Override
    public void columnRemoved(final TableColumnModelEvent e) {
        drawGameLineTitles();
    }

    @Override
    public void columnMoved(final TableColumnModelEvent e) {
        drawGameLineTitles();
    }

    @Override
    public void columnMarginChanged(final ChangeEvent e) {
        drawGameLineTitles();
    }

    @Override
    public void columnSelectionChanged(final ListSelectionEvent e) {

    }

    @Override
    public void componentResized(final ComponentEvent e) {
        drawGameLineTitles();
    }

    @Override
    public void componentMoved(final ComponentEvent e) {

    }

    @Override
    public void componentShown(final ComponentEvent e) {

    }

    @Override
    public void componentHidden(final ComponentEvent e) {

    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            drawGameLineTitles();
        }
    }

}
