package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.model.Games;
import com.sia.client.model.SportType;
import com.sia.client.ui.TableUtils;
import com.sia.client.ui.control.MainScreen;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class RowSelection {

    @JsonProperty
    private Map<Integer, Set<Integer>> selectedGamesBySportId = new HashMap<>();
    @JsonIgnore
    private Set<String> lockedSportNames = new HashSet<>();

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
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSport(table).getSportId());
        return null != selectedRows && selectedRows.contains(gameId);
    }
    @JsonIgnore
    public void addSelectedGames(JComponent table, Integer ... gameIds) {
        addSelectedGames(table,Arrays.asList(gameIds));
    }
    @JsonIgnore
    public void addSelectedGames(JComponent table, List<Integer> gameIds) {
        SportType st = getSport(table);
        if ( ! lockedSportNames.contains(st.getSportName())) {
            Set<Integer> selectedRows = selectedGamesBySportId.computeIfAbsent(st.getSportId(), (key) -> new HashSet<>());
            selectedRows.addAll(gameIds);
        }
    }
    @JsonIgnore
    public void removeSelectedGames(JComponent table, Integer ... gameIds) {
        SportType st = getSport(table);
        if ( ! lockedSportNames.contains(st.getSportName())) {
            Set<Integer> selectedRows = selectedGamesBySportId.get(st.getSportId());
            if (null != selectedRows) {
                Arrays.asList(gameIds).forEach(selectedRows::remove);
            }
        }
    }
    @JsonIgnore
    public void clearSelectedGames(JComponent table) {
        SportType st = getSport(table);
        if ( ! lockedSportNames.contains(st.getSportName())) {
            Set<Integer> selectedRows = selectedGamesBySportId.get(st.getSportId());
            if (null != selectedRows) {
                selectedRows.clear();
            }
        }
    }
    @JsonIgnore
    public Collection<Integer> getSelectedGameIds(JComponent table) {
        Set<Integer> selectedRows = selectedGamesBySportId.get(getSport(table).getSportId());
        return null!=selectedRows?new HashSet<>(selectedRows):new HashSet<>();
    }
    @JsonIgnore
    public void setSportRowSelectionLocked(String sportName,boolean locked) {
        if ( locked) {
            lockedSportNames.add(sportName);
        } else {
            lockedSportNames.remove(sportName);
        }
    }
    @JsonIgnore
    public void syncWithGames() {
        for(Set<Integer> gamesBySport: selectedGamesBySportId.values()) {
            List<Integer> obsoleteGameIds = gamesBySport.stream()
                    .filter(key-> ! Games.instance().containsGameId(key))
                    .collect(Collectors.toList());

            obsoleteGameIds.forEach(key-> {
                gamesBySport.remove(key);
            });
        }
    }
    private static SportType getSport(JComponent jcomponent) {
        MainScreen mainScreen = TableUtils.findParent(jcomponent,MainScreen.class);
        return mainScreen.getSportType();
    }
}
