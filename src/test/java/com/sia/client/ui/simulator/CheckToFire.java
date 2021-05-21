package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//test ColumnCustomizableDataModel::checktofire performance
public class CheckToFire implements EventGenerator{

    final int [] testGameIds = {2,103,1002,1103};
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
                        final ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();

                        boolean isShowing = tblProp.table.isShowing();
                        System.out.println("table " + tblProp.table.getName() + " isDisplayable=" + isShowing);
                        if ( gameId > 1000) {
                            if ( secondPaneSet.compareAndSet(false,true)) {
                                checkToFile(model, tblProp,gameId,isShowing, count);
                            } else {
                                continue;
                            }
                        } else {
                            checkToFile(model, tblProp, gameId, isShowing, count);
                        }
                    }
                }
            }
        }
        );

        thread.start();
    }
    private void checkToFile(ColumnCustomizableDataModel<TestGame> model,TableProperties tblProp,int gameId,boolean repaint,int count) {
        model.checktofire(gameId,repaint);
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
