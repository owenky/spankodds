package com.sia.client.ui.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.LineGames;
import com.sia.client.model.TableSection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestTableSection extends TableSection<TestGame> {

    public static final String TestGroupGameHeaderPrefix = "TEST GAME ";
    private static int instanceCount = 0;

    public static TestTableSection createTestTableSection(TestGameCache testGameCache, final int idSeed, int rowCount, int tableIndex) {

        List<TestGame> gameVec = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            TestGame tg = testGameCache.makeTestGame(1000 * tableIndex + idSeed * 100 + rowIndex);
            gameVec.add(tg);
        }
        TestTableSection testTableSection = new TestTableSection(TestGroupGameHeaderPrefix + idSeed, testGameCache, true, gameVec);
        int sectionRowHeight = 0 == (instanceCount++) % 2 ? SiaConst.NormalRowheight : SiaConst.SoccerRowheight;
        testTableSection.setRowHeight(sectionRowHeight);
        return testTableSection;
    }

    public TestTableSection(String groupHeader, TestGameCache gameCache, boolean toAddBlankGameId, List<TestGame> gameVec) {
        super(GameGroupHeader.create(groupHeader,null,0,0), gameCache, toAddBlankGameId, gameVec);
    }

    @Override
    protected void prepareLineGamesForTableModel(final LineGames<TestGame> gamesVec) {
        gamesVec.sort(Comparator.comparingInt(TestGame::getGame_id));
    }

    @Override
    protected List<Object> makeRowData(final TestGame game) {
        int gameid = game.getGame_id();
        if (SiaConst.BlankGameId == gameid) {
            List<Object> rtn = new ArrayList<>(game.colCount);
            for (int i=0;i<game.colCount;i++) {
                rtn.add(this.getGameGroupHeader());
            }
            return rtn;
        } else {
            return game.getRowData();
        }
    }
}
