package com.sia.client.ui;

import com.sia.client.config.ColumnSettings;
import com.sia.client.config.Config;
import com.sia.client.config.RowSelection;
import com.sia.client.config.SiaConst;
import com.sia.client.model.*;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TableUtils {

    public static boolean isCellEditable(JTable table, int row, int column) {

        if ( isNoteColumn(table,column)) {

            if ( table instanceof AccessableToGame) {
                int rowModelIndex = table.convertRowIndexToModel(row);
                return ((AccessableToGame)table).getGame(rowModelIndex).getGame_id() >0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public static boolean isNoteColumn(JTable table, int columnIndex) {
        TableColumn tc = table.getColumnModel().getColumn(columnIndex);
        return tc.getIdentifier() instanceof Integer && (Integer) tc.getIdentifier() == SiaConst.NoteColumnBookieId;
    }

    public static void updateRowSelection(ColumnCustomizableTable<?> gameTable, ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();
        RowSelection rowSelection = Config.instance().getRowSelection();
        rowSelection.clearSelectedGames(gameTable);
        int beginIndex = listSelectionModel.getMinSelectionIndex();
        int endIndex = listSelectionModel.getMaxSelectionIndex();
        List<Integer> selectedGameIdList = new ArrayList<>(endIndex - beginIndex + 1);
        for (int i = beginIndex; i <= endIndex; i++) {
            if (listSelectionModel.isSelectedIndex(i)) {
                int modelIndex = gameTable.convertRowIndexToModel(i);
                int gameId = gameTable.getGame(modelIndex).getGame_id();
                if (0 < gameId) {
                    selectedGameIdList.add(gameId);
                }
            }
        }
        rowSelection.addSelectedGames(gameTable, selectedGameIdList);
    }

    public static void selectRowsFromConfig(ColumnCustomizableTable<?> gameTable) {
        RowSelection rowSelection = Config.instance().getRowSelection();
        Collection<Integer> selectedGameIds = rowSelection.getSelectedGameIds(gameTable);
        int remaining = selectedGameIds.size();
        for (int viewIndex = 0; viewIndex < gameTable.getRowCount(); viewIndex++) {
            int modelIndex = gameTable.convertRowIndexToModel(viewIndex);
            int gameId = gameTable.getGame(modelIndex).getGame_id();
            if (selectedGameIds.contains(gameId)) {
                gameTable.addRowSelectionInterval(viewIndex, viewIndex);
                if (0 == (--remaining)) {
                    break;
                }

            }
        }
    }

    public static <T> T findParent(JComponent jComponent, Class<T> parentClass) {
        Component parent = jComponent;
        do {
            if (parent.getClass().equals(parentClass)) {
                break;
            }
            parent = parent.getParent();
        } while (!(parent instanceof JFrame));

        if (parent.getClass().equals(parentClass)) {
            return (T) parent;
        } else {
            return null;
        }
    }

    public static void highLightCellWhenRowSelected(JTable jtable, JComponent cellRender, int rowViewIndex, Color highLightColor) {
        boolean isRowSelected = jtable.isRowSelected(rowViewIndex);
        List<JComponent> children = TableUtils.getChildren(cellRender);
        children.add(cellRender);
        UserDisplaySettings uds = AppController.getUserDisplaySettings();
        for (JComponent jcomp : children) {
            if (isRowSelected) {

                if (jcomp instanceof JLabel) {
                    if (iswhite(jcomp.getBackground()) || (

                            (!jcomp.getBackground().equals(uds.getFirstcolor())) &&
                                    (!jcomp.getBackground().equals(uds.getSecondcolor())) &&
                                    (!jcomp.getBackground().equals(uds.getThirdcolor())))
                    ) {
                        jcomp.setBackground(highLightColor);
                        jcomp.setForeground(getFgColor(highLightColor));
                    }
                }
            }

        }
    }

    public static List<JComponent> getChildren(JComponent comp) {
        List<JComponent> children = new ArrayList<>();
        List<JComponent> parents = new ArrayList<>();
        parents.add(comp);
        for (int i = 0; i < parents.size(); i++) {
            JComponent next = parents.get(i);
            if (next != comp) {
                children.add(next);
            }
            while (next.getComponents().length > 0) {
                Component[] myChildern = next.getComponents();
                boolean firstFound = false;
                for (Component c : myChildern) {
                    if (c instanceof JComponent) {
                        if (!firstFound) {
                            next = (JComponent) c;
                            children.add(next);
                            firstFound = true;
                        } else {
                            parents.add((JComponent) c);
                        }
                    }
                }
            }
        }
        return children;
    }

    public static <V extends KeyedObject> JComponent configTableLockColumns(ColumnCustomizableTable<V> mainTable, int lockedColumnBoundaryIndex) {

        int windowIndex = mainTable.getModel().getSpankyWindowConfig().getWindowIndex();
        SportsTabPane stp = SpankyWindow.findSpankyWindow(windowIndex).getSportsTabPane();
        mainTable.removeLockedColumnIndex(lockedColumnBoundaryIndex);
        mainTable.createUnlockedColumns();
//        mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //necessary for horizontal scroll bar showing up.


        RowHeaderTable<V> rowHeaderTable = mainTable.getRowHeaderTable();
        rowHeaderTable.createDefaultColumnsFromModel();
        rowHeaderTable.getTableHeader().setFont(mainTable.getTableHeader().getFont());
        // Put rowHeaderTable in a viewport that we can control.
        JViewport jv = new JViewport();
        jv.setView(rowHeaderTable);
        rowHeaderTable.optimizeTableWidth(rowHeaderTable.getPreferredWidth());
//        jv.setPreferredSize(rowHeaderTable.getMaximumSize());


        JScrollPane tableScrollPane = mainTable.getTableScrollPane();
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        tableScrollPane.getVerticalScrollBar().setUnitIncrement(29);
        tableScrollPane.setRowHeader(jv);
        tableScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTable.getTableHeader());

        TableScrollPaneContaine container = new TableScrollPaneContaine(mainTable);
        container.setLayout(new BorderLayout());
        container.add(tableScrollPane, BorderLayout.CENTER);
        container.add(tableScrollPane.getHorizontalScrollBar(), BorderLayout.SOUTH);

        mainTable.getTableColumnHeaderManager().installListeners();
        //add table column header listeners
//        new TableColumnManager(stp, mainTable, "");
//        new TableColumnManager(stp, mainTable.getRowHeaderTable(), "fixed");
        TableColumnManager.instance().installListeners(mainTable);
        TableColumnManager.instance().installListeners(mainTable.getRowHeaderTable());
        return container;
    }

    public static void processTableModelEvent(ColumnCustomizableDataModel<?> tm) {
        tm.processTableModelEvent(new TableModelEvent(tm, 0, Integer.MAX_VALUE, 0, TableModelEvent.UPDATE));
    }

    public static boolean toRebuildCache(TableModelEvent e) {
        if (null == e) {
            return true;
        }
        //when update for lastrow=Integer.MAX_VALUE, all row heights are rest to table row height,
        return e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE
                || (e.getType() == TableModelEvent.UPDATE && e.getLastRow() == Integer.MAX_VALUE)
                ;
    }

    public static TableColumn cloneTableColumn(TableColumn sourceTc, int columnIndex) {

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
        int tableSectionRowHeight = Config.instance().getFontConfig().getNormalRowHeight();
        if (null != games) {
            for (Game g : games) {
                if (null == g) {
                    continue;
                }
                if (SiaConst.SoccerLeagueId == g.getLeague_id()) {
                    tableSectionRowHeight = Config.instance().getFontConfig().getSoccerRowHeight();
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
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn tc = columnModel.getColumn(i);
            Object bookieId = tc.getIdentifier();
            sb.append(bookieId);
            if (i < columnModel.getColumnCount() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class TableScrollPaneContaine extends JPanel {

        private final ColumnCustomizableTable<?> mainTable;
        private boolean firstShown = false;

        public TableScrollPaneContaine(ColumnCustomizableTable<?> mainTable) {
            this.mainTable = mainTable;
        }

        @Override
        public void setVisible(boolean visible) {
            if (visible && !firstShown) {
                mainTable.configHeaderRow();
                mainTable.getRowHeaderTable().optimizeSize();
                firstShown = true;
            }
            super.setVisible(visible);
        }
    }

    private static Color getFgColor(Color bgcolor) {

        int r = bgcolor.getRed();
        int g = bgcolor.getGreen();
        int b = bgcolor.getBlue();
        double fgnum = ((r * 299) + (g * 587) + (b * 114)) / 1000;
        if (fgnum >= 128) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }


    }

    private static boolean iswhite(Color color) {
//        if(color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255)
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
        return Color.WHITE.equals(color);
    }
}
