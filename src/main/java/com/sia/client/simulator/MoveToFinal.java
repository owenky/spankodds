package com.sia.client.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.TableSection;
import com.sia.client.ui.AppController;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import java.util.List;

import static com.sia.client.config.Utils.log;

public class MoveToFinal extends TestExecutor{

    private final MainGameTableModel model;
    public MoveToFinal(MainGameTableModel model) {
        super(10,600000);
        this.model = model;
    }
    @Override
    public void run() {

        try {
            List<TableSection<Game>> tableSections = model.getTableSections();
            //remove final section if presented
            SwingUtilities.invokeAndWait(() -> removeFinalSection());
            Thread.sleep(6000);
            for (TableSection<Game> section : tableSections) {
                if (section.getRowCount() > 0) {
                    Game game = section.getGame(1);
                    SwingUtilities.invokeAndWait(() -> moveGameToFinal(game));
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void moveGameToFinal(Game game) {
        log("move game "+game.getGame_id()+", "+game.getHometeam()+", "+game.getVisitorteam());
        AppController.moveGameToThisHeader(game, SiaConst.FinalStr);
    }
    private void removeFinalSection() {
        List<TableSection<Game>> tableSections = model.getTableSections();
        while ( tableSections.size() > 0) {
            TableSection<Game> lastSection = tableSections.get(tableSections.size() - 1);
            String header = lastSection.getGameGroupHeader();

            if ( null != header && header.contains(SiaConst.FinalStr)) {
                tableSections.remove(tableSections.size() - 1);
            } else {
                break;
            }
        }
        model.fireTableChanged(new TableModelEvent(model,0,model.getRowCount()-1, 1,TableModelEvent.INSERT));
    }
}
