package com.sia.client.ui.lineseeker;// Demonstrate BoxLayout and the Box class.

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sia.client.config.GameUtils;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AbstractLayeredDialog;
import com.sia.client.ui.AppController;
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
    private static final String delBtnText = "Delete";
    private static final String cloneBtnText = "Clone";
    private static final long actionWaitTime = 300L;
    public static final Dimension dialogPreferredSize = new Dimension(totalWidth+50,800);
    private final GameComboBox gameNumBox = new GameComboBox();
    private JComboBox<AlertPeriod> period;
    private AlertConfig alertConfig;
    private JLabel editStatusLabel = new JLabel();
    private LineSeekerAlertComboBox alertsCombobox = new LineSeekerAlertComboBox();
    private final List<SectionComponents> sectionComponentsList;
    private final AlertComponentListener alertComponentListener;

    public AlertLayout(SportsTabPane stp) {
       super(stp,"Line Seeker Alerts");
       this.alertComponentListener = new AlertComponentListener(this);
       this.setTitlePanelLeftComp(makeAlertComboBoxPanel());
       this.sectionComponentsList = new ArrayList<>();
        for(AlertSectionName alertSectionName: AlertSectionName.getSortedSectionNames()) {
            SectionComponents sectionComps = new SectionComponents(alertSectionName);
            sectionComps.addActionListener(alertComponentListener);
            sectionComponentsList.add(sectionComps);
        }
        withCloseValidor(this::validateClose);
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
    public AlertConfig getAlertConfig() {
        if ( null == alertConfig) {
            this.alertConfig = AlertConfig.BlankAlert;
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
        alertsCombobox.addValueChangeListener(this::updateLayoutComponents);
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
        SectionComponents sc = getSectionComponents(sectionName);
        sc.setSectionCompValues(getAlertConfig().getSectionAtrribute(sectionName));
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator(sc.getSectionName().getDisplay(),totalWidth,rowHeight,sc.activateStatus);
        SectionLayout sectionLayout = new SectionLayout(sc);
        sectionComponentsList.add(sc);
        titledPanelGenerator.setBodyComponent(sectionLayout.getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent bottomControlSection() {
        JButton clsBtn = new JButton("Close");
        clsBtn.addActionListener((event)->this.close());

        JButton saveBtn = new JButton(saveBtnText);
        saveBtn.addActionListener(this::save);

        JButton delBtn = new JButton(delBtnText);
        delBtn.addActionListener(this::delete);

        JButton cloneBtn = new JButton(cloneBtnText);
        cloneBtn.addActionListener(this::clone);

        JPanel bottomCtrPanel = new JPanel();
        Border outsideBorder = BorderFactory.createMatteBorder(1,0,0,0, Color.darkGray);
        bottomCtrPanel.setBorder(outsideBorder);

        bottomCtrPanel.add(saveBtn);
        bottomCtrPanel.add(cloneBtn);
        bottomCtrPanel.add(delBtn);
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
            this.alertConfig = selectedAlertConfig;
        }
        return selectedAlertConfig;
    }
    private void clone(ActionEvent event) {
        //construct a new instance, don't use the singleton
        AlertConfig buffer = new AlertConfig(SelectionItem.SELECT_BLANK_KEY,AlertPeriod.Full);
        saveCompValuesToAlertConfig(buffer);
        alertsCombobox.setSelectedIndex(0);
        this.updateLayoutComponents(buffer);
        this.gameNumBox.setEnabled(true);
        this.period.setEnabled(true);
        this.setEditStatus(true);
    }
    private void delete(ActionEvent event) {
        if ( JOptionPane.NO_OPTION == Utils.showOptions(getSportsTabPane(),"Do you want to delete this alert?") ) {
            return;
        }

        JButton delBtn = (JButton)event.getSource();
        //removeFromCache must be before removeItemAt
        boolean isNewAlert =  alertConfig.getGameId() < 0;
        if ( ! isNewAlert) {
            AlertAttrManager.removeFromCache(alertConfig);
        } else {
            this.updateLayoutComponents(alertConfig);
            return;
        }
        alertsCombobox.removeItemAt(alertsCombobox.getSelectedIndex());
        delBtn.setText("Deleting...");
        delBtn.setEnabled(false);
        SwingWorker<Void,Void> saveWorker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground()  {
                Utils.sleep(actionWaitTime);
                return null;
            }
            @Override
            protected void done() {
                delBtn.setText(delBtnText);
                delBtn.setEnabled(true);
                alertsCombobox.setSelectedIndex(0);
            }
        };
        saveWorker.execute();
    }
    private void saveCompValuesToAlertConfig(AlertConfig targetAlertConfig) {
        for(SectionComponents sc : sectionComponentsList) {
            SectionAttribute sectionAttribute = targetAlertConfig.getSectionAtrribute(sc.getSectionName());
            sc.updateSectionAttribute(sectionAttribute);
        }
    }
    private void save(ActionEvent event) {

        final AlertConfig selectedAlert = checkNewAlert();
        saveCompValuesToAlertConfig(selectedAlert);
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
                gameNumBox.setEnabled(false);
                period.setEnabled(false);
                btn.setText(saveBtnText);
                btn.setEnabled(true);
            }
        };
        saveWorker.execute();
    }
    private void performSave() throws JsonProcessingException {
        //doing backgroud work.
        Utils.sleep(actionWaitTime);
    }
    private void updateLineSeekerAlertSection(ValueChangedEvent event) {
        GameSelectionItem item = (GameSelectionItem)gameNumBox.getSelectedItem();
        Game game = item.getGame();
        if (GameUtils.isRealGame(game)) {
            String visitor = game.getVisitorteam();
            String home = game.getHometeam();
            SectionComponents spreadComps = getSectionComponents(AlertSectionName.spreadName);
            SectionComponents mlinesFieldGrp = getSectionComponents(AlertSectionName.mLineName);
            spreadComps.setLeftColumnTitle(visitor);
            spreadComps.setRightColumnTitle(home);
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
    public SectionComponents getSectionComponents(AlertSectionName alertSectionName) {
        return sectionComponentsList.stream().filter(g->g.sectionName == alertSectionName).findAny().get();
    }
    private void updateLayoutComponents(ValueChangedEvent event) {
        LineSeekerAlertSelectionItem lineSeekerAlertSelectionItem = (LineSeekerAlertSelectionItem)this.alertsCombobox.getSelectedItem();
        AlertConfig selectedAlertConfig = lineSeekerAlertSelectionItem.getAlertConfig();
        updateLayoutComponents(selectedAlertConfig);
    }
    private void updateLayoutComponents(AlertConfig selectedAlertConfig) {

        if ( selectedAlertConfig.getGameId() != this.alertConfig.getGameId() || selectedAlertConfig.getPeriod() != this.alertConfig.getPeriod()) {
            boolean entableControlBoxes =  0 >= selectedAlertConfig.getGameId();
            this.gameNumBox.setEnabled(entableControlBoxes);
            this.period.setEnabled(entableControlBoxes);
        }
        this.alertConfig = selectedAlertConfig;
        List<AlertSectionName> alertSectionNames = AlertSectionName.getSortedSectionNames();
        for(AlertSectionName alertSectionName:alertSectionNames ) {
            SectionComponents sc = getSectionComponents(alertSectionName);
            sc.setSectionCompValues(alertConfig.getSectionAtrribute(alertSectionName));
        }
        this.gameNumBox.setSelectedItem(new GameSelectionItem(AppController.getGame(alertConfig.getGameId())));
        this.period.setSelectedItem(alertConfig.getPeriod());
        setEditStatus(false);
    }
    public void setEditStatus(boolean status) {
        String statusTxt = status?editIndicator:"";
        this.editStatusLabel.setText(statusTxt);
        this.alertsCombobox.setEnabled(!status);
        String tooltipText = status ?"To select another Alert, please save or delete this configuration.":"";
        alertsCombobox.setToolTipText(tooltipText);

    }
    private boolean validateClose() {
        if ( isEdited()) {
            int option = Utils.showOptions(getSportsTabPane(),"Do you want to discard changes to this alert configuration?");
            return JOptionPane.YES_OPTION == option;
        } else {
            return true;
        }
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