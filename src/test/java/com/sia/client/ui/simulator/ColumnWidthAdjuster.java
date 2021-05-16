package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnAdjustScheduler;

import javax.swing.event.TableModelEvent;

public class ColumnWidthAdjuster implements EventGenerator{

    private static final ColumnAdjustScheduler COLUMN_ADJUST_SCHEDULER = new ColumnAdjustScheduler();
    private final int updatedRow;
    public ColumnWidthAdjuster(int updatedRow) {
        this.updatedRow = updatedRow;
    }
    @Override
    public void generatEvent(final TableProperties tblProp) {
        int col = 0;
        String value = tblProp.dataVector.get(updatedRow).get(col);
        tblProp.dataVector.get(updatedRow).set(col, value + "XX");

        col = 5;
        value = tblProp.dataVector.get(updatedRow).get(col);
        tblProp.dataVector.get(updatedRow).set(col, value + "XX");

        TableModelEvent te = new TableModelEvent(tblProp.table.getModel(), updatedRow);
        tblProp.table.getModel().fireTableChanged(te);
        COLUMN_ADJUST_SCHEDULER.addRowData(new TestRowData(tblProp.table, updatedRow));
    }
}
