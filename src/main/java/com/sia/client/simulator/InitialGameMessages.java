package com.sia.client.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class InitialGameMessages {

    private static List<String> buffer = new ArrayList<>();
    private static final String filePath = "c:\\temp\\spankodds_initGameMesgs.txt";

    public static void addText(String text) {
        if (SiaConst.TestProperties.shouldLogInitGameMesg.get()) {
            buffer.add(text);
        }
    }
    public static void postDataLoading() {
        flushMessages();
        loadGamesFromLog();
    }
    private static void loadGamesFromLog() {
        if (SiaConst.TestProperties.getGamesFromLog.get()) {
//            read games from file
        }
    }
    private static void flushMessages() {
        if (SiaConst.TestProperties.shouldLogInitGameMesg.get()) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(filePath);
                for (String line : buffer) {
                    fw.write(line);
                    fw.write("\n");
                }

            }catch(Exception e) {
                Utils.log(e);
            } finally {
                if ( null != fw) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        Utils.log(e);
                    }
                }
            }
        }
    }
    public static void main(String [] argv) {

        SiaConst.TestProperties.shouldLogInitGameMesg.set(Boolean.parseBoolean(System.getProperty("LogInitGameMesg")));
        addText("this is line10");
        addText("this is line20");
        flushMessages();
    }
}
