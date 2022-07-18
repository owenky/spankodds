package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.*;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
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
                MainScreen mainScreen = (MainScreen) c;
                MainGameTableModel model = mainScreen.getDataModels();
                if ( null != model) {
                    List<TableSection<Game>> tsList = model.getTableSections();
                    if (0 < tsList.size()) {
                        //update halftime
                        updateStageSectionClocks(model, GameStatus.HalfTime.getGroupHeader());
                    }
                }
            }
        }
    }
    private void updateStageSectionClocks(MainGameTableModel model,GameGroupHeader gameGroupHeader) {
        TableSection<Game> ts = model.findTableSectionByHeaderValue(gameGroupHeader);
        if ( null != ts && 0 < ts.size()-1) {
            int firstRow = model.mapToRowModelIndex(ts,1); //index 0 is group header row
            int lastRow = model.mapToRowModelIndex(ts,ts.size()-1);
            model.updateColumn(firstRow,lastRow,SiaConst.BookieId.DetailColumnBookieId);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final GameClockUpdater instance = new GameClockUpdater();
    }
}
