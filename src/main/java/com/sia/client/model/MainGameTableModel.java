package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.TableUtils;

import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sia.client.config.Utils.log;

public class MainGameTableModel extends ColumnCustomizableDataModel<Game> {

    private final SportType sportType;
    //TODO
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Set<String> stageStrs = new HashSet<>();
    static {
        stageStrs.add(SiaConst.FinalStr);
        stageStrs.add(SiaConst.InProgresStr);
        stageStrs.add(SiaConst.InGamePricesStr);
//        stageStrs.add(SiaConst.SoccerInGamePricesStr);
        stageStrs.add(SiaConst.SeriesPricesStr);
//        stageStrs.add(SiaConst.SoccerSeriesPricesStr);
    }
    public MainGameTableModel(SportType sportType,Vector<TableColumn> allColumns) {
        super(allColumns);
        this.sportType = sportType;
        int cnt = counter.getAndAdd(1);
        if ( 0 ==cnt%2) {
            log("*******8 LinesTableData instance: "+cnt);
        }
    }
    public void copyTo(Collection<LinesTableData> destCollection) {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> ltd : gameLines) {
            destCollection.add((LinesTableData)ltd);
        }

    }
    //refactored from MainScreen::addGame(Game, boolean)
    public void addGameToGameGroup(String gameGroupHeader,Game game,boolean paint,Runnable callBackOnNotFound) {
        if ( game.isSeriesprice()) {
            if ( game.getLeague_id() == SiaConst.SoccerLeagueId) {
                gameGroupHeader = SiaConst.SoccerSeriesPricesStr;
            } else {
                gameGroupHeader = SiaConst.SeriesPricesStr;
            }
        }  else if ( game.isInGame2()) {
            if ( game.getLeague_id() == SiaConst.SoccerLeagueId) {
                gameGroupHeader = SiaConst.SoccerInGamePricesStr;
            } else {
                gameGroupHeader = SiaConst.InGamePricesStr;
            }
        }
        LinesTableData ltd = findTableSectionByHeaderValue(gameGroupHeader);
        if ( null != ltd) {
            ltd.addGame(game, paint);
        } else {
            //this method is called only when the game belong to this table, see conditioin of ms.parentOfGame(g)  in SportsTabPane::addGame()
            //need to re-draw screen when game group is not found in this table
 //TODO remove debug
SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
log("DEBUG::::::::::::::::::::::::, game not found in the table, gameid=" + game.getGame_id() + ", leagueId=" + game.getLeague_id() + ", sport=" + AppController.getSportByLeagueId(game.getSportIdentifyingLeagueId()).getSportname() +
        ", title=" + AppController.getSportByLeagueId(game.getSportIdentifyingLeagueId()).getLeaguename() + " " + sdf2.format(game.getGamedate())+", status=" + game.getStatus()
        + ", isSeriecPrice=" + game.isSeriesprice() + ", isInGame2=" + game.isInGame2());
//END Of DEBUG TODO
            if ( GameUtils.isGameNear(game)) {
                if ( ! game.isInStage()) {
                    callBackOnNotFound.run();
                    log("REFRESH main screen for this game id=" + game.getGame_id());
                } else {
                    //when game is in stage, there might not be a regular game header for this game, instead this game is in stage header (i.e. Final, Halftime, etc...) -- 2021-10-05
                    log("SKIP REFRESHing main screen for this game id="+game.getGame_id()+" because the game is in stage. status="+game.getStatus());
                }
            } else {
                log("SKIP REFRESHing main screen for this game id="+game.getGame_id()+" because the game is not near.");
            }
        }

    }
    public void clearColors() {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).clearColors();
        }
        TableUtils.fireTableModelChanged(this);
    }
    public SportType getSportType() {
        return this.sportType;
    }
    public int getGameId(int rowModelIndex) {
       return getRowKey(rowModelIndex);
    }
    @Override
    public LinesTableData findTableSectionByHeaderValue(String gameGroupHeader) {
        LinesTableData rtn = (LinesTableData)super.findTableSectionByHeaderValue(gameGroupHeader);
        if ( null == rtn) {
            rtn = findOrCreateStageSectionByHeaderValue(gameGroupHeader);
        }
        return rtn;
    }
    public boolean containsGroupHeader(String gameGroupHeader) {
        return null != super.findTableSectionByHeaderValue(gameGroupHeader);
    }
    private LinesTableData findOrCreateStageSectionByHeaderValue(String header) {
        TableSection<Game> ltd;
        ltd = createStageSection(header);
        return (LinesTableData)ltd;
    }
    private LinesTableData createStageSection(String header) {

        List<TableSection<Game>> sections = getTableSections();
        if ( null == header || null == sections || 0 == sections.size()) {
            return null;
        }

        header = GameUtils.normalizeGameHeader(header);
        LinesTableData rtn;
        if ( stageStrs.contains(header)) {
            LinesTableData section0 = (LinesTableData)sections.get(0);
            rtn = new LinesTableData(sportType,section0.getDisplayType(), section0.getPeriodType(), section0.getClearTime(), new Vector<>(), section0.getTimesort(), section0.getShortteam(), section0.getOpener()
                    , section0.getLast(),header,getAllColumns());
            sections.add(rtn);
        } else {
            rtn = null;
        }
        return rtn;
    }
}
