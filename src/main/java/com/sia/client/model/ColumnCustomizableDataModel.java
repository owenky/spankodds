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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class ColumnCustomizableDataModel<V extends KeyedObject> implements TableModel {

    private final List<TableSection<V>> tableSections = new ArrayList<>();
    private final DefaultTableModel delegator = new DefaultTableModel();
    private final List<TableColumn> allColumns;
    private ColumnHeaderProperty columnHeaderProperty;
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
    public void removeGamesAndCleanup(String[] gameidstr) {
        Set<Integer> gameIdRemovedSet = Arrays.stream(gameidstr).map(Integer::parseInt).collect(Collectors.toSet());
        List<TableSection<V>> gameLines = getTableSections();
        for (TableSection<V> linesTableData : gameLines) {
            try {
                linesTableData.removeGameIdsAndCleanup(gameIdRemovedSet);
            } catch (Exception ex) {
                log(ex);
            }
        }
        this.buildIndexMappingCache();
        this.fireTableChanged(new TableModelEvent(this,0,Integer.MAX_VALUE,ALL_COLUMNS,TableModelEvent.UPDATE));
    }
    public void fireTableChanged(TableModelEvent e) {
        //TODO: need to  buildIndexMappingCache for update? ( scenario: game data changed.)
        if (TableUtils.toRebuildCache(e) ) {
            long begin = System.currentTimeMillis();
            buildIndexMappingCache();
Utils.log("debug.... rebuild table model cache..... time elapsed:"+(System.currentTimeMillis()-begin));
        }
        delegator.fireTableChanged(e);
    }
    public TableSection<V> findTableSectionByHeaderValue(String gameGroupHeader) {
        TableSection<V> rtn = null;
        List<TableSection<V>> gameLines = getTableSections();
        for (TableSection<V> ltd : gameLines) {
            if (gameGroupHeader.equalsIgnoreCase(ltd.getGameGroupHeader())) {
                rtn = ltd;
                break;
            }
        }
        return rtn;
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
    public void moveGameToThisHeader(V g, String header) {
        V thisgame = null;
        TableSection<V> group = null;
        for (TableSection<V> gameLine : tableSections) {
            thisgame = gameLine.removeGameId(g.getGame_id(),false);
            if (thisgame != null) {
                group = gameLine;
                break;
            }
        }
        // now lets see if i found it in either
        if ( null != thisgame ) // i did find it
        {
            TableSection<V> ltd = findTableSectionByHeaderValue(header);

log("MOVING GAME, the game id:"+g.getGame_id()+", teams:"+g.getTeams()+" has been moved from secion "+group.getGameGroupHeader()+" and is about to add to header "+header);

            if ( null != ltd) {
                ltd.addGame(thisgame, false);
                log("MOVING GAME, the game id:"+g.getGame_id()+", teams:"+g.getTeams()+" has been moved from secion "+group.getGameGroupHeader()+" and SUCCESSFULLY added to "+header);
            } else {
                log( new Exception("can't find LinesTableData for header:"+header));
            }
            this.buildIndexMappingCache();
            fireTableChanged(new TableModelEvent(this));
        } else {
            log("MOVING GAME FAILURED! can't find game "+ GameUtils.getGameDebugInfo((Game)g)+" in any section.");
        }
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
    //copied from MainScreen::removeGame(int)
    public V removeGame(Integer rowKey,boolean repaint) {
        V rowData = null;
        for (TableSection<V> sec : tableSections) {
            //TODO add if logic
            rowData = sec.removeGameId(rowKey,repaint);
            if (null != rowData) {
                //gameid is removed from a LinesTableData, don't need to continue because a gameid can only be in one LinesTableData
                break;
            }
        }
        return rowData;
    }
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
    public List<BlankGameStruct<V>> getBlankGameIdIndex() {
        List<BlankGameStruct<V>> idIndexList = new ArrayList<>();
        int rowModeIndex=0;
        for(TableSection<V> ltd: tableSections) {
            if ( ltd.getRowCount() > 0 && ltd.hasHeader()) {
                idIndexList.add(new BlankGameStruct<>(rowModeIndex, ltd));
            }
            rowModeIndex+=ltd.getRowCount();
        }
        return idIndexList;
    }
    public void addGameLine(TableSection<V> gameLine) {
        addGameLine(tableSections.size(),gameLine);
    }
    public void addGameLine(int index,TableSection<V> gameLine) {
        gameLine.setContainingTableModel(this);
        tableSections.add(index,gameLine);
        resetSectionIndex(index);
    }
    private void resetSectionIndex(int startIndex) {
        for(int i=startIndex;i<tableSections.size();i++) {
            TableSection<V> section = tableSections.get(i);
            section.setIndex(i);
        }
    }
    public void buildIndexMappingCache() {
        ltdSrhStructCache.clear();
        int modelIndex = 0;
        for(TableSection<V> sec: tableSections) {
            sec.resetDataVector();
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
        if ( value instanceof String) {
            String [] parts = ((String)value).split(SiaConst.GameGroupHeaderIden);
            if ( 2 == parts.length && parts[0].isEmpty()) {
                return parts[1];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public TableSection<V> getLinesTableDataWithSecionIndex(int sectionIndex) {
       return tableSections.get(sectionIndex);
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
