package com.sia.client.model;

import com.sia.client.ui.LinesTableData;

public interface LinesTableDataSupplier {

    LinesTableData getLinesTableData(int row);
}
