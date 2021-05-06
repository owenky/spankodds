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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLineTitleManager implements HierarchyListener, TableColumnModelListener, ComponentListener {

    private final MainGameTable mainGameTable;
    private boolean isMainTableFirstShown = false;
    private final Map<String, JComponent> gameGroupHeaderComponents = new HashMap<>();
    private static final Color titleColor = new Color(0, 0, 128);
    private static final Font titleFont = new Font("Verdana", Font.BOLD, 11);

    public GameLineTitleManager(MainGameTable mainGameTable) {
        this.mainGameTable = mainGameTable;
    }
    public void installListeners() {
        mainGameTable.addHierarchyListener(this);
        mainGameTable.getColumnModel().addColumnModelListener(this);
        mainGameTable.getParent().addComponentListener(this);
    }
    @Override
    public void hierarchyChanged(final HierarchyEvent e) {
        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
            Object source = e.getSource();
            if ( source == mainGameTable && ! isMainTableFirstShown && mainGameTable.isShowing()) {
                isMainTableFirstShown = true;
                drawGameLineTitles();
            }
        }
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
    private void drawGameLineTitles() {
        MainGameTableModel model = mainGameTable.getModel();
        List<BlankGameStruct> blankGameIndex = model.getBlankGameIdIndex();
        for(BlankGameStruct struct: blankGameIndex) {
            drawGameLineTitle(struct);
        }
    }
    private void drawGameLineTitle(BlankGameStruct struct) {
        Rectangle r1 = mainGameTable.getCellRect(struct.rowTableModelIndex, 0, true);
        Rectangle r2 = mainGameTable.getCellRect(struct.rowTableModelIndex, mainGameTable.getColumnCount()-1, true);
        JComponent tableParent = (JComponent)mainGameTable.getParent();

        int x1 = 0;
        int y1 = (int)r1.getY();
        int tableWidth = mainGameTable.getWidth();
        int tableParentWidth = tableParent.getWidth();
        int width = Math.min(tableWidth, tableParentWidth);
        int height = (int)r2.getHeight();
        JComponent titleBar = getGameGroupHeaderComp(struct.linesTableData.getGameGroupHeader());
        titleBar.setBounds(x1, y1,width,height);
    }
    private JComponent getGameGroupHeaderComp(String gameGroupHeader) {
        return gameGroupHeaderComponents.computeIfAbsent(gameGroupHeader,(gln-> {
            JPanel jPanel = new JPanel();
            jPanel.setBackground(titleColor);
            BorderLayout bl = new BorderLayout();
            bl.setVgap(3);
            jPanel.setLayout(bl);
            jPanel.setMaximumSize(new Dimension(0, SiaConst.GameGroupHeaderHeight));
            jPanel.setPreferredSize(new Dimension(0, SiaConst.GameGroupHeaderHeight));
            JLabel titleLabel = new JLabel(gameGroupHeader);
            titleLabel.setOpaque(false);
            titleLabel.setFont(titleFont);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            titleLabel.setForeground(Color.WHITE);
            jPanel.add(BorderLayout.CENTER, titleLabel);
            mainGameTable.add(jPanel);
            return jPanel;
        }));
    }
}
