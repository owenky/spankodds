package com.sia.client.model;

import com.sia.client.ui.LinesTableData;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class MainGameTableModelTest {
    private static Games gameCache = new Games();
    @Test
    public void testGetRowModelIndex() {
        MainGameTableModel model = new MainGameTableModel();
        LinesTableData l1 =  makeLinesTableData(10);
        LinesTableData l2 =  makeLinesTableData(20);
        LinesTableData l3 = makeLinesTableData(30);
        LinesTableData l4 = makeLinesTableData(40);

        model.addGameLine(l1);
        model.addGameLine(l2);
        model.addGameLine(l3);
        model.addGameLine(l4);

        int rowModelIndex = model.getRowModelIndex(l3,300);
        assertEquals(9,rowModelIndex);
    }
    private LinesTableData makeLinesTableData(int seed) {

        Vector<Bookie> columns = new Vector<>();
        columns.add(new Bookie(-1,"test","test","test","test"));

        Vector<Game> games = new Vector<>();
        Game g = new Game();
        g.setGame_id(seed);
        games.add(g);

        g = new Game();
        g.setGame_id(seed*10);
        games.add(g);

        g = new Game();
        g.setGame_id(seed*100);
        games.add(g);

        g = new Game();
        g.setGame_id(seed*1000);
        games.add(g);

        gameCache.addAll(games);
        LinesTableData gr = new LinesTableData(games,columns,gameCache);
        return gr;
    }
}
