package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.config.Utils;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import java.util.function.Supplier;

import static com.sia.client.config.Utils.log;

public class TableColumnPopupMenu{

    private final JTable table;
    private final AnchoredLayeredPane anchoredLayeredPane;
    private JMenuItem deleteItem;
    private JMenuItem choseColorItem;
    private JMenuItem closeItem;
    private int tableColumnIndex;
    private JPanel menuBar;
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
        this.tableColumnIndex = tableColumnIndex;
        menuBar = new JPanel();
        menuBar.setSize(new Dimension(110,57));
        menuBar.setBorder(BorderFactory.createEtchedBorder());
        menuBar.add(getChoseColorItem());
        menuBar.add(getDeleteItem());
//        menuBar.add(getCloseItem());

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
            closeItem.setBorder(BorderFactory.createEmptyBorder());
        }
        return closeItem;
    }
    private JMenuItem getDeleteItem() {
        if ( null == deleteItem) {
            deleteItem = new JMenuItem("Delete Column");
            deleteItem.addActionListener((event)->deleteColumn());
            deleteItem.setBorder(BorderFactory.createEmptyBorder());
        }
        return deleteItem;
    }
    private JMenuItem getChoseColorItem() {
        if ( null == choseColorItem) {
            choseColorItem = new JMenuItem("Chose Color");
            choseColorItem.addActionListener((event)-> choseColumnColor());
            choseColorItem.setBorder(BorderFactory.createEmptyBorder());
        }
        return choseColorItem;
    }
    private void choseColumnColor() {
        showColorChoserPanel();
        hideMenu();
    }
    private void deleteColumn() {
        TableColumn tc = table.getColumnModel().getColumn(tableColumnIndex);
        BookieColumnController2 bookieColumnController2 = new BookieColumnController2(false);
        bookieColumnController2.setSelectedValueByBookieId(tc.getIdentifier());
        bookieColumnController2.doRemove();
        bookieColumnController2.doSave();
        hideMenu();
    }
    private void showColorChoserPanel() {

        TableColumn tc = table.getColumnModel().getColumn(tableColumnIndex);
        Object headerValue = tc.getHeaderValue();
        log("change column color, headervalue is " + headerValue+", column index="+tableColumnIndex);

        JColorChooser chooser = new JColorChooser();

        chooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                Color color2 = chooser.getColor();
                log("change column color, new color:"+color2);
            }
        });


        JDialog dialog = JColorChooser.createDialog(null, headerValue + " Color",
                true, chooser, null, null);


        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        Point p = menuBar.getLocationOnScreen();
        dialog.setLocation(p.x, p.y + menuBar.getSize().height);
        //dialog.setLocationRelativeTo(r);
        dialog.setVisible(true);

        Color color = chooser.getColor();
        if (color != null) {
            log("color chosen was " + color);
            String bookieid = AppController.getBookieId(headerValue.toString());
            AppController.getBookieColors().put(bookieid, color);
            Vector dm = AppController.getDataModels();
            ((LinesTableData)dm.get(0)).fire(null);
        }
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
