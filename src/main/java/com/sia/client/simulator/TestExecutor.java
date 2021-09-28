package com.sia.client.simulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class TestExecutor implements Runnable{

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final AtomicBoolean startStatus = new AtomicBoolean(false);
    private final int initialDelay;
    private final int period;

    public TestExecutor(int initialDelay,int period) {
        this.initialDelay = initialDelay;
        this.period = period;
    }
    public void start() {
        if (startStatus.compareAndSet(false, true) ) {
            scheduledExecutorService.scheduleAtFixedRate(this::run, initialDelay, period, TimeUnit.SECONDS);
        }
    }
}
