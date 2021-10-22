package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;

import javax.swing.table.TableCellRenderer;
import java.util.HashMap;
import java.util.Map;

public class MainGameTable extends ColumnCustomizableTable<Game> implements LinesTableDataSupplier {

    private final LineRenderer soccerLineRenderer = new LineRenderer(SiaConst.SoccerStr);
    private final LineRenderer lineRenderer = new LineRenderer();
    private final Map<Integer, Boolean> rowToSoccerStatusMap = new HashMap<>();

    public MainGameTable(MainGameTableModel tm) {
        super(false,tm);
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
        Boolean isSoccer = rowToSoccerStatusMap.get(rowViewIndex);
        if ( null != isSoccer && isSoccer) {
            return soccerLineRenderer;
        } else {
            return lineRenderer;
        }
    }
    @Override
    public LinesTableData getLinesTableData(int row) {
        return (LinesTableData)super.getLinesTableData(row);
    }
    @Override
    public void setName(String name) {
        super.setName(name);
    }
    @Override
    public void configHeaderRow() {
        super.configHeaderRow();
        if ( null != rowToSoccerStatusMap) {
            rowToSoccerStatusMap.clear();
            for (int rowViewIndex = 0; rowViewIndex < getRowCount(); rowViewIndex++) {
                rowToSoccerStatusMap.put(rowViewIndex, isSoccer(rowViewIndex));
            }
        }
    }
    public LinesTableData findTableSectionByHeaderValue(String gameGroupHeader) {
        return getModel().findTableSectionByHeaderValue(gameGroupHeader);
    }
    private boolean isSoccer(int rowViewIndex) {
        int rowModelIndex = this.convertRowIndexToModel(rowViewIndex);
        Game g  = getModel().getGame(rowModelIndex);
        return SiaConst.SoccerLeagueId == g.getLeague_id();
    }
}
