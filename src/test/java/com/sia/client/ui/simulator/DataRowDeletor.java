package com.sia.client.ui.simulator;

import javax.swing.event.TableModelEvent;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class DataRowDeletor implements EventGenerator{
    @Override
    public void generatEvent(final TableProperties tblProp) {
        int deletedRow = -1;
        for ( int i=0;i<tblProp.dataVector.size();i++) {
            LabeledList row = tblProp.dataVector.get(i);
            if ( null == row.getHeader()) {
                deletedRow = i;
                break;
            }
        }
        if ( deletedRow>=0) {
            tblProp.dataVector.remove(deletedRow);
            TableModelEvent e = new TableModelEvent(tblProp.table.getModel(), deletedRow, deletedRow, ALL_COLUMNS, TableModelEvent.DELETE);
            tblProp.table.getModel().fireTableChanged(e);
        }
    }
}
