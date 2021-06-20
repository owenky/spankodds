package com.sia.client.ui;

import com.sia.client.config.Utils;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import static com.sia.client.config.Utils.log;

public class NewWindowAction implements ActionListener {


    public void actionPerformed(ActionEvent e) {

        SportsTabPane stbnew = new SportsTabPane();
        TopView tv = new TopView(stbnew);
        tv.initComponents();
        SportsMenuBar smb = new SportsMenuBar(stbnew, tv);

        JFrame frame = new JFrame(AppController.getUser().getUsername() + " Logged In");
        String spankoddsicon = "spanky.jpg";

        try {
            URL imgResource = Utils.getMediaResource(spankoddsicon);
            Image spankyimage = ImageIO.read(imgResource);
            frame.setIconImage(spankyimage);
        } catch (Exception ex) {
            log(ex);
        }
        frame.setJMenuBar(smb);

        JPanel mainpanel = new JPanel();
        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(tv, BorderLayout.PAGE_START);
        mainpanel.add(stbnew, BorderLayout.CENTER);

        frame.setContentPane(mainpanel);
        frame.setSize(600, 600);

        AppController.addFrame(frame, stbnew);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AppController.getUserPrefsProducer().sendUserPrefs();
                log("Window Closing2! ");
                AppController.removeFrame(frame);

            }
        });


        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


}