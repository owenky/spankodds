package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.ui.TableUtils;
import com.sia.client.ui.control.MainScreen;

import javax.swing.*;
import java.util.*;

public class RowSelection {

    private Map<Integer, Set<Integer>> selectedGamesBySportId = new HashMap<>();

    @JsonProperty
    public Map<Integer, Set<Integer>> getSelectedGamesBySportId() {
        return selectedGamesBySportId;
    }
    @JsonProperty
    public void setSelectedGamesBySportId(Map<Integer, Set<Integer>> selectedGamesBySportId) {
        this.selectedGamesBySportId = selectedGamesBySportId;
    }
    @JsonIgnore
    public boolean isGameSelected(JComponent table , int gameId) {
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSportId(table));
        return null != selectedRows && selectedRows.contains(gameId);
    }
    @JsonIgnore
    public void addSelectedGames(JComponent table, Integer ... gameIds) {
        Set<Integer> selectedRows = selectedGamesBySportId.computeIfAbsent(getSportId(table), (key)->new HashSet<>());
        selectedRows.addAll(Arrays.asList(gameIds));
    }
    @JsonIgnore
    public void removeSelectedGames(JComponent table, Integer ... gameIds) {
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSportId(table));
        if ( null != selectedRows) {
            Arrays.asList(gameIds).forEach(selectedRows::remove);
        }
    }
    @JsonIgnore
    public void clearSelectedGames(JComponent table) {
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSportId(table));
        if ( null != selectedRows) {
            selectedRows.clear();
        }
    }
    @JsonIgnore
    public Collection<Integer> getSelectedGameIds(JComponent table) {
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSportId(table));
        return null!=selectedRows?new HashSet<>(selectedRows):new HashSet<>();
    }
    private static Integer getSportId(JComponent jcomponent) {
        MainScreen mainScreen = TableUtils.findParent(jcomponent,MainScreen.class);
        return mainScreen.getSportType().getSportId();
    }
}
