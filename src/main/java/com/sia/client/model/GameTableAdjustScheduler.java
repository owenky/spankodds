package com.sia.client.model;

import com.sia.client.ui.ColumnCustomizableTable;

public abstract class GameTableAdjustScheduler {

    private static final ColumnAdjustScheduler columnAdjustScheduler = new ColumnAdjustScheduler();

    public static <T extends KeyedObject> void adjustColumn(ColumnCustomizableTable<T> mainGameTable, TableSection<T> ltd, int gameid) {
        ColAdjustStruct<T> colAdjustStr = new ColAdjustStruct<>(mainGameTable,ltd,gameid);
        columnAdjustScheduler.addRowData(colAdjustStr);
    }
    /////////////////////////////////////////////////////////////////////////////////////
    private static class ColAdjustStruct<T extends KeyedObject> implements TableModelRowData {
        private final ColumnCustomizableTable<T> mainGameTable;
        private final TableSection<T> ltd;
        private final int gameid;
        private ColAdjustStruct(ColumnCustomizableTable<T> mainGameTable, TableSection<T> ltd, int gameid) {
            this.mainGameTable = mainGameTable;
            this.ltd = ltd;
            this.gameid = gameid;
        }

        @Override
        public Integer getRowModelIndex() {
            return mainGameTable.getModel().getRowModelIndexByGameId(ltd,gameid);
        }

        @Override
        public ColumnCustomizableTable<?> getTable() {
            return mainGameTable;
        }
    }
}
