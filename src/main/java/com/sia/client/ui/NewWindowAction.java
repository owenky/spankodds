package com.sia.client.ui;

import com.sia.client.config.Utils;
//import com.sia.client.model.User;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.net.URL;

public class NewWindowAction implements ActionListener {


    public void actionPerformed(ActionEvent e) {

        // owen still need!


        SportsTabPane stbnew = new SportsTabPane();
        TopView tv = new TopView(stbnew);
        SportsMenuBar smb = new SportsMenuBar(stbnew, tv);
        //new OddsFrame(stbnew,tv);

        JFrame frame = new JFrame(AppController.getUser().getUsername()+" Logged In");
        String spankoddsicon = "spanky.jpg";

        try {
            URL imgResource = Utils.getMediaResource(spankoddsicon);
            Image spankyimage = ImageIO.read(imgResource);
            frame.setIconImage(spankyimage);
        }
        catch(Exception ex)
        {
            System.out.println("exception loading image!! "+ex);
        }
        frame.setJMenuBar(smb);

        //	SportsTabPane stbnew = (SportsTabPane)((SportsTabPane)stb).clone();
        //	TopView tv = (TopView)this.clone();
        //owen gotta find a way to deep clone stb

        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        //mainpanel.add(tv,BorderLayout.CENTER);
        mainpanel.add(tv, BorderLayout.PAGE_START);
        mainpanel.add(stbnew, BorderLayout.CENTER);
        //frame.setLayout(new BorderLayout());

        frame.setContentPane(mainpanel);
        frame.setSize(600, 600);

        AppController.addFrame(frame, stbnew);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AppController.getUserPrefsProducer().sendUserPrefs();
                System.out.println("Window Closing2! ");
                AppController.removeFrame(frame);

            }
        });


        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


}