package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.ColumnCustomizableTable;

import java.awt.Color;
import java.awt.Font;

public class ColumnHeaderProvider<V extends KeyedObject> {

    private ColumnHeaderProperty columnHeaderProperty = null;
    private ColumnCustomizableTable<V> mainTable;

    public ColumnHeaderProvider() {
    }
    public void setMainTable(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
    }
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
    private ColumnHeaderProperty provide() {
        ColumnHeaderProperty columnHeaderProperty;
        columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor,SiaConst.DefaultHeaderFontColor, SiaConst.DefaultHeaderFont, SiaConst.GameGroupHeaderHeight,
                mainTable.getRowModelIndex2GameGroupHeaderMap());
        return columnHeaderProperty;
    }
}
