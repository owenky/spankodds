package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Games;
import com.sia.client.model.Sport;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class MainScreen extends JPanel {
    public Vector datamodelsvec = new Vector();
    public Timer timer;
    public int timer2count = 0;
    public int currentmaxlength = 0;
    public String name;
    public boolean timesort = false;
    public boolean shortteam = false;
    public boolean opener = false;
    public boolean last = false;
    public String display = "default";
    public int period = 0;
    private List<String> gamegroupheaders = new ArrayList<>();
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
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
    public long cleartime;
    public boolean showheaders = true;
    public boolean showseries = true;
    public boolean showingame = true;
    public boolean showadded = true;
    public boolean showextra = true;
    public boolean showprops = true;
    public Vector customheaders = new Vector();
    private JScrollPane lastScrollPane;
    private MainGameTable mainGameTable;

    public MainScreen(String name) {
        this.name = name;
        cleartime = new java.util.Date().getTime();
    }

    public MainScreen(String name, Vector customheaders) {
        this.name = name;
        this.customheaders = customheaders;
        cleartime = new java.util.Date().getTime();
    }

    public MainScreen(String name, Vector customheaders, boolean showheaders, boolean showseries, boolean showingame, boolean showadded, boolean showextra, boolean showprops) {
        this.name = name;
        this.customheaders = customheaders;
        this.showheaders = showheaders;
        this.showseries = showseries;
        this.showingame = showingame;
        this.showadded = showadded;
        this.showextra = showextra;
        this.showprops = showprops;
        cleartime = new java.util.Date().getTime();


    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MainScreen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);
    }

    public void setCustomHeaders(Vector v) {
        customheaders = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public boolean checktofire(int gameid) {
        List<LinesTableData> v = getDataModels();
        boolean status = false;
        for (int j = 0; j < v.size(); j++) {
            final LinesTableData ltd = v.get(j);
            status = ltd.checktofire(gameid);
            if ( status ) {
                ColumnAdjustManager.adjustColumn(this.getMainGameTable(),ltd,gameid);
                break;
            }
        }
        return status;
    }

    public List<LinesTableData> getDataModels() {
        return datamodelsvec;
    }

    public void setClearTime(long clear) {
        cleartime = clear;
        List<LinesTableData> v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            v.get(j).clearColors();
        }
    }

    public void clearAll() {
        List<LinesTableData> v = getDataModels();

        for (int j = 0; j < v.size(); j++) {
            v.get(j).clearColors();
        }
    }

    public void makeDataModelsVisible(boolean b) {
        List<LinesTableData> v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
           v.get(j).setInView(b);
        }
    }

    public void removeGame(int gameid) {
        List<LinesTableData> v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            //TODO add if logic
            if ( null != v.get(j).removeGameId(gameid) ) {
                //gameid is removed from a LinesTableData, don't need to continue because a gameid can only be in one LinesTableData
                break;
            }
        }
    }

    public void removeGames(String[] gameids) {
        List<LinesTableData> v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            try {
               v.get(j).removeGameIds(gameids);
            } catch (Exception ex) {
                log(ex);
            }
        }
    }

    public void addGame(Game g, boolean repaint) // only gets called when adding new game into system
    {
        int leagueid = g.getLeague_id();
        if (leagueid == 9) {
            leagueid = g.getSubleague_id();
        }
        String title = AppController.getSport(leagueid).getLeaguename() + " " + sdf2.format(g.getGamedate());
        //TODO gamegroupheaders should be part of LinesTableData -- 05/01/2021
        for (int i = 0; i < gamegroupheaders.size(); i++) {
            String header = gamegroupheaders.get(i);
            if (header.equals(title)) {
                LinesTableData thisltd = (LinesTableData) datamodelsvec.get(i);
                thisltd.addGame(g, repaint);
                break;
            }
        }
    }

    public void moveGameToThisHeader(Game g, String header) {
        Game thisgame = null;

        for (int k = 0; k < datamodelsvec.size(); k++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(k);
            thisgame = ltd.removeGameId(g.getGame_id());
            if (thisgame != null) {
                break;
            }
        }
        // now lets see if i found it in either
        if (thisgame != null) // i did find it
        {
            if (header.equalsIgnoreCase("In Progress")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(datamodelsvec.size() - 4);
                ltd.addGame(thisgame, true);
            } else if (header.equalsIgnoreCase("Soccer In Progress")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(datamodelsvec.size() - 3);
                ltd.addGame(thisgame, true);
            } else if (header.equalsIgnoreCase("FINAL")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(datamodelsvec.size() - 2);
                ltd.addGame(thisgame, true);
            } else if (header.equalsIgnoreCase("Soccer FINAL")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(datamodelsvec.size() - 1);
                ltd.addGame(thisgame, true);
            } else if (header.equalsIgnoreCase("Halftime")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(0);
                ltd.addGame(thisgame, true);

            } else if (header.equalsIgnoreCase("Soccer Halftime")) {
                LinesTableData ltd = (LinesTableData) datamodelsvec.get(1);
                ltd.addGame(thisgame, true);

            }
        }
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
        Vector gamegroupvec = new Vector();
        Vector gamegroupheadervec = new Vector();
        Vector gamegroupLeagueIDvec = new Vector();
        Vector currentvec = new Vector();


        Games allgames = new Games();


        int maxlength;
        currentmaxlength = 0;
        String[] prefs;

        Games allgamesforpref = AppController.getGamesVec();

        if (name.equalsIgnoreCase("football")) {
            boolean all = false;
            String footballpref = AppController.getUser().getFootballPref();
            prefs = footballpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String[] tmp = {};
            try {
                if (prefs.length > 2) {
                    tmp = prefs[2].split(",");
                    if (tmp[0].equalsIgnoreCase("football")) {
                        all = true;
                    }
                    this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                    this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                    this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                    this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                    this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                    this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);

                }

            } catch (Exception ex) {
                log(ex);
            }


            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }


            }
        } else if (name.equalsIgnoreCase("basketball")) {

            boolean all = false;
            String basketballpref = AppController.getUser().getBasketballPref();
            prefs = basketballpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }

            String[] tmp = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("basketball")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("baseball")) {
            boolean all = false;
            String baseballpref = AppController.getUser().getBaseballPref();
            prefs = baseballpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String tmp[] = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("baseball")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("hockey")) {
            boolean all = false;
            String hockeypref = AppController.getUser().getHockeyPref();
            prefs = hockeypref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String[] tmp = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("hockey")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("fighting")) {
            boolean all = false;
            String fightingpref = AppController.getUser().getFightingPref();
            prefs = fightingpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String tmp[] = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("fighting")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase(SiaConst.SoccerStr)) {
            boolean all = false;
            String soccerpref = AppController.getUser().getSoccerPref();
            prefs = soccerpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String tmp[] = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase(SiaConst.SoccerStr)) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("auto racing")) {
            boolean all = false;
            String autoracingpref = AppController.getUser().getAutoracingPref();
            prefs = autoracingpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String tmp[] = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("auto racing")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    allgames.add(tempGame);
                }

            }
        } else if (name.equalsIgnoreCase("golf")) {
            boolean all = false;
            String golfpref = AppController.getUser().getGolfPref();
            prefs = golfpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String tmp[] = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("golf")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    //	System.out.println("yes"+set+"---");
                    allgames.add(tempGame);
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);

            }
        } else if (name.equalsIgnoreCase("tennis")) {
            boolean all = false;
            String tennispref = AppController.getUser().getTennisPref();
            prefs = tennispref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            String[] tmp = {};
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase("tennis")) {
                    all = true;
                }
                this.showheaders = (Boolean.parseBoolean(prefs[3]) ? true : false);
                this.showseries = (Boolean.parseBoolean(prefs[4]) ? true : false);
                this.showingame = (Boolean.parseBoolean(prefs[5]) ? true : false);
                this.showadded = (Boolean.parseBoolean(prefs[6]) ? true : false);
                this.showextra = (Boolean.parseBoolean(prefs[7]) ? true : false);
                this.showprops = (Boolean.parseBoolean(prefs[8]) ? true : false);
            }

            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = allgamesforpref.getByIndex(z);
                int LID = tempGame.getLeague_id();
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    //System.out.println("yes"+set+"---");
                    allgames.add(tempGame);
                }

            }
        } else {
            allgames = allgamesforpref;
        }

        java.util.Date today = new java.util.Date();

        String lastdate = null;
        int lastleagueid = 0;

        log("timesort?=" + timesort + "..allgames size=" + allgames.size());

        allgames = transformGamesVecToCustomGamesVec(customheaders, allgames);


        for (int k = 0; k < allgames.size(); k++) {

            int gameid=-1;
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
            Sport s = AppController.getSport("" + leagueid);

            Sport s2;
            if (s == null) {
                log("skipping " + leagueid + "...cuz of null sport");
                continue;
            }
            if (customheaders.size() > 0 || s.getSportname().equalsIgnoreCase(name) || (name.equalsIgnoreCase("Today") && gamedate.compareTo(todaysgames) <= 0)) {


                if (shortteam) {
                    maxlength = calcmaxlength(g.getShortvisitorteam(), g.getShorthometeam());

                } else {
                    maxlength = calcmaxlength(g.getVisitorteam(), g.getHometeam());
                }

                if (maxlength > currentmaxlength) {
                    currentmaxlength = maxlength;
                }

                if (leagueid == 9) // soccer need to look at subleagueid
                {
                    leagueid = g.getSubleague_id();

                    s2 = AppController.getSport("" + leagueid);

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

                int leagueidtemp = g.getLeague_id();
                Sport stemp = AppController.getSport("" + leagueid);
                int id = s.getParentleague_id();

                if (g.getStatus().equalsIgnoreCase("Tie") || g.getStatus().equalsIgnoreCase("Cncld") || g.getStatus().equalsIgnoreCase("Poned") || g.getStatus().equalsIgnoreCase("Final")
                        || g.getStatus().equalsIgnoreCase("Win") || (g.getTimeremaining().equalsIgnoreCase("Win"))

                ) {


                    if (id == 9) {
                        finalgamessoccer.add(g);
                    } else {
                        finalgames.add(g);
                    }

                } else if (!g.getStatus().equalsIgnoreCase("NULL") && !g.getStatus().equals("")) {
                    if (g.getStatus().equalsIgnoreCase("Time")) {
                        if (id == 9) {
                            halftimegamessoccer.add(g);
                        } else {
                            halftimegames.add(g);
                        }

                    } else {
                        if (id == 9) {
                            inprogressgamessoccer.add(g);
                        } else {
                            inprogressgames.add(g);
                        }


                    }
                } else if (g.isSeriesprice()) {
                    if (id == 9) {
                        seriesgamessoccer.add(g);
                    } else {
                        seriesgames.add(g);
                    }


                } else if ((g.isIngame() || description.indexOf("In-Game") != -1)) {
                    if (id == 9) {
                        ingamegamessoccer.add(g);
                    } else {
                        ingamegames.add(g);
                    }

                } else if (lastdate == null) // new date
                {
                    System.out.println("ddd");
                    gamegroupheadervec.add(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()));
                    gamegroupLeagueIDvec.add(s2.getParentleague_id());
                    System.out.println("eee");
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
                    gamegroupheadervec.add(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()));
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

        if (seriesgames.size() > 0 && isShowSeries()) {
            gamegroupheadervec.add("Series Prices");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(seriesgames);
        }
        if (seriesgamessoccer.size() > 0 && isShowSeries()) {
            gamegroupheadervec.add("Soccer Series Prices");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(seriesgamessoccer);
        }


        if (ingamegames.size() > 0 && isShowIngame()) {
            gamegroupheadervec.add("In Game Prices");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(ingamegames);
        }

        if (ingamegamessoccer.size() > 0 && isShowIngame()) {
            gamegroupheadervec.add("Soccer  In Game Prices");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(ingamegamessoccer);
        }


        if (inprogressgames.size() > 0) {
            gamegroupheadervec.add("In Progress");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(inprogressgames);
        } else {
            gamegroupheadervec.add("In Progress");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(new Vector());
        }

        if (inprogressgamessoccer.size() > 0) {
            gamegroupheadervec.add("Soccer In Progress");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(inprogressgamessoccer);
        } else {
            gamegroupheadervec.add("Soccer In Progress");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(new Vector());
        }


        if (finalgames.size() > 0) {
            gamegroupheadervec.add("FINAL");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(finalgames);
        } else {
            gamegroupheadervec.add("FINAL");
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.add(new Vector());
        }


        if (finalgamessoccer.size() > 0) {
            gamegroupheadervec.add("Soccer FINAL");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(finalgamessoccer);
        } else {
            gamegroupheadervec.add("Soccer FINAL");
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.add(new Vector());
        }


        if (halftimegames.size() > 0) {
            gamegroupheadervec.insertElementAt("Halftime", 0);
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.insertElementAt(halftimegames, 0);
        } else {
            gamegroupheadervec.insertElementAt("Halftime", 0);
            gamegroupLeagueIDvec.add(-1);
            gamegroupvec.insertElementAt(new Vector(), 0);
        }

        if (halftimegamessoccer.size() > 0) {
            gamegroupheadervec.insertElementAt("Soccer Halftime", 1);
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.insertElementAt(halftimegamessoccer, 1);
        } else {
            gamegroupheadervec.insertElementAt("Soccer Halftime", 1);
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.insertElementAt(new Vector(), 1);
        }
        vecofgamegroups = gamegroupvec;
        gamegroupheaders = gamegroupheadervec;
        long ct = AppController.getClearAllTime();
        if (ct > cleartime) {
            cleartime = ct;
        }

        System.out.println("about to draw...teammaxlength=" + currentmaxlength);


        checkAndRunInEDT(() -> {
            drawIt();
            log("done drawing");
        });

        timer = new Timer(1000, new ActionListener() { //Change parameters to your needs.
            public void actionPerformed(ActionEvent e) {
                try {
                    firedatamodels();

                } catch (Exception ex) {
                    log(ex);
                }

            }
        });

        //TODO disable firedatamodels
//        timer.start();
//        log("timer start");
        //END of debug TODO
    }

    public Games transformGamesVecToCustomGamesVec(Vector customheaders, Games gamesvec) {

        if (customheaders.size() == 0) {
            return gamesvec;
        }
        Games newgamesvec = new Games();
        for (int i = 0; i < customheaders.size(); i++) {

            String header = (String) customheaders.elementAt(i);
            for (int k = 0; k < gamesvec.size(); k++) {
                Game g = gamesvec.getByIndex(k);
                if (g == null) {
                    continue;
                }

                if (g.getGamedate() == null) {
                    continue;
                }

                int leagueid = g.getLeague_id();
                Sport s = AppController.getSport("" + leagueid);

                Sport s2;
                if (s == null) {
                    continue;
                }
                if (leagueid == 9) // soccer need to look at subleagueid
                {
                    leagueid = g.getSubleague_id();

                    s2 = AppController.getSport("" + leagueid);

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
        if (s1.length() > s2.length()) {
            return s1.length();
        } else {
            return s2.length();
        }
    }

    public boolean isShowAdded() {
        return showadded;
    }

    public void setShowAdded(boolean b) {
        showadded = b;
    }

    public boolean isShowExtra() {
        return showextra;
    }

    public void setShowExtra(boolean b) {
        showextra = b;
    }

    public boolean isShowProps() {
        return showprops;
    }

    public void setShowProps(boolean b) {
        showprops = b;
    }

    public boolean isShowSeries() {
        return showseries;
    }

    public void setShowSeries(boolean b) {
        showseries = b;
    }

    public boolean isShowIngame() {
        return showingame;
    }

    public void setShowIngame(boolean b) {
        showingame = b;
    }
    public void drawIt() {

        mainGameTable = createMainGameTable();
        Vector newBookiesVec = AppController.getBookiesVec();
        ScrollablePanel tablePanel = new ScrollablePanel();
        tablePanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);


        //changed this to stretch for Vertical Scroll Bar to appear if frame is resized and data can not fit in viewport
        tablePanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);

        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

        JTable table0 = new JTable();
        table0.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table0.setPreferredScrollableViewportSize(table0.getPreferredSize());
        table0.setOpaque(true);
        table0.changeSelection(0, 0, false, false);
        table0.setAutoCreateColumnsFromModel(false);

        if (name.equalsIgnoreCase(SiaConst.SoccerStr)) {
            table0.setRowHeight(60);
        } else {
            table0.setRowHeight(30);
        }
        JTableHeader tableHeader = table0.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);

        JScrollPane scrollPane0 = new JScrollPane(table0);

        Vector hiddencols = AppController.getHiddenCols();
        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = (Bookie) newBookiesVec.get(k);

            if (hiddencols.contains(b)) {
                continue;
            }
            TableColumn column;

            column = new TableColumn(k, 30, null, null);

            column.setHeaderValue(b.getShortname() + "");
            column.setIdentifier("" + b.getBookie_id());
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

            table0.addColumn(column);
            mainGameTable.addColumn(column);
        }

        // here i'm adding a blank column
        TableColumn blankcol = new TableColumn(newBookiesVec.size(), 10, null, null);
        blankcol.setHeaderValue("");
        blankcol.setIdentifier("9999999");

        blankcol.setMaxWidth(30);
        blankcol.setPreferredWidth(30);
        table0.addColumn(blankcol);

        LinesTableData dataModel0 = new LinesTableData(display, period, 0, new Vector(), table0, timesort, shortteam, opener, last);
        table0.setModel(dataModel0);
        JTable fixed0 = makeFixedRowHeader(AppController.getNumFixedCols(), table0, false);

        fixed0.setPreferredScrollableViewportSize(fixed0.getPreferredSize());

        JTableHeader tableHeaderfixed = fixed0.getTableHeader();
        Font headerFontfixed = new Font("Verdana", Font.BOLD, 11);
        tableHeaderfixed.setFont(headerFontfixed);

        scrollPane0.setRowHeaderView(fixed0);
        scrollPane0.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed0.getTableHeader());

        scrollPane0.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane0.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane0.removeMouseWheelListener(scrollPane0.getMouseWheelListeners()[0]);


        log("gamergroup headers start..." + new java.util.Date());

        Vector oldgamegroupvec = new Vector();
        for (int j = 0; j < gamegroupheaders.size(); j++) {
            boolean showit = true;
            Vector newgamegroupvec = (Vector) vecofgamegroups.get(j);
            int LID = 0;

            if ((newgamegroupvec == null || newgamegroupvec.size() == 0))// && !gamegroupheaders.get(j).equals("FINAL")) dont show header if its blank! however must show final for scrollpane purposes
            {
                showit = false;
            }


            JLabel label = new JLabel("                                                                                                                                                      " +
                    gamegroupheaders.get(j) +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      ");


            if ((gamegroupheaders.get(j) + "").contains(SiaConst.SoccerStr)) {

                if (name.equalsIgnoreCase(SiaConst.SoccerStr) || (oldgamegroupvec.size() == 0)) {
                    String orginal = gamegroupheaders.get(j) + "";
                    String nameWithoutSoccer = orginal.replace(SiaConst.SoccerStr, "");
                    label.setText("                                                                                                                                                      " +

                            nameWithoutSoccer +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      ");


                } else {
                    showit = false;
                }
            }
            label.setOpaque(true);
            label.setBackground(new Color(0, 0, 128));
            label.setForeground(Color.WHITE);
            for (int k = 0; k < newgamegroupvec.size(); k++) {
                Game g = (Game) newgamegroupvec.get(k);
                int leagueid = g.getLeague_id();
                Sport s = AppController.getSport("" + leagueid);
                LID = s.getParentleague_id();

            }
            JTable tablex;
            if ((gamegroupheaders.get(j) + "").contains(SiaConst.SoccerStr) || (LID == 9)) {
                tablex = new SoccerTableView();
                tablex.setRowHeight(60);
            } else {
                tablex = new RegularTableView();
                tablex.setRowHeight(30);
            }
            tablex.setName("table" + j);
            tablex.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tablex.setColumnModel(table0.getColumnModel());
            tablex.setPreferredScrollableViewportSize(tablex.getPreferredSize());
            tablex.setOpaque(true);

            tablex.changeSelection(0, 0, false, false);
            tablex.setAutoCreateColumnsFromModel(false);
            LinesTableData dataModel = new LinesTableData(display, period, cleartime, newgamegroupvec, tablex, timesort, shortteam, opener, last);

            tablex.setModel(dataModel);
            datamodelsvec.add(dataModel);
            JScrollPane scrollPanex = new JScrollPane(tablex);

            scrollPanex.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            scrollPanex.getHorizontalScrollBar().setModel(scrollPane0.getHorizontalScrollBar().getModel());
            if (j == vecofgamegroups.size() - 1) // last group
            {

                scrollPanex.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                lastScrollPane = scrollPanex;


                JTable fixedx = makeFixedRowHeader(AppController.getNumFixedCols(), tablex, true);
                //fixedx.setColumnAutoResizable(true);
                fixedx.setColumnModel(fixed0.getColumnModel());
                //fixedx.setDefaultRenderer(Object.class, new LineRenderer());

                if ((gamegroupheaders.get(j) + "").contains(SiaConst.SoccerStr) || (LID == 9)) {
                    fixedx.setRowHeight(60);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer(SiaConst.SoccerStr.toLowerCase()));
                } else {
                    fixedx.setRowHeight(30);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer());
                }
                fixedx.setPreferredScrollableViewportSize(fixedx.getPreferredSize());
                scrollPanex.setRowHeaderView(fixedx);

            } else {
                scrollPanex.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

                JTable fixedx = makeFixedRowHeader(AppController.getNumFixedCols(), tablex, false);
                fixedx.setColumnModel(fixed0.getColumnModel());

                if ((gamegroupheaders.get(j) + "").contains(SiaConst.SoccerStr) || (LID == 9)) {
                    fixedx.setRowHeight(60);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer(SiaConst.SoccerStr.toLowerCase()));
                } else {
                    fixedx.setRowHeight(30);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer());
                }

                fixedx.setPreferredScrollableViewportSize(fixedx.getPreferredSize());
                scrollPanex.setRowHeaderView(fixedx);
            }


            tablex.setTableHeader(null); // this has to be placed all the way down here!!!!
            //System.out.println("nnn");
            if (isShowHeaders()) {
                if (showit) {
                    tablePanel.add(label);
                }
            }
            if (newgamegroupvec.size() > 0) {
                scrollPanex.setPreferredSize(new Dimension(700, tablex.getRowHeight() * newgamegroupvec.size()));
            } else {
                scrollPanex.setPreferredSize(new Dimension(1, 1));
            }
            //TODO add tablemodel to MainGameTable
            mainGameTable.addGameLine((LinesTableData)tablex.getModel());
           tablePanel.add(scrollPanex);
            scrollPanex.removeMouseWheelListener(scrollPanex.getMouseWheelListeners()[0]);

            oldgamegroupvec = newgamegroupvec;
        }

        log("gamergroup headers end..." + new java.util.Date());
        log("hidden end..." + new java.util.Date());
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(29); // 29 has nothing to do with rowheight
        scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        JScrollBar bar = lastScrollPane.getHorizontalScrollBar();


        removeAll();
        revalidate();
        //TODO don't add scrollPane0
//        add(scrollPane0, BorderLayout.PAGE_START);
        //TODO add mainGameTable instead of scrollPane to center
//        add(scrollPane, BorderLayout.CENTER);
        mainGameTable.optimizeRowHeights();
        JScrollPane mainTableScrollPane = makeMainTableScrollPane(mainGameTable);
        add(mainTableScrollPane, BorderLayout.CENTER);
        //END OF TODO
        add(bar, BorderLayout.PAGE_END);
        AppController.addDataModels(getDataModels());
        log("Datamodels size is :" + AppController.getDataModels().size());
        //TODO disable following two lines
        adjustcols(true);
//        firedatamodels();
        //END of TODO

        Timer timer2 = new Timer(500, new ActionListener() { //Change parameters to your needs.
            public void actionPerformed(ActionEvent e) {
                try {

                    adjustcols(false);
                    timer2count++;
                    if (timer2count > 12) {
                        timer2count = 0;
                        ((Timer) e.getSource()).stop();
                    }
                } catch (Exception ex) {
                    log("exception firing data models " + ex);
                }

            }
        });
        //TODO disable it for debug
//        timer2.start();
//        log("timer2 start");
//        log("drawit end..." + new java.util.Date());
        //END of Debug TODO
    }

    public void firedatamodels() {
        log(new Exception("disable firedatamodels"));
        //TODO disable
        /**
        for (int i = 0; i < datamodelsvec.size(); i++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(i);
            ltd.fire();
        }
         */
    }

    public JTable makeFixedRowHeader(int fixedColumns, JTable main, boolean deletefrommain) {

        JTable fixed = new JTable();
        fixed.setAutoCreateColumnsFromModel(false);
        fixed.setModel(main.getModel());
        fixed.setSelectionModel(main.getSelectionModel());
        fixed.setFocusable(false);


        for (int i = 0; i < fixedColumns; i++) {
            TableColumnModel columnModel = main.getColumnModel();
            if (deletefrommain) {
                TableColumn column = columnModel.getColumn(0);
                columnModel.removeColumn(column);
                fixed.getColumnModel().addColumn(column);
            } else {
                TableColumn column = columnModel.getColumn(i);
                fixed.getColumnModel().addColumn(column);

            }

        }

        return fixed;
    }

    public boolean isShowHeaders() {
        return showheaders;
    }

    public void setShowHeaders(boolean b) {
        showheaders = b;
    }

    public void adjustcols(boolean includeheader) {
        //TODO
//        for (int i = 0; i < adjusters.size(); i++) {
//            TableColumnAdjuster col = (TableColumnAdjuster) adjusters.get(i);
//            col.adjustColumns(includeheader);
//        }
        mainGameTable.adjustColumns(includeheader);
    }
    public MainGameTable getMainGameTable() {
        return mainGameTable;
    }
    public void destroyMe() {
        AppController.removeDataModels(getDataModels());
        datamodelsvec.clear();
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

        if (timer != null) {
            timer.stop();
        }
        log("destroyed mainscreen!!!!");

    }
    private MainGameTable createMainGameTable() {
        MainGameTable mainGameTable = new MainGameTable();
        mainGameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainGameTable.setIntercellSpacing(new Dimension(4,2));
        mainGameTable.setPreferredScrollableViewportSize(mainGameTable.getPreferredSize());
        //since there is no setDateVector() is called for this table, columnIdentifiers of this table is blank
        //MainGameTableModel.this.fireTableChanged(e) would wipe out this table if autoCreateColumnsFromModel is true
        mainGameTable.setAutoCreateColumnsFromModel(false);
        mainGameTable.setName(name);
        JTableHeader tableHeader = mainGameTable.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        return mainGameTable;
    }
    private JScrollPane makeMainTableScrollPane(JTable table) {
        JScrollPane rtn = new JScrollPane(table);
        return rtn;
    }
}
