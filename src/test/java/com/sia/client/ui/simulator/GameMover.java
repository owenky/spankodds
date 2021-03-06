package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.GameGroupHeader;

public class GameMover implements EventGenerator{
    private static int pointer=0;
    public static final int movedGameId = 2;
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        TableProperties tblProp = tblProps[0];
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        String targetHeader;
        if ( 0 == pointer++%2 ) {
            targetHeader = TestTableSection.TestGroupGameHeaderPrefix+1;
        } else {
            targetHeader = TestTableSection.TestGroupGameHeaderPrefix+0;
        }
        TestGame game = tblProp.testGameCache.getGame(movedGameId);
        model.moveGameToThisHeader(game, GameGroupHeader.create(targetHeader,null,0,0));
    }
}
