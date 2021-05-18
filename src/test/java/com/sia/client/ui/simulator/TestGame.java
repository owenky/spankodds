package com.sia.client.ui.simulator;

import com.sia.client.model.KeyedObject;

public class TestGame implements KeyedObject {

    private int gameId;
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    @Override
    public int getGame_id() {
        return gameId;
    }
}
