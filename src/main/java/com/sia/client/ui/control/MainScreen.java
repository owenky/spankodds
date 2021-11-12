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
import com.sia.client.model.Sport;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private List<String> customheaders = new ArrayList<>();
    private List<GameGroupHeader> gamegroupheaders = new ArrayList<>();
    private Vector vecofgamegroups = new Vector();
    private Vector inprogressgames = new Vector();
    private Vector halftimegames = new Vector();
    private Vector finalgames = new Vector();
    private Vector inprogressgamessoccer = new Vector();
    private Vector halftimegamessoccer = new Vector();
    private Vector finalgamessoccer = new Vector();
    private Vector seriesgames = new Vector();
    private Vector ingamegames = new Vector();
    private Vector seriesgamessoccer = new Vector();
    private Vector ingamegamessoccer = new Vector();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
    private MainGameTable mainGameTable;
    private MainGameTableModel tableModel;

    MainScreen(SportType sportType, List<String> customheaders) {
        this(sportType);
        this.customheaders = customheaders;
    }

    MainScreen(SportType sportType) {
        cleartime = new java.util.Date().getTime();
        this.sportType = sportType;
        final String name = sportType.getSportName();
        setName(name);
    }

    MainScreen(SportType sportType, List<String> customheaders, boolean showheaders, boolean showseries, boolean showingame, boolean showadded, boolean showextra, boolean showprops) {
        this(sportType);
        this.customheaders = GameUtils.convertLeagueIdHeaderToGameGroupHeaderStr(customheaders);
        this.showheaders = showheaders;
        sportType.setShowseries(showseries);
        sportType.setShowingame(showingame);
        sportType.setShowAdded(showadded);
        sportType.setShowExtra(showextra);
        sportType.setShowProps(showprops);
    }

    public void setClearTime(long clear) {
        cleartime = clear;
        getDataModels().clearColors();
    }

    public synchronized MainGameTableModel getDataModels() {
        if ( null == tableModel ) {
            tableModel = buildModel();
        }
        return tableModel;
    }

    private static MainGameTable createMainGameTable(MainGameTableModel model,String name) {
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

    public void clearColors() {
        getDataModels().clearColors();
    }
    @Override
    public void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet) {
        log("MainScreen: "+getSportType().getSportName()+"-- Removing game ids "+gameIdRemovedSet);
        AbstractScreen.super.removeGamesAndCleanup(gameIdRemovedSet);
    }
    public void addGame(Game g) { // only gets called when adding new game into system
        Function<GameGroupHeader,LinesTableData> function = (ggh) -> {
            if ( isPreDefinedSport() ) {
                return this.createLinesTableData(new Vector(),ggh);
            } else {
                return null;
            }
        };

        GameGroupHeader gameGroupHeader;
        GameStatus gameStatus = GameStatus.find(g.getStatus());
        if (null == gameStatus) {
            gameGroupHeader = GameUtils.createGameGroupHeader(g);
        } else {
            gameGroupHeader = gameStatus.getGroupHeader();
        }
        getDataModels().addGameToGameGroup(gameGroupHeader, g, function);

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
        Vector gamegroupvec = new Vector();
        List<GameGroupHeader> gamegroupheadervec = new ArrayList<>();
        Vector gamegroupLeagueIDvec = new Vector();
        Vector currentvec = new Vector();


        Games allgames = new Games();


        int maxlength;
        currentmaxlength = 0;
        String[] prefs;

        Games allgamesforpref = AppController.getGamesVec();

        // added sorting code 5/6/2021
        try {
//            allgamesforpref.sort(new GameLeagueSorter().thenComparing(new GameDateSorter().thenComparing(new GameNumSorter())));
//            allgamesforpref.sort(new GameDateSorter());
//            Collections.sort(allgamesforpref, new GameLeagueSorter().thenComparing(new GameDateSorter().thenComparing(new GameNumSorter())));
//            Collections.sort(allgamesforpref, new GameDateSorter());

        } catch (Exception ex) {
            log("exception sorting " + ex);
            log(ex);
        }

        String name = getName();
        boolean all = false;
        String[] tmp = {};
        if (name.equalsIgnoreCase("football")) {
            String footballpref = AppController.getUser().getFootballPref();
            prefs = footballpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }

            try {
                if (prefs.length > 2) {
                    tmp = prefs[2].split(",");
                    if (tmp[0].equalsIgnoreCase("football")) {
                        all = true;
                    }
                    setShowProperties(prefs);
                }

            } catch (Exception ex) {
                log(ex);
            }
            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }


            }
        } else if (name.equalsIgnoreCase("basketball")) {
            String basketballpref = AppController.getUser().getBasketballPref();
            prefs = basketballpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("basketball")) {
                    all = true;
                }
                this.setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("baseball")) {
            String baseballpref = AppController.getUser().getBaseballPref();
            prefs = baseballpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("baseball")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }
            }
            log("size of all ames=" + allgames.size());
        } else if (name.equalsIgnoreCase("hockey")) {
            String hockeypref = AppController.getUser().getHockeyPref();
            prefs = hockeypref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("hockey")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("fighting")) {
            String fightingpref = AppController.getUser().getFightingPref();
            prefs = fightingpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("fighting")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase(SiaConst.SoccerStr)) {
            String soccerpref = AppController.getUser().getSoccerPref();
            prefs = soccerpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase(SiaConst.SoccerStr)) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("auto racing")) {
            String autoracingpref = AppController.getUser().getAutoracingPref();
            prefs = autoracingpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("auto racing")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("golf")) {
            String golfpref = AppController.getUser().getGolfPref();
            prefs = golfpref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("golf")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    //	log("yes"+set+"---");
                    allgames.add(tempGame);
                }
                setShowProperties(prefs);

            }
        } else if (name.equalsIgnoreCase("tennis")) {
            String tennispref = AppController.getUser().getTennisPref();
            prefs = tennispref.split("\\|");
            sportType.setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("tennis")) {
                    all = true;
                }
                setShowProperties(prefs);
            }

            sportType.setLeagueFilter(new LeagueFilter(tmp, all));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                if (sportType.shouldSelect(tempGame)) {
                    //log("yes"+set+"---");
                    allgames.add(tempGame);
                }

            }
        } else {
            allgames = allgamesforpref;
            sportType.setComingDays(-1);
            sportType.setLeagueFilter(null);
        }

        java.util.Date today = new java.util.Date();

        String lastdate = null;
        int lastleagueid = 0;

        log("timesort?=" + timesort + "..allgames size=" + allgames.size());

        allgames = transformGamesVecToCustomGamesVec(customheaders, allgames);


        for (int k = 0; k < allgames.size(); k++) {

            int gameid = -1;
            Game g = allgames.getByIndex(k);


            if (g == null) {
                log("skipping gameid=" + gameid + "...cuz of null game");
                continue;
            } else {
                gameid = g.getGame_id();
            }
            if (g.getGamedate() == null) {
                log("skipping gameid=" + gameid + "...cuz of null game date");
                continue;
            }

            String gamedate = sdf.format(g.getGamedate());
            String todaysgames = sdf.format(today);
            int leagueid = g.getLeague_id();
            Sport s = AppController.getSportByLeagueId(leagueid);

            if (s == null) {
                log("skipping " + leagueid + "...cuz of null sport");
                continue;
            }
            if (customheaders.size() > 0 || s.getSportname().equalsIgnoreCase(name) || (name.equalsIgnoreCase("Today") && gamedate.compareTo(todaysgames) <= 0)) {

                Sport s2;
                if (shortteam) {
                    maxlength = calcmaxlength(g.getShortvisitorteam(), g.getShorthometeam());

                } else {
                    maxlength = calcmaxlength(g.getVisitorteam(), g.getHometeam());
                }

                if (maxlength > currentmaxlength) {
                    currentmaxlength = maxlength;
                }

                if (leagueid == SiaConst.SoccerLeagueId) // soccer need to look at subleagueid
                {
                    leagueid = g.getSubleague_id();
                    s2 = AppController.getSportByLeagueId(leagueid);
                    if (null == s2) {
                        log("MainScreen: Can't find sport for leagueid " + leagueid + ", game id:" + gameid);
                        continue;
                    }

                } else {
                    s2 = s;
                }


                String description = g.getDescription();
                if (description == null || description.equalsIgnoreCase("null")) {
                    description = "";
                }

                if (g.getStatus() == null) {
                    g.setStatus("");
                }
                if (g.getTimeremaining() == null) {
                    g.setTimeremaining("");
                }

                if (g.isAddedgame() && !isShowAdded()) {
                    continue;
                }
                if (g.isExtragame() && !isShowExtra()) {
                    continue;
                }
                if (g.isForprop() && !isShowProps()) {
                    continue;
                }
                // here we are going to check if we should go on..
                if (customheaders.size() > 0) {
                    if (!customheaders.contains(s2.getLeaguename() + " " + sdf2.format(g.getGamedate())) && !customheaders.contains(leagueid + " " + sdf2.format(g.getGamedate()))) {
                        continue;
                    }
                }

                int id = s.getParentleague_id();

                if (g.isInFinal()) {
                    if (id == SiaConst.SoccerLeagueId) {
                        finalgamessoccer.add(g);
                    } else {
                        finalgames.add(g);
                    }

                } else if (g.isHalfTime() || g.isInProgress()) {
                    if (g.isHalfTime()) {
                        if (id == SiaConst.SoccerLeagueId) {
                            halftimegamessoccer.add(g);
                        } else {
                            halftimegames.add(g);
                        }

                    } else {
                        if (id == SiaConst.SoccerLeagueId) {
                            inprogressgamessoccer.add(g);
                        } else {
                            inprogressgames.add(g);
                        }


                    }
                } else if (g.isSeriesprice()) {
                    if (id == SiaConst.SoccerLeagueId) {
                        seriesgamessoccer.add(g);
                    } else {
                        seriesgames.add(g);
                    }


                } else if (g.isInGame2()) {
                    if (id == SiaConst.SoccerLeagueId) {
                        ingamegamessoccer.add(g);
                    } else {
                        ingamegames.add(g);
                    }

                } else if (lastdate == null) // new date
                {
                    gamegroupheadervec.add(GameUtils.createGameGroupHeader(s2.getNormalizedLeaguename(),g.getSubleague_id(),g.getLeague_id(),g.getGamedate()));
                    gamegroupLeagueIDvec.add(s2.getParentleague_id());
                    lastdate = gamedate;
                    lastleagueid = leagueid;
                    Vector v = new Vector();
                    gamegroupvec.add(v);
                    v.add(g);
                    currentvec = v;
                } else if (!lastdate.equals(gamedate) || lastleagueid != leagueid) // new date or new league!
                {
                    lastdate = gamedate;
                    lastleagueid = leagueid;
                    gamegroupheadervec.add(GameUtils.createGameGroupHeader(s2.getNormalizedLeaguename(),g.getSubleague_id(),g.getLeague_id(),g.getGamedate()));
                    gamegroupLeagueIDvec.add(s2.getParentleague_id());
                    Vector v2 = new Vector();
                    //v2.add(gameid);
                    v2.add(g);
                    gamegroupvec.add(v2);
                    currentvec = v2;
                } else // same date
                {
                    currentvec.add(g);

                }
            }
        }

        if (seriesgames.size() > 0 && sportType.isShowseries()) {
            gamegroupheadervec.add(GameStatus.SeriesPrice.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(seriesgames);
        }
        if (seriesgamessoccer.size() > 0 && sportType.isShowseries()) {
            gamegroupheadervec.add(GameStatus.SeriesPrice.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(seriesgamessoccer);
        }


        if (ingamegames.size() > 0 && isShowIngame()) {
            gamegroupheadervec.add(GameStatus.InGamePrices.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(ingamegames);
        }

        if (ingamegamessoccer.size() > 0 && isShowIngame()) {
            gamegroupheadervec.add(GameStatus.InGamePrices.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(ingamegamessoccer);
        }


        if (inprogressgames.size() > 0) {
            gamegroupheadervec.add(GameStatus.InProgress.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(inprogressgames);
        } else {
            gamegroupheadervec.add(GameStatus.InProgress.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(new Vector());
        }

        if (inprogressgamessoccer.size() > 0) {
            gamegroupheadervec.add(GameStatus.InProgress.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(inprogressgamessoccer);
        } else {
            gamegroupheadervec.add(GameStatus.InProgress.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(new Vector());
        }


        if (finalgames.size() > 0) {
            gamegroupheadervec.add(GameStatus.Final.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(finalgames);
        } else {
            gamegroupheadervec.add(GameStatus.Final.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(new Vector());
        }


        if (finalgamessoccer.size() > 0) {
            gamegroupheadervec.add(GameStatus.Final.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(finalgamessoccer);
        } else {
            gamegroupheadervec.add(GameStatus.Final.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.add(new Vector());
        }


        if (halftimegames.size() > 0) {
            gamegroupheadervec.add(0,GameStatus.HalfTime.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.insertElementAt(halftimegames, 0);
        } else {
            gamegroupheadervec.add(0,GameStatus.HalfTime.getGroupHeader());
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.insertElementAt(new Vector(), 0);
        }

        if (halftimegamessoccer.size() > 0) {
            gamegroupheadervec.add(1,GameStatus.HalfTime.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.insertElementAt(halftimegamessoccer, 1);
        } else {
            gamegroupheadervec.add(1,GameStatus.HalfTime.getGroupHeader());
            gamegroupLeagueIDvec.add(SiaConst.SoccerLeagueId);
            gamegroupvec.insertElementAt(new Vector(), 1);
        }
        vecofgamegroups = gamegroupvec;
        gamegroupheaders = gamegroupheadervec;
        long ct = AppController.getClearAllTime();
        if (ct > cleartime) {
            cleartime = ct;
        }
    }

    private void setShowProperties(String[] prefs) {
        this.showheaders = (parseBoolean(prefs[3]));
        sportType.setShowseries(parseBoolean(prefs[4]));
        sportType.setShowingame(parseBoolean(prefs[5]));
        sportType.setShowAdded(parseBoolean(prefs[6]));
        sportType.setShowExtra(parseBoolean(prefs[7]));
        sportType.setShowProps(parseBoolean(prefs[8]));
    }

    public Games transformGamesVecToCustomGamesVec(List<String> customheaders, Games gamesvec) {

        if (customheaders.size() == 0) {
            return gamesvec;
        }
        Games newgamesvec = new Games();
        for (String header : customheaders) {

            for (int k = 0; k < gamesvec.size(); k++) {
                Game g = gamesvec.getByIndex(k);
                if (g == null) {
                    continue;
                }

                if (g.getGamedate() == null) {
                    continue;
                }

                int leagueid = g.getLeague_id();
                Sport s = AppController.getSportByLeagueId(leagueid);

                Sport s2;
                if (s == null) {
                    continue;
                }
                if (leagueid == SiaConst.SoccerLeagueId) // soccer need to look at subleagueid
                {
                    leagueid = g.getSubleague_id();
                    s2 = AppController.getSportByLeagueId(leagueid);
                } else {
                    s2 = s;
                }
                if (s2 == null) {
                    continue;
                }
                if (header.equals(s2.getLeaguename() + " " + sdf2.format(g.getGamedate())) || header.equals(leagueid + " " + sdf2.format(g.getGamedate()))) {
                    newgamesvec.add(g);
                } else {
                    continue;
                }

            }
        }

        return newgamesvec;

    }

    public int calcmaxlength(String s1, String s2) {
        return Math.max(s1.length(), s2.length());
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

    public boolean isShowIngame() {
        return sportType.isShowingame();
    }

    public void setShowIngame(boolean b) {
        sportType.setShowingame(b);
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
    private MainGameTableModel buildModel() {

        MainGameTableModel model = new MainGameTableModel(sportType,allColumns,customheaders);
        Vector<Bookie> newBookiesVec = AppController.getBookiesVec();
        Vector hiddencols = AppController.getHiddenCols();
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

        Map<GameGroupHeader, LinesTableData> headerMap = new HashMap<>();
        for (int j = 0; j < gamegroupheaders.size(); j++) {
            Vector<Game> newgamegroupvec = (Vector<Game>) vecofgamegroups.get(j);
            GameGroupHeader gameGroupHeader = gamegroupheaders.get(j);
            if (null != gameGroupHeader || (null != newgamegroupvec && 0 < newgamegroupvec.size())) {
                LinesTableData tableSection;
                tableSection = headerMap.get(gameGroupHeader);
                if (null == tableSection) {
                    tableSection = createLinesTableData(newgamegroupvec, gameGroupHeader);
                    model.addGameLine(tableSection);
                    headerMap.put(gameGroupHeader, tableSection);
                } else {
                    tableSection.addAllGames(newgamegroupvec);
                }
            }
        }
//        if (sportType.isPredifined()) {
//            int rowHeight = SportType.Soccer.equals(sportType) ? SiaConst.SoccerRowheight : SiaConst.NormalRowheight;
//            addHalfTimeWhenAbsent(rowHeight, model);
//        }
        model.buildIndexMappingCache(true);
        AppController.addDataModels(model);

        return model;
    }
    private LinesTableData createLinesTableData(Vector newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType, display, period, cleartime, newgamegroupvec, timesort, shortteam, opener, last, gameGroupHeader, allColumns);
        int tableSectionRowHeight = TableUtils.calTableSectionRowHeight(newgamegroupvec);
        tableSection.setRowHeight(tableSectionRowHeight);
        return tableSection;
    }

    private void addHalfTimeWhenAbsent(int rowHeight, MainGameTableModel model) {
        if (!model.containsGroupHeader(GameStatus.HalfTime.getGroupHeader())) {
            LinesTableData tableSection = createLinesTableData(new Vector(), GameStatus.HalfTime.getGroupHeader());
            tableSection.setRowHeight(rowHeight);
            model.addGameLine(0, tableSection);
        }
    }

    private JComponent makeMainTableScrollPane(MainGameTable table) {
        return TableUtils.configTableLockColumns(table, AppController.getNumFixedCols());
    }

    public void setShowSeries(boolean b) {
        sportType.setShowseries(b);
    }

    public boolean isPreDefinedSport() {
        return this.sportType.isPredifined();
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

    public SportType getSportType() {
        return this.sportType;
    }

    @Override
    public void destroyMe() {
        inprogressgames.clear();
        finalgames.clear();
        halftimegames.clear();
        seriesgames.clear();
        ingamegames.clear();

        inprogressgamessoccer.clear();
        finalgamessoccer.clear();
        halftimegamessoccer.clear();
        seriesgamessoccer.clear();
        ingamegamessoccer.clear();
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
        if ( null == mainGameTable) {
            mainGameTable =  createMainGameTable(getDataModels(), getName());
        }
        return mainGameTable;
    }

}
