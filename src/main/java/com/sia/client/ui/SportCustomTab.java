package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class SportCustomTab {

    private final SportsTabPane stp;
    private final String sportName;
    private static AnchoredLayeredPane anchoredLayeredPane;
    private static final int DefaultConfigPanelHeight=730;
    private static final int DefaultConfigPanelWidth=800;
    public SportCustomTab(SportsTabPane stp, String sportName) {

        this.stp = stp;
        this.sportName = sportName;
    }
    public void show() {
        if ( null != anchoredLayeredPane) {
            anchoredLayeredPane.close();
        }
        anchoredLayeredPane = new AnchoredLayeredPane(stp);
        SportType st = SportType.findBySportName(sportName);
        SportConfigurator sportConfigurator = new SportConfigurator(anchoredLayeredPane,st);

        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BorderLayout());
        JPanel mainPanel = sportConfigurator.getMainPanel();
        JPanel titlePanel = sportConfigurator.getTitlePanel();
        configPanel.add(titlePanel,BorderLayout.NORTH);
        configPanel.add(mainPanel,BorderLayout.CENTER);
        JScrollPane jScrollPane = new JScrollPane(configPanel);
        jScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
        jScrollPane.setPreferredSize(new Dimension(DefaultConfigPanelWidth,DefaultConfigPanelHeight));
        anchoredLayeredPane.openAndCenter(jScrollPane,false);
    }
}