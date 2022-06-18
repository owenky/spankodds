package com.sia.client.ui;

import com.sia.client.model.AccessableToGame;
import com.sia.client.model.BookieManager;
import com.sia.client.model.Game;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameTableMouseListener extends MouseAdapter {

    private final int  windowIndex;
    public GameTableMouseListener(int  windowIndex) {
        this.windowIndex = windowIndex;
    }
    @Override
    public void mousePressed(MouseEvent event) {
        if ( event.getButton() == MouseEvent.BUTTON1) {
            JTable table = (JTable)event.getSource();
            Point point = event.getPoint();
            int row = table.rowAtPoint(point);
            if ( table.isRowSelected(row)) {
                table.removeRowSelectionInterval(row,row);
            } else {
                table.addRowSelectionInterval(row,row);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        // for double click or right click, show game details
        SportsTabPane stp = SpankyWindow.findSpankyWindow(windowIndex).getSportsTabPane();
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
}
