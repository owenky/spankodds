package com.sia.client.ui;

public interface ColumnHeaderDrawer {

    boolean isColumnHeaderDrawn(Object columnHeaderValue);
    void drawColumnHeaderOnViewIndex(int rowViewIndex, Object columnHeaderValue);
}
