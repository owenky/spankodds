package com.sia.client.model;


import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.FontConfig;
import com.sia.client.ui.GameBatchUpdator;
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

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class ColumnCustomizableDataModel<V extends KeyedObject> implements TableModel {

    private final List<TableSection<V>> tableSections = new ArrayList<>();
    private final DefaultTableModel delegator = new DefaultTableModel();
    private final List<TableColumn> allColumns;
    private final List<TableSectionListener> tableSectionListenerList = new ArrayList<>();
    private ColumnHeaderProperty columnHeaderProperty;
    private boolean toConfigHeaderRow = false;
//    private boolean isDetroyed = false;
    private final Map<Integer,LtdSrhStruct<V>> ltdSrhStructCache = new HashMap<>();
    private final GameBatchUpdator gameBatchUpdator;
    private final ScreenProperty screenProperty;

    public ColumnCustomizableDataModel(ScreenProperty screenProperty,List<TableColumn> allColumns) {
        this.screenProperty = screenProperty;
        this.allColumns = allColumns;
        validateAndFixColumnModelIndex(allColumns);
        gameBatchUpdator = GameBatchUpdator.instance();
    }
    public synchronized ColumnHeaderProperty getColumnHeaderProperty() {
        if ( null == columnHeaderProperty) {
            columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor, SiaConst.DefaultHeaderFontColor, FontConfig.instance().getDefaultHeaderFont(), SiaConst.GameGroupHeaderHeight);
        }
        return columnHeaderProperty;
    }
    public SpankyWindowConfig getSpankyWindowConfig() {
        return screenProperty.getSpankyWindowConfig();
    }
    public ScreenProperty getScreenProperty() {
        return screenProperty;
    }
//    public void setDetroyed(boolean isDetroyed) {
//        this.isDetroyed = isDetroyed;
//    }
//    public boolean isDetroyed() {
//        return isDetroyed;
//    }
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
        flushUpdate();
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
        this.processTableModelEvent(new TableModelEvent(this,0,Integer.MAX_VALUE,ALL_COLUMNS,TableModelEvent.UPDATE));
    }
    public void flushUpdate() {
        this.gameBatchUpdator.flush();
    }
    public void processTableModelEvent(TableModelEvent e) {
        if (TableUtils.toRebuildCache(e) || toConfigHeaderRow()) {
            long begin = System.currentTimeMillis();
            buildIndexMappingCache(false);
Utils.log("debug.... rebuild table model cache..... time elapsed:"+(System.currentTimeMillis()-begin));
            this.gameBatchUpdator.flush(e);
        } else {
            this.gameBatchUpdator.addUpdateEvent(e);
        }

    }
    public void fireTableChanged(TableModelEvent e) {
        try {
            delegator.fireTableChanged(e);
        } catch(Exception ex) {
            String errMs = "TableModelEvent is thrown following, TableModelEvent firstRow="+e.getFirstRow()+", lastRow="+e.getLastRow()+", column="+e.getColumn()+", name="+this.getScreenProperty().getName()+", window index="
                    +this.getSpankyWindowConfig().getWindowIndex();
            log(errMs,ex);
        }
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
        return getTableSections().stream().filter(ts-> ts.containsGame(gameId)).findAny().orElse(null);
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

        flushUpdate();
        sourceSection.removeGameId(game.getGame_id());
        fireTableSectionChangeEvent();
        log("MOVING GAME, the game id:"+game.getGame_id()+", teams:"+game.getTeams()+" has been removed from section "+sourceSection.getGameGroupHeader());

        TableSection<V> ltd = findTableSectionByHeaderValue(targetGameGroupHeader);
        if ( null != ltd) {
            addGameToTableSection(ltd,game);
            log("GAME MOVED, the game id:"+game.getGame_id()+", teams:"+game.getTeams()+" has been moved from section "+sourceSection.getGameGroupHeader()+" and SUCCESSFULLY added to "+targetGameGroupHeader);
        } else {
            log( new Exception("can't find LinesTableData for header:"+targetGameGroupHeader));
        }

    }
    protected void addGameToTableSection(TableSection<V> ltd, V game) {

        if ( ltd.toAddToModel(game)) {
            checkAndRunInEDT(() -> {
                flushUpdate();
                ltd.addOrUpdate(game);
                ltd.sort(getGameComparator());
                fireTableSectionChangeEvent();
//        this.buildIndexMappingCache(false); //to be executed by  fireTableChanged(e) -- 2021-11-06
                int affectedRowModelIndex = getRowModelIndexByGameId(ltd, game.getGame_id());
                TableModelEvent e = new TableModelEvent(this, affectedRowModelIndex, affectedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
                processTableModelEvent(e);
            });
        } else {
            ltd.addToHiddenGameIdList(game.getGame_id());
        }
    }
    public void updateRow(TableSection<V> tableSection, int rowIndexInTableSection) {
        checkAndRunInEDT(() -> {
            int affectedRowModelIndex = mapToRowModelIndex(tableSection, rowIndexInTableSection);
            TableModelEvent e = new TableModelEvent(this, affectedRowModelIndex, affectedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
//            checkAndRunInEDT(() -> fireTableChanged(e));
            gameBatchUpdator.addUpdateEvent(e);
        });
    }
    public TableSection<V> checktofire(V game) {
        List<TableSection<V>> gameLines = getTableSections();
        TableSection<V> rtn = null;
        for (final TableSection<V> ltd : gameLines) {
            boolean status = ltd.checktofire(game);
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
    public int getRowModelIndexByGameId(TableSection<V> ltd, Integer gameId) {
        int gameIndex = ltd.getRowIndex(gameId);
        return mapToRowModelIndex(ltd,gameIndex);
    }
    public int mapToRowModelIndex(TableSection<V> ltd, int rowIndexInTableSection) {
        if ( 0 <= rowIndexInTableSection) {
            int offset = 0;
            for (int i = 0; i < ltd.getIndex(); i++) {
                offset += tableSections.get(i).getRowCount();
            }
            return rowIndexInTableSection + offset;
        } else {
            return -1;
        }
    }
    public void addGameLine(TableSection<V> gameLine) {
        addGameLine(tableSections.size(),gameLine);
    }
    public void addGameLine(int index,TableSection<V> gameLine) {
        addGameLine(index,gameLine,true);
    }
    public void addGameLine(int index,TableSection<V> gameLine,boolean toSort) {
        gameLine.setContainingTableModel(this);
        tableSections.add(index,gameLine);
        fireTableSectionChangeEvent();
        if ( toSort) {
            this.sortTableSection(getTableSectionComparator());
        }
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
            for(int i=0;i<sec.getRowCount();i++) {
                int offset = modelIndex-i;
                ltdSrhStructCache.put(modelIndex++,new LtdSrhStruct<>(sec,offset));
            }
        }
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
    public void resetGameStatesForAllTableSections() {
        List<TableSection<V>> tableSectionList = this.getTableSections();
        for(TableSection<V> tableSection: tableSectionList) {
            tableSection.resetDataVector();
        }
    }
    protected Comparator<TableSection<V>> getTableSectionComparator() {
        throw new IllegalStateException("getTableSectionComparator must be implemented");
    }
    protected Comparator<V> getGameComparator() {
        throw new IllegalStateException("getGameComparator must be implemented");
    }
    public final void sort() {
        sortTableSection(getTableSectionComparator());
        sortGamesForAllTableSections(getGameComparator());
    }
    public final void sortGamesForAllTableSections() {
        sortGamesForAllTableSections(getGameComparator());
    }
    public void sortGamesForAllTableSections(Comparator<? super V> gameComparator) {
        if ( null != gameComparator) {
            flushUpdate();
            for (TableSection<V> ts : tableSections) {
                ts.sort(gameComparator);
            }
        }
    }
    public void sortTableSection(Comparator<TableSection<V>> tableSectionComparator) {
        if ( null != tableSectionComparator) {
            flushUpdate();
            tableSections.sort(tableSectionComparator);
            resetSectionIndex(0);
            fireTableSectionChangeEvent();
        }
    }
    public TableSection<V> getLinesTableDataWithSectionIndex(int sectionIndex) {
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
}
