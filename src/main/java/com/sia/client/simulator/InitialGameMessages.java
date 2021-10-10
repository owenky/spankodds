package com.sia.client.simulator;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.sia.client.config.Utils.log;

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
            try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                stream.forEach(text->AppController.addGame(GameUtils.parseGameText(text)));
            } catch ( Exception e) {
                log(e);
            }
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
                log(e);
            } finally {
                if ( null != fw) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        log(e);
                    }
                }
            }
        }
    }
    public static void main(String [] argv) {

       System.out.println(""+new Timestamp(0).getTime());
        System.out.println(""+new Timestamp(-1).getTime());
        System.out.println(Long.parseLong(""));
    }
}
