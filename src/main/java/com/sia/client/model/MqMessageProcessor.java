package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class MqMessageProcessor {

    private final MessageConsumingScheduler<Game> gameConsumingScheculer;
    private final boolean doStats;
    private long lastUpdate = System.currentTimeMillis();
    private final String name;

    public MqMessageProcessor(String name, long initialDelayInMilliSeconds, long periodInMilliSeconcs) {
        gameConsumingScheculer = new MessageConsumingScheduler<>(createConsumer());
        gameConsumingScheculer.setInitialDelay(initialDelayInMilliSeconds);
        gameConsumingScheculer.setUpdatePeriodInMilliSeconds(periodInMilliSeconcs);
        doStats = 0 < periodInMilliSeconcs;
        this.name = name;
    }
    public void addGame(Game game) {
        if ( null  != game) {
            gameConsumingScheculer.addMessage(game);
        }
    }


    private Consumer<List<Game>> createConsumer() {
        return (buffer) -> {
            Set<Game> distinctSet = new HashSet<>(buffer);
            Utils.checkAndRunInEDT(()-> AppController.fireAllTableDataChanged(distinctSet));
            if ( doStats) {
                long now = System.currentTimeMillis();
                log("MqMessageProcessor, name:"+name+", buffer size="+buffer.size()+", uniq size="+distinctSet.size()+", time since last process:"+(now-lastUpdate));
                lastUpdate = now;
            }
        };
    }
}
