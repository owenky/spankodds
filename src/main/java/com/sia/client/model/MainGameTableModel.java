package com.sia.client.model;

import com.sia.client.ui.LinesTableData;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainGameTableModel extends DefaultTableModel {

    private final List<LinesTableData> gameLines = new ArrayList<>();
    private final LinesTableDataListner linesTableDataListner = new LinesTableDataListner();

    public MainGameTableModel() {

    }
    public void addGameLine(LinesTableData gameLine) {
        gameLine.setIndex(gameLines.size());
        gameLines.add(gameLine);
        removeAndAddTableModelListener(gameLine);
    }
    public int getRowModelIndex(LinesTableData ltd, int gameId) {
        int gameIndex = ltd.getRowIndex(gameId);
        int offset=0;
        for(int i=0;i<ltd.getIndex();i++) {
            offset+=gameLines.get(i).getRowCount();
        }
        return gameIndex+offset;
    }
    @Override
    public Object getValueAt(int rowModelIndex, int colModelIndex) {
        LtdSrhStruct ltdSrhStruct = getLinesTableData(rowModelIndex);
        LinesTableData r = ltdSrhStruct.linesTableData;
        return r.getValueAt((rowModelIndex-ltdSrhStruct.offset),colModelIndex);
    }
    @Override
    public int getRowCount() {

        if (null==gameLines)  {
            //first call to this method is from DefaultTableModel constructor before gameLines is instantialized.
            return 0;
        }
        int rowCount=0;
        for(LinesTableData r: gameLines) {
            rowCount += r.getRowCount();
        }
        return rowCount;
    }
    @Override
    public int getColumnCount() {
        return super.getColumnCount();
    }
    public LtdSrhStruct getLinesTableData(int rowModelIndex) {
        int modelIndex=0;
        LinesTableData rtn = null;
        for(LinesTableData r: gameLines) {
            if ( (modelIndex+r.getRowCount()) <= rowModelIndex) {
                modelIndex += r.getRowCount();
            } else {
                rtn = r;
                break;
            }
        }
        if ( null == rtn) {
            throw new IllegalStateException("rowModeIndex:"+rowModelIndex+" is out of bound");
        }
        return new LtdSrhStruct(rtn,modelIndex);
    }
    private void removeAndAddTableModelListener(LinesTableData gameLine) {
        for (TableModelListener l : gameLine.getTableModelListeners()) {
            if ( l instanceof LinesTableDataListner) {
                gameLine.removeTableModelListener(l);
            }
        }
        gameLine.addTableModelListener(linesTableDataListner);
    }
    @Override
    public void setValueAt(Object value,int rowModelIndex, int colModelIndex) {
        throw new IllegalStateException("Pending implementation");
    }
    @Override
    public void setDataVector(Vector dataVector, Vector columnIndetifiers) {
        if ( 0 == dataVector.size() && 0 == columnIndetifiers.size()) {
            //necessary for default constructor
            super.setDataVector(dataVector,columnIndetifiers);
        } else {
            throw new IllegalStateException("method not supported");
        }
    }
    @Override
    public void setDataVector(Object[][] dataVector, Object [] columnIndetifiers) {
        throw new IllegalStateException("method not supported");
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    public static class LtdSrhStruct {
        public final LinesTableData linesTableData;
        public final int offset;
        public LtdSrhStruct(LinesTableData linesTableData,int offset) {
            this.linesTableData = linesTableData;
            this.offset = offset;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////
    private class LinesTableDataListner implements TableModelListener {

        @Override
        public void tableChanged(final TableModelEvent e) {
            MainGameTableModel.this.fireTableChanged(e);
        }
    }
}
