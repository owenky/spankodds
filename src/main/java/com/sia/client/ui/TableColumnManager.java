package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Bookie;
import com.sia.client.model.BookieManager;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.checkAndRunInEDT;

/**
 * The TableColumnManager can be used to manage TableColumns. It will give the
 * user the ability to hide columns and then reshow them in their last viewed
 * position. This functionality is supported by a popup menu added to the
 * table header of the table. The TableColumnModel is still used to control
 * the view for the table. The manager will inovoke the appropriate methods
 * of the TableColumnModel to hide/show columns as required.
 */
public class TableColumnManager implements java.awt.event.MouseListener, javax.swing.event.TableColumnModelListener {
//    private final JTable table;
//    private final SportsTabPane stp;
//    private TableColumnModel tcm;
//    private String fixed;
//    private List<TableColumn> allColumns;

    public static TableColumnManager instance() {
        return LazyInitHolder.instance;
    }
    private TableColumnManager() {

    }
    public void installListeners(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        tcm.removeColumnModelListener(this);
        tcm.addColumnModelListener(this);
        table.getTableHeader().removeMouseListener(this);
        table.getTableHeader().addMouseListener(this);

        //  Keep a duplicate TableColumns for managing hidden TableColumns
//        int count = tcm.getColumnCount();
//        allColumns = new ArrayList<>(count);
//
//        for (int i = 0; i < count; i++) {
//            allColumns.add(tcm.getColumn(i));
//        }
    }
//    private TableColumnManager(SportsTabPane stp,JTable table, String fixed) {
//
//        this(stp,table, true, fixed);
//    }
//    private TableColumnManager(SportsTabPane stp,JTable table, boolean menuPopup, String fixed) {
//        this.table = table;
//        this.fixed = fixed;
//        this.stp = stp;
//        setMenuPopup(menuPopup);
//
//        table.addPropertyChangeListener(this);
//        reset();
//    }
//
//    /**
//     * Reset the TableColumnManager to only manage the TableColumns that are
//     * currently visible in the table.
//     * <p>
//     * Generally this method should only be invoked by the TableColumnManager
//     * when the TableModel of the table is changed.
//     */
//    public void reset() {
//        table.getColumnModel().removeColumnModelListener(this);
//        tcm = table.getColumnModel();
//        tcm.addColumnModelListener(this);
//
//        //  Keep a duplicate TableColumns for managing hidden TableColumns
//
//        int count = tcm.getColumnCount();
//        allColumns = new ArrayList<>(count);
//
//        for (int i = 0; i < count; i++) {
//            allColumns.add(tcm.getColumn(i));
//        }
//    }
//    /**
//     * Add/remove support for a popup menu to the table header. The popup
//     * menu will give the user control over which columns are visible.
//     *
//     * @param menuPopup when true support for displaying a popup menu is added
//     *                  otherwise the popup menu is removed.
//     */
//    public void setMenuPopup(boolean menuPopup) {
//        table.getTableHeader().removeMouseListener(this);
//
//        if (menuPopup) {
//            table.getTableHeader().addMouseListener(this);
//        }
//
////        this.menuPopup = menuPopup;
//    }
//
//    /**
//     * Hide a column from view in the table.
//     *
//     * @param modelColumn the column index from the TableModel
//     *                    of the column to be removed
//     */
//    public void hideColumn(int modelColumn) {
//        int viewColumn = table.convertColumnIndexToView(modelColumn);
//
//        if (viewColumn != -1) {
//            TableColumn column = tcm.getColumn(viewColumn);
//            hideColumn(column);
//        }
//    }
//
//    /**
//     * Hide a column from view in the table.
//     *
//     * @param column the TableColumn to be removed from the
//     *               TableColumnModel of the table
//     */
//    public void hideColumn(TableColumn column) {
//        if (tcm.getColumnCount() == 1) {
//            return;
//        }
//
//        //  Ignore changes to the TableColumnModel made by the TableColumnManager
//
//        tcm.removeColumnModelListener(this);
//        tcm.removeColumn(column);
//        tcm.addColumnModelListener(this);
//    }
//
//    /**
//     * Hide a column from view in the table.
//     *
//     * @param columnName the column name of the column to be removed
//     */
//    public void hideColumn(Object columnName) {
//        //log("in hide column");
//        if (columnName == null) {
//            return;
//        }
//
//        for (int i = 0; i < tcm.getColumnCount(); i++) {
//            TableColumn column = tcm.getColumn(i);
//            //log("columnname is.."+columnName+"..header value is.."+column.getHeaderValue()+"..");
//            if (columnName.toString().equals(column.getHeaderValue() + "")) {
//                hideColumn(column);
//                break;
//            }
//        }
//    }
//
//    /**
//     * Show a hidden column in the table.
//     *
//     * @param modelColumn the column index from the TableModel
//     *                    of the column to be added
//     */
//    public void showColumn(int modelColumn) {
//        for (TableColumn column : allColumns) {
//            if (column.getModelIndex() == modelColumn) {
//                showColumn(column);
//                break;
//            }
//        }
//    }
//
//    /**
//     * Show a hidden column in the table. The column will be positioned
//     * at its proper place in the view of the table.
//     *
//     * @param column the TableColumn to be shown.
//     */
//    private void showColumn(TableColumn column) {
//        //  Ignore changes to the TableColumnModel made by the TableColumnManager
//
//        tcm.removeColumnModelListener(this);
//
//        //  Add the column to the end of the table
//
//        tcm.addColumn(column);
//        String columnString = "";
//        for (int i = 0; i < tcm.getColumnCount(); i++) {
//            TableColumn col = tcm.getColumn(i);
//            columnString = columnString + col.getIdentifier() + ",";
//        }
//
//        log("COLUMNSTRING=" + fixed + "=" + columnString);
//        //  Move the column to its position before it was hidden.
//        //  (Multiple columns may be hidden so we need to find the first
//        //  visible column before this column so the column can be moved
//        //  to the appropriate position)
//		/*
//		int position = allColumns.indexOf( column );
//		int from = tcm.getColumnCount() - 1;
//		int to = 0;
//
//		for (int i = position - 1; i > -1; i--)
//		{
//			try
//			{
//				TableColumn visibleColumn = allColumns.get( i );
//				to = tcm.getColumnIndex( visibleColumn.getHeaderValue() ) + 1;
//				break;
//			}
//			catch(IllegalArgumentException e) {}
//		}
//
//		tcm.moveColumn(from, to);
//		*/
//        tcm.addColumnModelListener(this);
//    }
//
//    /**
//     * Show a hidden column in the table.
//     *
//     * @param columnName the column name from the TableModel
//     *                   of the column to be added
//     */
//    public void showColumn(Object columnName) {
//        for (TableColumn column : allColumns) {
//            if (column.getHeaderValue().equals(columnName.toString())) {
//                showColumn(column);
//                break;
//            }
//        }
//    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        checkForPopup(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        checkForPopup(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void checkForPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTableHeader header = (JTableHeader) e.getComponent();
            JTable table = TableUtils.findParent(header,JTable.class);
            SportsTabPane stp = TableUtils.findParent(table,SportsTabPane.class);
            int column = header.columnAtPoint(e.getPoint());
            checkAndRunInEDT(() -> showPopup(column,stp,table));

        }
    }

    /*
     *  Show a popup containing items for all the columns found in the
     *  table column manager. The popup will be displayed below the table
     *  header columns that was clicked.
     *
     *  @param  index  index of the table header column that was clicked
     */

    private void showPopup(int index, SportsTabPane stp, JTable table) {
        TableColumnPopupMenu tableColumnPopupMenu = TableColumnPopupMenu.of(stp,table);
        tableColumnPopupMenu.showMenu(index);
    }
//    /*
//     *  A table column will either be added to the table or removed from the
//     *  table depending on the state of the menu item that was clicked.
//     */
//    public void actionPerformed(ActionEvent event) {
//		/*
//		JMenuItem item = (JMenuItem)event.getSource();
//
//		if (item.isSelected())
//		{
//			showColumn(item.getText());
//		}
//		else
//		{
//			hideColumn(item.getText());
//		}
//		*/
//    }

    //
    @Override
    public void columnAdded(TableColumnModelEvent e) {
//        //  A table column was added to the TableColumnModel so we need
//        //  to update the manager to track this column
//        TableColumnModel tcm = (TableColumnModel)e.getSource();
//        TableColumn column = tcm.getColumn(e.getToIndex());
//
//        if (allColumns.contains(column)) {
//            return;
//        } else {
//            allColumns.add(column);
//        }
        Utils.log(new Exception("columnAdded not supported"));
    }
    @Override
    public void columnRemoved(TableColumnModelEvent e) {
        Utils.log(new Exception("columnRemoved not supported"));
    }
    @Override
    public void columnMoved(TableColumnModelEvent e) {

        if (e.getFromIndex() == e.getToIndex()) {
            return;
        }
        TableColumnModel tcm = (TableColumnModel)e.getSource();
        //  A table column has been moved one position to the left or right
        //  in the view of the table so we need to update the manager to
        //  track the new location

        int index = e.getToIndex();
        TableColumn movedColumn = tcm.getColumn(index);

        for(int i=e.getFromIndex();i<=e.getToIndex();i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setModelIndex(i);
        }

        List<Bookie> oldColumnList = BookieManager.instance().getShownCols();
        Bookie movedBookie = oldColumnList.remove(e.getFromIndex());
        oldColumnList.add(e.getToIndex(),movedBookie);
        String newBookieStr = oldColumnList.stream().map(b-> b.getBookie_id() + "=" + b.getShortname()).collect(Collectors.joining(","));


//        if (fixed.equals("")) {
//            AppController.getUser().setBookieColumnPrefs(columnString.substring(1));
//        } else {
//            AppController.getUser().setFixedColumnPrefs(columnString.substring(1));
//        }
        AppController.getUser().setBookieColumnPrefs(newBookieStr);
        BookieManager.instance().reset();
    }
    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }
    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
    }

    //
//  Implement PropertyChangeListener
//
//    @Override
//    public void propertyChange(PropertyChangeEvent e) {
//        if ("model".equals(e.getPropertyName())) {
//            if (table.getAutoCreateColumnsFromModel()) {
//                reset();
//            }
//        }
//    }
    private static class LazyInitHolder {
        private static final TableColumnManager instance = new TableColumnManager();
    }
}
