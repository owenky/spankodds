package com.sia.client.ui.comps;

import com.jidesoft.swing.CheckBoxTree;
import com.sia.client.model.Bookie;
import com.sia.client.ui.AppController;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.sia.client.config.Utils.log;

public class BookieTree {

    private final Map<String,String> bookienameidhash = new HashMap<>();
    private CheckBoxTree sportsbooktree;
    
    public CheckBoxTree getSportTreeModel() {
        if ( null == sportsbooktree) {
            DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
            DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);
            try {

                java.util.List<Bookie> newBookiesVec = AppController.getBookiesVec();
                java.util.List<Bookie> hiddencols = AppController.getHiddenCols();
                String allbookies = "";
                for (Bookie b : newBookiesVec) {
                    if (hiddencols.contains(b) || b.getBookie_id() >= 990) {
                        continue;
                    }
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(b.getName());
                    rootbookie.add(child);
                    bookienameidhash.put(b.getName(), String.valueOf(b.getBookie_id()));
                    allbookies = allbookies + b.getBookie_id() + ",";
                }
                if (allbookies.endsWith(",")) {
                    allbookies = allbookies.substring(0, allbookies.length() - 1);
                }

                bookienameidhash.put("All Bookies", allbookies);
                sportsbooktree = new CheckBoxTree(treeModelbookie);
                sportsbooktree.setRootVisible(true);
                sportsbooktree.getCheckBoxTreeSelectionModel().setDigIn(true);
                sportsbooktree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                sportsbooktree.setClickInCheckBoxOnly(false);

                sportsbooktree.setShowsRootHandles(true);
                DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) sportsbooktree.getActualCellRenderer();
                renderer2.setLeafIcon(null);
                renderer2.setOpenIcon(null);
                renderer2.setClosedIcon(null);
            } catch (Exception e) {
                log(e);
            }


//            for (int z = 0; z < sportlist.length; z++) {
//                String thissport = sportlist[z];
//
//                DefaultMutableTreeNode root = new DefaultMutableTreeNode(thissport);
//                DefaultTreeModel treeModel = new DefaultTreeModel(root);
//
//
//                try {
//                    String allsports = "";
//                    List<Sport> sportsVec = AppController.getSportsVec();
//                    for (Sport sport : sportsVec) {
//                        if (sport.getSportname().equals(thissport)) {
//
//                        } else {
//                            continue;
//                        }
//
//
//                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(sport.getLeaguename());
//                        root.add(child);
//                        leaguenameidhash.put(sport.getLeaguename(), "" + sport.getLeague_id());
//
//
//                        allsports = allsports + sport.getLeague_id() + ",";
//
//                    }
//                    if (allsports.endsWith(",")) {
//                        allsports = allsports.substring(0, allsports.length() - 1);
//                    }
//                    leaguenameidhash.put(thissport, allsports);
//                    CheckBoxTree thistree = new CheckBoxTree(treeModel) {
//                        @Override
//                        public Dimension getPreferredScrollableViewportSize() {
//                            return new Dimension(300, 300);
//                        }
//                    };
//                    thistree.setRootVisible(true);
//                    thistree.getCheckBoxTreeSelectionModel().setDigIn(true);
//                    thistree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
//                    thistree.setClickInCheckBoxOnly(false);
//
//                    thistree.setShowsRootHandles(true);
//                    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) thistree.getActualCellRenderer();
//                    renderer.setLeafIcon(null);
//                    renderer.setOpenIcon(null);
//                    renderer.setClosedIcon(null);
//                    trees[z] = thistree;
//
//                    if (z == 0) {
//                        treePanel.add(new JLabel(""));
//                    }
//
//                } catch (Exception e) {
//                    log(e);
//                }
//
//            }
        }
        return sportsbooktree;
    }
}
