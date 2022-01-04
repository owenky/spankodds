package com.sia.client.ui;

import com.sia.client.model.SportType;
import com.sia.client.model.TabUnhideListener;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class PredefinedSportMenu {

    private final SportType sportType;
    private final SportsTabPane stp;
    private JMenu menu = null;

    public PredefinedSportMenu(SportsTabPane stp, SportType sportType) {
        this.sportType = sportType;
        this.stp = stp;
    }
    public JMenu getMenu() {
        if ( null == menu) {
            int sportTabIndex = SportType.getPreDefinedSportTabIndex(sportType);
            if ( 0 > sportTabIndex) {
                throw new IllegalArgumentException("Sport name "+sportType.getSportName()+" is not predefined sport.");
            }
            final String sportName = sportType.getSportName();
            menu = new JMenu(sportName);
            menu.addMenuListener(new MenuListener() {
                @Override
                public void menuSelected(MenuEvent e) {
                    menu.removeAll();
                    JMenuItem go = new JMenuItem("Goto");
                    JMenuItem manage = new JMenuItem("Manage");
                    JMenuItem hide = new JMenuItem("Hide");
                    JMenuItem unhide = new JMenuItem("unHide");
                    final int index = stp.indexOfTab(sportName);

                    menu.add(go);
                    go.addActionListener(ae -> stp.setSelectedIndex(index));
                    menu.add(manage);
                    manage.addActionListener(ae -> new SportCustomTab(stp,sportName).show());

                    if (0<=index) {
                        menu.add(hide);
                        hide.addActionListener(ae -> {
                            AppController.SpotsTabPaneVector.remove(sportTabIndex);
                            SpankyWindow.applyToAllWindows((window)-> {
                                window.setSelectedIndex(0);
                                window.remove(index);
                            });
                            go.setEnabled(false);
                        });
                    } else {
                        menu.add(unhide);
                        unhide.addActionListener(ae -> {
//                            new TabUnhideListener(sportType,1).unHide();
                            new TabUnhideListener(sportType,sportTabIndex).unHide();
                            go.setEnabled(true);
                        });
                        go.setEnabled(false);
                    }

                }
                @Override
                public void menuDeselected(MenuEvent e) {
                }
                @Override
                public void menuCanceled(MenuEvent e) {
                }
            });
        }
        return menu;
    }
}
