package com.sia.client.ui;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.model.*;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class CustomTab2 extends AbstractLayeredDialog {

    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final String UP_BUTTON_LABEL = "^";
    private static final String DOWN_BUTTON_LABEL = "v";
    private static final String ADD_BUTTON_LABEL = "Add >";
//    private static final String ADD_ALL_BUTTON_LABEL = "Add All >>";
    private static final String REMOVE_BUTTON_LABEL = "< Remove";
    private static final String REMOVE_ALL_BUTTON_LABEL = "<< Remove All";
    private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Available Sports";
    private static final String DEFAULT_DEST_CHOICE_LABEL = "Selected Sports";
    private static final String SAVE_BUTTON_LABEL = "Save";
    private boolean editing = false;
    private final List<String> illegalnames = new ArrayList<>();
    private Map<String,TreePath> pathhash;
    private JTree jtree;
    private JScrollPane jscrlp = new JScrollPane();
    private final InvisibleNode root = new InvisibleNode("Games");
    private MyListModel2 sourceListModel;
    private final JList<GameGroupNode> destList;
    private final MyListModel2 destListModel;
    private JLabel jlab;
    private final JCheckBox includeheaders;
    private final JCheckBox includeseries;
    private final JCheckBox includeingame;
    private final JCheckBox includeadded;
    private final JCheckBox includeextra;
    private final JCheckBox includeprops;
    private final JTextField tabname;
    private final int activeSportsTabPaneIndex;
    private final JPanel panel = new JPanel();

    public CustomTab2(SportsTabPane stp, String title, int activeSportsTabPaneIndex) {
        super(stp,title,SiaConst.CUSTOMTABHELPURL);
        this.activeSportsTabPaneIndex = activeSportsTabPaneIndex;
        tabname = new JTextField(10);
        tabname.setDocument(new JTextFieldLimit(10));
        TextPrompt tp7 = new TextPrompt("Name Your Tab", tabname);
        tp7.setForeground(Color.RED);
        tp7.setShow(TextPrompt.Show.FOCUS_LOST);
        tp7.changeStyle(Font.BOLD + Font.ITALIC);

        tabname.setMinimumSize(new Dimension(100, 20));
        includeheaders = new JCheckBox("Include Headers", true);
        includeseries = new JCheckBox("Include Series Prices", true);
        includeingame = new JCheckBox("Include In Game", true);
        includeadded = new JCheckBox("Include Added Games", true);
        includeextra = new JCheckBox("Include Extra Games", true);
        includeprops = new JCheckBox("Include Props", true);
        destListModel = new MyListModel2();
        destList = new JList<>(destListModel);
        destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        init();
        initScreen();
    }
    public CustomTab2(SportsTabPane stp,String title,int activeSportsTabPaneIndex, String tabnamestr, int tabindex) {
        super(stp,title,SiaConst.CUSTOMTABHELPURL);
        this.activeSportsTabPaneIndex = activeSportsTabPaneIndex;
//        this.tabindex = tabindex;

        tabname = new JTextField(tabnamestr, 10);
        tabname.setDocument(new JTextFieldLimit(10));
        TextPrompt tp7 = new TextPrompt("Name Your Tab", tabname);
        tp7.setForeground(Color.RED);
        tp7.setShow(TextPrompt.Show.FOCUS_LOST);
        //tp7.changeAlpha(0.5f);
        tp7.changeStyle(Font.BOLD + Font.ITALIC);
        tabname.setMinimumSize(new Dimension(100, 20));
        includeheaders = new JCheckBox("Include Headers", true);
        includeseries = new JCheckBox("Include Series Prices", true);
        includeingame = new JCheckBox("Include In Game", true);
        includeadded = new JCheckBox("Include Added Games", true);
        includeextra = new JCheckBox("Include Extra Games", true);
        includeprops = new JCheckBox("Include Props", true);
        String msinfo = AppController.getTabInfo(tabnamestr);
        if (msinfo == null) {
//            msinfo = "";
            editing = false;
        } else {
            editing = true;
            String[] items = msinfo.split("\\*");

            for (int j = 0; j < items.length; j++) {
                log(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (String header : headers) {
                        if ( ! header.equals("")) {
                            final List<String> customheaders = new ArrayList<>();
                            customheaders.add(header);
                            log("adding header=" + header);
                        }
                    }
                } else if (j == 1) {
                    tabname.setText(items[j]);
                    tabname.setEnabled(false);
                } else if (j == 2) {
                    includeheaders.setSelected(Boolean.parseBoolean(items[j]));
                } else if (j == 3) {
                    includeseries.setSelected(Boolean.parseBoolean(items[j]));
                } else if (j == 4) {
                    includeingame.setSelected(Boolean.parseBoolean(items[j]));
                } else if (j == 5) {
                    includeadded.setSelected(Boolean.parseBoolean(items[j]));
                } else if (j == 6) {
                    includeextra.setSelected(Boolean.parseBoolean(items[j]));
                } else if (j == 7) {
                    includeprops.setSelected(Boolean.parseBoolean(items[j]));
                }

            }

        }

        destListModel = new MyListModel2();
        destList = new JList<>(destListModel);
        destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);


        init(tabnamestr);
        initScreen(tabnamestr);

    }
    @Override
    public JComponent getUserComponent() {
        return panel;
    }
    private void init() {
        init("");
    }

    private void initScreen() {
        initScreen("");
    }

    private void addSportTypes(SportType st,Map<String,GameGroupNode> gameGroupNodeMap) {
        InvisibleNode node = new InvisibleNode(st.getSportName());
        root.add(node);
        //dont filter game by game near for custom tab selecting sport. -- 05/30/2022
        Function<Game,Boolean> gameFilter = (game)-> null == GameUtils.checkError(game)  && st.isMyType(game);
        GameGroupAggregator gameGroupAggregator = new GameGroupAggregator(st,gameFilter,false);
        Map<GameGroupHeader, Vector<Game>> headerToGameListMap = gameGroupAggregator.aggregate();
        List<GameGroupHeader> gameGroupHeaderList = headerToGameListMap.keySet().stream().sorted(new GameGroupDateSorter().thenComparing(new GameGroupLeagueSorter())).collect(Collectors.toList());
        for(GameGroupHeader header: gameGroupHeaderList) {
            List<Game> gameList = headerToGameListMap.get(header);
            int displayNo = (int) gameList.stream().filter(g -> !g.isSeriesprice()).filter(g -> !g.isForprop()).count();
            GameGroupNode ggn = new GameGroupNode(header,displayNo);
            InvisibleNode child = new InvisibleNode(ggn);
            gameGroupNodeMap.put(header.getGameGroupHeaderStr(),ggn);
            node.add(child);
        }
    }

    public void init(String tabnamestr) {


        pathhash = new HashMap<>();
        jlab = new JLabel();
        SportType [] predefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: predefinedSportTypes) {
            illegalnames.add(st.getSportName().toLowerCase());
        }

        SportType [] selectableTypes;
        final SportType selectedType = SportType.findBySportName(tabnamestr);
        if ( null != selectedType && selectedType.isPredifined()) {
            selectableTypes = new SportType[1];
            selectableTypes[0] = selectedType;
        } else {
            selectableTypes = SportType.getPreDefinedSports();
        }

        Map<String,GameGroupNode> gameGroupNodeMap = new HashMap<>();
        for(SportType st: selectableTypes) {
            addSportTypes(st,gameGroupNodeMap);
        }
        if ( null != selectedType &&  null != selectedType.getCustomheaders()) {
            List<GameGroupNode> gameGroupNodes = new ArrayList<>(selectedType.getCustomheaders().size());

            for(String header: selectedType.getCustomheaders()) {
                InvisibleNode sourceNode = findSourceNode(root,header);
                if ( null != sourceNode) {
                    gameGroupNodes.add((GameGroupNode) sourceNode.getUserObject());
                    sourceNode.setVisible(false);
                }
            }
            addDestinationElements(gameGroupNodes.toArray(new GameGroupNode[0]));
        }


        InvisibleTreeModel ml = new InvisibleTreeModel(root);
        jtree = new JTree(ml);


        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value, boolean sel, boolean expanded, boolean leaf,
                                                          int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded,
                        leaf, row, hasFocus);
                if (!((InvisibleNode) value).isVisible()) {
                    setForeground(Color.green);
                } else {
                    setForeground(Color.black);
                }
                return this;
            }
        };

        renderer.setLeafIcon(null);
        renderer.setOpenIcon(new IconUIResource(new NodeIcon('-')));
        renderer.setClosedIcon(new IconUIResource(new NodeIcon('+')));

        jtree.setCellRenderer(renderer);

        jtree.setEditable(false);
        jtree.setRootVisible(false);


//        TreeSelectionModel tsm = jtree.getSelectionModel();
        //tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        jscrlp = new JScrollPane(jtree);

        // Listen for tree expansion events.
        jtree.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent tse) {
                // Get the path to the expansion point.
                TreePath tp = tse.getPath();

                // Display the node
                jlab.setText("Expansion: " +
                        tp.getLastPathComponent());
            }

            public void treeCollapsed(TreeExpansionEvent tse) {
                // Get the path to the expansion point.
                TreePath tp = tse.getPath();

                // Display the node
                jlab.setText("Collapse: " +
                        tp.getLastPathComponent());
            }
        });

        // Listen for tree selection events.
        jtree.addTreeSelectionListener(tse -> {
            // Get the path to the selection.
            TreePath tp = tse.getPath();

            // Display the selected node.
            jlab.setText("Selection event: " +
                    tp.getLastPathComponent());
        });

        // Listen for tree model events. Notice that the
        // listener is registered with the tree model.
        jtree.getModel().addTreeModelListener(new TreeModelListener() {
            public void treeNodesChanged(TreeModelEvent tse) {
                // Get the path to the change.
                TreePath tp = tse.getTreePath();

                // Display the path
                jlab.setText("Model change path: " + tp);
            }
            public void treeNodesInserted(TreeModelEvent tse) {
            }

            public void treeNodesRemoved(TreeModelEvent tse) {
            }

            public void treeStructureChanged(TreeModelEvent tse) {
            }
        });


        jtree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int selRow = jtree.getRowForLocation(me.getX(), me.getY());
                TreePath selPath = jtree.getPathForLocation(me.getX(), me.getY());
                if (selRow != -1) {


                    if (jtree.isCollapsed(selRow)) {
                        jtree.expandRow(selRow);
                    } else {
                        jtree.collapseRow(selRow);
                    }


                    if (me.getClickCount() == 2 && selPath.getPathCount() == 3) {
                        log("treepath=" + selPath);
                        log("pathcount=" + selPath.getPathCount());
                        log("lastPathComponent=" + selPath.getLastPathComponent());
                        InvisibleNode node = (InvisibleNode) selPath.getLastPathComponent();

                        if (pathhash.get(node.toString()) == null) // not there already
                        {
                            addDestinationElements( (GameGroupNode)((InvisibleNode) selPath.getLastPathComponent()).getUserObject());

                            log("node=" + node.toString() + "..");
                            pathhash.put(node.toString(), selPath);
                            node.setVisible(false);
                            ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                            if (selRow != 0) {
                                jtree.setSelectionRow(0);
                            } else {
                                jtree.setSelectionRow(1);
                            }
                        }
                    }
                }
            }
        });


    }
    private InvisibleNode findSourceNode(InvisibleNode node,String header) {
        if ( nodeContainsHeader(node,header)) {
            return node;
        }

        for(int i=0;i<node.getChildCount();i++) {
            InvisibleNode child = (InvisibleNode)node.getChildAt(i);
            InvisibleNode target = findSourceNode(child,header);
            if ( null != target) {
                return target;
            }
        }
        return null;
    }
    private boolean nodeContainsHeader(InvisibleNode node, String header) {
        if ( node.getUserObject() instanceof GameGroupNode) {
            GameGroupNode gameGroupNode = (GameGroupNode)node.getUserObject();
            return header.equals(gameGroupNode.getGameGroupHeader());
        }
        return false;
    }
    private void initScreen(String tabnamestr) {

        panel.setBorder(BorderFactory.createEtchedBorder());


        panel.setLayout(new GridBagLayout());
        final JLabel sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
        sourceListModel = new MyListModel2();
//        sourceList = new JList(sourceListModel);


        panel.add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(jscrlp, new GridBagConstraints(0, 1, 1, 4, .5,
                1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                EMPTY_INSETS, 0, 0));

        final JButton addButton = new JButton(ADD_BUTTON_LABEL);
        panel.add(addButton, new GridBagConstraints(1, 1, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        addButton.addActionListener(new AddListener());

	/*
    add(addAllButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addAllButton.addActionListener(new AddAllListener());
	*/


        final JButton removeButton = new JButton(REMOVE_BUTTON_LABEL);
        panel.add(removeButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
        removeButton.addActionListener(new RemoveListener());

        final JButton removeAllButton = new JButton(REMOVE_ALL_BUTTON_LABEL);
        panel.add(removeAllButton, new GridBagConstraints(1, 3, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
        removeAllButton.addActionListener(new RemoveAllListener());


        JLabel lab = new JLabel("Tab Name");

        panel.add(lab, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(tabname, new GridBagConstraints(1, 5, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));

        final JLabel destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);


        destList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    log("Double clicked on.." + destList.getSelectedValue() + "..");
                    TreePath tp = pathhash.get(destList.getSelectedValue() + "");
                    log("treepath=" + tp);
                    InvisibleNode node = (InvisibleNode) tp.getLastPathComponent();
                    node.setVisible(true);
                    ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                    pathhash.remove(destList.getSelectedValue() + "");
                    clearDestinationSelected();

                }
            }
        });


        panel.add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 4, .5,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                EMPTY_INSETS, 0, 0));


        final JButton upButton = new JButton(UP_BUTTON_LABEL);
        upButton.addActionListener(new MoveUpListener());
        final JButton downButton = new JButton(DOWN_BUTTON_LABEL);
        downButton.addActionListener(new MoveDownListener());
        panel.add(upButton, new GridBagConstraints(3, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(downButton, new GridBagConstraints(3, 2, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));


        final JButton saveButton = new JButton(SAVE_BUTTON_LABEL);

        saveButton.addActionListener(new SaveListener());
        panel.add(includeheaders, new GridBagConstraints(3, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(includeseries, new GridBagConstraints(3, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(includeingame, new GridBagConstraints(3, 6, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(includeadded, new GridBagConstraints(3, 7, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(includeextra, new GridBagConstraints(3, 8, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        panel.add(includeprops, new GridBagConstraints(3, 9, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));

        panel.add(saveButton, new GridBagConstraints(3, 10, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
    }

    public void addDestinationElements(GameGroupNode ... newValue) {
        fillListModel(destListModel, newValue);
    }

    private void clearDestinationSelected() {
        List<GameGroupNode> selected = destList.getSelectedValuesList();
        for (int i = selected.size() - 1; i >= 0; --i) {
            destListModel.removeElement(selected.get(i));
        }
        destList.getSelectionModel().clearSelection();
    }

    private void fillListModel(MyListModel2 model, GameGroupNode[] newValues) {
        model.addAll(newValues);
    }
    public void addSourceElements(GameGroupNode[] newValue) {
        fillListModel(sourceListModel, newValue);
    }
    private void clearDestinationAll() {
        Object[] selected = ((MyListModel2) destList.getModel()).toArray();
        for (int i = selected.length - 1; i >= 0; --i) {
            destListModel.removeElement((GameGroupNode)selected[i]);
        }
        destList.getSelectionModel().clearSelection();
    }

    private class MoveUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<GameGroupNode> selected = destList.getSelectedValuesList();
            int[] selectedindices = destList.getSelectedIndices();


            destListModel.moveUp(selected);
            int[] newselectedindices = new int[selectedindices.length];
            for (int i = 0; i < selectedindices.length; i++) {
                int oldindex = selectedindices[i];
                if (oldindex != 0) {
                    newselectedindices[i] = oldindex - 1;
                }
            }
            destList.setSelectedIndices(newselectedindices);
            destList.ensureIndexIsVisible(destList.getSelectedIndex());

        }
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String tab = tabname.getText().trim();
            String tablc = tab.toLowerCase();
            List<GameGroupNode> selectedlist = ((MyListModel2) destList.getModel()).getModelVec();
            if (!AppController.getMainTabPane().isTabNameAvailable(tab) && !editing) {
                JOptionPane.showMessageDialog(null, tab + " name is taken!");
                return;
            } else if (illegalnames.contains(tablc)) // dont think this will execute cus maintabpane handles already!
            {
                JOptionPane.showMessageDialog(null, tab + " name is taken by system! Try something else.");
                return;
            } else if (tab.contains("~") || tab.contains("|") || tab.contains("*") || tab.contains(",") || tab.contains("!") || tab.contains("?")) {
                JOptionPane.showMessageDialog(null, "Illegal character(s) used!");
                return;
            } else if (tab.equals("")) {
                JOptionPane.showMessageDialog(null, "Please Name Your Tab!");
                return;
            } else if (selectedlist.size() == 0) {
                JOptionPane.showMessageDialog(null, "Empty List!");
                return;
            }
            List<String> customvec = new ArrayList<>();
            StringBuilder msstring = new StringBuilder();
            for (GameGroupNode ggn : selectedlist) {
                customvec.add(ggn.getGameGroupHeader());
                msstring.append("|").append(ggn.getSportIdentifyingLeagueId()).append(" ").append(ggn.getDateString());

            }

            msstring.append("*").append(tab).append("*").append(includeheaders.isSelected()).append("*").append(includeseries.isSelected()).append("*").append(includeingame.isSelected()).append("*").append(includeadded.isSelected()).append("*").append(includeextra.isSelected()).append("*").append(includeprops.isSelected());

            log("adding=" + msstring);
            AppController.addCustomTab(tab, msstring.toString());

            Consumer<SportsTabPane> consumer;
            if (!editing) {
                consumer = (tp) -> {
                    int numtabs = tp.getTabCount();
                    SportType st = SportType.createCustomizedSportType(tab, customvec,includeheaders.isSelected(), includeseries.isSelected(), includeingame.isSelected(), includeadded.isSelected(), includeextra.isSelected(),includeprops.isSelected());
                    MainScreen ms = tp.createMainScreen(st);
                    setMainScreenProperties(ms);
                    tp.insertTab(ms.getName(), null, ms, ms.getName(), numtabs - 1);
                    if (tp.getWindowIndex() == activeSportsTabPaneIndex) {
                        tp.setSelectedIndex(numtabs-1);
                    }
                };
            } else {
                //editing existing tab
                consumer = (tp) -> {
                    SportType editedSportType = SportType.findBySportName(tab);
                    editedSportType.setShowheaders(includeheaders.isSelected());
                    editedSportType.setShowseries(includeseries.isSelected());
                    editedSportType.setShowingame(includeingame.isSelected());
                    editedSportType.setShowAdded(includeadded.isSelected());
                    editedSportType.setShowExtra(includeextra.isSelected());
                    editedSportType.setShowProps(includeprops.isSelected());
                    MainScreen editedMs = tp.findMainScreen(editedSportType.getSportName());
                    editedMs.setCustomheaders(customvec);
                    setMainScreenProperties(editedMs);
                    if ( tp.getSelectedComponent() == editedMs) {
                        tp.rebuildMainScreen();
                    }
                };
            }
            SpankyWindow.applyToAllWindows(consumer);
            close();
        }
    }
    private void setMainScreenProperties(MainScreen ms) {
        ms.setShowHeaders(includeheaders.isSelected());
        ms.setShowSeries(includeseries.isSelected());
        ms.setShowIngame(includeingame.isSelected());
        ms.setShowAdded(includeadded.isSelected());
        ms.setShowExtra(includeextra.isSelected());
        ms.setShowProps(includeprops.isSelected());
    }
    private class MoveDownListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<GameGroupNode> selected = destList.getSelectedValuesList();
            int[] selectedindices = destList.getSelectedIndices();


            destListModel.moveDown(selected);
            int[] newselectedindices = new int[selectedindices.length];
            for (int i = 0; i < selectedindices.length; i++) {
                int oldindex = selectedindices[i];
                if (oldindex != (destList.getModel()).getSize()) {
                    newselectedindices[i] = oldindex + 1;
                }
            }
            destList.setSelectedIndices(newselectedindices);
            destList.ensureIndexIsVisible(destList.getMaxSelectionIndex());
        }
    }

    private class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            TreePath[] selPathArray = jtree.getSelectionPaths();

            for (TreePath selPath : selPathArray) {
                if (selPath != null && selPath.getPathCount() == 3) {
                    log("treepath=" + selPath);
                    log("pathcount=" + selPath.getPathCount());
                    log("lastPathComponent=" + selPath.getLastPathComponent());
                    InvisibleNode node = (InvisibleNode) selPath.getLastPathComponent();
                    if (pathhash.get(node.toString()) == null) // not there already
                    {
                        DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode)selPath.getLastPathComponent();
                        addDestinationElements((GameGroupNode) lastPathComponent.getUserObject());

                        log("node=" + node.toString() + "..");
                        pathhash.put(node.toString(), selPath);
                        node.setVisible(false);
                        ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);

                        jtree.setSelectionRow(0);

                    }
                }

            }


        }
    }
    private class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            List<GameGroupNode> selected = destList.getSelectedValuesList();

            for (final GameGroupNode o : selected) {
                InvisibleNode node = findSourceNode(root,o.getGameGroupHeader());
                if ( null != node) {
                    node.setVisible(true);
                    ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                }
            }

            clearDestinationSelected();
        }
    }

    private class RemoveAllListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GameGroupNode[] selected = ((MyListModel2) destList.getModel()).toArray();
            addSourceElements(selected);
            for (final GameGroupNode selectedNode : selected) {
                InvisibleNode sourceNode = findSourceNode(root,selectedNode.getGameGroupHeader());
                sourceNode.setVisible(true);
                ((DefaultTreeModel) jtree.getModel()).nodeChanged(sourceNode);
            }
            clearDestinationAll();
        }
    }
}

class MyListModel2 extends AbstractListModel<GameGroupNode> {

    Vector<GameGroupNode> model;

    public MyListModel2() {
        model = new Vector<>();
    }
    public Vector<GameGroupNode> getModelVec() {
        return model;
    }

    public void moveUp(List<GameGroupNode> values) {

        for (final GameGroupNode value : values) {
            int firstindex = model.indexOf(value);
            if (firstindex == 0) {
                break;
            } else {
                GameGroupNode tempobj = model.get(firstindex - 1);
                model.set(firstindex - 1, value);
                model.set(firstindex, tempobj);
                fireContentsChanged(this, 0, getSize());
            }

        }

    }

    public int getSize() {
        return model.size();
    }
    @Override
    public GameGroupNode getElementAt(int index) {
        return model.get(index);
    }

    public void moveDown(List<GameGroupNode> values) {

        for (int i = values.size() - 1; i >= 0; i--) {
            int firstindex = model.indexOf(values.get(i));
            GameGroupNode selected = values.get(i);
            if (firstindex == model.size() - 1) {
                break;
            } else {
                GameGroupNode tempobj = model.get(firstindex + 1);
                model.set(firstindex + 1, selected);
                model.set(firstindex, tempobj);
                fireContentsChanged(this, 0, getSize());
            }


        }

    }

    public void add(GameGroupNode element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }
    public void addAll(GameGroupNode[] elements) {
        Collection<GameGroupNode> c = Arrays.asList(elements);
        model.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(GameGroupNode element) {
        return model.contains(element);
    }

    public Iterator<GameGroupNode> iterator() {
        return model.iterator();
    }

    public GameGroupNode[] toArray() {
        return model.toArray(new GameGroupNode[0]);
    }

    public boolean removeElement(GameGroupNode element) {
        boolean removed = model.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }


}


class InvisibleNode extends DefaultMutableTreeNode {

    protected boolean isVisible;
    protected String key;

    public InvisibleNode(Object userObject) {
        this(userObject, true, true);
    }

    public InvisibleNode(Object userObject, boolean allowsChildren,
                         boolean isVisible) {
        super(userObject, allowsChildren);
        this.isVisible = isVisible;
    }

    public TreeNode getChildAt(int index, boolean filterIsActive) {
        if (!filterIsActive) {
            return super.getChildAt(index);
        }
        if (children == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        int realIndex = -1;
        int visibleIndex = -1;
        Enumeration<InvisibleNode> e = children.elements();
        while (e.hasMoreElements()) {
            InvisibleNode node =  e.nextElement();
            if (node.isVisible()) {
                visibleIndex++;
            }
            realIndex++;
            if (visibleIndex == index) {
                return (TreeNode) children.elementAt(realIndex);
            }
        }

        throw new ArrayIndexOutOfBoundsException("index unmatched");
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public int getChildCount(boolean filterIsActive) {
        if (!filterIsActive) {
            return super.getChildCount();
        }
        if (children == null) {
            return 0;
        }

        int count = 0;
        Enumeration<InvisibleNode> e = children.elements();
        while (e.hasMoreElements()) {
            InvisibleNode node = e.nextElement();
            if (node.isVisible()) {
                count++;
            }
        }

        return count;
    }
}


class InvisibleTreeModel extends DefaultTreeModel {

    protected boolean filterIsActive;

    public InvisibleTreeModel(TreeNode root) {
        this(root, false);
    }

    public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren) {
        this(root, false, false);
    }

    public InvisibleTreeModel(TreeNode root, boolean asksAllowsChildren,
                              boolean filterIsActive) {
        super(root, asksAllowsChildren);
        this.filterIsActive = filterIsActive;
    }
    public Object getChild(Object parent, int index) {
        if (filterIsActive) {
            if (parent instanceof InvisibleNode) {
                return ((InvisibleNode) parent).getChildAt(index,
                        filterIsActive);
            }
        }
        return ((TreeNode) parent).getChildAt(index);
    }

    public int getChildCount(Object parent) {
        if (filterIsActive) {
            if (parent instanceof InvisibleNode) {
                return ((InvisibleNode) parent).getChildCount(filterIsActive);
            }
        }
        return ((TreeNode) parent).getChildCount();
    }

}