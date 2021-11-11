package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.TableSection;

public class NewGameCreator implements EventGenerator{

    public NewGameCreator() {
    }
    @Override
    public void generatEvent(final TableProperties [] tblProps) {
        TableProperties tblProp = tblProps[0];
        int insertedSectionIndex = 1;
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        TableSection<TestGame> section = model.getLinesTableDataWithSecionIndex(insertedSectionIndex);
        TestGame testGame = tblProp.testGameCache.makeTestGame(insertedSectionIndex*100+section.getRowCount());
        section.addOrUpdate(testGame);
    }
}
