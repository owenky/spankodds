package com.sia.client.ui.simulator;

import com.sia.client.model.KeyedObject;

public class TestGame implements KeyedObject {

    private int gameId;
    @Override
    public int getGame_id() {
        return gameId;
    }

    @Override
    public void setGame_id(final int gameId) {
        this.gameId = gameId;
    }
}
