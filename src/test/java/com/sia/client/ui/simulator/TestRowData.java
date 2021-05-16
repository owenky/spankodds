package com.sia.client.ui.simulator;

import com.sia.client.model.TableModelRowData;
import com.sia.client.ui.ColumnCustomizableTable;

public class TestRowData implements TableModelRowData {

    private final ColumnCustomizableTable testTable;
    private final int rowModelIndex;

    public TestRowData(ColumnCustomizableTable testTable, int rowModelIndex) {
        this.testTable = testTable;
        this.rowModelIndex = rowModelIndex;
    }

    @Override
    public Integer getRowModelIndex() {
        return rowModelIndex;
    }

    @Override
    public ColumnCustomizableTable getTable() {
        return testTable;
    }
}
