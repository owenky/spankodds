package com.sia.client.config;

import com.sia.client.simulator.InitialGameMessages;

import java.io.PrintStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Logger {

    public static PrintStream logPs=System.out;
    public static PrintStream errPs=System.err;

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static void log(String mesg) {
        String fullMsg = logHeader()+mesg;
        executor.execute(()->logPs.println(fullMsg));
    }
    public static void log(String errMsg,Throwable e) {
        String logHeader = logHeader();
        executor.execute(()->{errPs.println(logHeader+"| errMsg:"+errMsg);e.printStackTrace(Logger.errPs);});
    }
    public static void log(Throwable e) {
        log("",e);
    }
    public static String logHeader() {
        return Utils.nowShortString()+", Thread="+Thread.currentThread().getName()+" |";
    }
    public static void debug(String mesg) {
        log(logHeader()+" DEBUG:"+mesg);
    }
    public static void consoleLogPeekGameId(String keyword, int gameid) {
        if (InitialGameMessages.PeekGameId == gameid) {
            consoleLogPeek("game id "+gameid+" received at "+keyword);
        }
    }
    public static void consoleLogPeek(String mesg) {
        if ( InitialGameMessages.Debug) {
            System.out.println(logHeader() + mesg);
        }
    }
    public static void consoleLogPeek(Exception e) {
        if ( InitialGameMessages.Debug) {
            e.printStackTrace();
        }
    }
    public static void log(Object mesg) {
        log(logHeader()+mesg);
    }
}
