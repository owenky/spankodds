package com.sia.client.ui.control;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.model.AbstractScreen;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameStatus;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.ScreenGameModel;
import com.sia.client.model.ScreenProperty;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.model.SportType;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.MainGameTable;
import com.sia.client.ui.MainScreenLoader;

import javax.swing.JPanel;
import javax.swing.table.JTableHeader;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

import static com.sia.client.config.Utils.log;

public class MainScreen extends JPanel implements AbstractScreen<Game> {

    private final SportType sportType;
    private MainGameTable mainGameTable;
    private final ScreenProperty screenProperty;
    private final ScreenGameModel screenGameModel;


    MainScreen(SportType sportType, SpankyWindowConfig spankyWindowConfig) {
        this.sportType = sportType;
        screenProperty = new ScreenProperty(sportType.getSportName(),spankyWindowConfig);
        final String name = sportType.getSportName();
        setName(name);
        screenGameModel = new ScreenGameModel(screenProperty,sportType);
    }

    MainScreen(SportType sportType,SpankyWindowConfig spankyWindowConfig,boolean showheaders) {
        this(sportType,spankyWindowConfig);
    }
    public int getWindowIndex() {
        return screenProperty.getSpankyWindowConfig().getWindowIndex();
    }
    public MainGameTableModel buildModel() {

        screenGameModel.build();
        MainGameTableModel model = new MainGameTableModel(sportType, screenProperty,screenGameModel.getAllTableColumns());
        model.buildCustomTabGameGroupHeader();

        Collection<LinesTableData> tableSections = screenGameModel.getTableSections();
        log("MainScreen: adding "+tableSections.size()+" of game group to data model");

        tableSections.forEach(tableSection->model.addGameLine(model.getTableSections().size(),tableSection,false));
        //add stage sections if absent
        for( GameStatus status: GameStatus.values() ) {
            if ( ! model.containsGroupHeader(status.getGroupHeader())) {
                LinesTableData tableSection = createLinesTableData(new Vector<>(),status.getGroupHeader());
                model.addGameLine(model.getTableSections().size(),tableSection,false);
            }
        }
        model.buildIndexMappingCache(true);
        return model;
    }
    public void setCustomheaders(List<String> customheaders) {
        this.sportType.setCustomheaders(customheaders);
    }
    public void setClearTime(long clear) {
        screenProperty.setCleartime(clear);
        getDataModels().clearColors();
    }

    public synchronized MainGameTableModel getDataModels() {
       return null == mainGameTable? null: mainGameTable.getModel();
    }
    public LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType,screenProperty, newgamegroupvec, gameGroupHeader, screenGameModel.getAllTableColumns());
        if (sportType.equals(SportType.Soccer)) {
            tableSection.setRowHeight(SiaConst.SoccerRowheight);
        }
        return tableSection;
    }
    @Override
    public void addGame(Game g) { // only gets called when adding new game into system
        Function<GameGroupHeader, LinesTableData> function = (ggh) -> {
            if (isPreDefinedSport()) {
                return this.createLinesTableData(new Vector(), ggh);
            } else {
                return null;
            }
        };

        GameGroupHeader gameGroupHeader;
        GameStatus gameStatus = GameStatus.getGameStatus(g);
        if (null == gameStatus) {
            gameGroupHeader = GameUtils.createGameGroupHeader(g);
        } else {
            gameGroupHeader = gameStatus.getGroupHeader();
        }
        getDataModels().addGameToGameGroup(gameGroupHeader, g, function);

    }

    public boolean isPreDefinedSport() {
        return this.sportType.isPredifined();
    }

    public void moveGameToThisHeader(Game g, GameGroupHeader header) {
        getDataModels().moveGameToThisHeader(g, header);
    }
    public void createMe(Runnable listener) {
        sportType.enrichSportType();
        MainScreenLoader loader = new MainScreenLoader(this);
        loader.setListener(listener);
        loader.load();
    }
    public void setShowIngame(boolean b) {
        sportType.setShowingame(b);
    }

    public void createColumnCustomizableTable(MainGameTableModel model) {

        mainGameTable = new MainGameTable(model);
        mainGameTable.setIntercellSpacing(new Dimension(4, 2));
        mainGameTable.setName(getName());
        JTableHeader tableHeader = mainGameTable.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        model.addTableSectionListener(() -> {
            mainGameTable.setToConfigHeaderRow(true);
        });
    }
    public void setShowAdded(boolean b) {
        sportType.setShowAdded(b);
    }

    public void setShowExtra(boolean b) {
        sportType.setShowExtra(b);
    }

    public void setShowProps(boolean b) {
        sportType.setShowProps(b);
    }

    public void setShowSeries(boolean b) {
        sportType.setShowseries(b);
    }

    public void setShowHeaders(boolean b) {
        sportType.setShowheaders(b);
    }

    public void adjustcols() {
        getColumnCustomizableTable().adjustColumns();
    }

    public boolean isTableReday() {
        return null != mainGameTable;
    }
    public boolean containsGame(Game g) {
        boolean containing;
        if (getSportType().isPredifined() && getSportType().isMyType(g)) {
            //this is faster than getDataModels().containsGame(g.getGame_id());
            containing = true;
        } else {
            containing = getDataModels().containsGame(g.getGame_id());
        }
        return containing;
    }
    @Override
    public void destroyMe() {
        removeAll();
        if ( null != mainGameTable) {
            mainGameTable.getColumnAdjusterManager().removeFromScheduler();
//            mainGameTable.getModel().setDetroyed(true);
            mainGameTable.getModel().destroy();
            mainGameTable = null;
        }
        log("destroyed mainscreen!!!!");
    }

    @Override
    public boolean shouldAddToScreen(Game g) {

        if (!this.isShowing()) {
            return false;
        }
        return this.sportType.shouldSelect(g);
    }

    @Override
    public synchronized MainGameTable getColumnCustomizableTable() {
        return mainGameTable;
    }

    @Override
    public void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet) {
        log("MainScreen: " + getSportType().getSportName() + "-- Removing game ids " + gameIdRemovedSet);
        AbstractScreen.super.removeGamesAndCleanup(gameIdRemovedSet);
    }

    public SportType getSportType() {
        return this.sportType;
    }

}
