package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.config.GameNotes;
import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SportType;
import com.sia.client.ui.comps.NodeCellEditor;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class MainGameTable extends ColumnCustomizableTable<Game>  {

    private final TableCellEditor tableCellEditor = new NodeCellEditor();
    private final SportType sporetType;

    public MainGameTable(MainGameTableModel tm) {
        super(false,tm);
        sporetType = tm.getSportType();
        this.addMouseListener(GameTableMouseListener.instance());
    }
    public int getWindowIndex() {
        return getModel().getScreenProperty().getSpankyWindowConfig().getWindowIndex();
    }
    // force ctrl key down when mouse click a row by setting toggle=true
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        super.changeSelection(rowIndex,columnIndex,true,extend);
    }
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        TableUtils.updateRowSelection(this,e);
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
         return isSoccer(rowViewIndex)? LineRenderer.soccerInstance():LineRenderer.instance();
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
    @Override
    public boolean isCellEditable(int row, int col) {
        return TableUtils.isCellEditable(this,row,col);
    }
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        return tableCellEditor;
    }
    @Override
    public void setValueAt(Object value,int row, int column) {
        if ( TableUtils.isNoteColumn(this,column)) {
            int rowModelIndex = convertRowIndexToModel(row);
            int gameId = getGame(rowModelIndex).getGame_id();
            GameNotes gameNotes = Config.instance().getGameNotes();
            gameNotes.addNote(gameId,String.valueOf(value));
        }
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
}
