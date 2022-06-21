package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.model.BookieManager;
import com.sia.client.ui.AppController;

import java.util.HashMap;
import java.util.Map;

public class ColumnSettings {

    @JsonProperty
    private Map<Integer,Integer> columnWidthMap = new HashMap<>();
    private static final Map<Integer,Integer> defaultColumnWidthMap = new HashMap<>();

    static {
        defaultColumnWidthMap.put(990,60);  //Details
        defaultColumnWidthMap.put(994,80); //*Chart
        defaultColumnWidthMap.put(991,40);  //Time
        defaultColumnWidthMap.put(992,45);   //Gm#
        defaultColumnWidthMap.put(993,30); //Team
        defaultColumnWidthMap.put(BookieManager.NoteColumnBookieId,40); //Note
    }

    @JsonProperty
    public Map<Integer, Integer> getColumnWidthMap() {
        return columnWidthMap;
    }
    @JsonProperty
    public void setColumnWidthMap(Map<Integer, Integer> columnWidthMap) {
        this.columnWidthMap = columnWidthMap;
    }
    @JsonIgnore
    public int getColumnWidth(Object columnHeaderValue) {
        String columnName = String.valueOf(columnHeaderValue);
        int bookieId = AppController.getBookieId(String.valueOf(columnName));
        Integer width = columnWidthMap.get(bookieId);
        if ( null == width) {
            width = defaultColumnWidthMap.get(bookieId);
            if ( null == width) {
                width = SiaConst.DefaultColumnWidth;
            }
        }
        return width;
    }
    @JsonIgnore
    public void setColumnWidth(Object columnName,Integer width) {
        int bookieId = AppController.getBookieId(String.valueOf(columnName));
        columnWidthMap.put(bookieId,width);
    }
}
