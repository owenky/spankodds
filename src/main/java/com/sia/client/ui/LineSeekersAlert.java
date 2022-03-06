package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.sia.client.ui.control.SportsTabPane;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;


public class LineSeekersAlert extends AbstractLayeredDialog {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final int totalWidth = 800;
    private static final int rowHeight =150;
    public static final Dimension dialogPreferredSize = new Dimension(totalWidth+50,800);
    private final HintTextField gameNumField = new HintTextField("");
    private JComboBox<String> period;
    private final LineSeekerSectionFieldGroup spreadFieldGrp = new LineSeekerSectionFieldGroup("Atlanta Hawks","Orlando Magic");
    private final LineSeekerSectionFieldGroup totalsFieldGrp = new LineSeekerSectionFieldGroup("Over","Under");
    private final LineSeekerSectionFieldGroup mlinesFieldGrp = new LineSeekerSectionFieldGroup("Atlanta Hawks","Orlando Magic");
    private final LineSeekerSectionFieldGroup awayTTFieldGrp = new LineSeekerSectionFieldGroup("Over","Under");
    private final LineSeekerSectionFieldGroup homeTTFieldGrp = new LineSeekerSectionFieldGroup("Over","Under");

    public LineSeekersAlert(SportsTabPane stp) {
       super(stp,"Line Seeker Alerts");
    }
    private JPanel createUserComp() {
        return new JPanel();
    }
    @Override
    protected JComponent getUserComponent() {
        JPanel userComp = createUserComp();
        userComp.setLayout(new BoxLayout(userComp, BoxLayout.Y_AXIS));
        userComp.add(controlSec());
        userComp.add(spreadSec());
        userComp.add(totalsSec());
        userComp.add(moneyLineSec());
        userComp.add(awayTTSec());
        userComp.add(homeTTSec());
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
gameNumField.setText("537");
        gameNumField.setPreferredSize(fieldDim);
        c.gridx = 0;
        c.gridy = 1;
        bodyComp.add(gameNumField,c);

        c.gridx=1;
        c.gridy=1;
        period.setPreferredSize(fieldDim);
        bodyComp.add(period,c);

        return titledPanelGenerator.getPanel();
    }
    private JComponent spreadSec() {
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("Spreads",totalWidth,rowHeight,spreadFieldGrp.activateStatus);

//        spreadFieldGrp.leftColumn.setTitle("Laker");
//        spreadFieldGrp.rightColumn.setTitle("Knix");

        titledPanelGenerator.setBodyComponent(new LineSeekerSectionLayout(spreadFieldGrp).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent totalsSec() {
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("Totals",totalWidth,rowHeight,totalsFieldGrp.activateStatus);
        titledPanelGenerator.setBodyComponent(new LineSeekerSectionLayout(totalsFieldGrp).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent moneyLineSec() {
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("Money Lines",totalWidth,rowHeight,mlinesFieldGrp.activateStatus);
        titledPanelGenerator.setBodyComponent(new LineSeekerSectionLayout(mlinesFieldGrp).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent awayTTSec() {
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("Away TT",totalWidth,rowHeight,awayTTFieldGrp.activateStatus);
        titledPanelGenerator.setBodyComponent(new LineSeekerSectionLayout(awayTTFieldGrp).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private JComponent homeTTSec() {
        TitledPanelGenerator titledPanelGenerator = new TitledPanelGenerator("Home TT",totalWidth,rowHeight,homeTTFieldGrp.activateStatus);
        titledPanelGenerator.setBodyComponent(new LineSeekerSectionLayout(homeTTFieldGrp).getLayoutPane());
        return titledPanelGenerator.getPanel();
    }
    private Vector<String> getPeriodItems() {
        Vector<String> periodItems = new Vector<>();
        periodItems.add("Full Game");
        periodItems.add("First Half");
        periodItems.add("Second Half");

        return periodItems;
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