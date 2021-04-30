package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.LinesTableData;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class MainGameTableModel extends DefaultTableModel {

    private final List<LinesTableData> gameLines = new ArrayList<>();

    public void addGameLine(LinesTableData gameLine) {
        gameLines.add(gameLine);
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
    public boolean isSoccer(int row) {
        LtdSrhStruct ltdSrhStruct = getLinesTableData(row);
        return SiaConst.SoccerStr.equalsIgnoreCase(ltdSrhStruct.linesTableData.getTitle());
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
////////////////////////////////////////////////////////////////////////////////////////////////
    public static class LtdSrhStruct {
        public final LinesTableData linesTableData;
        public final int offset;
        public LtdSrhStruct(LinesTableData linesTableData,int offset) {
            this.linesTableData = linesTableData;
            this.offset = offset;
        }
    }
}
