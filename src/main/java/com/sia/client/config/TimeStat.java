package com.sia.client.config;

public class TimeStat {

    private int stepCount;
    private long accumulateTime = 0;
    private long beginTime;
    public TimeStat() {

    }
    public void reset() {
        stepCount = 0;
        accumulateTime = 0L;
    }
    public void beginStep() {
        beginTime = System.currentTimeMillis();
    }
    public void endStep() {
        accumulateTime += (System.currentTimeMillis()-beginTime);
        stepCount++;
    }
    public String getStat() {
        String stat = "Total time="+accumulateTime+", stepCount="+stepCount;
        reset();
        return stat;
    }
}
