package com.sia.client.model;

import com.sia.client.ui.ColumnCustomizableTable;

public interface AbstratScreen<T extends KeyedObject> {

    ColumnCustomizableTable<T> getColumnCustomizableTable();
    default void checktofire(int gameid) {
        ColumnCustomizableTable<T> table = getColumnCustomizableTable();
        ColumnCustomizableDataModel<T> model = table.getModel();
        TableSection<T> ltd = model.checktofire(gameid,table.isShowing());
        boolean status = (null != ltd);
        if (status) {
            GameTableAdjustScheduler.adjustColumn(table, ltd, gameid);
        }
    }
}
