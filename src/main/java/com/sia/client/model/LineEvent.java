package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.Utils;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.util.function.Consumer;

public class LineEvent {

    private final LineIdentity lineIdentity;
    private int rowModelIndex = -1;
    private int columnModelIndex = -1;
    private int lastRowModelIndex = -1;
    private TableSection<Game> tableSection;
    private boolean initStatus = false;

    public LineEvent(LineIdentity lineIdentity) {
       this.lineIdentity = lineIdentity;
    }
    public int getGameid() {
        return lineIdentity.getGameId();
    }
    public int getBookieid() {
        return lineIdentity.getBookieId();
    }
    public int getPeriod() {
        return lineIdentity.getPeriod();
    }
    public void updateTable() {
        OngoingGameMessages.lineUpdateStat.beginStep(lineIdentity.getGameId()+"_"+lineIdentity.getBookieId());
        Sport sport = GameUtils.getSport(AppController.getGame(lineIdentity.getGameId()));
        if ( null != sport) {
            doTableUpdate(sport);
        }  else {
//            Utils.log("Warning: can't find sport for game id:"+gameId);
        }
        OngoingGameMessages.lineUpdateStat.endStep();
    }
    private void doTableUpdate(Sport sport) {
        final Consumer<SportsTabPane> updateMethod = (stp) -> {
            SportType selectedSportType = stp.getSelectedSportType();
            if (null != selectedSportType && selectedSportType.getSportId() == sport.getSport_id()) {
                MainScreen ms = (MainScreen) stp.getSelectedComponent();
                if ( null != ms ) { //when switching tab, ms is null -- 07/16/2022
                    MainGameTableModel gameTableModel = ms.getColumnCustomizableTable().getModel();
                    if (!initStatus) {
                        initStatus = true;
                        initTableModelEventProp(gameTableModel);
                    }
                    if (null != tableSection) {
                        if (!gameTableModel.isFiringTableChangeEvent()) {
                            TableModelEvent tme = new TableModelEvent(gameTableModel, rowModelIndex, lastRowModelIndex, columnModelIndex, TableModelEvent.UPDATE);
                            gameTableModel.fireTableChanged(tme);
                        }
                    } else {
//                    Utils.log("game id not found in the sport " + selectedSportType.getSportName() + " table. Skip line event updating");
                    }
                }
            }

        };
        SwingUtilities.invokeLater(() -> SpankyWindow.applyToAllWindows(updateMethod));
    }
    private void initTableModelEventProp(MainGameTableModel gameTableModel) {
        tableSection = gameTableModel.findTableSectionByGameid(lineIdentity.getGameId());
        if (null != tableSection) {
            if ( tableSection.isGameHidden(lineIdentity.getGameId())) {
                Game game = AppController.getGame(lineIdentity.getGameId());
                Utils.log("MainGameTableModel::processNewLines, rebuild modle");
                tableSection.activateGame(game);
                gameTableModel.buildIndexMappingCache(true);
                rowModelIndex = 0;
                lastRowModelIndex = Integer.MAX_VALUE;
                columnModelIndex = TableModelEvent.ALL_COLUMNS;
            } else {
                rowModelIndex = gameTableModel.getRowModelIndexByGameId(tableSection,lineIdentity.getGameId());
                lastRowModelIndex = rowModelIndex;
                columnModelIndex = gameTableModel.getColumnModelIndexByBookieId(lineIdentity.getBookieId());
            }
        }
    }
}
