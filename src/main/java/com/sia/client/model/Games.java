package com.sia.client.model;

import com.sia.client.config.Config;

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
    @Override
    public Game removeGame(Integer gameId) {
        fireGameRemoveEvent();
        return super.removeGame(gameId);
    }
    @Override
    public void clear() {
        fireGameRemoveEvent();
        super.clear();
    }
    private void fireGameRemoveEvent() {
        Config.instance().setOutOfSync();
    }
}
