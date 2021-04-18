package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Sport;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
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
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;


public class GameAlert {
    String soundfile = "";
    JFileChooser fc = new JFileChooser();
    String prefs[];
    JList selectedList = new JList();
    JList eventsList = new JList();
    DefaultListModel eventsModel = new DefaultListModel();
    int popupsecs = 5;
    ;
    int popuplocationint = 0;
    String defaultsoundfile = "";
    String defaultsoundfileplay = "";
    JLabel soundlabel = new JLabel("DEFAULT");
    private Vector checkedsports = new Vector();
    private Vector checkednodes = new Vector();
    private CheckBoxTree _tree;
    private Hashtable leaguenameidhash = new Hashtable();
    private String alerttype = "";

    public GameAlert(String atype) {
        alerttype = atype;
        String userpref = "";

        if (alerttype.equalsIgnoreCase("Final")) {
            userpref = AppController.getUser().getFinalAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "final.wav";
        } else if (alerttype.equalsIgnoreCase("Started")) {
            userpref = AppController.getUser().getStartedAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "started.wav";
        } else if (alerttype.equalsIgnoreCase("Halftime")) {
            userpref = AppController.getUser().getHalftimeAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "halftime.wav";
        } else if (alerttype.equalsIgnoreCase("Lineup")) {
            userpref = AppController.getUser().getLineupAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "lineups.wav";
        } else if (alerttype.equalsIgnoreCase("Official")) {
            userpref = AppController.getUser().getOfficialAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "officials.wav";
        } else if (alerttype.equalsIgnoreCase("Injury")) {
            userpref = AppController.getUser().getInjuryAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "injury.wav";
        } else if (alerttype.equalsIgnoreCase("Time change")) {
            userpref = AppController.getUser().getTimechangeAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "timechange.wav";
        } else if (alerttype.equalsIgnoreCase("Limit change")) {
            userpref = AppController.getUser().getLimitchangeAlert();
            log("userprefs =" + userpref);
            defaultsoundfile = "limitchange.wav";
        }
        defaultsoundfileplay = defaultsoundfile;
        prefs = userpref.split("\\|");

        try {
            popupsecs = Integer.parseInt(prefs[2]);
        } catch (Exception ex) {
            log(ex);
        }
        try {
            popuplocationint = Integer.parseInt(prefs[3]);
        } catch (Exception ex) {
            log(ex);
        }

        try {
            if (prefs[4].equals("")) {
                defaultsoundfileplay = defaultsoundfile;
                soundlabel.setText("DEFAULT");
            } else {
                soundfile = defaultsoundfileplay = prefs[4];
                soundlabel.setText(defaultsoundfileplay);

            }
        } catch (Exception ex) {
            log(ex);
        }


        String checkedsportsarr[] = prefs[5].split(",");
        for (int i = 0; i < checkedsportsarr.length; i++) {
            try {
                checkedsports.add("" + checkedsportsarr[i]);
            } catch (Exception ex) {
                log(ex);
            }
        }
        for (int j = 0; j < prefs.length; j++) {
            log(j + "=" + prefs[j]);
        }


        // owen still needs prefs4 which is sound file
  /*
  checkedsports.add("1");
 checkedsports.add("3");
 checkedsports.add("Hockey");
 checkedsports.add("11");
 */


        //LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        LookAndFeelFactory.installJideExtension();
        // Create a new JFrame container.
        JFrame jfrm = new JFrame(alerttype + " Alerts");

        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new FlowLayout());

        // Give the frame an initial size.
        jfrm.setSize(840, 840);

        // Terminate the program when the user closes the application.
        //jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //checkboxtree
        final TreeModel treeModel = createSportTreeModel();

        JPanel treePanel = new JPanel(new BorderLayout(2, 2));
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), alerttype + " Alerts", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        _tree = new CheckBoxTree(treeModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(300, 600);
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
        //	renderer.setOpenIcon(new IconUIResource(new NodeIcon('-')));
        //	renderer.setClosedIcon(new IconUIResource(new NodeIcon('+')));
        //   renderer.setLeafIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/song.png"));
        //   renderer.setClosedIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/album.png"));
        //   renderer.setOpenIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/album.png"));


        _tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] paths = e.getPaths();
                for (TreePath path : paths) {
                    eventsModel.addElement((e.isAddedPath(path) ? "Added - " : "Removed - ") + path);
                }
                eventsModel.addElement("---------------");
                eventsList.ensureIndexIsVisible(eventsModel.size() - 1);
                DefaultListModel selectedModel = new DefaultListModel();
                TreePath[] treePaths = _tree.getCheckBoxTreeSelectionModel().getSelectionPaths();

                int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                System.out.println("treerows=" + treeRows);
                if (treeRows != null) {
                    java.util.Arrays.sort(treeRows);
                    for (int i = 0; i < treeRows.length; i++) {
                        TreePath path = _tree.getPathForRow(treeRows[i]);
                        selectedModel.addElement(path.getLastPathComponent());
                        //System.out.println("selected="+path.getLastPathComponent());

                    }
                }
				
				/*
                if (treePaths != null) 
				{
                    for (TreePath path : treePaths) 
					{
                        selectedModel.addElement(path.getLastPathComponent());
						
                    }
					
                }
				*/
                log("-------------------------");
                selectedList.setModel(selectedModel);
            }
        });
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


        SearchableUtils.installSearchable(_tree);

        TreeUtils.expandAll(_tree, true);

        treePanel.add(new JScrollPane(_tree));
        //end checkboxtree


        // Make the labels.
        JLabel jlabOne = new JLabel("Button Group One");
        JLabel jlabpopup = new JLabel("Popup Notification");
        JLabel jlabpopupsecs = new JLabel("Number of Seconds Before Popup Disappears        ");
        JLabel jlabsound = new JLabel("Sound Notification");
        JLabel popuplocationlabel = new JLabel("Popup Location");


        // Make the buttons.
        JButton jbtnOne = new JButton("One");
        JButton jbtnTwo = new JButton("Two");
        JButton jbtnThree = new JButton("Three");
        JButton jbtnFour = new JButton("Four");
        Dimension btnDim = new Dimension(100, 25);

        JButton testsoundBut = new JButton("Test Sound");
        JButton testpopupBut = new JButton("Test Popup");
        JButton saveBut = new JButton("Save");

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

        try {
            enablepopup.setSelected(Boolean.parseBoolean(prefs[0]));
        } catch (Exception ex) {
            log(ex);
        }
        try {
            enablesound.setSelected(Boolean.parseBoolean(prefs[1]));
        } catch (Exception ex) {
            log(ex);
        }

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


        JideToggleButton upperright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperright.png")));
        JideToggleButton upperleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("upperleft.png")));
        JideToggleButton lowerright = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerright.png")));
        JideToggleButton lowerleft = new JideToggleButton(new ImageIcon(Utils.getMediaResource("lowerleft.png")));

        upperright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.NORTH_EAST;
                }
            }
        });
        upperleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.NORTH_WEST;
                }
            }
        });
        lowerright.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.SOUTH_EAST;
                }
            }
        });
        lowerleft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    popuplocationint = SwingConstants.SOUTH_WEST;
                }
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


        testsoundBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                //playSound(defaultsoundfileplay);
                new SoundPlayer(defaultsoundfileplay, true);

            }
        });

        testpopupBut.addActionListener(ae -> {

            new UrgentMessage("<HTML><H1>" + alerttype.toUpperCase() + "</H1><FONT COLOR=BLUE>" +
                    "NFL<BR><TABLE cellspacing=5 cellpadding=5>" +

                    "<TR><TD COLSPAN=3>TEST MESSAGE A1</TD></TR>" +
                    "<TR><TD COLSPAN=3>TEST MESSAGE A2</TD></TR>" +
                    "</TABLE></FONT></HTML>", popupsecs * 1000, popuplocationint, jfrm);
            log("popupsecs=" + popupsecs);
        });

        customsoundBut.addActionListener(ae -> {

            checkAndRunInEDT(() -> {
                int returnVal = fc.showOpenDialog(jfrm);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();

                    String filename = file.getAbsolutePath();
                    if (filename.indexOf("~") != -1 || filename.indexOf("|") != -1 || filename.indexOf("*") != -1 || filename.indexOf(",") != -1
                            || filename.indexOf("!") != -1 || filename.indexOf("?") != -1) {
                        JOptionPane.showMessageDialog(null, "Illegal character(s) used for file! Please rename file and try again");
                        return;
                    }


                    soundfile = file.getAbsolutePath();
                    defaultsoundfileplay = soundfile;
                    soundlabel.setText(soundfile);
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName());
                } else {
                    //log.append("Open command cancelled by user." + newline);
                }
            });
        });


        defaultsoundBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                defaultsoundfileplay = defaultsoundfile;
                soundfile = "";
                soundlabel.setText("DEFAULT");

            }
        });
        saveBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                String sportselected = "";
					/*
					Enumeration enum00 = leaguenameidhash.keys();
					int z = 0;
					while(enum00.hasMoreElements())
					{
						z++;
						String key = (String)enum00.nextElement();
						String value = (String)leaguenameidhash.get(key);
						System.out.println("key="+key+"..value="+value);
						if(z > 20) {break;}
					}
					*/


                int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                System.out.println("treerows=" + treeRows);
                if (treeRows != null) {
                    java.util.Arrays.sort(treeRows);
                    for (int i = 0; i < treeRows.length; i++) {
                        TreePath path = _tree.getPathForRow(treeRows[i]);
                        System.out.println(path.getLastPathComponent());
                        String id = "" + leaguenameidhash.get("" + path.getLastPathComponent());

                        if (id == null || id.equals("null")) {
                            id = "" + path.getLastPathComponent();
                        }
                        sportselected = sportselected + id + ",";


                    }
                }


                //sportselected = sportselected.substring(1);
                String newuserprefs = enablepopup.isSelected() + "|" + enablesound.isSelected() + "|" + popupsecs + "|" + popuplocationint + "|" +
                        soundfile + "|" + sportselected;

                System.out.println("newprefs=" + newuserprefs);
                if (alerttype.equalsIgnoreCase("Final")) {
                    AppController.getUser().setFinalAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Started")) {
                    AppController.getUser().setStartedAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Halftime")) {
                    AppController.getUser().setHalftimeAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Lineup")) {
                    AppController.getUser().setLineupAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Official")) {
                    AppController.getUser().setOfficialAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Injury")) {
                    AppController.getUser().setInjuryAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Time Change")) {
                    AppController.getUser().setTimechangeAlert(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Limit Change")) {
                    AppController.getUser().setLimitchangeAlert(newuserprefs);
                }
                jfrm.dispose();
            }
        });

        JPanel testbutPanel = new JPanel(new GridLayout(0, 2, 2, 2));

        testbutPanel.add(testpopupBut);
        testbutPanel.add(testsoundBut);

        JPanel soundbutPanel = new JPanel(new GridLayout(0, 2, 2, 2));

        soundbutPanel.add(defaultsoundBut);
        soundbutPanel.add(customsoundBut);


        // Create three vertical boxes.
        Box box1 = Box.createVerticalBox();
        Box box2 = Box.createVerticalBox();
        Box box3 = Box.createVerticalBox();

        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box3.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add the components to the boxes.
	/*
    box1.add(jlabOne); 
    box1.add(Box.createRigidArea(new Dimension(0, 4))); 
    box1.add(jbtnOne); 
    box1.add(Box.createRigidArea(new Dimension(0, 4))); 
    box1.add(jbtnTwo); 
*/
        box1.add(treePanel);

        box2.add(selectedPanel);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(enablepopup);
        box2.add(enablesound);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(Box.createRigidArea(new Dimension(0, 4)));
        box2.add(jlabpopupsecs);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(popupsecsslider);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(Box.createRigidArea(new Dimension(0, 4)));
        box2.add(popuplocationlabel);
        //box2.add(popuplocation);
        box2.add(radioPanel);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));

        box2.add(Box.createRigidArea(new Dimension(0, 10)));
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));

        box2.add(soundbutPanel);
        box2.add(soundlabel);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(testbutPanel);
        box2.add(saveBut);


        box3.add(jlabOne);
        box3.add(jcbOne);
        box3.add(jcbTwo);


        //initialize tree
        for (int j = 0; j < checkednodes.size(); j++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes.elementAt(j);
            //System.out.println("its"+node);
            TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
            //System.out.println("its==========================="+checkednodes.get(j)+"psth"+path);
            _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
        }

        selectedList.revalidate();
        selectedPanel.revalidate();

        selectedList.setEnabled(false);

        // Add the boxes to the content pane.
        jfrm.getContentPane().add(box1);
        jfrm.getContentPane().add(box2);
        //jfrm.getContentPane().add(box3);

        // Display the frame.
        jfrm.setVisible(true);
    }


    public TreeModel createSportTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("All Sports");
        DefaultTreeModel treeModel = new DefaultTreeModel(root);


        DefaultMutableTreeNode football = new DefaultMutableTreeNode("Football");
        DefaultMutableTreeNode basketball = new DefaultMutableTreeNode("Basketball");
        DefaultMutableTreeNode baseball = new DefaultMutableTreeNode("Baseball");
        DefaultMutableTreeNode hockey = new DefaultMutableTreeNode("Hockey");
        DefaultMutableTreeNode soccer = new DefaultMutableTreeNode("Soccer");
        DefaultMutableTreeNode fighting = new DefaultMutableTreeNode("Fighting");
        DefaultMutableTreeNode golf = new DefaultMutableTreeNode("Golf");
        DefaultMutableTreeNode tennis = new DefaultMutableTreeNode("Tennis");
        DefaultMutableTreeNode autoracing = new DefaultMutableTreeNode("Auto Racing");
        DefaultMutableTreeNode horse = new DefaultMutableTreeNode("Horse");
        DefaultMutableTreeNode esport = new DefaultMutableTreeNode("E-Sport");
        DefaultMutableTreeNode other = new DefaultMutableTreeNode("Other");
        root.add(football);
        root.add(basketball);
        root.add(baseball);
        root.add(hockey);
        root.add(soccer);
        root.add(fighting);
        root.add(golf);
        root.add(tennis);
        root.add(autoracing);
        root.add(horse);
        //root.add(esport);
        //root.add(other);


        try {

            Vector sportsVec = AppController.getSportsVec();
            for (int i = 0; i < sportsVec.size(); i++) {
                DefaultMutableTreeNode tempnode;
                Sport sport = (Sport) sportsVec.elementAt(i);
                if (sport.getSportname().equals("Football")) {
                    tempnode = football;
                } else if (sport.getSportname().equals("Basketball")) {
                    tempnode = basketball;
                } else if (sport.getSportname().equals("Baseball")) {
                    tempnode = baseball;
                } else if (sport.getSportname().equals("Hockey")) {
                    tempnode = hockey;
                } else if (sport.getSportname().equals("Soccer")) {
                    tempnode = soccer;
                } else if (sport.getSportname().equals("Fighting")) {
                    tempnode = fighting;
                } else if (sport.getSportname().equals("Golf")) {
                    tempnode = golf;
                } else if (sport.getSportname().equals("Tennis")) {
                    tempnode = tennis;
                } else if (sport.getSportname().equals("Auto Racing")) {
                    tempnode = autoracing;
                } else if (sport.getSportname().equals("Horse")) {
                    tempnode = horse;
                }
				/*
				else if(sport.getSportname().equals("E-Sport"))
				{
					tempnode = esport;
				}
				*/
                else {
                    tempnode = other;
                }

                DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                tempnode.add(child);
                leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());
                if (checkedsports.contains("" + sport.getLeague_id()) || checkedsports.contains(sport.getSportname())
                        || checkedsports.contains("All Sports")) {
                    checkednodes.add(child);

                }
                //CheckBoxTreeSelectionModel#addSelectionPaths()
			/*
			

        final JButton selectAll = new JButton(new AbstractAction("Select The Second Leaf Node") {
            private static final long serialVersionUID = -5580913906799074020L;

            public void actionPerformed(ActionEvent e) {
                TreeModel treeModel = _tree.getModel();
                Object child = treeModel.getChild(treeModel.getChild(treeModel.getRoot(), 0), 2);
                if (child instanceof DefaultMutableTreeNode) {
                    TreePath path = new TreePath(((DefaultMutableTreeNode) child).getPath());
                    _tree.getCheckBoxTreeSelectionModel().addSelectionPath(path);
                }
            }
        });
		*/
            }


            return treeModel;
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            log(e);
        }
        return null;
    }

    public void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            log(ex);
            JOptionPane.showMessageDialog(null, "Error Playing File! Check file path. Only AIFF,AU and WAV are supported!");
        }
    }
}