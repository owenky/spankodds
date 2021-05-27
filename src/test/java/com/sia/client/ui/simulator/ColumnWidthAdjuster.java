package com.sia.client.ui.simulator;

import javax.swing.event.TableModelEvent;
import java.util.List;

public class ColumnWidthAdjuster implements EventGenerator{

//    private static final ColumnAdjustScheduler COLUMN_ADJUST_SCHEDULER = new ColumnAdjustScheduler();
    private final int tesgGameId1 = 2;
    private final int testGameid2 = 103;
    public ColumnWidthAdjuster() {
    }
    @Override
    public void generatEvent(final TableProperties [] tblProps) {

        TableProperties tblProp = tblProps[0];
        updateTestGame(tblProp.testGameCache.getGame(tesgGameId1));
        updateTestGame(tblProp.testGameCache.getGame(testGameid2));

        tblProp.table.getModel().fireTableChanged(new TableModelEvent(tblProp.table.getModel()));
//        COLUMN_ADJUST_SCHEDULER.addRowData(new TestRowData(tblProp.table, updatedRow));
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
