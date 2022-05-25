package com.sia.client.ui;

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.tree.TreeUtils;
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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineAlertOpeners extends AbstractLayeredDialog implements ItemListener {

    public static String[] gameperiod = new String[]{"Full Game", "1st Half", "2nd Half", "All Halfs", " ", "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter", "Live", "All Periods"};
    static int idx;
    JLabel welcome = new JLabel("LINE ALERT");
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
    private JComboBox sportComboBox;

    private JLabel linetype = new JLabel("LINE TYPE");
    private JLabel notify = new JLabel("NOTIFY");
    private JList selectedList = new JList();
    private Box box1 = Box.createVerticalBox();
    private Box box2 = Box.createVerticalBox();
    private Box box3 = Box.createVerticalBox();
    private int popupsecs = 5;
    private int popuplocationint = 0;
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

    public LineAlertOpeners(SportsTabPane stp) {
        super(stp,"Openers Line Alerts");
    }
    @Override
    protected JComponent getUserComponent() {
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

        int index=0;
        sportlist[index++] = "Please Select a Sport...";
        SportType [] predefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: predefinedSportTypes) {
            sportlist[index++] = st.getSportName();
        }
        sportlist[9] = "Auto Racing";
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
        AppController.LineOpenerAlertNodeList.get(LineAlertOpeners.idx).popuplocationint = popuplocationint;
        PopupLocationConfig.PopupLocationListener popupLocationListener = (popUpLocation)-> {
            int i = LineAlertOpeners.idx;
            AppController.LineOpenerAlertNodeList.get(i).isUpperRight = false;
            AppController.LineOpenerAlertNodeList.get(i).isUpperLeft = false;
            AppController.LineOpenerAlertNodeList.get(i).isLowerRight = false;
            AppController.LineOpenerAlertNodeList.get(i).isLowerLeft = false;
            popuplocationint = popUpLocation.getLocation();
            AppController.LineOpenerAlertNodeList.get(i).popuplocationint = popuplocationint;
            if ( popUpLocation == LineSeekerAlertMethodAttr.PopupLocation.TOP_RIGHT) {
                AppController.LineOpenerAlertNodeList.get(i).isUpperRight = true;
            } else if ( popUpLocation == LineSeekerAlertMethodAttr.PopupLocation.TOP_LEFT) {
                AppController.LineOpenerAlertNodeList.get(i).isUpperLeft = true;
            } else if ( popUpLocation == LineSeekerAlertMethodAttr.PopupLocation.BOTTOM_LEFT) {
                AppController.LineOpenerAlertNodeList.get(i).isLowerLeft = true;
            } else if ( popUpLocation == LineSeekerAlertMethodAttr.PopupLocation.BOTTOM_RIGHT) {
                AppController.LineOpenerAlertNodeList.get(i).isLowerRight = true;
            }

        };
        popupLocationConfig.setPopupLocationListener(popupLocationListener);
        popupLocationConfig.setSelectedPopupLocation(popuplocationint);

        sportComboBox.setSelectedIndex(0);
        sportComboBox.addItemListener(this);

        audiocheckbox = new JCheckBox("Play Audio");
        audiocheckbox.addItemListener(e -> {
            int i = LineAlertOpeners.idx;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
            } else {
                AppController.LineOpenerAlertNodeList.get(i).isAudioChecks = true;
            }
        });


        popupcheckbox = new JCheckBox("Show Popup");
        popupcheckbox.addItemListener(e -> {
            int i = LineAlertOpeners.idx;
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
            } else {
                AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks = true;
            }
        });
        popupsecsComboBox = new JComboBox(secslist);
        popupsecsComboBox.addItemListener(e -> {
            int i = LineAlertOpeners.idx;
            AppController.LineOpenerAlertNodeList.get(i).showpopvalue = Integer.parseInt((String) popupsecsComboBox.getSelectedItem());
        });


        renotifyComboBox = new JComboBox(minslist);
        renotifyComboBox.addItemListener(e -> {
            int i = LineAlertOpeners.idx;
            AppController.LineOpenerAlertNodeList.get(i).renotifyvalue = Double.parseDouble((String) renotifyComboBox.getSelectedItem());


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


        spreadcheckbox.addActionListener(e -> {
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
        });

        totalcheckbox.addActionListener(e -> {
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
        });

        moneylinecheckbox.addActionListener(e -> {
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
        });

        teamtotalcheckbox.addActionListener(e -> {
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
        });

        alllinescheckbox.addActionListener(e -> {
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
        });


        fullgame.addItemListener(e -> {
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
        });

        firstquarter.addItemListener(e -> {
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
        });

        secondquarter.addItemListener(e -> {
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
        });


        thirdquarter.addItemListener(e -> {
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
        });

        fourthtquarter.addItemListener(e -> {
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
        });


        firsthalf.addItemListener(e -> {
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
        });


        secondhalf.addItemListener(e -> {
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
        });

        allhafs.addActionListener(e -> {
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
        });
        allquarters.addActionListener(e -> {
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
        });
        allperiods.addActionListener(e -> {
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
                log("iam after 2nd");
                thirdquarter.setSelected(false);
                log("iam after 3rd");
                fourthtquarter.setSelected(false);
                log("iam after 4rt");
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
        });

        usedefaultsound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int i = LineAlertOpeners.idx;
                AppController.LineOpenerAlertNodeList.get(i).soundfile = "openers.wav";
                AppController.LineOpenerAlertNodeList.get(i).soundlabel = "DEFAULT";
                soundlabel.setText("DEFAULT");
            }
        });
        usecustomsound.addActionListener(ae -> {
            int i = LineAlertOpeners.idx;
            JFileChooser jfc = new JFileChooser();
            log("hai iam from filechooser");
            jfc.showOpenDialog(jfrm1);
            File f1 = jfc.getSelectedFile();

            AppController.LineOpenerAlertNodeList.get(i).soundfile = f1.getPath();
            AppController.LineOpenerAlertNodeList.get(i).soundlabel = f1.getPath();
            soundlabel.setText(f1.getPath());
        });


        testsound.addActionListener(ae -> {
            try {
                playSound("openers.wav");
            } catch (Exception ex) {
                showMessageDialog(null, "Error Playing File!");
                log(ex);
            }
        });

        testpopup.addActionListener(ae -> {
            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, jfrm1);
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
        panel.add(popupLocationConfig, c);
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
        set.addActionListener(ae -> {
            try {
                String sport = (String) sportComboBox.getSelectedItem();

                int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                String selectedleagues[] = new String[treeRows.length];

                //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).leagues=treeRows;
                log("********treerows length*****=" + treeRows.length);
                if (treeRows != null) {
                    AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes.clear();
                    java.util.Arrays.sort(treeRows);
                    for (int i = 0; i < treeRows.length; i++) {
                        TreePath path = _tree.getPathForRow(treeRows[i]);
                        //log("treerows="+treeRows[0]);
                        selectedleagues[i] = "" + path.getLastPathComponent();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(selectedleagues[i]);
                        //tempnode.add(child);
                        //	leaguenameidhash.put(sport.getLeaguename(),""+sport.getLeague_id());

                        AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes.add(child);


                    }
                }
                log("********kkk length*****=" + treeRows.length);
                if (treeRows.length != 0) {
                    if (selectedleagues[0].equalsIgnoreCase(sport)) {
                        AppController.LineOpenerAlertNodeList.get(idx).isAllLeaguesSelected = true;
                    } else {
                        AppController.LineOpenerAlertNodeList.get(idx).isAllLeaguesSelected = false;
                    }
                }
                log("***********selectedleagues Arrays are prepared********");
                //log("treerows="+treeRows[0]);
                int[] treeRows1 = sportsbooktree.getCheckBoxTreeSelectionModel().getSelectionRows();
                String selectedbookies[] = new String[treeRows1.length];
                log("********abc length*****=" + treeRows.length);
                //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).bookies=treeRows1;
                //log("treerows="+treeRows[0]);
                if (treeRows1 != null) {
                    AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes.clear();
                    java.util.Arrays.sort(treeRows1);
                    for (int i = 0; i < treeRows1.length; i++) {
                        TreePath path = sportsbooktree.getPathForRow(treeRows1[i]);
                        log("treerows=" + treeRows1[0]);
                        selectedbookies[i] = "" + path.getLastPathComponent();
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(selectedbookies[i]);
                        //tempnode.add(child);
                        //	leaguenameidhash.put(sport.getLeaguename(),""+sport.getLeague_id());

                        AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes.add(child);


                    }
                }
                log("********xyz length*****=" + treeRows.length);
                if (treeRows1.length != 0) {
                    if (selectedbookies[0].equalsIgnoreCase("All Bookies")) {
                        AppController.LineOpenerAlertNodeList.get(idx).isAllBookiesSelected = true;
                    } else {
                        AppController.LineOpenerAlertNodeList.get(idx).isAllBookiesSelected = false;
                    }
                }
                log("***********selectedbookies Arrays are prepared********");
                bookeis = AppController.getBookiesVec();
                sports = AppController.getSportsVec();
                int sport_id = 0;
                ArrayList selectedbookieids = new ArrayList();
                if (treeRows != null) {

                    for (final String selectedbooky : selectedbookies) {
                        for (Object bookei : bookeis) {
                            Bookie bk = (Bookie) bookei;
                            String bookiename = bk.getName();
                            if (bookiename.equalsIgnoreCase(selectedbooky)) {
                                selectedbookieids.add(bk.getBookie_id());
                            }
                        }

                    }
                }
                log("***********selectedbookieids added ********");
                for (Sport s : sports) {
                    if (s.getSportname().equalsIgnoreCase(sport)) {
                        sport_id = s.getSport_id();
                        break;
                    }

                }
                log("***********sport id added ********");

                List<Integer> selectedleagueids = new ArrayList<>();
                if (treeRows1 != null) {
                    for (final String selectedleague : selectedleagues) {
                        for (Sport s : sports) {
                            String leaguename = s.getLeaguename();
                            int sid = s.getSport_id();
                            if (selectedleague.equalsIgnoreCase(leaguename) && sport_id == sid) {
                                selectedleagueids.add(s.getLeague_id());
                            }

                        }

                    }
                }
                log("***********selectedleagueids added ********");
                log("selectedleagueidssize=" + selectedleagueids.size());
                AppController.LineOpenerAlertNodeList.get(idx).sports_id = sport_id;
                AppController.LineOpenerAlertNodeList.get(idx).leagues = selectedleagueids;
                AppController.LineOpenerAlertNodeList.get(idx).bookies = selectedbookieids;
                //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).renotifyvalue=(String)renotifyComboBox.getSelectedItem();
                //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).showpopvalue=(String)popupsecsComboBox.getSelectedItem();
                //com.sia.client.ui.AppController.LineOpenerAlertNodeList.get(idx).audiovalue=(String)audioComboBox.getSelectedItem();
                String popsec = (String) popupsecsComboBox.getSelectedItem();
                AppController.LineOpenerAlertNodeList.get(idx).popupsec = Integer.parseInt(popsec);

                log("*****************BOOOOOOOOOOOOKIES************************");
                for (int i = 0; i < selectedbookieids.size(); i++) {
                    log("selectedbookieids=" + selectedbookieids.get(i) + "  selectedbookies=" + selectedbookies[i]);
                }

                log("*****************LEAGuEEEEEEEEEEEEEEEEEEEEEE************************");
                for (int i = 0; i < selectedleagueids.size(); i++) {
                    log("selectedleagueids=" + selectedleagueids.get(i) + "  selectedleagues=" + selectedleagues[i]);
                }
                log("*****************Selected Periods************************");
                List<?> per = AppController.LineOpenerAlertNodeList.get(idx).periods;
                for (Object o : per) {
                    log("per=" + o);
                }
                log("*****************Selected Periods************************");
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

    public static void spreadOpenerAlert(int Gid, int bid, int per, String opener, double newvisitorspread, double newvisitorjuice, double newhomespread, double newhomejuice) {

        String visitorValue = LineAlertManager.shortenSpread(newvisitorspread, newvisitorjuice);
        String homeValue = LineAlertManager.shortenSpread(newhomespread, newhomejuice);


        Game game = AppController.getGame(Gid);
        if ( null == game) {
            return;
        }
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid);
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

//                    int sport_id = (int) AppController.LineOpenerAlertNodeList.get(i).sports_id;

                    String soundfile = AppController.LineOpenerAlertNodeList.get(i).soundfile;
                    if (sportname.equalsIgnoreCase(sn)) {
                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && isspreadcheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

//                                String popalertname = "Alert at:" + hrmin + "Spread Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
//                                AppController.alertsVector.addElement(popalertname);
                                String mesg = "Spread Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.addAlert(hrmin,mesg);

                                //log(com.sia.client.ui.AppController.alertsVector.size());

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

    public static void totalOpenerAlert(int Gid, int bid, int per, String opener, double newover, double newoverjuice, double newunder, double newunderjuice) {

        String overValue = LineAlertManager.shortenTotal(newover, newoverjuice);
        String underValue = LineAlertManager.shortenTotal(newunder, newunderjuice);


        Game game = AppController.getGame(Gid);
        if ( null == game) {
            log(new Exception("Can find game for game id:"+Gid));
            return;
        }
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid);
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

                    List periods = AppController.LineOpenerAlertNodeList.get(i).periods;
                    List bookeis = AppController.LineOpenerAlertNodeList.get(i).bookies;
                    List leagues = AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String audiovalue = AppController.LineOpenerAlertNodeList.get(i).audiovalue;
                    int popupsec = AppController.LineOpenerAlertNodeList.get(i).popupsec;
                    String sn = AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean istotalcheck = AppController.LineOpenerAlertNodeList.get(i).isTotalCheck;
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

//                                String popalertname = "Alert at:" + hrmin + "Total Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
//                                AppController.alertsVector.addElement(popalertname);
                                String mesg = "Total Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.addAlert(hrmin,mesg);


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
        if ( null== game) {
            log(new Exception("Error: null game detected: game_id="+Gid));
            return;
        }
        int hometeamgamenumber = game.getHomegamenumber();
        int visitotteamgamenumber = game.getVisitorgamenumber();
        String homeTeam = game.getHometeam();
        String visitorTeam = game.getVisitorteam();
        Bookie bk = AppController.getBookie(bid);
       if ( null == bk) {
           log(new Exception("WARNING: null Bookie is detected, bid="+bid));
           return;
       }
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

                    List periods = AppController.LineOpenerAlertNodeList.get(i).periods;
                    List bookeis = AppController.LineOpenerAlertNodeList.get(i).bookies;
                    List leagues = AppController.LineOpenerAlertNodeList.get(i).leagues;
                    boolean isAudioChecks = AppController.LineOpenerAlertNodeList.get(i).isAudioChecks;
                    boolean isShowpopChecks = AppController.LineOpenerAlertNodeList.get(i).isShowpopChecks;
                    String audiovalue = AppController.LineOpenerAlertNodeList.get(i).audiovalue;
                    int popupsec = AppController.LineOpenerAlertNodeList.get(i).popupsec;
                    String sn = AppController.LineOpenerAlertNodeList.get(i).SportName;
                    boolean ismoneycheck = AppController.LineOpenerAlertNodeList.get(i).isMoneyCheck;
                    boolean isAllBookiesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllBookiesSelected;
                    boolean isAllLeaguesSelected = AppController.LineOpenerAlertNodeList.get(i).isAllLeaguesSelected;

                    int popupsecs = AppController.LineOpenerAlertNodeList.get(i).showpopvalue;
                    int popuplocationint = AppController.LineOpenerAlertNodeList.get(i).popuplocationint;

                    int sport_id = AppController.LineOpenerAlertNodeList.get(i).sports_id;
                    String soundfile = AppController.LineOpenerAlertNodeList.get(i).soundfile;
                    if (sportname.equalsIgnoreCase(sn)) {
                        if ((leagues.contains(lid) || isAllLeaguesSelected) && (bookeis.contains(bid) || isAllBookiesSelected) && periods.contains(per) && ismoneycheck) {
                            if (isAudioChecks) {
                                playSound(soundfile);
                            }
                            if (isShowpopChecks) {
                                String hrmin = AppController.getCurrentHoursMinutes();
                                String teaminfo = game.getShortvisitorteam() + "@" + game.getShorthometeam();

//                                String popalertname = "Alert at:" + hrmin + "Money Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
//                                AppController.alertsVector.addElement(popalertname);
                                String mesg = "Money Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.addAlert(hrmin,mesg);

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
        Bookie bk = AppController.getBookie(bid);
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

//                                String popalertname = "Alert at:" + hrmin + "TeamTotal Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
//                                AppController.alertsVector.addElement(popalertname);
                                String mesg = "TeamTotal Opener:" + getLeagueAbbr(lid) + "," + teaminfo;
                                AppController.addAlert(hrmin,mesg);

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


                Vector checkedLeagueNodes = AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes;
                log("gjhhsizeeeee" + checkednodes2.size());
                for (int j = 0; j < checkednodes2.size(); j++) {
                    //  ArrayList<TreePath> arrayList;
                    //log("its==========================="+checkedLeagueNodes.get(j));
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes2.elementAt(j);
                    //log("its"+node);

                    TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
                    log("its===========================" + checkednodes2.get(j) + "psth" + path);
                    _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
                }
                selectedList.revalidate();
                treePanel.revalidate();
                selectedList.setEnabled(false);

//                Vector checkedBookieNodes = AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes;
                for (DefaultMutableTreeNode node : checkednodes3) {
                    TreePath path = new TreePath(node.getPath());
                    log("its===========================" + node + "psth" + path);
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

            List<Sport> sportsVec = AppController.getSportsVec();
            Vector checkedsports = AppController.LineOpenerAlertNodeList.get(idx).checkedLeagueNodes;


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

    public TreeModel createBookieTreeModel2() {
        DefaultMutableTreeNode bookienode = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModel = new DefaultTreeModel(bookienode);

        try {

            List<Bookie> bookieVec = AppController.getBookiesVec();
            Vector checkedbookies = AppController.LineOpenerAlertNodeList.get(idx).checkedBookieNodes;
            List<Bookie> hiddenBookies = AppController.getHiddenCols();

            for (Bookie value : bookieVec) {
                Bookie bookie;
                DefaultMutableTreeNode tempnode;
                bookie = (Bookie) value;
                if (hiddenBookies.contains(bookie) || bookie.getBookie_id() >= 990) {
                    continue;
                }
                if (!(bookie.getName().equalsIgnoreCase("Team") || bookie.getName().equalsIgnoreCase("Gm#") || bookie.getName().equalsIgnoreCase("Time") || bookie.getName().equalsIgnoreCase("Details") || bookie.getName().equalsIgnoreCase("Chart"))) {
                    tempnode = bookienode;
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(bookie.getName());
                    tempnode.add(child);
                    bookienameidhash.put(bookie.getName(), "" + bookie.getBookie_id());

                    for (Object checkedbooky : checkedbookies) {
                        if ((checkedbooky + "").equalsIgnoreCase("" + bookie.getName()) || (checkedbooky + "").equalsIgnoreCase("All Bookies")) {
                            log("Checked Sports araay  " + checkedbooky);
                            checkednodes3.add(child);
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

    public void createSportTreeModel() {

        // first lets create sportsbook checkboxtree
        DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);


        try {

            List<Bookie> newBookiesVec = AppController.getBookiesVec();
            List<Bookie> hiddencols = AppController.getHiddenCols();
            String allbookies = "";
            for (Object o : newBookiesVec) {
                Bookie b = (Bookie) o;

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
                    //treePanel.add(new JScrollPane(thistree));
                    //treePanel.add(new JScrollPane());
                    treePanel.add(new JLabel(""));
                }

            } catch (Exception e) {
                log(e);
            }

        }

    }

//*******end of class***********
}

