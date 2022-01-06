package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Supplier;


public class SportCustomTab {

    private final SportsTabPane stp;
    private final String sportName;
    private static AnchoredLayeredPane anchoredLayeredPane;
    private static final int DefaultConfigPanelHeight=730;
    private static final int DefaultConfigPanelWidth=800;
    private static final Color ConfigPanelBck = Color.DARK_GRAY;
    public SportCustomTab(SportsTabPane stp, String sportName) {

        this.stp = stp;
        this.sportName = sportName;
        if ( null != anchoredLayeredPane) {
            anchoredLayeredPane.close();
        }
    }
    public void show() {
        SportType st = SportType.findBySportName(sportName);
        SportConfigurator sportConfigurator = new SportConfigurator(st);

//        doFrame(sportConfigurator,st);
        doLayerPane(sportConfigurator,st);
    }
    private void doLayerPane(SportConfigurator sportConfigurator,SportType st) {
        sportConfigurator.setCloseActionListener((ae)->{
            if ( null != anchoredLayeredPane) {
                anchoredLayeredPane.close();
            }
        });
        anchoredLayeredPane = new AnchoredLayeredPane(stp, LayedPaneIndex.SportConfigIndex);
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BorderLayout());
        JPanel mainPanel = sportConfigurator.getMainPanel();
        JPanel titlePanel = sportConfigurator.getTitlePanel();
//        mainPanel.setBackground(ConfigPanelBck);
//        titlePanel.setBackground(ConfigPanelBck);
        configPanel.add(titlePanel,BorderLayout.NORTH);
        configPanel.add(mainPanel,BorderLayout.CENTER);
        JScrollPane jScrollPane = new JScrollPane(configPanel);
        jScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
        anchoredLayeredPane.setUserPane(jScrollPane,false);
        anchoredLayeredPane.openAndAnchoredAt(getLocationSuppr());
    }
    private Supplier<Point> getLocationSuppr() {

        return ()->{
            Dimension selectedPaneDim = stp.getSelectedComponent().getSize();
            int maxUserCompWidth = (int)selectedPaneDim.getWidth()-30;
            int maxUserCompHeight = (int)selectedPaneDim.getHeight()-30;

            int userCompWidth= Math.min(maxUserCompWidth, DefaultConfigPanelWidth);
            int userCompHeight= Math.min(maxUserCompHeight, DefaultConfigPanelHeight);
            anchoredLayeredPane.getUserComponent().setSize(userCompWidth,userCompHeight);

            int tabIndex = stp.indexOfTab(sportName);
            Rectangle r = stp.getUI().getTabBounds(stp,tabIndex);
            Point tabPaneAnchor = stp.getLocationOnScreen();
            return new Point( tabPaneAnchor.x+ (int)((selectedPaneDim.getWidth()-userCompWidth)/2), tabPaneAnchor.y+ (int)r.getHeight()+30);

        };
    }
    private void doFrame(SportConfigurator sportConfigurator,SportType st) {
        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        final JFrame jfrm = new JFrame(sportName + " Preferences");

        sportConfigurator.setCloseActionListener((ae)->{
            jfrm.setVisible(false);
            jfrm.dispose();
        });
        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new BorderLayout());
        jfrm.getContentPane().add(sportConfigurator.getMainPanel(),BorderLayout.CENTER);
        // Give the frame an initial size.
        jfrm.setSize(840, 840);
//        // Display the frame.
        jfrm.setVisible(true);
    }
}