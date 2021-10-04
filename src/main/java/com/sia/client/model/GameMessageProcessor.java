package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class GameMessageProcessor {

    private final MessageConsumingScheduler<Game> gameConsumingScheculer;
    public GameMessageProcessor(long initialDelayInMilliSeconds, long periodInMilliSeconcs) {
        gameConsumingScheculer = new MessageConsumingScheduler<>(createConsumer());
        gameConsumingScheculer.setInitialDelay(initialDelayInMilliSeconds);
        gameConsumingScheculer.setUpdatePeriodInMilliSeconds(periodInMilliSeconcs);
    }
    public void addGame(Game game) {
        if ( null  == game) {
            log(new Exception("null game detected....."));
        } else {
            gameConsumingScheculer.addMessage(game);
        }
    }

    //TODO debug variable lastUpdate
    private static long lastUpdate = System.currentTimeMillis();
    //END of debug TODO

    private Consumer<List<Game>> createConsumer() {
        return (buffer) -> {
            Set<Game> distinctSet = new HashSet<>(buffer);
            Utils.checkAndRunInEDT(()->{
                AppController.fireAllTableDataChanged(distinctSet);
            });

        };
    }
}
