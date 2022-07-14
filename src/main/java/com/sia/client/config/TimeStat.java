package com.sia.client.config;

import java.util.HashSet;
import java.util.Set;

public class TimeStat {

    private int stepCount;
    private long accumulateTime = 0;
    private long beginTime;
    private Set<Object> idens = new HashSet<>();
    public TimeStat() {

    }
    public void reset() {
        stepCount = 0;
        accumulateTime = 0L;
        idens.clear();
    }
    public void beginStep() {
        beginStep(null);
    }
    public void beginStep(Object iden) {
        beginTime = System.currentTimeMillis();
        if ( null != iden) {
            idens.add(iden);
        }
    }
    public void endStep() {
        accumulateTime += (System.currentTimeMillis()-beginTime);
        stepCount++;
    }
    public String getStat() {
        String stat = "Total time="+accumulateTime+", stepCount="+stepCount+", idens="+idens.size();
        reset();
        return stat;
    }
}
