package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;

import static com.sia.client.config.Utils.log;

public class MainGameTable extends ColumnLockableTable implements LinesTableDataSupplier {

    private TableColumnAdjuster tableColumnAdjuster;
    private boolean isSoccer=false;


    public MainGameTable() {
        super(false);
    }
    @Override
    protected RowHeaderGameTable createNewRowHeaderTable() {
        return new RowHeaderGameTable(this,hasRowNumber());
    }
    //TODO: this override is for debug only
    @Override
    public void setRowHeight(int row,int height) {
        if ( getRowHeight(row) == SiaConst.GameGroupHeaderHeight && height != SiaConst.GameGroupHeaderHeight ) {
            new Exception("set RowHeight after header is set row="+row).printStackTrace();
        }
        super.setRowHeight(row,height);
    }
    //TODO: this override is for debug only
    @Override
    public void setRowHeight(int height) {
        if ( getRowHeight(0) == SiaConst.GameGroupHeaderHeight && height != SiaConst.GameGroupHeaderHeight ) {
            new Exception("set RowHeight after header is set height="+height).printStackTrace();
        }
        super.setRowHeight(height);
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
    @Override
    public LinesTableData getLinesTableData(int row) {
        return getModel().getLinesTableData(row).linesTableData;
    }
    public void optimizeRowHeightsAndGameLineTitles() {
        if (isSoccer) {
            setRowHeight(SiaConst.SoccerRowheight);
        } else {
            setRowHeight(SiaConst.NormalRowheight);
        }
        GameGropHeaderManager gameGropHeaderManager = new GameGropHeaderManager(this);
        gameGropHeaderManager.installListeners();
    }
    public boolean isSoccer() {
        return isSoccer;
    }
    @Override
    public void setName(String name) {
        super.setName(name);
        isSoccer = SiaConst.SoccerStr.equalsIgnoreCase(name);
    }
    public final void tableChangedBySuperClass(TableModelEvent e_) {
        super.tableChanged(e_);
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
