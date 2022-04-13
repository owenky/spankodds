package com.sia.client.ui.lineseeker;// Demonstrate BoxLayout and the Box class.

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sia.client.config.GameUtils;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AbstractLayeredDialog;
import com.sia.client.ui.TitledPanelGenerator;
import com.sia.client.ui.control.SportsTabPane;
import com.sia.client.ui.games.GameComboBox;
import com.sia.client.ui.games.GameSelectionItem;
import com.sia.client.ui.sbt.ValueChangedEvent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class AlertLayout extends AbstractLayeredDialog {

    private static final String editIndicator="*";
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final int totalWidth = 800;
    private static final int rowHeight =150;
    private static final String saveBtnText = "Save";
    public static final Dimension dialogPreferredSize = new Dimension(totalWidth+50,800);
    private final GameComboBox gameNumBox = new GameComboBox();
    private JComboBox<AlertPeriod> period;
    private AlertConfig alertConfig;
    private JLabel editStatusLabel = new JLabel();
    private LineSeekerAlertComboBox alertsCombobox = new LineSeekerAlertComboBox();
    private final List<SectionFieldGroup> sectionFieldGroupList;
    private final AlertComponentListener alertComponentListener;

    public AlertLayout(SportsTabPane stp) {
       super(stp,"Line Seeker Alerts");
       this.alertComponentListener = new AlertComponentListener(this);
       this.setTitlePanelLeftComp(makeAlertComboBoxPanel());
       this.sectionFieldGroupList = new ArrayList<>();
        for(AlertSectionName alertSectionName: AlertSectionName.getSortedSectionNames()) {
            SectionFieldGroup sectionComps = new SectionFieldGroup(alertSectionName);
            sectionComps.addActionListener(alertComponentListener);
            sectionFieldGroupList.add(sectionComps);
        }
    }
    private JComponent makeAlertComboBoxPanel() {
        JPanel panel = new JPanel();
        panel.add(alertsCombobox);
        panel.add(new JLabel("Alerts"));
        panel.add(editStatusLabel);
        return panel;
    }
    private JPanel createUserComp() {
        return new JPanel();
    }
    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }
    public AlertConfig getAlertConfig() {
        if ( null == alertConfig) {
            setAlertConfig(new AlertConfig(SelectionItem.SELECT_BLANK_KEY,AlertPeriod.Full));
        }
        return this.alertConfig;
    }
    public SportsTabPane getSportsTabPane() {
        return this.getAnchoredLayeredPane().getSportsTabPane();
    }
    @Override
    protected JComponent getUserComponent() {
        alertsCombobox.loadAlerts();
        gameNumBox.loadGames();
        gameNumBox.setSelectedItem(GameSelectionItem.promptItem);
        JPanel userComp = createUserComp();
        userComp.setLayout(new BoxLayout(userComp, BoxLayout.Y_AXIS));
        userComp.add(controlSec());
        List<AlertSectionName> alertSectionNames = AlertSectionName.getSortedSectionNames();
        for(AlertSectionName alertSectionName:alertSectionNames ) {
            userComp.add(getSectionComponent(alertSectionName));
        }
        userComp.add(bottomControlSection());
        gameNumBox.addValueChangeListener(this::updateLineSeekerAlertSection);
        alertsCombobox.addValueChangeListener(this::updateAlertAttributes);
        return userComp;
    }
    private JComponent controlSec() {

        final Dimension fieldDim = new Dimension(100,30);
        period = new JComboBox<>(new Vector<>(AlertPeriod.getOrderedVec()));

        JPanel bodyComp = new JPanel();
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator(null,totalWidth,80,null);
        titledPanelGenerator.setBodyComponent(bodyComp);

        bodyComp.setLayout(new GridBagLayout());
        GridBagConstraints c = createDefaultGridBagConstraints();

        //1. title
        c.gridy=0;
        c.anchor = GridBagConstraints.SOUTH;
        JLabel gameNumLabel = new JLabel("Game #");
        JLabel periodLabel = new JLabel("Period");

        c.gridx=0;
        bodyComp.add(gameNumLabel,c);
        c.gridx = 1;
        bodyComp.add(periodLabel,c);

        //2. input fields
        c.anchor = GridBagConstraints.NORTH;
        //let setPrototypeDisplayValue(prototypeDisplayValue); in GameComboBox::loadGames decides the size of combobox -- 03/23/2022
//        gameNumBox.setPreferredSize(fieldDim);

        c.gridy = 1;
        c.gridx = 0;
        bodyComp.add(gameNumBox,c);
        c.gridx = 1;
//        period.setPreferredSize(fieldDim);
        bodyComp.add(period,c);

        return titledPanelGenerator.getPanel();
    }
    private JComponent getSectionComponent(AlertSectionName sectionName) {
        SectionFieldGroup sectionFieldGroup = getSectionFieldGroup(sectionName);
        sectionFieldGroup.setSectionAtrribute(getAlertConfig().getSectionAtrribute(sectionName));
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator(sectionFieldGroup.getSectionName().getDisplay(),totalWidth,rowHeight,sectionFieldGroup.activateStatus);
        SectionLayout sectionLayout = new SectionLayout(sectionFieldGroup);
        sectionFieldGroupList.add(sectionFieldGroup);
        titledPanelGenerator.setBodyComponent(sectionLayout.getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent bottomControlSection() {
        JButton clsBtn = new JButton("Close");
        clsBtn.addActionListener((event)->this.close());

        JButton saveBtn = new JButton(saveBtnText);
        saveBtn.addActionListener(this::save);
        JPanel bottomCtrPanel = new JPanel();
        Border outsideBorder = BorderFactory.createMatteBorder(1,0,0,0, Color.darkGray);
        bottomCtrPanel.setBorder(outsideBorder);

        bottomCtrPanel.add(saveBtn);
        bottomCtrPanel.add(clsBtn);

        JPanel bottomCrtPanelWrapper = new JPanel();
        bottomCrtPanelWrapper.setLayout(new BorderLayout());
        bottomCrtPanelWrapper.add(bottomCtrPanel,BorderLayout.CENTER);

        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("",totalWidth,30,null);
        titledPanelGenerator.setBodyComponent(bottomCrtPanelWrapper);
        return titledPanelGenerator.getPanel();
    }
    private AlertConfig checkNewAlert() {
        AlertConfig alertConfig =getAlertConfig();
        int selectGameId = ((GameSelectionItem)gameNumBox.getSelectedItem()).getGame().getGame_id();
        AlertPeriod selectPeriod = (AlertPeriod)period.getSelectedItem();
        AlertConfig selectedAlertConfig = AlertAttrManager.of(selectGameId, selectPeriod);
        if ( alertConfig.getGameId()  < 0 ) {
            selectedAlertConfig.cloneAttributes(alertConfig);
            setAlertConfig(selectedAlertConfig);
        }
        return selectedAlertConfig;
    }
    private void save(ActionEvent event) {

        final AlertConfig selectedAlert = checkNewAlert();
        for(SectionFieldGroup sfg : sectionFieldGroupList) {
            sfg.updateSectionAttribute();
        }
        final AbstractButton btn = (AbstractButton)event.getSource();
        String err = AlertConfigValidator.validate(this);
        if ( null != err ) {
            Utils.showMessageDialog(getSportsTabPane(),err);
            return;
        }

        btn.setText("Saving...");
        btn.setEnabled(false);
        SwingWorker<Void,Void> saveWorker = new SwingWorker<Void,Void>() {
            private Exception e;
            @Override
            protected Void doInBackground()  {
                e = null;
                try {
                    performSave();
                } catch (JsonProcessingException ex) {
                    e = ex;
                }
                return null;
            }
            @Override
            protected void done() {
                if ( null != e) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    Utils.showErrorMessageDialog(getSportsTabPane(),sw.toString());
                } else {
                    alertsCombobox.addIfAbsent(selectedAlert,true);
                }
                btn.setText(saveBtnText);
                btn.setEnabled(true);
            }
        };
        saveWorker.execute();
    }
    private void performSave() throws JsonProcessingException {
        //doing backgroud work.
    }
    private void updateLineSeekerAlertSection(ValueChangedEvent event) {
        GameSelectionItem item = (GameSelectionItem)gameNumBox.getSelectedItem();
        Game game = item.getGame();
        if (GameUtils.isRealGame(game)) {
            String visitor = game.getVisitorteam();
            String home = game.getHometeam();
            SectionFieldGroup spreadFieldGrp = getSectionFieldGroup(AlertSectionName.spreadName);
            SectionFieldGroup mlinesFieldGrp = getSectionFieldGroup(AlertSectionName.mLineName);
            spreadFieldGrp.setLeftColumnTitle(visitor);
            spreadFieldGrp.setRightColumnTitle(home);
            mlinesFieldGrp.setLeftColumnTitle(visitor);
            mlinesFieldGrp.setRightColumnTitle(home);
        }
    }
    public int getSelectedGameId() {
        return ((GameSelectionItem)gameNumBox.getSelectedItem()).getGame().getGame_id();
    }
    public AlertPeriod getSelectedAlertPeriod() {
        return (AlertPeriod)period.getSelectedItem();
    }
    public SectionFieldGroup getSectionFieldGroup(AlertSectionName alertSectionName) {
        return sectionFieldGroupList.stream().filter(g->g.sectionName == alertSectionName).findAny().get();
    }
    private void updateAlertAttributes(ValueChangedEvent event) {
        LineSeekerAlertSelectionItem lineSeekerAlertSelectionItem = (LineSeekerAlertSelectionItem)this.alertsCombobox.getSelectedItem();
        AlertConfig alertConfig = lineSeekerAlertSelectionItem.getAlertAttributes();
        if ( alertConfig.getGameId() != this.alertConfig.getGameId() || alertConfig.getPeriod() != this.alertConfig.getPeriod()) {
            boolean entableControlBoxes =  0 >= alertConfig.getGameId();
            this.gameNumBox.setEnabled(entableControlBoxes);
            this.period.setEnabled(entableControlBoxes);
        }
        this.alertConfig = alertConfig;
        List<AlertSectionName> alertSectionNames = AlertSectionName.getSortedSectionNames();
        for(AlertSectionName alertSectionName:alertSectionNames ) {
            SectionFieldGroup sectionFieldGroup = getSectionFieldGroup(alertSectionName);
            sectionFieldGroup.setSectionAtrribute(alertConfig.getSectionAtrribute(alertSectionName));
        }
        setEditStatus(false);
    }
    public void setEditStatus(boolean status) {
        String statusTxt = status?editIndicator:"";
        this.editStatusLabel.setText(statusTxt);
    }
    private boolean isEdited() {
        return editIndicator.equals(editStatusLabel.getText());
    }
    private static GridBagConstraints createDefaultGridBagConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
//        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.insets = EMPTY_INSETS;
        c.ipadx = 0;
        c.ipady = 0;

        return c;
    }
////////////////////////////////////////////////////////////////////////////////////////////

}