package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.Game;
import com.sia.client.model.KeyedObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.util.List;

public abstract class TableUtils {

    public static <V extends KeyedObject> JComponent configTableLockColumns(ColumnCustomizableTable<V> mainTable, int lockedColumnBoundaryIndex) {

        mainTable.removeLockedColumnIndex(lockedColumnBoundaryIndex);
        mainTable.createUnlockedColumns();
//        mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());
        mainTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF ); //necessary for horizontal scroll bar showing up.


        RowHeaderTable<V> rowHeaderTable = mainTable.getRowHeaderTable();
        rowHeaderTable.createDefaultColumnsFromModel();
        rowHeaderTable.getTableHeader().setFont(mainTable.getTableHeader().getFont());
        // Put rowHeaderTable in a viewport that we can control.
        JViewport jv = new JViewport();
        jv.setView(rowHeaderTable);
//        jv.setPreferredSize(rowHeaderTable.getMaximumSize());


        JScrollPane tableScrollPane = mainTable.getTableScrollPane();
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        tableScrollPane.getVerticalScrollBar().setUnitIncrement(29);
        tableScrollPane.setRowHeader(jv);
        tableScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowHeaderTable.getTableHeader());

        TableScrollPaneContaine container = new TableScrollPaneContaine(mainTable);
        container.setLayout(new BorderLayout());
        container.add(tableScrollPane,BorderLayout.CENTER);
        container.add(tableScrollPane.getHorizontalScrollBar(),BorderLayout.SOUTH);

        mainTable.getTableColumnHeaderManager().installListeners();
        //add table column header listeners
        new TableColumnManager(mainTable,"");
        new TableColumnManager(mainTable.getRowHeaderTable(),"fixed");
        return container;
    }
    public static void processTableModelEvent(ColumnCustomizableDataModel<?> tm) {
        tm.processTableModelEvent(new TableModelEvent(tm,0,Integer.MAX_VALUE,0,TableModelEvent.UPDATE));
    }
    public static boolean toRebuildCache(TableModelEvent e) {
        if ( null == e) {
            return true;
        }
        //when update for lastrow=Integer.MAX_VALUE, all row heights are rest to table row height,
        return e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE
                || (e.getType() == TableModelEvent.UPDATE && e.getLastRow() == Integer.MAX_VALUE)
                ;
    }
    public static TableColumn cloneTableColumn(TableColumn sourceTc,int columnIndex) {

        TableColumn rtn = new TableColumn(columnIndex);
        rtn.setCellRenderer(sourceTc.getCellRenderer());
        rtn.setCellEditor(sourceTc.getCellEditor());
        rtn.setHeaderRenderer(sourceTc.getHeaderRenderer());
        rtn.setHeaderValue(sourceTc.getHeaderValue());
        rtn.setIdentifier(sourceTc.getIdentifier());
        rtn.setMaxWidth(sourceTc.getMaxWidth());
        rtn.setMinWidth(sourceTc.getMinWidth());
        rtn.setWidth(sourceTc.getWidth());
        rtn.setPreferredWidth(sourceTc.getPreferredWidth());
        rtn.setResizable(sourceTc.getResizable());
        return rtn;
    }
    public static int calTableSectionRowHeight(List<Game> games) {
        int tableSectionRowHeight = SiaConst.NormalRowheight;
        if ( null != games) {
            for(Game g: games) {
                if ( null == g) {
                    continue;
                }
                if (SiaConst.SoccerLeagueId == g.getLeague_id()) {
                    tableSectionRowHeight = SiaConst.SoccerRowheight;
                }
                break;
            }
        }
        return tableSectionRowHeight;
    }
    public static <V extends KeyedObject> void saveTableColumnPreference(ColumnCustomizableTable<V> table) {

        String showncols = concatBookieId(table);
        String fixedcols = concatBookieId(table.getRowHeaderTable());
        AppController.getUser().setBookieColumnPrefs(showncols);
        AppController.getUser().setFixedColumnPrefs(fixedcols);
    }
    private static String concatBookieId(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<columnModel.getColumnCount();i++) {
            TableColumn tc = columnModel.getColumn(i);
            Object bookieId = tc.getIdentifier();
            sb.append(bookieId);
            if ( i < columnModel.getColumnCount()-1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class  TableScrollPaneContaine extends JPanel {

        private final ColumnCustomizableTable<?> mainTable;
        private boolean firstShown = false;
        public TableScrollPaneContaine(ColumnCustomizableTable<?> mainTable) {
            this.mainTable = mainTable;
        }
        @Override
        public void setVisible(boolean visible) {
            if ( visible && ! firstShown) {
                mainTable.configHeaderRow();
                mainTable.getRowHeaderTable().optimizeSize();
                firstShown = true;
            }
            super.setVisible(visible);
        }
    }
}
