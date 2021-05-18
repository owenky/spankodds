package com.sia.client.model;

import com.sia.client.ui.LinesTableData;

import java.util.Collection;
import java.util.List;

import static com.sia.client.config.Utils.log;

public class MainGameTableModel extends ColumnCustomizableDataModel<Game> {

//    private final List<LinesTableData> gameLines = new ArrayList<>();

    public MainGameTableModel() {

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
    @Override
    public LinesTableData findTableSectionByHeaderValue(String gameGroupHeader) {
        LinesTableData rtn = null;
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> ltd : gameLines) {
            if (gameGroupHeader.equals(ltd.getGameGroupHeader())) {
                rtn = (LinesTableData)ltd;
                break;
            }
        }
        if ( null == rtn) {
            rtn = findStageSectionByHeaderValue(gameGroupHeader);
        }
        return rtn;
    }
    public void removeGames(String[] gameids) {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            try {
                ((LinesTableData)linesTableData).removeGameIds(gameids);
            } catch (Exception ex) {
                log(ex);
            }
        }
    }
    public void makeDataModelsVisible(boolean b) {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).setInView(b);
        }
    }
    public void clearColors() {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).clearColors();
        }
    }
    public LinesTableData checktofire(int gameId) {
        List<TableSection<Game>> gameLines = getTableSections();
        LinesTableData rtn = null;
        for (final TableSection<Game> ltd : gameLines) {
            boolean status = ltd.checktofire(gameId);
            if (status) {
                rtn  = (LinesTableData)ltd;
                break;
            }
        }
        return rtn;
    }
    public int getGameId(int rowModelIndex) {
       return getRowKey(rowModelIndex);
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
