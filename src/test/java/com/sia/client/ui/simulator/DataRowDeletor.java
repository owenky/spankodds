package com.sia.client.ui.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;

import javax.swing.event.TableModelEvent;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class DataRowDeletor implements EventGenerator{
    @Override
    public void generatEvent(final TableProperties tblProp) {
        int deletedRow = -1;
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        for ( int i=0;i<model.getRowCount();i++) {
            if ( ! SiaConst.BlankGameId.equals(model.getRowKey(i))) {
                deletedRow = i;
                break;
            }
        }
        if ( deletedRow>=0) {
            model.removeGame(model.getRowKey(deletedRow));
            TableModelEvent e = new TableModelEvent(tblProp.table.getModel(), deletedRow, deletedRow, ALL_COLUMNS, TableModelEvent.DELETE);
            tblProp.table.getModel().fireTableChanged(e);
        }
    }
}
