package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

import javax.swing.event.TableModelEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class NewHeaderCreator implements EventGenerator{
    private static final AtomicInteger counter = new AtomicInteger(0);
    @Override
    public void generatEvent(TableProperties [] tblProps) {
        TableProperties tblProp = tblProps[0];
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        TestTableSection section = new TestTableSection(tblProp.testGameCache,true, new ArrayList<>());
        section.setGameGroupHeader("NEW GAME GROUP "+counter.addAndGet(1));
        int insertedRow = 0;

        model.addGameLine(1,section);
        TableModelEvent e = new TableModelEvent(tblProp.table.getModel(), insertedRow, insertedRow, ALL_COLUMNS, TableModelEvent.INSERT);
        tblProp.table.getModel().fireTableChanged(e);
    }
}
