package com.sia.client.ui.simulator;

import com.sia.client.model.LineGames;
import com.sia.client.model.TableSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestTableSection extends TableSection<TestGame> {

    public static final String TestGroupGameHeaderPrefix = "TEST GAME ";
    private final int columnCount;

    public static TestTableSection createTestTableSection(TestGameCache testGameCache, final int idSeed,int rowCount,int columnCount) {

        List<TestGame> gameVec = new ArrayList<>();
        for(int rowIndex=0;rowIndex<rowCount;rowIndex++) {
            TestGame tg = testGameCache.makeTestGame(idSeed*100+rowIndex);
            gameVec.add(tg);
        }
        TestTableSection testTableSection = new TestTableSection(testGameCache,true,gameVec,columnCount);
        testTableSection.setGameGroupHeader(TestGroupGameHeaderPrefix+idSeed);
        return testTableSection;
    }
    public TestTableSection(TestGameCache gameCache, boolean toAddBlankGameId, List<TestGame> gameVec,int columnCount) {
        super(gameCache, toAddBlankGameId, gameVec);
        this.columnCount = columnCount;
    }
    @Override
    protected void prepareLineGamesForTableModel(final LineGames<TestGame> gamesVec) {
        gamesVec.sort(Comparator.comparingInt(TestGame::getGame_id));
    }

    @Override
    protected List<Object> makeRowData(final TestGame game) {
        List<Object> rtn = new ArrayList<>();
        for(int i=0; i< columnCount; i++) {
            rtn.add(""+game.getGame_id()+"_"+i);
        }
        return rtn;
    }


}
