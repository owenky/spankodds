package com.sia.client.ui.simulator;

import java.util.List;

public class CheckToFileTest implements EventGenerator{
    private static int pointer=0;
    public static final int movedGameId = 2;
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

//        TableProperties tblProp = tblProps[0];
//        updateTestGame(tblProp.testGameCache.getGame(tesgGameId1));
//        updateTestGame(tblProp.testGameCache.getGame(testGameid2));
//
//        tblProp.table.getModel().fireTableChanged(new TableModelEvent(tblProp.table.getModel()));
    }
    private void updateTestGame(TestGame game) {

        updateRowData(game.getRowData(),1);
        updateRowData(game.getRowData(),40);
    }
    private void updateRowData(List<Object> rowData, int col) {
        String oldValue = String.valueOf(rowData.get(col));
        if ( oldValue.length() < 15) {
            rowData.set(col, oldValue + "X");
        } else {
            System.out.println("Stop adding X.....");
        }
    }
}
