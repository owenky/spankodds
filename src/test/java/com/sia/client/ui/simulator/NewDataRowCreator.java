package com.sia.client.ui.simulator;

import javax.swing.event.TableModelEvent;
import java.util.Set;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class NewDataRowCreator implements EventGenerator{

    private final Set<Integer> barRowSet;
    public NewDataRowCreator(Set<Integer> barRowSet) {
        this.barRowSet = barRowSet;
    }
    @Override
    public void generatEvent(final TableProperties tblProp) {
        int insertedRow = 2;
        LabeledList newRow = EventGenerator.makeRow(insertedRow, tblProp.columnCount,barRowSet);
        tblProp.dataVector.add(insertedRow, newRow);
        TableModelEvent e = new TableModelEvent(tblProp.table.getModel(), insertedRow, insertedRow, ALL_COLUMNS, TableModelEvent.INSERT);
        tblProp.table.getModel().fireTableChanged(e);
    }
}
