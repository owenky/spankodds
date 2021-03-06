package com.sia.client.ui;// Demonstrate BoxLayout and the Box class.

import com.jidesoft.swing.*;
import com.jidesoft.tree.TreeUtils;
import com.sia.client.model.Sport;
import com.sia.client.model.SportNode;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.MainScreen;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.sia.client.config.Utils.log;


public class SportConfigurator {

    public static final int FPS_MAX = 14;
    private static final int FPS_MIN = 0;
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private final JList<Object> selectedList = new JList<>();
    private final JList<String> eventsList = new JList<>();
    private final DefaultListModel<String> eventsModel = new DefaultListModel<>();
    private int maxShowDays = 5;
    private final List<String> checkedsports = new ArrayList<>();
    private final List<DefaultMutableTreeNode> checkednodes = new ArrayList<>();
    private CheckBoxTree _tree;
    private final SportType sportType;
    private final AnchoredLayeredPane anchoredLayeredPane;

    public SportConfigurator(AnchoredLayeredPane anchoredLayeredPane,SportType sportType) {
        this.sportType = sportType;
        this.anchoredLayeredPane = anchoredLayeredPane;
    }
    public JPanel getMainPanel() {

        String userpref = sportType.getPerference();
        final String[] prefs = userpref.split("\\|");

        try {
            maxShowDays = getEffectiveMaxShowDays(Integer.parseInt(prefs[1]));
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
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
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
            DefaultListModel<Object> selectedModel = new DefaultListModel<>();

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
        JLabel jlabpopupsecs = new JLabel("Till how many days games should show up");

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
//
//
//        if (popupsecs > FPS_MAX) {
//            FPS_MAX = popupsecs;
//        }
        maxShowDays = getEffectiveMaxShowDays(maxShowDays);

        JSlider popupsecsslider = new JSlider(JSlider.HORIZONTAL,
                FPS_MIN, FPS_MAX, Math.min(FPS_MAX,maxShowDays));

        Hashtable<Object,Object> dictionary = popupsecsslider.createStandardLabels(1);
        popupsecsslider.setLabelTable(dictionary);

        popupsecsslider.setMajorTickSpacing(1);
        popupsecsslider.setMinorTickSpacing(0);
        popupsecsslider.setPaintTicks(true);
        popupsecsslider.setPaintLabels(true);
        popupsecsslider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                maxShowDays = source.getValue();
//                log("secs=" + popupsecs);
            }
        });

        JLabel maxValueLabel = (JLabel)dictionary.get(FPS_MAX);
        maxValueLabel.setText("\u221e");

        saveBut.addActionListener(ae -> {

            String sportselected = "";
            int[] treeRows = _tree.getCheckBoxTreeSelectionModel().getSelectionRows();
            if (treeRows != null) {
                java.util.Arrays.sort(treeRows);
                for (final int treeRow : treeRows) {
                    TreePath path = _tree.getPathForRow(treeRow);
                    log(path.getLastPathComponent());
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                    Object userObject = treeNode.getUserObject();
                    if ( userObject instanceof SportNode) {
                        //userObject of root node is String, such as "Soccer", representing sport type -- 03/27/2022
                        int id = ((SportNode)userObject).getLeague_id();
                        sportselected = sportselected + id + ",";
                    } else if ( userObject.equals(sportType.getSportName())) {
                        //this scenario is for select all games for this sport type. -- 03/27/2022
                        sportselected = sportType.getSportName();
                        break;
                    }
                }
            }

            if (sportselected.length() == 0) {
                sportselected = "null";
            }

            String newuserprefs = dateRadioButton.isSelected() + "|" + maxShowDays + "|" + sportselected + "|" + includeheaders.isSelected() + "|" + includeseries.isSelected() + "|" + includeingame.isSelected() + "|" + includeadded.isSelected() + "|" + includeextra.isSelected() + "|" + includeprops.isSelected();

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
                tp.refreshSport(sportType);
            });
            close();
        });
        Box box1 = Box.createVerticalBox();
        Box box2 = Box.createVerticalBox();

        // Create invisible borders around the boxes.
        box1.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box2.setBorder(
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
        for (DefaultMutableTreeNode node : checkednodes) {
            TreePath path = new TreePath(node.getPath());
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
    public static int getEffectiveMaxShowDays(int popupsecs) {
        int effectiveMaxShowDays = Math.min(popupsecs, FPS_MAX);
        if ( FPS_MAX ==effectiveMaxShowDays) {
            effectiveMaxShowDays = Integer.MAX_VALUE;
        }
        return effectiveMaxShowDays;
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
                    SportNode sn = new SportNode(sport);
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(sn);
                    tempnode.add(child);
                    if (checkedsports.contains("" + sn.getLeague_id()) || checkedsports.contains(sn.getSportname())
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
        if ( null != anchoredLayeredPane) {
            anchoredLayeredPane.close();
        }
    }
}