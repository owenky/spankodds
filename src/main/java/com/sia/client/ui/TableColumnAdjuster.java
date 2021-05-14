package com.sia.client.ui;

import com.sia.client.model.MarginProvider;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

/*
 *	Class to manage the widths of colunmns in a table.
 *
 *  Various properties control how the width of the column is calculated.
 *  Another property controls whether column width calculation should be dynamic.
 *  Finally, various Actions will be added to the table to allow the user
 *  to customize the functionality.
 *
 *  This class was designed to be used with tables that use an auto resize mode
 *  of AUTO_RESIZE_OFF. With all other modes you are constrained as the width
 *  of the columns must fit inside the table. So if you increase one column, one
 *  or more of the other columns must decrease. Because of this the resize mode
 *  of RESIZE_ALL_COLUMNS will work the best.
 */
public class TableColumnAdjuster implements PropertyChangeListener, TableModelListener {
    private final JTable table;
    private final MarginProvider marginProvider;
    private boolean isColumnHeaderIncluded;
    private boolean isColumnDataIncluded;
    private boolean isOnlyAdjustLarger;
    private boolean isDynamicAdjustment;
    private Map<TableColumn, Integer> columnSizes = new HashMap<>();

    /*
     *  Specify the table and spacing
     */
    public TableColumnAdjuster(JTable table, MarginProvider marginProvider) {
        this.table = table;
        this.marginProvider = marginProvider;
        setColumnHeaderIncluded(false);
        //setColumnHeaderIncluded( true );
        setColumnDataIncluded(true);
        setOnlyAdjustLarger(true);
        setDynamicAdjustment(true);
        installActions();
    }

    /*
     *	Indicates whether to include the header in the width calculation
     */
    public void setColumnHeaderIncluded(boolean isColumnHeaderIncluded) {
        this.isColumnHeaderIncluded = isColumnHeaderIncluded;
    }

    /*
     *	Indicates whether to include the model data in the width calculation
     */
    public void setColumnDataIncluded(boolean isColumnDataIncluded) {
        this.isColumnDataIncluded = isColumnDataIncluded;
    }

    /*
     *	Indicates whether columns can only be increased in size
     */
    public void setOnlyAdjustLarger(boolean isOnlyAdjustLarger) {
        this.isOnlyAdjustLarger = isOnlyAdjustLarger;
    }

    /*
     *  Indicate whether changes to the model should cause the width to be
     *  dynamically recalculated.
     */
    public void setDynamicAdjustment(boolean isDynamicAdjustment) {
        //  May need to add or remove the TableModelListener when changed

        //TODO disable following block
//        if (this.isDynamicAdjustment != isDynamicAdjustment) {
//            if (isDynamicAdjustment) {
//                table.addPropertyChangeListener(this);
//                table.getModel().addTableModelListener(this);
//            } else {
//                table.removePropertyChangeListener(this);
//                table.getModel().removeTableModelListener(this);
//            }
//        }
        //END of TODO

        this.isDynamicAdjustment = isDynamicAdjustment;
    }

    /*
     *  Install Actions to give user control of certain functionality.
     */
    private void installActions() {
        installColumnAction(true, true, "adjustColumn", "control ADD");
        installColumnAction(false, true, "adjustColumns", "control shift ADD");
        installColumnAction(true, false, "restoreColumn", "control SUBTRACT");
        installColumnAction(false, false, "restoreColumns", "control shift SUBTRACT");

        installToggleAction(true, false, "toggleDynamic", "control MULTIPLY");
        installToggleAction(false, true, "toggleLarger", "control DIVIDE");
    }

    /*
     *  Update the input and action maps with a new ColumnAction
     */
    private void installColumnAction(
            boolean isSelectedColumn, boolean isAdjust, String key, String keyStroke) {
        Action action = new ColumnAction(isSelectedColumn, isAdjust);
        KeyStroke ks = KeyStroke.getKeyStroke(keyStroke);
        table.getInputMap().put(ks, key);
        table.getActionMap().put(key, action);
    }

    /*
     *  Update the input and action maps with new ToggleAction
     */
    private void installToggleAction(
            boolean isToggleDynamic, boolean isToggleLarger, String key, String keyStroke) {
        Action action = new ToggleAction(isToggleDynamic, isToggleLarger);
        KeyStroke ks = KeyStroke.getKeyStroke(keyStroke);
        table.getInputMap().put(ks, key);
        table.getActionMap().put(key, action);
    }
    private AdjustStatic adjustStatic = new AdjustStatic();
    public void adjustColumnsOnRow(Integer ... rowModelIndice) {
        adjustStatic.start(rowModelIndice.length);
        TableColumnModel tcm = table.getColumnModel();
        for(int colIndex=0;colIndex<tcm.getColumnCount();colIndex++) {
            int maxCellWidth = 0;
            for(int rowModelIndex:rowModelIndice) {
                int cellWidth = this.getCellDataWidth(rowModelIndex, colIndex);
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }
            TableColumn tc = tcm.getColumn(colIndex);
            if ( maxCellWidth > tc.getPreferredWidth()) {
                this.updateTableColumn(colIndex,maxCellWidth);
                adjustStatic.addAdjustCount();
            } else {
                adjustStatic.addSkipCount();
            }
        }
        adjustStatic.finish();
    }

    /*
     *  Adjust the widths of all the columns in the table
     */
    public void adjustColumns() {
        adjustColumns(false);
    }

    /*
     *  Adjust the widths of all the columns in the table
     */
    public void adjustColumns(boolean includeheader) {
        setColumnHeaderIncluded(includeheader);
        TableColumnModel tcm = table.getColumnModel();
        if (tcm == null) {
            log("tcm is null!");
        }
 long begin=System.currentTimeMillis();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            adjustColumn(i);
        }
 log("TableColumnAdjuster::adjustColumns, update "+tcm.getColumnCount()+" columns took "+(System.currentTimeMillis()-begin));
    }
    /*
     *  Adjust the width of the specified column in the table
     */
    private void adjustColumn(final int column) {
//        log("TableColumnAdjuster::adjustColumn is called, column=" + column);
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) {
            return;
        }

        int columnHeaderWidth = getColumnHeaderWidth(column);

        int columnDataWidth = getColumnDataWidth(column);

        int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);
log("col="+column+", columnHeaderWidth="+columnHeaderWidth+", columnDataWidth="+columnDataWidth);
        updateTableColumn(column, preferredWidth);
    }

    /*
     *  Calculated the width based on the column name
     */
    private int getColumnHeaderWidth(int column) {
        //TODO no need for this check because we use big table, header is not table0 any more.
//        if (!isColumnHeaderIncluded) {
//            return 0;
//        }


        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        Object value = tableColumn.getHeaderValue();
        if (value == null) {
            return 0;
        }
        TableCellRenderer renderer = tableColumn.getHeaderRenderer();

        if (renderer == null) {
            if (table.getTableHeader() == null) {
                return 0;
            }
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
        int columnWidth = c.getPreferredSize().width+ (int) marginProvider.get().getWidth()*2;
log("TableColumnAdjuster::getColumnHeaderWidth, columnWidth="+columnWidth+",  tableColumn.getPreferredWidth()="+ tableColumn.getPreferredWidth()+", table name="+ table.getName());
        return Math.max(columnWidth, tableColumn.getPreferredWidth());
        //return 45;
    }

    /*
     *  Calculate the width based on the widest cell renderer for the
     *  given column.
     */
    private int getColumnDataWidth(int column) {
        if (!isColumnDataIncluded) {
            return 0;
        }

        int preferredWidth = 0;
        int maxWidth = table.getColumnModel().getColumn(column).getMaxWidth();

        for (int row = 0; row < table.getRowCount(); row++) {
            preferredWidth = Math.max(preferredWidth, getCellDataWidth(row, column));

            //  We've exceeded the maximum width, no need to check other rows

            if (preferredWidth >= maxWidth) {
                break;
            }
        }
        return preferredWidth;
        //return 45;
    }

    /*
     *  Update the TableColumn with the newly calculated width
     */
    private void updateTableColumn(int column, int width) {
        final TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) {
            return;
        }
        //  Don't shrink the column width

        if (isOnlyAdjustLarger) {
            width = Math.max(width, tableColumn.getPreferredWidth());
        }

        columnSizes.put(tableColumn, tableColumn.getWidth());
        if (table.getTableHeader() != null) {
            //TODO suspicious call, see description of this method
            table.getTableHeader().setResizingColumn(tableColumn);
        }
        //tableColumn.setWidth(width); // owen took this out and made it preferredwidth instead!
        tableColumn.setMinWidth(width);
        tableColumn.setPreferredWidth(width);
    }

    /*
     *  Get the preferred width for the specified cell
     */
    private int getCellDataWidth(int row, int column) {

        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
        Component c = table.prepareRenderer(cellRenderer, row, column);
        int initial = 0;
        if (c != null) {
            initial = c.getPreferredSize().width;
        }
        int width = initial + (int) marginProvider.get().getWidth()*2;
        return width;

    }

    /*
     *  Restore the widths of the columns in the table to its previous width
     */
    public void restoreColumns() {
        TableColumnModel tcm = table.getColumnModel();

        for (int i = 0; i < tcm.getColumnCount(); i++) {
            restoreColumn(i);
        }
    }

    /*
     *  Restore the width of the specified column to its previous width
     */
    private void restoreColumn(int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        Integer width = columnSizes.get(tableColumn);

        if (width != null) {
            if (table.getTableHeader() != null) {
                table.getTableHeader().setResizingColumn(tableColumn);
            }
            tableColumn.setWidth(width.intValue());
        }
    }

    //
//  Implement the PropertyChangeListener
//
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        //  When the TableModel changes we need to update the listeners and column widths

        if ("model".equals(e.getPropertyName())) {
            TableModel model = (TableModel) e.getOldValue();
            model.removeTableModelListener(this);

            model = (TableModel) e.getNewValue();
            model.addTableModelListener(this);
            adjustColumns();
        }
    }
    //
//  Implement the TableModelListener
//
    @Override
    public void tableChanged(TableModelEvent e) {
        if (!isColumnDataIncluded) {
            return;
        }

        //  Needed when table is sorted.

        checkAndRunInEDT(() -> {
            //  A cell has been updated

            int column = table.convertColumnIndexToView(e.getColumn());

            if (e.getType() == TableModelEvent.UPDATE && column != -1) {
                //  Only need to worry about an increase in width for this cell

                if (isOnlyAdjustLarger) {
                    int row = e.getFirstRow();
                    TableColumn tableColumn = table.getColumnModel().getColumn(column);

                    if (tableColumn.getResizable()) {
                        int width = getCellDataWidth(row, column);
                        updateTableColumn(column, width);
                    }
                }

                //	Could be an increase of decrease so check all rows

                else {
                    adjustColumn(column);
                }
            } else {
                adjustColumns();
            }
        });
    }

    /*
     *  Action to adjust or restore the width of a single column or all columns
     */
    class ColumnAction extends AbstractAction {
        private boolean isSelectedColumn;
        private boolean isAdjust;

        public ColumnAction(boolean isSelectedColumn, boolean isAdjust) {
            this.isSelectedColumn = isSelectedColumn;
            this.isAdjust = isAdjust;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //  Handle selected column(s) width change actions

            if (isSelectedColumn) {
                int[] columns = table.getSelectedColumns();

                for (int i = 0; i < columns.length; i++) {
                    if (isAdjust) {
                        adjustColumn(columns[i]);
                    } else {
                        restoreColumn(columns[i]);
                    }
                }
            } else {
                if (isAdjust) {
                    adjustColumns();
                } else {
                    restoreColumns();
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////
    /*
     *  Toggle properties of the TableColumnAdjuster so the user can
     *  customize the functionality to their preferences
     */
    class ToggleAction extends AbstractAction {
        private boolean isToggleDynamic;
        private boolean isToggleLarger;

        public ToggleAction(boolean isToggleDynamic, boolean isToggleLarger) {
            this.isToggleDynamic = isToggleDynamic;
            this.isToggleLarger = isToggleLarger;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isToggleDynamic) {
                setDynamicAdjustment(!isDynamicAdjustment);
                return;
            }

            if (isToggleLarger) {
                setOnlyAdjustLarger(!isOnlyAdjustLarger);
                return;
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////
    private static class AdjustStatic {
        private long totalCount = 0;
        private long lastTime=0;
        private long begin = 0;
        private int skipCount=0;
        private int adjustCount=0;
        private int rowCount;

        public void start(int rowCount) {
            totalCount++;
            skipCount = 0;
            adjustCount = 0;
            this.rowCount = rowCount;
            begin = System.currentTimeMillis();
            if ( 0 ==lastTime ) {
                lastTime = System.currentTimeMillis();
            }
        }
        public void addSkipCount() {
            skipCount++;
        }
        public void addAdjustCount() {
            adjustCount++;
        }
        public void finish() {
            long now = System.currentTimeMillis();
            if ( adjustCount > 0) {
                log("Apart from last call: " + ((now - lastTime) / 1000L) + " seconds, processing time=" + (now - begin) + " milliseconds, rowCount=" + rowCount
                        + ", skipCount=" + skipCount + " ******ADJUST COUNT*******=" + adjustCount + ", TOTAL:" + totalCount);
            } else {
                log("Apart from last call: " + ((now - lastTime) / 1000L) + " seconds, processing time=" + (now - begin) + " milliseconds, rowCount=" + rowCount
                        + " ALL SKIPPED, TOTAL:" + totalCount);
            }
            lastTime = System.currentTimeMillis();
        }
    }
}
