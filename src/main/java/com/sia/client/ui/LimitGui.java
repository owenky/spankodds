package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.comps.PopupLocationConfig;
import com.sia.client.ui.control.SportsTabPane;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LimitGui extends AbstractLayeredDialog implements ItemListener {

    public static String[] gameperiod = new String[]{"Full Game", "1st Half", "2nd Half", "All Halfs", " ", "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter", "Live", "All Periods"};
    static int idx;
    JLabel welcome = new JLabel("LINE ALERT");
    JComboBox renotifyComboBox;
    JComboBox percentageComboBox;
    JButton testincsound = new JButton("Test Inc Sound");
    JButton testdecsound = new JButton("Test Dec Sound");
    JButton testpopup = new JButton("Test Popup");
    String sport = "";
    JCheckBox audiocheckbox;
    JCheckBox popupcheckbox;
    JCheckBox audiocheckbox2;
    JCheckBox popupcheckbox2;
    JCheckBox textcheckbox;
    JComboBox popupsecsComboBox;
    JPanel panel = new JPanel();
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JCheckBox spreadcheckbox = new JCheckBox("Spread");
    JCheckBox totalcheckbox = new JCheckBox("Total");
    JCheckBox moneylinecheckbox = new JCheckBox("Money Line");
    JCheckBox teamtotalcheckbox = new JCheckBox("Team Total");
    JCheckBox alllinescheckbox = new JCheckBox("All Lines");
    JCheckBox fullgame = new JCheckBox("Full Game ");
    JCheckBox firsthalf = new JCheckBox("1st Half");
    JCheckBox secondhalf = new JCheckBox("2nd Half ");
    JCheckBox allhafs = new JCheckBox("All Halfs");
    JCheckBox firstquarter = new JCheckBox("1st Quarter");
    JCheckBox secondquarter = new JCheckBox("2nd Quarter");
    JCheckBox thirdquarter = new JCheckBox("3rd Quarter");
    JCheckBox fourthtquarter = new JCheckBox("4th Quarter");
    JCheckBox allquarters = new JCheckBox("All Quarters");
    JCheckBox allperiods = new JCheckBox("All Periods");
    JButton set = new JButton("Save");
    JTextField linealertname = new JTextField(20);
    JButton usedefaultsound = new JButton("Use Default Sound");
    JButton usecustomsound = new JButton("Use Custom Sound");
    JLabel soundlabel = new JLabel("DEFAULT");
    JPanel treePanel = new JPanel(new BorderLayout(2, 2));
    JPanel sportsbooktreePanel = new JPanel(new BorderLayout(2, 2));
    CheckBoxTree[] trees = new CheckBoxTree[10];
    String[] sportlist = new String[10];
    String[] secslist = new String[60];
    String[] minslist = new String[20];
    String[] percentagelist = new String[16];
    String[] incaudiolist = new String[8];
    String[] decaudiolist = new String[8];
    private JComboBox sportComboBox;

    private JComboBox incaudioComboBox;
    private JComboBox decaudioComboBox;

    private JLabel linetype = new JLabel("LINE TYPE");
    private JLabel notify = new JLabel("NOTIFY");
    private JList selectedList = new JList();
    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    private int popupsecs = 5;
    private int popuplocationint = 8;
    private List<Bookie> bookeis;
    private List<Sport> sports;
    private Vector checkednodes2 = new Vector();
    private List<DefaultMutableTreeNode> checkednodes3 = new ArrayList<>();
    private CheckBoxTree _tree;
    private CheckBoxTree sportsbooktree;
    private Hashtable leaguenameidhash = new Hashtable();
    private Hashtable bookienameidhash = new Hashtable();
    private String alerttype = "";
    private final PopupLocationConfig popupLocationConfig = new PopupLocationConfig();
    private String soundfile = "";
    private String incsoundfile = "";
    private String decsoundfile = "";

    Vector<String> incaudiofilevec = new Vector<>();
    Vector<String> decaudiofilevec = new Vector<>();

    JideToggleButton upperright;
    JideToggleButton upperleft;
    JideToggleButton lowerright;
    JideToggleButton lowerleft;


    JList eventsList = new JList();
    DefaultListModel eventsModel = new DefaultListModel();

    public LimitGui(SportsTabPane stp) {
        super(stp,"Limit Change Alerts");
    }
    @Override
    protected JComponent getUserComponent() {
        linealertname.setDocument(new JTextFieldLimit(20));
        TextPrompt tp7 = new TextPrompt("Name Your Line Alert", linealertname);
        tp7.setForeground(Color.RED);
        tp7.setShow(TextPrompt.Show.FOCUS_LOST);
        tp7.changeStyle(Font.BOLD + Font.ITALIC);

        incaudiolist[0] = "Limit Change";
        incaudiolist[1] = "Limit Increase";
        incaudiolist[2] = "Fire Away";
        incaudiolist[3] = "Come Get Some";
        incaudiolist[4] = "Come Get Some XXX";
        incaudiolist[5] = "Horn";
        incaudiolist[6] = "Double Horn";
        incaudiolist[7] = "Scream";

        incaudiofilevec.add("limitchange.wav");
        incaudiofilevec.add("limitincrease.wav");
        incaudiofilevec.add("fireaway.wav");
        incaudiofilevec.add("comegetsome.wav");
        incaudiofilevec.add("comegetsomeyoulittlebitch.wav");
        incaudiofilevec.add("horn.wav");
        incaudiofilevec.add("doublehorn.wav");
        incaudiofilevec.add("scream.wav");

        decaudiolist[0] = "Limit Change";
        decaudiolist[1] = "Limit Decrease";
        decaudiolist[2] = "Getting Nervous";
        decaudiolist[3] = "Getting Nervous XXX";
        decaudiolist[4] = "What The F*ck";
        decaudiolist[5] = "Horn";
        decaudiolist[6] = "Double Horn";
        decaudiolist[7] = "Scream";

        decaudiofilevec.add("limitchange.wav");
        decaudiofilevec.add("limitdecrease.wav");
        decaudiofilevec.add("oogettingnervous.wav");
        decaudiofilevec.add("ohshitiamgettingveryverynervous.wav");
        decaudiofilevec.add("whatthefuck2.wav");
        decaudiofilevec.add("horn.wav");
        decaudiofilevec.add("doublehorn.wav");
        decaudiofilevec.add("scream.wav");

        incaudioComboBox = new JComboBox(incaudiolist);
        decaudioComboBox = new JComboBox(decaudiolist);

        for (int v = 1; v <= 60; v++) {
            secslist[v - 1] = v + "";
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

        percentagelist[0] = "0";
        percentagelist[1] = "5";
        percentagelist[2] = "10";
        percentagelist[3] = "15";
        percentagelist[4] = "20";
        percentagelist[5] = "25";
        percentagelist[6] = "30";
        percentagelist[7] = "35";
        percentagelist[8] = "40";
        percentagelist[9] = "45";
        percentagelist[10] = "50";
        percentagelist[11] = "60";
        percentagelist[12] = "70";
        percentagelist[13] = "80";
        percentagelist[14] = "90";
        percentagelist[15] = "100";


        int index=0;
        sportlist[index++] = "Please Select a Sport...";
        SportType [] predefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: predefinedSportTypes) {
            sportlist[index++] = st.getSportName();
        }
        //sportlist[9] = "Auto Racing";
        sportComboBox = new JComboBox(sportlist);
        sportComboBox.setMaximumRowCount(sportlist.length);

        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm1 = SpankyWindow.findSpankyWindow(getAnchoredLayeredPane().getSportsTabPane().getWindowIndex());
        JPanel userComponent = new JPanel();
        userComponent.setLayout(new FlowLayout());

        int FPS_MIN = 5;
        int FPS_MAX = 60;

        if (popupsecs > FPS_MAX) {
            FPS_MAX = popupsecs;
        }

        // spank new
        createSportTreeModel3();
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
        // end spank new







        JSlider popupsecsslider = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, popupsecs);

        popupsecsslider.setMajorTickSpacing(5);
        popupsecsslider.setMinorTickSpacing(1);
        popupsecsslider.setPaintTicks(true);
        popupsecsslider.setPaintLabels(true);
        popupsecsslider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
//                int value = source.getValue();
                if (!source.getValueIsAdjusting()) {
                    popupsecs = source.getValue();
                    log("secs=" + popupsecs);
                }
            }
        });
        String[] display2 = new String[4];
        display2[0] = "Top Left";
        display2[1] = "Bottom Left";
        display2[2] = "Top Right";
        display2[3] = "Bottom Right";

        upperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
        upperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
        lowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
        lowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));

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


        sportComboBox.setSelectedIndex(0);
        sportComboBox.addItemListener(this);

        audiocheckbox = new JCheckBox("Play Audio (Increases)");
        audiocheckbox2 = new JCheckBox("Play Audio (Decreases)");
        textcheckbox = new JCheckBox("Send Text ");



        popupcheckbox = new JCheckBox("Show Popup (Increases)");
        popupcheckbox2 = new JCheckBox("Show Popup (Decreases)");

        popupsecsComboBox = new JComboBox(secslist);



        renotifyComboBox = new JComboBox(minslist);
        percentageComboBox = new JComboBox(percentagelist);

        panel1.setBorder(BorderFactory.createEtchedBorder());
        panel1.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());
        panel2.setBorder(BorderFactory.createEtchedBorder());
        panel2.setLayout(new GridBagLayout());

        JLabel forlab = new JLabel("for length (secs) =");
        JLabel percentlab = new JLabel("%");
        JLabel secondslab = new JLabel("seconds");
        JLabel renotifyme = new JLabel("Renotify me on same Sport+Bookie only after");
        JLabel renotifyme2 = new JLabel(" minutes have elapsed");

        JLabel minpercentagelab = new JLabel("Only alert me of % changes that are >=");




        usedefaultsound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = LimitGui.idx;
                soundfile = "limitchange.wav";

                soundlabel.setText("DEFAULT");
            }
        });
        usecustomsound.addActionListener(ae -> {
            int i = LimitGui.idx;
            JFileChooser jfc = new JFileChooser();
            log("hai iam from filechooser");
            jfc.showOpenDialog(jfrm1);
            File f1 = jfc.getSelectedFile();
            soundfile = f1.getPath();
            //  AppController.LimitNodeList.get(i).soundfile = f1.getPath();
            //  AppController.LimitNodeList.get(i).soundlabel = f1.getPath();
            soundlabel.setText(f1.getPath());
        });


        testincsound.addActionListener(ae -> {
            try {

                new SoundPlayer(incaudiofilevec.elementAt(incaudioComboBox.getSelectedIndex()), true);
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing Inc File!");
                log(ex);
            }
        });
        testdecsound.addActionListener(ae -> {
            try {

                new SoundPlayer(decaudiofilevec.elementAt(decaudioComboBox.getSelectedIndex()), true);
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing Dec File!");
                log(ex);
            }
        });
        testpopup.addActionListener(ae -> {
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "Limit Change<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, jfrm1);
            log("popupsecs=" + popupsecs);

        });


        GridBagConstraints c = new GridBagConstraints();

        //periods  checkboxes

        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 6;
        panel2.add(fullgame, c);
        c.gridx = 4;
        c.gridwidth = 1;


        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 3;
        panel2.add(firsthalf, c);

        c.gridx = 4;
        c.gridwidth = 1;
        //panel2.add(allhafs, c);
        panel2.add(secondhalf, c);

        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 3;
        panel2.add(firstquarter, c);

        c.gridx = 4;
        c.gridwidth = 3;
        panel2.add(secondquarter, c);


        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(thirdquarter, c);

        c.gridx = 4;
        c.gridwidth = 2;
        panel2.add(fourthtquarter, c);
        // panel2.add(allperiods, c);

        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 2;
        //panel2.add(allquarters, c);
        c.gridx = 4;
        c.gridwidth = 2;
        //blank
        //spread total moneyline and teamtotal checkboxes
        c.gridy = 12;
        c.gridx = 0;
        c.gridwidth = 1;
        panel1.add(spreadcheckbox, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel1.add(totalcheckbox, c);
        c.gridy = 14;
        c.gridx = 0;
        c.gridwidth = 2;
        panel1.add(moneylinecheckbox, c);
        c.gridx = 4;
        c.gridwidth = 2;
        panel1.add(teamtotalcheckbox, c);
        c.gridy = 16;
        c.gridx = 2;
        c.gridwidth = 2;
        //panel1.add(alllinescheckbox, c);

        //notify
        c.gridy = 20;

        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(audiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 2;

        // add increase audio choices here
        panel.add(incaudioComboBox, c);
        c.gridx = 4;
        c.gridwidth = 1;
        //panel.add(usecustomsound, c);
        //c.gridx = 8;
        //c.gridwidth = 4;
        panel.add(testincsound, c);


        c.gridy = 21;
        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(audiocheckbox2, c);
        c.gridx = 2;
        c.gridwidth = 2;

        // add increase audio choices here
        panel.add(decaudioComboBox, c);
        c.gridx = 4;
        c.gridwidth = 1;
        //panel.add(usecustomsound, c);
        //c.gridx = 8;
        //c.gridwidth = 4;
        panel.add(testdecsound, c);



       // c.gridx = 3;
       // c.gridwidth = 2;
       // panel.add(soundlabel, c);

        c.gridy = 24;

        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(popupcheckbox, c);



        // panel.add(popupLocationConfig.getUserComponent(), c);
        c.gridheight = 2;
        c.gridx = 2;
        c.gridwidth = 1;
        panel.add(forlab, c);
        c.gridx =3;
        c.gridwidth = 1;
        panel.add(popupsecsComboBox, c);

       // c.gridx = 4;
        //c.gridwidth = 1;
       //panel.add(secondslab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        c.gridheight = 2;
        panel.add(radioPanel, c);
       // c.gridx = 7;
       // c.gridwidth = 1;
       // panel.add(testpopup, c);
        c.gridheight = 1;
        c.gridy = 25;
        c.gridx = 0;
        c.gridwidth=2;
        c.gridheight=1;
        panel.add(popupcheckbox2, c);
        // c.gridx = 2;
        // c.gridwidth = 1;
        //  panel.add(testpopup, c);


        c.gridy = 26;

        c.gridx = 0;
        c.gridwidth = 3;
        panel.add(renotifyme, c);
        c.gridx = 3;
        c.gridwidth = 1;
        panel.add(renotifyComboBox, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel.add(renotifyme2, c);

        c.gridy = 27;
        c.gridx = 0;
        c.gridwidth=3;
       // c.anchor=GridBagConstraints.BASELINE_TRAILING;
        panel.add(minpercentagelab, c);
      //  c.anchor=GridBagConstraints.BASELINE_LEADING;
        c.gridx = 3;
        c.gridwidth=1;
        panel.add(percentageComboBox, c);
        c.anchor=GridBagConstraints.BELOW_BASELINE_LEADING;
        c.gridx = 4;
        c.gridwidth=1;
        panel.add(percentlab, c);





        selectedList.revalidate();
        sportsbooktreePanel.revalidate();

        //************************end of panel*******************

        //set Buttong actions
        set.addActionListener(ae -> {
            try {
                String sport = (String) sportComboBox.getSelectedItem();

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

                log("lan=" + sport);
                log("sportselected=" + sportselected);
                log("bookieselected=" + bookieselected);






                AppController.LimitNodeList.get(idx).setSport(sport);
                AppController.LimitNodeList.get(idx).checkedLeagueNodes = sportselectedvec;
                AppController.LimitNodeList.get(idx).checkedBookieNodes = bookieselectedvec;
                AppController.LimitNodeList.get(idx).sportcodes = new ArrayList<String>(Arrays.asList(sportselected.split(",")));;
                AppController.LimitNodeList.get(idx).bookiecodes = new ArrayList<String>(Arrays.asList(bookieselected.split(",")));;
                //com.sia.client.ui.AppController.LimitNodeList.get(idx).renotifyvalue=(String)renotifyComboBox.getSelectedItem();
                //com.sia.client.ui.AppController.LimitNodeList.get(idx).showpopvalue=(String)popupsecsComboBox.getSelectedItem();
                //com.sia.client.ui.AppController.LimitNodeList.get(idx).audiovalue=(String)audioComboBox.getSelectedItem();
                String popsec = (String) popupsecsComboBox.getSelectedItem();
                AppController.LimitNodeList.get(idx).popupsec = Integer.parseInt(popsec);
                AppController.LimitNodeList.get(idx).popuplocationint = popuplocationint;

                String minpercentageS = (String) percentageComboBox.getSelectedItem();
                AppController.LimitNodeList.get(idx).minpercentagechange = Integer.parseInt(minpercentageS);






                AppController.LimitNodeList.get(idx).renotifyvalue = Double.parseDouble((String) renotifyComboBox.getSelectedItem());
                AppController.LimitNodeList.get(idx).showpopvalue = Integer.parseInt((String) popupsecsComboBox.getSelectedItem());
                AppController.LimitNodeList.get(idx).isIncShowpopChecks = popupcheckbox.isSelected();
                AppController.LimitNodeList.get(idx).isDecShowpopChecks = popupcheckbox2.isSelected();
                AppController.LimitNodeList.get(idx).isIncAudioChecks = audiocheckbox.isSelected();
                AppController.LimitNodeList.get(idx).isDecAudioChecks = audiocheckbox2.isSelected();
                AppController.LimitNodeList.get(idx).minpercentagechange = Integer.parseInt((String)percentageComboBox.getSelectedItem());

                AppController.LimitNodeList.get(idx).is1stHafCheck = firsthalf.isSelected();
                AppController.LimitNodeList.get(idx).is2ndHalfCheck = secondhalf.isSelected();
                //AppController.LimitNodeList.get(idx).isAllHalfsCheck = true;
                AppController.LimitNodeList.get(idx).is1stQutCheck = firstquarter.isSelected();
                AppController.LimitNodeList.get(idx).is2ndQutCheck = secondquarter.isSelected();
                AppController.LimitNodeList.get(idx).is3rdQutCheck = thirdquarter.isSelected();
                AppController.LimitNodeList.get(idx).is4thQutCheck = fourthtquarter.isSelected();
                // AppController.LimitNodeList.get(idx).isAllQutCheck = true;
                AppController.LimitNodeList.get(idx).isFullGameCheck = fullgame.isSelected();
                // AppController.LimitNodeList.get(idx).isAllPerCheck = true;

                AppController.LimitNodeList.get(idx).isSpreadCheck = spreadcheckbox.isSelected();
                AppController.LimitNodeList.get(idx).isTotalCheck = totalcheckbox.isSelected();
                AppController.LimitNodeList.get(idx).isMoneyCheck = moneylinecheckbox.isSelected();
                AppController.LimitNodeList.get(idx).isTeamTotalCheck = teamtotalcheckbox.isSelected();

                AppController.LimitNodeList.get(idx).incsoundfile = incaudiofilevec.elementAt(incaudioComboBox.getSelectedIndex());
                AppController.LimitNodeList.get(idx).decsoundfile = decaudiofilevec.elementAt(decaudioComboBox.getSelectedIndex());
                // System.out.println("popuplocationint="+popuplocationint);
                LimitAlertManager.reloadprefs();

            } catch (Exception e) {
                checkednodes2.clear();
                checkednodes3.clear();

            }
            close();
        });

        // Create three vertical boxes.


        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));


        box2.add(new JLabel("Choose Sport"));
        box2.add(sportComboBox);
//	box1.add(new JLabel("Edit Existing Line Alert"));
        //box1.add(lanComboBox);


        selectedList.revalidate();

        selectedList.setEnabled(false);

        // Add the boxes to the content pane.
        userComponent.add(box2);
        userComponent.add(box1);

        for (int i = 0; i < AppController.LimitNodeList.size(); i++) {
            //  System.out.println(i+"="+(LimitNode)AppController.LimitNodeList.get(i) );
        }

        return userComponent;
//*******end of cons***********
    }

    public static void playSound(String file) {
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



    public static String getSportName(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String sportname = "";
        for (Sport sp : Sports) {
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                sportname = sp.getSportname();
                break;

            }
        }
        return sportname;

    }

    public static String getLeagueName(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String leaguename = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leaguename = sp.getLeaguename();
                break;

            }
        }

        return leaguename;
    }

    public static String getLeagueAbbr(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String leagueabbr = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leagueabbr = sp.getLeagueabbr();
                break;

            }
        }

        return leagueabbr;
    }





    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        if (e.getSource() == sportComboBox) {

            if (sportComboBox.getSelectedIndex() != 0) {
                int i;
                String sportname = (String) sportComboBox.getSelectedItem();
                for (i = 0; i < AppController.LimitNodeList.size(); i++) {
                    if (AppController.LimitNodeList.get(i).SportName.equalsIgnoreCase(sportname)) {
                        LimitGui.idx = i;
                        break;
                    }
                }
                LimitNode lan = (LimitNode)AppController.LimitNodeList.get(idx);
                sport = sportlist[sportComboBox.getSelectedIndex()];



                //------------------------------
                // owen new code

                //log("SportCodes For "+idx+"..="+AppController.LimitNodeList.get(idx).sportcodes);
                // log("SportCodes For "+idx+"..="+AppController.LimitNodeList.get(idx).sportcodes);

                List<String> lansports = AppController.LimitNodeList.get(idx).sportcodes;
                List<String> lanbookies = AppController.LimitNodeList.get(idx).bookiecodes;
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
                    List<Bookie> newBookiesVec = AppController.getBookiesVec();
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




                // end owen new code


                //set line checkes
                if (AppController.LimitNodeList.get(i).isSpreadCheck) {
                    spreadcheckbox.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isTotalCheck) {
                    totalcheckbox.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isMoneyCheck) {
                    moneylinecheckbox.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isTeamTotalCheck) {
                    teamtotalcheckbox.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isAllLinesCheck) {
                    alllinescheckbox.setSelected(true);
                }

                //set period cheks
                if (AppController.LimitNodeList.get(i).isFullGameCheck) {
                    fullgame.setSelected(true);
                }

                if (AppController.LimitNodeList.get(i).is1stHafCheck) {
                    firsthalf.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).is2ndHalfCheck) {
                    secondhalf.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isAllHalfsCheck) {
                    allhafs.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).is1stQutCheck) {
                    firstquarter.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).is2ndQutCheck) {
                    secondquarter.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).is3rdQutCheck) {
                    thirdquarter.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).is4thQutCheck) {
                    fourthtquarter.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isAllHalfsCheck) {
                    allhafs.setSelected(true);
                }
                if (AppController.LimitNodeList.get(i).isAllPerCheck) {
                    allperiods.setSelected(true);
                }
                //checks and set audio and popup  checks
                if (AppController.LimitNodeList.get(i).isIncAudioChecks) {
                    audiocheckbox.setSelected(true);

                }
                if (AppController.LimitNodeList.get(i).isDecAudioChecks) {
                    audiocheckbox2.setSelected(true);

                }
                if (AppController.LimitNodeList.get(i).isTextChecks) {
                    textcheckbox.setSelected(true);

                }
                incsoundfile = AppController.LimitNodeList.get(i).incsoundfile;
                decsoundfile = AppController.LimitNodeList.get(i).decsoundfile;
                incaudioComboBox.setSelectedIndex(incaudiofilevec.indexOf(incsoundfile));
                decaudioComboBox.setSelectedIndex(decaudiofilevec.indexOf(decsoundfile));


                if(soundfile.equals("limitchange.wav")) {
                    soundlabel.setText("DEFAULT");
                }
                else
                {
                    soundlabel.setText(soundfile);
                }
                if (AppController.LimitNodeList.get(i).isIncShowpopChecks) {
                    popupcheckbox.setSelected(true);


                }
                if (AppController.LimitNodeList.get(i).isDecShowpopChecks) {
                    popupcheckbox2.setSelected(true);


                }

                popupsecsComboBox.setSelectedItem("" + AppController.LimitNodeList.get(i).popupsec);
                percentageComboBox.setSelectedItem("" + AppController.LimitNodeList.get(i).minpercentagechange);

                renotifyComboBox.setSelectedItem("" + AppController.LimitNodeList.get(i).renotifyvalue);

                popuplocationint = lan.popuplocationint;
                if (popuplocationint == SwingConstants.NORTH_EAST) {
                    upperright.setSelected(true);
                } else if (popuplocationint == SwingConstants.NORTH_WEST) {
                    upperleft.setSelected(true);
                } else if (popuplocationint == SwingConstants.SOUTH_EAST) {
                    lowerright.setSelected(true);
                } else if (popuplocationint == SwingConstants.SOUTH_WEST) {
                    lowerleft.setSelected(true);
                }



                box2.removeAll();
                box1.removeAll();

                //   box1.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));


                //treePanel.add(_tree);

                //box1.add(gameperiodComboBox);
                box1.add(treePanel);
                box1.add(sportsbooktreePanel);
                //box1.add(linealertname);
                //box1.add(saveBut);
                box1.revalidate();
                box1.repaint();

                //box2.add(spreadcheckbox);
                //linetype.setBounds(0,0,0,10);
                box2.setAlignmentY(Component.TOP_ALIGNMENT);
                box2.add(welcome);
                box2.add(panel2);
                box2.add(new JLabel("  "));
                box2.add(linetype);
                box2.add(panel1);
                //	box2.add(totalcheckbox);
                //box2.add(moneylinecheckbox);
                //box2.add(teamtotalcheckbox);
                //notify.setBounds(20,10,10,10);
                box2.add(new JLabel("  "));
                box2.add(notify);
                box2.add(panel);
                box2.add(new JLabel("  "));
                box2.add(set);

				/*	//box2.add(totalcheckbox);
					box2.add(totalPanel);
					//box2.add(moneylinecheckbox);
					box2.add(moneylinePanel);
					//box2.add(teamtotalcheckbox);
					box2.add(teamtotalPanel);*/


                box2.revalidate();
                box2.repaint();


            }

        }


    }

    public TreeModel createSportTreeModel2(String tabname) {
        DefaultMutableTreeNode sportnode = new DefaultMutableTreeNode(tabname);
        DefaultTreeModel treeModel = new DefaultTreeModel(sportnode);

        try {

            List<Sport> sportsVec = AppController.getSportsVec();
            Vector checkedsports = AppController.LimitNodeList.get(idx).checkedLeagueNodes;


            for (Sport value : sportsVec) {
                Sport sport;
                DefaultMutableTreeNode tempnode;
                sport = value;
                if (sport.getSportname().equals(tabname)) {
                    tempnode = sportnode;
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                    log("sports name   " + sport.getSportname());
                    tempnode.add(child);
                    leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());

                    for (Object checkedsport : checkedsports) {
                        if ((checkedsport + "").equalsIgnoreCase("" + sport.getLeaguename()) || (checkedsport + "").equalsIgnoreCase(tabname)) {
                            log("Checked Sports araay  " + checkedsport);
                            checkednodes2.add(child);
                        }

                    }
                }

            }

            return treeModel;
        } catch (Exception e) {
            log(e);
        }
        return null;

    }



    public void createSportTreeModel3() {

        // first lets create sportsbook checkboxtree
        DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);


        try {

            List<Bookie> newBookiesVec = AppController.getBookiesVec();
            List<Bookie> hiddencols = AppController.getHiddenCols();
            String allbookies = "";
            for (Bookie b : newBookiesVec) {
                if (hiddencols.contains(b) || b.getBookie_id() >= 990) {
                    continue;
                }


//                DefaultMutableTreeNode tempnode;


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




//*******end of class***********
}

