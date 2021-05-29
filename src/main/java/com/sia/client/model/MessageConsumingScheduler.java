package com.sia.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class MessageConsumingScheduler<V> {

    private static final long DefaultUpdatePeriodInMilliSeconds = 1500L;
    private final ArrayBlockingQueue<V> messageQueue = new  ArrayBlockingQueue<>(10000);
    private final ScheduledExecutorService excutorService = Executors.newSingleThreadScheduledExecutor();
    private long updatePeriodInMilliSeconds;
    private long initialDelay;
    private final Consumer<List<V>> messageConsumer;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    public MessageConsumingScheduler(Consumer<List<V>> messageConsumer) {
        updatePeriodInMilliSeconds = DefaultUpdatePeriodInMilliSeconds;
        this.messageConsumer = messageConsumer;
        initialDelay = 0;
    }
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }
    public void setUpdatePeriodInMilliSeconds(long updatePeriodInMilliSeconds){
        this.updatePeriodInMilliSeconds = updatePeriodInMilliSeconds;
    }
    public void addMessage(V message) {
        if ( 0 < updatePeriodInMilliSeconds) {
            if (isStarted.compareAndSet(false, true)) {
                excutorService.scheduleAtFixedRate(this::executeGameBatch, initialDelay, updatePeriodInMilliSeconds, TimeUnit.MILLISECONDS);
            }
            try {
                messageQueue.put(message);
            } catch (InterruptedException e) {
                log(e);
            }
        } else {
            List<V> buffer = new ArrayList<>();
            buffer.add(message);
            messageConsumer.accept(buffer);
        }
    }
    private void executeGameBatch() {
        List<V> buffer = new ArrayList<>();
        messageQueue.drainTo(buffer);
        if ( buffer.size() > 0) {
            messageConsumer.accept(buffer);
        }
    }
}
