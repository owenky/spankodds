package com.sia.client.model;

import com.sia.client.ui.simulator.TestGame;
import com.sia.client.ui.simulator.TestGameCache;
import com.sia.client.ui.simulator.TestTableSection;
import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class MainGameTableModelTest {
    private TestGameCache gameCache = new TestGameCache();
    private ColumnCustomizableDataModel<TestGame> model;
    @Before
    public void init() {
        gameCache = new TestGameCache();
        Vector<TableColumn> allColumn = new Vector<>();
        allColumn.add(new TableColumn());
        model = new ColumnCustomizableDataModel<>(allColumn);
    }
    @Test
    public void testAddGameLine() {
        TableSection<TestGame> l1 =  makeLinesTableData(10);
        TableSection<TestGame> l2 =  makeLinesTableData(20);
        model.addGameLine(l1);
        model.addGameLine(l2);

        assertEquals(1,l2.getIndex());
    }
    @Test
    public void testGetRowModelIndex() {

        TableSection<TestGame> l1 =  makeLinesTableData(10);
        TableSection<TestGame> l2 =  makeLinesTableData(20);
        TableSection<TestGame> l3 = makeLinesTableData(30);
        TableSection<TestGame> l4 = makeLinesTableData(40);

        model.addGameLine(l1);
        model.addGameLine(l2);
        model.addGameLine(l3);
        model.addGameLine(l4);

        int rowModelIndex = model.getRowModelIndex(l3,31);
        assertEquals(12,rowModelIndex);
    }
    private TableSection<TestGame> makeLinesTableData(int seed) {

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(seed));
        games.add(gameCache.makeTestGame(seed+1));
        games.add(gameCache.makeTestGame(seed+2));
        games.add(gameCache.makeTestGame(seed+3));

        gameCache.addAll(games);
        return new TestTableSection(gameCache, true, games);
    }
}
