package com.sia.client.ui.simulator;

import javax.swing.event.TableModelEvent;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class NewHeaderCreator implements EventGenerator{
    private static final AtomicInteger counter = new AtomicInteger(0);
    @Override
    public void generatEvent(TableProperties tblProp) {
        int insertedRow = 0;
        LabeledList headerRow = new LabeledList();
        for(int col=0;col<tblProp.columnCount;col++) {
            headerRow.add("");
        }
        headerRow.setHeader("New Header "+counter.addAndGet(1));
        tblProp.dataVector.add(insertedRow,headerRow);
        TableModelEvent e = new TableModelEvent(tblProp.table.getModel(), insertedRow, insertedRow, ALL_COLUMNS, TableModelEvent.INSERT);
        tblProp.table.getModel().fireTableChanged(e);
    }
}
