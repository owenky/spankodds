package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import java.awt.Dimension;


public class SportCustomTab {

    private final SportsTabPane stp;
    private final String sportName;
    private AnchoredLayeredPane anchoredLayeredPane;
    private static final int DefaultConfigPanelHeight=730;
    private static final int DefaultConfigPanelWidth=800;
    public SportCustomTab(SportsTabPane stp, String sportName) {

        this.stp = stp;
        this.sportName = sportName;
    }
    public void show() {
        anchoredLayeredPane = new AnchoredLayeredPane(stp);
        anchoredLayeredPane.setTitle(sportName + " Preferences");
        SportType st = SportType.findBySportName(sportName);
        SportConfigurator sportConfigurator = new SportConfigurator(anchoredLayeredPane,st);
        anchoredLayeredPane.openAndCenter(sportConfigurator.getMainPanel(),new Dimension(DefaultConfigPanelWidth,DefaultConfigPanelHeight),false);
    }
}