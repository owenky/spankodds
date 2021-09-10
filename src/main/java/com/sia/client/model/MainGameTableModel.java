package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.TableUtils;

import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class MainGameTableModel extends ColumnCustomizableDataModel<Game> {

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
    public void addGameToGameGroup(String gameGroupHeader,Game game,boolean paint,Runnable callBackOnNotFound) {
        LinesTableData ltd = findTableSectionByHeaderValue(gameGroupHeader);
        if ( null != ltd) {
            ltd.addGame(game, paint);
        } else {
            //this method is called only when the game belong to this table, see conditioin of ms.parentOfGame(g)  in SportsTabPane::addGame()
            //need to re-draw screen when game group is not found in this table
            callBackOnNotFound.run();
//DEBUG...
SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
log("DEBUG::::::::::::::::::::::::, game not found in the table, gameid="+game.getGame_id()+", leagueId="+game.getLeague_id()+", sport="+ AppController.getSport(game.getSportIdentifyingLeagueId()).getSportname()+
        ", title="+AppController.getSport(game.getSportIdentifyingLeagueId()).getLeaguename() + " " + sdf2.format(game.getGamedate()));
//END OF DEBUG
        }

    }
    public void clearColors() {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).clearColors();
        }
        TableUtils.fireTableModelChanged(this);
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
//        if (header.equalsIgnoreCase(SiaConst.InProgresStr)) {
//            ltd = gameLines.get(gameLines.size() - 4);
//        } else if (header.equalsIgnoreCase("Soccer "+SiaConst.InProgresStr)) {
//            ltd = gameLines.get(gameLines.size() - 3);
//        } else if (header.equalsIgnoreCase("FINAL")) {
//            ltd = gameLines.get(gameLines.size() - 2);
//        } else if (header.equalsIgnoreCase("Soccer FINAL")) {
//            ltd = gameLines.get(gameLines.size() - 1);
//        } else if (header.equalsIgnoreCase("Halftime")) {
//            ltd = gameLines.get(0);
//        } else if (header.equalsIgnoreCase("Soccer Halftime")) {
//            ltd = gameLines.get(1);
//        } else {
//            ltd = null;
//        }
        //the above is no longer correct -- 06/12/2021
        if (header.equalsIgnoreCase("Halftime")) {
            ltd = gameLines.get(0);
        } else if (header.equalsIgnoreCase("Soccer Halftime")) {
            ltd = gameLines.get(1);
        } else {
            ltd = createStageSection(header);
        }
        return (LinesTableData)ltd;
    }
    private LinesTableData createStageSection(String header) {

        List<TableSection<Game>> sections = getTableSections();
        if ( null == header || null == sections || 0 == sections.size()) {
            return null;
        }

        header = normalizeStageString(header);
        LinesTableData rtn;
        if ( header.contains(SiaConst.FinalStr) || header.contains(SiaConst.InProgresStr) ) {
            LinesTableData section0 = (LinesTableData)sections.get(0);
            rtn = new LinesTableData(section0.getDisplayType(), section0.getPeriodType(), section0.getClearTime(), new Vector<>(), section0.getTimesort(), section0.getShortteam(), section0.getOpener()
                    , section0.getLast(),header,getAllColumns());
            sections.add(rtn);
        } else {
            rtn = null;
        }
        return rtn;
    }
    private static String normalizeStageString(String header) {
        header = Utils.replaceIgnoreCase(header,SiaConst.FinalStr);
        header = Utils.replaceIgnoreCase(header,SiaConst.InProgresStr);
        return header;
    }
}
