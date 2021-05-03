package com.sia.client.ui;


import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Games;
import org.junit.Test;

import javax.swing.table.TableColumn;
import java.util.Vector;

import static junit.framework.Assert.assertEquals;

public class MainGameTableTest {

    private static Games gameCache = new Games();
    @Test
    public void testgetValueAt() {
        MainGameTable table = new MainGameTable();
        table.addColumn(new TableColumn());
        table.addGameLine( makeGameRegion(10));
        table.addGameLine( makeGameRegion(20));
        table.addGameLine( makeGameRegion(30));
        table.addGameLine( makeGameRegion(40));

        assertEquals(10,table.getValueAt(0,0));
        assertEquals(100,table.getValueAt(1,0));

        assertEquals(20,table.getValueAt(3,0));

        assertEquals(40,table.getValueAt(9,0));
        assertEquals(4000,table.getValueAt(11,0));
    }
    private LinesTableData makeGameRegion(int seed) {

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

        gameCache.add(g);
        LinesTableData gr = new LinesTableData(games,columns,gameCache);
        return gr;
    }
}
