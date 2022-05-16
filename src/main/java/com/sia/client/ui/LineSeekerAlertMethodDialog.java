package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.control.SportsTabPane;
import com.sia.client.ui.lineseeker.AlertState;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineSeekerAlertMethodDialog extends AbstractLayeredDialog  {

    public LineSeekerAlertMethodDialog(SportsTabPane stp) {
        super(stp,"Line Seeker Alert Method", SiaConst.LayedPaneIndex.LineSeekerAlertMethodDialogIndex);
    }
    @Override
    protected JComponent getUserComponent() {

        JPanel userComponent; //don't cache uesrComponent because LineSeekerAlertMethodStateLayout depends on SpankyWindow.
        userComponent = new JPanel();
        BoxLayout boxLayout = new BoxLayout(userComponent, BoxLayout.Y_AXIS);
        userComponent.setLayout(boxLayout);
        SportsTabPane stp = getSportsTabPane();
        SpankyWindow sw = SpankyWindow.findSpankyWindow(stp.getWindowIndex());
        for(AlertState alertState: AlertState.values()) {
            LineSeekerAlertMethodStateLayout stateLayout = new LineSeekerAlertMethodStateLayout(alertState.name());
            userComponent.add(stateLayout.getLayoutPane(sw));
        }
        return userComponent;
    }
}

