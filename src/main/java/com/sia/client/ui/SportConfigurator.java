package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.MainScreen;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;


public class SportConfigurator {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    String[] prefs;
    JList selectedList = new JList();
    private JList<String> eventsList = new JList<>();
    private DefaultListModel<String> eventsModel = new DefaultListModel<>();
    int popupsecs = 5;
    private Vector checkedsports = new Vector();
    private Vector checkednodes = new Vector();
    private CheckBoxTree _tree;
    private Hashtable leaguenameidhash = new Hashtable();
    private final SportType sportType;
    private ActionListener closeActionListener;

    public SportConfigurator(SportType sportType) {
        this.sportType = sportType;
    }
    public JPanel getConfigurationPanel() {
        JPanel configPanel = new JPanel();
        configPanel.setSize(840, 840);
        configPanel.setLayout(new BorderLayout());
        configPanel.add(getTitlePanel(),BorderLayout.NORTH);
        configPanel.add(getMainPanel(),BorderLayout.CENTER);
        return configPanel;
    }
    public void setCloseActionListener(ActionListener closeActionListener) {
        this.closeActionListener = closeActionListener;
    }
    private JPanel getTitlePanel() {

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel title = new JLabel(sportType.getSportName() + " Preferences");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        Font defaultFont = title.getFont();
        Font titleFont = new Font(defaultFont.getFontName(),Font.BOLD,defaultFont.getSize()+2);
        title.setFont(titleFont);

        JButton closeBtn = new JButton("X");
        closeBtn.setFont(new Font(defaultFont.getFontName(),Font.BOLD,defaultFont.getSize()+4));
        closeBtn.setOpaque(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder());
        titlePanel.add(closeBtn,BorderLayout.EAST);
        titlePanel.add(title,BorderLayout.CENTER);

        closeBtn.addActionListener(event-> {
            close();
        });
        titlePanel.setBorder(BorderFactory.createEmptyBorder(7, 5, 1, 7));
        return titlePanel;
    }
    private JPanel getMainPanel() {

        String userpref = sportType.getPerference();
        prefs = userpref.split("\\|");

        try {
            popupsecs = Integer.parseInt(prefs[1]);
        } catch (Exception ex) {
            log(ex);
        }


        String[] checkedsportsarr = prefs[2].split(",");
        for (final String s : checkedsportsarr) {
            try {
                checkedsports.add("" + s);
            } catch (Exception ex) {
                log(ex);
            }
        }
        JPanel mainPanel = new JPanel();
        // *** Use FlowLayout for the content pane. ***
        mainPanel.setLayout(new FlowLayout());
        //checkboxtree
        final TreeModel treeModel = createSportTreeModel(sportType.getSportName());

        JPanel treePanel = new JPanel(new BorderLayout(2, 2));
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), sportType.getSportName() + " Alerts", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
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

        _tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(e -> {
            TreePath[] paths = e.getPaths();
            for (TreePath path : paths) {
                eventsModel.addElement((e.isAddedPath(path) ? "Added - " : "Removed - ") + path);
            }
            eventsModel.addElement("---------------");
            eventsList.ensureIndexIsVisible(eventsModel.size() - 1);
            DefaultListModel selectedModel = new DefaultListModel();

            int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
            if (treeRows != null) {
                java.util.Arrays.sort(treeRows);
                for (final int treeRow : treeRows) {
                    TreePath path = _tree.getPathForRow(treeRow);
                    selectedModel.addElement(path.getLastPathComponent());
                }
            }
            selectedList.setModel(selectedModel);
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
        JLabel jlabpopupsecs = new JLabel("Till how many days games should show up  ");

        // Make the buttons.
        JButton jbtnOne = new JButton("One");
        JButton jbtnTwo = new JButton("Two");
        JButton jbtnThree = new JButton("Three");
        JButton jbtnFour = new JButton("Four");
        Dimension btnDim = new Dimension(100, 25);

        JButton saveBut = new JButton("Save");

        jbtnOne.setMinimumSize(btnDim);
        jbtnOne.setMaximumSize(btnDim);
        jbtnTwo.setMinimumSize(btnDim);
        jbtnTwo.setMaximumSize(btnDim);
        jbtnThree.setMinimumSize(btnDim);
        jbtnThree.setMaximumSize(btnDim);
        jbtnFour.setMinimumSize(btnDim);
        jbtnFour.setMaximumSize(btnDim);

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
            log(ex);
        }


        JCheckBox includeheaders = new JCheckBox("Include Headers", true);
        JCheckBox includeseries = new JCheckBox("Include Series Prices", true);
        JCheckBox includeingame = new JCheckBox("Include In Game", true);
        JCheckBox includeadded = new JCheckBox("Include Added Games", true);
        JCheckBox includeextra = new JCheckBox("Include Extra Games", true);
        JCheckBox includeprops = new JCheckBox("Include Props", true);
        try {

            includeheaders.setSelected(Boolean.parseBoolean(prefs[3]));
            includeseries.setSelected(Boolean.parseBoolean(prefs[4]));
            includeingame.setSelected(Boolean.parseBoolean(prefs[5]));
            includeadded.setSelected(Boolean.parseBoolean(prefs[6]));
            includeextra.setSelected(Boolean.parseBoolean(prefs[7]));
            includeprops.setSelected(Boolean.parseBoolean(prefs[8]));


        } catch (Exception ex) {
            log(ex);
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
        popupsecsslider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                popupsecs = source.getValue();
                log("secs=" + popupsecs);
            }
        });


        saveBut.addActionListener(ae -> {

            String sportselected = "";
            int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
            if (treeRows != null) {
                java.util.Arrays.sort(treeRows);
                for (final int treeRow : treeRows) {
                    TreePath path = _tree.getPathForRow(treeRow);
                    log(path.getLastPathComponent());
                    String id = "" + leaguenameidhash.get("" + path.getLastPathComponent());
                    if ( "null".equals(id)) {
                        id = "" + path.getLastPathComponent();
                    }
                    sportselected = sportselected + id + ",";
                }
            }

            if (sportselected.length() == 0) {
                sportselected = "null";
            }

            String newuserprefs = dateRadioButton.isSelected() + "|" + popupsecs + "|" + sportselected + "|" + includeheaders.isSelected() + "|" + includeseries.isSelected() + "|" + includeingame.isSelected() + "|" + includeadded.isSelected() + "|" + includeextra.isSelected() + "|" + includeprops.isSelected();

            sportType.setPerference(newuserprefs);
            int tabVal = SportType.getPreDefinedSportTabIndex(sportType);
            SpankyWindow.applyToAllWindows((tp)-> {
                MainScreen oldms = (MainScreen) tp.getComponentAt(tabVal);
                oldms.destroyMe();
                MainScreen ms = tp.createMainScreen(sportType);
                ms.setShowHeaders(includeheaders.isSelected());
                ms.setShowSeries(includeseries.isSelected());
                ms.setShowIngame(includeingame.isSelected());
                ms.setShowAdded(includeadded.isSelected());
                ms.setShowExtra(includeextra.isSelected());
                ms.setShowProps(includeprops.isSelected());
                tp.setComponentAt(tabVal, ms);
                tp.refreshCurrentTab();
            });
            close();
        });
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
        mainPanel.add(box1);
        mainPanel.add(box2);

        return mainPanel;
    }
    private TreeModel createSportTreeModel(String tabname) {
        DefaultMutableTreeNode sportnode = new DefaultMutableTreeNode(tabname);
        DefaultTreeModel treeModel = new DefaultTreeModel(sportnode);

        try {

            List<Sport> sportsVec = AppController.getSportsVec();

            for (Sport value : sportsVec) {
                Sport sport;
                DefaultMutableTreeNode tempnode;
                sport = value;
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
            log(e);
        }
        return null;
    }
    private void close() {
        if ( null != closeActionListener) {
            ActionEvent ac = new ActionEvent(this,ActionEvent.ACTION_PERFORMED, "close");
            closeActionListener.actionPerformed(ac);
        }
    }
}