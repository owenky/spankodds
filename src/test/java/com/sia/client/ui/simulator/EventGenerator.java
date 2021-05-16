package com.sia.client.ui.simulator;

import java.util.Set;

public interface EventGenerator {

    void generatEvent(TableProperties tblProp);
    static LabeledList makeRow(int row, int colCount, Set<Integer> barRowSet) {
        LabeledList rowData = new LabeledList();
        for (int i = 0; i < colCount; i++) {
            rowData.add("" + row + "_" + i);
        }
        if (barRowSet.contains(row)) {
            rowData.setHeader("TEST ROW "+row);
        }
        return rowData;
    }
}
