package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.Games;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class CustomTab extends JPanel {

    JLabel jlab;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");

    public CustomTab() {
        // Create a new JFrame container.
        Vector gamegroupvec = new Vector();
        Vector gamegroupheadervec = new Vector();
        Vector currentvec = new Vector();
        Vector gamesVec = new Vector();
        JFrame jfrm = new JFrame("Custom Tab");

        // Use the default border layout manager.

        // Give the frame an initial size.
        jfrm.setSize(600, 600);

        // Terminate the program when the user closes the application.
        //jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label that will dislay the tree selection.
        jlab = new JLabel();

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Games");

//        DefaultMutableTreeNode football = new DefaultMutableTreeNode(SportName.Football);
//        DefaultMutableTreeNode basketball = new DefaultMutableTreeNode(SportName.Basketball);
//        DefaultMutableTreeNode baseball = new DefaultMutableTreeNode(SportName.Baseball);
//        DefaultMutableTreeNode hockey = new DefaultMutableTreeNode(SportName.Hockey);
//        DefaultMutableTreeNode fighting = new DefaultMutableTreeNode(SportName.Fighting);
//        DefaultMutableTreeNode soccer = new DefaultMutableTreeNode(SportName.Soccer);
//        DefaultMutableTreeNode autoracing = new DefaultMutableTreeNode(SportName.Auto_Racing);
//        DefaultMutableTreeNode golf = new DefaultMutableTreeNode(SportName.Golf);
//        DefaultMutableTreeNode tennis = new DefaultMutableTreeNode(SportName.Tennis);
        SportType [] predefinedSportTypes = SportType.getPreDefinedSports();
        for(SportType st: predefinedSportTypes) {
            root.add(new DefaultMutableTreeNode(st.getSportName()));
        }
//
//
//        root.add(football);
//        root.add(basketball);
//        root.add(baseball);
//        root.add(hockey);
//        root.add(fighting);
//        root.add(soccer);
//        root.add(autoracing);
//        root.add(golf);
//        root.add(tennis);

        Games allgames = AppController.getGamesVec();
        String lastdate = null;
        int lastleagueid = 0;

        Iterator<Game> ite = allgames.iterator();
        while(ite.hasNext()) {

            int gameid = -1;
            Game g = ite.next();


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


            //log(s.getSportname());
            if (s == null) {
                log("skipping " + leagueid + "...cuz of null sport");
                continue;
            }
            int childCount = root.getChildCount();
            DefaultMutableTreeNode currenttreenode = (DefaultMutableTreeNode)root.getChildAt(0);
            for(int i=0;i<childCount;i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
                if ( s.getSportname().equals(node.getUserObject())) {
                    currenttreenode = node;
                    break;
                }
            }
//            if (s.getSportname().equalsIgnoreCase("Football")) {
//                currenttreenode = football;
//            } else if (s.getSportname().equalsIgnoreCase("Basketball")) {
//                currenttreenode = basketball;
//            } else if (s.getSportname().equalsIgnoreCase("Baseball")) {
//                currenttreenode = baseball;
//            } else if (s.getSportname().equalsIgnoreCase("Hockey")) {
//                currenttreenode = hockey;
//            } else if (s.getSportname().equalsIgnoreCase("Fighting")) {
//                currenttreenode = fighting;
//            } else if (s.getSportname().equalsIgnoreCase(SiaConst.SoccerStr)) {
//                currenttreenode = soccer;
//            } else if (s.getSportname().equalsIgnoreCase("Auto Racing")) {
//                currenttreenode = autoracing;
//            } else if (s.getSportname().equalsIgnoreCase("Golf")) {
//                currenttreenode = golf;
//            } else if (s.getSportname().equalsIgnoreCase("Tennis")) {
//                currenttreenode = tennis;
//            } else {
//                log("should never enter gameid=" + g.getGame_id());
//                currenttreenode = football;
//
//            }


            if (leagueid == 9) // soccer need to look at subleagueid
            {
                leagueid = g.getSubleague_id();

                s2 = AppController.getSportByLeagueId(leagueid);

            } else {
                s2 = s;
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

            } else if (!g.getStatus().equalsIgnoreCase("NULL") && !g.getStatus().equals("")) {
                continue;
                //inprogressgames.add(g);
            } else if (g.isSeriesprice()) {
                continue;
                //seriesgames.add(g);
            } else if (g.isIngame() || description.indexOf("In-Game") != -1) {
                continue;
                //ingamegames.add(g);
            } else if (lastdate == null) // new date
            {
                gamegroupheadervec.add(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()));
                currenttreenode.add(new DefaultMutableTreeNode(s2.getLeaguename() + " " + sdf2.format(g.getGamedate())));
                lastdate = gamedate;
                lastleagueid = leagueid;
                Vector v = new Vector();
                gamegroupvec.add(v);
                //v.add(gameid);
                v.add(g);
                currentvec = v;
            } else if (!lastdate.equals(gamedate) || lastleagueid != leagueid) // new date or new league!
            {
                //log("newdate!...lastdate="+lastdate+"..gamedate="+g.getGamedate());
                lastdate = gamedate;
                lastleagueid = leagueid;
                gamegroupheadervec.add(s2.getLeaguename() + " " + sdf2.format(g.getGamedate()));
                currenttreenode.add(new DefaultMutableTreeNode(s2.getLeaguename() + " " + sdf2.format(g.getGamedate())));
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


        JTree jtree = new JTree(root);


        jtree.setEditable(false);
        jtree.setRootVisible(false);


        TreeSelectionModel tsm = jtree.getSelectionModel();
        tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


        JScrollPane jscrlp = new JScrollPane(jtree);

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

        // Add the tree and label to the content pane.
        jfrm.getContentPane().add(jscrlp, BorderLayout.CENTER);
        jfrm.getContentPane().add(jlab, BorderLayout.SOUTH);

        // Display the frame.
        jfrm.setVisible(true);
    }
}