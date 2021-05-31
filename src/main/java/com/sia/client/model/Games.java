package com.sia.client.model;

public class Games extends KeyedObjectList<Game> {

    @Override
    protected Game createInstance() {
        return new Game();
    }
}
