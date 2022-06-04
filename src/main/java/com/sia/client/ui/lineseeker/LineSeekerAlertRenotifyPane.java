package com.sia.client.ui.lineseeker;

import com.sia.client.ui.comps.EditablePane;
import com.sia.client.ui.comps.SbtStringComboBox;
import com.sia.client.ui.comps.UICompValueBinder;

import javax.swing.*;
import java.awt.*;

public class LineSeekerAlertRenotifyPane implements EditablePane {

    private final JLabel editStatusLabel = new JLabel();
    private final AlertSeekerMethods alertSeekerMethods;
    private SbtStringComboBox renotifyComboBox;
    private final String[] minslist = new String[20];
    private UICompValueBinder uiCompValueBinder;

    public LineSeekerAlertRenotifyPane(AlertSeekerMethods alertSeekerMethods) {
        this.alertSeekerMethods = alertSeekerMethods;
    }
    @Override
    public JLabel getEditStatusLabel() {
        return editStatusLabel;
    }
    @Override
    public UICompValueBinder getJComponentBinder() {
        if ( null == uiCompValueBinder) {
            uiCompValueBinder = UICompValueBinder.register(this,alertSeekerMethods,getOnValueChangedEventFunction());
            uiCompValueBinder.bind("renotifyInMinutes",renotifyComboBox);
        }
        return uiCompValueBinder;
    }
    @Override
    public JComponent getPane() {

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
        renotifyComboBox = new SbtStringComboBox(minslist);
    }
}

