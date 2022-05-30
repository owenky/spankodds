package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class WelcomeImagePane extends JPanel implements LayerAnchored

{
    private final AnchoredLayeredPane anchoredLayeredPane;
    Image spankyimage;
    public WelcomeImagePane(AnchoredLayeredPane anchoredLayeredPane) {
        this.anchoredLayeredPane = anchoredLayeredPane;
        initScreen();

    }
    @Override
    public AnchoredLayeredPane getAnchoredLayeredPane() {
        return anchoredLayeredPane;
    }

    private void initScreen() {
        //setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBorder(null);

        try
        {
            //URL imgResource = Utils.getResource("spankoddsrgb.png");
            //spankyimage = ImageIO.read(imgResource);
            spankyimage =  Utils.getImage("spankoddsrgb.png");
        }
        catch(Exception ex) { System.out.println("error reading spanky image "+ex); }
        try {

            new SoundPlayer("welcometospankodds.wav", true);
        } catch (Exception ex) {
            showMessageDialog(null, "Error Playing welcome File!");
            log(ex);
        }
        new Timer(2000, (e) -> {  anchoredLayeredPane.close(); }).start();
    }
    @Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);
        G.drawImage(spankyimage, 0, 0, this.getWidth(), this.getHeight(), null);
    }


}
