package com.sia.client.ui.lineseeker;// Demonstrate BoxLayout and the Box class.

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
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
import java.util.Vector;


public class AlertLayout extends AbstractLayeredDialog {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final int totalWidth = 800;
    private static final int rowHeight =150;
    private static final String saveBtnText = "Save";
    public static final Dimension dialogPreferredSize = new Dimension(totalWidth+50,800);
    private final GameComboBox gameNumBox = new GameComboBox();
    private JComboBox<String> period;
    private AlertConfig alertConfig;

    public AlertLayout(SportsTabPane stp) {
       super(stp,"Line Seeker Alerts");
    }
    private JPanel createUserComp() {
        return new JPanel();
    }
    public void setAlertConfig(AlertConfig alertConfig) {
        this.alertConfig = alertConfig;
    }
    public AlertConfig getAlertConfig() {
        if ( null == alertConfig) {
            alertConfig = new AlertConfig();
        }
        return this.alertConfig;
    }
    @Override
    protected JComponent getUserComponent() {
        gameNumBox.loadGames();
        gameNumBox.setEditable(false);
        gameNumBox.setSelectedItem(GameSelectionItem.promptItem);
        JPanel userComp = createUserComp();
        userComp.setLayout(new BoxLayout(userComp, BoxLayout.Y_AXIS));
        userComp.add(controlSec());
        userComp.add(getSectionComponent(AlertConfig.spreadName));
        userComp.add(getSectionComponent(AlertConfig.totalsName));
        userComp.add(getSectionComponent(AlertConfig.mLineName));
        userComp.add(getSectionComponent(AlertConfig.awayName));
        userComp.add(getSectionComponent(AlertConfig.homeTTName));
        userComp.add(bottomControlSection());
        gameNumBox.addValueChangeListener(this::updateLineSeekerAlertSection);
        return userComp;
    }
    private JComponent controlSec() {

        final Dimension fieldDim = new Dimension(100,30);
        period = new JComboBox<>(getPeriodItems());

        JPanel bodyComp = new JPanel();
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator(null,totalWidth,80,null);
        titledPanelGenerator.setBodyComponent(bodyComp);

        bodyComp.setLayout(new GridBagLayout());
        GridBagConstraints c = createDefaultGridBagConstraints();

        //1. title
        c.gridx=0;
        c.gridy=0;
        c.anchor = GridBagConstraints.SOUTH;
        JLabel gameNumLabel = new JLabel("Game #");
        JLabel periodLabel = new JLabel("Period");

        bodyComp.add(gameNumLabel,c);
        c.gridx = 1;
        bodyComp.add(periodLabel,c);

        //2. input fields
        c.anchor = GridBagConstraints.NORTH;
        //let setPrototypeDisplayValue(prototypeDisplayValue); in GameComboBox::loadGames decides the size of combobox -- 03/23/2022
//        gameNumBox.setPreferredSize(fieldDim);
        c.gridx = 0;
        c.gridy = 1;
        bodyComp.add(gameNumBox,c);

        c.gridx=1;
        c.gridy=1;
//        period.setPreferredSize(fieldDim);
        bodyComp.add(period,c);

        return titledPanelGenerator.getPanel();
    }
    private JComponent getSectionComponent(String sectionName) {
        SectionFieldGroup sectionFieldGroup = getAlertConfig().getSectionFieldGroup(sectionName);
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator(sectionFieldGroup.getSectionName(),totalWidth,rowHeight,sectionFieldGroup.activateStatus);
        titledPanelGenerator.setBodyComponent(new SectionLayout(sectionFieldGroup).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent bottomControlSection() {
        JButton clsBtn = new JButton("Close");
        clsBtn.addActionListener((event)->this.close());

        JButton saveBtn = new JButton(saveBtnText);
        saveBtn.addActionListener(this::save);
        JPanel bottomCtrPanel = new JPanel();
        Border outsideBorder = BorderFactory.createMatteBorder(1,0,0,0, Color.darkGray);
//        Border insideBorder = new EmptyBorder(10, 10, 10, 10);
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
    private void save(ActionEvent event) {
        final AbstractButton btn = (AbstractButton)event.getSource();
        btn.setText("Saving...");
        btn.setEnabled(false);
        SwingWorker<Void,Void> saveWorker = new SwingWorker<Void,Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(3000L);
                performSave();
                return null;
            }
            @Override
            protected void done() {
                btn.setText(saveBtnText);
                btn.setEnabled(true);
            }
        };
        saveWorker.execute();
    }
    private void performSave() {
        System.out.println("LineSeekerAlert::save Need Implementation................");
    }
    private Vector<String> getPeriodItems() {
        Vector<String> periodItems = new Vector<>();
        periodItems.add("Full Game");
        periodItems.add("First Half");
        periodItems.add("Second Half");

        return periodItems;
    }
    private void updateLineSeekerAlertSection(ValueChangedEvent event) {
        GameSelectionItem item = (GameSelectionItem)gameNumBox.getSelectedItem();
        Game game = item.getGame();
        if (GameUtils.isRealGame(game)) {
            String visitor = game.getVisitorteam();
            String home = game.getHometeam();
            SectionFieldGroup spreadFieldGrp = getAlertConfig().getSectionFieldGroup(AlertConfig.spreadName);
            SectionFieldGroup mlinesFieldGrp = getAlertConfig().getSectionFieldGroup(AlertConfig.mLineName);
            spreadFieldGrp.setLeftColumnTitle(visitor);
            spreadFieldGrp.setRightColumnTitle(home);
            mlinesFieldGrp.setLeftColumnTitle(visitor);
            mlinesFieldGrp.setRightColumnTitle(home);
        }
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