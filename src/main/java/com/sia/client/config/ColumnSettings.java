package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ColumnSettings {

    @JsonProperty
    private Map<String,Integer> columnWidthMap = new HashMap<>();
    private static final Map<String,Integer> defaultColumnWidthMap = new HashMap<>();

    static {
        defaultColumnWidthMap.put("Details",60);  //60
        defaultColumnWidthMap.put("Dtals",60);   //60
        defaultColumnWidthMap.put("*Chart",80); //80
        defaultColumnWidthMap.put("Time",40);  //40
        defaultColumnWidthMap.put("Gm#",45);   //45
        defaultColumnWidthMap.put("Team",45); //30
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
    public int getColumnWidth(Object columnHeaderValue) {
        String columnName = String.valueOf(columnHeaderValue);
        Integer width = columnWidthMap.get(columnName);
        if ( null == width) {
            width = defaultColumnWidthMap.get(columnName);
            if ( null == width) {
                width = SiaConst.DefaultColumnWidth;
            }
        }
        return width;
    }
    @JsonIgnore
    public void setColumnWidth(String columnName,Integer width) {
        columnWidthMap.put(columnName,width);
    }
}
