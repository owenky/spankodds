package com.sia.client.model;


import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.TableUtils;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sia.client.config.Utils.*;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class ColumnCustomizableDataModel<V extends KeyedObject> implements TableModel {

    private final List<TableSection<V>> tableSections = new ArrayList<>();
    private final DefaultTableModel delegator = new DefaultTableModel();
    private final List<TableColumn> allColumns;
    private final List<TableSectionListener> tableSectionListenerList = new ArrayList<>();
    private ColumnHeaderProperty columnHeaderProperty;
    private boolean toConfigHeaderRow = false;
    private final Map<Integer,LtdSrhStruct<V>> ltdSrhStructCache = new HashMap<>();

    public ColumnCustomizableDataModel(List<TableColumn> allColumns) {
        this.allColumns = allColumns;
        validateAndFixColumnModelIndex(allColumns);
    }
    public synchronized ColumnHeaderProperty getColumnHeaderProperty() {
        if ( null == columnHeaderProperty) {
            columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor, SiaConst.DefaultHeaderFontColor, SiaConst.DefaultHeaderFont, SiaConst.GameGroupHeaderHeight);
        }
        return columnHeaderProperty;
    }
    public void setToConfigHeaderRow(boolean toConfigHeaderRow) {
        this.toConfigHeaderRow = toConfigHeaderRow;
    }
    public boolean toConfigHeaderRow() {
        return this.toConfigHeaderRow;
    }
    @Override
    public final Object getValueAt(int rowModelIndex, int colModelIndex) {
        LtdSrhStruct<V> ltdSrhStruct = getLinesTableData(rowModelIndex);
        TableSection<V> r = ltdSrhStruct.linesTableData;
        return r.getValueAt((rowModelIndex-ltdSrhStruct.offset),colModelIndex);
    }
    @Override
    public final int getRowCount() {
        int rowCount=0;
        for(TableSection<V> sec: tableSections) {
            rowCount += sec.getRowCount();
        }
        return rowCount;
    }
    @Override
    public int getColumnCount() {
        return allColumns.size();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        Object columnName =  allColumns.get(columnIndex).getHeaderValue();
        if ( null == columnName) {
            columnName = allColumns.get(columnIndex).getIdentifier();
        }
        if ( null == columnName) {
            columnName = "";
        } else {
            columnName = columnName.toString();
        }
        return (String)columnName;
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        throw new IllegalArgumentException("Method not supported");
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }
    @Override
    public void addTableModelListener(final TableModelListener l) {
        delegator.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(final TableModelListener l) {
        delegator.removeTableModelListener(l);
    }
    @Override
    public void setValueAt(Object value,int rowModelIndex, int colModelIndex) {
        throw new IllegalStateException("Pending implementation");
    }
    public void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet) {
        List<TableSection<V>> gameLines = getTableSections();
        for (TableSection<V> linesTableData : gameLines) {
            try {
                linesTableData.removeGameIdsAndCleanup(gameIdRemovedSet);
            } catch (Exception ex) {
                log(ex);
            }
        }
        fireTableSectionChangeEvent();
//        this.buildIndexMappingCache(false); //moved to be exceuted by this.fireTableChanged -- 2021-11-06
        this.fireTableChanged(new TableModelEvent(this,0,Integer.MAX_VALUE,ALL_COLUMNS,TableModelEvent.UPDATE));
    }
    public void fireTableChanged(TableModelEvent e) {
        if (TableUtils.toRebuildCache(e) || toConfigHeaderRow()) {
            long begin = System.currentTimeMillis();
            buildIndexMappingCache(false);
Utils.log("debug.... rebuild table model cache..... time elapsed:"+(System.currentTimeMillis()-begin));
        }
        delegator.fireTableChanged(e);
    }
    public TableSection<V> findTableSectionByHeaderValue(GameGroupHeader gameGroupHeader) {
        TableSection<V> rtn = null;
        List<TableSection<V>> gameLines = getTableSections();
        for (TableSection<V> ltd : gameLines) {
            if (gameGroupHeader.equals(ltd.getGameGroupHeader())) {
                rtn = ltd;
                break;
            }
        }
        return rtn;
    }
    public TableSection<V> findTableSectionByGameid(int gameId) {
        return getTableSections().stream().filter(ts-> 0<=ts.getRowIndex(gameId)).findAny().orElse(null);
    }
    public List<TableColumn> getAllColumns() {
        return this.allColumns;
    }
    public Integer getRowKey(int rowModelIndex) {
        LtdSrhStruct<V> ltdSrhStruct = getLinesTableData(rowModelIndex);
        TableSection<V> section = ltdSrhStruct.linesTableData;
        int rowIndexInLinesTableData = rowModelIndex-ltdSrhStruct.offset;
        return section.getRowKey(rowIndexInLinesTableData);
    }
    public V getGame(int rowModelIndex) {
        LtdSrhStruct<V> ltdSrhStruct = getLinesTableData(rowModelIndex);
        TableSection<V> section = ltdSrhStruct.linesTableData;
        int rowIndexInLinesTableData = rowModelIndex-ltdSrhStruct.offset;
        return section.getGame(rowIndexInLinesTableData);
    }
    //refactored from MainScreen::moveGameToThisHeader(Game, String)
//    public void moveGameToThisHeader(V g, GameGroupHeader header) {
//        V thisgame = null;
//        TableSection<V> group = null;
//        for (TableSection<V> gameLine : tableSections) {
//            thisgame = gameLine.removeGameId(g.getGame_id());
//            if (thisgame != null) {
//                group = gameLine;
//                fireTableSectionChangeEvent();
//                log("MOVING GAME, the game id:"+g.getGame_id()+", teams:"+g.getTeams()+" has been removed from secion "+group.getGameGroupHeader());
//                break;
//            }
//        }
//        // now lets see if i found it in either
//        if ( null != thisgame ) {
//            TableSection<V> ltd = findTableSectionByHeaderValue(header);
//            if ( null != ltd) {
//                addGameToTableSection(ltd,thisgame);
//                log("GAME MOVED, the game id:"+g.getGame_id()+", teams:"+g.getTeams()+" has been moved from secion "+group.getGameGroupHeader()+" and SUCCESSFULLY added to "+header);
//            } else {
//                log( new Exception("can't find LinesTableData for header:"+header));
//            }
//        } else {
//            log("MOVING GAME FAILURED! can't find game "+ GameUtils.getGameDebugInfo((Game)g)+" in any section.");
//        }
//    }
    public void moveGameToThisHeader(V g, GameGroupHeader header) {
        TableSection<V> sourceGroup = null;
        for (TableSection<V> gameLine : tableSections) {
            int index = gameLine.getRowIndex(g.getGame_id());
            if ( 0 <= index) {
                sourceGroup = gameLine;
                break;
            }
        }
        // now lets see if i found it in either
        if ( null != sourceGroup ) {
            moveGameFromSourceToTarget(sourceGroup,g,header);
        } else {
            log("MOVING GAME FAILURED! can't find game "+ GameUtils.getGameDebugInfo((Game)g)+" in any section.");
        }
    }
    public void moveGameFromSourceToTarget(TableSection<V> sourceSection, V game,GameGroupHeader targetGameGroupHeader) {

        sourceSection.removeGameId(game.getGame_id());
        fireTableSectionChangeEvent();
        log("MOVING GAME, the game id:"+game.getGame_id()+", teams:"+game.getTeams()+" has been removed from secion "+sourceSection.getGameGroupHeader());

        TableSection<V> ltd = findTableSectionByHeaderValue(targetGameGroupHeader);
        if ( null != ltd) {
            addGameToTableSection(ltd,game);
            log("GAME MOVED, the game id:"+game.getGame_id()+", teams:"+game.getTeams()+" has been moved from secion "+sourceSection.getGameGroupHeader()+" and SUCCESSFULLY added to "+targetGameGroupHeader);
        } else {
            log( new Exception("can't find LinesTableData for header:"+targetGameGroupHeader));
        }

    }
    protected void addGameToTableSection(TableSection<V> ltd, V game) {

        ltd.addOrUpdate(game);
        ltd.sort(getDefaultGameComparator());
        fireTableSectionChangeEvent();
//        this.buildIndexMappingCache(false); //to be executed by  fireTableChanged(e) -- 2021-11-06
        int affectedRowModelIndex = getRowModelIndex(ltd, ltd.getRowIndex(game.getGame_id()));
        TableModelEvent e = new TableModelEvent(this, affectedRowModelIndex, affectedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        checkAndRunInEDT(() -> fireTableChanged(e));

    }
    public void updateRow(TableSection<V> tableSection, int rowIndexInTableSection) {
        int affectedRowModelIndex = getRowModelIndex(tableSection, rowIndexInTableSection);
        TableModelEvent e = new TableModelEvent(this, affectedRowModelIndex, affectedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
        checkAndRunInEDT(() -> fireTableChanged(e));
    }
    public TableSection<V> checktofire(V game,boolean repaint) {
        List<TableSection<V>> gameLines = getTableSections();
        TableSection<V> rtn = null;
        for (final TableSection<V> ltd : gameLines) {
            boolean status = ltd.checktofire(game,repaint);
            if (status) {
                rtn  = ltd;
                break;
            }
        }
        return rtn;
    }
    public List<TableSection<V>> getTableSections() {
        return tableSections;
    }
//    //copied from MainScreen::removeGame(int)
//    public V removeGame(Integer rowKey,boolean repaint) {
//        V rowData = null;
//        for (TableSection<V> sec : tableSections) {
//            //TODO add if logic
//            rowData = sec.removeGameId(rowKey,repaint);
//            if (null != rowData) {
//                //gameid is removed from a LinesTableData, don't need to continue because a gameid can only be in one LinesTableData
//                break;
//            }
//        }
//        return rowData;
//    }
    public int getRowModelIndex(TableSection<V> ltd, Integer gameId) {
        int gameIndex = ltd.getRowIndex(gameId);
        if ( gameIndex >=0) {
            int offset = 0;
            for (int i = 0; i < ltd.getIndex(); i++) {
                offset += tableSections.get(i).getRowCount();
            }
            return gameIndex + offset;
        } else {
            return -1;
        }
    }
//    public List<BlankGameStruct<V>> getBlankGameIdIndex() {
//        List<BlankGameStruct<V>> idIndexList = new ArrayList<>();
//        int rowModeIndex=0;
//        for(TableSection<V> ltd: tableSections) {
//            if ( ltd.getRowCount() > 0 && ltd.hasHeader()) {
//                idIndexList.add(new BlankGameStruct<>(rowModeIndex, ltd));
//            }
//            rowModeIndex+=ltd.getRowCount();
//        }
//        return idIndexList;
//    }
    public void addGameLine(TableSection<V> gameLine) {
        addGameLine(tableSections.size(),gameLine);
    }
    public void addGameLine(int index,TableSection<V> gameLine) {
        gameLine.setContainingTableModel(this);
        tableSections.add(index,gameLine);
        fireTableSectionChangeEvent();
//        resetSectionIndex(index);
    }
    private void resetSectionIndex(int startIndex) {
        for(int i=startIndex;i<tableSections.size();i++) {
            TableSection<V> section = tableSections.get(i);
            section.setIndex(i);
        }
    }
    public void buildIndexMappingCache(boolean toSort) {
        ltdSrhStructCache.clear();
        if ( toSort) {
            sort();
            resetSectionIndex(0);
        }
        int modelIndex = 0;
        for(TableSection<V> sec: tableSections) {
//            sec.resetDataVector();  -- Is it safe to disable it? --2021-11-07
            for(int i=0;i<sec.getRowCount();i++) {
                int offset = modelIndex-i;
                ltdSrhStructCache.put(modelIndex++,new LtdSrhStruct<>(sec,offset));
            }
        }
        debug("resetDataVector Disabled in buildIndexMappingCache");
    }
    public LtdSrhStruct<V> getLinesTableData(int rowModelIndex) {

        return ltdSrhStructCache.computeIfAbsent(rowModelIndex,(index)-> {
            int modelIndex=0;
            TableSection<V> rtn = null;
            for(TableSection<V> sec: tableSections) {
                sec.resetDataVector();
                if ( (modelIndex+sec.getRowCount()) <= index) {
                    modelIndex += sec.getRowCount();
                } else {
                    rtn = sec;
                    break;
                }
            }

            return new LtdSrhStruct<>(rtn,modelIndex);
        });

    }

    /**
     *
     * @param rowModelIndex row model index
     * @return game group header if the row is game group header row, otherwise return null
     */
    public String getGameGroupHeader(int rowModelIndex) {
        Object value = this.getValueAt(rowModelIndex,0);
        return retrieveGameGroupHeader(value);
    }
    public boolean containsGame(int gameId) {
        return this.getTableSections().stream().anyMatch(s-> 0 <= s.getRowIndex(gameId));
    }
    public static String retrieveGameGroupHeader(Object value) {
        if ( value instanceof GameGroupHeader) {
            return ((GameGroupHeader)value).getGameGroupHeaderStr();
        } else {
            return null;
        }
    }
    protected Comparator<TableSection<V>> getdefaultTableSectionComparator() {
        return null;
    }
    protected Comparator<? super V> getDefaultGameComparator() {
        return null;
    }
    public final void sort() {
        sortTableSection(getdefaultTableSectionComparator());
        sortGamesForAllTableSections(getDefaultGameComparator());
    }

    public void sortGamesForAllTableSections(Comparator<? super V> gameComparator) {
        if ( null != gameComparator) {
            for (TableSection<V> ts : tableSections) {
                ts.sort(gameComparator);
            }
        }
    }
    public void sortTableSection(Comparator<TableSection<V>> tableSectionComparator) {
        if ( null != tableSectionComparator) {
            tableSections.sort(tableSectionComparator);
            resetSectionIndex(0);
            fireTableSectionChangeEvent();
        }
    }
    public TableSection<V> getLinesTableDataWithSecionIndex(int sectionIndex) {
       return tableSections.get(sectionIndex);
    }
    public void addTableSectionListener(TableSectionListener l) {
        tableSectionListenerList.add(l);
    }
    private void fireTableSectionChangeEvent() {
        for(TableSectionListener l :tableSectionListenerList) {
            l.processTableSectionChanged();
        }
    }
    private static void validateAndFixColumnModelIndex(List<TableColumn> allColumns) {
        if ( ! validateColumnIndex(allColumns)) {
            for( int i=0;i<allColumns.size();i++) {
                allColumns.get(i).setModelIndex(i);
            }
        }
    }
    private static boolean validateColumnIndex(List<TableColumn> allColumns) {
        int modelIndex0Count=0;
        boolean status = true;
        for(TableColumn tc: allColumns) {
            if ( 0 == tc.getModelIndex()) {
                if ( ++modelIndex0Count > 1) {
                    status = false;
                    break;
                }
            }
        }
        return status;
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    public static class LtdSrhStruct<V extends KeyedObject> {
        public final TableSection<V> linesTableData;
        //index of the first row of this linesTableData in tableModel
        public final int offset;
        public LtdSrhStruct(TableSection<V> linesTableData,int offset) {
            this.linesTableData = linesTableData;
            this.offset = offset;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////
    public static class BlankGameStruct<V extends KeyedObject> {
        public final int tableRowModelIndex;
        public final TableSection<V> linesTableData;
        public BlankGameStruct(int tableRowModelIndex, TableSection<V> linesTableData) {
            this.tableRowModelIndex = tableRowModelIndex;
            this.linesTableData = linesTableData;
        }
    }
}
