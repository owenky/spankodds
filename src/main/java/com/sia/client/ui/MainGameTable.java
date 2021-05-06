package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.MainGameTableModel;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableCellRenderer;

import static com.sia.client.config.Utils.log;

public class MainGameTable extends JTable {

    private static final int SoccerRowHeight = 60;
    private static final int NormalRowHeight = 30;
    private TableColumnAdjuster tableColumnAdjuster;
    private boolean isSoccer=false;


    public MainGameTable() {
    }
    @Override
    public MainGameTableModel createDefaultDataModel() {
        return new MainGameTableModel();
    }
    @Override
    public MainGameTableModel getModel() {
        return (MainGameTableModel)super.getModel();
    }
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (isSoccer) {
            return new LineRenderer(SiaConst.SoccerStr);
        } else {
            return new LineRenderer();
        }
    }
    public void addGameLine(LinesTableData gameLine) {
        getModel().addGameLine(gameLine);
    }
    public LinesTableData getLinesTableData(int row) {
        return getModel().getLinesTableData(row).linesTableData;
    }
    public void optimizeRowHeightsAndGameLineTitles() {
        if (isSoccer) {
            setRowHeight(SoccerRowHeight);
        } else {
            setRowHeight(NormalRowHeight);
        }
        setGameGroupHeaderRowHeight();
        GameLineTitleManager gameLineTitleManager = new GameLineTitleManager(this);
        gameLineTitleManager.installListeners();
    }
    public boolean isSoccer() {
        return isSoccer;
    }
    @Override
    public void setName(String name) {
        super.setName(name);
        isSoccer = SiaConst.SoccerStr.equalsIgnoreCase(name);
    }
    public void adjustColumns(boolean includeHeaders) {
        getTableColumnAdjuster().adjustColumns(includeHeaders);
    }
    public void adjustColumnsOn(Integer ... gameIds) {
        getTableColumnAdjuster().adjustColumnsOnRow(gameIds);
    }
    public void adjustColumn(int col) {
        getTableColumnAdjuster().adjustColumn(col);
    }
    private void setGameGroupHeaderRowHeight() {
        for(int rowViewIndex=0;rowViewIndex<getRowCount();rowViewIndex++) {
            int rowModelIndex = this.convertRowIndexToModel(rowViewIndex);
            int gameId = getModel().getGameId(rowModelIndex);
            if ( SiaConst.BlankGameId == gameId) {
                setRowHeight(rowViewIndex, SiaConst.GameGroupHeaderHeight);
            }
        }
    }
    private TableColumnAdjuster getTableColumnAdjuster() {
        if ( null == tableColumnAdjuster) {
            tableColumnAdjuster = new TableColumnAdjuster(this);
            tableColumnAdjuster.setColumnHeaderIncluded(true);
            //TODO debug code block
            this.getColumnModel().addColumnModelListener( new TableColumnModelListener() {

                @Override
                public void columnAdded(final TableColumnModelEvent e) {

                }

                @Override
                public void columnRemoved(final TableColumnModelEvent e) {
                    log("columnRemoved....column size="+MainGameTable.this.getColumnCount());
                }

                @Override
                public void columnMoved(final TableColumnModelEvent e) {

                }

                @Override
                public void columnMarginChanged(final ChangeEvent e) {

                }

                @Override
                public void columnSelectionChanged(final ListSelectionEvent e) {

                }
            });
            //END OF DEBUG TODO
        }
        return tableColumnAdjuster;
    }
}
