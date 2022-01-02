package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.function.Supplier;


public class SportCustomTab {

    private final SportsTabPane stp;
    private final String sportName;
    private AnchoredLayeredPane anchoredLayeredPane;
    public SportCustomTab(SportsTabPane stp, String sportName) {

        this.stp = stp;
        this.sportName = sportName;
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
                anchoredLayeredPane.hide();
            }
        });
        anchoredLayeredPane = new AnchoredLayeredPane(stp, LayedPaneIndex.SportConfigIndex);
        anchoredLayeredPane.setUserPane(sportConfigurator.getConfigurationPanel());
        anchoredLayeredPane.openAndAnchoredAt(getLocationSuppr());
    }
    private Supplier<Point> getLocationSuppr() {

        int tabIndex = stp.indexOfTab(sportName);
        Rectangle r = stp.getUI().getTabBounds(stp,tabIndex);
        Point tabPaneAnchor = stp.getLocationOnScreen();
        Point loc = new Point(tabPaneAnchor.x+ (int)r.getX()+(int)(r.getWidth()/2), tabPaneAnchor.y+ (int)r.getHeight()+2);
        return ()->loc;
    }
    private void doFrame(SportConfigurator sportConfigurator,SportType st) {
        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm = new JFrame(sportName + " Preferences");
        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new BorderLayout());
        jfrm.getContentPane().add(sportConfigurator.getConfigurationPanel(),BorderLayout.CENTER);
        // Give the frame an initial size.
        jfrm.setSize(840, 840);
//        // Display the frame.
        jfrm.setVisible(true);
    }
}