//package com.sia.client.model;
//
//import com.sia.client.config.SiaConst;
//
//import java.awt.Color;
//import java.awt.Font;
//
//public class ColumnHeaderProvider<V extends KeyedObject> {
//
//    private ColumnHeaderProperty columnHeaderProperty = null;
//    private ColumnCustomizableDataModel<V> tableModel;
//    private RowHeaderProvider rowHeaderProvider;
//
//    public ColumnHeaderProvider() {
//    }
//    public void setTableModel(ColumnCustomizableDataModel<V> mainTable) {
//        this.tableModel = mainTable;
//        rowHeaderProvider = new RowHeaderProviderImpl<>(tableModel);
//    }
//    public Color getHeaderBackground() {
//        return getColumnHeaderProperty().getHeaderBackground();
//    }
//    public Color getHeaderForeground() {
//        return getColumnHeaderProperty().getHeaderForeground();
//    }
//
//    public Font getHeaderFont() {
//        return getColumnHeaderProperty().getHeaderFont();
//    }
//
//    public int getColumnHeaderHeight() {
//        return getColumnHeaderProperty().getColumnHeaderHeight();
//    }
//    public synchronized void resetColumnHeaderProperty() {
//        columnHeaderProperty = provide();
//    }
//    public Object getColumnHeaderAt(int rowModelIndex) {
//        return getColumnHeaderProperty().getColumnHeaderAt(rowModelIndex);
//    }
//    private synchronized ColumnHeaderProperty getColumnHeaderProperty() {
//        if ( null == columnHeaderProperty) {
//            columnHeaderProperty = provide();
//        }
//        return columnHeaderProperty;
//    }
//    private ColumnHeaderProperty provide() {
//        ColumnHeaderProperty columnHeaderProperty;
//        columnHeaderProperty = new ColumnHeaderProperty(SiaConst.DefaultHeaderColor,SiaConst.DefaultHeaderFontColor, SiaConst.DefaultHeaderFont, SiaConst.GameGroupHeaderHeight, rowHeaderProvider);
//        return columnHeaderProperty;
//    }
//}
