package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.TableSection;

import java.util.Set;

public class NewDataRowCreator implements EventGenerator{

    private final Set<Integer> barRowSet;
    public NewDataRowCreator(Set<Integer> barRowSet) {
        this.barRowSet = barRowSet;
    }
    @Override
    public void generatEvent(final TableProperties tblProp) {
        int insertedSectionIndex = 2;
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        TableSection<TestGame> section = model.getLinesTableDataWithSecionIndex(insertedSectionIndex);
        TestGame testGame = tblProp.testGameCache.makeTestGame(insertedSectionIndex*100+section.getRowCount());
        section.addGame(testGame,true);
    }
}
