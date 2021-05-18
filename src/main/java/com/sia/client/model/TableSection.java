package com.sia.client.model;

import javax.swing.event.TableModelListener;

public interface TableSection<V extends KeyedObject> {

    Object getValueAt(int rowModelIndex,int colModelIndex);
    int getRowHeight();
    Object getHeaderValue();
    int getRowCount();
    int getIndex();
    void setIndex(int index);
    int getRowIndex(Integer rowKeyValue);
    Integer getRowKey(int rowModelIndex);
    V removeGameId(Integer keyValue);
    void addGame(V rowData,boolean toRepain);
    void resetDataVector();
    String getGameGroupHeader();
    boolean checktofire(Integer keyValue);

    void addTableModelListener(TableModelListener l);
    void removeTableModelListener(TableModelListener l);
    TableModelListener [] getTableModelListeners();

    default boolean hasHeader() {
        return null != getGameGroupHeader();
    }
}
