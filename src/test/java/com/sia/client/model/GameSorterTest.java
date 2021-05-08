package com.sia.client.model;

import com.sia.client.config.SiaConst;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GameSorterTest {

    @Test
    public void testGameDateSorter() {
        List<Game> gameList = new ArrayList<>();
        Game game = new Game();
        game.setGame_id(100);
        gameList.add(game);

        game = new Game();
        game.setGame_id(SiaConst.BlankGameId);
        gameList.add(game);

        game = new Game();
        game.setGame_id(-1);
        gameList.add(game);

        game = new Game();
        game.setGame_id(SiaConst.BlankGameId);
        gameList.add(game);

        gameList.sort(new GameDateSorter() );

        assertTrue(SiaConst.BlankGameId==gameList.get(0).getGame_id());
        assertTrue(SiaConst.BlankGameId==gameList.get(1).getGame_id());
    }
}
