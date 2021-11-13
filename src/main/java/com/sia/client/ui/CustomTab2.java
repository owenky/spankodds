package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.GameDateSorter;
import com.sia.client.model.GameNumSorter;
import com.sia.client.model.Games;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class CustomTab2 extends JPanel {
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    private static final String UP_BUTTON_LABEL = "^";
    private static final String DOWN_BUTTON_LABEL = "v";
    private static final String ADD_BUTTON_LABEL = "Add >";
    private static final String ADD_ALL_BUTTON_LABEL = "Add All >>";
    private static final String REMOVE_BUTTON_LABEL = "< Remove";
    private static final String REMOVE_ALL_BUTTON_LABEL = "<< Remove All";
    private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Available Sports";
    private static final String DEFAULT_DEST_CHOICE_LABEL = "Selected Sports";
    private static final String SAVE_BUTTON_LABEL = "Save";
    boolean editing = false;
    Vector customheaders = new Vector();
    Vector<String> illegalnames = new Vector<String>();
    Hashtable pathhash;
    Hashtable nodehash = new Hashtable();
    JTree jtree;
    Vector gamegroupvec = new Vector();
    Vector gamegroupheadervec = new Vector();
    Vector currentvec = new Vector();
    Vector gamesVec = new Vector();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
    JScrollPane jscrlp = new JScrollPane();
    int tabindex = -1;
    private InvisibleNode root = new InvisibleNode("Games");
    private Hashtable sporthash = new Hashtable();
    private Hashtable mainhash = new Hashtable();
    private JLabel sourceLabel;
    private JList sourceList;
    private MyListModel2 sourceListModel;
    private JList destList;
    private MyListModel2 destListModel;
    private JLabel destLabel;
    private JButton addButton;
    private JButton removeButton;
    private JButton addAllButton;
    private JButton removeAllButton;
    private JButton upButton;
    private JButton downButton;
    private JButton saveButton;
    private JLabel jlab;
    private JCheckBox includeheaders;
    private JCheckBox includeseries;
    private JCheckBox includeingame;
    private JCheckBox includeadded;
    private JCheckBox includeextra;
    private JCheckBox includeprops;
    private JTextField tabname;
    private JFrame f = new JFrame("Custom Tab");

    public CustomTab2() {
        tabname = new JTextField(10);
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
        destListModel = new MyListModel2();
        destList = new JList(destListModel);
        destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        init();
        initScreen();


        f.getContentPane().add(this, BorderLayout.CENTER);
        f.setSize(1000, 400);
        //f.pack();
        f.setVisible(true);


    }

    private void init() {
        init("");
    }

    private void initScreen() {
        initScreen("");
    }

    public void init(String tabnamestr) {


        pathhash = new Hashtable();
        jlab = new JLabel();


        illegalnames.add("football");
        illegalnames.add("basketball");
        illegalnames.add("baseball");
        illegalnames.add("hockey");
        illegalnames.add("fighting");
        illegalnames.add(SiaConst.SoccerStr.toLowerCase());
        illegalnames.add("auto racing");
        illegalnames.add("golf");
        illegalnames.add("tennis");


        InvisibleNode football = new InvisibleNode("Football");
        InvisibleNode basketball = new InvisibleNode("Basketball");
        InvisibleNode baseball = new InvisibleNode("Baseball");
        InvisibleNode hockey = new InvisibleNode("Hockey");
        InvisibleNode fighting = new InvisibleNode("Fighting");
        InvisibleNode soccer = new InvisibleNode(SiaConst.SoccerStr);
        InvisibleNode autoracing = new InvisibleNode("Auto Racing");
        InvisibleNode golf = new InvisibleNode("Golf");
        InvisibleNode tennis = new InvisibleNode("Tennis");


        if (tabnamestr.equalsIgnoreCase("football")) {
            root.add(football);
        } else if (tabnamestr.equalsIgnoreCase("Basketball")) {
            root.add(basketball);
        } else if (tabnamestr.equalsIgnoreCase("Baseball")) {
            root.add(baseball);
        } else if (tabnamestr.equalsIgnoreCase("hockey")) {
            root.add(hockey);
        } else if (tabnamestr.equalsIgnoreCase("fighting")) {
            root.add(fighting);
        } else if (tabnamestr.equalsIgnoreCase(SiaConst.SoccerStr)) {
            root.add(soccer);
        } else if (tabnamestr.equalsIgnoreCase("Auto racing")) {
            root.add(autoracing);
        } else if (tabnamestr.equalsIgnoreCase("golf")) {
            root.add(golf);
        } else if (tabnamestr.equalsIgnoreCase("Tennis")) {
            root.add(tennis);
        } else {
            root.add(football);
            root.add(basketball);
            root.add(baseball);
            root.add(hockey);
            root.add(fighting);
            root.add(soccer);
            root.add(autoracing);
            root.add(golf);
            root.add(tennis);
        }


        InvisibleNode currenttreenode;
        Games allgames = AppController.getGamesVec();
        Vector allsports = AppController.getSportsVec();
        java.util.Date today = new java.util.Date();

        String lastdate = "";
        int lastleagueid = 0;


        try {

            allgames.sort(new GameLeagueSorter().thenComparing(new GameDateSorter().thenComparing(new GameNumSorter())));
            allgames.sort(new GameDateSorter());
            log("sorted by gm date");
            log("sorted by gm num");

        } catch (Exception ex) {
            log(ex);
        }
        Game g = null;
        try {

            for (int k = 0; k < allgames.size(); k++) {

                int gameid = -1;
                g = allgames.getByIndex(k);


                if (g == null) {
                    log("skipping gameid=" + gameid + "...cuz of null game");
                    continue;
                } else {
                    gameid = g.getGame_id();
                }
                if (g.getGamedate() == null) {
                    log("skipping gameid=" + gameid + "...cuz of null game date");
                    continue;
                }

                String gamedate = sdf.format(g.getGamedate());

                int leagueid = g.getLeague_id();

                Sport s = AppController.getSportByLeagueId(leagueid);

                Sport s2;

                if (s == null) {
                    log("skipping " + leagueid + "...cuz of null sport");
                    continue;
                }

                if (s.getSportname().equalsIgnoreCase("Football")) {
                    currenttreenode = football;
                } else if (s.getSportname().equalsIgnoreCase("Basketball")) {
                    currenttreenode = basketball;
                } else if (s.getSportname().equalsIgnoreCase("Baseball")) {
                    currenttreenode = baseball;
                } else if (s.getSportname().equalsIgnoreCase("Hockey")) {
                    currenttreenode = hockey;
                } else if (s.getSportname().equalsIgnoreCase("Fighting")) {
                    currenttreenode = fighting;
                } else if (s.getSportname().equalsIgnoreCase(SiaConst.SoccerStr)) {
                    currenttreenode = soccer;
                } else if (s.getSportname().equalsIgnoreCase("Auto Racing")) {
                    currenttreenode = autoracing;
                } else if (s.getSportname().equalsIgnoreCase("Golf")) {
                    currenttreenode = golf;
                } else if (s.getSportname().equalsIgnoreCase("Tennis")) {
                    currenttreenode = tennis;
                } else {
                    log("should never enter gameid=" + g.getGame_id());
                    currenttreenode = football;

                }


                if (leagueid == SiaConst.SoccerLeagueId) // soccer need to look at subleagueid
                {
                    leagueid = g.getSubleague_id();
                    s2 = AppController.getSportByLeagueId(leagueid);
                } else {
                    s2 = s;
                }

                if (s2 == null) {
                    log("skipping " + leagueid + "...cuz of null sport2");
                    continue;
                }
                String description = g.getDescription();
                if (description == null || description.equalsIgnoreCase("null")) {
                    description = "";
                }

                if (g.getStatus() == null) {
                    g.setStatus("");
                }
                if (g.getTimeremaining() == null) {
                    g.setTimeremaining("");
                }


                if (g.getStatus().equalsIgnoreCase("Tie") || g.getStatus().equalsIgnoreCase("Cncld") || g.getStatus().equalsIgnoreCase("Poned") || g.getStatus().equalsIgnoreCase(SiaConst.FinalStr)
                        || g.getStatus().equalsIgnoreCase("Win") || (g.getTimeremaining().equalsIgnoreCase("Win"))

                ) {

                    //finalgames.add(g);
                    //log("skipping "+g.getGame_id());
                    continue;

                }
                // adding inprogress games to custyom tabs!
                //else if(!g.getStatus().equalsIgnoreCase("NULL") && !g.getStatus().equals(""))
                //{
                //	continue;
                //inprogressgames.add(g);
                //}
                else if (g.isSeriesprice()) {
                    continue;
                    //seriesgames.add(g);
                } else if (g.isForprop()) {
                    continue;
                    //seriesgames.add(g);
                } else if (g.isIngame() || description.indexOf("In-Game") != -1) {
                    continue;
                    //ingamegames.add(g);
                } else if (!lastdate.equals(gamedate) || lastleagueid != leagueid) // new date or new league!
                {
                    if (lastleagueid <= 4 || leagueid <= 4) {
                        //log("new!...lastdate="+lastdate+"..gamedate="+g.getGamedate()+".."+lastleagueid+"..new="+leagueid);
                    }
                    lastdate = gamedate;
                    lastleagueid = leagueid;
                    gamegroupheadervec.add(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()));

                    //	currenttreenode.add(new InvisibleNode(s2.getLeaguename()+" "+sdf2.format(g.getGamedate())));
                    sporthash.put(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()), currenttreenode);
                    mainhash.put(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()), leagueid + " " + sdf2.format(g.getGamedate()));
                    Vector v2 = new Vector();
                    //v2.add(gameid);
                    v2.add(g);
                    gamegroupvec.add(v2);
                    currentvec = v2;
                } else // same date
                {
                    //currentvec.add(gameid);
                    currentvec.add(g);

                }


            }
        } catch (Exception ex) {
            log("game=" + g.getGame_id() + "...leagueid=" + g.getLeague_id() + "..subleagueid=" + g.getSubleague_id());
            log(ex);
        }
        for (int j = 0; j < gamegroupvec.size(); j++) {
            Vector thisvec = (Vector) gamegroupvec.elementAt(j);
            int numgames = thisvec.size();
            String title = "" + gamegroupheadervec.elementAt(j);
            InvisibleNode currenttreenode2 = (InvisibleNode) sporthash.get(title);

            String value = (String) mainhash.get(title);


            if (numgames == 1) {
                title = title + " (" + numgames + " Event)";
            } else if (numgames > 1) {
                title = title + " (" + numgames + " Events)";
            }

            InvisibleNode childnode = new InvisibleNode(title);


            if (customheaders.contains(value)) {
                childnode.setVisible(false);
                nodehash.put(value, childnode);
                log("adding value=" + value + "..." + childnode.toString());
                //addDestinationElements(new Object[] {childnode});
            }

            currenttreenode2.add(childnode);

        }

        for (int z = 0; z < customheaders.size(); z++) {
            String key = (String) customheaders.elementAt(z);
            log("key=" + key);
            InvisibleNode node = (InvisibleNode) nodehash.get(key);
            if (node != null) {
                log(" not null");
                pathhash.put(node.toString(), new TreePath(node.getPath()));
                node.setVisible(false);
                addDestinationElements(new Object[]{node});
            } else {
                log(" is null");
            }
        }


        InvisibleTreeModel ml = new InvisibleTreeModel(root);
        //ml.activateFilter(true);

        //jtree = new JTree(root);
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


        TreeSelectionModel tsm = jtree.getSelectionModel();
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
        jtree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                // Get the path to the selection.
                TreePath tp = tse.getPath();

                // Display the selected node.
                jlab.setText("Selection event: " +
                        tp.getLastPathComponent());
            }
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

            // Empty implementations of the remaing TreeModelEvent
            // methods. Implement these if your application
            // needs to handle these actions.
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


                            addDestinationElements(new Object[]{selPath.getLastPathComponent()});

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

    private void initScreen(String tabnamestr) {

        setBorder(BorderFactory.createEtchedBorder());


        setLayout(new GridBagLayout());
        sourceLabel = new JLabel(DEFAULT_SOURCE_CHOICE_LABEL);
        sourceListModel = new MyListModel2();
        sourceList = new JList(sourceListModel);


        add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(jscrlp, new GridBagConstraints(0, 1, 1, 4, .5,
                1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                EMPTY_INSETS, 0, 0));

        addButton = new JButton(ADD_BUTTON_LABEL);
        add(addButton, new GridBagConstraints(1, 1, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        addButton.addActionListener(new AddListener());


        addAllButton = new JButton(ADD_ALL_BUTTON_LABEL);
	/*
    add(addAllButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    addAllButton.addActionListener(new AddAllListener());
	*/


        removeButton = new JButton(REMOVE_BUTTON_LABEL);
        add(removeButton, new GridBagConstraints(1, 2, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
        removeButton.addActionListener(new RemoveListener());

        removeAllButton = new JButton(REMOVE_ALL_BUTTON_LABEL);
        add(removeAllButton, new GridBagConstraints(1, 3, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));
        removeAllButton.addActionListener(new RemoveAllListener());


        JLabel lab = new JLabel("Tab Name");

        add(lab, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(tabname, new GridBagConstraints(1, 5, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));

        destLabel = new JLabel(DEFAULT_DEST_CHOICE_LABEL);


        destList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    log("Double clicked on.." + destList.getSelectedValue() + "..");
                    TreePath tp = (TreePath) pathhash.get(destList.getSelectedValue() + "");
                    log("treepath=" + tp);
                    InvisibleNode node = (InvisibleNode) tp.getLastPathComponent();
                    node.setVisible(true);
                    ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                    pathhash.remove(destList.getSelectedValue() + "");
                    clearDestinationSelected();

                }
            }
        });


        add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 4, .5,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                EMPTY_INSETS, 0, 0));


        upButton = new JButton(UP_BUTTON_LABEL);
        upButton.addActionListener(new MoveUpListener());
        downButton = new JButton(DOWN_BUTTON_LABEL);
        downButton.addActionListener(new MoveDownListener());
        add(upButton, new GridBagConstraints(3, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(downButton, new GridBagConstraints(3, 2, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));


        saveButton = new JButton(SAVE_BUTTON_LABEL);

        saveButton.addActionListener(new SaveListener());
        add(includeheaders, new GridBagConstraints(3, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(includeseries, new GridBagConstraints(3, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(includeingame, new GridBagConstraints(3, 6, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(includeadded, new GridBagConstraints(3, 7, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(includeextra, new GridBagConstraints(3, 8, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));
        add(includeprops, new GridBagConstraints(3, 9, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                EMPTY_INSETS, 0, 0));

        add(saveButton, new GridBagConstraints(3, 10, 1, 1, 0, .1,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                0, 5, 0, 5), 0, 0));


    }

    public void addDestinationElements(Object newValue[]) {
        fillListModel(destListModel, newValue);
    }

    private void clearDestinationSelected() {
        Object selected[] = destList.getSelectedValues();
        for (int i = selected.length - 1; i >= 0; --i) {
            destListModel.removeElement(selected[i]);
        }
        destList.getSelectionModel().clearSelection();
    }

    private void fillListModel(MyListModel2 model, Object newValues[]) {
        model.addAll(newValues);
    }

    public CustomTab2(String tabnamestr, int tabindex) {
        this.tabindex = tabindex;

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
            msinfo = "";
            editing = false;
        } else {
            editing = true;
            String[] items = msinfo.split("\\*");

            for (int j = 0; j < items.length; j++) {
                log(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (int k = 0; k < headers.length; k++) {
                        String header = headers[k];
                        if (header.equals("")) {
                            continue;
                        } else {
                            customheaders.add(header);
                            log("adding header=" + header);
                        }
                    }
                } else if (j == 1) {
                    tabname.setText(items[j]);
                    //tabname.setEditable(false);
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
        destList = new JList(destListModel);
        destList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);


        init(tabnamestr);
        initScreen(tabnamestr);


        f.getContentPane().add(this, BorderLayout.CENTER);
        f.setSize(1000, 400);
        //f.pack();
        f.setVisible(true);
        //	revalidate();
        //	repaint();

    }

    private void setNodeVisible(final JTree tree, boolean isVisible) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreePath[] path = tree.getSelectionPaths();
        InvisibleNode node = null;
        for (int i = 0; i < path.length; i++) {
            node = (InvisibleNode) path[i].getLastPathComponent();
            if (!(node == model.getRoot())) {
                node.setVisible(isVisible);
            } else {
                log("refused: root node");
            }
        }
        if (path.length == 1) {
            model.nodeChanged(node);
        } else {
            model.reload();
        }
    }

    public String getSourceChoicesTitle() {
        return sourceLabel.getText();
    }

    public void setSourceChoicesTitle(String newValue) {
        sourceLabel.setText(newValue);
    }

    public String getDestinationChoicesTitle() {
        return destLabel.getText();
    }

    public void setDestinationChoicesTitle(String newValue) {
        destLabel.setText(newValue);
    }

    public void clearDestinationListModel() {
        destListModel.clear();
    }

    public void setSourceElements(ListModel newValue) {
        clearSourceListModel();
        addSourceElements(newValue);
    }

    public void clearSourceListModel() {
        sourceListModel.clear();
    }

    public void addSourceElements(ListModel newValue) {
        fillListModel(sourceListModel, newValue);
    }

    private void fillListModel(MyListModel2 model, ListModel newValues) {
        int size = newValues.getSize();
        for (int i = 0; i < size; i++) {
            model.add(newValues.getElementAt(i));
            log("adding .." + newValues.getElementAt(i));
        }
    }

    public void addDestinationElements(ListModel newValue) {
        fillListModel(destListModel, newValue);
    }

    public void setSourceElements(Object newValue[]) {
        clearSourceListModel();
        addSourceElements(newValue);
    }

    public void addSourceElements(Object newValue[]) {
        fillListModel(sourceListModel, newValue);
    }

    public Iterator sourceIterator() {
        return sourceListModel.iterator();
    }

    public Iterator destinationIterator() {
        return destListModel.iterator();
    }

    public ListCellRenderer getSourceCellRenderer() {
        return sourceList.getCellRenderer();
    }

    public void setSourceCellRenderer(ListCellRenderer newValue) {
        sourceList.setCellRenderer(newValue);
    }

    public ListCellRenderer getDestinationCellRenderer() {
        return destList.getCellRenderer();
    }

    public void setDestinationCellRenderer(ListCellRenderer newValue) {
        destList.setCellRenderer(newValue);
    }

    public int getVisibleRowCount() {
        return sourceList.getVisibleRowCount();
    }

    public void setVisibleRowCount(int newValue) {
        sourceList.setVisibleRowCount(newValue);
        destList.setVisibleRowCount(newValue);
    }

    public Color getSelectionBackground() {
        return sourceList.getSelectionBackground();
    }

    public void setSelectionBackground(Color newValue) {
        sourceList.setSelectionBackground(newValue);
        destList.setSelectionBackground(newValue);
    }

    public Color getSelectionForeground() {
        return sourceList.getSelectionForeground();
    }

    public void setSelectionForeground(Color newValue) {
        sourceList.setSelectionForeground(newValue);
        destList.setSelectionForeground(newValue);
    }

    private void clearSourceSelected() {
        Object selected[] = sourceList.getSelectedValues();
        for (int i = selected.length - 1; i >= 0; --i) {
            sourceListModel.removeElement(selected[i]);
        }
        sourceList.getSelectionModel().clearSelection();
    }

    private void clearSourceAll() {
        Object selected[] = ((MyListModel2) sourceList.getModel()).toArray();
        for (int i = selected.length - 1; i >= 0; --i) {
            sourceListModel.removeElement(selected[i]);
        }
        sourceList.getSelectionModel().clearSelection();
    }

    private void clearDestinationAll() {
        Object selected[] = ((MyListModel2) destList.getModel()).toArray();
        for (int i = selected.length - 1; i >= 0; --i) {
            destListModel.removeElement(selected[i]);
        }
        destList.getSelectionModel().clearSelection();
    }

    private class MoveUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected[] = destList.getSelectedValues();
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
            Vector selectedlist = ((MyListModel2) destList.getModel()).getModelVec();
            if (!AppController.getMainTabPane().isTabNameAvailable(tab) && !editing) {
                JOptionPane.showMessageDialog(null, tab + " name is taken!");
                return;
            } else if (illegalnames.contains(tablc)) // dont think this will execute cus maintabpane handles already!
            {
                JOptionPane.showMessageDialog(null, tab + " name is taken by system! Try something else.");
                return;
            } else if (tab.indexOf("~") != -1 || tab.indexOf("|") != -1 || tab.indexOf("*") != -1 || tab.indexOf(",") != -1 || tab.indexOf("!") != -1 || tab.indexOf("?") != -1) {
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
            for (Object o : selectedlist) {
                String s = o.toString();
                s = s.substring(0, s.indexOf("("));
                s = s.trim();
                customvec.add(s);
                msstring.append("|").append(mainhash.get(s));

            }

            msstring.append("*").append(tab).append("*").append(includeheaders.isSelected()).append("*").append(includeseries.isSelected()).append("*").append(includeingame.isSelected()).append("*").append(includeadded.isSelected()).append("*").append(includeextra.isSelected()).append("*").append(includeprops.isSelected());

            log("adding=" + msstring);
            AppController.addCustomTab(tab, msstring.toString());

            if (!editing) {
                checkAndRunInEDT(() -> {
                    Consumer<SportsTabPane> consumer = (tp)-> {
                        int numtabs = tp.getTabCount();
                        SportType st = SportType.createCustomizedSportType(tab,customvec);
                        MainScreen ms = tp.createMainScreen(st, customvec);
                        ms.setShowHeaders(includeheaders.isSelected());
                        ms.setShowSeries(includeseries.isSelected());
                        ms.setShowIngame(includeingame.isSelected());
                        ms.setShowAdded(includeadded.isSelected());
                        ms.setShowExtra(includeextra.isSelected());
                        ms.setShowProps(includeprops.isSelected());
                        tp.insertTab(ms.getName(), null, ms, ms.getName(), numtabs-1);
                    };
                    SpankyWindow.applyToAllWindows(consumer);
                });
            } else {
                checkAndRunInEDT(() -> {
                    Consumer<SportsTabPane> consumer = (tp)-> {
                        MainScreen oldms = (MainScreen) tp.getComponentAt(tabindex);
                        oldms.destroyMe();
                        SportType st = SportType.findBySportName(tab);
                        MainScreen ms =tp.createMainScreen(st, customvec);
                        ms.setShowHeaders(includeheaders.isSelected());
                        ms.setShowSeries(includeseries.isSelected());
                        ms.setShowIngame(includeingame.isSelected());
                        ms.setShowAdded(includeadded.isSelected());
                        ms.setShowExtra(includeextra.isSelected());
                        ms.setShowProps(includeprops.isSelected());
                        tp.setComponentAt(tabindex, ms);
                        tp.refreshCurrentTab();
                    };
                    SpankyWindow.applyToAllWindows(consumer);
                });

            }
            f.dispose();


        }
    }


    private class MoveDownListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object[] selected = destList.getSelectedValues();
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


                        addDestinationElements(new Object[]{selPath.getLastPathComponent()});

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


    private class AddAllListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected[] = ((MyListModel2) sourceList.getModel()).toArray();
            addDestinationElements(selected);
            clearSourceAll();
        }
    }

    private class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected[] = destList.getSelectedValues();

            for (int i = 0; i < selected.length; i++) {

                TreePath tp = (TreePath) pathhash.get(selected[i] + "");
                log("treepath=" + tp);
                InvisibleNode node = (InvisibleNode) tp.getLastPathComponent();
                node.setVisible(true);
                ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                pathhash.remove(selected[i] + "");
            }


            clearDestinationSelected();
        }
    }

    private class RemoveAllListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected[] = ((MyListModel2) destList.getModel()).toArray();
            addSourceElements(selected);
            for (int i = 0; i < selected.length; i++) {

                TreePath tp = (TreePath) pathhash.get(selected[i] + "");
                log("treepath=" + tp);
                InvisibleNode node = (InvisibleNode) tp.getLastPathComponent();
                node.setVisible(true);
                ((DefaultTreeModel) jtree.getModel()).nodeChanged(node);
                pathhash.remove(selected[i] + "");
            }
            clearDestinationAll();
        }
    }


}

class MyListModel2 extends AbstractListModel {

    Vector model;

    public MyListModel2() {
        model = new Vector();
    }

    public Vector getModelVec() {
        return model;
    }

    public void moveUp(Object[] values) {

        for (int i = 0; i < values.length; i++) {
            int firstindex = model.indexOf(values[i]);
            Object selected = values[i];
            if (firstindex == 0) {
                break;
            } else {
                Object tempobj = model.get(firstindex - 1);
                model.set(firstindex - 1, selected);
                model.set(firstindex, tempobj);
                fireContentsChanged(this, 0, getSize());
            }

        }

    }

    public int getSize() {
        return model.size();
    }

    public Object getElementAt(int index) {
        return model.toArray()[index];
    }

    public void moveDown(Object[] values) {

        //  for(int i=0; i <values.length; i++)
        for (int i = values.length - 1; i >= 0; i--) {
            int firstindex = model.indexOf(values[i]);
            Object selected = values[i];
            if (firstindex == model.size() - 1) {
                break;
            } else {
                Object tempobj = model.get(firstindex + 1);
                model.set(firstindex + 1, selected);
                model.set(firstindex, tempobj);
                fireContentsChanged(this, 0, getSize());
            }


        }

    }

    public void add(Object element) {
        if (model.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void addAll(Object elements[]) {
        Collection c = Arrays.asList(elements);
        model.addAll(c);
        fireContentsChanged(this, 0, getSize());
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(Object element) {
        return model.contains(element);
    }


    public Iterator iterator() {
        return model.iterator();
    }


    public Object[] toArray() {
        return model.toArray();
    }

    public boolean removeElement(Object element) {
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

    public InvisibleNode() {
        this(null);
    }

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
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            InvisibleNode node = (InvisibleNode) e.nextElement();
            if (node.isVisible()) {
                visibleIndex++;
            }
            realIndex++;
            if (visibleIndex == index) {
                return (TreeNode) children.elementAt(realIndex);
            }
        }

        throw new ArrayIndexOutOfBoundsException("index unmatched");
        //return (TreeNode)children.elementAt(index);
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
        Enumeration e = children.elements();
        while (e.hasMoreElements()) {
            InvisibleNode node = (InvisibleNode) e.nextElement();
            if (node.isVisible()) {
                count++;
            }
        }

        return count;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void activateFilter(boolean newValue) {
        filterIsActive = newValue;
    }

    public boolean isActivatedFilter() {
        return filterIsActive;
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