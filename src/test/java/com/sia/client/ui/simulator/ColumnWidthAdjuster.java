package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnAdjustScheduler;

import javax.swing.event.TableModelEvent;

import static com.sia.client.config.SiaConst.BlankGameId;

public class ColumnWidthAdjuster implements EventGenerator{

    private static final ColumnAdjustScheduler COLUMN_ADJUST_SCHEDULER = new ColumnAdjustScheduler();
    private final int updatedRow;
    public ColumnWidthAdjuster(int updatedRow) {
        this.updatedRow = updatedRow;
    }
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        TableProperties tblProp = tblProps[0];
        int firstDataRow=0;
        while ( BlankGameId.equals(tblProp.table.getModel().getRowKey(firstDataRow))) {
            firstDataRow++;
        }

        int col = 0;
        Object value = tblProp.table.getModel().getValueAt(firstDataRow,col);
        tblProp.table.getModel().setValueAt(value+"X",firstDataRow,col);

        col = 5;
        value = tblProp.table.getModel().getValueAt(firstDataRow,col);
        tblProp.table.getModel().setValueAt(value+"X",firstDataRow,col);

        TableModelEvent te = new TableModelEvent(tblProp.table.getModel(), updatedRow);
        tblProp.table.getModel().fireTableChanged(te);
        COLUMN_ADJUST_SCHEDULER.addRowData(new TestRowData(tblProp.table, updatedRow));
    }
}
