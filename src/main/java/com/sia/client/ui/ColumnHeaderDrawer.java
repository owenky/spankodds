package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;

public interface ColumnHeaderDrawer {

    boolean isColumnHeaderDrawn(Object columnHeaderValue);
    public void drawColumnHeaderOnViewIndex(ColumnHeaderProperty columnHeaderProperty,int rowViewIndex, Object columnHeaderValue);
}
