package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

public class EmptySection implements EventGenerator{
    private static int pointer=0;
    public static final int movedGameId = 2;
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        TableProperties tblProp = tblProps[0];
        ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        //move all games from first section to second section
        TestTableSection [] sections = tblProp.getSections();
        String targetHeader = sections[1].getGameGroupHeader();
        for(int i=0;i<sections[0].getRowCount();i++) {
            TestGame game = sections[0].getGame(i);
            model.moveGameToThisHeader(game,targetHeader);
        }
    }
}
