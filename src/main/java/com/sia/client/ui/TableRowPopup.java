package com.sia.client.ui;

import com.sia.client.config.SiaConst;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TableRowPopup {

    private JTable jtable;
    private Point pointAtTable;
    private int rowViewIndex;
    private int columnViewIndex;
    private JPopupMenu jPopupMenu;
    private static final Map<String, Map<Integer,Integer>> hiddenRows = new HashMap<>();
    private static TableRowPopup instance = new TableRowPopup();

    public static TableRowPopup instance() {
        return instance;
    }
    private TableRowPopup() {
        init();
    }
    private void init() {
        jPopupMenu = new JPopupMenu();
        JMenuItem hideRowsItem = new JMenuItem("Hide Selected Rows");
        JMenuItem unHideRowsItem = new JMenuItem("Un-hide Selected Rows");
        JMenuItem highlightRowsItem = new JMenuItem("Highlight Selected Rows");
        hideRowsItem.addActionListener(this::hideRows);
        unHideRowsItem.addActionListener(this::unHideRows);
        highlightRowsItem.addActionListener(this::highlightRows);

        jPopupMenu.add(hideRowsItem);
//        jPopupMenu.add(unHideRowsItem);
//        jPopupMenu.add(highlightRowsItem);
    }
    public void setJtable(JTable jtable) {
        this.jtable = jtable;
    }
    public void setPointAtTable(Point pointAtTable) {
        this.pointAtTable = pointAtTable;
        rowViewIndex = jtable.rowAtPoint(pointAtTable);
        columnViewIndex = jtable.columnAtPoint(pointAtTable);
    }
    public void show() {
        jPopupMenu.show(jtable,pointAtTable.x,pointAtTable.y);
    }
    private void reConfigHeaderRow() {
        int minRowViewIndex = Arrays.stream(getSelectedRows()).sorted().findFirst().getAsInt();
        ColumnCustomizableTable<?> mainGameTable = getMainGameTable();
        mainGameTable.configHeaderRow(minRowViewIndex, mainGameTable.getRowCount()-1, false, true);
    }
    private void hideRows(ActionEvent ae) {

        ColumnCustomizableTable<?> mainGameTable = getMainGameTable();
        for(int rViewIndex: getSelectedRows()) {
            int originalRowHeight = mainGameTable.getRowHeight(rViewIndex);
            int gameId = mainGameTable.getGame(mainGameTable.convertRowIndexToModel(rViewIndex)).getGame_id();
            mainGameTable.setRowHeight(rViewIndex, SiaConst.Ui.HiddenRowHeight);
            Map<Integer,Integer> hiddenRowsByTable = hiddenRows.computeIfAbsent(mainGameTable.getName(),(name)->new HashMap<>());
            hiddenRowsByTable.put(gameId,originalRowHeight);
        }
        reConfigHeaderRow();
        jPopupMenu.setVisible(false);
    }
    private void unHideRows(ActionEvent ae) {

        jPopupMenu.setVisible(false);
    }
    private void highlightRows(ActionEvent ae) {

        jPopupMenu.setVisible(false);
    }
    private ColumnCustomizableTable<?> getMainGameTable() {
        ColumnCustomizableTable<?> mainGameTable;
        if ( jtable instanceof RowHeaderTable) {
            mainGameTable = ((RowHeaderTable<?>)jtable).getMainTable();
        } else {
            mainGameTable = (ColumnCustomizableTable<?>)jtable;
        }
        return mainGameTable;
    }
    private int [] getSelectedRows() {
        int [] selectedRows = jtable.getSelectedRows();
        if ( null == selectedRows || 0 == selectedRows.length) {
            selectedRows = new int [1];
            selectedRows[0]=rowViewIndex;
        }
        return selectedRows;
    }
}
