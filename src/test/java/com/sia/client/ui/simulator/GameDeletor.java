package com.sia.client.ui.simulator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GameDeletor implements EventGenerator{
    private String [] idToDelete = {"2","102","201","202"};

    @Override
    public void generatEvent(final TableProperties [] tblProps) {
        Set<Integer> gameIdRemovedSet = Arrays.stream(idToDelete).map(Integer::parseInt).collect(Collectors.toSet());
        for(TableProperties prop: tblProps) {
            prop.getMainScreen().removeGamesAndCleanup(gameIdRemovedSet);
        }
    }
}
