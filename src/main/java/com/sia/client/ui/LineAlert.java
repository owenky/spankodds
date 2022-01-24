package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.sia.client.config.SiaConst.SportName;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Bookie;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;


public class LineAlert extends AbstractLayeredDialog implements ItemListener {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    boolean editing = false;
    LineAlertNode lan = null;
    JLabel welcome = new JLabel("LINE ALERT");
    JideToggleButton spreadupperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
    JideToggleButton spreadupperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
    JideToggleButton spreadlowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
    JideToggleButton spreadlowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));
    JideToggleButton totalupperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
    JideToggleButton totalupperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
    JideToggleButton totallowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
    JideToggleButton totallowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));
    JideToggleButton moneylineupperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
    JideToggleButton moneylineupperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
    JideToggleButton moneylinelowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
    JideToggleButton moneylinelowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));
    JideToggleButton teamtotalupperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
    JideToggleButton teamtotalupperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
    JideToggleButton teamtotallowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
    JideToggleButton teamtotallowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));
    String sport = "";
    JComboBox spreadptsComboBox;
    JComboBox totalptsComboBox;
    JComboBox teamtotalptsComboBox;
    JComboBox spreadsecsComboBox;
    JComboBox totalsecsComboBox;
    JComboBox moneylinesecsComboBox;
    JComboBox teamtotalsecsComboBox;
    JComboBox spreadbookiesComboBox;
    JComboBox totalbookiesComboBox;
    JComboBox moneylinebookiesComboBox;
    JComboBox teamtotalbookiesComboBox;
    JRadioButton moveatallButton;
    JRadioButton totalmoveatallButton;
    JRadioButton moneylinemoveatallButton;
    JRadioButton teamtotalmoveatallButton;
    JRadioButton movelinejuiceButton;
    JRadioButton moneylinemovelinejuiceButton;
    JRadioButton totalmovelinejuiceButton;
    JRadioButton teamtotalmovelinejuiceButton;
    JComboBox spreadjuiceComboBox;
    JComboBox totaljuiceComboBox;
    JComboBox moneylinejuiceComboBox;
    JComboBox teamtotaljuiceComboBox;
    JRadioButton movepercentagebutton;
    JRadioButton totalmovepercentagebutton;
    JRadioButton moneylinemovepercentagebutton;
    JRadioButton teamtotalmovepercentagebutton;
    JComboBox spreadpercentageComboBox;
    JComboBox totalpercentageComboBox;
    JComboBox moneylinepercentageComboBox;
    JComboBox teamtotalpercentageComboBox;
    JCheckBox spreadaudiocheckbox;
    JComboBox spreadaudioComboBox;
    JCheckBox totalaudiocheckbox;
    JComboBox totalaudioComboBox;
    JCheckBox moneylineaudiocheckbox;
    JComboBox moneylineaudioComboBox;
    JCheckBox teamtotalaudiocheckbox;
    JComboBox teamtotalaudioComboBox;
    JCheckBox spreadpopupcheckbox;
    JComboBox spreadpopupsecsComboBox;
    JCheckBox totalpopupcheckbox;
    JComboBox totalpopupsecsComboBox;
    JCheckBox moneylinepopupcheckbox;
    JComboBox moneylinepopupsecsComboBox;
    JCheckBox teamtotalpopupcheckbox;
    JComboBox teamtotalpopupsecsComboBox;
    JButton testspreadsoundBut;
    JButton testspreadpopupBut;
    JButton testtotalsoundBut;
    JButton testtotalpopupBut;
    JButton testmoneylinesoundBut;
    JButton testmoneylinepopupBut;
    JButton testteamtotalsoundBut;
    JButton testteamtotalpopupBut;
    JComboBox renotifyComboBox;
    JComboBox renotifytotalComboBox;
    JComboBox renotifymoneylineComboBox;
    JComboBox renotifyteamtotalComboBox;
    JComboBox spreadpopuplocation;
    JComboBox totalpopuplocation;
    JComboBox moneylinepopuplocation;
    JComboBox teamtotalpopuplocation;
    JCheckBox spreadcheckbox = new JCheckBox("Spread");
    JCheckBox totalcheckbox = new JCheckBox("Total");
    JCheckBox moneylinecheckbox = new JCheckBox("Money Line");
    JCheckBox teamtotalcheckbox = new JCheckBox("Team Total");

    JTextField linealertname = new JTextField(20);
    JPanel spreadPanel = new JPanel();
    JPanel totalPanel = new JPanel();
    JPanel moneylinePanel = new JPanel();
    JPanel teamtotalPanel = new JPanel();
    JButton saveBut = new JButton("Save");
    JButton removeBut = new JButton("Remove");
    String soundfile = "";
    JFileChooser fc = new JFileChooser();
    String prefs[];
    JList selectedList = new JList();
    JList eventsList = new JList();
    DefaultListModel eventsModel = new DefaultListModel();
    Box box1 = Box.createVerticalBox();
    Box box2 = Box.createVerticalBox();
    Box box3 = Box.createVerticalBox();
    int popupsecs = 5;
    int popuplocationint = 0;
    int spreadpopuplocationint = 0;
    int totalpopuplocationint = 0;
    int moneylinepopuplocationint = 0;
    int teamtotalpopuplocationint = 0;
    String defaultsoundfile = "";
    String defaultsoundfileplay = "";
    JLabel soundlabel = new JLabel("DEFAULT");
    JPanel treePanel = new JPanel(new BorderLayout(2, 2));
    JPanel sportsbooktreePanel = new JPanel(new BorderLayout(2, 2));
    CheckBoxTree[] trees = new CheckBoxTree[10];
    String[] sportlist = new String[10];
    String[] secslist = new String[60];
    String[] minslist = new String[20];
    String[] centslist = new String[100];
    String[] ptslist = new String[15];
    String[] soccerptslist = new String[9];
    String[] numbookieslist = new String[10];
    String[] gameperiodlist = new String[10];
    int[] gameperiodintlist = new int[10];
    String[] percentagelist = new String[100];
    String[] audiolist = new String[8];
   Vector<String> audiofilevec = new Vector<>();
    JComboBox sportComboBox;
    JComboBox lanComboBox;
    JComboBox gameperiodComboBox;
    private Vector checkednodes = new Vector();
    private CheckBoxTree _tree;
    private CheckBoxTree sportsbooktree;
    private Hashtable leaguenameidhash = new Hashtable();
    private Hashtable bookienameidhash = new Hashtable();
    private String alerttype = "Line Alert";


    public LineAlert(SportsTabPane stp) {
       super(stp,"Line Alerts");
    }
    @Override
    protected JComponent getUserComponent() {
        JPanel userComp = new JPanel();
        JFrame jfrm1 = SpankyWindow.findSpankyWindow(getAnchoredLayeredPane().getSportsTabPane().getWindowIndex());
        linealertname.setDocument(new JTextFieldLimit(20));
        TextPrompt tp7 = new TextPrompt("Name Your Line Alert", linealertname);
        tp7.setForeground(Color.RED);
        tp7.setShow(TextPrompt.Show.FOCUS_LOST);
        //tp7.changeAlpha(0.5f);
        tp7.changeStyle(Font.BOLD + Font.ITALIC);


        audiolist[0] = "Major Line Move";
        audiolist[1] = "Minor Line Move";
        audiolist[2] = "Beep1";
        audiolist[3] = "Beep2";
        audiolist[4] = "Beep3";
        audiolist[5] = "Horn";
        audiolist[6] = "Double Horn";
        audiolist[7] = "Scream";

        audiofilevec.add("majorlinemove.wav");
        audiofilevec.add("minorlinemove.wav");
        audiofilevec.add("beep1.wav");
        audiofilevec.add("beep2.wav");
        audiofilevec.add("beep3.wav");
        audiofilevec.add("horn.wav");
        audiofilevec.add("doublehorn.wav");
        audiofilevec.add("scream.wav");

        gameperiodlist[0] = "Full Game";
        gameperiodlist[1] = "1st Half";
        gameperiodlist[2] = "2nd Half";
        gameperiodlist[3] = "All Halfs";
        gameperiodlist[4] = "1st Quarter";
        gameperiodlist[5] = "2nd Quarter";
        gameperiodlist[6] = "3rd Quarter";
        gameperiodlist[7] = "4th Quarter";
        gameperiodlist[8] = "All Quarters";
        gameperiodlist[9] = "All Periods";

        gameperiodintlist[0] = 0;
        gameperiodintlist[1] = 1;
        gameperiodintlist[2] = 2;
        gameperiodintlist[3] = 1212;
        gameperiodintlist[4] = 5;
        gameperiodintlist[5] = 6;
        gameperiodintlist[6] = 7;
        gameperiodintlist[7] = 8;
        gameperiodintlist[8] = 5678;
        gameperiodintlist[9] = 125678;


        gameperiodComboBox = new JComboBox(gameperiodlist);
        gameperiodComboBox.setMaximumRowCount(gameperiodlist.length);

        for (int v = 1; v <= 60; v++) {
            secslist[v - 1] = v + "";
        }
        for (int v = 1; v <= 10; v++) {
            numbookieslist[v - 1] = v + "";
        }
        for (int v = 0; v < 100; v++) {
            centslist[v] = v + "";
        }

        minslist[0] = "0.5";
        minslist[1] = "1.0";
        minslist[2] = "1.5";
        minslist[3] = "2.0";
        minslist[4] = "2.5";
        minslist[5] = "3.0";
        minslist[6] = "3.5";
        minslist[7] = "4.0";
        minslist[8] = "4.5";
        minslist[9] = "5.0";
        minslist[10] = "5.5";
        minslist[11] = "6.0";
        minslist[12] = "6.5";
        minslist[13] = "7.0";
        minslist[14] = "7.5";
        minslist[15] = "8.0";
        minslist[16] = "8.5";
        minslist[17] = "9.0";
        minslist[18] = "9.5";
        minslist[19] = "10.0";


        percentagelist[0] = "0.1";
        percentagelist[1] = "0.2";
        percentagelist[2] = "0.3";
        percentagelist[3] = "0.4";
        percentagelist[4] = "0.5";
        percentagelist[5] = "0.6";
        percentagelist[6] = "0.7";
        percentagelist[7] = "0.8";
        percentagelist[8] = "0.9";
        percentagelist[9] = "1.0";

        percentagelist[10] = "1.1";
        percentagelist[11] = "1.2";
        percentagelist[12] = "1.3";
        percentagelist[13] = "1.4";
        percentagelist[14] = "1.5";
        percentagelist[15] = "1.6";
        percentagelist[16] = "1.7";
        percentagelist[17] = "1.8";
        percentagelist[18] = "1.9";
        percentagelist[19] = "2.0";

        percentagelist[20] = "2.1";
        percentagelist[21] = "2.2";
        percentagelist[22] = "2.3";
        percentagelist[23] = "2.4";
        percentagelist[24] = "2.5";
        percentagelist[25] = "2.6";
        percentagelist[26] = "2.7";
        percentagelist[27] = "2.8";
        percentagelist[28] = "2.9";
        percentagelist[29] = "3.0";

        percentagelist[30] = "3.1";
        percentagelist[31] = "3.2";
        percentagelist[32] = "3.3";
        percentagelist[33] = "3.4";
        percentagelist[34] = "3.5";
        percentagelist[35] = "3.6";
        percentagelist[36] = "3.7";
        percentagelist[37] = "3.8";
        percentagelist[38] = "3.9";
        percentagelist[39] = "4.0";

        percentagelist[40] = "4.1";
        percentagelist[41] = "4.2";
        percentagelist[42] = "4.3";
        percentagelist[43] = "4.4";
        percentagelist[44] = "4.5";
        percentagelist[45] = "4.6";
        percentagelist[46] = "4.7";
        percentagelist[47] = "4.8";
        percentagelist[48] = "4.9";
        percentagelist[49] = "5.0";

        percentagelist[50] = "5.1";
        percentagelist[51] = "5.2";
        percentagelist[52] = "5.3";
        percentagelist[53] = "5.4";
        percentagelist[54] = "5.5";
        percentagelist[55] = "5.6";
        percentagelist[56] = "5.7";
        percentagelist[57] = "5.8";
        percentagelist[58] = "5.9";
        percentagelist[59] = "6.0";

        percentagelist[60] = "6.1";
        percentagelist[61] = "6.2";
        percentagelist[62] = "6.3";
        percentagelist[63] = "6.4";
        percentagelist[64] = "6.5";
        percentagelist[65] = "6.6";
        percentagelist[66] = "6.7";
        percentagelist[67] = "6.8";
        percentagelist[68] = "6.9";
        percentagelist[69] = "7.0";

        percentagelist[70] = "7.1";
        percentagelist[71] = "7.2";
        percentagelist[72] = "7.3";
        percentagelist[73] = "7.4";
        percentagelist[74] = "7.5";
        percentagelist[75] = "7.6";
        percentagelist[76] = "7.7";
        percentagelist[77] = "7.8";
        percentagelist[78] = "7.9";
        percentagelist[79] = "8.0";

        percentagelist[80] = "8.1";
        percentagelist[81] = "8.2";
        percentagelist[82] = "8.3";
        percentagelist[83] = "8.4";
        percentagelist[84] = "8.5";
        percentagelist[85] = "8.6";
        percentagelist[86] = "8.7";
        percentagelist[87] = "8.8";
        percentagelist[88] = "8.9";
        percentagelist[89] = "9.0";

        percentagelist[90] = "9.1";
        percentagelist[91] = "9.2";
        percentagelist[92] = "9.3";
        percentagelist[93] = "9.4";
        percentagelist[94] = "9.5";
        percentagelist[95] = "9.6";
        percentagelist[96] = "9.7";
        percentagelist[97] = "9.8";
        percentagelist[98] = "9.9";
        percentagelist[99] = "10.0";


        ptslist[0] = "0.0";
        ptslist[1] = "0.5";
        ptslist[2] = "1.0";
        ptslist[3] = "1.5";
        ptslist[4] = "2.0";
        ptslist[5] = "2.5";
        ptslist[6] = "3.0";
        ptslist[7] = "3.5";
        ptslist[8] = "4.0";
        ptslist[9] = "4.5";
        ptslist[10] = "5.0";
        ptslist[11] = "5.5";
        ptslist[12] = "6.0";
        ptslist[13] = "6.5";
        ptslist[14] = "7.0";

        soccerptslist[0] = "0.00";
        soccerptslist[1] = "0.25";
        soccerptslist[2] = "0.50";
        soccerptslist[3] = "0.75";
        soccerptslist[4] = "1.00";
        soccerptslist[5] = "1.25";
        soccerptslist[6] = "1.50";
        soccerptslist[7] = "1.75";
        soccerptslist[8] = "2.00";


        int index = 0;
        sportlist[index++] = "Please Select a Sport...";
        SportType [] preDefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: preDefinedSportTypes) {
            sportlist[index++] = st.getSportName();
        }
        sportComboBox = new JComboBox<>(sportlist);
        sportComboBox.setMaximumRowCount(sportlist.length);


        if(AppController.getLineAlertNodes().size() == 0) {
            Vector<String> deflan = new Vector<>();
            deflan.add("No Line Alerts Stored");
            lanComboBox = new JComboBox<>(deflan);
            lanComboBox.setSelectedIndex(0);
        } else {
            lanComboBox = new JComboBox<>(AppController.getLineAlertNodes());
            lanComboBox.setSelectedIndex(-1);
        }

        spreadptsComboBox = new JComboBox(ptslist);
        totalptsComboBox = new JComboBox(ptslist);
        teamtotalptsComboBox = new JComboBox(ptslist);

        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // *** Use FlowLayout for the content pane. ***
        userComp.setLayout(new FlowLayout());


        createSportTreeModel();
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));

        sportsbooktreePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Bookies", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));


        eventsList.setModel(eventsModel);

        selectedList.setVisibleRowCount(15);
        eventsList.setVisibleRowCount(15);
        JPanel selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Selected Sports", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        selectedPanel.add(new JScrollPane(selectedList));

        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Event Fired", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        eventsPanel.add(new JScrollPane(eventsList));
        // Make the labels.
        JLabel jlabOne = new JLabel("Button Group One");

        // Make the buttons.
        JButton jbtnOne = new JButton("One");
        JButton jbtnTwo = new JButton("Two");
        JButton jbtnThree = new JButton("Three");
        JButton jbtnFour = new JButton("Four");
        Dimension btnDim = new Dimension(100, 25);

        JButton testsoundBut = new JButton("Test Sound");
        JButton testpopupBut = new JButton("Test Popup");


        JButton defaultsoundBut = new JButton("Use Default Sound");
        JButton customsoundBut = new JButton("Use Custom Sound");

        // Set minimum and maximum sizes for the buttons.
        jbtnOne.setMinimumSize(btnDim);
        jbtnOne.setMaximumSize(btnDim);
        jbtnTwo.setMinimumSize(btnDim);
        jbtnTwo.setMaximumSize(btnDim);
        jbtnThree.setMinimumSize(btnDim);
        jbtnThree.setMaximumSize(btnDim);
        jbtnFour.setMinimumSize(btnDim);
        jbtnFour.setMaximumSize(btnDim);

        // Make the checkboxes.
        JCheckBox jcbOne = new JCheckBox("Option One");
        JCheckBox jcbTwo = new JCheckBox("Option Two");

        JCheckBox enablepopup = new JCheckBox("Enable Popup Notification");
        JCheckBox enablesound = new JCheckBox("Enable Sound Notification");

        //prefs is always null, disable following setting -- 2022-01-23
//        try {
//            enablepopup.setSelected(Boolean.parseBoolean(prefs[0]));
//        } catch (Exception ex) {
//            log(ex);
//        }
//        try {
//            enablesound.setSelected(Boolean.parseBoolean(prefs[1]));
//        } catch (Exception ex) {
//            log(ex);
//        }

        int FPS_MIN = 5;
        int FPS_MAX = 60;

        if (popupsecs > FPS_MAX) {
            FPS_MAX = popupsecs;
        }

        JSlider popupsecsslider = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, popupsecs);

        popupsecsslider.setMajorTickSpacing(5);
        popupsecsslider.setMinorTickSpacing(1);
        popupsecsslider.setPaintTicks(true);
        popupsecsslider.setPaintLabels(true);
        popupsecsslider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
//            int value = source.getValue();
            if (!source.getValueIsAdjusting()) {
                popupsecs = source.getValue();
                log("secs=" + popupsecs);
            }
        });
        String[] display2 = new String[4];
        display2[0] = "Top Left";
        display2[1] = "Bottom Left";
        display2[2] = "Top Right";
        display2[3] = "Bottom Right";

        JideToggleButton upperright = new JideToggleButton(new ImageIcon("upperright.png"));
        JideToggleButton upperleft = new JideToggleButton(new ImageIcon("upperleft.png"));
        JideToggleButton lowerright = new JideToggleButton(new ImageIcon("lowerright.png"));
        JideToggleButton lowerleft = new JideToggleButton(new ImageIcon("lowerleft.png"));

        upperright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                popuplocationint = SwingConstants.NORTH_EAST;
            }
        });
        upperleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                popuplocationint = SwingConstants.NORTH_WEST;
            }
        });
        lowerright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                popuplocationint = SwingConstants.SOUTH_EAST;
            }
        });
        lowerleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                popuplocationint = SwingConstants.SOUTH_WEST;
            }
        });


        ButtonGroup group = new ButtonGroup();
        group.add(upperright);
        group.add(upperleft);
        group.add(lowerright);
        group.add(lowerleft);

        JPanel radioPanel = new JPanel(new GridLayout(2, 2, 0, 0));


        // owen here i will load in user prefs

        if (popuplocationint == SwingConstants.NORTH_EAST) {
            upperright.setSelected(true);
        } else if (popuplocationint == SwingConstants.NORTH_WEST) {
            upperleft.setSelected(true);
        } else if (popuplocationint == SwingConstants.SOUTH_EAST) {
            lowerright.setSelected(true);
        } else if (popuplocationint == SwingConstants.SOUTH_WEST) {
            lowerleft.setSelected(true);
        }
        radioPanel.add(upperleft);
        radioPanel.add(upperright);
        radioPanel.add(lowerleft);
        radioPanel.add(lowerright);





        testsoundBut.addActionListener(ae -> {
            try {
                new SoundPlayer(defaultsoundfileplay, true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testpopupBut.addActionListener(ae -> {
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, jfrm1);
            log("popupsecs=" + popupsecs);

        });

        customsoundBut.addActionListener(ae -> {
            int returnVal = fc.showOpenDialog(jfrm1);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();

                String filename = file.getAbsolutePath();
                if (filename.contains("~") || filename.contains("|") || filename.contains("*") || filename.contains(",")
                        || filename.contains("!") || filename.contains("?")) {
                    JOptionPane.showMessageDialog(null, "Illegal character(s) used for file! Please rename file and try again");
                    return;
                }


                soundfile = file.getAbsolutePath();
                defaultsoundfileplay = soundfile;
                soundlabel.setText(soundfile);
                //This is where a real application would open the file.
                log("Opening: " + file.getName());
            } else {
                //log.append("Open command cancelled by user." + newline);
            }

        });


        defaultsoundBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                defaultsoundfileplay = defaultsoundfile;
                soundfile = "";
                soundlabel.setText("DEFAULT");

            }
        });

        removeBut.addActionListener(ae -> {
            AppController.removeLineAlertNode(lan);
            close();
        });

        saveBut.addActionListener(ae -> {


            String name = linealertname.getText().trim();


            if (!editing && !AppController.isLineAlertNameAvailable(name)) {
                JOptionPane.showMessageDialog(null, name + " name is taken!");
                return;
            } else if (name.indexOf("~") != -1 || name.indexOf("|") != -1 || name.indexOf("*") != -1 || name.indexOf(",") != -1 || name.indexOf("!") != -1 || name.indexOf("?") != -1) {
                JOptionPane.showMessageDialog(null, "Illegal character(s) used!");
                return;
            } else if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Please Name Your Line Alert!");
                return;
            }

            Vector sportselectedvec = new Vector();
            String sportselected = "";

            int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
            log("treerows=" + treeRows);
            if (treeRows != null) {
                java.util.Arrays.sort(treeRows);
                for (final int treeRow : treeRows) {
                    TreePath path = _tree.getPathForRow(treeRow);

                    String id = "" + leaguenameidhash.get("" + path.getLastPathComponent());
                    log("SAVE=" + path.getLastPathComponent() + "...." + id);
                    if (id.equals("null")) {
                        id = "" + path.getLastPathComponent();
                    }
                    if (id.contains(",")) {
                        String[] sports = id.split(",");
                        for (String sportid : sports) {
                            if (!sportid.equals("")) {
                                sportselectedvec.add(sportid);
                            }
                        }
                    } else {
                        sportselectedvec.add(id);
                    }
                    sportselected = sportselected + id + ",";


                }
            }

            String bookieselected = "";
            Vector bookieselectedvec = new Vector();
            int[] treeRows2 = sportsbooktree.getCheckBoxTreeSelectionModel().getSelectionRows();
            log("treerows2=" + treeRows2);
            if (treeRows2 != null) {
                java.util.Arrays.sort(treeRows2);
                for (final int j : treeRows2) {
                    TreePath path = sportsbooktree.getPathForRow(j);
                    log(path.getLastPathComponent());
                    String id2 = "" + bookienameidhash.get("" + path.getLastPathComponent());

                    if (id2.equals("null")) {
                        id2 = "" + path.getLastPathComponent();
                    }
                    if (id2.contains(",")) {
                        String[] books = id2.split(",");
                        for (String bookid : books) {
                            if (!bookid.equals("")) {
                                bookieselectedvec.add(bookid);
                            }
                        }
                    } else {
                        bookieselectedvec.add(id2);
                    }

                    bookieselected = bookieselected + id2 + ",";


                }
            }

            log("lan=" + name);
            log("sportselected=" + sportselected);
            log("bookieselected=" + bookieselected);

            log("spreadcheckbox=" + spreadcheckbox.isSelected());
            log("totalcheckbox=" + totalcheckbox.isSelected());
            log("moneylinecheckbox=" + moneylinecheckbox.isSelected());
            log("teamtotalcheckbox=" + teamtotalcheckbox.isSelected());

            log("gameperiodComboBox=" + gameperiodlist[gameperiodComboBox.getSelectedIndex()]);

            //need to use model here
            log("spreadptsComboBox=" + spreadptsComboBox.getModel().getSelectedItem());
            log("totalptsComboBox=" + totalptsComboBox.getModel().getSelectedItem());
            log("teamtotalptsComboBox=" + teamtotalptsComboBox.getModel().getSelectedItem());


            log("spreadsecsComboBox=" + secslist[spreadsecsComboBox.getSelectedIndex()]);
            log("totalsecsComboBox=" + secslist[totalsecsComboBox.getSelectedIndex()]);
            log("moneylinesecsComboBox=" + secslist[moneylinesecsComboBox.getSelectedIndex()]);
            log("teamtotalsecsComboBox=" + secslist[teamtotalsecsComboBox.getSelectedIndex()]);


            log("spreadbookiesComboBox=" + numbookieslist[spreadbookiesComboBox.getSelectedIndex()]);
            log("totalbookiesComboBox=" + numbookieslist[totalbookiesComboBox.getSelectedIndex()]);
            log("moneylinebookiesComboBox=" + numbookieslist[moneylinebookiesComboBox.getSelectedIndex()]);
            log("teamtotalbookiesComboBox=" + numbookieslist[teamtotalbookiesComboBox.getSelectedIndex()]);


            log("spreadjuiceComboBox=" + centslist[spreadjuiceComboBox.getSelectedIndex()]);
            log("totaljuiceComboBox=" + centslist[totaljuiceComboBox.getSelectedIndex()]);
            log("moneylinejuiceComboBox=" + centslist[moneylinejuiceComboBox.getSelectedIndex()]);
            log("teamtotaljuiceComboBox=" + centslist[teamtotaljuiceComboBox.getSelectedIndex()]);


            log("spreadpercentageComboBox=" + percentagelist[spreadpercentageComboBox.getSelectedIndex()]);
            log("totalpercentageComboBox=" + percentagelist[totalpercentageComboBox.getSelectedIndex()]);
            log("moneylinepercentageComboBox=" + percentagelist[moneylinepercentageComboBox.getSelectedIndex()]);
            log("teamtotalpercentageComboBox=" + percentagelist[teamtotalpercentageComboBox.getSelectedIndex()]);


            log("spreadaudiocheckbox=" + spreadaudiocheckbox.isSelected());
            log("totalaudiocheckbox=" + totalaudiocheckbox.isSelected());
            log("moneylineaudiocheckbox=" + moneylineaudiocheckbox.isSelected());
            log("teamtotalaudiocheckbox=" + teamtotalaudiocheckbox.isSelected());
            log("spreadaudioComboBox=" + audiolist[spreadaudioComboBox.getSelectedIndex()]);
            log("totalaudioComboBox=" + audiolist[totalaudioComboBox.getSelectedIndex()]);
            log("moneylineaudioComboBox=" + audiolist[moneylineaudioComboBox.getSelectedIndex()]);
            log("teamtotalaudioComboBox=" + audiolist[teamtotalaudioComboBox.getSelectedIndex()]);


            log("spreadpopupsecsComboBox=" + secslist[spreadpopupsecsComboBox.getSelectedIndex()]);
            log("totalpopupsecsComboBox=" + secslist[totalpopupsecsComboBox.getSelectedIndex()]);
            log("moneylinepopupsecsComboBox=" + secslist[moneylinepopupsecsComboBox.getSelectedIndex()]);
            log("teamtotalpopupsecsComboBox=" + secslist[teamtotalpopupsecsComboBox.getSelectedIndex()]);


            log("spreadpopupcheckbox=" + spreadpopupcheckbox.isSelected());
            log("totalpopupcheckbox=" + totalpopupcheckbox.isSelected());
            log("moneylinepopupcheckbox=" + moneylinepopupcheckbox.isSelected());
            log("teamtotalpopupcheckbox=" + teamtotalpopupcheckbox.isSelected());

            log("renotifyComboBox=" + minslist[renotifyComboBox.getSelectedIndex()]);
            log("renotifytotalComboBox=" + minslist[renotifytotalComboBox.getSelectedIndex()]);
            log("renotifymoneylineComboBox=" + minslist[renotifymoneylineComboBox.getSelectedIndex()]);
            log("renotifyteamtotalComboBox=" + minslist[renotifyteamtotalComboBox.getSelectedIndex()]);


            log("spreadpopuplocationint=" + spreadpopuplocationint);
            log("totalpopuplocationint=" + totalpopuplocationint);
            log("moneylinepopuplocationint=" + moneylinepopuplocationint);
            log("teamtotalpopuplocationint=" + teamtotalpopuplocationint);


            log("moveatallButton=" + moveatallButton.isSelected());
            log("movelinejuiceButton=" + movelinejuiceButton.isSelected());
            log("movepercentagebutton=" + movepercentagebutton.isSelected());


            log("totalmoveatallButton=" + totalmoveatallButton.isSelected());
            log("totalmovelinejuiceButton=" + totalmovelinejuiceButton.isSelected());
            log("totalmovepercentagebutton=" + totalmovepercentagebutton.isSelected());

            log("moneylinemoveatallButton=" + moneylinemoveatallButton.isSelected());
            log("moneylinemovelinejuiceButton=" + moneylinemovelinejuiceButton.isSelected());
            log("moneylinemovepercentagebutton=" + moneylinemovepercentagebutton.isSelected());

            log("teamtotalmoveatallButton=" + teamtotalmoveatallButton.isSelected());
            log("teamtotalmovelinejuiceButton=" + teamtotalmovelinejuiceButton.isSelected());
            log("teamtotalmovepercentagebutton=" + teamtotalmovepercentagebutton.isSelected());

            int gameperiodint = gameperiodintlist[gameperiodComboBox.getSelectedIndex()];

            LineAlertNode lan2 = new LineAlertNode(name, sport, gameperiodint, sportselectedvec, bookieselectedvec,
                    spreadcheckbox.isSelected(),
                    Integer.parseInt(secslist[spreadsecsComboBox.getSelectedIndex()]),
                    Integer.parseInt(numbookieslist[spreadbookiesComboBox.getSelectedIndex()]),
                    moveatallButton.isSelected(), movelinejuiceButton.isSelected(), movepercentagebutton.isSelected(),
                    Double.parseDouble(spreadptsComboBox.getModel().getSelectedItem().toString()),
                    Integer.parseInt(centslist[spreadjuiceComboBox.getSelectedIndex()]),
                    Double.parseDouble(percentagelist[spreadpercentageComboBox.getSelectedIndex()]),
                    spreadaudiocheckbox.isSelected(), audiofilevec.elementAt(spreadaudioComboBox.getSelectedIndex()),
                    spreadpopupcheckbox.isSelected(), spreadpopuplocationint, Integer.parseInt(secslist[spreadpopupsecsComboBox.getSelectedIndex()]),
                    Double.parseDouble(minslist[renotifyComboBox.getSelectedIndex()]),

                    totalcheckbox.isSelected(),
                    Integer.parseInt(secslist[totalsecsComboBox.getSelectedIndex()]),
                    Integer.parseInt(numbookieslist[totalbookiesComboBox.getSelectedIndex()]),
                    totalmoveatallButton.isSelected(), totalmovelinejuiceButton.isSelected(), totalmovepercentagebutton.isSelected(),
                    Double.parseDouble(totalptsComboBox.getModel().getSelectedItem().toString()),
                    Integer.parseInt(centslist[totaljuiceComboBox.getSelectedIndex()]),
                    Double.parseDouble(percentagelist[totalpercentageComboBox.getSelectedIndex()]),
                    totalaudiocheckbox.isSelected(), audiofilevec.elementAt(totalaudioComboBox.getSelectedIndex()),
                    totalpopupcheckbox.isSelected(), totalpopuplocationint, Integer.parseInt(secslist[totalpopupsecsComboBox.getSelectedIndex()]),
                    Double.parseDouble(minslist[renotifytotalComboBox.getSelectedIndex()]),

                    moneylinecheckbox.isSelected(),
                    Integer.parseInt(secslist[moneylinesecsComboBox.getSelectedIndex()]),
                    Integer.parseInt(numbookieslist[moneylinebookiesComboBox.getSelectedIndex()]),
                    moneylinemoveatallButton.isSelected(), moneylinemovelinejuiceButton.isSelected(), moneylinemovepercentagebutton.isSelected(),
                    Integer.parseInt(centslist[moneylinejuiceComboBox.getSelectedIndex()]),
                    Double.parseDouble(percentagelist[moneylinepercentageComboBox.getSelectedIndex()]),
                    moneylineaudiocheckbox.isSelected(),audiofilevec.elementAt(moneylineaudioComboBox.getSelectedIndex()),
                    moneylinepopupcheckbox.isSelected(), moneylinepopuplocationint, Integer.parseInt(secslist[moneylinepopupsecsComboBox.getSelectedIndex()]),
                    Double.parseDouble(minslist[renotifymoneylineComboBox.getSelectedIndex()]),


                    teamtotalcheckbox.isSelected(),
                    Integer.parseInt(secslist[teamtotalsecsComboBox.getSelectedIndex()]),
                    Integer.parseInt(numbookieslist[teamtotalbookiesComboBox.getSelectedIndex()]),
                    teamtotalmoveatallButton.isSelected(), teamtotalmovelinejuiceButton.isSelected(), teamtotalmovepercentagebutton.isSelected(),
                    Double.parseDouble(teamtotalptsComboBox.getModel().getSelectedItem().toString()),
                    Integer.parseInt(centslist[teamtotaljuiceComboBox.getSelectedIndex()]),
                    Double.parseDouble(percentagelist[teamtotalpercentageComboBox.getSelectedIndex()]),
                    teamtotalaudiocheckbox.isSelected(),audiofilevec.elementAt(teamtotalaudioComboBox.getSelectedIndex()),
                    teamtotalpopupcheckbox.isSelected(), teamtotalpopuplocationint, Integer.parseInt(secslist[teamtotalpopupsecsComboBox.getSelectedIndex()]),
                    Double.parseDouble(minslist[renotifyteamtotalComboBox.getSelectedIndex()])
            );

            if (editing) // remove old
            {
                AppController.replaceLineAlertNode(lan, lan2);
            } else {
                AppController.addLineAlertNode(lan2);
            }
            close();
        });

        JPanel testbutPanel = new JPanel(new GridLayout(0, 2, 2, 2));

        testbutPanel.add(testpopupBut);
        testbutPanel.add(testsoundBut);

        JPanel soundbutPanel = new JPanel(new GridLayout(0, 2, 2, 2));

        soundbutPanel.add(defaultsoundBut);
        soundbutPanel.add(customsoundBut);


        sportComboBox.setSelectedIndex(0);
        sportComboBox.addItemListener(this);
       // lanComboBox.setSelectedIndex(0);
        lanComboBox.addItemListener(this);

        JLabel ifwithin = new JLabel("If within ");
        JLabel seconds = new JLabel(" seconds, >= ");
//        JLabel greaterthanorequal = new JLabel(">= ");
        JLabel bookieslab = new JLabel(" bookies ");

        JLabel pts = new JLabel(" pts or move juice >=");
        JLabel centslab = new JLabel(" cents");
        JLabel forlab = new JLabel("for ");
        JLabel secondslab = new JLabel("seconds");
        JLabel renotifyme = new JLabel("Renotify me on same side only after");
        JLabel renotifyme2 = new JLabel(" minutes have elapsed");


        spreadsecsComboBox = new JComboBox(secslist);
        totalsecsComboBox = new JComboBox(secslist);
        moneylinesecsComboBox = new JComboBox(secslist);
        teamtotalsecsComboBox = new JComboBox(secslist);


        spreadbookiesComboBox = new JComboBox(numbookieslist);
        totalbookiesComboBox = new JComboBox(numbookieslist);
        moneylinebookiesComboBox = new JComboBox(numbookieslist);
        teamtotalbookiesComboBox = new JComboBox(numbookieslist);

        moveatallButton = new JRadioButton("move at all");
        totalmoveatallButton = new JRadioButton("move at all");
        moneylinemoveatallButton = new JRadioButton("move at all");
        teamtotalmoveatallButton = new JRadioButton("move at all");


        movelinejuiceButton = new JRadioButton("move line >=");
        moneylinemovelinejuiceButton = new JRadioButton("move juice >=");
        totalmovelinejuiceButton = new JRadioButton("move line >=");
        teamtotalmovelinejuiceButton = new JRadioButton("move line >=");


        spreadjuiceComboBox = new JComboBox(centslist);
        totaljuiceComboBox = new JComboBox(centslist);
        moneylinejuiceComboBox = new JComboBox(centslist);
        teamtotaljuiceComboBox = new JComboBox(centslist);

        movepercentagebutton = new JRadioButton("move >=");
        totalmovepercentagebutton = new JRadioButton("move >=");
        moneylinemovepercentagebutton = new JRadioButton("move >=");
        teamtotalmovepercentagebutton = new JRadioButton("move >=");
        //JLabel movepercentage = new JLabel("move >= ");
        spreadpercentageComboBox = new JComboBox(percentagelist);
        totalpercentageComboBox = new JComboBox(percentagelist);
        moneylinepercentageComboBox = new JComboBox(percentagelist);
        teamtotalpercentageComboBox = new JComboBox(percentagelist);

        JLabel percentofvalue = new JLabel(" % of value");
        JLabel totalpercentofvalue = new JLabel(" % of value");
        JLabel moneylinepercentofvalue = new JLabel(" % of value");
        JLabel teamtotalpercentofvalue = new JLabel(" % of value");

        spreadaudiocheckbox = new JCheckBox("Play Audio");
        spreadaudioComboBox = new JComboBox(audiolist);
        totalaudiocheckbox = new JCheckBox("Play Audio");
        totalaudioComboBox = new JComboBox(audiolist);
        moneylineaudiocheckbox = new JCheckBox("Play Audio");
        moneylineaudioComboBox = new JComboBox(audiolist);
        teamtotalaudiocheckbox = new JCheckBox("Play Audio");
        teamtotalaudioComboBox = new JComboBox(audiolist);


        spreadpopupcheckbox = new JCheckBox("Show Popup");
        spreadpopupsecsComboBox = new JComboBox(secslist);

        totalpopupcheckbox = new JCheckBox("Show Popup");
        totalpopupsecsComboBox = new JComboBox(secslist);

        moneylinepopupcheckbox = new JCheckBox("Show Popup");
        moneylinepopupsecsComboBox = new JComboBox(secslist);

        teamtotalpopupcheckbox = new JCheckBox("Show Popup");
        teamtotalpopupsecsComboBox = new JComboBox(secslist);

        testspreadsoundBut = new JButton("Test Sound");
        testspreadpopupBut = new JButton("Test Popup");

        testtotalsoundBut = new JButton("Test Sound");
        testtotalpopupBut = new JButton("Test Popup");

        testmoneylinesoundBut = new JButton("Test Sound");
        testmoneylinepopupBut = new JButton("Test Popup");

        testteamtotalsoundBut = new JButton("Test Sound");
        testteamtotalpopupBut = new JButton("Test Popup");

        renotifyComboBox = new JComboBox(minslist);
        renotifytotalComboBox = new JComboBox(minslist);
        renotifymoneylineComboBox = new JComboBox(minslist);
        renotifyteamtotalComboBox = new JComboBox(minslist);


        ButtonGroup spreadbuttongroup = new ButtonGroup();
        spreadbuttongroup.add(moveatallButton);
        spreadbuttongroup.add(movelinejuiceButton);
        spreadbuttongroup.add(movepercentagebutton);

        ButtonGroup totalbuttongroup = new ButtonGroup();
        totalbuttongroup.add(totalmoveatallButton);
        totalbuttongroup.add(totalmovelinejuiceButton);
        totalbuttongroup.add(totalmovepercentagebutton);

        ButtonGroup moneylinebuttongroup = new ButtonGroup();
        moneylinebuttongroup.add(moneylinemoveatallButton);
        moneylinebuttongroup.add(moneylinemovelinejuiceButton);
        moneylinebuttongroup.add(moneylinemovepercentagebutton);

        ButtonGroup teamtotalbuttongroup = new ButtonGroup();
        teamtotalbuttongroup.add(teamtotalmoveatallButton);
        teamtotalbuttongroup.add(teamtotalmovelinejuiceButton);
        teamtotalbuttongroup.add(teamtotalmovepercentagebutton);


        spreadpopuplocation = new JComboBox(display2);
        totalpopuplocation = new JComboBox(display2);
        moneylinepopuplocation = new JComboBox(display2);
        teamtotalpopuplocation = new JComboBox(display2);


        testspreadsoundBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    new SoundPlayer(audiofilevec.elementAt(spreadaudioComboBox.getSelectedIndex()), true);
                    //playSound(defaultsoundfileplay);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error Playing File!");
                    log(ex);
                }
            }
        });

        testtotalsoundBut.addActionListener(ae -> {
            try {
                new SoundPlayer(audiofilevec.elementAt(totalaudioComboBox.getSelectedIndex()), true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testteamtotalsoundBut.addActionListener(ae -> {
            try {
                new SoundPlayer(audiofilevec.elementAt(teamtotalaudioComboBox.getSelectedIndex()), true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testmoneylinesoundBut.addActionListener(ae -> {
            try {
                new SoundPlayer(audiofilevec.elementAt(moneylineaudioComboBox.getSelectedIndex()), true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testspreadpopupBut.addActionListener(ae -> new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                "</TABLE></FONT></HTML>", Integer.parseInt(secslist[spreadpopupsecsComboBox.getSelectedIndex()]) * 1000, spreadpopuplocationint, jfrm1));

        testtotalpopupBut.addActionListener(ae -> new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                "</TABLE></FONT></HTML>", Integer.parseInt(secslist[totalpopupsecsComboBox.getSelectedIndex()]) * 1000, totalpopuplocationint, jfrm1));
        testteamtotalpopupBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                        "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                        "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                        "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                        "</TABLE></FONT></HTML>", Integer.parseInt(secslist[teamtotalpopupsecsComboBox.getSelectedIndex()]) * 1000, teamtotalpopuplocationint, jfrm1);


            }
        });
        testmoneylinepopupBut.addActionListener(ae -> new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                "</TABLE></FONT></HTML>", Integer.parseInt(secslist[moneylinepopupsecsComboBox.getSelectedIndex()]) * 1000, moneylinepopuplocationint, jfrm1));

        spreadupperright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                spreadpopuplocationint = SwingConstants.NORTH_EAST;
            }
        });
        spreadupperleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                spreadpopuplocationint = SwingConstants.NORTH_WEST;
            }
        });
        spreadlowerright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                spreadpopuplocationint = SwingConstants.SOUTH_EAST;
            }
        });
        spreadlowerleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                spreadpopuplocationint = SwingConstants.SOUTH_WEST;
            }
        });


        ButtonGroup spreadgroup = new ButtonGroup();
        spreadgroup.add(spreadupperright);
        spreadgroup.add(spreadupperleft);
        spreadgroup.add(spreadlowerright);
        spreadgroup.add(spreadlowerleft);

        JPanel spreadradioPanel = new JPanel(new GridLayout(2, 2, 0, 0));


        // owen here i will load in user prefs

        if (spreadpopuplocationint == SwingConstants.NORTH_EAST) {
            spreadupperright.setSelected(true);
        } else if (spreadpopuplocationint == SwingConstants.NORTH_WEST) {
            spreadupperleft.setSelected(true);
        } else if (spreadpopuplocationint == SwingConstants.SOUTH_EAST) {
            spreadlowerright.setSelected(true);
        } else if (spreadpopuplocationint == SwingConstants.SOUTH_WEST) {
            spreadlowerleft.setSelected(true);
        }
        spreadradioPanel.add(spreadupperleft);
        spreadradioPanel.add(spreadupperright);
        spreadradioPanel.add(spreadlowerleft);
        spreadradioPanel.add(spreadlowerright);


//total
        totalupperright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                totalpopuplocationint = SwingConstants.NORTH_EAST;
            }
        });
        totalupperleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                totalpopuplocationint = SwingConstants.NORTH_WEST;
            }
        });
        totallowerright.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                totalpopuplocationint = SwingConstants.SOUTH_EAST;
            }
        });
        totallowerleft.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                totalpopuplocationint = SwingConstants.SOUTH_WEST;
            }
        });


        ButtonGroup totalgroup = new ButtonGroup();
        totalgroup.add(totalupperright);
        totalgroup.add(totalupperleft);
        totalgroup.add(totallowerright);
        totalgroup.add(totallowerleft);

        JPanel totalradioPanel = new JPanel(new GridLayout(2, 2, 0, 0));


        // owen here i will load in user prefs

        if (totalpopuplocationint == SwingConstants.NORTH_EAST) {
            totalupperright.setSelected(true);
        } else if (totalpopuplocationint == SwingConstants.NORTH_WEST) {
            totalupperleft.setSelected(true);
        } else if (totalpopuplocationint == SwingConstants.SOUTH_EAST) {
            totallowerright.setSelected(true);
        } else if (totalpopuplocationint == SwingConstants.SOUTH_WEST) {
            totallowerleft.setSelected(true);
        }
        totalradioPanel.add(totalupperleft);
        totalradioPanel.add(totalupperright);
        totalradioPanel.add(totallowerleft);
        totalradioPanel.add(totallowerright);
// end total

//moneyline
        moneylineupperright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    moneylinepopuplocationint = SwingConstants.NORTH_EAST;
                }
            }
        });
        moneylineupperleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    moneylinepopuplocationint = SwingConstants.NORTH_WEST;
                }
            }
        });
        moneylinelowerright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    moneylinepopuplocationint = SwingConstants.SOUTH_EAST;
                }
            }
        });
        moneylinelowerleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    moneylinepopuplocationint = SwingConstants.SOUTH_WEST;
                }
            }
        });


        ButtonGroup moneylinegroup = new ButtonGroup();
        moneylinegroup.add(moneylineupperright);
        moneylinegroup.add(moneylineupperleft);
        moneylinegroup.add(moneylinelowerright);
        moneylinegroup.add(moneylinelowerleft);

        JPanel moneylineradioPanel = new JPanel(new GridLayout(2, 2, 0, 0));


        // owen here i will load in user prefs

        if (moneylinepopuplocationint == SwingConstants.NORTH_EAST) {
            moneylineupperright.setSelected(true);
        } else if (moneylinepopuplocationint == SwingConstants.NORTH_WEST) {
            moneylineupperleft.setSelected(true);
        } else if (moneylinepopuplocationint == SwingConstants.SOUTH_EAST) {
            moneylinelowerright.setSelected(true);
        } else if (moneylinepopuplocationint == SwingConstants.SOUTH_WEST) {
            moneylinelowerleft.setSelected(true);
        }
        moneylineradioPanel.add(moneylineupperleft);
        moneylineradioPanel.add(moneylineupperright);
        moneylineradioPanel.add(moneylinelowerleft);
        moneylineradioPanel.add(moneylinelowerright);

        teamtotalupperright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    teamtotalpopuplocationint = SwingConstants.NORTH_EAST;
                }
            }
        });
        teamtotalupperleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    teamtotalpopuplocationint = SwingConstants.NORTH_WEST;
                }
            }
        });
        teamtotallowerright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    teamtotalpopuplocationint = SwingConstants.SOUTH_EAST;
                }
            }
        });
        teamtotallowerleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    teamtotalpopuplocationint = SwingConstants.SOUTH_WEST;
                }
            }
        });


        ButtonGroup teamtotalgroup = new ButtonGroup();
        teamtotalgroup.add(teamtotalupperright);
        teamtotalgroup.add(teamtotalupperleft);
        teamtotalgroup.add(teamtotallowerright);
        teamtotalgroup.add(teamtotallowerleft);

        JPanel teamtotalradioPanel = new JPanel(new GridLayout(2, 2, 0, 0));


        // owen here i will load in user prefs

        if (teamtotalpopuplocationint == SwingConstants.NORTH_EAST) {
            teamtotalupperright.setSelected(true);
        } else if (teamtotalpopuplocationint == SwingConstants.NORTH_WEST) {
            teamtotalupperleft.setSelected(true);
        } else if (teamtotalpopuplocationint == SwingConstants.SOUTH_EAST) {
            teamtotallowerright.setSelected(true);
        } else if (teamtotalpopuplocationint == SwingConstants.SOUTH_WEST) {
            teamtotallowerleft.setSelected(true);
        }
        teamtotalradioPanel.add(teamtotalupperleft);
        teamtotalradioPanel.add(teamtotalupperright);
        teamtotalradioPanel.add(teamtotallowerleft);
        teamtotalradioPanel.add(teamtotallowerright);

        spreadPanel.setBorder(BorderFactory.createEtchedBorder());
        spreadPanel.setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10; // 8
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = 0;
        c.insets = EMPTY_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        spreadPanel.add(spreadcheckbox, c);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 1;

        c.gridx = 0;
        spreadPanel.add(ifwithin, c);
        c.gridx = 1;
        spreadPanel.add(spreadsecsComboBox, c);
        c.gridx = 2;
        spreadPanel.add(seconds, c);


        c.gridx = 3;
        spreadPanel.add(spreadbookiesComboBox, c);
        c.gridx = 4;
        spreadPanel.add(bookieslab, c);


        c.gridx = 5;

        spreadPanel.add(moveatallButton, c);

        c.gridy = 2;

        c.gridx = 5;
        spreadPanel.add(movelinejuiceButton, c);
        c.gridx = 6;
        spreadPanel.add(spreadptsComboBox, c);
        c.gridx = 7;
        spreadPanel.add(pts, c);
        c.gridx = 8;
        spreadPanel.add(spreadjuiceComboBox, c);
        c.gridx = 9;
        spreadPanel.add(centslab, c);

        c.gridy = 3;

        c.gridx = 5;
        spreadPanel.add(movepercentagebutton, c);
        c.gridx = 6;
        spreadPanel.add(spreadpercentageComboBox, c);
        c.gridx = 7;
        spreadPanel.add(percentofvalue, c);


        c.gridy = 4;

        c.gridx = 0;
        c.gridwidth = 2;
        spreadPanel.add(spreadaudiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 4;
        spreadPanel.add(spreadaudioComboBox, c);
        c.gridx = 8;
        c.gridwidth = 2;
        spreadPanel.add(testspreadsoundBut, c);


        c.gridy = 5;

        c.gridx = 0;
        c.gridwidth = 2;
        spreadPanel.add(spreadpopupcheckbox, c);
        c.gridx = 2;
        c.gridwidth = 1;
        spreadPanel.add(spreadradioPanel, c);
        c.gridx = 3;
        c.gridwidth = 1;
        spreadPanel.add(forlab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        spreadPanel.add(spreadpopupsecsComboBox, c);
        c.gridx = 5;
        c.gridwidth = 1;
        spreadPanel.add(secondslab, c);
        c.gridx = 8;
        c.gridwidth = 2;
        spreadPanel.add(testspreadpopupBut, c);

        c.gridy = 6;

        c.gridx = 0;
        c.gridwidth = 4;
        spreadPanel.add(renotifyme, c);
        c.gridx = 4;
        c.gridwidth = 1;
        spreadPanel.add(renotifyComboBox, c);
        c.gridx = 5;
        c.gridwidth = 3;
        spreadPanel.add(renotifyme2, c);

        totalPanel.setBorder(BorderFactory.createEtchedBorder());
        totalPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        ifwithin = new JLabel("If within ");
        seconds = new JLabel(" seconds, >= ");
//        greaterthanorequal = new JLabel(">= ");
        bookieslab = new JLabel(" bookies ");

        pts = new JLabel(" pts or move juice >=");
        centslab = new JLabel(" cents");
        forlab = new JLabel("for ");
        secondslab = new JLabel("seconds");
        renotifyme = new JLabel("Renotify me on same side only after");
        renotifyme2 = new JLabel(" minutes have elapsed");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = 0;
        c.insets = EMPTY_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        totalPanel.add(totalcheckbox, c);
        c.gridheight = 1;


        c.gridwidth = 1;
        c.gridy = 1;

        c.gridx = 0;
        totalPanel.add(ifwithin, c);
        c.gridx = 1;
        totalPanel.add(totalsecsComboBox, c);
        c.gridx = 2;
        totalPanel.add(seconds, c);


        c.gridx = 3;
        totalPanel.add(totalbookiesComboBox, c);
        c.gridx = 4;
        totalPanel.add(bookieslab, c);


        c.gridx = 5;

        totalPanel.add(totalmoveatallButton, c);

        c.gridy = 2;

        c.gridx = 5;
        totalPanel.add(totalmovelinejuiceButton, c);
        c.gridx = 6;
        totalPanel.add(totalptsComboBox, c);
        c.gridx = 7;
        totalPanel.add(pts, c);
        c.gridx = 8;
        totalPanel.add(totaljuiceComboBox, c);
        c.gridx = 9;
        totalPanel.add(centslab, c);

        c.gridy = 3;

        c.gridx = 5;
        totalPanel.add(totalmovepercentagebutton, c);
        c.gridx = 6;
        totalPanel.add(totalpercentageComboBox, c);
        c.gridx = 7;
        totalPanel.add(totalpercentofvalue, c);


        c.gridy = 4;

        c.gridx = 0;
        c.gridwidth = 2;
        totalPanel.add(totalaudiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 4;
        totalPanel.add(totalaudioComboBox, c);
        c.gridx = 8;
        c.gridwidth = 2;
        totalPanel.add(testtotalsoundBut, c);


        c.gridy = 5;

        c.gridx = 0;
        c.gridwidth = 2;
        totalPanel.add(totalpopupcheckbox, c);
        c.gridx = 2;
        c.gridwidth = 1;
        totalPanel.add(totalradioPanel, c);
        c.gridx = 3;
        c.gridwidth = 1;
        totalPanel.add(forlab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        totalPanel.add(totalpopupsecsComboBox, c);
        c.gridx = 5;
        c.gridwidth = 1;
        totalPanel.add(secondslab, c);
        c.gridx = 8;
        c.gridwidth = 2;
        totalPanel.add(testtotalpopupBut, c);

        c.gridy = 6;

        c.gridx = 0;
        c.gridwidth = 4;
        totalPanel.add(renotifyme, c);
        c.gridx = 4;
        c.gridwidth = 1;
        totalPanel.add(renotifytotalComboBox, c);
        c.gridx = 5;
        c.gridwidth = 3;
        totalPanel.add(renotifyme2, c);

        moneylinePanel.setBorder(BorderFactory.createEtchedBorder());
        moneylinePanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        ifwithin = new JLabel("If within ");
        seconds = new JLabel(" seconds, >= ");
//        greaterthanorequal = new JLabel(">= ");
        bookieslab = new JLabel(" bookies ");

//        pts = new JLabel(" pts or move juice >=");
        centslab = new JLabel(" cents");
        forlab = new JLabel("for ");
        secondslab = new JLabel("seconds");
        renotifyme = new JLabel("Renotify me on same side only after");
        renotifyme2 = new JLabel(" minutes have elapsed");

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = 0;
        c.insets = EMPTY_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        moneylinePanel.add(moneylinecheckbox, c);
        c.gridheight = 1;


        c.gridwidth = 1;
        c.gridy = 1;

        c.gridx = 0;
        moneylinePanel.add(ifwithin, c);
        c.gridx = 1;
        moneylinePanel.add(moneylinesecsComboBox, c);
        c.gridx = 2;
        moneylinePanel.add(seconds, c);


        c.gridx = 3;
        moneylinePanel.add(moneylinebookiesComboBox, c);
        c.gridx = 4;
        moneylinePanel.add(bookieslab, c);


        c.gridx = 5;

        moneylinePanel.add(moneylinemoveatallButton, c);

        c.gridy = 2;

        c.gridx = 5;

        moneylinePanel.add(moneylinemovelinejuiceButton, c);
        c.gridx = 6;

        moneylinePanel.add(moneylinejuiceComboBox, c);

        c.gridx = 7;

        moneylinePanel.add(centslab, c);

        c.gridx = 8;
        moneylinePanel.add(new JLabel("                          "), c);
        c.gridx = 9;
        moneylinePanel.add(new JLabel("                          "), c);
        c.gridy = 3;

        c.gridx = 5;
        moneylinePanel.add(moneylinemovepercentagebutton, c);
        c.gridx = 6;
        moneylinePanel.add(moneylinepercentageComboBox, c);
        c.gridx = 7;
        moneylinePanel.add(moneylinepercentofvalue, c);


        c.gridy = 4;

        c.gridx = 0;
        c.gridwidth = 2;
        moneylinePanel.add(moneylineaudiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 4;
        moneylinePanel.add(moneylineaudioComboBox, c);
        c.gridx = 8;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        moneylinePanel.add(testmoneylinesoundBut, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridy = 5;

        c.gridx = 0;
        c.gridwidth = 2;
        moneylinePanel.add(moneylinepopupcheckbox, c);
        c.gridx = 2;
        c.gridwidth = 1;
        moneylinePanel.add(moneylineradioPanel, c);
        c.gridx = 3;
        c.gridwidth = 1;
        moneylinePanel.add(forlab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        moneylinePanel.add(moneylinepopupsecsComboBox, c);
        c.gridx = 5;
        c.gridwidth = 1;
        moneylinePanel.add(secondslab, c);
        c.gridx = 8;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.EAST;
        moneylinePanel.add(testmoneylinepopupBut, c);


        c.anchor = GridBagConstraints.WEST;
        c.gridy = 6;

        c.gridx = 0;
        c.gridwidth = 4;
        moneylinePanel.add(renotifyme, c);
        c.gridx = 4;
        c.gridwidth = 1;
        moneylinePanel.add(renotifymoneylineComboBox, c);
        c.gridx = 5;
        c.gridwidth = 3;
        moneylinePanel.add(renotifyme2, c);

        teamtotalPanel.setBorder(BorderFactory.createEtchedBorder());
        teamtotalPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        ifwithin = new JLabel("If within ");
        seconds = new JLabel(" seconds, >= ");
//        greaterthanorequal = new JLabel(">= ");
        bookieslab = new JLabel(" bookies ");

        pts = new JLabel(" pts or move juice >=");
        centslab = new JLabel(" cents");
        forlab = new JLabel("for ");
        secondslab = new JLabel("seconds");
        renotifyme = new JLabel("Renotify me on same side only after");
        renotifyme2 = new JLabel(" minutes have elapsed");


        //GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill,
        //Insets insets, int ipadx, int ipady)

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = 0;
        c.insets = EMPTY_INSETS;
        c.ipadx = 0;
        c.ipady = 0;
        teamtotalPanel.add(teamtotalcheckbox, c);
        c.gridheight = 1;


        c.gridwidth = 1;
        c.gridy = 1;

        c.gridx = 0;
        teamtotalPanel.add(ifwithin, c);
        c.gridx = 1;
        teamtotalPanel.add(teamtotalsecsComboBox, c);
        c.gridx = 2;
        teamtotalPanel.add(seconds, c);


        c.gridx = 3;
        teamtotalPanel.add(teamtotalbookiesComboBox, c);
        c.gridx = 4;
        teamtotalPanel.add(bookieslab, c);


        c.gridx = 5;

        teamtotalPanel.add(teamtotalmoveatallButton, c);

        c.gridy = 2;

        c.gridx = 5;
        teamtotalPanel.add(teamtotalmovelinejuiceButton, c);
        c.gridx = 6;
        teamtotalPanel.add(teamtotalptsComboBox, c);
        c.gridx = 7;
        teamtotalPanel.add(pts, c);
        c.gridx = 8;
        teamtotalPanel.add(teamtotaljuiceComboBox, c);
        c.gridx = 9;
        teamtotalPanel.add(centslab, c);

        c.gridy = 3;

        c.gridx = 5;
        teamtotalPanel.add(teamtotalmovepercentagebutton, c);
        c.gridx = 6;
        teamtotalPanel.add(teamtotalpercentageComboBox, c);
        c.gridx = 7;
        teamtotalPanel.add(teamtotalpercentofvalue, c);


        c.gridy = 4;

        c.gridx = 0;
        c.gridwidth = 2;
        teamtotalPanel.add(teamtotalaudiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 4;
        teamtotalPanel.add(teamtotalaudioComboBox, c);
        c.gridx = 8;
        c.gridwidth = 2;
        teamtotalPanel.add(testteamtotalsoundBut, c);


        c.gridy = 5;

        c.gridx = 0;
        c.gridwidth = 2;
        teamtotalPanel.add(teamtotalpopupcheckbox, c);
        c.gridx = 2;
        c.gridwidth = 1;
        teamtotalPanel.add(teamtotalradioPanel, c);
        c.gridx = 3;
        c.gridwidth = 1;
        teamtotalPanel.add(forlab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        teamtotalPanel.add(teamtotalpopupsecsComboBox, c);
        c.gridx = 5;
        c.gridwidth = 1;
        teamtotalPanel.add(secondslab, c);
        c.gridx = 8;
        c.gridwidth = 2;
        teamtotalPanel.add(testteamtotalpopupBut, c);

        c.gridy = 6;

        c.gridx = 0;
        c.gridwidth = 4;
        teamtotalPanel.add(renotifyme, c);
        c.gridx = 4;
        c.gridwidth = 1;
        teamtotalPanel.add(renotifyteamtotalComboBox, c);
        c.gridx = 5;
        c.gridwidth = 3;
        teamtotalPanel.add(renotifyme2, c);

        // end teamtotalpanel

//owen 9/21 next step is to add all these to a panel and see how it looks!


        // Create three vertical boxes.


        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));


        box2.add(new JLabel("Add New Line Alert"));
        box2.add(sportComboBox);
        box1.add(new JLabel("Edit Existing Line Alert"));
        box1.add(lanComboBox);


        //box2.add(selectedPanel);


        box3.add(jlabOne);
        box3.add(jcbOne);
        box3.add(jcbTwo);


        //initialize tree
        for (int j = 0; j < checkednodes.size(); j++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes.elementAt(j);
            TreePath path = new TreePath((node).getPath());
            _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
        }

        selectedList.revalidate();
        selectedPanel.revalidate();

        selectedList.setEnabled(false);

        // Add the boxes to the content pane.
        userComp.add(box2);
        userComp.add(box1);

        return userComp;
    }

    public void createSportTreeModel() {

        // first lets create sportsbook checkboxtree
        DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);


        try {

            Vector<Bookie> newBookiesVec = AppController.getBookiesVec();
            List<Bookie> hiddencols = AppController.getHiddenCols();
            String allbookies = "";
            for (Bookie b : newBookiesVec) {
                if (hiddencols.contains(b) || b.getBookie_id() >= 990) {
                    continue;
                }


                DefaultMutableTreeNode tempnode;


                DefaultMutableTreeNode child = new DefaultMutableTreeNode(b.getName());
                rootbookie.add(child);
                bookienameidhash.put(b.getName(), "" + b.getBookie_id());
                allbookies = allbookies + b.getBookie_id() + ",";
            }
            if (allbookies.endsWith(",")) {
                allbookies = allbookies.substring(0, allbookies.length() - 1);
            }

            bookienameidhash.put("All Bookies", allbookies);
            sportsbooktree = new CheckBoxTree(treeModelbookie) {
                @Override
                public Dimension getPreferredScrollableViewportSize() {
                    return new Dimension(300, 300);
                }
            };
            sportsbooktree.setRootVisible(true);
            sportsbooktree.getCheckBoxTreeSelectionModel().setDigIn(true);
            sportsbooktree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            sportsbooktree.setClickInCheckBoxOnly(false);

            sportsbooktree.setShowsRootHandles(true);
            DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) sportsbooktree.getActualCellRenderer();
            renderer2.setLeafIcon(null);
            renderer2.setOpenIcon(null);
            renderer2.setClosedIcon(null);


            sportsbooktreePanel.add(new JScrollPane(sportsbooktree));


        } catch (Exception e) {
            log(e);
        }


        for (int z = 0; z < sportlist.length; z++) {
            String thissport = sportlist[z];

            DefaultMutableTreeNode root = new DefaultMutableTreeNode(thissport);
            DefaultTreeModel treeModel = new DefaultTreeModel(root);


            try {
                String allsports = "";
                List<Sport> sportsVec = AppController.getSportsVec();
                for (Sport sport : sportsVec) {
                    if (sport.getSportname().equals(thissport)) {

                    } else {
                        continue;
                    }


                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                    root.add(child);
                    leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());


                    allsports = allsports + sport.getLeague_id() + ",";

                }
                if (allsports.endsWith(",")) {
                    allsports = allsports.substring(0, allsports.length() - 1);
                }
                leaguenameidhash.put(thissport, allsports);
                CheckBoxTree thistree = new CheckBoxTree(treeModel) {
                    @Override
                    public Dimension getPreferredScrollableViewportSize() {
                        return new Dimension(300, 300);
                    }
                };
                thistree.setRootVisible(true);
                thistree.getCheckBoxTreeSelectionModel().setDigIn(true);
                thistree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                thistree.setClickInCheckBoxOnly(false);

                thistree.setShowsRootHandles(true);
                DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) thistree.getActualCellRenderer();
                renderer.setLeafIcon(null);
                renderer.setOpenIcon(null);
                renderer.setClosedIcon(null);
                trees[z] = thistree;

                if (z == 0) {
                    treePanel.add(new JLabel(""));
                }

            } catch (Exception e) {
                log(e);
            }

        }

    }
    public void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error Playing File! Check file path. Only AIFF,AU and WAV are supported!");
            log(ex);
        }
    }
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        if (e.getSource() == sportComboBox) {
            sport = sportlist[sportComboBox.getSelectedIndex()];
            _tree = trees[sportComboBox.getSelectedIndex()];
            treePanel.removeAll();
            treePanel.add(new JScrollPane(_tree));
            treePanel.revalidate();
            treePanel.repaint();

            if (sportComboBox.getSelectedIndex() != 0) {


                if (sportComboBox.getSelectedIndex() == 5) {
                    DefaultComboBoxModel model = new DefaultComboBoxModel(soccerptslist);
                    spreadptsComboBox.setModel(model);
                    DefaultComboBoxModel model2 = new DefaultComboBoxModel(soccerptslist);
                    totalptsComboBox.setModel(model2);
                    DefaultComboBoxModel model3 = new DefaultComboBoxModel(soccerptslist);
                    teamtotalptsComboBox.setModel(model3);
                }


                box2.removeAll();
                box1.removeAll();

                box1.add(welcome);
                box1.add(gameperiodComboBox);
                box1.add(treePanel);
                box1.add(sportsbooktreePanel);
                box1.add(linealertname);
                box1.add(saveBut);
                box1.revalidate();
                box1.repaint();

                box2.add(spreadPanel);
                box2.add(totalPanel);
                box2.add(moneylinePanel);
                box2.add(teamtotalPanel);


                box2.revalidate();
                box2.repaint();
            }

        } else if (e.getSource() == lanComboBox) {

            editing = true;
            lan = AppController.getLineAlertNodes().elementAt(lanComboBox.getSelectedIndex());
            sport = lan.getSport();
            if (lan.getSport().equals(SportName.Soccer)) {
                DefaultComboBoxModel model = new DefaultComboBoxModel(soccerptslist);
                spreadptsComboBox.setModel(model);
                DefaultComboBoxModel model2 = new DefaultComboBoxModel(soccerptslist);
                totalptsComboBox.setModel(model2);
                DefaultComboBoxModel model3 = new DefaultComboBoxModel(soccerptslist);
                teamtotalptsComboBox.setModel(model3);
            }


            List<String> lansports = lan.getSportCodes();
            List<String> lanbookies = lan.getBookieCodes();
            linealertname.setText(lan.getName());
            linealertname.setEnabled(false);

            gameperiodComboBox.setSelectedIndex(gamePeriodIndex(lan.getGamePeriod()));

            spreadcheckbox.setSelected(lan.isSpreadCheck());
            spreadsecsComboBox.setSelectedItem("" + lan.getSpreadSecs());
            spreadbookiesComboBox.setSelectedItem("" + lan.getSpreadNumBookies());
            moveatallButton.setSelected(lan.isSpreadMoveAtAll());
            movelinejuiceButton.setSelected(lan.isSpreadPtsMove());
            movepercentagebutton.setSelected(lan.isSpreadPercentageMove());
            spreadptsComboBox.getModel().setSelectedItem("" + lan.getSpreadPtsMove());
            spreadjuiceComboBox.setSelectedItem("" + lan.getSpreadJuiceMove());
            spreadpercentageComboBox.setSelectedItem("" + lan.getSpreadPercentageMove());
            spreadaudiocheckbox.setSelected(lan.isSpreadPlayAudio());

            spreadaudioComboBox.setSelectedIndex(audiofilevec.indexOf(lan.getSpreadAudioFileName()));
            spreadpopupcheckbox.setSelected(lan.isSpreadShowPopup());
            spreadpopuplocationint = lan.getSpreadPopupLocation();
            if (spreadpopuplocationint == SwingConstants.NORTH_EAST) {
                spreadupperright.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.NORTH_WEST) {
                spreadupperleft.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.SOUTH_EAST) {
                spreadlowerright.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.SOUTH_WEST) {
                spreadlowerleft.setSelected(true);
            }
            spreadpopupsecsComboBox.setSelectedItem("" + lan.getSpreadPopupSecs());
            renotifyComboBox.setSelectedItem("" + lan.getSpreadMinsRenotify());

            spreadcheckbox.setSelected(lan.isSpreadCheck());
            spreadsecsComboBox.setSelectedItem("" + lan.getSpreadSecs());
            spreadbookiesComboBox.setSelectedItem("" + lan.getSpreadNumBookies());
            moveatallButton.setSelected(lan.isSpreadMoveAtAll());
            movelinejuiceButton.setSelected(lan.isSpreadPtsMove());
            movepercentagebutton.setSelected(lan.isSpreadPercentageMove());
            spreadptsComboBox.getModel().setSelectedItem("" + lan.getSpreadPtsMove());
            spreadjuiceComboBox.setSelectedItem("" + lan.getSpreadJuiceMove());
            spreadpercentageComboBox.setSelectedItem("" + lan.getSpreadPercentageMove());
            spreadaudiocheckbox.setSelected(lan.isSpreadPlayAudio());
            spreadaudioComboBox.setSelectedItem("" + lan.getSpreadAudioFileName());
            spreadpopupcheckbox.setSelected(lan.isSpreadShowPopup());
            spreadpopuplocationint = lan.getSpreadPopupLocation();
            if (spreadpopuplocationint == SwingConstants.NORTH_EAST) {
                spreadupperright.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.NORTH_WEST) {
                spreadupperleft.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.SOUTH_EAST) {
                spreadlowerright.setSelected(true);
            } else if (spreadpopuplocationint == SwingConstants.SOUTH_WEST) {
                spreadlowerleft.setSelected(true);
            }
            spreadpopupsecsComboBox.setSelectedItem("" + lan.getSpreadPopupSecs());
            renotifyComboBox.setSelectedItem("" + lan.getSpreadMinsRenotify());

            totalcheckbox.setSelected(lan.isTotalCheck());
            totalsecsComboBox.setSelectedItem("" + lan.getTotalSecs());
            totalbookiesComboBox.setSelectedItem("" + lan.getTotalNumBookies());
            totalmoveatallButton.setSelected(lan.isTotalMoveAtAll());
            totalmovelinejuiceButton.setSelected(lan.isTotalPtsMove());
            totalmovepercentagebutton.setSelected(lan.isTotalPercentageMove());
            totalptsComboBox.getModel().setSelectedItem("" + lan.getTotalPtsMove());
            totaljuiceComboBox.setSelectedItem("" + lan.getTotalJuiceMove());
            totalpercentageComboBox.setSelectedItem("" + lan.getTotalPercentageMove());
            totalaudiocheckbox.setSelected(lan.isTotalPlayAudio());
            totalaudioComboBox.setSelectedIndex(audiofilevec.indexOf(lan.getTotalAudioFileName()));

            totalpopupcheckbox.setSelected(lan.isTotalShowPopup());
            totalpopuplocationint = lan.getTotalPopupLocation();
            if (totalpopuplocationint == SwingConstants.NORTH_EAST) {
                totalupperright.setSelected(true);
            } else if (totalpopuplocationint == SwingConstants.NORTH_WEST) {
                totalupperleft.setSelected(true);
            } else if (totalpopuplocationint == SwingConstants.SOUTH_EAST) {
                totallowerright.setSelected(true);
            } else if (totalpopuplocationint == SwingConstants.SOUTH_WEST) {
                totallowerleft.setSelected(true);
            }
            totalpopupsecsComboBox.setSelectedItem("" + lan.getTotalPopupSecs());
            renotifytotalComboBox.setSelectedItem("" + lan.getTotalMinsRenotify());

            teamtotalcheckbox.setSelected(lan.isTeamtotalCheck());
            teamtotalsecsComboBox.setSelectedItem("" + lan.getTeamtotalSecs());
            teamtotalbookiesComboBox.setSelectedItem("" + lan.getTeamtotalNumBookies());
            teamtotalmoveatallButton.setSelected(lan.isTeamtotalMoveAtAll());
            teamtotalmovelinejuiceButton.setSelected(lan.isTeamtotalPtsMove());
            teamtotalmovepercentagebutton.setSelected(lan.isTeamtotalPercentageMove());
            teamtotalptsComboBox.getModel().setSelectedItem("" + lan.getTeamtotalPtsMove());
            teamtotaljuiceComboBox.setSelectedItem("" + lan.getTeamtotalJuiceMove());
            teamtotalpercentageComboBox.setSelectedItem("" + lan.getTeamtotalPercentageMove());
            teamtotalaudiocheckbox.setSelected(lan.isTeamtotalPlayAudio());

            teamtotalaudioComboBox.setSelectedIndex(audiofilevec.indexOf(lan.getTeamtotalAudioFileName()));

            teamtotalpopupcheckbox.setSelected(lan.isTeamtotalShowPopup());
            teamtotalpopuplocationint = lan.getTeamtotalPopupLocation();
            if (teamtotalpopuplocationint == SwingConstants.NORTH_EAST) {
                teamtotalupperright.setSelected(true);
            } else if (teamtotalpopuplocationint == SwingConstants.NORTH_WEST) {
                teamtotalupperleft.setSelected(true);
            } else if (teamtotalpopuplocationint == SwingConstants.SOUTH_EAST) {
                teamtotallowerright.setSelected(true);
            } else if (teamtotalpopuplocationint == SwingConstants.SOUTH_WEST) {
                teamtotallowerleft.setSelected(true);
            }
            teamtotalpopupsecsComboBox.setSelectedItem("" + lan.getTeamtotalPopupSecs());
            renotifyteamtotalComboBox.setSelectedItem("" + lan.getTeamtotalMinsRenotify());

            moneylinecheckbox.setSelected(lan.isMoneylineCheck());
            moneylinesecsComboBox.setSelectedItem("" + lan.getMoneylineSecs());
            moneylinebookiesComboBox.setSelectedItem("" + lan.getMoneylineNumBookies());
            moneylinemoveatallButton.setSelected(lan.isMoneylineMoveAtAll());
            moneylinemovelinejuiceButton.setSelected(lan.isMoneylineJuiceMove());
            moneylinemovepercentagebutton.setSelected(lan.isMoneylinePercentageMove());
            moneylinejuiceComboBox.setSelectedItem("" + lan.getMoneylineJuiceMove());
            moneylinepercentageComboBox.setSelectedItem("" + lan.getMoneylinePercentageMove());
            moneylineaudiocheckbox.setSelected(lan.isMoneylinePlayAudio());

            moneylineaudioComboBox.setSelectedIndex(audiofilevec.indexOf(lan.getMoneylineAudioFileName()));

            moneylinepopupcheckbox.setSelected(lan.isMoneylineShowPopup());
            moneylinepopuplocationint = lan.getMoneylinePopupLocation();
            if (moneylinepopuplocationint == SwingConstants.NORTH_EAST) {
                moneylineupperright.setSelected(true);
            } else if (moneylinepopuplocationint == SwingConstants.NORTH_WEST) {
                moneylineupperleft.setSelected(true);
            } else if (moneylinepopuplocationint == SwingConstants.SOUTH_EAST) {
                moneylinelowerright.setSelected(true);
            } else if (moneylinepopuplocationint == SwingConstants.SOUTH_WEST) {
                moneylinelowerleft.setSelected(true);
            }
            moneylinepopupsecsComboBox.setSelectedItem("" + lan.getMoneylinePopupSecs());
            renotifymoneylineComboBox.setSelectedItem("" + lan.getMoneylineMinsRenotify());

            DefaultMutableTreeNode root = new DefaultMutableTreeNode(lan.getSport());
            DefaultTreeModel treeModel = new DefaultTreeModel(root);
            _tree = new CheckBoxTree(treeModel) {
                @Override
                public Dimension getPreferredScrollableViewportSize() {
                    return new Dimension(300, 300);
                }
            };
            _tree.setRootVisible(true);
            _tree.getCheckBoxTreeSelectionModel().setDigIn(true);
            _tree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            _tree.setClickInCheckBoxOnly(false);


            _tree.setShowsRootHandles(true);
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) _tree.getActualCellRenderer();
            renderer.setLeafIcon(null);
            renderer.setOpenIcon(null);
            renderer.setClosedIcon(null);


            try {
                String allsports = "";
                List<Sport> sportsVec = AppController.getSportsVec();
                for (Sport sport : sportsVec) {
                    if (!sport.getSportname().equals(lan.getSport())) {
                        continue;
                    } else {
//                        log(i+" Found sport! "+sport.getSportname());
                    }

                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                    root.add(child);
                    TreePath path = new TreePath(((DefaultMutableTreeNode) child).getPath());

                    if (lansports.contains("" + sport.getLeague_id())) {
                        _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
                    } else {
                        _tree.getCheckBoxTreeSelectionModel().removeSelectionPath(path);
                    }

                    leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());
                    allsports = allsports + sport.getLeague_id() + ",";

                }
                if (allsports.endsWith(",")) {
                    allsports = allsports.substring(0, allsports.length() - 1);
                }
                leaguenameidhash.put("" + lan.getSport(), allsports);
                for (int zzz = 0; zzz < _tree.getRowCount(); zzz++) {
                    _tree.expandRow(zzz);
                }

            } catch (Exception ex3) {
                log(ex3);
            }


            treePanel.removeAll();
            treePanel.add(new JScrollPane(_tree));
            treePanel.revalidate();
            treePanel.repaint();


            // first lets create sportsbook checkboxtree
            DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
            DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);
            sportsbooktree = new CheckBoxTree(treeModelbookie) {
                @Override
                public Dimension getPreferredScrollableViewportSize() {
                    return new Dimension(300, 300);
                }
            };
            //sportsbooktree.setVisibleRowCount(20);
            sportsbooktree.setRootVisible(true);
            sportsbooktree.getCheckBoxTreeSelectionModel().setDigIn(true);
            sportsbooktree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            sportsbooktree.setClickInCheckBoxOnly(false);


            sportsbooktree.setShowsRootHandles(true);
            DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) sportsbooktree.getActualCellRenderer();
            renderer2.setLeafIcon(null);
            renderer2.setOpenIcon(null);
            renderer2.setClosedIcon(null);

            JScrollPane spsp = new JScrollPane(sportsbooktree);
            spsp.setViewportView(sportsbooktree);
            sportsbooktreePanel.add(spsp);


            try {

                Vector newBookiesVec = AppController.getBookiesVec();
                List<Bookie> hiddencols = AppController.getHiddenCols();
                String allbookies = "";
                for (Object o : newBookiesVec) {
                    Bookie b = (Bookie) o;

                    if (b.getBookie_id() >= 990) {
                        continue;
                    }

                    if (hiddencols.contains(b) && !lanbookies.contains("" + b.getBookie_id())) {
                        continue;
                    }
//                    DefaultMutableTreeNode tempnode;


                    DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(b.getName());
                    rootbookie.add(child2);
                    TreePath path2 = new TreePath(( child2).getPath());

                    if (lanbookies.contains("" + b.getBookie_id())) {
                        sportsbooktree.getCheckBoxTreeSelectionModel().addSelectionPath(path2);
                    } else {
                        sportsbooktree.getCheckBoxTreeSelectionModel().removeSelectionPath(path2);
                    }


                    bookienameidhash.put(b.getName(), "" + b.getBookie_id());
                    allbookies = allbookies + b.getBookie_id() + ",";
                }
                if (allbookies.endsWith(",")) {
                    allbookies = allbookies.substring(0, allbookies.length() - 1);
                }

                bookienameidhash.put("All Bookies", allbookies);

                for (int zzz = 0; zzz < sportsbooktree.getRowCount(); zzz++) {
                    sportsbooktree.expandRow(zzz);
                }

            } catch (Exception ex4) {
                log(ex4);
            }


            box2.removeAll();
            box1.removeAll();

            box1.add(welcome);
            box1.add(gameperiodComboBox);
            box1.add(treePanel);
            box1.add(sportsbooktreePanel);
            box1.add(linealertname);
            box1.add(saveBut);
            box1.add(removeBut);
            box1.revalidate();
            box1.repaint();

            box2.add(spreadPanel);
            box2.add(totalPanel);
            box2.add(moneylinePanel);
            box2.add(teamtotalPanel);


            box2.revalidate();
            box2.repaint();


        }


    }
    private int gamePeriodIndex(int z) {
        for (int i = 0; i < gameperiodintlist.length; i++) {
            if (z == gameperiodintlist[i]) {
                return i;
            }
        }
        return -1;
    }
}