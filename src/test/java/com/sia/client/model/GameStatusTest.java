package com.sia.client.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GameStatusTest {

    @Test
    public void test() {
        String str = "1st H";
        GameStatus  status = GameStatus.find(str);
        assertEquals(status,GameStatus.InProgress);

        str = "";
        status = GameStatus.find(str);
        assertNull(status);

        str = "null";
        status = GameStatus.find(str);
        assertNull(status);

        str = "NULL";
        status = GameStatus.find(str);
        assertNull(status);

        str = null;
        status = GameStatus.find(str);
        assertNull(status);
    }
}
