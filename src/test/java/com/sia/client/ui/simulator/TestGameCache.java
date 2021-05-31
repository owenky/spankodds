package com.sia.client.ui.simulator;

import com.sia.client.model.KeyedObjectList;

public class TestGameCache extends KeyedObjectList<TestGame> {
    public static final int colCount = 46;
    @Override
    protected TestGame createInstance() {
        TestGame testGame = new TestGame();
        testGame.setColCount(colCount);
        return testGame;
    }
    public TestGame makeTestGame(int gameId) {
        TestGame testGame = createInstance();
        testGame.setGame_id(gameId);
        add(testGame);
        return testGame;
    }
}
