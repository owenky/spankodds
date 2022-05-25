package com.sia.client.ui.lineseeker;

import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.comps.EditableLayout;
import com.sia.client.ui.comps.JComponentBinder;
import com.sia.client.ui.comps.LightComboBox;

import javax.swing.*;
import java.awt.*;

public class LineSeekerAlertRenotifyLayout implements EditableLayout {

    private final JLabel editStatusLabel = new JLabel();
    private final AlertSeekerMethods alertSeekerMethods;
    private JComboBox<String> renotifyComboBox;
    private final String[] minslist = new String[20];
    private JComponentBinder jComponentBinder;

    public LineSeekerAlertRenotifyLayout(AlertSeekerMethods alertSeekerMethods) {
        this.alertSeekerMethods = alertSeekerMethods;
    }
    @Override
    public JLabel getEditStatusLabel() {
        return editStatusLabel;
    }
    @Override
    public JComponentBinder getJComponentBinder() {
        if ( null == jComponentBinder) {
            jComponentBinder = new JComponentBinder(alertSeekerMethods,getOnValueChangedEventFunction());
            jComponentBinder.bind("renotifyInMinutes",renotifyComboBox);
        }
        return jComponentBinder;
    }
    @Override
    public JComponent getLayoutPane() {

        initComponents();

        JPanel contentPanel = new JPanel();
        JLabel renotifyme = new JLabel("Renotify me on same Game only after ");
        JLabel renotifyme2 = new JLabel(" minutes have elapsed");
        contentPanel.add(renotifyme);
        contentPanel.add(renotifyComboBox);
        contentPanel.add(renotifyme2);

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(editStatusLabel,BorderLayout.EAST);
        wrapperPanel.add(contentPanel,BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEtchedBorder());
        return wrapperPanel;
    }
    private void initComponents() {
        for(int i=0;i<20;i++) {
            minslist[i]=String.valueOf(0.5+0.5*i);
        }
        renotifyComboBox = new JComboBox<>(minslist);
    }
}

