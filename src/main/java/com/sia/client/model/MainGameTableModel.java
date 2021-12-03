package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.TableUtils;

import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

import static com.sia.client.config.SiaConst.StageGroupAnchorOffset;
import static com.sia.client.config.Utils.log;

public class MainGameTableModel extends ColumnCustomizableDataModel<Game> {

    private final SportType sportType;
    private static final Set<String> stageStrs = new HashSet<>();
    private final Map<String,Integer> customizedTabGameGroupHeaderIndex = new HashMap<>();

    static {
        stageStrs.add(SiaConst.FinalStr);
        stageStrs.add(SiaConst.InProgresStr);
        stageStrs.add(SiaConst.InGamePricesStr);
        stageStrs.add(SiaConst.SeriesPricesStr);
    }
    public MainGameTableModel(SportType sportType,ScreenProperty screenProperty,Vector<TableColumn> allColumns) {
        super(screenProperty,allColumns);
        this.sportType = sportType;
        List<String> customerizedGameGroupHeader = sportType.getCustomheaders();
        if ( 0 <customerizedGameGroupHeader.size()) {
            int offset = StageGroupAnchorOffset-1000;
            for(String header: customerizedGameGroupHeader) {
                this.customizedTabGameGroupHeaderIndex.put(header,offset++);
            }
            //add GameStatus
            for(GameStatus gs: GameStatus.values()) {
                this.customizedTabGameGroupHeaderIndex.put(gs.getGroupHeader().getGameGroupHeaderStr(),gs.getGroupHeader().getAnchorPos());
            }
        }
    }
    @Override
    protected Comparator<TableSection<Game>> getTableSectionComparator() {
        final GameGroupDateSorter gameGroupDateSorter = new GameGroupDateSorter();
        final GameGroupLeagueSorter gameGroupLeagueSorter = new GameGroupLeagueSorter();
        return (l1,l2)-> {
            int result;
            if ( 0 == customizedTabGameGroupHeaderIndex.size()) {
                result = gameGroupDateSorter.compare(l1.getGameGroupHeader(), l2.getGameGroupHeader());
                if (0 == result) {
                    result = gameGroupLeagueSorter.compare(l1.getGameGroupHeader(), l2.getGameGroupHeader());
                }
            } else {
                Integer index1 = customizedTabGameGroupHeaderIndex.get(l1.getGameGroupHeader().getGameGroupHeaderStr());
                Integer index2 = customizedTabGameGroupHeaderIndex.get(l2.getGameGroupHeader().getGameGroupHeaderStr());
                index1 = null==index1?0:index1;
                index2 = null==index2?0:index2;
                result = index1-index2;
            }
            return result;
        };
    }
    @Override
    protected Comparator<Game> getGameComparator() {
        return getScreenProperty().getSpankyWindowConfig().getGameComparator();
    }
    public void copyTo(Collection<LinesTableData> destCollection) {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> ltd : gameLines) {
            destCollection.add((LinesTableData)ltd);
        }

    }
    public void addGameToGameGroup(GameGroupHeader gameGroupHeader,Game game, Function<GameGroupHeader,LinesTableData> function) {
        Utils.checkAndRunInEDT(()-> {
            LinesTableData ltd = computeIfNeeded(gameGroupHeader,game,function);
            if ( null != ltd) {
                int rowIndex = ltd.getRowIndex(game.getGame_id());
                if ( 0 <= rowIndex) {
                    updateRow(ltd,rowIndex);
                } else {
                    //check if this game is in other table section, if yes, then do move, else do add -- 2021-11-07
                    TableSection<Game> oldTableSection = this.findTableSectionByGameid(game.getGame_id());
                    if ( null != oldTableSection) {
                        this.moveGameFromSourceToTarget(oldTableSection,game,gameGroupHeader);
                    } else {
                        addGameToTableSection(ltd, game);
                    }
                }
            }
        });
    }
    private LinesTableData computeIfNeeded(GameGroupHeader gameGroupHeader, Game game, Function<GameGroupHeader,LinesTableData> function) {
        LinesTableData ltd = findTableSectionByHeaderValue(gameGroupHeader);
        if ( null == ltd) {
            if ( GameUtils.isGameNear(game)) {
                log("Creating NEW game group "+gameGroupHeader);
                ltd = function.apply(gameGroupHeader);
                if ( null != ltd) {
                    this.addGameLine(ltd);
                } else {
                    log("Warning: Can't create LinesTableData for gameGroupHeader="+gameGroupHeader+" for game "+GameUtils.getGameDebugInfo(game));
                }
            } else {
                log("Game NOT added to group "+gameGroupHeader.getGameGroupHeaderStr()+" before it is NOT near! "+GameUtils.getGameDebugInfo(game));
            }
        }
        return ltd;
    }
    public void clearColors() {
        List<TableSection<Game>> gameLines = getTableSections();
        for (TableSection<Game> linesTableData : gameLines) {
            ((LinesTableData)linesTableData).clearColors();
        }
        TableUtils.processTableModelEvent(this);
    }
    public SportType getSportType() {
        return this.sportType;
    }
    public int getGameId(int rowModelIndex) {
       return getRowKey(rowModelIndex);
    }
    @Override
    public LinesTableData findTableSectionByHeaderValue(GameGroupHeader gameGroupHeader) {
        LinesTableData rtn = (LinesTableData)super.findTableSectionByHeaderValue(gameGroupHeader);
        if ( null == rtn) {
            rtn = findOrCreateStageSectionByHeaderValue(gameGroupHeader);
        }
        return rtn;
    }
    public final boolean containsGroupHeader(GameGroupHeader gameGroupHeader) {
        return null != super.findTableSectionByHeaderValue(gameGroupHeader);
    }

    /**
     * return table section representing league of the game
     * the game itself might not be in that section if it is in stage sections such as Final, Halftime, In Progress etc... -- 2021-10-26
     */
    public LinesTableData findLeagueSection(Game g) {
        GameGroupHeader gameGroupHeader = GameUtils.createGameGroupHeader(g);
        if (null != gameGroupHeader ) {
            return findTableSectionByHeaderValue(gameGroupHeader);
        } else {
            return null;
        }
    }
    private LinesTableData findOrCreateStageSectionByHeaderValue(GameGroupHeader gameGroupHeader) {
        TableSection<Game> ltd;
        ltd = createStageSection(gameGroupHeader);
        return (LinesTableData)ltd;
    }
    private LinesTableData createStageSection(GameGroupHeader gameGroupHeader) {

        List<TableSection<Game>> sections = getTableSections();
        if ( null == sections || 0 == sections.size()) {
            return null;
        }

        String header = gameGroupHeader.getGameGroupHeaderStr();
        LinesTableData rtn;
        if ( stageStrs.contains(header)) {
            LinesTableData section0 = (LinesTableData)sections.get(0);
            rtn = new LinesTableData(sportType,getScreenProperty(), new Vector<>(),gameGroupHeader,getAllColumns());
            sections.add(rtn);
        } else {
            rtn = null;
        }
        return rtn;
    }
}
