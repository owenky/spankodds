package com.sia.client.model;

public class Games extends KeyedObjectCollection<Game> {

    @Override
    protected Game createInstance() {
        return new Game();
    }
}
