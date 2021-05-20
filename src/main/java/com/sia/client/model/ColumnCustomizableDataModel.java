package com.sia.client.model;


import com.sia.client.ui.LinesTableData;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sia.client.config.Utils.log;

public class ColumnCustomizableDataModel<V extends KeyedObject> implements TableModel {

    private final List<TableSection<V>> tableSections = new ArrayList<>();
    private final DefaultTableModel delegator = new DefaultTableModel();
    private final List<TableColumn> allColumns;
    private final Map<Integer,LtdSrhStruct<V>> ltdSrhStructCache = new HashMap<>();
    //TODO debug flag
    private boolean headerInstalled = false;
    public void setHeaderInstalled(boolean headerInstalled) {this.headerInstalled = headerInstalled;}
    //END OF debug TODO

    public ColumnCustomizableDataModel(List<TableColumn> allColumns) {
        this.allColumns = allColumns;
        validateAndFixColumnModelIndex(allColumns);
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
//        return delegator.getColumnCount();
        return allColumns.size();
    }

    @Override
    public String getColumnName(final int columnIndex) {
//        return delegator.getColumnName(columnIndex);
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
//        return delegator.getColumnClass(columnIndex);
        throw new IllegalArgumentException("Method not supported");
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
//        return delegator.isCellEditable(rowIndex,columnIndex);
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
    public void fireTableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE ) {
            ltdSrhStructCache.clear();
            buildIndexMappingCache();
        }
        delegator.fireTableChanged(e);
    }
    public TableSection<V> findTableSectionByHeaderValue(String gameGroupHeader) {
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
    public List<TableColumn> getAllColumns() {
        return this.allColumns;
    }
    public Integer getRowKey(int rowModelIndex) {
        LtdSrhStruct<V> ltdSrhStruct = getLinesTableData(rowModelIndex);
        TableSection<V> section = ltdSrhStruct.linesTableData;
        int rowIndexInLinesTableData = rowModelIndex-ltdSrhStruct.offset;
        return section.getRowKey(rowIndexInLinesTableData);
    }
    public void clear() {
        tableSections.clear();
        headerInstalled = false;
    }
    //refactored from MainScreen::moveGameToThisHeader(Game, String)
    public boolean moveGameToThisHeader(V g, String header) {
        V thisgame = null;

        for (TableSection<V> gameLine : tableSections) {
            thisgame = gameLine.removeGameId(g.getGame_id(),false);
            if (thisgame != null) {
                break;
            }
        }
        // now lets see if i found it in either
        if ( null != thisgame ) // i did find it
        {
            TableSection<V> ltd = findTableSectionByHeaderValue(header);


            if ( null != ltd) {
                ltd.addGame(thisgame, false);
            } else {
                log( new Exception("can't find LinesTableData for header:"+header));
            }
            TableModelEvent evt = new TableModelEvent(this, 0, Integer.MAX_VALUE, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
            fireTableChanged(evt);
        }
        return null != thisgame;
    }
    public LinesTableData checktofire(int gameId) {
        List<TableSection<V>> gameLines = getTableSections();
        LinesTableData rtn = null;
        for (final TableSection<V> ltd : gameLines) {
            boolean status = ltd.checktofire(gameId);
            if (status) {
                rtn  = (LinesTableData)ltd;
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
    public int getRowModelIndex(TableSection<V> ltd, Integer key) {
        int gameIndex = ltd.getRowIndex(key);
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
        gameLine.setContainingTableModel(this);
        gameLine.setIndex(tableSections.size());
        tableSections.add(gameLine);
    }
    public void addGameLine(int index,TableSection<V> gameLine) {
        gameLine.setContainingTableModel(this);
        tableSections.add(index,gameLine);
        resetSectionIndex();
    }
    private void resetSectionIndex() {
        for(int i=0;i<tableSections.size();i++) {
            TableSection<V> section = tableSections.get(i);
            section.setIndex(i);
        }
    }
    public void buildIndexMappingCache() {
        for (int i=0;i<getRowCount();i++) {
            getLinesTableData(i);
        }
    }
    public LtdSrhStruct<V> getLinesTableData(int rowModelIndex) {

        return ltdSrhStructCache.computeIfAbsent(rowModelIndex,(index)-> {
            int modelIndex=0;
            TableSection<V> rtn = null;
            for(TableSection<V> sec: tableSections) {
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
