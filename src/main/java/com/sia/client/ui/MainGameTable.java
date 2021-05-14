package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.GameTableTableColumnProvider;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import java.util.Set;
import java.util.stream.Collectors;

public class MainGameTable extends ColumnCustomizableTable implements LinesTableDataSupplier {

    private boolean isSoccer=false;
    private Set<Integer> columnHeaderRowViewIndex;


    public MainGameTable(GameTableTableColumnProvider columnHeaderProvider) {
        super(false,columnHeaderProvider);
        columnHeaderProvider.setMainGameTable(this);
    }
    @Override
    protected RowHeaderGameTable createNewRowHeaderTable() {
        return new RowHeaderGameTable(this,hasRowNumber());
    }
    public Set<Integer> getColumnHeaderRowViewIndex() {
        if ( null == columnHeaderRowViewIndex) {
            columnHeaderRowViewIndex = getModel().getBlankGameIdIndex().stream().map(struct -> struct.tableRowModelIndex).map(this::convertRowIndexToView).collect(Collectors.toSet());
        }
        return columnHeaderRowViewIndex;
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
    public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colViewIndex) {
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
    @Override
    public void tableChanged(TableModelEvent e) {
        if ( e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT ) {
            columnHeaderRowViewIndex = null;
        }
        super.tableChanged(e);
    }
}
