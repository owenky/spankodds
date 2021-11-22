package com.sia.client.ui;

import com.sia.client.model.MarginProvider;
import com.sia.client.ui.ColumnAdjustPreparer.AdjustRegion;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

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
public class TableColumnAdjuster {
    private final JTable table;
    private final MarginProvider marginProvider;
    private final Map<TableColumn, Integer> columnSizes = new HashMap<>();
    private final Map<TableColumn, Integer> headerSizes = new HashMap<>();
    private boolean isOnlyAdjustLarger;
    private boolean isDynamicAdjustment;
    private int firstRow;
    private int lastRow;
    private int firstCol;
    private int lastCol;
    private final AdjustStatic adjustStatic = new AdjustStatic();

    /*
     *  Specify the table and spacing
     */
    public TableColumnAdjuster(JTable table, MarginProvider marginProvider) {
        this.table = table;
        this.marginProvider = marginProvider;
        setOnlyAdjustLarger(true);
        setDynamicAdjustment(true);
        installActions();
        clear();
    }
    public void clear() {
        columnSizes.clear();
        headerSizes.clear();
        firstRow = 0;
        lastRow = 0;
        firstCol = 0;
        lastCol = 0;
    }
    /*
     *	Indicates whether columns can only be increased in size
     */
    private void setOnlyAdjustLarger(boolean isOnlyAdjustLarger) {
        this.isOnlyAdjustLarger = isOnlyAdjustLarger;
    }

    /*
     *  Indicate whether changes to the model should cause the width to be
     *  dynamically recalculated.
     */
    private void setDynamicAdjustment(boolean isDynamicAdjustment) {
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

    public void adjustColumnsOnRow(Integer... rowModelIndice) {
        adjustStatic.start(rowModelIndice.length);
        TableColumnModel tcm = table.getColumnModel();
        for (int colIndex = 0; colIndex < tcm.getColumnCount(); colIndex++) {
            int maxCellWidth = 0;
            for (int rowModelIndex : rowModelIndice) {
                int cellWidth = this.getCellDataWidth(rowModelIndex, colIndex);
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }
            TableColumn tc = tcm.getColumn(colIndex);
            if (maxCellWidth > tc.getPreferredWidth()) {
                this.updateTableColumn(colIndex, maxCellWidth);
                adjustStatic.addAdjustCount();
            } else {
                adjustStatic.addSkipCount();
            }
        }
        adjustStatic.finish();
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
        int width = initial + (int) marginProvider.get().getWidth() * 2;
        return width;

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
            //TODO    log("disable suspecious table.getTableHeader().setResizingColumn call --05/22/2021");
//            table.getTableHeader().setResizingColumn(tableColumn);
        }
        //tableColumn.setWidth(width); // owen took this out and made it preferredwidth instead!
//        tableColumn.setMinWidth(width); //set min width to width disable manually dragging column narrower -- 06/01/2021
        tableColumn.setPreferredWidth(width);
    }

    /*
     *  Adjust the widths of all the columns in the table
     */
    public void adjustColumns(AdjustRegion adjustRegion) {

        firstRow = adjustRegion.firstRow;
        lastRow = adjustRegion.lastRow;
        firstCol = adjustRegion.firstColumn;
        lastCol = adjustRegion.lastColumn;

        long begin = System.currentTimeMillis();
        for (int i = firstCol; i <= lastCol; i++) {
            adjustColumn(i);
        }
    }
    /*
     *  Adjust the width of the specified column in the table
     */
    public int adjustColumn(final int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);

        if (!tableColumn.getResizable()) {
            return tableColumn.getPreferredWidth();
        }

        /**
         * For all columns, we always want to auto adjust only if data does not fit (exclude column header in factoring)…
         * If user makes column bigger than data then it stays that way. If user makes column smaller but data still fits then we also keep it that way.
         * The only time auto adjust kicks in to make column bigger is if data does not fit.  -- 06/25/2021
         */
//        int columnHeaderWidth = getColumnHeaderWidth(column);  //excluding header as stated above -- 06/25/2021
        int columnHeaderWidth = 0;

        int columnDataWidth = getColumnDataWidth(column);

        int preferredWidth = Math.max(columnHeaderWidth, columnDataWidth);
        updateTableColumn(column, preferredWidth);
        return preferredWidth;
    }

    /*
     *  Calculated the width based on the column name
     */
    private int getColumnHeaderWidth(int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        return headerSizes.computeIfAbsent(tableColumn, tc -> {
            Object value = tc.getHeaderValue();
            if (value == null) {
                return 0;
            }
            TableCellRenderer renderer = tc.getHeaderRenderer();

            if (renderer == null) {
                if (table.getTableHeader() == null) {
                    return 0;
                }
                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component c = renderer.getTableCellRendererComponent(table, value, false, false, -1, column);
            int columnWidth = c.getPreferredSize().width + (int) marginProvider.get().getWidth() * 2;
            return Math.max(columnWidth, tc.getPreferredWidth());
        });
    }

    /*
     *  Calculate the width based on the widest cell renderer for the
     *  given column.
     */
    private int getColumnDataWidth(int column) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        if ( firstRow < 0) {
            firstRow = 0;
        }
        if ( lastRow < 0) {
            lastRow = table.getRowCount()-1;
        }


        int preferredWidth = tc.getPreferredWidth();
        int maxWidth = tc.getMaxWidth();

        for (int row = firstRow; row <= lastRow; row++) {
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
            tableColumn.setWidth(width);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////
    private static class AdjustStatic {
        private long totalCount = 0;
        private long lastTime = 0;
        private long begin = 0;
        private int skipCount = 0;
        private int adjustCount = 0;
        private int rowCount;

        public void start(int rowCount) {
            totalCount++;
            skipCount = 0;
            adjustCount = 0;
            this.rowCount = rowCount;
            begin = System.currentTimeMillis();
            if (0 == lastTime) {
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
            if (adjustCount > 0) {
//                log("Apart from last call: " + ((now - lastTime) / 1000L) + " seconds, processing time=" + (now - begin) + " milliseconds, rowCount=" + rowCount
//                        + ", skipCount=" + skipCount + " ******ADJUST COUNT*******=" + adjustCount + ", TOTAL:" + totalCount);
            } else {
//                log("Apart from last call: " + ((now - lastTime) / 1000L) + " seconds, processing time=" + (now - begin) + " milliseconds, rowCount=" + rowCount
//                        + " ALL SKIPPED, TOTAL:" + totalCount);
            }
            lastTime = System.currentTimeMillis();
        }
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

            firstRow = 0;
            lastRow = table.getRowCount()-1;
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
                    for(int col=0;col<table.getColumnCount()-1;col++) {
                        adjustColumn(col);
                    }
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
            }
        }
    }
}
