package com.sia.client.model;

import com.sia.client.ui.ColumnCustomizableTable;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.MainGameTable;

public abstract class GameTableAdjustScheduler {

    private static final ColumnAdjustScheduler columnAdjustScheduler = new ColumnAdjustScheduler();

    public static void adjustColumn(MainGameTable mainGameTable, LinesTableData ltd, int gameid) {
        ColAdjustStruct colAdjustStr = new ColAdjustStruct(mainGameTable,ltd,gameid);
        columnAdjustScheduler.addRowData(colAdjustStr);
    }
    /////////////////////////////////////////////////////////////////////////////////////
    private static class ColAdjustStruct implements TableModelRowData {
        private final MainGameTable mainGameTable;
        private final LinesTableData ltd;
        private final int gameid;
        private ColAdjustStruct(MainGameTable mainGameTable, LinesTableData ltd, int gameid) {
            this.mainGameTable = mainGameTable;
            this.ltd = ltd;
            this.gameid = gameid;
        }

        @Override
        public Integer getRowModelIndex() {
            return mainGameTable.getModel().getRowModelIndex(ltd,gameid);
        }

        @Override
        public ColumnCustomizableTable getTable() {
            return mainGameTable;
        }
    }
}
