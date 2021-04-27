package com.sia.client.model;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class GameRegion {

    private final DefaultTableModel tableModel;
    private String title;

    public GameRegion() {
        this.tableModel = new DefaultTableModel();
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public int getRowCount() {
        return tableModel.getRowCount();
    }
    public Object getValueAt(int row, int col){
        return tableModel.getValueAt(row,col);
    }
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
        tableModel.setDataVector(dataVector,columnIdentifiers);
    }
}
