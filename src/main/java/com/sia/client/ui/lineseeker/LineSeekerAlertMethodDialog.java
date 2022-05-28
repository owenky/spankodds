package com.sia.client.ui.lineseeker;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.AbstractLayeredDialog;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.TitledPanelGenerator;
import com.sia.client.ui.comps.BookieTree;
import com.sia.client.ui.comps.BookieTreeLayout;
import com.sia.client.ui.comps.EditableLayout;
import com.sia.client.ui.comps.ActionOnEditableLayouts;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LineSeekerAlertMethodDialog extends AbstractLayeredDialog implements ActionOnEditableLayouts {

    private final Map<String, LineSeekerAlertMethodStateLayout> layoutMap = new HashMap<>();
    private final Dimension verticalSpacing = new Dimension(0,1);
    private final JButton saveBtn = new JButton("Save");
    private final AlertSeekerMethods alertSeekerMethods;
    private final LineSeekerAlertRenotifyLayout lineSeekerAlertRenotifyLayout;
    private final int methodPanelWidth;
    private final int methodPaneHeight;
    private final int bookiePanelWidth;
    private final int bookiePanelWidtHeight;
    private final int renotifyHeight = 40;
    private final int savePanelHeight = 40;
    private BookieTreeLayout bookieTreeLayout;
    private java.util.List<EditableLayout> editableLayoutList;

    public LineSeekerAlertMethodDialog(SportsTabPane stp,AlertSeekerMethods alertSeekerMethods) {
        super(stp,"Line Seeker Alert Method", SiaConst.LayedPaneIndex.LineSeekerAlertMethodDialogIndex);
        this.alertSeekerMethods = alertSeekerMethods;
        int height = (int)SiaConst.UIProperties.LineAlertMethodDim.getHeight();
        saveBtn.addActionListener(this::persist);
        lineSeekerAlertRenotifyLayout = new LineSeekerAlertRenotifyLayout(this.alertSeekerMethods);
        bookiePanelWidtHeight = height -savePanelHeight-40;
        bookiePanelWidth = 225;
        methodPanelWidth = (int)SiaConst.UIProperties.LineAlertMethodDim.getWidth()-bookiePanelWidth-40;
        methodPaneHeight = (bookiePanelWidtHeight-renotifyHeight)/3;
    }
    @Override
    public java.util.List<EditableLayout> getEditablelayout() {
        if ( null== editableLayoutList) {
            editableLayoutList = new ArrayList<>(5);
            for(AlertState alertState: AlertState.values()) {
                LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);
                editableLayoutList.add(stateLayout);
            }
            editableLayoutList.add(lineSeekerAlertRenotifyLayout);
            editableLayoutList.add(bookieTreeLayout);
        }
        return editableLayoutList;
    }
    @Override
    protected JComponent getUserComponent() {
        JComponent leftPanel = makeBookiePanel();
        JComponent rightPanel = makeMethodPanel();
        JPanel configPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(configPanel,BoxLayout.X_AXIS);
        configPanel.setLayout(boxLayout);
        configPanel.add(leftPanel);
        configPanel.add(rightPanel);

        JPanel wrapper = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(wrapper,BoxLayout.Y_AXIS);
        wrapper.setLayout(boxLayout2);
        wrapper.add(configPanel);
        //save button
        wrapper.add(Box.createRigidArea(verticalSpacing));
        wrapper.add(saveBtn);
        return wrapper;
    }
    private JComponent makeBookiePanel() {
        bookieTreeLayout = new BookieTreeLayout(new BookieTree(),AlertAttrManager.getAlertAttColl().getBookies());
        JComponent sportsbooktreePanel = bookieTreeLayout.getLayoutPane();
        Dimension preferredSize = new Dimension(bookiePanelWidth, bookiePanelWidtHeight);
        sportsbooktreePanel.setPreferredSize(preferredSize);
        return sportsbooktreePanel;
    }

    private JComponent makeMethodPanel() {
        JPanel userComponentLeft; //don't cache uesrComponent because LineSeekerAlertMethodStateLayout depends on SpankyWindow.
        userComponentLeft = new JPanel();
        BoxLayout boxLayout = new BoxLayout(userComponentLeft, BoxLayout.Y_AXIS);
        userComponentLeft.setLayout(boxLayout);
        for(AlertState alertState: AlertState.values()) {
            userComponentLeft.add(Box.createRigidArea(verticalSpacing));
            LineSeekerAlertMethodStateLayout stateLayout = getLineSeekerAlertMethodStateLayout(alertState);
            TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("",methodPanelWidth,methodPaneHeight,null,null);
            titledPanelGenerator.setTitleBarBgColor(Color.GRAY.brighter().brighter());
            titledPanelGenerator.setTitleBarFgColor(Color.BLACK);
            titledPanelGenerator.setBodyComponent(stateLayout.getLayoutPane());
            JComponent sectionPanel = titledPanelGenerator.getPanel();
            Utils.respectComponentSize(sectionPanel,new Dimension(methodPanelWidth,methodPaneHeight));
            userComponentLeft.add(sectionPanel);
        }
        //notify panel
        JComponent renotifyComp = lineSeekerAlertRenotifyLayout.getLayoutPane();
        Utils.respectComponentSize(renotifyComp,new Dimension(methodPanelWidth,renotifyHeight));
        userComponentLeft.add(renotifyComp);
        return userComponentLeft;
    }
    private LineSeekerAlertMethodStateLayout getLineSeekerAlertMethodStateLayout(AlertState alertState) {
        SportsTabPane stp = getSportsTabPane();
        SpankyWindow sw = SpankyWindow.findSpankyWindow(stp.getWindowIndex());
        return layoutMap.computeIfAbsent(alertState.name(),(name)-> new LineSeekerAlertMethodStateLayout(AlertAttrManager.getLineSeekerAlertMethodAttr(name),sw));
    }
    private void persist(ActionEvent ae) {
        persist();
        close();
    }
    @Override
    public boolean isEdited() {
        return ActionOnEditableLayouts.super.isEdited();
    }
}

