package com.sia.client.ui;

import com.sia.client.config.SiaConst;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.List;

public class MainGameTable extends JTable {

    private final List<LinesTableData> gameLines = new ArrayList<>();
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if ( isSoccer(row,column) ) {
            return new LineRenderer(SiaConst.SoccerStr);
        } else {
            return new LineRenderer();
        }
    }
    public void addRegion(LinesTableData region) {
        gameLines.add(region);
    }
    @Override
    public Object getValueAt(int row, int col) {
        int modelIndex=0;
        Object value=null;
        for(LinesTableData r: gameLines) {
            if ( (modelIndex+r.getRowCount()) <= row) {
                modelIndex += r.getRowCount();
            } else {
                value = r.getValueAt((row-modelIndex),col);
                break;
            }
        }
        return value;
    }
    @Override
    public int getRowCount() {
        int rowCount=0;
        for(LinesTableData r: gameLines) {
            rowCount += r.getRowCount();
        }
        return rowCount;
    }
    private boolean isSoccer(int row, int col) {
        int modelIndex=0;
        boolean status = false;
        for(LinesTableData r: gameLines) {
            if ( (modelIndex+r.getRowCount()) <= row) {
                modelIndex += r.getRowCount();
            } else {
               status = SiaConst.SoccerStr.equalsIgnoreCase(r.getTitle());
               break;
            }
        }
        return status;
    }
}
