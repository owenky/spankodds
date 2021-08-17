package com.sia.client.ui.simulator;

import com.sia.client.ui.SpankOddsTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

//test ColumnCustomizableDataModel::checktofire performance
public class CheckToFire implements EventGenerator{

    final Integer [] testGameIds = {2,103,1002,1103};
    final AtomicBoolean secondPaneSet = new AtomicBoolean(false);
    final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        if ( started.compareAndSet(false,true)) {
            startThread(tblProps);
        }

    }
    private void startThread(final TableProperties [] tblProps) {
        Thread thread = new Thread(()->
        {
            int count = 0;
            while (count++ < 60000) {
                for(int gameId:testGameIds ) {
                    for (TableProperties tblProp : tblProps) {
                        if ( gameId > 1000) {
                            if ( secondPaneSet.compareAndSet(false,true)) {
                                modifyTGames(tblProp,gameId, count);
                            } else {
                                continue;
                            }
                        } else {
                            modifyTGames(tblProp, gameId, count);
                        }
                    }
                }

                for (TableProperties tblProp : tblProps) {
                    if ( tblProp.getMainScreen().isShowing()) {
                        tblProp.getMainScreen().checktofire(Arrays.stream(testGameIds).map(SpankOddsTest.testGameCache::getGame).collect(Collectors.toList()));
                    }
                }
            }
        }
        );

        thread.start();
    }
    private void modifyTGames(TableProperties tblProp,int gameId,int count) {
        try {
            TestGame game = tblProp.testGameCache.getGame(gameId);
            List<Object> rowData = game.getRowData();
            String value = String.valueOf(count % 100);
            rowData.set(1, value);
            rowData.set(4, value);
            Thread.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
