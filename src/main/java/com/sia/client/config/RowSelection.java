package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class RowSelection {

    private Map<String, Set<Integer>> selectedRowsByTable = new HashMap<>();

    @JsonProperty
    public Map<String, Set<Integer>> getSelectedRowsByTable() {
        return selectedRowsByTable;
    }
    @JsonProperty
    public void setSelectedRowsByTable(Map<String, Set<Integer>> selectedRowsByTable) {
        this.selectedRowsByTable = selectedRowsByTable;
    }
    @JsonIgnore
    public boolean isRowSelected(String tableName,int gameId) {
        Set<Integer> selectedRows = selectedRowsByTable.get(tableName);
        return null != selectedRows && selectedRows.contains(gameId);
    }
    @JsonIgnore
    public void addSelectedRows(String tableName,Integer ... gameIds) {
        Set<Integer> selectedRows = selectedRowsByTable.computeIfAbsent(tableName, (key)->new HashSet<>());
        selectedRows.addAll(Arrays.asList(gameIds));
    }
    @JsonIgnore
    public void removeSelectedRows(String tableName,Integer ... gameIds) {
        Set<Integer> selectedRows = selectedRowsByTable.get(tableName);
        if ( null != selectedRows) {
            Arrays.asList(gameIds).forEach(selectedRows::remove);
        }
    }
    @JsonIgnore
    public void clearSelectedRows(String tableName) {
        Set<Integer> selectedRows = selectedRowsByTable.get(tableName);
        if ( null != selectedRows) {
            selectedRows.clear();
        }
    }
}
