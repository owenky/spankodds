package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameStatus;
import com.sia.client.model.MessageConsumingScheduler;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.OngoingGameMessages.MessageType;
import com.sia.client.simulator.ScoreChangeProcessorTest;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class ScoresConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    // private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";

    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    //TODO: need to fine tune GameMessageProcessor constructor parameters.
    private final MessageConsumingScheduler<Game> scoreMessageProcessor;
    private static boolean toSimulateMQ = false;
    private final AtomicBoolean simulateStatus = new AtomicBoolean(false);

    public ScoresConsumer(ActiveMQConnectionFactory factory, Connection connection, String scoresconsumerqueue) throws JMSException {

        this.factory = factory;
        this.connection = connection;

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(scoresconsumerqueue + "?consumer.retroactive=true");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        scoreMessageProcessor = createScoreMessageProcessor();
        connection.start();
    }

    public static void main(String[] args) throws JMSException {
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
        AppController.createLoggedInConnection("reguser", "0hbaby*(");
        //AppController.createLoggedInConnection("guest","spank0dds4ever");
        LinesConsumer consumer = new LinesConsumer(AppController.getConnectionFactory(), AppController.getLoggedInConnection(), "spankoddsin.LINECHANGE");

    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void onMessage(Message message) {
//        synchronized (SiaConst.GameLock) {
            if (!InitialGameMessages.getMessagesFromLog) {
                if (toSimulateMQ) {
                    if (simulateStatus.compareAndSet(false, true)) {
                        new ScoreChangeProcessorTest(this).start();
                    }
                } else {
                    Utils.ensureNotEdtThread();
                    OngoingGameMessages.addMessage(MessageType.Score, message);
                    processMessage((MapMessage)message);
                }
            }
//        }
    }
    public void processMessage(MapMessage mapMessage) {
        try {
            String changetype = mapMessage.getStringProperty("messageType");
            if (changetype.equals("ScoreChange")) {
                int gameid = mapMessage.getInt("eventnumber");


                String period = "";
                String timer = "";
                String status = "";
                long gamestatuslong = 100;
                int currentvisitorscore = 0;
                String visitorscoresupplemental = "";
                long scorets = 100;
                int currenthomescore = 0;
                String homescoresupplemental = "";


                try {
                    period = mapMessage.getString("period");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    timer = mapMessage.getString("timer");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    status = mapMessage.getString("status");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    gamestatuslong = mapMessage.getLong("gamestatusts");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    currentvisitorscore = mapMessage.getInt("currentvisitorscore");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    visitorscoresupplemental = mapMessage.getString("visitorscoresupplemental");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    scorets = mapMessage.getLong("scorets");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    currenthomescore = mapMessage.getInt("currenthomescore");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    homescoresupplemental = mapMessage.getString("homescoresupplemental");
                } catch (Exception ex) {
                    log(ex);
                }

                Game g = AppController.getGame(gameid);
                if (g != null) {
                    GameStatus newGameStatus = GameStatus.find(status);
                    if ( null != newGameStatus ) {
                        ScoreChangedProcessor.process(newGameStatus,g,currentvisitorscore,currenthomescore);
                    } else {
                        Utils.log("ScoreConsumer: skip status "+status+", league id="+g.getLeague_id());
//                        Utils.log("ScoreConsumer: SHOULD Disable DEBUG false status for debugging, set to in progress skip status "+status+", league id="+g.getLeague_id());
//                        //TOD O should disable following after debug....
//                        new ScoreChangedProcessor().process(GameStatus.InProgress,g,GameStatus.InProgress.getGroupHeader(),currentvisitorscore,currenthomescore);
                    }

                    g.updateScore(period, timer, status, gamestatuslong, currentvisitorscore, visitorscoresupplemental,
                            scorets, currenthomescore, homescoresupplemental);

                } else {
                    g = new Game();
                    g.updateScore(period, timer, status, gamestatuslong, currentvisitorscore, visitorscoresupplemental,
                            scorets, currenthomescore, homescoresupplemental);
//                    AppController.addGame(g);
//                    scoreMessageProcessor.addMessage(g);
                    //should not add if game id not found from cache, because new Game() does not give game critical game info like date, and league -- 2021-10-30
                    log("Warning: null game found for game id:"+gameid);
                }
            }
        } catch (Exception e) {
            log("exception scores consumer " + e);
            log(mapMessage.toString());
            log(e);

        }
    }

    public void playSound(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            log("Error with playing sound.");
            log(ex);
        }
    }
    private MessageConsumingScheduler<Game> createScoreMessageProcessor() {
        Consumer<List<Game>> messageConsumer = (buffer)-> {
            Set<Game> distinctSet = new HashSet<>(buffer);
            Utils.checkAndRunInEDT(()->{
                AppController.fireAllTableDataChanged(distinctSet);
            });

        };
        MessageConsumingScheduler<Game> scoreMessageProcessor = new MessageConsumingScheduler<>(messageConsumer);
        scoreMessageProcessor.setInitialDelay(2*1000L);
//        scoreMessageProcessor.setUpdatePeriodInMilliSeconds(1500L);
        scoreMessageProcessor.setUpdatePeriodInMilliSeconds(-1500L);
        return scoreMessageProcessor;
    }
}
