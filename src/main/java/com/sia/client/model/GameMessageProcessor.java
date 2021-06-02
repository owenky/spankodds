package com.sia.client.model;

import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class GameMessageProcessor {

    private final MessageConsumingScheduler<Integer> gameIdConsumingScheculer;
    private final String name;
    private final boolean toDebug;

    public GameMessageProcessor(String name,long initialDelayInMilliSeconds, long periodInMilliSeconcs) {
        this.name = name;
        gameIdConsumingScheculer = new MessageConsumingScheduler<>(createConsumer());
        gameIdConsumingScheculer.setInitialDelay(initialDelayInMilliSeconds);
        gameIdConsumingScheculer.setUpdatePeriodInMilliSeconds(periodInMilliSeconcs);
        this.toDebug = (0<periodInMilliSeconcs);
    }
    public void addGameId(int gameId) {
        gameIdConsumingScheculer.addMessage(gameId);
    }

    //TODO debug variable lastUpdate
    private static long lastUpdate = System.currentTimeMillis();
    //END of debug TODO

    private Consumer<List<Integer>> createConsumer() {
        return (buffer) -> {
            Set<Integer> distinctSet = new HashSet<>(buffer);
//TODO debug statements
if ( toDebug) {
    long now = System.currentTimeMillis();
    long diff = now - lastUpdate;
    lastUpdate = now;
    log("queue size="+buffer.size()+", distinct size="+distinctSet.size()+", since last update: "+diff+" milliseconds. scheduler name="+name);
}
//END of debug TODO

            //            check but don't file , instead, fire in batch'
            AppController.fireAllTableDataChanged(distinctSet);
        };
    }
}
