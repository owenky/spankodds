package com.sia.client.ui.simulator;

import com.sia.client.model.KeyedObjectCollection;

public class TestGameCache extends KeyedObjectCollection<TestGame>  {
    @Override
    protected TestGame createInstance() {
        return new TestGame();
    }
    public TestGame makeTestGame(int gameId) {
        TestGame testGame = createInstance();
        testGame.setGame_id(gameId);
        add(testGame);
        return testGame;
    }
}
