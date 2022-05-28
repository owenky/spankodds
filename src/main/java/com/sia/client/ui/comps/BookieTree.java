package com.sia.client.ui.comps;

import com.jidesoft.swing.CheckBoxTree;
import com.sia.client.config.SiaConst;
import com.sia.client.model.Bookie;
import com.sia.client.ui.AppController;

import javax.swing.tree.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class BookieTree implements ActionableOnChanged {

    private static final String valueDelimeter = SiaConst.ColumnDelimiter;
    private final Map<String, String> bookienameidhash = new HashMap<>();
    private CheckBoxTree sportsbooktree;

    public CheckBoxTree getSportTree() {
        if (null == sportsbooktree) {
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
                    Object obj = child.getUserObject();
                    rootbookie.add(child);
                    String bookieIdStr = String.valueOf(b.getBookie_id());
                    bookienameidhash.put(b.getName(),bookieIdStr );
                    allbookies = allbookies + b.getBookie_id() + valueDelimeter;
                }
                if (allbookies.endsWith(valueDelimeter)) {
                    allbookies = allbookies.substring(0, allbookies.length() - valueDelimeter.length());
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
        }
        return sportsbooktree;
    }

    @Override
    public void setValue(Object str) {
        Object [] selectedObjs = String.valueOf(str).split(valueDelimeter);
        TreePath treePath = new TreePath(selectedObjs);
        sportsbooktree.setSelectionPath(treePath);
    }
    @Override
    public Object getValue() {
        if ( null == sportsbooktree.getSelectionPath()) {
            return "";
        }
        Object [] selectedValues = sportsbooktree.getSelectionPath().getPath();
        return Arrays.stream(selectedValues).map(String::valueOf).collect(Collectors.joining());
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        sportsbooktree.addTreeSelectionListener(l);
    }
    @Override
    public String checkError() {
        if ( null == sportsbooktree.getSelectionPath()) {
            return "Please select a bookie.";
        } else {
            return null;
        }
    }
}
