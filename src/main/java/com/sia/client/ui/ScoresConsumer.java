package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
import com.sia.client.model.MessageConsumingScheduler;
import com.sia.client.model.Sport;
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
import java.util.function.Consumer;

import static com.sia.client.config.Utils.log;

public class ScoresConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    // private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";

    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
    //TODO: need to fine tune GameMessageProcessor constructor parameters.
    private final MessageConsumingScheduler<Integer> scoreMessageProcessor;

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
    public void onMessage(Message message) {
        try {

            mapMessage = (MapMessage) message;

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

                //scorets =scorets +1000*60*60*3;
                //gamestatuslong =gamestatuslong +1000*60*60*3;

                Game g = AppController.getGame(gameid);
                boolean refreshtabs = false;
                if (g != null) {

                    //owen 8/11 moved final as first block since grand salami was causing started and final to both execute
                    if ("Final".equalsIgnoreCase(status) || "Win".equalsIgnoreCase(status)) {
                        if (!g.getStatus().equals(status)) // just became final
                        {

                            Sport s = AppController.getSport(g.getLeague_id());

                            int id = s.getParentleague_id();
                            if (id == SiaConst.SoccerLeagueId) {
                                //looks like "Soccer FINAL" is wrong, should be "FINAL" for soccer -- 06/05/2021
//                                AppController.moveGameToThisHeader(g, "Soccer FINAL");
                                AppController.moveGameToThisHeader(g, "FINAL");
                            } else {
                                AppController.moveGameToThisHeader(g, "FINAL");
                            }
                            refreshtabs = true;
                            String finalprefs = AppController.getUser().getFinalAlert();
                            log("game " + gameid + "..just went final");
                            String finalarr[] = finalprefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
                            int popupsecs = 15;
                            int location = 6;
                            String audiofile = "";
                            String[] sports;
                            boolean goodsport = false;

                            try {
                                popup = Boolean.parseBoolean(finalarr[0]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sound = Boolean.parseBoolean(finalarr[1]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                popupsecs = Integer.parseInt(finalarr[2]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                location = Integer.parseInt(finalarr[3]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                audiofile = finalarr[4];
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sports = finalarr[5].split(",");
                                for (int j = 0; j < sports.length; j++) {
                                    String sportid = sports[j];
                                    if (sportid.equals("" + s.getLeague_id()) || sportid.equals(s.getSportname())
                                            || sportid.equalsIgnoreCase("All Sports")) {
                                        goodsport = true;
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                log(ex);
                            }

                            if (goodsport) {
                                if (popup) {
                                    String hrmin = AppController.getCurrentHoursMinutes();
                                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-" + g.getShorthometeam() + "-" + currenthomescore;

//                                    String popalertname = "Alert at:" + hrmin + "\nFINAL :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
//                                    AppController.alertsVector.addElement(popalertname);
                                    String mesg = "FINAL :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                                    AppController.addAlert(hrmin,mesg);

                                    new UrgentMessage("<HTML><H2>FINAL " + s.getLeaguename() + "</H2>" +
                                            "<TABLE cellspacing=1 cellpadding=1>" +

                                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD><TD>" + currentvisitorscore + "</TR>" +
                                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD><TD>" + currenthomescore + "</TR>" +
                                            "</TABLE></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());
                                }

                                if (sound) {
                                    if (audiofile.equals("")) {
                                        new SoundPlayer("final.wav");
                                    } else {
                                        //playSound(audiofile);
                                        new SoundPlayer(audiofile);
                                    }
                                }


                            }
                            refreshtabs = true;


                        }
                    } else if (g.getStatus() == null || g.getStatus().equalsIgnoreCase("NULL") || g.getStatus().equals("")) {
                        // game hasn't started
                        if (!g.getStatus().equals(status)) {
                            //status change game just started
                            Sport s = AppController.getSport(g.getLeague_id());
                            log("game " + gameid + "..just started");
                            int id = s.getParentleague_id();
                            if (id == SiaConst.SoccerLeagueId) {
                                //for soccer, In Progress header is NOT Soccer In Progress, it is In Progress -- 06/05/2021
//                                AppController.moveGameToThisHeader(g, "Soccer In Progress");
                                AppController.moveGameToThisHeader(g, "In Progress");
                            } else {
                                AppController.moveGameToThisHeader(g, "In Progress");
                            }


                            refreshtabs = true;


                            String prefs = AppController.getUser().getStartedAlert();
                            String arr[] = prefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
                            int popupsecs = 15;
                            int location = 6;
                            String audiofile = "";
                            String[] sports;
                            boolean goodsport = false;

                            try {
                                popup = Boolean.parseBoolean(arr[0]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sound = Boolean.parseBoolean(arr[1]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                popupsecs = Integer.parseInt(arr[2]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                location = Integer.parseInt(arr[3]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                audiofile = arr[4];
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sports = arr[5].split(",");
                                for (int j = 0; j < sports.length; j++) {
                                    String sportid = sports[j];
                                    if (sportid.equals("" + s.getLeague_id()) || sportid.equals(s.getSportname()) || sportid.equalsIgnoreCase("All Sports")) {
                                        goodsport = true;
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                log(ex);
                            }

                            if (goodsport) {
                                if (popup) {
                                    String hrmin = AppController.getCurrentHoursMinutes();
                                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-" + g.getShorthometeam() + "-" + currenthomescore;

//                                    String popalertname = "Alert at:" + hrmin + "\nSTARTED :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
//                                    AppController.alertsVector.addElement(popalertname);
                                    String mesg = "TARTED :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                                    AppController.addAlert(hrmin,mesg);

                                    new UrgentMessage("<HTML><H1>STARTED</H1><FONT COLOR=BLUE>" +
                                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD></TR>" +
                                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD></TR>" +
                                            "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());
                                }

                                if (sound) {
                                    if (audiofile.equals("")) {
                                        //playSound("started.wav");
                                        new SoundPlayer("started.wav");
                                    } else {
                                        //playSound(audiofile);
                                        new SoundPlayer(audiofile);
                                    }
                                }

                            }


                        }
                    } else if (status.equalsIgnoreCase("Time"))    // its halftime
                    {
                        if (!g.getStatus().equals(status)) // just became halftime
                        {
                            log("game " + gameid + "..just went to halftime");
                            refreshtabs = true;
                            Sport s = AppController.getSport(g.getLeague_id());
                            int id = s.getParentleague_id();
                            if (id == SiaConst.SoccerLeagueId) {
//                                AppController.moveGameToThisHeader(g, "Soccer Halftime");
                                AppController.moveGameToThisHeader(g, "Halftime");

                            } else {
                                AppController.moveGameToThisHeader(g, "Halftime");
                            }
                            refreshtabs = true;

                            String prefs = AppController.getUser().getHalftimeAlert();
                            String arr[] = prefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
                            int popupsecs = 15;
                            int location = 6;
                            String audiofile = "";
                            String[] sports;
                            boolean goodsport = false;

                            try {
                                popup = Boolean.parseBoolean(arr[0]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sound = Boolean.parseBoolean(arr[1]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                popupsecs = Integer.parseInt(arr[2]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                location = Integer.parseInt(arr[3]);
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                audiofile = arr[4];
                            } catch (Exception ex) {
                                log(ex);
                            }
                            try {
                                sports = arr[5].split(",");
                                for (int j = 0; j < sports.length; j++) {
                                    String sportid = sports[j];
                                    if (sportid.equals("" + s.getLeague_id()) || sportid.equals(s.getSportname()) || sportid.equalsIgnoreCase("All Sports")) {
                                        goodsport = true;
                                        break;
                                    }
                                }
                            } catch (Exception ex) {
                                log(ex);
                            }

                            if (goodsport) {
                                if (popup) {
                                    String hrmin = AppController.getCurrentHoursMinutes();
                                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-" + g.getShorthometeam() + "-" + currenthomescore;

//                                    String popalertname = "Alert at:" + hrmin + "\nHALFTIME :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
//                                    AppController.alertsVector.addElement(popalertname);
                                    String mesg = "HALFTIME :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                                    AppController.addAlert(hrmin,mesg);


                                    new UrgentMessage("<HTML><H1>HALFTIME</H1><FONT COLOR=BLUE>" +
                                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD><TD>" + currentvisitorscore + "</TR>" +
                                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD><TD>" + currenthomescore + "</TR>" +
                                            "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());
                                }

                                if (sound) {
                                    if (audiofile.equals("")) {
                                        //playSound("halftime.wav");
                                        new SoundPlayer("halftime.wav");
                                    } else {
                                        //playSound(audiofile);
                                        new SoundPlayer(audiofile);
                                    }
                                }


                            }


                        }
                    } else if (g.getStatus().equalsIgnoreCase("Time"))    // its already halftime // instead may make index of a :?
                    {
                        if (!g.getStatus().equals(status)) // halftime just finished
                        {
                            log("game " + gameid + "..just went out of halftime");
                            refreshtabs = true;
                            Sport s = AppController.getSport(g.getLeague_id());
                            int id = s.getParentleague_id();
                            if (id == SiaConst.SoccerLeagueId) {
//                                AppController.moveGameToThisHeader(g, "Soccer In Progress");
                                //should be In Progress 06/12/2021
                                AppController.moveGameToThisHeader(g, "In Progress");

                            } else {
                                AppController.moveGameToThisHeader(g, "In Progress");
                            }


                        }
                    }


                    g.updateScore(period, timer, status, new java.sql.Timestamp(gamestatuslong), currentvisitorscore, visitorscoresupplemental,
                            new java.sql.Timestamp(scorets), currenthomescore, homescoresupplemental);

                    if (refreshtabs) {

                    } else {

                    }
                } else {
                    g = new Game();
                    g.updateScore(period, timer, status, new java.sql.Timestamp(gamestatuslong), currentvisitorscore, visitorscoresupplemental,
                            new java.sql.Timestamp(scorets), currenthomescore, homescoresupplemental);
                    AppController.addGame(g);
                }
                scoreMessageProcessor.addMessage(gameid);
            }

            //AppController.fireAllTableDataChanged();
        } catch (Exception e) {
            log("exception scores consumer " + e);
            log(mapMessage.toString());
            log(e);

        }
//        try {
//            AppController.fireAllTableDataChanged("" + gameid);
//        } catch (Exception ex) {
//            log(ex);
//        }
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
    private MessageConsumingScheduler<Integer> createScoreMessageProcessor() {
        Consumer<List<Integer>> messageConsumer = (buffer)-> {
            Set<Integer> distinctSet = new HashSet<>(buffer);
if ( buffer.size() > 1) {
    log("ScoresConsumer batch process: queue size=" + buffer.size() + ", distinctSet size=" + distinctSet.size());
}
            Utils.checkAndRunInEDT(()->{
                AppController.fireAllTableDataChanged(distinctSet);
            });

        };
        MessageConsumingScheduler<Integer> scoreMessageProcessor = new MessageConsumingScheduler<>(messageConsumer);
        scoreMessageProcessor.setInitialDelay(2*1000L);
//        scoreMessageProcessor.setUpdatePeriodInMilliSeconds(1500L);
        scoreMessageProcessor.setUpdatePeriodInMilliSeconds(-1500L);
        return scoreMessageProcessor;
    }
}
