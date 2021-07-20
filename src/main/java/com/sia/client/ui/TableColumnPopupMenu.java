package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.config.Utils;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Supplier;

public class TableColumnPopupMenu{

    private final JTable table;
    private final AnchoredLayeredPane anchoredLayeredPane;
    private JMenuItem deleteItem;
    private JMenuItem choseColorItem;
    private JMenuItem closeItem;
    private static TableColumnPopupMenu oldTableColumnPopupMenu=null;

    public static TableColumnPopupMenu of(JTable table) {
        if ( null != oldTableColumnPopupMenu) {
            oldTableColumnPopupMenu.hideMenu();
        }
        oldTableColumnPopupMenu = new TableColumnPopupMenu(table);
        return oldTableColumnPopupMenu;
    }
    private TableColumnPopupMenu(JTable table) {
        this.table = table;
        anchoredLayeredPane = new AnchoredLayeredPane(table, LayedPaneIndex.TableColumnMenuIndex);
    }
    public void showMenu(int tableColumnIndex) {
        JPopupMenu menuBar = new JPopupMenu();
        menuBar.add(getChoseColorItem());
        menuBar.add(getDeleteItem());
        menuBar.add(getCloseItem());

        anchoredLayeredPane.setUserPane(menuBar);
        Supplier<Point> anchorPointSupl = ()-> {
            JTableHeader header = table.getTableHeader();
            Point headerAtScreen = header.getLocationOnScreen();
            Rectangle r = header.getHeaderRect(tableColumnIndex);
            return new Point((int)(r.getX()+headerAtScreen.getX()),(int)(r.getHeight()+headerAtScreen.getY()));
        };

        anchoredLayeredPane.openAndAnchoredAt(anchorPointSupl);
    }
    private JMenuItem getCloseItem() {
        if ( null == closeItem) {
            closeItem = new JMenuItem("Close this menu");
            closeItem.addActionListener((event)-> hideMenu());
        }
        return closeItem;
    }
    private JMenuItem getDeleteItem() {
        if ( null == deleteItem) {
            deleteItem = new JMenuItem("Delete Column");
            deleteItem.addActionListener((event)-> hideMenu());
        }
        return deleteItem;
    }
    private JMenuItem getChoseColorItem() {
        if ( null == choseColorItem) {
            choseColorItem = new JMenuItem("Chose Color");
            choseColorItem.addActionListener((event)-> hideMenu());
        }
        return choseColorItem;
    }
    public void hideMenu() {
        Utils.removeItemListeners(closeItem);
        Utils.removeItemListeners(deleteItem);
        Utils.removeItemListeners(choseColorItem);
        if ( null != anchoredLayeredPane) {
            anchoredLayeredPane.close();
        }
    }
}
