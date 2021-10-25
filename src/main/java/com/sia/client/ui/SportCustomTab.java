package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import static com.sia.client.config.Utils.log;


public class SportCustomTab {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
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
    private int tabindex = 0;

    public SportCustomTab(String atype, int tabindex) {
        alerttype = atype;
        this.tabindex = tabindex;
        String userpref = "";

        if (alerttype.equalsIgnoreCase("Football")) {
            userpref = AppController.getUser().getFootballPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Basketball")) {
            userpref = AppController.getUser().getBasketballPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Baseball")) {
            userpref = AppController.getUser().getBaseballPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Hockey")) {
            userpref = AppController.getUser().getHockeyPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Fighting")) {
            userpref = AppController.getUser().getFightingPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase(SiaConst.SoccerStr)) {
            userpref = AppController.getUser().getSoccerPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Auto racing")) {
            userpref = AppController.getUser().getAutoracingPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Golf")) {
            userpref = AppController.getUser().getGolfPref();
            log("userprefs =" + userpref);

        } else if (alerttype.equalsIgnoreCase("Tennis")) {
            userpref = AppController.getUser().getTennisPref();
            log("userprefs =" + userpref);

        }


        prefs = userpref.split("\\|");

        try {
            popupsecs = Integer.parseInt(prefs[1]);
        } catch (Exception ex) {
        }


        String checkedsportsarr[] = prefs[2].split(",");
        for (int i = 0; i < checkedsportsarr.length; i++) {
            try {
                checkedsports.add("" + checkedsportsarr[i]);
            } catch (Exception ex) {
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
        JFrame jfrm = new JFrame(alerttype + " Preferences");

        // *** Use FlowLayout for the content pane. ***
        jfrm.getContentPane().setLayout(new FlowLayout());

        // Give the frame an initial size.
        jfrm.setSize(840, 840);

        // Terminate the program when the user closes the application.
        //jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //checkboxtree
        final TreeModel treeModel = createSportTreeModel(atype);

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
                log("treerows=" + treeRows);
                if (treeRows != null) {
                    java.util.Arrays.sort(treeRows);
                    for (int i = 0; i < treeRows.length; i++) {
                        TreePath path = _tree.getPathForRow(treeRows[i]);
                        selectedModel.addElement(path.getLastPathComponent());
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
        JLabel jlabpopupsecs = new JLabel("Till how many days games should  should show up  ");
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

        JRadioButton dateRadioButton = new JRadioButton("Sort by Date");
        JRadioButton leagueRadioButton = new JRadioButton("Sort by League");
        ButtonGroup sortgroup = new ButtonGroup();
        sortgroup.add(dateRadioButton);
        sortgroup.add(leagueRadioButton);


        try {

            if (Boolean.parseBoolean(prefs[0])) {
                dateRadioButton.setSelected(true);
            } else {
                leagueRadioButton.setSelected(true);
            }

        } catch (Exception ex) {
        }


        JCheckBox includeheaders = new JCheckBox("Include Headers", true);
        JCheckBox includeseries = new JCheckBox("Include Series Prices", true);
        JCheckBox includeingame = new JCheckBox("Include In Game", true);
        JCheckBox includeadded = new JCheckBox("Include Added Games", true);
        JCheckBox includeextra = new JCheckBox("Include Extra Games", true);
        JCheckBox includeprops = new JCheckBox("Include Props", true);
        try {

            if (Boolean.parseBoolean(prefs[3])) {
                includeheaders.setSelected(true);
            } else {
                includeheaders.setSelected(false);
            }
            if (Boolean.parseBoolean(prefs[4])) {
                includeseries.setSelected(true);
            } else {
                includeseries.setSelected(false);
            }
            if (Boolean.parseBoolean(prefs[5])) {
                includeingame.setSelected(true);
            } else {
                includeingame.setSelected(false);
            }
            if (Boolean.parseBoolean(prefs[6])) {
                includeadded.setSelected(true);
            } else {
                includeadded.setSelected(false);
            }
            if (Boolean.parseBoolean(prefs[7])) {
                includeextra.setSelected(true);
            } else {
                includeextra.setSelected(false);
            }
            if (Boolean.parseBoolean(prefs[8])) {
                includeprops.setSelected(true);
            } else {
                includeprops.setSelected(false);
            }


        } catch (Exception ex) {
        }
        int FPS_MIN = 0;
        int FPS_MAX = 14;

        if (popupsecs > FPS_MAX) {
            FPS_MAX = popupsecs;
        }

        JSlider popupsecsslider = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, popupsecs);

        popupsecsslider.setMajorTickSpacing(1);
        popupsecsslider.setMinorTickSpacing(0);
        popupsecsslider.setPaintTicks(true);
        popupsecsslider.setPaintLabels(true);
        popupsecsslider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int value = source.getValue();
                if (!source.getValueIsAdjusting()) {
                    popupsecs = source.getValue();
                    log("secs=" + popupsecs);
                }
            }
        });


        saveBut.addActionListener(new ActionListener() {
            int tabVal = 0;

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
						log("key="+key+"..value="+value);
						if(z > 20) {break;}
					}
					*/


                int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
                log("treerows=" + treeRows);
                if (treeRows != null) {
                    java.util.Arrays.sort(treeRows);
                    for (int i = 0; i < treeRows.length; i++) {
                        TreePath path = _tree.getPathForRow(treeRows[i]);
                        log(path.getLastPathComponent());
                        String id = "" + leaguenameidhash.get("" + path.getLastPathComponent());
                        if (id == null || id.equals("null")) {
                            id = "" + path.getLastPathComponent();
                        }
                        sportselected = sportselected + id + ",";


                    }
                }

                if (sportselected.length() == 0) {
                    sportselected = "null";
                }


                //sportselected = sportselected.substring(1);
                String newuserprefs = dateRadioButton.isSelected() + "|" + popupsecs + "|" + sportselected + "|" + includeheaders.isSelected() + "|" + includeseries.isSelected() + "|" + includeingame.isSelected() + "|" + includeadded.isSelected() + "|" + includeextra.isSelected() + "|" + includeprops.isSelected();

                log("newprefs=" + newuserprefs);
                if (alerttype.equalsIgnoreCase("football")) {
                    tabVal = 0;
                    AppController.getUser().setFootballPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Basketball")) {
                    tabVal = 1;
                    AppController.getUser().setBasketballPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Baseball")) {
                    tabVal = 2;
                    AppController.getUser().setBaseballPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Hockey")) {
                    tabVal = 3;
                    AppController.getUser().setHockeyPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Fighting")) {
                    tabVal = 4;
                    AppController.getUser().setFightingPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase(SiaConst.SoccerStr)) {
                    tabVal = 5;
                    AppController.getUser().setSoccerPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Auto racing")) {
                    tabVal = 6;
                    AppController.getUser().setAutoracingPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Golf")) {
                    tabVal = 7;
                    AppController.getUser().setGolfPref(newuserprefs);
                } else if (alerttype.equalsIgnoreCase("Tennis")) {
                    tabVal = 8;
                    AppController.getUser().setTennisPref(newuserprefs);
                }

                jfrm.dispose();
                Vector<SportsTabPane> tabpanes = AppController.getTabPanes();
                for (SportsTabPane tp : tabpanes) {
                    MainScreen oldms = (MainScreen) tp.getComponentAt(tabVal);
                    oldms.destroyMe();
                    SportType st = SportType.findBySportName(alerttype);
                    MainScreen ms = new MainScreen(st);
                    ms.setShowHeaders(includeheaders.isSelected());
                    ms.setShowSeries(includeseries.isSelected());
                    ms.setShowIngame(includeingame.isSelected());
                    ms.setShowAdded(includeadded.isSelected());
                    ms.setShowExtra(includeextra.isSelected());
                    ms.setShowProps(includeprops.isSelected());
                    tp.setComponentAt(tabVal, ms);
                    tp.refreshCurrentTab();
                }


            }
        });


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
        box2.add(dateRadioButton);
        box2.add(leagueRadioButton);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(Box.createRigidArea(new Dimension(0, 4)));
        box2.add(jlabpopupsecs);

        box2.add(popupsecsslider);
        box2.add(new JSeparator(SwingConstants.HORIZONTAL));
        box2.add(Box.createRigidArea(new Dimension(0, 4)));

        //saveBut.setMargin(new Insets(90, 90, 100, 100));


        box2.add(includeheaders, new GridBagConstraints(3, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(includeseries, new GridBagConstraints(3, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(includeingame, new GridBagConstraints(3, 6, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(includeadded, new GridBagConstraints(3, 7, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(includeextra, new GridBagConstraints(3, 8, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(includeprops, new GridBagConstraints(3, 9, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        box2.add(saveBut);
        box2.setAlignmentX(Component.CENTER_ALIGNMENT);


        // box3.add(jlabOne);
        // box3.add(jcbOne);
        //  box3.add(jcbTwo);


        //initialize tree
        for (int j = 0; j < checkednodes.size(); j++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkednodes.elementAt(j);
            //log("its"+node);
            TreePath path = new TreePath(((DefaultMutableTreeNode) node).getPath());
            log(checkednodes.elementAt(j) + "its" + path);
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


    public TreeModel createSportTreeModel(String tabname) {
        DefaultMutableTreeNode sportnode = new DefaultMutableTreeNode(tabname);
        DefaultTreeModel treeModel = new DefaultTreeModel(sportnode);

        try {

            Vector sportsVec = AppController.getSportsVec();

            for (int i = 0; i < sportsVec.size(); i++) {
                Sport sport;
                DefaultMutableTreeNode tempnode;
                sport = (Sport) sportsVec.elementAt(i);
                if (sport.getSportname().equals(tabname)) {
                    tempnode = sportnode;
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
                    tempnode.add(child);
                    leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());
                    if (checkedsports.contains("" + sport.getLeague_id()) || checkedsports.contains(sport.getSportname())
                            || checkedsports.contains("All Sports")) {
                        checkednodes.add(child);

                    }
                }

            }

            return treeModel;
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            log(e);
        }
        return null;
    }

}