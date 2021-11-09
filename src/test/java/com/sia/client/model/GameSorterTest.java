package com.sia.client.model;

import com.sia.client.config.SiaConst;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameSorterTest {

    @Test
    public void testGameSorter() {
        List<Game> gameList = new ArrayList<>();
        Game game = new Game();
        game.setGame_id(100);
        gameList.add(game);

        game = new Game();
        game.setGame_id(SiaConst.BlankGameId);
        gameList.add(game);

        game = new Game();
        game.setGame_id(3);
        gameList.add(game);

        game = new Game();
        game.setGame_id(1);
        gameList.add(game);

        gameList.sort(new GameSorter() );

        assertTrue(SiaConst.BlankGameId==gameList.get(0).getGame_id());
        assertTrue(1==gameList.get(1).getGame_id());
    }
    @Test
    public void testGameDateSorter() {

        GameGroupDateSorter sorter = new GameGroupDateSorter();
        LocalDateTime ltd1 = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime ltd2 = LocalDateTime.of(2020, 10, 1, 0, 0);
        LocalDateTime ltd3 = LocalDateTime.of(2020, 1, 1, 0, 0);
        GameGroupHeader ggh1 = GameGroupHeader.create("", ltd1,0,0);
        GameGroupHeader ggh2 = GameGroupHeader.create("", ltd2,0,0);
        GameGroupHeader ggh3 = GameGroupHeader.create("", ltd3,0,0);
        List<GameGroupHeader> gghList = new ArrayList<>();
        gghList.add(ggh1);
        gghList.add(ggh2);
        gghList.add(ggh3);

        gghList.sort(sorter);

        assertSame(ggh3, gghList.get(0));
        assertEquals(ggh1, gghList.get(2));
    }
}
