package com.sia.client.model;

public class Games extends KeyedObjectList<Game> {

    private static final Games instance = new Games();

    public static Games instance() {
        return instance;
    }
    private Games() {

    }
    @Override
    protected Game createElement() {
        return new Game();
    }
}
