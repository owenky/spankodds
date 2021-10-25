package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.SportType;
import com.sia.client.model.TabUnhideListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class SportsMenuBar extends JMenuBar {

    SportsTabPane stb;
    TopView tv;
    JMenu filemenu = new JMenu("File");
    JMenu bookiemenu = new JMenu("Columns");
    JMenu linealertsmenu = new JMenu("Line Alerts");
    JMenu gamealertsmenu = new JMenu("Game Alerts");
    JMenu tabsmenu = new JMenu("Tabs");
    JMenu windowmenu = new JMenu("Window");

    public SportsMenuBar() {
        super();
        this.init();
    }

    public void init() {

        add(filemenu);

        JMenuItem storeprefs = new JMenuItem("Store User Prefs");
        storeprefs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // need to store user prefs
                AppController.getUserPrefsProducer().sendUserPrefs();
            }
        });
        filemenu.add(storeprefs);

        JMenuItem logout = new JMenuItem("Exit...");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // need to store user prefs
                AppController.getUserPrefsProducer().sendUserPrefs();
                System.exit(0);
            }
        });
        filemenu.add(logout);


        add(bookiemenu);

        JMenuItem bookiecolumn = new JMenuItem("Manage");
        bookiecolumn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // AudioClip clipfinal = new AudioClip("c:\\spankoddsclient\\final.wav");
                //  clipfinal.play();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        BookieColumnController2 bcc2 = new BookieColumnController2();


                    }
                });


            }
        });
        JMenuItem bookiecolumn1 = new JMenuItem("Chart");
        bookiecolumn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // AudioClip clipfinal = new AudioClip("c:\\spankoddsclient\\final.wav");
                //  clipfinal.play();
                checkAndRunInEDT(() -> new ChartHome().setVisible(true));


            }
        });
        bookiemenu.add(bookiecolumn);
        bookiemenu.add(bookiecolumn1);

        add(linealertsmenu);


        JMenuItem generallinealert = new JMenuItem("Line Moves");
        generallinealert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                LineAlert la = new LineAlert("Started");
            }
        });
        JMenuItem majorlinemove = new JMenuItem("Line Seekers");
        JMenuItem openers = new JMenuItem("Openers");
        openers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                LineAlertOpeners la = new LineAlertOpeners();
            }
        });
        linealertsmenu.add(generallinealert);
        linealertsmenu.add(majorlinemove);
        linealertsmenu.add(openers);


        add(gamealertsmenu);

        JMenuItem started = new JMenuItem("Started");
        started.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Started");
            }
        });
        JMenuItem finals = new JMenuItem("Finals");
        finals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert(SiaConst.FinalStr);
            }
        });
        JMenuItem halftimes = new JMenuItem("Halftimes");
        halftimes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert(SiaConst.HalfTimeStr);
            }
        });

        JMenuItem lineups = new JMenuItem("Lineups");
        lineups.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Lineup");
            }
        });
        JMenuItem officials = new JMenuItem("Officials");
        officials.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Official");
            }
        });
        JMenuItem injuries = new JMenuItem("Injuries");
        injuries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Injury");
            }
        });
        JMenuItem timechange = new JMenuItem("Time Changes");
        timechange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Time Change");
            }
        });
        JMenuItem limitchange = new JMenuItem("Limit Changes");
        limitchange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GameAlert ga = new GameAlert("Limit Change");
            }
        });


        JMenuItem test = new JMenuItem("Test");
        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
            }
        });

        JMenuItem test2 = new JMenuItem("Test NW");
        test2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.NORTH_WEST, AppController.getMainTabPane());
            }
        });
        JMenuItem test3 = new JMenuItem("Test SW");
        test3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_WEST, AppController.getMainTabPane());
            }
        });
        JMenuItem test4 = new JMenuItem("Test SE");
        test4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_EAST, AppController.getMainTabPane());
            }
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
        //gamealertsmenu.add(test2);
        //gamealertsmenu.add(test3);
        //gamealertsmenu.add(test4);


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
        //SportsTabPane stb=new SportsTabPane();
        //AppController.addTabPane(stb);
        //********************Football*******************
        JMenu football = new JMenu("Football");
        tabsmenu.add(football);
        football.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                football.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                Vector tabpanes = AppController.getTabPanes();


                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Football")) {
                        tp = 1;
                        break;
                    }
                }
                football.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Football"));
                    }
                });
                football.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Football", stb.indexOfTab("Football"));
                    }
                });
                if (tp == 1) {
                    football.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {

                            int idx = stb.indexOfTab("Football");

                            AppController.SpotsTabPaneVector.remove(0);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {

                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);

                                tp.remove(idx);

                                tp.setSelectedIndex(0);

                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    football.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Football,0).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        //*******Basketball	**************
        JMenu basketball = new JMenu("Basketball");
        tabsmenu.add(basketball);
        basketball.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                basketball.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Basketball")) {
                        tp = 1;
                        break;
                    }
                }
                basketball.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Basketball"));
                    }
                });
                basketball.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Basketball", stb.indexOfTab("Basketball"));
                    }
                });
                if (tp == 1) {
                    basketball.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Basketball");
                            AppController.SpotsTabPaneVector.remove(1);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    basketball.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Basketball,1).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
        //***********Baseball***********************
        JMenu baseball = new JMenu("Baseball");
        tabsmenu.add(baseball);
        baseball.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                baseball.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Baseball")) {
                        tp = 1;
                        break;
                    }
                }
                baseball.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Baseball"));
                    }
                });
                baseball.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Baseball", stb.indexOfTab("Baseball"));
                    }
                });
                if (tp == 1) {
                    baseball.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Baseball");
                            AppController.SpotsTabPaneVector.remove(2);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    baseball.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Baseball,2).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });


        //***********************hockey*********************
        JMenu hockey = new JMenu("Hockey");
        tabsmenu.add(hockey);
        hockey.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                hockey.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Hockey")) {
                        tp = 1;
                        break;
                    }
                }
                hockey.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Hockey"));
                    }
                });
                hockey.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Hockey", stb.indexOfTab("Hockey"));
                    }
                });
                if (tp == 1) {
                    hockey.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Hockey");
                            AppController.SpotsTabPaneVector.remove(3);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    hockey.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Hockey,3).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        //***********************fighting****************************
        JMenu fighting = new JMenu("Fighting");
        tabsmenu.add(fighting);
        fighting.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                fighting.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Fighting")) {
                        tp = 1;
                        break;
                    }
                }
                fighting.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Fighting"));
                    }
                });
                fighting.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Fighting", stb.indexOfTab("Fighting"));
                    }
                });
                if (tp == 1) {
                    fighting.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Fighting");
                            AppController.SpotsTabPaneVector.remove(4);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    fighting.add(unhide);
                    unhide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            new TabUnhideListener(SportType.Fighting,4).unHide();
                            go.setEnabled(true);
                        }
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });


        //*****************soccer*************************
        JMenu soccer = new JMenu(SiaConst.SoccerStr);
        tabsmenu.add(soccer);
        soccer.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                soccer.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase(SiaConst.SoccerStr)) {
                        tp = 1;
                        break;
                    }
                }
                soccer.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab(SiaConst.SoccerStr));
                    }
                });
                soccer.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab(SiaConst.SoccerStr, stb.indexOfTab(SiaConst.SoccerStr));
                    }
                });
                if (tp == 1) {
                    soccer.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab(SiaConst.SoccerStr);
                            AppController.SpotsTabPaneVector.remove(5);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    soccer.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Soccer,5).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });


        //************************golf*********************
        JMenu golf = new JMenu("Golf");
        tabsmenu.add(golf);
        golf.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                golf.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Golf")) {
                        tp = 1;
                        break;
                    }
                }
                golf.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Golf"));
                    }
                });
                golf.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Golf", stb.indexOfTab("Golf"));
                    }
                });
                if (tp == 1) {
                    golf.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Golf");
                            AppController.SpotsTabPaneVector.remove(7);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    golf.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Golf,7).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });


        //**********************autoracing****************************
        JMenu autoracing = new JMenu("Auto Racing");
        tabsmenu.add(autoracing);
        autoracing.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                autoracing.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Auto Racing")) {
                        tp = 1;
                        break;
                    }
                }
                autoracing.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Auto Racing"));
                    }
                });
                autoracing.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Auto Racing", stb.indexOfTab("Auto Racing"));
                    }
                });
                if (tp == 1) {
                    autoracing.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Auto Racing");
                            AppController.SpotsTabPaneVector.remove(6);

                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    autoracing.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.AutoRacing,6).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });

        //*******Tennis*****************
        JMenu tennis = new JMenu("Tennis");
        tabsmenu.add(tennis);
        tennis.addMenuListener(new MenuListener() {

            public void menuSelected(MenuEvent e) {
                tennis.removeAll();
                JMenuItem go = new JMenuItem("Goto");
                JMenuItem manage = new JMenuItem("Manage");
                JMenuItem hide = new JMenuItem("Hide");
                JMenuItem unhide = new JMenuItem("unHide");
                int tc = stb.getTabCount();
                int tp = 0;
                int j;
                for (j = 0; j < tc; j++) {
                    if (stb.getTitleAt(j).equalsIgnoreCase("Tennis")) {
                        tp = 1;
                        break;
                    }
                }
                tennis.add(go);
                go.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        stb.setSelectedIndex(stb.indexOfTab("Tennis"));
                    }
                });
                tennis.add(manage);
                manage.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new SportCustomTab("Tennis", stb.indexOfTab("Tennis"));
                    }
                });
                if (tp == 1) {
                    tennis.add(hide);
                    hide.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            int idx = stb.indexOfTab("Tennis");
                            AppController.SpotsTabPaneVector.remove(8);
                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                        }
                    });
                } else {
                    tennis.add(unhide);
                    unhide.addActionListener(ae -> {
                        new TabUnhideListener(SportType.Tennis,8).unHide();
                        go.setEnabled(true);
                    });
                    go.setEnabled(false);
                }

            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
        //***********************end of Tab Menus******************************

        for (int i = 0; i < AppController.customTabsVec.size(); i++) {
            String tabinfo = (String) AppController.customTabsVec.elementAt(i);
            String[] tabarr = tabinfo.split("\\*");
            String tabname = tabarr[1];
            JMenu temp = new JMenu(tabname);
            tabsmenu.add(temp);
            JMenuItem go = new JMenuItem("Goto");
            JMenuItem manage = new JMenuItem("Edit");
            JMenuItem hide = new JMenuItem("Remove");


            temp.add(go);
            go.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    stb.setSelectedIndex(stb.indexOfTab(temp.getText()));
                }
            });
            temp.add(manage);
            manage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            int idx = stb.indexOfTab(temp.getText());
                            new CustomTab2(stb.getTitleAt(idx), idx);

                        }
                    });

                }
            });

            temp.add(hide);
            hide.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //stb.remove(idx);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            int idx = stb.indexOfTab(temp.getText());
                            AppController.removeCustomTab(stb.getTitleAt(idx));
                            Vector tabpanes = AppController.getTabPanes();
                            log("tabpanes size= " + tabpanes.size());
                            for (int j = 0; j < tabpanes.size(); j++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(j);
                                tp.setSelectedIndex(0);
                                tp.remove(idx);
                            }
                            go.setEnabled(false);
                            hide.setEnabled(false);
                        }
                    });


                }
            });

        }


        JMenuItem addnew = new JMenuItem("Add New...");
        tabsmenu.add(addnew);
        addnew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new CustomTab2();

                    }
                });
            }
        });


    }

    public SportsMenuBar(SportsTabPane stb, TopView tv) {
        super();
        this.stb = stb;
        this.tv = tv;
        this.init();
        AppController.addMenuBar(this);
    }
}