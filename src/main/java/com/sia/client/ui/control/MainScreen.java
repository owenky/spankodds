package com.sia.client.ui.control;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.model.AbstractScreen;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameStatus;
import com.sia.client.model.LeagueFilter;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.ScreenGameModel;
import com.sia.client.model.ScreenProperty;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.MainGameTable;
import com.sia.client.ui.ScrollablePanel;
import com.sia.client.ui.TableUtils;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

import static com.sia.client.config.Utils.log;
import static java.lang.Boolean.parseBoolean;

public class MainScreen extends JPanel implements AbstractScreen<Game> {

    private final SportType sportType;
    private MainGameTable mainGameTable;
    private MainGameTableModel tableModel;
    private final ScreenProperty screenProperty;
    private ScreenGameModel screenGameModel;


    MainScreen(SportType sportType, SpankyWindowConfig spankyWindowConfig) {
        this.sportType = sportType;
        screenProperty = new ScreenProperty(sportType.getSportName(),spankyWindowConfig);
        final String name = sportType.getSportName();
        setName(name);
    }

    MainScreen(SportType sportType,SpankyWindowConfig spankyWindowConfig,boolean showheaders, boolean showseries, boolean showingame, boolean showadded, boolean showextra, boolean showprops) {
        this(sportType,spankyWindowConfig);
        screenProperty.setShowheaders(showheaders);
        sportType.setShowseries(showseries);
        sportType.setShowingame(showingame);
        sportType.setShowAdded(showadded);
        sportType.setShowExtra(showextra);
        sportType.setShowProps(showprops);
    }
    public void setCustomheaders(List<String> customheaders) {
        this.sportType.setCustomheaders(customheaders);
    }
    public void setClearTime(long clear) {
        screenProperty.setCleartime(clear);
        getDataModels().clearColors();
    }

    public synchronized MainGameTableModel getDataModels() {
        if (null == tableModel) {
            tableModel = buildModel();
        }
        return tableModel;
    }

    private MainGameTableModel buildModel() {

        screenGameModel = new ScreenGameModel(screenProperty,sportType);
        screenGameModel.build();
        MainGameTableModel model = new MainGameTableModel(sportType, screenProperty,screenGameModel.getAllTableColumns());

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
//        AppController.addDataModels(model);
        return model;
    }

    private LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
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

    public void createMe(JLabel loadlabel) {
        setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);
        add(loadlabel);
        enrichSportType();
        drawIt();
        log("done drawing");
    }
    private void drawIt() {
        ScrollablePanel tablePanel = new ScrollablePanel();
        tablePanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
        //changed this to stretch for Vertical Scroll Bar to appear if frame is resized and data can not fit in viewport
        tablePanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);

        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

        removeAll();
        revalidate();
        JComponent mainTableContainer = makeMainTableScrollPane(getColumnCustomizableTable());
        add(mainTableContainer, BorderLayout.CENTER);
    }
    public void setShowIngame(boolean b) {
        sportType.setShowingame(b);
    }

    private JComponent makeMainTableScrollPane(MainGameTable table) {
        return TableUtils.configTableLockColumns(table, AppController.getNumFixedCols());
    }
    private void enrichSportType() {
        String userPrefStr = sportType.getUserPerf();
        if ( null != userPrefStr) { // predefined sport type
            String[] prefs = userPrefStr.split("\\|");
            String[] tmp = {};
            boolean all = false;
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                sportType.setTimeSort(true);
            }

            try {
                if (prefs.length > 2) {
                    tmp = prefs[2].split(",");
                    if (tmp[0].equalsIgnoreCase(sportType.getSportName())) {
                        all = true;
                    }
                    setShowProperties(prefs);
                }

            } catch (Exception ex) {
                log(ex);
            }
            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
        } else { // customized sport type
            sportType.setComingDays(-1);
            sportType.setLeagueFilter(null);
        }
    }
    private void setShowProperties(String[] prefs) {
        screenProperty.setShowheaders(parseBoolean(prefs[3]));
        sportType.setShowseries(parseBoolean(prefs[4]));
        sportType.setShowingame(parseBoolean(prefs[5]));
        sportType.setShowAdded(parseBoolean(prefs[6]));
        sportType.setShowExtra(parseBoolean(prefs[7]));
        sportType.setShowProps(parseBoolean(prefs[8]));
    }

    private static MainGameTable createMainGameTable(MainGameTableModel model, String name) {
        MainGameTable table = new MainGameTable(model);
        table.setIntercellSpacing(new Dimension(4, 2));
        table.setName(name);
        JTableHeader tableHeader = table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        model.addTableSectionListener(() -> {
            table.setToConfigHeaderRow(true);
        });
        return table;
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
        screenProperty.setShowheaders(b);
    }

    public void adjustcols() {
        getColumnCustomizableTable().adjustColumns();
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
            mainGameTable.getModel().setDetroyed(true);
            mainGameTable = null;
        }
        tableModel = null;
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
        if (null == mainGameTable) {
            mainGameTable = createMainGameTable(getDataModels(), getName());
        }
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
