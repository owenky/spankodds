package com.sia.client.ui.simulator;

import javax.swing.JTabbedPane;

public class SportsTabPaneTest extends JTabbedPane {

    private int currentTabIndex = 0;
    private int previousTabIndex = 0;
    public SportsTabPaneTest() {
        addListeners();
    }
    private void addListeners() {

        this.addChangeListener(ce -> {

            previousTabIndex = currentTabIndex;
            currentTabIndex = getSelectedIndex();
            MainScreenTest oldms = (MainScreenTest) getComponentAt(previousTabIndex);
            oldms.destroyMe();
            Object pane =  getComponentAt(currentTabIndex);
            if ( pane instanceof MainScreenTest) {
                MainScreenTest newms = (MainScreenTest)pane;
                newms.createMe();
            }
        });
    }
}
