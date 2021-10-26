package com.sia.client.ui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.sia.client.config.Utils.log;

public class ChartCheckerTest {

    private static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    public static void main(String [] argv) throws InterruptedException {

        Runnable schduledAction = () -> {
            int count =0;
            while ( count++ < 10000) {
              log("count="+count);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        scheduledThreadPoolExecutor.schedule(schduledAction, 1, TimeUnit.SECONDS);

        Thread.sleep(5000L);
        scheduledThreadPoolExecutor.shutdownNow();
        scheduledThreadPoolExecutor.schedule(schduledAction, 1, TimeUnit.SECONDS);
    }
}
