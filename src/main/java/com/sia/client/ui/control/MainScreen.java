package com.sia.client.ui.control;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.model.AbstractScreen;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameStatus;
import com.sia.client.model.Games;
import com.sia.client.model.LeagueFilter;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LineRenderer;
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
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;

import static com.sia.client.config.Utils.log;
import static java.lang.Boolean.parseBoolean;

public class MainScreen extends JPanel implements AbstractScreen<Game> {

    private final SportType sportType;
    private final Vector<TableColumn> allColumns = new Vector<>();
    public int currentmaxlength = 0;
    public boolean timesort = false;
    public boolean shortteam = false;
    public boolean opener = false;
    public boolean last = false;
    public String display = "default";
    public int period = 0;
    public long cleartime;
    public boolean showheaders = true;
    private MainGameTable mainGameTable;
    private MainGameTableModel tableModel;
    private final int windowIndex;
    private final Map<GameGroupHeader, LinesTableData> headerMap = new HashMap<>();


    MainScreen(SportType sportType,int windowIndex) {
        this.windowIndex = windowIndex;
        cleartime = new java.util.Date().getTime();
        this.sportType = sportType;
        final String name = sportType.getSportName();
        setName(name);
    }

    MainScreen(SportType sportType,int windowIndex,boolean showheaders, boolean showseries, boolean showingame, boolean showadded, boolean showextra, boolean showprops) {
        this(sportType,windowIndex);
        this.showheaders = showheaders;
        sportType.setShowseries(showseries);
        sportType.setShowingame(showingame);
        sportType.setShowAdded(showadded);
        sportType.setShowExtra(showextra);
        sportType.setShowProps(showprops);
    }
    public int getWindowIndex() {
        return windowIndex;
    }
    public void setCustomheaders(List<String> customheaders) {
        this.sportType.setCustomheaders(customheaders);
    }
    public void setClearTime(long clear) {
        cleartime = clear;
        getDataModels().clearColors();
    }

    public synchronized MainGameTableModel getDataModels() {
        if (null == tableModel) {
            tableModel = buildModel();
        }
        return tableModel;
    }

    private MainGameTableModel buildModel() {

        MainGameTableModel model = new MainGameTableModel(sportType, windowIndex,allColumns);
        Vector<Bookie> newBookiesVec = AppController.getBookiesVec();
        List<Bookie> hiddencols = AppController.getHiddenCols();
        allColumns.clear();
        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = newBookiesVec.get(k);

            if (hiddencols.contains(b)) {
                continue;
            }
            TableColumn column;

            column = new TableColumn(k, 30, new LineRenderer(), null);

            column.setHeaderValue(b.getShortname());
            column.setIdentifier(b.getBookie_id());
            if (b.getBookie_id() == 990) {
                column.setPreferredWidth(60);
            } else if (b.getBookie_id() == 994) {
                column.setPreferredWidth(80);
            } else if (b.getBookie_id() == 991) {
                column.setPreferredWidth(40);
            } else if (b.getBookie_id() == 992) {
                column.setPreferredWidth(45);
            } else if (b.getBookie_id() == 993) {
                if (shortteam) {
                    column.setPreferredWidth(30);
                } else {
                    column.setPreferredWidth(currentmaxlength * 7);
                }

            } else if (b.getBookie_id() > 1000) {
                column.setMinWidth(10);
                column.setPreferredWidth(65);
            } else {
                column.setMinWidth(10);
                column.setPreferredWidth(30);
            }
            allColumns.add(column);
        }
        log("gamergroup headers start..." + new java.util.Date());

        headerMap.values().forEach(tableSection->model.addGameLine(model.getTableSections().size(),tableSection,false));
        headerMap.clear();
        //add stage sections if absent
        for( GameStatus status: GameStatus.values() ) {
            if ( ! model.containsGroupHeader(status.getGroupHeader())) {
                LinesTableData tableSection = createLinesTableData(new Vector<>(),status.getGroupHeader());
                model.addGameLine(model.getTableSections().size(),tableSection,false);
            }
        }
        model.buildIndexMappingCache(true);
        AppController.addDataModels(model);

        return model;
    }

    private LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType, display, period, cleartime, newgamegroupvec, timesort, shortteam, opener, last, gameGroupHeader, allColumns);
        if (sportType.equals(SportType.Soccer)) {
            tableSection.setRowHeight(SiaConst.SoccerRowheight);
        }
        return tableSection;
    }

    public void clearColors() {
        getDataModels().clearColors();
    }

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

    public void createMe(String display, int period, boolean timesort, boolean shortteam, boolean opener, boolean last, JLabel loadlabel) {
        setLayout(new BorderLayout(0, 0));
        // add progress
        this.setOpaque(true);

        add(loadlabel);

        this.display = display;
        this.period = period;
        this.timesort = timesort;
        this.shortteam = shortteam;
        this.opener = opener;
        this.last = last;
        createData();
        drawIt();
        log("done drawing");
    }

    public void createData() {
        headerMap.clear();
        int maxlength;
        currentmaxlength = 0;

        Games allgames = AppController.getGamesVec();
        if (sportType.isPredifined()) {
            enrichSportType();
        } else {
            sportType.setComingDays(-1);
            sportType.setLeagueFilter(null);
        }
        log("timesort?=" + timesort + "..allgames size=" + allgames.size());
        for (int k = 0; k < allgames.size(); k++) {
            Game g = allgames.getByIndex(k);
            if (!sportType.shouldSelect(g)) {
                continue;
            }
            if (shortteam) {
                maxlength = calcmaxlength(g.getShortvisitorteam(), g.getShorthometeam());
            } else {
                maxlength = calcmaxlength(g.getVisitorteam(), g.getHometeam());
            }
            if (maxlength > currentmaxlength) {
                currentmaxlength = maxlength;
            }

            if (null == g.getStatus()) {
                g.setStatus("");
            }
            if (null == g.getTimeremaining()) {
                g.setTimeremaining("");
            }

            GameGroupHeader gameGroupHeader;
            GameStatus status = GameStatus.getGameStatus(g);
            if ( null != status) {
                gameGroupHeader = status.getGroupHeader();
            } else {
                gameGroupHeader = GameUtils.createGameGroupHeader(g);
            }

            LinesTableData tableSection = headerMap.computeIfAbsent(gameGroupHeader,(key)-> createLinesTableData(new Vector<>(), key));
            tableSection.addOnInit(g);
        }
        long ct = AppController.getClearAllTime();
        if (ct > cleartime) {
            cleartime = ct;
        }
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

    private void enrichSportType() {
        String[] prefs = sportType.getUserPerf().split("\\|");
        String[] tmp = {};
        boolean all = false;
        sportType.setComingDays(Integer.parseInt(prefs[1]));
        if (parseBoolean(prefs[0])) {
            this.timesort = true;
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
    }

    public int calcmaxlength(String s1, String s2) {
        return Math.max(s1.length(), s2.length());
    }

    public boolean isShowIngame() {
        return sportType.isShowingame();
    }

    public void setShowIngame(boolean b) {
        sportType.setShowingame(b);
    }

    private JComponent makeMainTableScrollPane(MainGameTable table) {
        return TableUtils.configTableLockColumns(table, AppController.getNumFixedCols());
    }

    private void setShowProperties(String[] prefs) {
        this.showheaders = (parseBoolean(prefs[3]));
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

    public boolean isShowAdded() {
        return sportType.isShowAdded();
    }

    public void setShowAdded(boolean b) {
        sportType.setShowAdded(b);
    }

    public boolean isShowExtra() {
        return sportType.isShowExtra();
    }

    public void setShowExtra(boolean b) {
        sportType.setShowExtra(b);
    }

    public boolean isShowProps() {
        return sportType.isShowProps();
    }

    public void setShowProps(boolean b) {
        sportType.setShowProps(b);
    }

    public void setShowSeries(boolean b) {
        sportType.setShowseries(b);
    }

    public boolean isShowHeaders() {
        return showheaders;
    }

    public void setShowHeaders(boolean b) {
        showheaders = b;
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
        mainGameTable = null;
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
