package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
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

    public GameClockUpdater() {
        timer = new Timer(delay,createActionListener());
    }
    public void start() {
        timer.start();
    }
    private ActionListener createActionListener() {
        return (event)-> {
            Consumer<SportsTabPane> updater = (stp) -> {
                updateHalfTimeSection(stp);
            };
            SpankyWindow.applyToAllWindows(updater);
        };
    }
    private void updateHalfTimeSection(SportsTabPane stp) {
        Component c = stp.getSelectedComponent();
        if ( c instanceof MainScreen) {
            MainGameTableModel model = ((MainScreen)c).getDataModels();
System.out.print(Utils.logHeader()+"Timer: rows updated: ");
            List<TableSection<Game>> tsList = model.getTableSections();
            if ( 0 < tsList.size() ) {
                TableSection<Game> halfTimeSection = tsList.get(0);
                if ( 0 < halfTimeSection.size()-1) {
                    int firstRow = model.mapToRowModelIndex(halfTimeSection,1); //index 0 is group header row
                    int lastRow = model.mapToRowModelIndex(halfTimeSection,halfTimeSection.size()-1);
System.out.println(firstRow+"->"+lastRow);
                    TableModelEvent me = new TableModelEvent(model,firstRow,lastRow);
                    model.fireTableChanged(me);
                }
            }
        }
    }
}
