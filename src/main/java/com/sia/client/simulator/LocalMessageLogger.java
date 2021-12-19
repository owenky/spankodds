package com.sia.client.simulator;

import com.sia.client.config.Logger;
import com.sia.client.config.Utils;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class LocalMessageLogger extends Logger {

    public static final ThreadLocal<LocalDateTime> localMessageTimeStamp = new ThreadLocal<>();
    public static final AtomicReference<LocalDateTime> localMessageClock = new AtomicReference<>();
    @Override
    public String logHeader() {
        String timeStampStr;
        LocalDateTime timeStamp = localMessageTimeStamp.get();
        if ( null == timeStamp ) {
            timeStampStr =  Utils.nowShortString();
        } else {
            timeStampStr = localMessageClock.get().format(Utils.sdf);
        }
        return timeStampStr+", Thread="+Thread.currentThread().getName()+" |";
    }
}
