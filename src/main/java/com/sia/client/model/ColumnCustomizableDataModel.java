package com.sia.client.model;


import com.sia.client.ui.TableUtils;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sia.client.config.Utils.log;
import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class ColumnCustomizableDataModel<V extends KeyedObject> implements TableModel {

    private final List<TableSection<V>> tableSections = new ArrayList<>();
    private final DefaultTableModel delegator = new DefaultTableModel();
    private final List<TableColumn> allColumns;
    private final ColumnHeaderProvider<V> columnHeaderProvider;
    private Map<Integer,Object> rowModelIndex2GameGroupHeaderMap;
    private final Map<Integer,LtdSrhStruct<V>> ltdSrhStructCache = new HashMap<>();

    public ColumnCustomizableDataModel(List<TableColumn> allColumns) {
        this.allColumns = allColumns;
        validateAndFixColumnModelIndex(allColumns);
        columnHeaderProvider = new ColumnHeaderProvider<>();
        columnHeaderProvider.setTableModel(this);
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
    public void removeGames(String[] gameidstr) {
        Integer [] gameIds = new Integer[gameidstr.length];
        for(int i=0;i<gameidstr.length;i++) {
            gameIds[i] = Integer.parseInt(gameidstr[i]);
        }
        List<TableSection<V>> gameLines = getTableSections();
        for (TableSection<V> linesTableData : gameLines) {
            try {
                linesTableData.removeGameIds(gameIds);
            } catch (Exception ex) {
                log(ex);
            }
        }
        this.fireTableChanged(new TableModelEvent(this,0,Integer.MAX_VALUE,ALL_COLUMNS,TableModelEvent.UPDATE));
    }
    public void fireTableChanged(TableModelEvent e) {
        //TODO: need to  buildIndexMappingCache for update? ( scenario: game data changed.)
        if (TableUtils.toRebuildCache(e) ) {
            rowModelIndex2GameGroupHeaderMap = null;
            columnHeaderProvider.resetColumnHeaderProperty();
            buildIndexMappingCache();
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
    public ColumnHeaderProvider<V> getColumnHeaderProvider() {
        return columnHeaderProvider;
    }
    public final Map<Integer,Object> getRowModelIndex2GameGroupHeaderMap() {
        if ( null == rowModelIndex2GameGroupHeaderMap) {
            Map<Integer,Object> map = getBlankGameIdIndex().stream().
                    collect(HashMap::new, (m, struct)->m.put(struct.tableRowModelIndex,struct.linesTableData.getGameGroupHeader()), HashMap::putAll);
            rowModelIndex2GameGroupHeaderMap = Collections.unmodifiableMap(map);
        }
        return rowModelIndex2GameGroupHeaderMap;
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

log("DEBUG: moving game id:"+g.getGame_id()+", teams:"+g.getTeams()+" from secion "+group.getGameGroupHeader()+" to specified header "+header);

            if ( null != ltd) {
                ltd.addGame(thisgame, false);
            } else {
                log( new Exception("can't find LinesTableData for header:"+header));
            }
            fireTableChanged(new TableModelEvent(this));
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
    protected List<TableSection<V>> getTableSections() {
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
            if ( ltd.hasHeader()) {
                idIndexList.add(new BlankGameStruct(rowModeIndex, ltd));
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
