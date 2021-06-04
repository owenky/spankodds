package com.sia.client.ui.simulator;

public class GameDeletor implements EventGenerator{
    private String [] idToDelete = {"2","102","201","202"};

    @Override
    public void generatEvent(final TableProperties [] tblProps) {
        for(TableProperties prop: tblProps) {
            prop.getMainScreen().removeGames(idToDelete);
        }
    }
}
