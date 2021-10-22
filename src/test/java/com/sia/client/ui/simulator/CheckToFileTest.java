package com.sia.client.ui.simulator;

import com.sia.client.ui.SpankOddsTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CheckToFileTest implements EventGenerator{

    public static final Integer [] gameIds = {1,3,5,7,9,11,101,103,105,107,109,111,113};
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        updateTestGame(tblProps);
    }
    private void updateTestGame(TableProperties [] tblProps) {
        for(int gameId: gameIds) {
            TestGame tg = SpankOddsTest.testGameCache.getGame(gameId);
            if (null != tg) {
                updateRowData(tg.getRowData(), 1);
            }
        }

        for(TableProperties tp: tblProps) {
            MainScreenTest mst = tp.getMainScreen();
            if ( mst.isShowing()) {
                mst.checktofire(Arrays.stream(gameIds).map(SpankOddsTest.testGameCache::getGame).collect(Collectors.toList()));
            }
        }
    }
    private static void updateRowData(List<Object> rowData, int col) {
        String oldValue = String.valueOf(rowData.get(col));
        if ( oldValue.length() < 15) {
            rowData.set(col, oldValue + "X");
        } else {
            System.out.println("Stop adding X.....");
        }
    }
}
