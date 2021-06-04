package com.sia.client.model;

import com.sia.client.ui.LinesTableData;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class MainGameTableModel extends ColumnCustomizableDataModel<Game> {

//    private final List<LinesTableData> gameLines = new ArrayList<>();

    public MainGameTableModel(Vector<TableColumn> allColumns) {
        super(allColumns);
    }
    public void copyTo(Collection<LinesTableData> destCollection) {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> ltd : gameLines) {
            destCollection.add((LinesTableData)ltd);
        }

    }
    //refactored from MainScreen::addGame(Game, boolean)
    public void addGameToGameGroup(String gameGroupHeader,Game game,boolean paint) {
        LinesTableData ltd = findTableSectionByHeaderValue(gameGroupHeader);
        if ( null != ltd) {
            ltd.addGame(game, paint);
        }

    }
    public void removeGames(String[] gameidstr) {
        Integer [] gameIds = new Integer[gameidstr.length];
        for(int i=0;i<gameidstr.length;i++) {
            gameIds[i] = Integer.parseInt(gameidstr[i]);
        }
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            try {
                ((LinesTableData)linesTableData).removeGameIds(gameIds);
            } catch (Exception ex) {
                log(ex);
            }
        }
        this.fireTableChanged(new TableModelEvent(this,0,Integer.MAX_VALUE,ALL_COLUMNS,TableModelEvent.UPDATE));
    }
    public void clearColors() {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).clearColors();
        }
        fireTableChanged(new TableModelEvent(this,0,Integer.MAX_VALUE,0,TableModelEvent.UPDATE));
    }
    public int getGameId(int rowModelIndex) {
       return getRowKey(rowModelIndex);
    }
    @Override
    public LinesTableData findTableSectionByHeaderValue(String gameGroupHeader) {
        LinesTableData rtn = (LinesTableData)super.findTableSectionByHeaderValue(gameGroupHeader);
        if ( null == rtn) {
            rtn = findStageSectionByHeaderValue(gameGroupHeader);
        }
        return rtn;
    }
    private LinesTableData findStageSectionByHeaderValue(String header) {
        TableSection<Game> ltd;
        List<TableSection<Game>> gameLines = getTableSections();
        if (header.equalsIgnoreCase("In Progress")) {
            ltd = gameLines.get(gameLines.size() - 4);
        } else if (header.equalsIgnoreCase("Soccer In Progress")) {
            ltd = gameLines.get(gameLines.size() - 3);
        } else if (header.equalsIgnoreCase("FINAL")) {
            ltd = gameLines.get(gameLines.size() - 2);
        } else if (header.equalsIgnoreCase("Soccer FINAL")) {
            ltd = gameLines.get(gameLines.size() - 1);
        } else if (header.equalsIgnoreCase("Halftime")) {
            ltd = gameLines.get(0);
        } else if (header.equalsIgnoreCase("Soccer Halftime")) {
            ltd = gameLines.get(1);
        } else {
            ltd = null;
        }
        return (LinesTableData)ltd;
    }
}
