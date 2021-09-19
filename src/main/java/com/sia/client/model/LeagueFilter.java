package com.sia.client.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LeagueFilter {

    private final boolean selectAll;
    private final Set<Integer> selectedIds;
    public LeagueFilter(String [] selectedIdStr, boolean selectAll) {
        this.selectAll = selectAll;
        Set<Integer> selectedIdsTmp = null;
        if ( !selectAll) {
            selectedIdsTmp = Arrays.stream(selectedIdStr).map(Integer::parseInt).collect(Collectors.toSet());
        }
        selectedIds = selectedIdsTmp;
    }
    public boolean isSelected(int leagueId) {
        return selectAll || selectedIds.contains(leagueId);
    }
}
