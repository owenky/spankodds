package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.LinesTableDataSupplier;

public class RowHeaderGameTable extends RowHeaderTable<Game> implements LinesTableDataSupplier {

    public RowHeaderGameTable(MainGameTable mainTable,boolean hasRowNumber) {
        super(mainTable,hasRowNumber);
    }

    @Override
    public LinesTableData getLinesTableData(final int row) {
        return ((MainGameTable)getMainTable()).getLinesTableData(row);
    }
}
