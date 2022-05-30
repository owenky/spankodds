package com.sia.client.ui.comps;

import com.jidesoft.swing.CheckBoxTree;
import com.sia.client.config.SiaConst;
import com.sia.client.model.Bookie;
import com.sia.client.ui.AppController;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class BookieTree implements ActionableOnChanged {

    private static final String valueDelimeter = SiaConst.ColumnDelimiter;
    private final Map<String, String> bookienameidhash = new HashMap<>();
    private final Map<CompValueChangedListener,TreeSelectionListener> compoundListenerMap = new HashMap<>();
    private final CheckBoxTree sportsbooktree;
    private java.util.List<String> selections;

    public BookieTree() {
        sportsbooktree = createCheckBoxTree();
    }

    private CheckBoxTree createCheckBoxTree() {
        DefaultMutableTreeNode rootbookie = new DefaultMutableTreeNode("All Bookies");
        DefaultTreeModel treeModelbookie = new DefaultTreeModel(rootbookie);
        CheckBoxTree sportsbooktree_;
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
                bookienameidhash.put(b.getName(), bookieIdStr);
                allbookies = allbookies + b.getBookie_id() + valueDelimeter;
            }
            if (allbookies.endsWith(valueDelimeter)) {
                allbookies = allbookies.substring(0, allbookies.length() - valueDelimeter.length());
            }

            bookienameidhash.put("All Bookies", allbookies);
            sportsbooktree_ = new CheckBoxTree(treeModelbookie);
            sportsbooktree_.setRootVisible(true);
            sportsbooktree_.getCheckBoxTreeSelectionModel().setDigIn(true);
            sportsbooktree_.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            sportsbooktree_.setClickInCheckBoxOnly(false);

            sportsbooktree_.setShowsRootHandles(true);
            DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) sportsbooktree_.getActualCellRenderer();
            renderer2.setLeafIcon(null);
            renderer2.setOpenIcon(null);
            renderer2.setClosedIcon(null);
        } catch (Exception e) {
            log(e);
            sportsbooktree_ = null;
        }

        return sportsbooktree_;
    }

    public CheckBoxTree getSportTree() {
        return sportsbooktree;
    }

    @Override
    public void setValue(Object obj) {
        if ( null == obj || "".equals(obj)) {
            selections = new ArrayList<>();
        } else {
            selections = splitAndSort(String.valueOf(obj));
        }
        updateTreeSelectionsFromSelectionValues();
    }

    @Override
    public Object getValue() {
        if ( null == selections || 0 == selections.size()){
            return "";
        }
        return selections.stream().collect(Collectors.joining(valueDelimeter));
    }

    @Override
    public void addListener(CompValueChangedListener l) {
        TreeSelectionListener compoundListener = (e) -> {
            updateSelections(e);
            l.valueChanged(e);
        };
        compoundListenerMap.put(l,compoundListener);
        sportsbooktree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(compoundListener);
    }

    @Override
    public void rmListener(CompValueChangedListener l) {
        TreeSelectionListener compoundListener = compoundListenerMap.remove(l);
        if ( null != compoundListener) {
            sportsbooktree.getCheckBoxTreeSelectionModel().removeTreeSelectionListener(compoundListener);
        }
    }

    @Override
    public String checkError() {
        if (null == selections || 0 == selections.size()) {
            return "Please select a bookie.";
        } else {
            return null;
        }
    }
    private void updateTreeSelectionsFromSelectionValues() {
        java.util.List<TreePath> selectedPath = new ArrayList<>();
        for(int i=0;i<sportsbooktree.getRowCount();i++) {
            TreePath path = sportsbooktree.getPathForRow(i);
            if ( selections.contains(String.valueOf(path.getLastPathComponent()))) {
                selectedPath.add(path);
            }
        }
        sportsbooktree.getCheckBoxTreeSelectionModel().setSelectionPaths(selectedPath.toArray(new TreePath[0]));
    }
    private void updateSelections(TreeSelectionEvent e) {
        TreePath[] paths = e.getPaths();
        String leaf = String.valueOf(paths[0].getLastPathComponent());
        if ( e.isAddedPath()) {
            selections.add(leaf);
            selections = selections.stream().distinct().sorted().collect(Collectors.toList());
        } else {
            selections.remove(leaf);
        }

    }
    private static java.util.List<String> splitAndSort(String str) {
        String [] values = str.split(valueDelimeter);
        return Arrays.stream(values).sorted().collect(Collectors.toList());
    }
}
