package com.sia.client.model;

import com.sia.client.config.Logger;
import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MqMessageProcessor implements TableModelListener {


    private final MessageConsumingScheduler<Game> gameConsumingScheculer;
    private final boolean doStats;
    private long lastUpdate = System.currentTimeMillis();
    private final long initialDelayInMilliSeconds = 500L;
    private final long periodInMilliSeconcs = 200L;
    private final long uiUpdateInterval = 500L;
    private volatile long lastTableUpdateTime = 0L;

    public static MqMessageProcessor getInstance() {
        return LazyInitHolder.instance;
    }
    private MqMessageProcessor() {
        gameConsumingScheculer = new MessageConsumingScheduler<>(createConsumer());
        gameConsumingScheculer.setInitialDelay(initialDelayInMilliSeconds);
        gameConsumingScheculer.setUpdatePeriodInMilliSeconds(periodInMilliSeconcs);
        doStats = 0 < periodInMilliSeconcs;
    }
    public void addGame(Game game) {
        if ( null  != game) {
            AppController. pushGameToCache(game);
            gameConsumingScheculer.addMessage(game);
        }
    }


    private Consumer<List<Game>> createConsumer() {
        return (buffer) -> {
            if ( uiUpdateInterval < (System.currentTimeMillis()-lastTableUpdateTime)) {
                Set<Game> distinctSet = new HashSet<>(buffer);
                Utils.checkAndRunInEDT(() -> AppController.fireAllTableDataChanged(distinctSet));
                if (doStats) {
                    long now = System.currentTimeMillis();
                    Logger.consoleLogPeek("MqMessageProcessor, " + ", buffer size=" + buffer.size() + ", uniq size=" + distinctSet.size() + ", time since last process:" + (now - lastUpdate));
                    lastUpdate = now;
                }
            }
        };
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        long now = System.currentTimeMillis();
//        long diff = now - lastTableUpdateTime;
//        if ( diff < 50) {
//            Logger.consoleLogPeek(new Exception("MqMessageProcessor: Abnormal table update -- ago is too short. Last table update ago=" + diff));
//        }
        lastTableUpdateTime = now;

    }
 ///////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final MqMessageProcessor instance = new MqMessageProcessor();
    }
}
