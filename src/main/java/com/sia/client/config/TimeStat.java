package com.sia.client.config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStat {

    private final AtomicInteger stepCount = new AtomicInteger(0);
    private long accumulateTime = 0;
    private long beginTime;
    private Set<Object> idens = new HashSet<>();

    public TimeStat() {

    }
    public void reset() {
        stepCount.set(0);
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
        stepCount.addAndGet(1);
    }
    public String flushStat() {
        String stat = getStat();
        reset();
        return stat;
    }
    private String getStat() {
        long ave;
        if ( 0==stepCount.get()) {
            ave = 0l;
        } else {
            ave = accumulateTime/stepCount.get();
        }
        return "Total time="+accumulateTime+", stepCount="+stepCount.get()+", average="+ave+", idens="+idens.size();
    }
    @Override
    public String toString() {
       return getStat();
    }
}
