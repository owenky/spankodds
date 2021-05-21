package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

public class GameDeletor implements EventGenerator{
    private int [] idToDelete = {2,102,201,202};
    private static int pointer=0;
    @Override
    public void generatEvent(final TableProperties [] tblProps) {
        TableProperties tblProp = tblProps[0];
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        int gameId = idToDelete[pointer];
        model.removeGame(gameId,true);
        if ( (++pointer) >=4) {
            pointer = 0;
        }
    }
}
