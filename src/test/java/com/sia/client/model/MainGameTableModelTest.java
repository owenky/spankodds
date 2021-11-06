package com.sia.client.model;

import com.sia.client.config.SiaConst;
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
    @Test
    public void testEmptyHalfTime1() {
        TestTableSection halfTime = new TestTableSection(SiaConst.HalfTimeStr,gameCache, true, new ArrayList<>());
        model.addGameLine(halfTime);
        assertEquals(0,model.getRowCount());

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(1000));
        games.add(gameCache.makeTestGame(1001));
        final String blankHeader = null;
        TestTableSection blankHeaderSection = new TestTableSection(blankHeader,gameCache, true, games);
        model.addGameLine(blankHeaderSection);
        assertEquals(3, model.getRowCount());
        assertEquals(blankHeader,model.getValueAt(0, 0));
    }
    @Test
    public void testEmptyHalfTime2() {
        TestTableSection halfTime = new TestTableSection(SiaConst.HalfTimeStr,gameCache, true, new ArrayList<>());
        model.addGameLine(halfTime);
        assertEquals(0,model.getRowCount());

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(1000));
        games.add(gameCache.makeTestGame(1001));
        final String testHeader = "TestGameHeader";
        TestTableSection testHeaderSection = new TestTableSection(testHeader,gameCache, true, games);
        model.addGameLine(testHeaderSection);
        assertEquals(3, model.getRowCount());
        assertEquals(testHeader,model.getValueAt(0, 0));
    }
    @Test
    public void testHalfTime() {
        List<TestGame> halfTimeames = new ArrayList<>();
        halfTimeames.add(gameCache.makeTestGame(100));
        TestTableSection halfTime = new TestTableSection(SiaConst.HalfTimeStr,gameCache, true, halfTimeames);
        model.addGameLine(halfTime);
        assertEquals(2,model.getRowCount());
        assertEquals(SiaConst.HalfTimeStr,model.getValueAt(0, 0));
        assertEquals("100_0",model.getValueAt(1, 0));

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(1000));
        games.add(gameCache.makeTestGame(1001));
        final String testHeader = "TestGameHeader";
        TestTableSection testHeaderSection = new TestTableSection(testHeader,gameCache, true, games);
        model.addGameLine(testHeaderSection);
        assertEquals(5, model.getRowCount());
        assertEquals(testHeader,model.getValueAt(2, 0));
    }
    @Test
    public void testBuildIndexMappingCache() {
        List<TestGame> halfTimeames = new ArrayList<>();
        halfTimeames.add(gameCache.makeTestGame(100));
        TestTableSection halfTime = new TestTableSection(SiaConst.HalfTimeStr,gameCache, true, halfTimeames);
        model.addGameLine(halfTime);

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(1000));
        games.add(gameCache.makeTestGame(1001));
        final String testHeader = "TestGameHeader";
        TestTableSection testHeaderSection = new TestTableSection(testHeader,gameCache, true, games);
        model.addGameLine(testHeaderSection);
        model.buildIndexMappingCache(true);
        assertEquals(5, model.getRowCount());
        assertEquals(testHeader,model.getValueAt(2, 0));
    }
    private TableSection<TestGame> makeLinesTableData(int seed) {

        List<TestGame> games = new ArrayList<>();
        games.add(gameCache.makeTestGame(seed));
        games.add(gameCache.makeTestGame(seed+1));
        games.add(gameCache.makeTestGame(seed+2));
        games.add(gameCache.makeTestGame(seed+3));

        gameCache.addAll(games);
        return new TestTableSection("",gameCache, true, games);
    }
}
