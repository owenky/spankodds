package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.model.SportType;

import javax.swing.JFrame;
import java.awt.BorderLayout;


public class SportCustomTab {

    public SportCustomTab(String sportName) {

        SportType st = SportType.findBySportName(sportName);
        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm = new JFrame(sportName + " Preferences");

        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new BorderLayout());
        SportConfigurator sportConfigurator = new SportConfigurator(st);
        jfrm.getContentPane().add(sportConfigurator.getConfigurationPanel(),BorderLayout.CENTER);
        // Give the frame an initial size.
        jfrm.setSize(840, 840);

//        // Display the frame.
        jfrm.setVisible(true);
    }
}