package com.sia.client.ui;

import com.sia.client.model.AccessableToGame;
import com.sia.client.model.BookieManager;
import com.sia.client.model.Game;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameTableMouseListener extends MouseAdapter implements ListSelectionListener {

    public static GameTableMouseListener instance() {
        return LazyInitHolder.instance;
    }
    private GameTableMouseListener() {
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
//        Object source = e.getSource();
//        System.out.println("source="+source);
    }
    @Override
    public void mousePressed(MouseEvent event) {
//        if ( event.getButton() == MouseEvent.BUTTON1) {
//            JTable table = (JTable)event.getSource();
//            Point point = event.getPoint();
//            int row = table.rowAtPoint(point);
//            RowSelection rowSelection = Config.instance().getRowSelection();
//            AccessableToGame<Game> accessableToGame = (AccessableToGame<Game>)event.getSource();
//            int gameId = accessableToGame.getGame(table.convertRowIndexToModel(row)).getGame_id();
//            if ( rowSelection.isGameSelected(table,gameId)) {
//                table.removeRowSelectionInterval(row,row);
//                rowSelection.clearSelectedGames(table);
//            } else {
//                rowSelection.clearSelectedGames(table);
//                rowSelection.addSelectedGames(table,gameId);
//            }
//        }
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        // for double click or right click, show game details
        SportsTabPane stp = TableUtils.findParent((JTable)event.getSource(),SportsTabPane.class);
         if (  (2 == event.getClickCount() && event.getButton() == MouseEvent.BUTTON1)
                || event.getButton() == MouseEvent.BUTTON3) {

            AccessableToGame<Game> accessableToGame = (AccessableToGame<Game>)event.getSource();
            JTable table = (JTable)accessableToGame;
            Point point = event.getPoint();
            int row = table.rowAtPoint(point);
            int col = table.columnAtPoint(point);
            int rowModelIndex = table.convertRowIndexToModel(row);
            int colModelIndex = table.convertRowIndexToModel(col);
            TableColumn tc = table.getColumnModel().getColumn(colModelIndex);
            String bookieShortName = String.valueOf(tc.getHeaderValue());
            Integer bookieId = BookieManager.instance().getBookieId(bookieShortName);
            if (  null != bookieId && bookieId < 990) {
                SwingUtilities.convertPointToScreen(point,table);
                Game game = accessableToGame.getGame(rowModelIndex);
                GameHistPane.showHistPane(stp,point,game,bookieId);
            } else {
                TableRowPopup tableRowPopup = TableRowPopup.instance();
                tableRowPopup.setJtable(table);
                tableRowPopup.setPointAtTable(point);
                tableRowPopup.show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final GameTableMouseListener instance = new GameTableMouseListener();
    }
}
