package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import static com.sia.client.config.Utils.checkAndRunInEDT;

public class SportsMenuBar extends JMenuBar {

    private final SportsTabPane stb;
    TopView tv;
    JMenu filemenu = new JMenu("File");
    JMenu bookiemenu = new JMenu("Columns");
    JMenu linealertsmenu = new JMenu("Line Alerts");
    JMenu gamealertsmenu = new JMenu("Game Alerts");
    JMenu tabsmenu = new JMenu("Tabs");
    JMenu windowmenu = new JMenu("Window");

    public SportsMenuBar(SportsTabPane stb, TopView tv) {
        super();
        this.stb = stb;
        this.tv = tv;
        this.init();
        AppController.addMenuBar(this);
    }
    public void init() {

        add(filemenu);

        JMenuItem storeprefs = new JMenuItem("Store User Prefs");
        storeprefs.addActionListener(ev -> AppController.getUserPrefsProducer().sendUserPrefs());
        filemenu.add(storeprefs);

        JMenuItem logout = new JMenuItem("Exit...");
        logout.addActionListener(ev -> {
            // need to store user prefs
            AppController.getUserPrefsProducer().sendUserPrefs();
            System.exit(0);
        });
        filemenu.add(logout);


        add(bookiemenu);

        JMenuItem bookiecolumn = new JMenuItem("Manage");
        bookiecolumn.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
            AnchoredLayeredPane anchoredLayeredPane = new AnchoredLayeredPane(stb);
            BookieColumnController2 bcc2 = new BookieColumnController2(anchoredLayeredPane);
            bcc2.setPreferredSize(new Dimension(700, 700));
            bcc2.openAndCenter(false);
        }));
        JMenuItem bookiecolumn1 = new JMenuItem("Chart");
        bookiecolumn1.addActionListener(ae -> checkAndRunInEDT(() -> new ChartHome().setVisible(true)));
        bookiemenu.add(bookiecolumn);
        bookiemenu.add(bookiecolumn1);

        add(linealertsmenu);


        JMenuItem generallinealert = new JMenuItem("Line Moves");
        generallinealert.addActionListener(ae -> {
            LineAlert la = new LineAlert("Started");
        });
        JMenuItem majorlinemove = new JMenuItem("Line Seekers");
        JMenuItem openers = new JMenuItem("Openers");
        openers.addActionListener(ae -> {
            LineAlertOpeners la = new LineAlertOpeners();
        });
        linealertsmenu.add(generallinealert);
        linealertsmenu.add(majorlinemove);
        linealertsmenu.add(openers);


        add(gamealertsmenu);

        JMenuItem started = new JMenuItem("Started");
        started.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Started");
        });
        JMenuItem finals = new JMenuItem("Finals");
        finals.addActionListener(ae -> {
            GameAlert ga = new GameAlert(SiaConst.FinalStr);
        });
        JMenuItem halftimes = new JMenuItem("Halftimes");
        halftimes.addActionListener(ae -> {
            GameAlert ga = new GameAlert(SiaConst.HalfTimeStr);
        });

        JMenuItem lineups = new JMenuItem("Lineups");
        lineups.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Lineup");
        });
        JMenuItem officials = new JMenuItem("Officials");
        officials.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Official");
        });
        JMenuItem injuries = new JMenuItem("Injuries");
        injuries.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Injury");
        });
        JMenuItem timechange = new JMenuItem("Time Changes");
        timechange.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Time Change");
        });
        JMenuItem limitchange = new JMenuItem("Limit Changes");
        limitchange.addActionListener(ae -> {
            GameAlert ga = new GameAlert("Limit Change");
        });


        JMenuItem test = new JMenuItem("Test");
        test.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
        });

        JMenuItem test2 = new JMenuItem("Test NW");
        test2.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.NORTH_WEST, AppController.getMainTabPane());
        });
        JMenuItem test3 = new JMenuItem("Test SW");
        test3.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_WEST, AppController.getMainTabPane());
        });
        JMenuItem test4 = new JMenuItem("Test SE");
        test4.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_EAST, AppController.getMainTabPane());
        });
        gamealertsmenu.add(started);
        gamealertsmenu.add(finals);
        gamealertsmenu.add(halftimes);
        gamealertsmenu.add(lineups);
        gamealertsmenu.add(officials);
        gamealertsmenu.add(injuries);
        gamealertsmenu.add(timechange);
        gamealertsmenu.add(limitchange);
        gamealertsmenu.add(test);
        add(tabsmenu);
        populateTabsMenu();


        add(windowmenu);
        JMenuItem newwindow = new JMenuItem("New..");
        newwindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0)); // 0 means no modifiers
        newwindow.addActionListener(AppController.getNewWindowAction());
        windowmenu.add(newwindow);


    }

    public void populateTabsMenu() {
        tabsmenu.removeAll();
        /*
         * for pre defined sports.
         */
        for(SportType st: SportType.getPreDefinedSports()) {
            JMenu menu = new PredefinedSportMenu(stb,st).getMenu();
            tabsmenu.add(menu);
        }
        /*
         * for custom sports
         */
        for (int i = 0; i < AppController.customTabsVec.size(); i++) {
            String tabinfo =  AppController.customTabsVec.elementAt(i);
            String[] tabarr = tabinfo.split("\\*");
            String tabname = tabarr[1];
            JMenu temp = new JMenu(tabname);
            tabsmenu.add(temp);
            JMenuItem go = new JMenuItem("Goto");
            JMenuItem manage = new JMenuItem("Edit");
            JMenuItem hide = new JMenuItem("Remove");


            temp.add(go);
            go.addActionListener(ae -> stb.setSelectedIndex(stb.indexOfTab(temp.getText())));
            temp.add(manage);
            manage.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
                int idx = stb.indexOfTab(temp.getText());
                new CustomTab2(stb.getWindowIndex(),stb.getTitleAt(idx), idx);

            }));

            temp.add(hide);
            hide.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
                int idx = stb.indexOfTab(temp.getText());
                AppController.removeCustomTab(stb.getTitleAt(idx));
                SpankyWindow.applyToAllWindows((tp)-> {
                    tp.setSelectedIndex(0);
                    tp.remove(idx);
                });
                go.setEnabled(false);
                hide.setEnabled(false);
            }));

        }
        JMenuItem addnew = new JMenuItem("Add New...");
        tabsmenu.add(addnew);
        addnew.addActionListener(ae -> SwingUtilities.invokeLater(() -> new CustomTab2(stb.getWindowIndex())));
    }
}