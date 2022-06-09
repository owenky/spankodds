package com.sia.client.ui;
import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PaintPanel;
import com.jidesoft.utils.PortingUtils;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;

import javax.swing.*;
import java.awt.*;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class WelcomeMessage
{

    Image spankyimage;
    public WelcomeMessage()
    {
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



       final JLabel imagelabel = new JLabel(new ImageIcon(spankyimage));
        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(imagelabel, BorderLayout.CENTER);
        panel.setOpaque(true);
       // panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height, new Color(212, 208, 200)));
        panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(0, 0, 0), 0, panel.getPreferredSize().height, new Color(0, 0, 0)));
        //https://docs.oracle.com/javase/tutorial/uiswing/misc/modality.html+
        Alert alert = new Alert();
        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));

        JideButton closeButton = createButton(Utils.getImageIcon(("close.png")));
        closeButton.addActionListener(e -> alert.hidePopupImmediately());
        rightPanel.add(closeButton);

        JPanel topPanel = JideSwingUtilities.createTopPanel(rightPanel);
        panel.add(topPanel, BorderLayout.AFTER_LINE_ENDS);






        alert.getContentPane().setLayout(new BorderLayout());
        alert.getContentPane().add(panel);

        alert.setOwner(AppController.getMainTabPane());
        alert.setResizable(false);
        alert.setMovable(false);
        alert.setTransient(false);
        alert.setTimeout(3000);
        alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

        CustomAnimation showAnimation = new CustomAnimation();

        showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        showAnimation.setDirection(CustomAnimation.TOP_LEFT);
        //showAnimation.setDirection(CustomAnimation.BOTTOM);
        showAnimation.setEffect(CustomAnimation.EFFECT_FLY);

        //showAnimation.setSpeed(CustomAnimation.SPEED_MEDIUM);
        showAnimation.setSpeed(CustomAnimation.SPEED_FAST);
        showAnimation.setFunctionY(CustomAnimation.FUNC_BOUNCE);
        showAnimation.setFunctionX(CustomAnimation.FUNC_BOUNCE);

       // CustomAnimation hideAnimation = new CustomAnimation();
       // hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
       // hideAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);
       // hideAnimation.setEffect(CustomAnimation.EFFECT_FADE);
       // hideAnimation.setEffect(CustomAnimation.EFFECT_FLY);
       // hideAnimation.setDirection(CustomAnimation.TOP_RIGHT);

        //hideAnimation.setSpeed(CustomAnimation.SPEED_VERY_FAST);


        alert.setShowAnimation(showAnimation);
       // alert.setHideAnimation(hideAnimation);
        alert.showPopup(SwingConstants.CENTER);
    }

    private static JideButton createButton(Icon icon) {
        return new JideButton(icon);
    }









}
