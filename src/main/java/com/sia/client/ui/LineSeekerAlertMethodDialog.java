package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.control.SportsTabPane;
import com.sia.client.ui.lineseeker.AlertAttrManager;
import com.sia.client.ui.lineseeker.AlertConfig;
import com.sia.client.ui.lineseeker.AlertState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class LineSeekerAlertMethodDialog extends AbstractLayeredDialog  {

    private final Map<String,LineSeekerAlertMethodStateLayout> layoutMap = new HashMap<>();
    private final JButton saveBtn = new JButton("Save");
    private final AlertConfig alertConfig;
    public LineSeekerAlertMethodDialog(SportsTabPane stp,AlertConfig alertConfig) {
        super(stp,"Line Seeker Alert Method", SiaConst.LayedPaneIndex.LineSeekerAlertMethodDialogIndex);
        this.alertConfig = alertConfig;
        saveBtn.addActionListener(this::saveAlertMethodAttr);
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
            userComponent.add(Box.createRigidArea(new Dimension(0, 3)));
            LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);

            int width = (int)SiaConst.UIProperties.LineAlertMethodDim.getWidth()-25;
            int height = ((int)SiaConst.UIProperties.LineAlertMethodDim.getHeight()-75)/3;
            TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("",width,height,null,null);
            titledPanelGenerator.setTitleBarBgColor(Color.GRAY.brighter().brighter());
            titledPanelGenerator.setTitleBarFgColor(Color.BLACK);
            titledPanelGenerator.setBodyComponent(stateLayout.getLayoutPane(sw));

            userComponent.add(titledPanelGenerator.getPanel());
        }
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout());
        btnPanel.add(saveBtn,BorderLayout.CENTER);
        userComponent.add(btnPanel);
        return userComponent;
    }
    private LineSeekerAlertMethodStateLayout getLineSeekerAlertMethodStateLayout(AlertState alertState) {
        return layoutMap.computeIfAbsent(alertState.name(),(name)-> new LineSeekerAlertMethodStateLayout(AlertAttrManager.getLineSeekerAlertMethodAttr(name)));
    }
    private void saveAlertMethodAttr(ActionEvent ae) {
        for(AlertState alertState: AlertState.values()) {
            LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);
            stateLayout.saveMethodAttr();
        }
        close();
    }
    public void updateAlertMethodAttr() {
        for(AlertState alertState: AlertState.values()) {
            LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);
            stateLayout.updateAlertMethodAttr();
        }
    }
    @Override
    public boolean isEdited() {
        boolean isEdited = false;
        for(AlertState alertState: AlertState.values()) {
            LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);
            isEdited = stateLayout.isEdited();
            if ( isEdited) {
                break;
            }
        }
        return isEdited;
    }
}

