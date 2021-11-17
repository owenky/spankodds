package com.sia.client.ui;

import com.sia.client.config.Logger;
import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.TableSection;

import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.sia.client.config.Utils.checkAndRunInEDT;

public class GameBatchUpdator {

    private final Timer flushingScheduler;
    private final String name;
    private long lastUpdateTime = 0;
    private List<TableModelEvent> updateEvents = new ArrayList<>(20);
    private Set<Integer> updatedGameIdSet = new HashSet<>();
    private int accumulateCnt = 0;
    public GameBatchUpdator(String name) {
        this.name = name;
        ActionListener al = e->this.checkToUpdate();
        flushingScheduler = new Timer(SiaConst.DataRefreshRate,al);
        flushingScheduler.start();
    }

    public void addGameToGameGroup(MainGameTableModel mainGameTableModel,GameGroupHeader gameGroupHeader, Game game, LinesTableData ltd) {

        int rowIndex = ltd.getRowIndex(game.getGame_id());
        if (0 <= rowIndex) {
            updateRow(mainGameTableModel,game.getGame_id(),ltd, rowIndex);
        } else {
            //force pending update events
            flush();
            //check if this game is in other table section, if yes, then do move, else do add -- 2021-11-07
            TableSection<Game> oldTableSection = mainGameTableModel.findTableSectionByGameid(game.getGame_id());
            if (null != oldTableSection) {
                mainGameTableModel.moveGameFromSourceToTarget(oldTableSection, game, gameGroupHeader);
            } else {
                mainGameTableModel.addGameToTableSection(ltd, game);
            }
        }

    }
    public void flush() {
        lastUpdateTime = 0;
        checkToUpdate();
    }
    private void checkToUpdate() {
        long now = System.currentTimeMillis();
        if ( SiaConst.DataRefreshRate< (now-lastUpdateTime)) {
            checkAndRunInEDT(() -> {
                for (TableModelEvent e : updateEvents) {
                    ColumnCustomizableDataModel<?> model = (ColumnCustomizableDataModel<?>) e.getSource();
                    model.fireTableChanged(e);
                }
Logger.consoleLogPeek("In GameBatchUpdator, accumulateCnt="+accumulateCnt+", actual size="+updatedGameIdSet.size()+", ago="+(now-lastUpdateTime+", model name="+name));
                updateEvents.clear();
                updatedGameIdSet.clear();
                accumulateCnt=0;
            });
            lastUpdateTime = now;
        }
    }
    private void updateRow(MainGameTableModel mainGameTableModel,int gameId,LinesTableData tableSection, int rowIndexInTableSection) {
        accumulateCnt++;
        if (!updatedGameIdSet.contains(gameId)) {
            int affectedRowModelIndex = mainGameTableModel.mapToRowModelIndex(tableSection, rowIndexInTableSection);
            TableModelEvent e = new TableModelEvent(mainGameTableModel, affectedRowModelIndex, affectedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
            updateEvents.add(e);
            updatedGameIdSet.add(gameId);
        }
    }
}
