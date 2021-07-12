package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;

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
    public TableColumnPopupMenu(JTable table) {
        this.table = table;
        anchoredLayeredPane = new AnchoredLayeredPane(table, LayedPaneIndex.TableColumnMenuIndex);
    }
    public void showMenu(int tableColumnIndex) {
        JPopupMenu menuBar = new JPopupMenu("Test Popup");
        menuBar.add(new JMenuItem("item1"));
        menuBar.add(new JMenuItem("item2"));

        anchoredLayeredPane.setUserPane(menuBar);
        Supplier<Point> anchorPointSupl = ()-> {
            JTableHeader header = table.getTableHeader();
            Rectangle r = header.getHeaderRect(tableColumnIndex);
            return new Point((int)r.getX(),(int)(r.getY()+r.getHeight()));
        };

        anchoredLayeredPane.openAndAnchoredAt(anchorPointSupl);
    }
    public void hideMenu() {
        anchoredLayeredPane.close();
    }
}
