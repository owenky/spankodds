package com.sia.client.ui;

import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.model.User;

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
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class MainScreen extends JPanel {
    public Vector datamodelsvec = new Vector();
    public Timer timer;
    public Timer timer2;
    public int timer2count = 0;
    public int currentmaxlength = 0;
    public String name;
    public boolean timesort = false;
    public boolean shortteam = false;
    public boolean opener = false;
    public boolean last = false;
    public String display = "default";
    public int period = 0;
    public Vector alltables = new Vector();//
    public Vector fixedtables = new Vector();//
    public Vector renderers = new Vector();//
    public Vector columns = new Vector();//
    public Vector adjusters = new Vector();//
    public Vector managers = new Vector();//
    public Vector gamegroupheaders = new Vector();
    public Vector vecofgamegroups = new Vector();
    public Vector gamegroupLeagueID = new Vector();
    public Vector inprogressgames = new Vector();
    public Vector halftimegames = new Vector();
    public Vector finalgames = new Vector();
    public Vector inprogressgamessoccer = new Vector();
    public Vector halftimegamessoccer = new Vector();
    public Vector finalgamessoccer = new Vector();
    public Vector seriesgames = new Vector();
    public Vector ingamegames = new Vector();
    public Vector seriesgamessoccer = new Vector();
    public Vector ingamegamessoccer = new Vector();
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MainScreen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.add(new MainScreen());
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        // frame.setLocationByPlatform( true );
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

    public void checktofire(String gameid) {
        Vector v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            ((LinesTableData) v.get(j)).checktofire(gameid);
        }

    }

    public Vector getDataModels() {
        return datamodelsvec;
    }

    public void setClearTime(long clear) {
        cleartime = clear;
        Vector v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            ((LinesTableData) v.get(j)).clearColors();
        }
    }

    public void clearAll() {
        Vector v = getDataModels();

        for (int j = 0; j < v.size(); j++) {
            ((LinesTableData) v.get(j)).clearColors();
        }
    }

    public void makeDataModelsVisible(boolean b) {
        Vector v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            ((LinesTableData) v.get(j)).setInView(b);
        }
    }

    public void removeGame(String gameid) {
        Vector v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            ((LinesTableData) v.get(j)).removeGameId("" + gameid);
        }
    }

    public void removeGames(String[] gameids) {
        Vector v = getDataModels();
        for (int j = 0; j < v.size(); j++) {
            try {
                ((LinesTableData) v.get(j)).removeGameIds(gameids);
            } catch (Exception ex) {
                System.out.println("exception removing games in mainscreen " + ex);
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
        for (int i = 0; i < gamegroupheaders.size(); i++) {
            String header = (String) gamegroupheaders.get(i);
            if (header.equals(title)) {

                //Vector thisgamegroup = (Vector)vecofgamegroups.get(i);
                //thisgamegroup.add(g);
                LinesTableData thisltd = (LinesTableData) datamodelsvec.get(i);
                thisltd.addGame(g, repaint);
                break;
            }
        }
        //this may not find anything becasue header is not on main screen i.e. game not applicable to current main screen
    }

    public void moveGameToThisHeader(Game g, String header) {
        Game thisgame = null;

        //System.out.println("moved---------------------------------");
        for (int k = 0; k < datamodelsvec.size(); k++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(k);
            thisgame = ltd.removeGameId("" + g.getGame_id());
            if (thisgame != null) {
                break;
            }
        }
	/*
		if(thisgame == null) // lastly check halftimedatamodel
		{
			thisgame = halftimedatamodel.removeGameId(""+g.getGame_id());
		}
	*/

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
            Vector tabpanes = AppController.getTabPanes();
		/*	for(int i=0;i<tabpanes.size();i++)
								{
							SportsTabPane tp = (SportsTabPane)tabpanes.get(i);


								tp.refreshCurrentTab();
								}*/


        }
        //this.createMe();
        //


    }

    public void createMe(String display, int period, boolean timesort, boolean shortteam, boolean opener, boolean last, JLabel loadlabel) {
        setLayout(new BorderLayout(0, 0));
        // add progress
        this.setOpaque(true);

        //ImageIcon loadgif = new ImageIcon("ajax-loader.gif");
        //ImageIcon loadgif = new ImageIcon("football.gif");


        add(loadlabel);


        //destroyMe();
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
        Vector gamesVec = new Vector();


        Vector allgames = new Vector();


        int maxlength = 0;
        currentmaxlength = 0;
        String prefs[];

        Vector allgamesforpref = AppController.getGamesVec();

        if (name.equalsIgnoreCase("football")) {
            boolean all = false;
            String footballpref = AppController.getUser().getFootballPref();
            prefs = footballpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }
            //	System.out.println(prefs[2]);
            String tmp[] = {};
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
            }


            Set<String> set = new HashSet<>(Arrays.asList(tmp));
            for (int z = 0; z < allgamesforpref.size(); z++) {
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
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
        } else if (name.equalsIgnoreCase("basketball")) {

            boolean all = false;
            String basketballpref = AppController.getUser().getBasketballPref();
            prefs = basketballpref.split("\\|");
            int comingdays = Integer.parseInt(prefs[1]);
            if (Boolean.parseBoolean(prefs[0])) {
                this.timesort = true;
            }

            String tmp[] = {};
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    //	System.out.println("yes"+set+"---");
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //	System.out.println("LID===="+LID);
                Date gmDate = tempGame.getGamedate();
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, comingdays);
                Date x = c.getTime();

                if ((set.contains(LID + "") || all) && gmDate.before(x)) {
                    //	System.out.println("yes"+set+"---");
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
            String tmp[] = {};
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //	System.out.println("LID===="+LID);
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //	System.out.println("LID===="+LID);
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
        } else if (name.equalsIgnoreCase("soccer")) {
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
                if (tmp[0].equalsIgnoreCase("soccer")) {
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
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
            String tmp[] = {};
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
                Game tempGame = (Game) allgamesforpref.get(z);
                int LID = tempGame.getLeague_id();
                //System.out.println("LID===="+LID);
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


        Vector allsports = AppController.getSportsVec();
        java.util.Date today = new java.util.Date();

        String lastdate = null;
        int lastleagueid = 0;

        System.out.println("timesort?=" + timesort + "..allgames size=" + allgames.size());
		/*try
		{
			if(timesort)
			{
				Collections.sort(allgames, new GameDateSorter().thenComparing(new GameTimeSorter()).thenComparing(new GameNumSorter()));
			}
			else
			{
				Collections.sort(allgames, new GameDateSorter().thenComparing(new GameNumSorter()));
			}
		}
		catch(Exception ex)
		{
			System.out.println("exception sorting "+ex);
		}*/


        allgames = transformGamesVecToCustomGamesVec(customheaders, allgames);


        for (int k = 0; k < allgames.size(); k++) {

            String gameid = "";
            Game g = (Game) allgames.get(k);


            if (g == null) {
                System.out.println("skipping gameid=" + gameid + "...cuz of null game");
                continue;
            } else {
                gameid = "" + g.getGame_id();
            }
            if (g.getGamedate() == null) {
                System.out.println("skipping gameid=" + gameid + "...cuz of null game date");
                continue;
            }

            String gamedate = sdf.format(g.getGamedate());
            String todaysgames = sdf.format(today);
            int leagueid = g.getLeague_id();
            Sport s = AppController.getSport("" + leagueid);

            Sport s2;


            //System.out.println(s.getSportname());
            if (s == null) {
                System.out.println("skipping " + leagueid + "...cuz of null sport");
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
                    //v.add(gameid);
                    v.add(g);
                    currentvec = v;
                } else if (!lastdate.equals(gamedate) || lastleagueid != leagueid) // new date or new league!
                {
                    //System.out.println("newdate!...lastdate="+lastdate+"..gamedate="+g.getGamedate());
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
                    //currentvec.add(gameid);
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
        //=======================================================


        if (halftimegamessoccer.size() > 0) {
            gamegroupheadervec.insertElementAt("Soccer Halftime", 1);
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.insertElementAt(halftimegamessoccer, 1);
        } else {
            gamegroupheadervec.insertElementAt("Soccer Halftime", 1);
            gamegroupLeagueIDvec.add(9);
            gamegroupvec.insertElementAt(new Vector(), 1);
        }
        //========================================================
		/*
		if(gamegroupvec.size() == 0)
		{
			setContent(new Label("No Games"));

		}
		else
		{

		createAndSetSwingContent(myParent.swingNode);
		//System.out.println("this="+this);
		setContent(myParent.swingNode);

		// moving this to create part below firedatamodels();
		}
		*/
        vecofgamegroups = gamegroupvec;
        gamegroupheaders = gamegroupheadervec;
        gamegroupLeagueID = gamegroupLeagueIDvec;
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

        timer.start();
        System.out.println("timer start");


        //AppController.addDataModels(getDataModels());


    }

    public Vector transformGamesVecToCustomGamesVec(Vector customheaders, Vector gamesvec) {
        System.out.println("lollll" + customheaders);

        java.util.Date today = new java.util.Date();
        if (customheaders.size() == 0) {
            return gamesvec;
        }
        Vector newgamesvec = new Vector();
        for (int i = 0; i < customheaders.size(); i++) {

            String header = (String) customheaders.elementAt(i);
            for (int k = 0; k < gamesvec.size(); k++) {

                String gameid = "";
                Game g = (Game) gamesvec.get(k);

                //System.out.println("bklppll"+g.getGame_id());
                if (g == null)
                {
                    //System.out.println("skipping gameid="+gameid+"...cuz of null game");
                    continue;
                }
                else
                {
                    gameid = "" + g.getGame_id();
                }
                if (g.getGamedate() == null) {
                    //System.out.println("skipping gameid="+gameid+"...cuz of null game date");
                    continue;
                }

                String gamedate = sdf.format(g.getGamedate());
                String todaysgames = sdf.format(today);
                int leagueid = g.getLeague_id();
                Sport s = AppController.getSport("" + leagueid);

                Sport s2;


                //System.out.println(s.getSportname());
                if (s == null) {
                    //System.out.println("skipping "+leagueid+"...cuz of null sport");
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
                    //System.out.println("skipping "+leagueid+"...cuz of null sport");
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

        Vector newBookiesVec = AppController.getBookiesVec();
        for (int i = 0; i < newBookiesVec.size(); i++) {
            //System.out.println("booooooooooooooooooooooooooooooook" + newBookiesVec.get(i));
        }
        final JPanel tablePanel1 = new JPanel();
        ScrollablePanel tablePanel = new ScrollablePanel();
        tablePanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);


        //changed this to stretch for Vertical Scroll Bar to appear if frame is resized and data can not fit in viewport
        tablePanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);

        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        //tablePanel.setLayout( new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS) );


        //setLayout( new BorderLayout(0,0) );
        //  Only the Table Header is displayed

        JTable table0 = new JTable();
        alltables.add(table0);
        table0.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table0.setPreferredScrollableViewportSize(table0.getPreferredSize());
        table0.setOpaque(true);
        table0.changeSelection(0, 0, false, false);
        table0.setAutoCreateColumnsFromModel(false);
        if (name.equalsIgnoreCase("soccer")) {
            table0.setRowHeight(60);
        } else {
            table0.setRowHeight(30);
        }
        //table0.setRowHeight(30);
        JTableHeader tableHeader = table0.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        //jidetable feature table0.setColumnAutoResizable(true);
        JScrollPane scrollPane0 = new JScrollPane(table0);

        User user = AppController.getUser();
        String bookiecolumnprefs = user.getBookieColumnPrefs();
        String fixedcolumnprefs = user.getFixedColumnPrefs();
        //System.out.println("BCM="+bookiecolumnprefs);


        Vector hiddencols = AppController.getHiddenCols();
        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = (Bookie) newBookiesVec.get(k);

            if (hiddencols.contains(b)) {
                continue;
            }
            TableColumn column;
		/*if(name.equalsIgnoreCase("soccer"))
		{
			System.out.println("soccer pressed");
			LineRenderer lr = new LineRenderer(name);
			renderers.add(lr);
			column = new TableColumn(k,30, lr, null);
		}*/
            //else
            {
                LineRenderer lr = new LineRenderer();
                //	renderers.add(lr);
                column = new TableColumn(k, 30, null, null);
            }


            // column = new TableColumn(k,30, lr, null);
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
                    //column.setPreferredWidth(150) ;
                }

            } else if (b.getBookie_id() > 1000) {
                column.setMinWidth(10);
                column.setPreferredWidth(65);
            } else {
                //column.setPreferredWidth(50) ;
                column.setMinWidth(10);
                column.setPreferredWidth(30);
            }

            //owen took this out 6/18
            table0.addColumn(column);
            columns.add(column);
        }

        // here i'm adding a blank column
        TableColumn blankcol = new TableColumn(newBookiesVec.size(), 10, null, null);
        blankcol.setHeaderValue("");
        blankcol.setIdentifier("9999999");

        blankcol.setMaxWidth(30);
        blankcol.setPreferredWidth(30);

        //owen took this out 6/18
        table0.addColumn(blankcol);


        columns.add(blankcol);

        //owen put this in 6/18
        //table0.setColumnModel(AppController.getColumnModel());


        TableColumnManager tcm0 = new TableColumnManager(table0, "");
        TableColumnAdjuster tca0 = new TableColumnAdjuster(table0);
        managers.add(tcm0);
        adjusters.add(tca0);
        LinesTableData dataModel0 = new LinesTableData(display, period, 0, new Vector(), table0, timesort, shortteam, opener, last);
        table0.setModel(dataModel0);
        //datamodelsvec.add(dataModel0);


        JTable fixed0 = makeFixedRowHeader(AppController.getNumFixedCols(), table0, false);
        fixedtables.add(fixed0);
	/*	if(name.equalsIgnoreCase("soccer"))
		{
			fixed0.setRowHeight(60);
		}
		else{
			fixed0.setRowHeight(30);
		}*/
        //fixed0.setRowHeight(30);

        fixed0.setPreferredScrollableViewportSize(fixed0.getPreferredSize());

        //owen added this 6/18
        //fixed0.setColumnModel(AppController.getFixedColumnModel());
        JTableHeader tableHeaderfixed = fixed0.getTableHeader();
        Font headerFontfixed = new Font("Verdana", Font.BOLD, 11);
        tableHeaderfixed.setFont(headerFontfixed);
        TableColumnManager tcmfixed0 = new TableColumnManager(fixed0, "fixed");

        //owen inserted 6/26
        TableColumnAdjuster tcafixed0 = new TableColumnAdjuster(fixed0);
        //fixed0.setColumnAutoResizable(true);
        scrollPane0.setRowHeaderView(fixed0);
        scrollPane0.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed0.getTableHeader());
        tca0.adjustColumns();

        //adjusters.add(tcafixed0);

        scrollPane0.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane0.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane0.removeMouseWheelListener(scrollPane0.getMouseWheelListeners()[0]);


        System.out.println("gamergroup headers start..." + new java.util.Date());

        Vector oldgamegroupvec = new Vector();
        for (int j = 0; j < gamegroupheaders.size(); j++) {
            boolean showit = true;
            Vector newgamegroupvec = (Vector) vecofgamegroups.get(j);

            //System.out.println(gamegroupheaders.get(j)+"--------"+newgamegroupvec);
            for (int i = 0; i < newgamegroupvec.size(); i++) {
                Game g = (Game) newgamegroupvec.get(i);
                //System.out.println("+++++"+g.getGame_id()+"+++++++");
            }
            //	System.out.println(gamegroupLeagueID.get(j));

            int LID = 0;


            if ((newgamegroupvec == null || newgamegroupvec.size() == 0))// && !gamegroupheaders.get(j).equals("FINAL"))
            // dont show header if its blank!
            //however must show final for scrollpane purposes
            {

                showit = false;

            }


            JLabel label = new JLabel("                                                                                                                                                      " +

                    gamegroupheaders.get(j) +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      " +
                    "                                                                                                                                                      ");


            //JLabel label = new JLabel("<html><body>" +gamegroupheaders.get(j)+"</body></html>");
            if ((gamegroupheaders.get(j) + "").contains("Soccer")) {

                if (name.equalsIgnoreCase("soccer") || (oldgamegroupvec.size() == 0)) {
                    String orginal = gamegroupheaders.get(j) + "";
                    String nameWithoutSoccer = orginal.replace("Soccer", "");
                    label.setText("                                                                                                                                                      " +

                            nameWithoutSoccer +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      " +
                            "                                                                                                                                                      ");


                } else {
                    showit = false;
                }
                //System.out.print("s");
            }
            label.setOpaque(true);


            //label.setPreferredSize(new Dimension(1, 1)); // this is the key part.

            label.setBackground(new Color(0, 0, 128));
            label.setForeground(Color.WHITE);
            //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            //int screenWidth = screenSize.width;
            //label.setMinimumSize(new Dimension(screenWidth,20));

            //label.setAlignmentX(Component.CENTER_ALIGNMENT);
            for (int k = 0; k < newgamegroupvec.size(); k++) {
                //Vector eachGame =(Vector) newgamegroupvec.get(k);
                Game g = (Game) newgamegroupvec.get(k);
                int leagueid = g.getLeague_id();
                Sport s = AppController.getSport("" + leagueid);
                LID = s.getParentleague_id();

            }
            JTable tablex;
            if ((gamegroupheaders.get(j) + "").contains("Soccer") || (LID == 9)) {
                //System.out.println("Soc=========================================================================="+gamegroupheaders.get(j)+"-lid="+LID);
                tablex = new SoccerTableView();


                //tablex.getColumnModel().setDefaultRenderer(Object.class, new LineRenderer("soccer"));
                //tablex.setDefaultRenderer(Object.class, new LineRenderer("soccer"));
                tablex.setRowHeight(60);
            } else {
                //System.out.println("Non-Socc================================================================================="+gamegroupheaders.get(j)+"-lid="+LID);
                tablex = new RegularTableView();

                //tablex.setDefaultRenderer(Object.class, new LineRenderer(""));

                tablex.setRowHeight(30);
                //tablex.getColumnModel().setDefaultRenderer(Object.class, new LineRenderer());
                //tablex.getColumnModel().setCellRenderer(new LineRenderer());
                //	tablex.setColumnModel( table0.getColumnModel() );

            }
            tablex.setName("table" + j);
            alltables.add(tablex);
            //jidetable feature tablex.setColumnAutoResizable(true);
            tablex.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            //System.out.println("eee");

            // tablex.setRowHeight(30);
            // LID==9;


            //LID = (int) gamegroupLeagueID.get(j);
            tablex.setColumnModel(table0.getColumnModel());

            tablex.setPreferredScrollableViewportSize(tablex.getPreferredSize());
            //System.out.println("ggg");
            tablex.setOpaque(true);

            tablex.changeSelection(0, 0, false, false);
            tablex.setAutoCreateColumnsFromModel(false);


            //System.out.println("aaa "+vecofgamegroups.size());
            LinesTableData dataModel = new LinesTableData(display, period, cleartime, newgamegroupvec, tablex, timesort, shortteam, opener, last);

            tablex.setModel(dataModel);

            //System.out.println("bbb");
            //AppController.addDataModel(dataModel);
            //System.out.println("ccc");
            datamodelsvec.add(dataModel);
            //System.out.println("ddd");

            //System.out.println("hhh");
            JScrollPane scrollPanex = new JScrollPane(tablex);
            //System.out.println("iii");


            //TableColumnManager tcmx= new TableColumnManager(tablex,"");

            TableColumnAdjuster tcax = new TableColumnAdjuster(tablex);


            //--------------------------------------------------------------------------------------------------------------------

            adjusters.add(tcax);


            scrollPanex.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            //System.out.println("lll");
            scrollPanex.getHorizontalScrollBar().setModel(scrollPane0.getHorizontalScrollBar().getModel());
            //System.out.println("mmm");
            if (j == vecofgamegroups.size() - 1) // last group
            {

                scrollPanex.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                lastScrollPane = scrollPanex;


                JTable fixedx = makeFixedRowHeader(AppController.getNumFixedCols(), tablex, true);
                fixedtables.add(fixedx);
                //fixedx.setColumnAutoResizable(true);
                fixedx.setColumnModel(fixed0.getColumnModel());
                //fixedx.setDefaultRenderer(Object.class, new LineRenderer());

                if ((gamegroupheaders.get(j) + "").contains("Soccer") || (LID == 9)) {
                    fixedx.setRowHeight(60);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer("soccer"));
                } else {
                    fixedx.setRowHeight(30);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer());
                }
                fixedx.setPreferredScrollableViewportSize(fixedx.getPreferredSize());

                TableColumnManager tcmx = new TableColumnManager(fixedx, "fixed");

                //owen added 6/26
                TableColumnAdjuster tcafx = new TableColumnAdjuster(fixedx);


                managers.add(tcmx);
                scrollPanex.setRowHeaderView(fixedx);
                //	scrollPanex.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedx.getTableHeader());
                tcax.adjustColumns();

            } else {
                scrollPanex.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

                JTable fixedx = makeFixedRowHeader(AppController.getNumFixedCols(), tablex, false);
                fixedtables.add(fixedx);
                fixedx.setColumnModel(fixed0.getColumnModel());

                //fixedx.setColumnAutoResizable(true);
                if ((gamegroupheaders.get(j) + "").contains("Soccer") || (LID == 9)) {
                    fixedx.setRowHeight(60);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer("soccer"));
                } else {
                    fixedx.setRowHeight(30);
                    fixedx.setDefaultRenderer(Object.class, new LineRenderer());
                }
                //fixedx.setRowHeight(30);

                fixedx.setPreferredScrollableViewportSize(fixedx.getPreferredSize());
                TableColumnManager tcmx = new TableColumnManager(fixedx, "fixed");


                //owen added 6/26
                TableColumnAdjuster tcafx = new TableColumnAdjuster(fixedx);

                managers.add(tcmx);
                scrollPanex.setRowHeaderView(fixedx);
                //	scrollPanex.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedx.getTableHeader());
                //	tcax.adjustColumns();

            }


            tablex.setTableHeader(null); // this has to be placed all the way down here!!!!
            //System.out.println("nnn");
            if (isShowHeaders()) {
                if (showit) {
                    tablePanel.add(label);
                }
            } else {
                //JLabel lab = new JLabel(new ImageIcon("blueline.png"));
                //lab.setPreferredSize(new Dimension(900,2));
                //tablePanel.add(lab);
            }
            if (newgamegroupvec.size() > 0) {
                scrollPanex.setPreferredSize(new Dimension(700, tablex.getRowHeight() * newgamegroupvec.size()));
            } else {
                scrollPanex.setPreferredSize(new Dimension(1, 1));
            }
            tablePanel.add(scrollPanex);
            scrollPanex.removeMouseWheelListener(scrollPanex.getMouseWheelListeners()[0]);


            //scrollPanex.setBorder(new EmptyBorder(0, 0, 0, 10));
            oldgamegroupvec = newgamegroupvec;
        }

        System.out.println("gamergroup headers end..." + new java.util.Date());

	 /*
		for(int j=0;j<hiddencols.size();j++)
		{
			Bookie b = (Bookie)hiddencols.get(j);
			//System.out.println("hiding "+b);
			tcm0.hideColumn(b.getShortname());
		}
	*/
        System.out.println("hidden end..." + new java.util.Date());
        //scrollPane0.setBorder(new EmptyBorder(0, 0, 0, 50));
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        //tablePanel.setBorder(new EmptyBorder(0, 0, 0, 5));
        scrollPane.getVerticalScrollBar().setUnitIncrement(29); // 29 has nothing to do with rowheight
        scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        JScrollBar bar = lastScrollPane.getHorizontalScrollBar();


        removeAll();
        revalidate();
        add(scrollPane0, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(bar, BorderLayout.PAGE_END);
        AppController.addDataModels(getDataModels());
        System.out.println("Datamodels size is :" + AppController.getDataModels().size());
        adjustcols(false);
        firedatamodels();

        Timer timer2 = new Timer(500, new ActionListener() { //Change parameters to your needs.
            public void actionPerformed(ActionEvent e) {
                try {
                    //System.out.println("about to run timer2!");

                    adjustcols(false);
                    timer2count++;
                    if (timer2count > 12) {
                        timer2count = 0;
                        ((Timer) e.getSource()).stop();
                    }
                } catch (Exception ex) {
                    System.out.println("exception firing data models " + ex);
                }

            }
        });
        //timer2.setRepeats(false);

        timer2.start();
        System.out.println("timer2 start");

        System.out.println("drawit end..." + new java.util.Date());
    }

    public void firedatamodels() {
        for (int i = 0; i < datamodelsvec.size(); i++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(i);
            ltd.fire();
        }
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
        //	SwingUtilities.invokeLater(new Runnable()
        //	{
        //	public void run()
        //	{
        for (int i = 0; i < adjusters.size(); i++) {
            TableColumnAdjuster col = (TableColumnAdjuster) adjusters.get(i);
            col.adjustColumns(includeheader);
        }
        //}
        //});
    }

    public void firedatamodels2() {
        for (int i = 0; i < datamodelsvec.size(); i++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(i);
            checkAndRunInEDT(() -> ltd.fireTableStructureChanged());
        }
    }

    public void destroyMe() {
        AppController.removeDataModels(getDataModels());
        for (int i = 0; i < alltables.size(); i++) {
            JTable table = (JTable) alltables.get(i);
            table = null;
        }
        for (int i = 0; i < fixedtables.size(); i++) {
            JTable table = (JTable) fixedtables.get(i);
            table = null;
        }
        for (int i = 0; i < renderers.size(); i++) {
            LineRenderer ren = (LineRenderer) renderers.get(i);
            ren = null;
        }
        for (int i = 0; i < columns.size(); i++) {
            TableColumn col = (TableColumn) columns.get(i);
            col = null;
        }
        for (int i = 0; i < managers.size(); i++) {
            TableColumnManager col = (TableColumnManager) managers.get(i);
            col = null;
        }
        for (int i = 0; i < adjusters.size(); i++) {
            TableColumnAdjuster col = (TableColumnAdjuster) adjusters.get(i);
            col = null;
        }
        for (int i = 0; i < datamodelsvec.size(); i++) {
            LinesTableData ltd = (LinesTableData) datamodelsvec.get(i);
            ltd = null;
        }
        alltables.clear();
        fixedtables.clear();
        renderers.clear();
        columns.clear();
        managers.clear();
        adjusters.clear();
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
        System.out.println("destroyed mainscreen!!!!");

    }
}
