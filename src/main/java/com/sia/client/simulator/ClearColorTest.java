package com.sia.client.simulator;

import com.sia.client.config.Utils;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Spreadline;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpreadTotalView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClearColorTest implements Runnable {
    private static final Integer [] bookieIds = {604,99,817,442,107,14};
    private static final Integer gameId = 901;
    @Override
    public void run() {
        Utils.log("***** new money line *****"+" timeStat STAT="+SpreadTotalView.timeStat.flushStat());
        Utils.log("***** new money line *****"+" timeStat2 STAT="+SpreadTotalView.timeStat2.flushStat());
        Utils.log("***** new money line *****"+" timeStat3 STAT="+SpreadTotalView.timeStat3.flushStat());
        Utils.log("***** new money line *****"+" timeStat4 STAT="+SpreadTotalView.timeStat4.flushStat());
        Utils.log("***** new money line *****"+" timeStat5 STAT="+SpreadTotalView.timeStat5.flushStat());
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
        Utils.log("***** AFTER new money line *****"+"STAT="+SpreadTotalView.timeStat.flushStat());
        Utils.log("***** AFTER new money line *****"+" timeStat2 STAT="+SpreadTotalView.timeStat2.flushStat());
        Utils.log("***** AFTER new money line *****"+" timeStat3 STAT="+SpreadTotalView.timeStat3.flushStat());
        Utils.log("***** AFTER new money line *****"+" timeStat4 STAT="+SpreadTotalView.timeStat4.flushStat());
        Utils.log("***** AFTER new money line *****"+" timeStat5 STAT="+SpreadTotalView.timeStat5.flushStat());
    }
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, 2000L,3000L, TimeUnit.MILLISECONDS);
    }
}
