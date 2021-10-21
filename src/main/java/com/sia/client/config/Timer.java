package com.sia.client.config;

import static com.sia.client.config.Utils.log;

public class Timer {

    private long lastStep = 0;
    private long begin = 0;
    public Timer() {

    }
    public void reset() {
        begin = System.currentTimeMillis();
        lastStep = System.currentTimeMillis();
    }
    public void step(String info) {
        long now = System.currentTimeMillis();
        long diff = now -lastStep;
        log("Time for "+info+" is: "+ diff);
        lastStep = now;
    }
    public void total() {
        log("Time total:"+(System.currentTimeMillis()-begin));
    }
}
