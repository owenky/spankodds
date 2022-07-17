package com.sia.client.config;

import com.sia.client.simulator.InitialGameMessages;

import java.io.PrintStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Logger {

    private PrintStream logPs=System.out;
    private PrintStream errPs=System.err;

    private final Executor executor = Executors.newSingleThreadExecutor();

    public void setErrPs(PrintStream errPs) {
        this.errPs = errPs;
    }
    public void setlogPs(PrintStream logPs) {
        this.logPs = logPs;
    }
    public void log(String mesg) {
        String fullMsg = logHeader()+mesg;
        Runnable r = ()-> {
            logPs.println(fullMsg);
        };
        executor.execute(r);
    }
    public void log(String errMsg,Throwable e) {
        String logHeader = logHeader();
        final String err;
        if ( null==errMsg || "".equals(errMsg)) {
            err = "Following exception thrown";
        } else {
            err = errMsg;
        }
        executor.execute(()->{errPs.println(logHeader+"| errMsg:"+err);e.printStackTrace(errPs);});
    }
    public void log(Throwable e) {
        log("",e);
    }
    public String logHeader() {
        return Utils.nowShortString()+", Thread="+Thread.currentThread().getName()+" |";
    }
    public void debug(String mesg) {
        log(logHeader()+" DEBUG:"+mesg);
    }
    public void consoleLogPeekGameId(String keyword, int gameid) {
        if (InitialGameMessages.PeekGameId == gameid) {
            consoleLogPeek("game id "+gameid+" received at "+keyword);
        }
    }
    public void consoleLogPeek(String mesg) {
        if ( InitialGameMessages.Debug) {
            System.out.println(logHeader() + mesg);
        }
    }
    public void consoleLogPeek(Exception e) {
        if ( InitialGameMessages.Debug) {
            e.printStackTrace();
        }
    }
    public void log(Object mesg) {
        log(String.valueOf(mesg));
    }
}
