package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.ui.AppController;

import java.util.HashMap;
import java.util.Map;

public class ColumnSettings {

    @JsonProperty
    private Map<String,Integer> columnWidthMap = new HashMap<>();
    private static final Map<Integer,Integer> defaultColumnWidthMap = new HashMap<>();

    static {
        defaultColumnWidthMap.put(990,60);  //Details
        defaultColumnWidthMap.put(994,80); //*Chart
        defaultColumnWidthMap.put(991,40);  //Time
        defaultColumnWidthMap.put(992,45);   //Gm#
        defaultColumnWidthMap.put(993,30); //Team
        defaultColumnWidthMap.put(SiaConst.BookieId.NoteColumnBookieId,40); //Note
    }

    @JsonProperty
    public Map<String, Integer> getColumnWidthMap() {
        return columnWidthMap;
    }
    @JsonProperty
    public void setColumnWidthMap(Map<String, Integer> columnWidthMap) {
        this.columnWidthMap = columnWidthMap;
    }
    @JsonIgnore
    public int getColumnWidth(Object columnHeaderValue,String sportName) {
        String columnName = String.valueOf(columnHeaderValue);
        int bookieId = AppController.getBookieId(String.valueOf(columnName));
        Integer width = columnWidthMap.get(keyValue(columnHeaderValue,sportName));
        if ( null == width) {
            width = defaultColumnWidthMap.get(bookieId);
            if ( null == width) {
                width = SiaConst.DefaultColumnWidth;
            }
        }
        return width;
    }
    @JsonIgnore
    public void setColumnWidth(Object columnName,String sportName,Integer width) {

        columnWidthMap.put(keyValue(columnName,sportName),width);
    }
    private static String keyValue(Object columnName, String sportName) {
        int bookieId = AppController.getBookieId(String.valueOf(columnName));
        return sportName+"_"+bookieId;
    }
}
