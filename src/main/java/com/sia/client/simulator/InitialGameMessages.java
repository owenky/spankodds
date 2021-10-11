package com.sia.client.simulator;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.TestProperties;
import com.sia.client.ui.AppController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.sia.client.config.Utils.log;

public abstract class InitialGameMessages {

    private static List<String> buffer = new ArrayList<>();
    private static final String filePath = TestProperties.MesgDir+ File.separator+"initGameMesgs.txt";

    public static void initMsgLoggingProps() {
        TestProperties.shouldLogMesg.set(Boolean.parseBoolean(System.getProperty("LogMesg")));
        TestProperties.shouldRunMainScreenTest.set(Boolean.parseBoolean(System.getProperty("MainScreenTest")));
        TestProperties.getMessagesFromLog.set(Boolean.parseBoolean(System.getProperty("GetMesgFromLog")));

        if ( TestProperties.getMessagesFromLog.get()) {
            TestProperties.shouldLogMesg.set(false);
        }

        if ( TestProperties.shouldLogMesg.get()) {
            backupTempDir();
        }
    }
    static void backupTempDir() {
        File tempDir = new File(TestProperties.MesgDir);
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_HH_mm_ss");
        String timeStamp = sdf.format(new Date());
        if ( tempDir.exists()) {
            if ( ! tempDir.isDirectory()) {
                tempDir.delete();
            } else {
                File backupdir = new File(TestProperties.MesgDir + "_" + timeStamp);
                tempDir.renameTo(backupdir);
            }
        }
        tempDir.mkdirs();
    }
    public static void addText(String text) {
        if (SiaConst.TestProperties.shouldLogMesg.get()) {
            buffer.add(text);
        }
    }
    public static void postDataLoading() {
        flushMessages();
        loadGamesFromLog();
    }
    private static void loadGamesFromLog() {
        if (SiaConst.TestProperties.getMessagesFromLog.get()) {
            try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                stream.forEach(text->AppController.addGame(GameUtils.parseGameText(text)));
            } catch ( Exception e) {
                log(e);
            }
        }
    }
    private static void flushMessages() {
        if (SiaConst.TestProperties.shouldLogMesg.get()) {
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
