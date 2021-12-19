package com.sia.client.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpankOdds;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.sia.client.config.Utils.log;
import static com.sia.client.simulator.InitialGameMessages.interestedMessageTypes;

public abstract class OngoingGameMessages {

    private static final long initDelay = 5L;
    private static final long period = 1800L;
    private static final ScheduledExecutorService bckThread = Executors.newScheduledThreadPool(1);
    private static final int batchSize = 2000;
    private static final String MessageTypeDelimiter = "####@@@@";
    public static final String TextPropertyName = "TEXT";
    private static final AtomicBoolean startStatus = new AtomicBoolean(false);
    private static List<String> buffer = new ArrayList<>();
    private static AtomicInteger batchCount = new AtomicInteger(0);
    private static long lastWriteTime = -1;
    private static MessageDispatcher toLinesMessageConsumer;
    private static MessageDispatcher toScoreMessageConsumer;
    private static MessageDispatcher toGameMessageConsumer;

    static {
        bckThread.scheduleAtFixedRate(OngoingGameMessages::flushRemainingMessages, initDelay, period, TimeUnit.SECONDS);
    }
    private static MessageDispatcher toLinesMessageConsumer() {
        if ( null == toLinesMessageConsumer) {
            toLinesMessageConsumer = new MessageDispatcher(m-> AppController.linesConsumer.processMessage((MapMessage)m));
        }
        return toLinesMessageConsumer;
    }
    private static MessageDispatcher toScoreMessageConsumer() {
        if ( null == toScoreMessageConsumer) {
            toScoreMessageConsumer =  new MessageDispatcher(m->  AppController.scoresConsumer.processMessage((MapMessage)m));
        }
        return toScoreMessageConsumer;
    }
    private static MessageDispatcher toGameMessageConsumer() {
        if ( null == toGameMessageConsumer) {
            toGameMessageConsumer =  new MessageDispatcher(m->  AppController.gamesConsumer.processMessage((TextMessage)m));
        }
        return toGameMessageConsumer;
    }
    public synchronized static void addMessage(MessageType messgeType, Message message) {
        if (InitialGameMessages.shouldLogMesg && interestedMessageTypes.contains(messgeType)) {
            String text;
            if (message instanceof ActiveMQMapMessage) {
                text = convert((ActiveMQMapMessage) message);
            } else {
                text = convert((ActiveMQTextMessage) message);
            }
            addText(messgeType, text);
        }
    }

    static String convertMap(Map<String, Object> contentMap) {

        Set<String> keySet = contentMap.keySet();
        StringBuilder sb = new StringBuilder();
        for (String name : keySet) {
            Object value = contentMap.get(name);
            if (null != value) {
                sb.append(name).append("=").append(value).append(SiaConst.PropertyDelimiter);
            }
        }
        return sb.toString();
    }
    public static String convert(ActiveMQMapMessage message) {
        try {
            Map<String, Object> contentMap = message.getContentMap();
            StringBuilder sb = new StringBuilder(convertMap(contentMap));
            sb.append(SiaConst.PropertyDelimiter).append(message.getProperties());
            return sb.toString();

        } catch (Exception e) {
            Utils.log(e);
            return "";
        }
    }

    public static String convert(ActiveMQTextMessage message) {
        try {
            Map<String, Object> prop = message.getMessage().getProperties();
            StringBuilder sb = new StringBuilder(convertMap(prop));
            sb.append(SiaConst.PropertyDelimiter).append(TextPropertyName).append("=").append(message.getText());
            return sb.toString();

        } catch (Exception e) {
            Utils.log(e);
            return "";
        }
    }

    static void addText(MessageType messgeType, String text) {
        if (InitialGameMessages.shouldLogMesg) {
            buffer.add(System.currentTimeMillis() + MessageTypeDelimiter + messgeType + MessageTypeDelimiter + text);
            if (buffer.size() >= batchSize) {
                flushMessages();
            }
        }
    }

    private synchronized static void flushMessages() {
        if (InitialGameMessages.shouldLogMesg) {
            String filePath = InitialGameMessages.MesgDir + File.separator + batchCount.getAndAdd(1) + ".txt";
            FileWriter fw = null;
            try {
                fw = new FileWriter(filePath);
                for (String line : buffer) {
                    fw.write(line);
                    fw.write("\n");
                }
                lastWriteTime = System.currentTimeMillis();
                buffer.clear();
            } catch (Exception e) {
                log(e);
            } finally {
                if (null != fw) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        log(e);
                    }
                }
            }
        }
    }

    private synchronized static void flushRemainingMessages() {
        if ((System.currentTimeMillis() - lastWriteTime) / 1000L > period && buffer.size() > 0) {
            flushMessages();
        }
    }
    public static void main(String[] argv) throws InterruptedException {
        long now = System.currentTimeMillis();
        LocalDateTime ldt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(ldt);
    }

    public static void loadMessagesFromLog() {
        AppController.waitForSpankyWindowLoaded();
        if (SpankOdds.getMessagesFromLog && startStatus.compareAndSet(false, true)) {
            File tempDir = new File(InitialGameMessages.MesgDir);
            String[] files = tempDir.list();
            if (null != files) {
                for (int i = 0; i < (files.length-1); i++) {  //skip initGameMesgs.txt
                    String filePath = InitialGameMessages.MesgDir + File.separator + i + ".txt";
//                    log("loading the "+i+".txt of out total of "+(files.length-1)+" files.");
                    try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
                        stream.forEach(OngoingGameMessages::processMessage);
                    } catch (Exception e) {
                        log(e);
                    }
                }
                log("Finished loading messages from local..... linesMesgCnt="+linesMesgCnt+", gamesMesgCnt="+gamesMesgCnt+", scoresMesgCnt="+scoresMesgCnt);
            }
        }
    }
    private static int linesMesgCnt = 0;
    private static int gamesMesgCnt = 0;
    private static int scoresMesgCnt = 0;
    private static void processMessage(String text) {

        String[] strs = text.split(MessageTypeDelimiter);
        if (3 > strs.length) {
            return;
        }
        String timeStamp = strs[0];
        LocalDateTime ltd = new Date(Long.parseLong(timeStamp)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalMessageLogger.localMessageTimeStamp.set(ltd);
        LocalMessageLogger.localMessageClock.set(ltd);
        String type = strs[1];
        String messageText = strs[2];
        boolean toContinue=false;
        for(String keyword: InitialGameMessages.filters) {
            if ( text.contains(keyword)) {
                toContinue=true;
                break;
            }
        }

        if ( ! toContinue && 0 < InitialGameMessages.filters.length) {
            return;
        }

        pause(50L);

        toContinue = interestedMessageTypes.stream().anyMatch(mt->mt.name().equals(type));
        if ( ! toContinue){
            return;
        }

        MessageDispatcher messageDispatcher;
        Message message;
        if (MessageType.Line.name().equals(type)) {
            message = new LocalMapMessage(parseText(messageText),ltd);
            messageDispatcher = toLinesMessageConsumer();
            linesMesgCnt++;
        } else if (MessageType.Score.name().equals(type)) {
            message = new LocalMapMessage(parseText(messageText),ltd);
            messageDispatcher = toScoreMessageConsumer();
            scoresMesgCnt++;
        } else if (MessageType.Game.name().equals(type)) {
            message = new LocalTextMessage(parseText(messageText),ltd);
            messageDispatcher = toGameMessageConsumer();
            gamesMesgCnt++;
        } else {
            throw new IllegalCharsetNameException("OnGoingGameMessage::processMessage -- ERROR! Unknown message type:" + type);
        }
        messageDispatcher.dispatch(message);
    }
    private static int messageCount=0;
    static void pause(long time) {
        if ( 0 == ((++messageCount)%100)) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                log(e);
            }
        }
    }
    private static Map<String, String> parseText(String text) {
        Map<String, String> map = new HashMap<>();
        String[] parts = text.split(SiaConst.PropertyDelimiter);
        for (String part : parts) {
            part = rmSpecialCharacters(part);
            String[] prop = part.split("=");
            if (2 == prop.length) {
                String name = prop[0].trim();
                String value = prop[1].trim();
                if (!value.isEmpty()) {
                    map.put(name, value);
                }
            }
        }
        return map;
    }
    private static Pattern pattern1 = Pattern.compile("^\\{.+?}$");
    private static String rmSpecialCharacters(String str) {
        str = str.trim();
        Matcher matcher = pattern1.matcher(str);
        if  ( matcher.find()) {
            return str.substring(1,str.length()-1).trim();
        } else {
            return str;
        }

    }
    /////////////////////////////////////////////////////////////
    public enum MessageType {
        Game,
        Score,
        Line;
    }
}
