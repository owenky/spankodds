package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.config.Utils;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

import static com.sia.client.config.Utils.log;

public class TableColumnPopupMenu{

    private final JTable table;
    private final SportsTabPane stp;
    private final AnchoredLayeredPane anchoredLayeredPane;
    private JMenuItem deleteItem;
    private JMenuItem renameItem;
    private JMenuItem choseColorItem;
    private JMenuItem closeItem;
    private int tableColumnIndex;
    private JPanel menuBar;
    private static TableColumnPopupMenu oldTableColumnPopupMenu=null;

    public static TableColumnPopupMenu of(SportsTabPane stp,JTable table) {
        if ( null != oldTableColumnPopupMenu) {
            oldTableColumnPopupMenu.hideMenu();
        }
        oldTableColumnPopupMenu = new TableColumnPopupMenu(stp,table);
        return oldTableColumnPopupMenu;
    }
    private TableColumnPopupMenu(SportsTabPane stp,JTable table) {
        this.table = table;
        this.stp = stp;
        anchoredLayeredPane = new AnchoredLayeredPane(stp,(JComponent)stp.getSelectedComponent(),LayedPaneIndex.TableColumnMenuIndex);
    }
    public void showMenu(int tableColumnIndex) {
        Dimension menuBarSize = new Dimension(130,110);
        this.tableColumnIndex = tableColumnIndex;
        menuBar = new JPanel();
        menuBar.setLayout(new GridLayout(0, 1, 0, 1));
        menuBar.setBorder(BorderFactory.createEtchedBorder());
        menuBar.add(getRenmeItem());
        menuBar.add(getChoseColorItem());
        menuBar.add(getDeleteItem());

        Supplier<Point> anchorPointSupl = ()-> {
            JTableHeader header = table.getTableHeader();
            Point headerAtScreen = header.getLocationOnScreen();
            Rectangle r = header.getHeaderRect(tableColumnIndex);
            return new Point((int)(r.getX()+headerAtScreen.getX()),(int)(r.getHeight()+headerAtScreen.getY()));
        };

        anchoredLayeredPane.openAndAnchoredAt(menuBar,menuBarSize,true,anchorPointSupl);
    }
    private JMenuItem getCloseItem() {
        if ( null == closeItem) {
            closeItem = new JMenuItem("Close this menu");
            closeItem.addActionListener((event)-> hideMenu());
            closeItem.setBorder(BorderFactory.createEmptyBorder());
        }
        return closeItem;
    }
    private JMenuItem getRenmeItem() {
        if ( null == renameItem) {
            renameItem = new JMenuItem("Rename Column");
            renameItem.addActionListener((event)->renameColumn());
            renameItem.setBorder(BorderFactory.createEmptyBorder());
        }
        return renameItem;
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
    private void renameColumn() {
        ActionListener renameAction = (evt)-> this.showMenu(tableColumnIndex);
        RenameColumnPopupMenu renameColumnPopupMenu = RenameColumnPopupMenu.of(stp,table,renameAction);
        renameColumnPopupMenu.showMenu(tableColumnIndex);
        hideMenu();
    }
    private void deleteColumn() {
        TableColumn tc = table.getColumnModel().getColumn(tableColumnIndex);
        int option = JOptionPane.showConfirmDialog((Component)null, "Do you really want to delete column "+tc.getHeaderValue()+"?", "Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE, (Icon)null);
        if ( option == JOptionPane.YES_OPTION) {
            BookieColumnController2 bookieColumnController2 = new BookieColumnController2(null);
            bookieColumnController2.setSelectedValueByBookieId(tc.getIdentifier());
            bookieColumnController2.doRemove();
            bookieColumnController2.doSave();
        }
        hideMenu();
    }
    private void showColorChoserPanel() {

        TableColumn tc = table.getColumnModel().getColumn(tableColumnIndex);
        Object headerValue = tc.getHeaderValue();
        log("change column color, headervalue is " + headerValue+", column index="+tableColumnIndex);

        JColorChooser chooser = new JColorChooser();
        for (AbstractColorChooserPanel p : chooser.getChooserPanels())
        {
            //System.out.println("color panel="+p.getDisplayName());
            if (p.getDisplayName().equals("HSV") || p.getDisplayName().equals("HSL") || p.getDisplayName().equals("CMYK"))
            {
                chooser.removeChooserPanel(p);
            }
        }

        /*
        chooser.getSelectionModel().addChangeListener(arg0 -> {
            Color color2 = chooser.getColor();
            log("change column color, new color:"+color2);
        });
        */
        ActionListener okListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = chooser.getColor();
                if(color != null && color != Color.WHITE)
                {
                    log("color chosen was " + color);
                    Integer bookieid = AppController.getBookieId(headerValue.toString());
                    AppController.putColor(bookieid, color);
                    MainGameTableModel model = ((MainGameTable)table).getModel();
                    TableModelEvent tme = new TableModelEvent(model,0,Integer.MAX_VALUE,model.getAllColumns().size(),TableModelEvent.UPDATE);
                    model.fireTableChanged(tme);

                }

            }
        };

        JDialog dialog = JColorChooser.createDialog(null, headerValue + " Color",
                true, chooser, okListener, null);


        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        Point p = menuBar.getLocationOnScreen();
        dialog.setLocation(p.x, p.y + menuBar.getSize().height);
        dialog.setVisible(true);
/*
        Color color = chooser.getColor();
        if (color != null) {
            log("color chosen was " + color);
            Integer bookieid = AppController.getBookieId(headerValue.toString());
            AppController.putColor(bookieid, color);
            MainGameTableModel model = ((MainGameTable)table).getModel();
            TableModelEvent tme = new TableModelEvent(model,0,Integer.MAX_VALUE,model.getAllColumns().size(),TableModelEvent.UPDATE);
            model.fireTableChanged(tme);
        }
        */

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
