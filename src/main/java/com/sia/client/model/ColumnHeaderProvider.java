package com.sia.client.model;

import java.awt.Color;
import java.awt.Font;

public abstract class ColumnHeaderProvider {

    private ColumnHeaderProperty columnHeaderProperty = null;

    abstract protected ColumnHeaderProperty provide();

    public Color getHeaderBackground() {
        return getColumnHeaderProperty().getHeaderBackground();
    }
    public Color getHeaderForeground() {
        return getColumnHeaderProperty().getHeaderForeground();
    }

    public Font getHeaderFont() {
        return getColumnHeaderProperty().getHeaderFont();
    }

    public int getColumnHeaderHeight() {
        return getColumnHeaderProperty().getColumnHeaderHeight();
    }
    public Object getColumnHeaderAt(int rowModelIndex) {
        return getColumnHeaderProperty().getColumnHeaderAt(rowModelIndex);
    }
    public synchronized void resetColumnHeaderProperty() {
        columnHeaderProperty = null;
    }
    private synchronized ColumnHeaderProperty getColumnHeaderProperty() {
        if ( null == columnHeaderProperty) {
            columnHeaderProperty = provide();
        }
        return columnHeaderProperty;
    }
}
