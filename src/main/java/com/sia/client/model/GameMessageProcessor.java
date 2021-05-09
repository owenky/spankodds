package com.sia.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameMessageProcessor {


    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    private final LinkedBlockingQueue<Runnable> mesgRunnerQueue = new LinkedBlockingQueue<>(10000);

    public GameMessageProcessor(int initialDelayInSeconds, int periodInSeconcs) {
        executorService.scheduleAtFixedRate(this::run,initialDelayInSeconds, periodInSeconcs, TimeUnit.SECONDS);
    }
    public void addRunnable(Runnable r) {
        mesgRunnerQueue.add(r);
    }
    private void run() {
        Set<Runnable> batch = new HashSet<> ();
        mesgRunnerQueue.drainTo(batch);
        for(Runnable r: batch) {
            r.run();
        }
    }
}
