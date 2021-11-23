package com.sia.client.simulator;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst.TestProperties;
import com.sia.client.simulator.OngoingGameMessages.MessageType;
import com.sia.client.ui.AppController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sia.client.config.Utils.log;

public abstract class InitialGameMessages {

    public static final int PeekGameId = 214217;
    public static boolean shouldLogMesg = false;
    public static boolean shouldRunSimulator; //set by System Property
    public static boolean getMessagesFromLog = false;
    public static boolean Debug;
    public static String [] filters;
    public static String MesgDir;
    public static Set<MessageType> interestedMessageTypes;
    private static List<String> buffer = new ArrayList<>();
    private static String InitialLoadingFilePath;

    public static void initMsgLoggingProps() {
        String debugStr = System.getProperty("Debug");
        if ( null == debugStr) {
            Debug = false;
        } else {
            Debug = Boolean.parseBoolean(debugStr);
        }
        String mesgDir = System.getProperty("MesgDir");
        MesgDir = null==mesgDir?TestProperties.DefaultMesgDir:mesgDir;
        shouldLogMesg = Boolean.parseBoolean(System.getProperty("LogMesg"));
        shouldRunSimulator = Boolean.parseBoolean(System.getProperty("RunSimulator"));
        getMessagesFromLog = Boolean.parseBoolean(System.getProperty("GetMesgFromLog"));
        String filterStr = System.getProperty("Filter");
        if ( null == filterStr) {
            filters = new String [0];
        } else {
            filters = filterStr.split(",");
        }
        String interestedMesgTypeStr = System.getProperty("InterestedMessageTypes");

        interestedMessageTypes = configInterestedMessageTypes(interestedMesgTypeStr);

        if ( getMessagesFromLog) {
            shouldLogMesg = false;
            String msgDir = System.getProperty("MessageLoadingFolder");
            if ( null != msgDir) {
                MesgDir = msgDir;
            }
        }

        InitialLoadingFilePath = MesgDir + File.separator+TestProperties.InitialLoadingFileName;

        if ( shouldLogMesg) {
            backupTempDir();
        }
    }
    static Set<MessageType> configInterestedMessageTypes(String interestedMesgTypeStr) {
        Set<MessageType> interestedMsgTypes = new HashSet<>();
        if ( null != interestedMesgTypeStr && ! interestedMesgTypeStr.isEmpty()) {
            String [] typesStr = interestedMesgTypeStr.split(",");
            for(String typeStr:typesStr) {
                try {
                    MessageType type = MessageType.valueOf(typeStr);
                    interestedMsgTypes.add(type);
                }catch(Exception e) {
                    throw new IllegalArgumentException("Interested message type defined as property in -DInterestedMessageTypes is not supported: " + typeStr);
                }
            }
        } else {
            interestedMsgTypes = Arrays.stream(MessageType.values()).collect(Collectors.toSet());
        }
        return Collections.unmodifiableSet(interestedMsgTypes);
    }
    static void backupTempDir() {
        File tempDir = new File(MesgDir);
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_HH_mm_ss");
        String timeStamp = sdf.format(new Date());
        if ( tempDir.exists()) {
            if ( ! tempDir.isDirectory()) {
                tempDir.delete();
            } else {
                File backupdir = new File(MesgDir + "_" + timeStamp);
                tempDir.renameTo(backupdir);
            }
        }
        tempDir.mkdirs();
    }
    public static void addText(String text) {
        if (shouldLogMesg) {
            buffer.add(text);
        }
    }
    public static void postDataLoading() {
        flushMessages();
        loadGamesFromLog();
    }
    private static void loadGamesFromLog() {
        if (getMessagesFromLog) {
            try (Stream<String> stream = Files.lines(Paths.get(InitialLoadingFilePath))) {
                stream.forEach(text->AppController.pushGameToCache(GameUtils.parseGameText(text)));
            } catch ( Exception e) {
                log(e);
            }
        }
    }
    private static void flushMessages() {
        if (shouldLogMesg) {
            FileWriter fw = null;
            try {
                fw = new FileWriter(InitialLoadingFilePath);
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

       log(""+new Timestamp(0).getTime());
        log(""+new Timestamp(-1).getTime());
        log(Long.parseLong(""));
    }
}
