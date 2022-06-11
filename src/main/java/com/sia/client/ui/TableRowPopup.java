package com.sia.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TableRowPopup {

    private JTable jtable;
    private Point pointAtTable;
    private JPopupMenu jPopupMenu;
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
    }
    public void show() {
        jPopupMenu.show(jtable,pointAtTable.x,pointAtTable.y);
    }
    private void hideRows(ActionEvent ae) {

        jPopupMenu.setVisible(false);
    }
    private void unHideRows(ActionEvent ae) {

        jPopupMenu.setVisible(false);
    }
    private void highlightRows(ActionEvent ae) {

        jPopupMenu.setVisible(false);
    }
}
