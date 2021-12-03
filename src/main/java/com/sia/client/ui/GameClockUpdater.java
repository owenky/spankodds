package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameStatus;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.TableSection;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

public class GameClockUpdater {

    private final Timer timer;
    private final int delay = 1000;

    public static GameClockUpdater instance() {
        return LazyInitHolder.instance;
    }
    private GameClockUpdater() {
        timer = new Timer(delay,createActionListener());
    }
    public void start() {
        timer.start();
    }
    private ActionListener createActionListener() {
        return (event)-> {
            Consumer<SportsTabPane> updater = this::updateStageSectionClocks;
            SpankyWindow.applyToAllWindows(updater);
        };
    }
    private void updateStageSectionClocks(SportsTabPane stp) {
        if ( AppController.isReadyForMessageProcessing()) {
            Component c = stp.getSelectedComponent();
            if (c instanceof MainScreen) {
                MainGameTableModel model = ((MainScreen) c).getDataModels();
                List<TableSection<Game>> tsList = model.getTableSections();
                if (0 < tsList.size()) {
                    //update halftime
                    updateStageSectionClocks(model, GameStatus.HalfTime.getGroupHeader());
                    //upate in progress
                    //but seem In Progress Detl column does not change second by second -- 2021-11-09
//                updateStageSectionClocks(model,GameStatus.InProgress.getGroupHeader());
                }
            }
        }
    }
    private void updateStageSectionClocks(MainGameTableModel model,GameGroupHeader gameGroupHeader) {
        TableSection<Game> ts = model.findTableSectionByHeaderValue(gameGroupHeader);
        if ( null != ts && 0 < ts.size()-1) {
            int firstRow = model.mapToRowModelIndex(ts,1); //index 0 is group header row
            int lastRow = model.mapToRowModelIndex(ts,ts.size()-1);
            TableModelEvent me = new TableModelEvent(model,firstRow,lastRow);
            model.fireTableChanged(me);  //don't use processTableModelEvent which delays firing.
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final GameClockUpdater instance = new GameClockUpdater();
    }
}
