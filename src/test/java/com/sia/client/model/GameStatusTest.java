package com.sia.client.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GameStatusTest {

    @Test
    public void test() {
        Game g = new Game();
        String str = "1st H";
        g.setStatus(str);
        GameStatus  status = GameStatus.getGameStatus(g);
        assertEquals(status,GameStatus.InProgress);

        str = "";
        g.setStatus(str);
        status = GameStatus.getGameStatus(g);
        assertNull(status);

        str = "null";
        g.setStatus(str);
        status = GameStatus.getGameStatus(g);
        assertNull(status);

        str = "NULL";
        g.setStatus(str);
        status = GameStatus.getGameStatus(g);
        assertNull(status);

        str = null;
        g.setStatus(str);
        status = GameStatus.getGameStatus(g);
        assertNull(status);
    }
}
