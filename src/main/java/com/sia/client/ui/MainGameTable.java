package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SportType;

import javax.swing.table.TableCellRenderer;

public class MainGameTable extends ColumnCustomizableTable<Game> implements LinesTableDataSupplier {

    private final LineRenderer soccerLineRenderer = new LineRenderer(SiaConst.SoccerStr);
    private final LineRenderer lineRenderer = new LineRenderer();
    private final SportType sporetType;

    public MainGameTable(MainGameTableModel tm) {
        super(false,tm);
        sporetType = tm.getSportType();
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
    public void setName(String name) {
        super.setName(name);
    }
    @Override
    public void configHeaderRow() {
        super.configHeaderRow();
        if ( ! getModel().getSportType().isPredifined()) {
            //for customized sport, stage table section contains mixed sport type games.
            //row height has to be calculated per row rather than per section -- 2021-11-09
           for(int rowViewIndex=0;rowViewIndex < getRowCount();rowViewIndex++) {
               if ( isSoccer(rowViewIndex)) {
                   setRowHeight(rowViewIndex,SiaConst.SoccerRowheight);
               }
           }
        }
    }
//    public LinesTableData findTableSectionByHeaderValue(GameGroupHeader gameGroupHeader) {
//        return getModel().findTableSectionByHeaderValue(gameGroupHeader);
//    }
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
}
