package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.SportName;
import com.sia.client.model.*;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainGameTable extends ColumnCustomizableTable<Game>  {

    private final LineRenderer soccerLineRenderer = new LineRenderer(SportName.Soccer);
    private final LineRenderer lineRenderer = new LineRenderer();
    private final SportType sporetType;

    public MainGameTable(MainGameTableModel tm) {
        super(false,tm);
        sporetType = tm.getSportType();
        this.addMouseListener(new MouseClickListener(getWindowIndex()));
    }
    public int getWindowIndex() {
        return getModel().getScreenProperty().getSpankyWindowConfig().getWindowIndex();
    }
    @Override
    public void setName(String name) {
        super.setName(name+"@"+getWindowIndex());
    }
    @Override
    protected RowHeaderGameTable createNewRowHeaderTable() {
        return new RowHeaderGameTable(this,hasRowNumber());
    }
    @Override
    public MainGameTableModel getModel() {
        return (MainGameTableModel)super.getModel();
    }
    @Override
    public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colViewIndex) {
         return isSoccer(rowViewIndex)? soccerLineRenderer:lineRenderer;
    }
    @Override
    public LinesTableData getLinesTableData(int row) {
        return (LinesTableData)super.getLinesTableData(row);
    }
    @Override
    protected int computeRowHeight(int rowModelIndex) {
        int rowHeight;
        Game game = getGame(rowModelIndex);
        if ( TableRowPopup.isHiddenRow(getName(), game.getGame_id())) {
            return SiaConst.Ui.HiddenRowHeight;
        }

        if ( ! getModel().getSportType().isPredifined()) {
            //for customized sport, stage table section contains mixed sport type games.
            //row height has to be calculated per row rather than per section -- 2021-11-09
            Game g  = getModel().getGame(rowModelIndex);
            boolean isSoccer = SiaConst.SoccerLeagueId == g.getLeague_id();
            if ( isSoccer) {
                rowHeight = Config.instance().getFontConfig().getSoccerRowHeight();
            } else {
                rowHeight = Config.instance().getFontConfig().getNormalRowHeight();
            }

        } else {
            rowHeight = super.computeRowHeight(rowModelIndex);
        }
        return rowHeight;
    }
    private boolean isSoccer(int rowViewIndex) {
        if ( sporetType.equals(SportType.Soccer)) {
            return true;
        } else if (sporetType.isPredifined()) {
            return false;
        }
        int rowModelIndex = this.convertRowIndexToModel(rowViewIndex);
        Game g  = getModel().getGame(rowModelIndex);
        return SiaConst.SoccerLeagueId == g.getLeague_id();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
    public static class MouseClickListener extends MouseAdapter {

        private final int  windowIndex;
        public MouseClickListener(int  windowIndex) {
            this.windowIndex = windowIndex;
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
}
