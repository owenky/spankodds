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
        for(int i=sections[0].getRowCount()-1;i>=0;i--) {
            TestGame game = sections[0].getGame(i);
            int gameid = game.getGame_id();
            if ( gameid >= 0) {
                model.moveGameToThisHeader(game, targetHeader);
            }
        }
    }
}
