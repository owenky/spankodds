package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

import java.util.List;

//test ColumnCustomizableDataModel::checktofire performance
public class CheckToFire implements EventGenerator{

    @Override
    public void generatEvent(final TableProperties tblProp) {

        final int testGameId = 2;
        final ColumnCustomizableDataModel<TestGame> model = tblProp.table.getModel();
        Thread thread = new Thread(()->
            {
                int count=0;
                while ( count++ < 60000) {
                    model.checktofire(testGameId);
                    try {
                        TestGame game = tblProp.testGameCache.getGame(testGameId);
                        List<Object> rowData = game.getRowData();
                        String value = String.valueOf(count%100);
                        rowData.set(1,value);
                        rowData.set(4, value);
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        );

        thread.start();
    }
}
