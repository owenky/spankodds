package com.sia.client.simulator;

import com.sia.client.config.Utils;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Spreadline;
import com.sia.client.ui.AppController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClearColorTest implements Runnable {
    private static final Integer [] bookieIds = {604,99,817,442,107,14};
    private static final Integer gameId = 901;
    @Override
    public void run() {
        Utils.log("***** new money line *****");
        for(Integer bid: bookieIds) {
            for(int p=0;p<=8;p++){
                Spreadline line;
//                    line = AppController.getSpreadline(bid, gameId, p);
//                    if ( null != line) {
//                        line.setCurrentts(System.currentTimeMillis());
//                    }
                Moneyline ml = AppController.getMoneyline(bid, gameId, p);
                if ( null != ml) {
                    ml.setCurrentts(System.currentTimeMillis());
                    AppController.addMoneyline(ml);
                }
            }
        }
    }
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, 2000L,3000L, TimeUnit.MILLISECONDS);
    }
}
