package com.sia.client.ui;

import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PaintPanel;
import com.jidesoft.utils.PortingUtils;
import com.sia.client.config.Utils;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.GridLayout;

public class UrgentMessage {

    public static boolean popupEnabled = true;
    static AlertGroup ag = new AlertGroup();

    public UrgentMessage(String urgentmessage) {

        this(urgentmessage, 10000, SwingConstants.NORTH_EAST, AppController.getMainTabPane());
    }

    public UrgentMessage(String urgentmessage, int ms, int where, Component c) {
        Utils.checkAndRunInEDT(() -> showUrgentMessage(urgentmessage, ms, where, c));
    }

    private void showUrgentMessage(String urgentmessage, int ms, int where, Component c) {

        if ( ! popupEnabled ) {
            Utils.log("DEBUG:  popupEnabled=false, popup is disabled..................");
            return;
        }
        String alerttext = "<HTML><H1>URGENT MESSAGE</H1><FONT COLOR=BLUE>" +
                "Alert Demo<BR>" +
                "Hello,<BR>" +
                "This is a sample alert demo.</FONT></HTML>";

        alerttext = urgentmessage;


        final JLabel message = new JLabel(alerttext);
        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(message, BorderLayout.CENTER);
        panel.setOpaque(true);
        panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height, new Color(212, 208, 200)));
        //https://docs.oracle.com/javase/tutorial/uiswing/misc/modality.html+
        Alert alert = new Alert();

        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));

        JideButton closeButton = createButton(Utils.getImageIcon(("close.png")));
        closeButton.addActionListener(e -> alert.hidePopupImmediately());
        rightPanel.add(closeButton);

        JPanel topPanel = JideSwingUtilities.createTopPanel(rightPanel);
        panel.add(topPanel, BorderLayout.AFTER_LINE_ENDS);


        ag.add(alert);
        alert.getContentPane().setLayout(new BorderLayout());
        alert.getContentPane().add(panel);
                /*alert.getContentPane().add(createSampleAlert(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        alert.hidePopupImmediately();
                    }
                }));
				*/
        alert.setOwner(c);
        alert.setResizable(true);
        alert.setMovable(true);
        alert.setTransient(false);
        alert.setTimeout(ms);
        alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

        CustomAnimation hideAnimation = new CustomAnimation();
        hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
        hideAnimation.setFunctionFade(CustomAnimation.FUNC_POW_HALF);
        alert.setHideAnimation(hideAnimation);
        alert.showPopup(where);
    }

    private static JideButton createButton(Icon icon) {
        return new JideButton(icon);
    }

}