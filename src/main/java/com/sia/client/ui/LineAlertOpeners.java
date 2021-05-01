package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Games;
import com.sia.client.model.Sport;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

class LineAlertOpeners implements ItemListener {

    public static String[] gameperiod = new String[]{"Full Game", "1st Half", "2nd Half", "All Halfs", " ", "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter", "Live", "All Periods"};
    static int idx;
    static JFrame jfrm;
    JLabel welcome = new JLabel("LINE ALERT");
    JideToggleButton upperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
    JideToggleButton upperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
    JideToggleButton lowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
    JideToggleButton lowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));
    JComboBox renotifyComboBox;
    JButton testsound = new JButton("Test Sound");
    JButton testpopup = new JButton("Test Popup");
    String sport = "";
    JCheckBox audiocheckbox;
    JCheckBox popupcheckbox;
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
    String[] audiolist = new String[8];
    JComboBox sportComboBox;

    JLabel linetype = new JLabel("LINE TYPE");
    JLabel notify = new JLabel("NOTIFY");
    JList selectedList = new JList();
    Box box1 = Box.createVerticalBox();
    Box box2 = Box.createVerticalBox();
    Box box3 = Box.createVerticalBox();
    int popupsecs = 5;
    int popuplocationint = 0;
    Vector bookeis;
    Vector sports;
    Games games;
    private Vector checkednodes2 = new Vector();
    private Vector checkednodes3 = new Vector();
    private CheckBoxTree _tree;
    private CheckBoxTree sportsbooktree;
    private Hashtable leaguenameidhash = new Hashtable();
    private Hashtable bookienameidhash = new Hashtable();
    private String alerttype = "";


    //******************************************************************************
    LineAlertOpeners() {
        linealertname.setDocument(new JTextFieldLimit(20));
        TextPrompt tp7 = new TextPrompt("Name Your Line Alert", linealertname);
        tp7.setForeground(Color.RED);
        tp7.setShow(TextPrompt.Show.FOCUS_LOST);
        tp7.changeStyle(Font.BOLD + Font.ITALIC);

        audiolist[0] = "select sound";
        audiolist[1] = "openers";
        audiolist[2] = "Custom Sounds";
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


        sportlist[0] = "Please Select a Sport...";
        sportlist[1] = "Football";
        sportlist[2] = "Basketball";
        sportlist[3] = "Baseball";
        sportlist[4] = "Hockey";
        sportlist[5] = SiaConst.SoccerStr;
        sportlist[6] = "Fighting";
        sportlist[7] = "Golf";
        sportlist[8] = "Tennis";
        sportlist[9] = "Auto Racing";
        sportComboBox = new JComboBox(sportlist);
        sportComboBox.setMaximumRowCount(sportlist.length);

        //lanComboBox = new JComboBox(com.sia.client.ui.AppController.getLineAlertNodes());

        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        jfrm = new JFrame("Openers Line Alerts");

        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new FlowLayout());

        // Give the frame an initial size.
        jfrm.setSize(1100, 850);

        // Terminate the program when the user closes the application.
        //jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //checkboxtree
        // final TreeModel treeModel = createSportTreeModel();

       /* createSportTreeModel();
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH),"", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
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
        eventsPanel.add(new JScrollPane(eventsList));		*/

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
        popupsecsslider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int value = source.getValue();
                if (!source.getValueIsAdjusting()) {
                    popupsecs = (int) source.getValue();
                    System.out.println("secs=" + popupsecs);
                }
            }
        });
        String[] display2 = new String[4];
        display2[0] = "Top Left";
        display2[1] = "Bottom Left";
        display2[2] = "Top Right";
        display2[3] = "Bottom Right";
        JComboBox popuplocation = new JComboBox(display2);
        AppController.LineOpenerAlertNodeList.get(LineAlertOpeners.idx).popuplocationint = popuplocationint;
        upperright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.NORTH_EAST;
                    AppController.LineOpenerAlertNodeList.get(i).isUpperRight = true;
                    AppController.LineOpenerAlertNodeList.get(i).popuplocationint = popuplocationint;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isUpperRight = false;
                }
            }
        });
        upperleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.NORTH_WEST;
                    AppController.LineOpenerAlertNodeList.get(i).isUpperLeft = true;
                    AppController.LineOpenerAlertNodeList.get(i).popuplocationint = popuplocationint;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isUpperLeft = true;
                }
            }
        });
        lowerright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.SOUTH_EAST;
                    AppController.LineOpenerAlertNodeList.get(i).isLowerRight = true;
                    AppController.LineOpenerAlertNodeList.get(i).popuplocationint = popuplocationint;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isLowerRight = false;
                }
            }
        });
        lowerleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.SOUTH_WEST;
                    AppController.LineOpenerAlertNodeList.get(i).isLowerLeft = true;
                    AppController.LineOpenerAlertNodeList.get(i).popuplocationint = popuplocationint;

                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isLowerLeft = true;
                }
            }
        });


        ButtonGroup group = new ButtonGroup();
        group.add(upperright);
        group.add(upperleft);
        group.add(lowerright);
        group.add(lowerleft);

        JPanel radioPanel = new JPanel(new GridLayout(2, 2, 0, 0));

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
        //lanComboBox.setSelectedIndex(0);
        //lanComboBox.addItemListener(this);

        audiocheckbox = new JCheckBox("Play Audio");
        audiocheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
                }
            }
        });


        popupcheckbox = new JCheckBox("Show Popup");
        popupcheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
                }
            }
        });
        popupsecsComboBox = new JComboBox(secslist);
        popupsecsComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                AppController.LineOpenerAlertNodeList.get(i).showpopvalue = Integer.parseInt((String) popupsecsComboBox.getSelectedItem());
            }
        });


        renotifyComboBox = new JComboBox(minslist);
        renotifyComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                AppController.LineOpenerAlertNodeList.get(i).renotifyvalue = Double.parseDouble((String) renotifyComboBox.getSelectedItem());


            }
        });

        panel1.setBorder(BorderFactory.createEtchedBorder());
        panel1.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setLayout(new GridBagLayout());
        panel2.setBorder(BorderFactory.createEtchedBorder());
        panel2.setLayout(new GridBagLayout());

        JLabel forlab = new JLabel("for ");
        JLabel secondslab = new JLabel("seconds");
        JLabel renotifyme = new JLabel("Renotify me on same Sport only after");
        JLabel renotifyme2 = new JLabel(" minutes have elapsed");


        spreadcheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (spreadcheckbox.isSelected()) {
                    if (totalcheckbox.isSelected() && moneylinecheckbox.isSelected() && teamtotalcheckbox.isSelected()) {
                        alllinescheckbox.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck = false;
                    alllinescheckbox.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                }
            }
        });

        totalcheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (totalcheckbox.isSelected()) {
                    if (spreadcheckbox.isSelected() && moneylinecheckbox.isSelected() && teamtotalcheckbox.isSelected()) {
                        alllinescheckbox.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = true;
                    }


                    AppController.LineOpenerAlertNodeList.get(i).isTotalCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isTotalCheck = false;
                    alllinescheckbox.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                }
            }
        });

        moneylinecheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (moneylinecheckbox.isSelected()) {
                    if (spreadcheckbox.isSelected() && totalcheckbox.isSelected() && teamtotalcheckbox.isSelected()) {
                        alllinescheckbox.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = true;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck = false;
                    alllinescheckbox.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                }
            }
        });

        teamtotalcheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (teamtotalcheckbox.isSelected()) {
                    if (spreadcheckbox.isSelected() && totalcheckbox.isSelected() && moneylinecheckbox.isSelected()) {
                        alllinescheckbox.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = true;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck = false;
                    alllinescheckbox.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                }
            }
        });

        alllinescheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (alllinescheckbox.isSelected()) {
                    spreadcheckbox.setSelected(true);
                    totalcheckbox.setSelected(true);
                    moneylinecheckbox.setSelected(true);
                    teamtotalcheckbox.setSelected(true);
                    AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isTotalCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = true;

                } else {
                    spreadcheckbox.setSelected(false);
                    totalcheckbox.setSelected(false);
                    moneylinecheckbox.setSelected(false);
                    teamtotalcheckbox.setSelected(false);
                    alllinescheckbox.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isTotalCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck = false;
                }
            }

        });


        fullgame.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (firstquarter.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected() && firsthalf.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;


                    }


                    AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(0);

                } else {
                    AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck = false;
                    allperiods.setSelected(false);

                    Integer I = 0;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });

        firstquarter.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected() && firsthalf.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;

                    }
                    if (secondquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected()) {
                        allquarters.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(5);
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = false;
                    allperiods.setSelected(false);
                    allquarters.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;
                    Integer I = 5;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });

        secondquarter.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && firstquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected() && firsthalf.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;

                    }
                    if (firstquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected()) {
                        allquarters.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                    }
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(6);

                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = false;
                    allperiods.setSelected(false);
                    allquarters.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;
                    Integer I = 6;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });


        thirdquarter.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && firstquarter.isSelected() && secondquarter.isSelected() && fourthtquarter.isSelected() && firsthalf.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;

                    }
                    if (firstquarter.isSelected() && secondquarter.isSelected() && fourthtquarter.isSelected()) {
                        allquarters.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                    }


                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(7);
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = false;
                    allperiods.setSelected(false);
                    allquarters.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;
                    Integer I = 7;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });

        fourthtquarter.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && firstquarter.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected() && firsthalf.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;

                    }
                    if (firstquarter.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected()) {
                        allquarters.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(8);
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = false;
                    Integer I = 8;
                    allperiods.setSelected(false);
                    allquarters.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });


        firsthalf.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && firstquarter.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected() && secondhalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;


                    }
                    if (secondhalf.isSelected()) {
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                    }

                    AppController.LineOpenerAlertNodeList.get(i).periods.add(1);
                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = false;
                    allperiods.setSelected(false);
                    allhafs.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = false;

                    Integer I = 1;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });


        secondhalf.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int i = LineAlertOpeners.idx;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (fullgame.isSelected() && firstquarter.isSelected() && secondquarter.isSelected() && thirdquarter.isSelected() && fourthtquarter.isSelected() && firsthalf.isSelected()) {
                        allperiods.setSelected(true);
                        allquarters.setSelected(true);
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                        AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;

                    }
                    if (firsthalf.isSelected()) {
                        allhafs.setSelected(true);
                        AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                    }
                    AppController.LineOpenerAlertNodeList.get(i).periods.add(2);

                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = true;
                } else {
                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = true;
                    allperiods.setSelected(false);
                    allhafs.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                    Integer I = 2;
                    AppController.LineOpenerAlertNodeList.get(i).periods.remove(I);
                }
            }
        });

        allhafs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (allhafs.isSelected()) {
                    firsthalf.setSelected(true);
                    secondhalf.setSelected(true);
                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;


                } else {
                    firsthalf.setSelected(false);
                    secondhalf.setSelected(false);
                    allperiods.setSelected(false);


                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = false;
                }
            }
        });
        allquarters.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (allquarters.isSelected()) {
                    firstquarter.setSelected(true);
                    secondquarter.setSelected(true);
                    thirdquarter.setSelected(true);
                    fourthtquarter.setSelected(true);
                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                } else {
                    firstquarter.setSelected(false);
                    secondquarter.setSelected(false);
                    thirdquarter.setSelected(false);
                    fourthtquarter.setSelected(false);
                    allperiods.setSelected(false);
                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;

                }
            }
        });
        allperiods.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int i = LineAlertOpeners.idx;
                if (allperiods.isSelected()) {
                    fullgame.setSelected(true);
                    firsthalf.setSelected(true);
                    secondhalf.setSelected(true);
                    allhafs.setSelected(true);
                    allquarters.setSelected(true);
                    firstquarter.setSelected(true);
                    secondquarter.setSelected(true);
                    thirdquarter.setSelected(true);
                    fourthtquarter.setSelected(true);
                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck = true;
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = true;
                } else {
                    fullgame.setSelected(false);
                    firsthalf.setSelected(false);
                    secondhalf.setSelected(false);

                    allhafs.setSelected(false);
                    allquarters.setSelected(false);
                    firstquarter.setSelected(false);

                    secondquarter.setSelected(false);
                    System.out.println("iam after 2nd");
                    thirdquarter.setSelected(false);
                    System.out.println("iam after 3rd");
                    fourthtquarter.setSelected(false);
                    System.out.println("iam after 4rt");
                    AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllQutCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck = false;
                    AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck = false;

                }
            }
        });

        usedefaultsound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = LineAlertOpeners.idx;
                AppController.LineOpenerAlertNodeList.get(i).soundfile = "openers.wav";
                AppController.LineOpenerAlertNodeList.get(i).soundlabel = "DEFAULT";
                soundlabel.setText("DEFAULT");
            }
        });
        usecustomsound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = LineAlertOpeners.idx;
                JFileChooser jfc = new JFileChooser();
                System.out.println("hai iam from filechooser");
                jfc.showOpenDialog(jfrm);
                File f1 = jfc.getSelectedFile();

                AppController.LineOpenerAlertNodeList.get(i).soundfile = f1.getPath();
                AppController.LineOpenerAlertNodeList.get(i).soundlabel = f1.getPath();
                soundlabel.setText(f1.getPath());
            }
        });


        testsound.addActionListener(ae -> {
            try {
                playSound("openers.wav");
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing File!");
            }
        });

        testpopup.addActionListener(ae -> {
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, jfrm);
            log("popupsecs=" + popupsecs);

        });


        GridBagConstraints c = new GridBagConstraints();

        //periods  checkboxes

        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(fullgame, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel2.add(firsthalf, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        panel2.add(secondhalf, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel2.add(allhafs, c);

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

        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 2;
        panel2.add(allquarters, c);
        c.gridx = 4;
        c.gridwidth = 2;
        panel2.add(allperiods, c);
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
        panel1.add(alllinescheckbox, c);

        //notify
        c.gridy = 20;

        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(audiocheckbox, c);
        c.gridx = 2;
        c.gridwidth = 2;
        panel.add(usedefaultsound, c);
        c.gridx = 4;
        c.gridwidth = 2;
        panel.add(usecustomsound, c);
        c.gridx = 8;
        c.gridwidth = 4;
        panel.add(testsound, c);
        c.gridy = 21;

        c.gridx = 3;
        c.gridwidth = 2;
        panel.add(soundlabel, c);

        c.gridy = 24;

        c.gridx = 0;
        c.gridwidth = 2;
        panel.add(popupcheckbox, c);
        c.gridx = 2;
        c.gridwidth = 1;
        panel.add(radioPanel, c);
        c.gridx = 3;
        c.gridwidth = 1;
        panel.add(forlab, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel.add(popupsecsComboBox, c);

        c.gridx = 5;
        c.gridwidth = 1;
        panel.add(secondslab, c);
        c.gridx = 8;
        c.gridwidth = 4;
        panel.add(testpopup, c);

        c.gridy = 26;

        c.gridx = 0;
        c.gridwidth = 4;
        panel.add(renotifyme, c);
        c.gridx = 4;
        c.gridwidth = 1;
        panel.add(renotifyComboBox, c);
        c.gridx = 5;
        c.gridwidth = 3;
        panel.add(renotifyme2, c);


        selectedList.revalidate();
        sportsbooktreePanel.revalidate();

        //************************end of panel*******************

        //set Buttong actions
        set.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String sport = (String) sportComboBox.getSelectedItem();

                    int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                    String selectedleagues[] = new String[treeRows.length];

                    //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).leagues=treeRows;
                    System.out.println("********treerows length*****=" + treeRows.length);
                    if (treeRows != null) {
                        AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes.clear();
                        java.util.Arrays.sort(treeRows);
                        for (int i = 0; i < treeRows.length; i++) {
                            TreePath path = _tree.getPathForRow(treeRows[i]);
                            //System.out.println("treerows="+treeRows[0]);
                            selectedleagues[i] = "" + path.getLastPathComponent();
                            DefaultMutableTreeNode child = new DefaultMutableTreeNode(selectedleagues[i]);
                            //tempnode.add(child);
                            //	leaguenameidhash.put(sport.getLeaguename(),""+sport.getLeague_id());

                            AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes.add(child);


                        }
                    }
                    System.out.println("********kkk length*****=" + treeRows.length);
                    if (treeRows.length != 0) {
                        if (selectedleagues[0].equalsIgnoreCase(sport)) {
                            AppController.LineOpenerAlertNodeList.get(idx).isAllLeaguesSelected = true;
                        } else {
                            AppController.LineOpenerAlertNodeList.get(idx).isAllLeaguesSelected = false;
                        }
                    }
                    System.out.println("***********selectedleagues Arrays are prepared********");
                    //System.out.println("treerows="+treeRows[0]);
                    int[] treeRows1 = sportsbooktree.getCheckBoxTreeSelectionModel().getSelectionRows();
                    String selectedbookies[] = new String[treeRows1.length];
                    System.out.println("********abc length*****=" + treeRows.length);
                    //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).bookies=treeRows1;
                    //System.out.println("treerows="+treeRows[0]);
                    if (treeRows1 != null) {
                        AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes.clear();
                        java.util.Arrays.sort(treeRows1);
                        for (int i = 0; i < treeRows1.length; i++) {
                            TreePath path = sportsbooktree.getPathForRow(treeRows1[i]);
                            System.out.println("treerows=" + treeRows1[0]);
                            selectedbookies[i] = "" + path.getLastPathComponent();
                            DefaultMutableTreeNode child = new DefaultMutableTreeNode(selectedbookies[i]);
                            //tempnode.add(child);
                            //	leaguenameidhash.put(sport.getLeaguename(),""+sport.getLeague_id());

                            AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes.add(child);


                        }
                    }
                    System.out.println("********xyz length*****=" + treeRows.length);
                    if (treeRows1.length != 0) {
                        if (selectedbookies[0].equalsIgnoreCase("All Bookies")) {
                            AppController.LineOpenerAlertNodeList.get(idx).isAllBookiesSelected = true;
                        } else {
                            AppController.LineOpenerAlertNodeList.get(idx).isAllBookiesSelected = false;
                        }
                    }
                    System.out.println("***********selectedbookies Arrays are prepared********");
                    int game_id = 0;
                    bookeis = AppController.getBookiesVec();
                    games = AppController.getGames();
                    sports = AppController.getSportsVec();
                    int sport_id = 0;
                    ArrayList selectedbookieids = new ArrayList();
                    if (treeRows != null) {

                        for (int i = 0; i < selectedbookies.length; i++) {
                            for (int j = 0; j < bookeis.size(); j++) {
                                Bookie bk = (Bookie) bookeis.get(j);
                                String bookiename = bk.getName();
                                if (bookiename.equalsIgnoreCase(selectedbookies[i])) {
                                    selectedbookieids.add(bk.getBookie_id());
                                }
                            }

                        }
                    }
                    System.out.println("***********selectedbookieids added ********");
                    for (int i = 0; i < sports.size(); i++) {
                        Sport s = (Sport) sports.get(i);
                        if (s.sportname.equalsIgnoreCase(sport)) {
                            sport_id = s.getSport_id();
                            break;
                        }

                    }
                    System.out.println("***********sport id added ********");

                    ArrayList selectedleagueids = new ArrayList();
                    if (treeRows1 != null) {
                        for (int j = 0; j < selectedleagues.length; j++) {
                            for (int i = 0; i < sports.size(); i++) {
                                Sport s = (Sport) sports.get(i);
                                String leaguename = s.getLeaguename();
                                int sid = s.getSport_id();
                                if (selectedleagues[j].equalsIgnoreCase(leaguename) && sport_id == sid) {
                                    selectedleagueids.add(s.getLeague_id());
                                }

                            }

                        }
                    }
                    System.out.println("***********selectedleagueids added ********");
                    System.out.println("selectedleagueidssize=" + selectedleagueids.size());
                    AppController.LineOpenerAlertNodeList.get(idx).sports_id = sport_id;
                    AppController.LineOpenerAlertNodeList.get(idx).leagues = selectedleagueids;
                    AppController.LineOpenerAlertNodeList.get(idx).bookies = selectedbookieids;
                    //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).renotifyvalue=(String)renotifyComboBox.getSelectedItem();
                    //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).showpopvalue=(String)popupsecsComboBox.getSelectedItem();
                    //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).audiovalue=(String)audioComboBox.getSelectedItem();
                    String popsec = (String) popupsecsComboBox.getSelectedItem();
                    AppController.LineOpenerAlertNodeList.get(idx).popupsec = Integer.parseInt(popsec);

                    System.out.println("*****************BOOOOOOOOOOOOKIES************************");
                    for (int i = 0; i < selectedbookieids.size(); i++) {
                        System.out.println("selectedbookieids=" + selectedbookieids.get(i) + "  selectedbookies=" + selectedbookies[i]);
                    }

                    System.out.println("*****************LEAGuEEEEEEEEEEEEEEEEEEEEEE************************");
                    for (int i = 0; i < selectedleagueids.size(); i++) {
                        System.out.println("selectedleagueids=" + selectedleagueids.get(i) + "  selectedleagues=" + selectedleagues[i]);
                    }
                    System.out.println("*****************Selected Periods************************");
                    ArrayList per = AppController.LineOpenerAlertNodeList.get(idx).periods;
                    for (int i = 0; i < per.size(); i++) {
                        System.out.println("per=" + per.get(i));
                    }
                    System.out.println("*****************Selected Periods************************");
                } catch (Exception e) {
                    checkednodes2.clear();
                    checkednodes3.clear();

                }
                jfrm.dispose();


            }
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
//	 selectedPanel.revalidate();

        selectedList.setEnabled(false);

        // Add the boxes to the content pane.
        jfrm.getContentPane().add(box2);
        jfrm.getContentPane().add(box1);

        //jfrm.getContentPane().add(box3);

        // Display the frame.
        jfrm.setVisible(true);


//*******end of cons***********
    }

    public static void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            JOptionPane.showMessageDialog(null, "Error Playing File! Check file path. Only AIFF,AU and WAV are supported!");
        }
    }

    public static void spreadOpenerAlert(int Gid, int bid, int per, String opener, double newvisitorspread, double newvisitorjuice, double newhomespread, double newhomejuice) {

        String visitorValue = LineAlertManager.shortenSpread(newvisitorspread, newvisitorjuice);
        String homeValue = LineAlertManager.shortenSpread(newhomespread, newhomejuice);


        Game game = AppController.getGame(Gid);
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid + "");
        String bookiename = bk.getName();
        int lid = game.getLeague_id();
        String sportname = getSportName(lid);
        String leaguename = getLeagueName(lid);

        if (opener.equals("1")) {
            for (int i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
                Instant start = AppController.LineOpenerAlertNodeList.get(i).start;
                if (start == null) {
                    start = Instant.now();
                }
                Instant end = Instant.now();
                Duration timeElapsed = Duration.between(start, end);
                String s = "" + timeElapsed.toMillis();
                double d = Double.parseDouble(s);
                double timeDiff = d / 1000 / 60;
                double selectedwaitTime = AppController.LineOpenerAlertNodeList.get(i).renotifyvalue;
                if (timeDiff >= selectedwaitTime) {
                    ArrayList periods = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).periods;
                    ArrayList bookeis = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).bookies;
                    ArrayList leagues = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String audiovalue = (String) AppController.LineOpenerAlertNodeList.get(i).audiovalue;
                    int popupsec = (int) AppController.LineOpenerAlertNodeList.get(i).popupsec;
                    String sn = (String) AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean isspreadcheck = (boolean) AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck;
                    boolean isAllBookiesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllBookiesSelected;
                    boolean isAllLeaguesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllLeaguesSelected;

                    int popupsecs = AppController.LineOpenerAlertNodeList.get(i).showpopvalue;
                    int popuplocationint = AppController.LineOpenerAlertNodeList.get(i).popuplocationint;

                    int sport_id = (int) AppController.LineOpenerAlertNodeList.get(i).sports_id;

                    String soundfile = (String) AppController.LineOpenerAlertNodeList.get(i).soundfile;
			/*Sport sp=com.sia.client.ui.AppController.getSport(sport_id+"");
			String leaguename=sp.getLeagueabbr();*/
                    if (sportname.equalsIgnoreCase(sn)) {
                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && isspreadcheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

                                String popalertname = "Alert at:" + hrmin + "Spread Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.alertsVector.addElement(popalertname);

                                //System.out.println(com.sia.client.ui.AppController.alertsVector.size());

                                new UrgentMessage("<HTML><H1>Openers " + sn + "</H1><FONT COLOR=BLUE>" +
                                        leaguename + "<BR><TABLE cellspacing=5 cellpadding=5>" +
                                        "<TR><TD COLSPAN=4>" + gameperiod[per] + "</TD><TD></TD><TD>Spread Line</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + bookiename + "</TD><TD></TD><TD></TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + visitotteamgamenumber + "</TD><TD>" + visitorTeam + "</TD><TD>" + visitorValue + "</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + hometeamgamenumber + "</TD><TD>" + homeTeam + "</TD><TD>" + homeValue + "</TD></TR>" +
                                        "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, AppController.getMainTabPane());

                            }
                            AppController.LineOpenerAlertNodeList.get(i).start = Instant.now();
                        }
                    }

                }
            }
        }

    }

    public static String getSportName(int lid) {
        Vector Sports = AppController.getSportsVec();
        String sportname = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = (Sport) Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                sportname = sp.getSportname();
                break;

            }
        }
        return sportname;

    }

    public static String getLeagueName(int lid) {
        Vector Sports = AppController.getSportsVec();
        String leaguename = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = (Sport) Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leaguename = sp.getLeaguename();
                break;

            }
        }

        return leaguename;
    }

    public static String getLeagueAbbr(int lid) {
        Vector Sports = AppController.getSportsVec();
        String leagueabbr = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = (Sport) Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leagueabbr = sp.getLeagueabbr();
                break;

            }
        }

        return leagueabbr;
    }

    public static void totalOpenerAlert(int Gid, int bid, int per, String opener, double newover, double newoverjuice, double newunder, double newunderjuice) {

        String overValue = LineAlertManager.shortenTotal(newover, newoverjuice);
        String underValue = LineAlertManager.shortenTotal(newunder, newunderjuice);


        Game game = AppController.getGame(Gid);
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid + "");
        String bookiename = bk.getName();
        int lid = game.league_id;
        String sportname = getSportName(lid);
        String leaguename = getLeagueName(lid);
        if (opener.equals("1")) {
            for (int i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
                Instant start = AppController.LineOpenerAlertNodeList.get(i).start;
                if (start == null) {
                    start = Instant.now();
                }
                Instant end = Instant.now();
                Duration timeElapsed = Duration.between(start, end);
                String s = "" + timeElapsed.toMillis();
                double d = Double.parseDouble(s);
                double timeDiff = d / 1000 / 60;
                double selectedwaitTime = AppController.LineOpenerAlertNodeList.get(i).renotifyvalue;
                if (timeDiff >= selectedwaitTime) {

                    ArrayList periods = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).periods;
                    ArrayList bookeis = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).bookies;
                    ArrayList leagues = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String audiovalue = (String) AppController.LineOpenerAlertNodeList.get(i).audiovalue;
                    int popupsec = (int) AppController.LineOpenerAlertNodeList.get(i).popupsec;
                    String sn = (String) AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean istotalcheck = (boolean) AppController.LineOpenerAlertNodeList.get(i).isTotalCheck;
                    boolean isAllBookiesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllBookiesSelected;
                    boolean isAllLeaguesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllLeaguesSelected;

                    int popupsecs = AppController.LineOpenerAlertNodeList.get(i).showpopvalue;
                    int popuplocationint = AppController.LineOpenerAlertNodeList.get(i).popuplocationint;

                    int sport_id = (int) AppController.LineOpenerAlertNodeList.get(i).sports_id;
                    String soundfile = (String) AppController.LineOpenerAlertNodeList.get(i).soundfile;

                    if (sportname.equalsIgnoreCase(sn)) {

                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && istotalcheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

                                String popalertname = "Alert at:" + hrmin + "Total Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.alertsVector.addElement(popalertname);


                                new UrgentMessage("<HTML><H1>Openers " + sn + "</H1><FONT COLOR=BLUE>" +
                                        leaguename + "<BR><TABLE cellspacing=5 cellpadding=5>" +
                                        "<TR><TD COLSPAN=4>" + gameperiod[per] + "</TD><TD></TD><TD>Total Line</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + bookiename + "</TD><TD></TD><TD></TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + visitotteamgamenumber + "</TD><TD>" + visitorTeam + "</TD><TD>" + overValue + "</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + hometeamgamenumber + "</TD><TD>" + homeTeam + "</TD><TD>" + underValue + "</TD></TR>" +
                                        "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, AppController.getMainTabPane());

                            }
                            AppController.LineOpenerAlertNodeList.get(i).start = Instant.now();
                        }
                    }

                }
            }
        }

    }

    public static void moneyOpenerAlert(int Gid, int bid, int per, String opener, double newvisitorjuice, double newhomejuice) {

        String visitorValue = LineAlertManager.mlfix(newvisitorjuice);
        String homeValue = LineAlertManager.mlfix(newhomejuice);

        Game game = AppController.getGame(Gid);
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid + "");
        String bookiename = bk.getName();
        int lid = game.league_id;

        String sportname = getSportName(lid);
        String leaguename = getLeagueName(lid);
        if (opener.equals("1")) {
            for (int i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
                Instant start = AppController.LineOpenerAlertNodeList.get(i).start;
                if (start == null) {
                    start = Instant.now();
                }
                Instant end = Instant.now();
                Duration timeElapsed = Duration.between(start, end);
                String s = "" + timeElapsed.toMillis();
                double d = Double.parseDouble(s);
                double timeDiff = d / 1000 / 60;
                double selectedwaitTime = AppController.LineOpenerAlertNodeList.get(i).renotifyvalue;
                if (timeDiff >= selectedwaitTime) {

                    ArrayList periods = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).periods;
                    ArrayList bookeis = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).bookies;
                    ArrayList leagues = (ArrayList) AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = (boolean) AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String audiovalue = (String) AppController.LineOpenerAlertNodeList.get(i).audiovalue;
                    int popupsec = (int) AppController.LineOpenerAlertNodeList.get(i).popupsec;
                    String sn = (String) AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean ismoneycheck = (boolean) AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck;
                    boolean isAllBookiesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllBookiesSelected;
                    boolean isAllLeaguesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllLeaguesSelected;

                    int popupsecs = AppController.LineOpenerAlertNodeList.get(i).showpopvalue;
                    int popuplocationint = AppController.LineOpenerAlertNodeList.get(i).popuplocationint;

                    int sport_id = (int) AppController.LineOpenerAlertNodeList.get(i).sports_id;
                    String soundfile = (String) AppController.LineOpenerAlertNodeList.get(i).soundfile;
                    if (sportname.equalsIgnoreCase(sn)) {
                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && ismoneycheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

                                String popalertname = "Alert at:" + hrmin + "Money Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.alertsVector.addElement(popalertname);

                                new UrgentMessage("<HTML><H1>Openers " + sn + "</H1><FONT COLOR=BLUE>" +
                                        leaguename + "<BR><TABLE cellspacing=5 cellpadding=5>" +
                                        "<TR><TD COLSPAN=4>" + gameperiod[per] + "</TD><TD></TD><TD>Money Line</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + bookiename + "</TD><TD></TD><TD></TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + visitotteamgamenumber + "</TD><TD>" + visitorTeam + "</TD><TD>" + visitorValue + "</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + hometeamgamenumber + "</TD><TD>" + homeTeam + "</TD><TD>" + homeValue + "</TD></TR>" +
                                        "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, AppController.getMainTabPane());

                            }
                            AppController.LineOpenerAlertNodeList.get(i).start = Instant.now();
                        }
                    }

                }
            }
        }

    }

    public static void teamTotalOpenerAlert(int Gid, int bid, int per, String opener, double newvisitorover, double newvisitoroverjuice, double newvisitorunder, double newvisitorunderjuice) {

        String overValue = LineAlertManager.shortenTotal(newvisitorover, newvisitoroverjuice);
        String underValue = LineAlertManager.shortenTotal(newvisitorunder, newvisitorunderjuice);

        Game game = AppController.getGame(Gid);
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid + "");
        String bookiename = bk.getName();
        int lid = game.league_id;

        String sportname = getSportName(lid);
        String leaguename = getLeagueName(lid);
        if (opener.equals("1")) {

            for (int i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
                Instant start = AppController.LineOpenerAlertNodeList.get(i).start;
                if (start == null) {
                    start = Instant.now();
                }
                Instant end = Instant.now();
                Duration timeElapsed = Duration.between(start, end);
                String s = "" + timeElapsed.toMillis();
                double d = Double.parseDouble(s);
                double timeDiff = d / 1000 / 60;
                double selectedwaitTime = AppController.LineOpenerAlertNodeList.get(i).renotifyvalue;
                if (timeDiff >= selectedwaitTime) {
                    List periods = AppController.LineOpenerAlertNodeList.get(i).periods;
                    List bookeis = AppController.LineOpenerAlertNodeList.get(i).bookies;
                    List leagues = AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String sn = AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean isteamtotalcheck = AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck;
                    boolean isAllBookiesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllBookiesSelected;
                    boolean isAllLeaguesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllLeaguesSelected;

                    int popupsecs = AppController.LineOpenerAlertNodeList.get(i).showpopvalue;
                    int popuplocationint = AppController.LineOpenerAlertNodeList.get(i).popuplocationint;

                    String soundfile = AppController.LineOpenerAlertNodeList.get(i).soundfile;
                    if (sportname.equalsIgnoreCase(sn)) {

                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && isteamtotalcheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

                                String popalertname = "Alert at:" + hrmin + "TeamTotal Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.alertsVector.addElement(popalertname);

                                new UrgentMessage("<HTML><H1>Openers " + sn + "</H1><FONT COLOR=BLUE>" +
                                        leaguename + "<BR><TABLE cellspacing=5 cellpadding=5>" +
                                        "<TR><TD COLSPAN=4>" + gameperiod[per] + "</TD><TD></TD><TD>TeamTotal Line</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + bookiename + "</TD><TD></TD><TD></TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + visitotteamgamenumber + "</TD><TD>" + visitorTeam + "</TD><TD>" + overValue + "</TD></TR>" +
                                        "<TR><TD COLSPAN=4>" + hometeamgamenumber + "</TD><TD>" + homeTeam + "</TD><TD>" + underValue + "</TD></TR>" +
                                        "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, AppController.getMainTabPane());

                            }
                            AppController.LineOpenerAlertNodeList.get(i).start = Instant.now();
                        }
                    }

                }
            }
        }

    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        if (e.getSource() == sportComboBox) {

            if (sportComboBox.getSelectedIndex() != 0) {
                int i;
                String sportname = (String) sportComboBox.getSelectedItem();
                for (i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
                    if (AppController.LineOpenerAlertNodeList.get(i).SportName.equalsIgnoreCase(sportname)) {
                        LineAlertOpeners.idx = i;
                        break;
                    }
                }

                sport = sportlist[sportComboBox.getSelectedIndex()];
                //------------------------------


                //--------------------------------------
                //_tree = trees[sportComboBox.getSelectedIndex()];
                final TreeModel treeModel = createSportTreeModel2(sport);

                JPanel treePanel = new JPanel(new BorderLayout(2, 2));
                treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), alerttype + " Alerts", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                        BorderFactory.createEmptyBorder(6, 0, 0, 0)));
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
                TreeUtils.expandAll(_tree, true);

                treePanel.add(new JScrollPane(_tree));


                final TreeModel bookietreeModel = createBookieTreeModel2();

                JPanel sportsbooktreePanel = new JPanel(new BorderLayout(2, 2));
                sportsbooktreePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), alerttype + " Alerts", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                        BorderFactory.createEmptyBorder(6, 0, 0, 0)));
                sportsbooktree = new CheckBoxTree(bookietreeModel) {
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
                DefaultTreeCellRenderer renderer1 = (DefaultTreeCellRenderer) sportsbooktree.getActualCellRenderer();
                renderer1.setLeafIcon(null);
                renderer1.setOpenIcon(null);
                renderer1.setClosedIcon(null);
                TreeUtils.expandAll(sportsbooktree, true);

                sportsbooktreePanel.add(new JScrollPane(sportsbooktree));


                //set line checkes
                if (AppController.LineOpenerAlertNodeList.get(i).isSpreadCheck) {
                    spreadcheckbox.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isTotalCheck) {
                    totalcheckbox.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck) {
                    moneylinecheckbox.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isTeamTotalCheck) {
                    teamtotalcheckbox.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isAllLinesCheck) {
                    alllinescheckbox.setSelected(true);
                }

                //set period cheks
                if (AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck) {
                    fullgame.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isFullGameCheck) {
                    fullgame.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is1stHafCheck) {
                    firsthalf.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is2ndHalfCheck) {
                    secondhalf.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck) {
                    allhafs.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is1stQutCheck) {
                    firstquarter.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is2ndQutCheck) {
                    secondquarter.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is3rdQutCheck) {
                    thirdquarter.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).is4thQutCheck) {
                    fourthtquarter.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isAllHalfsCheck) {
                    allhafs.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isAllPerCheck) {
                    allperiods.setSelected(true);
                }
                //checks and set audio and popup  checks
                if (AppController.LineOpenerAlertNodeList.get(i).isAudioChecks) {
                    audiocheckbox.setSelected(true);

                }
                soundlabel.setText("" + AppController.LineOpenerAlertNodeList.get(i).soundlabel);
                if (AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks) {
                    popupcheckbox.setSelected(true);
                    popupsecsComboBox.setSelectedItem("" + AppController.LineOpenerAlertNodeList.get(i).showpopvalue);

                }
                if (AppController.LineOpenerAlertNodeList.get(i).isUpperLeft) {
                    upperleft.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isUpperRight) {
                    upperright.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isLowerLeft) {
                    lowerleft.setSelected(true);
                }
                if (AppController.LineOpenerAlertNodeList.get(i).isLowerRight) {
                    lowerright.setSelected(true);
                }
                renotifyComboBox.setSelectedItem("" + AppController.LineOpenerAlertNodeList.get(i).renotifyvalue);

					/*if(com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).leagues!=null)
					{
						int treeRows[]=new int[com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).leagues.size()];
						for(int i1=0;i1<treeRows.length;i1++)
						{
							treeRows[i1]=(int)com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).leagues.get(i1);
						}
						java.util.Arrays.sort(treeRows);
						for(int i1=0;i1<treeRows.length;i1++)
						{
							 _tree.getPathForRow(treeRows[i1]).setBatchMode(true);

						}
					}
					if(com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).bookies!=null)
					{
						int treeRows1[]=new int[com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).bookies.size()];
						for(int i1=0;i1<treeRows1.length;i1++)
						{
							treeRows1[i1]=(int)com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(i).bookies.get(i1);
						}
						java.util.Arrays.sort(treeRows1);
						for(int i2=0;i2<treeRows1.length;i2++)
						{
							 sportsbooktree.getPathForRow(treeRows1[i2]).setBatchMode(true);

						}

					}
				/*	if(sportComboBox.getSelectedIndex() == 5)
					{
						DefaultComboBoxModel model = new DefaultComboBoxModel( soccerptslist );
						spreadptsComboBox.setModel( model );
						DefaultComboBoxModel model2 = new DefaultComboBoxModel( soccerptslist );
						totalptsComboBox.setModel( model2 );
						DefaultComboBoxModel model3 = new DefaultComboBoxModel( soccerptslist );
						teamtotalptsComboBox.setModel( model3 );
					}
					*/


                Vector checkedLeagueNodes = (Vector) AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes;
                System.out.println("gjhhsizeeeee" + checkednodes2.size());
                for (int j = 0; j < checkednodes2.size(); j++) {
                    //  ArrayList<TreePath> arrayList;
                    //System.out.println("its==========================="+checkedLeagueNodes.get(j));
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes2.elementAt(j);
                    //System.out.println("its"+node);

                    TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
                    System.out.println("its===========================" + checkednodes2.get(j) + "psth" + path);
                    _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
                }
                selectedList.revalidate();
                treePanel.revalidate();
                selectedList.setEnabled(false);

                Vector checkedBookieNodes = (Vector) AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes;
                for (int j = 0; j < checkednodes3.size(); j++) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes3.elementAt(j);
                    //System.out.println("its"+node);
                    TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
                    System.out.println("its===========================" + checkednodes3.get(j) + "psth" + path);
                    sportsbooktree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
                }
                selectedList.revalidate();
                sportsbooktreePanel.revalidate();
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

            Vector sportsVec = AppController.getSportsVec();
            Vector checkedsports = (Vector) AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes;


            for (int i = 0; i < sportsVec.size(); i++) {
                Sport sport;
                DefaultMutableTreeNode tempnode;
                sport = (Sport) sportsVec.elementAt(i);
                if (sport.getSportname().equals(tabname)) {
                    tempnode = sportnode;
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                    System.out.println("sports name   " + sport.getSportname());
                    tempnode.add(child);
                    leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());

                    for (int j = 0; j < checkedsports.size(); j++) {
                        if ((checkedsports.get(j) + "").equalsIgnoreCase("" + sport.getLeaguename()) || (checkedsports.get(j) + "").equalsIgnoreCase(tabname)) {
                            System.out.println("Checked Sports araay  " + checkedsports.get(j));
                            checkednodes2.add(child);
                        }

                    }
                }

            }

            return treeModel;
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;

    }

    public TreeModel createBookieTreeModel2() {
        DefaultMutableTreeNode bookienode = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModel = new DefaultTreeModel(bookienode);

        try {

            Vector bookieVec = AppController.getBookiesVec();
            Vector checkedbookies = (Vector) AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes;
            Vector hiddenBookies = AppController.getHiddenCols();

            for (int i = 0; i < bookieVec.size(); i++) {
                Bookie bookie;
                DefaultMutableTreeNode tempnode;
                bookie = (Bookie) bookieVec.elementAt(i);
                if (hiddenBookies.contains(bookie) || bookie.getBookie_id() >= 990) {
                    continue;
                }
                if (!(bookie.getName().equalsIgnoreCase("Team") || bookie.getName().equalsIgnoreCase("Gm#") || bookie.getName().equalsIgnoreCase("Time") || bookie.getName().equalsIgnoreCase("Details") || bookie.getName().equalsIgnoreCase("Chart"))) {
                    tempnode = bookienode;
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(bookie.getName());
                    //System.out.println("sports name   "+bookie.getSportname());
                    tempnode.add(child);
                    bookienameidhash.put(bookie.getName(), "" + bookie.getBookie_id());

                    for (int j = 0; j < checkedbookies.size(); j++) {
                        if ((checkedbookies.get(j) + "").equalsIgnoreCase("" + bookie.getName()) || (checkedbookies.get(j) + "").equalsIgnoreCase("All Bookies")) {
                            System.out.println("Checked Sports araay  " + checkedbookies.get(j));
                            checkednodes3.add(child);
                        }

                    }
                }


            }

            return treeModel;
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    public void createSportTreeModel() {

        // first lets create sportsbook checkboxtree
        DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);


        try {

            Vector newBookiesVec = AppController.getBookiesVec();
            Vector hiddencols = AppController.getHiddenCols();
            String allbookies = "";
            for (int k = 0; k < newBookiesVec.size(); k++) {
                Bookie b = (Bookie) newBookiesVec.get(k);

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
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }


        for (int z = 0; z < sportlist.length; z++) {
            String thissport = sportlist[z];

            DefaultMutableTreeNode root = new DefaultMutableTreeNode(thissport);
            DefaultTreeModel treeModel = new DefaultTreeModel(root);


            try {
                String allsports = "";
                Vector sportsVec = AppController.getSportsVec();
                for (int i = 0; i < sportsVec.size(); i++) {
                    DefaultMutableTreeNode tempnode;
                    Sport sport = (Sport) sportsVec.elementAt(i);
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
                    //treePanel.add(new JScrollPane(thistree));
                    //treePanel.add(new JScrollPane());
                    treePanel.add(new JLabel(""));
                }

            } catch (Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }

        }

    }

//*******end of class***********
}

