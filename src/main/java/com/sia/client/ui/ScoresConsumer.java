package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
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

import static com.sia.client.config.Utils.*;

public class ScoresConsumer implements MessageListener {

    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;

    public ScoresConsumer(ActiveMQConnectionFactory factory, Connection connection, String scoresconsumerqueue) throws JMSException {

        this.connection = connection;

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(scoresconsumerqueue + "?consumer.retroactive=true");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        connection.start();
    }

    public static void main(String[] args) throws JMSException {
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
        AppController.createLoggedInConnection("reguser", "0hbaby*(");
        //com.sia.client.ui.AppController.createLoggedInConnection("guest","spank0dds4ever");
        LinesConsumer consumer = new LinesConsumer(AppController.getConnectionFactory(), AppController.getLoggedInConnection(), "spankoddsin.LINECHANGE");

    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public void onMessage(Message message) {
        Utils.ensureNotEdtThread();
        int gameid = 0;
        try {

            mapMessage = (MapMessage) message;

            String changetype = mapMessage.getStringProperty("messageType");


            if ("ScoreChange".equals(changetype)) {
                gameid = mapMessage.getInt("eventnumber");
                final String period = getString(mapMessage, "period");
                final String timer = getString(mapMessage, "timer");
                final String status = getString(mapMessage, "status");
                final long gamestatuslong = getLong(mapMessage, "gamestatusts", 100L);
                final int currentvisitorscore = getInt(mapMessage, "currentvisitorscore");
                final String visitorscoresupplemental = getString(mapMessage, "visitorscoresupplemental");
                final long scorets = getLong(mapMessage, "scorets", 100L);
                final int currenthomescore = getInt(mapMessage, "currenthomescore");
                final String homescoresupplemental = getString(mapMessage, "homescoresupplemental");

                final Game g = AppController.getGame(gameid);
                if (null != g) {

                    //owen 8/11 moved final as first block since grand salami was causing started and final to both execute
                    if ("Final".equalsIgnoreCase(status) || "Win".equalsIgnoreCase(status)) {
                        if (!g.getStatus().equals(status)) // just became final
                        {

                            final Sport s = AppController.getSport(g.getLeague_id());

                            int id = s.getParentleague_id();
                            if (id == 9) {
                                AppController.moveGameToThisHeader(g, "Soccer FINAL");
                            } else {
                                AppController.moveGameToThisHeader(g, "FINAL");
                            }
                            String finalprefs = AppController.getUser().getFinalAlert();
                            log("game " + gameid + "..just went final");
                            String[] finalarr = finalprefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
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

                            final int popupsecs = parseInt(finalarr[2], 15);
                            final int location = parseInt(finalarr[3], 6);

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
                                            || "All Sports".equalsIgnoreCase(sportid)) {
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
                                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-"
                                            + g.getShorthometeam() + "-" + currenthomescore;

                                    String popalertname = "Alert at:" + hrmin + "\nFINAL :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;

                                    AppController.alertsVector.addElement(popalertname);

                                    new UrgentMessage("<HTML><H1>FINAL</H1><FONT COLOR=BLUE>" +
                                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD><TD>" + currentvisitorscore + "</TR>" +
                                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD><TD>" + currenthomescore + "</TR>" +
                                            "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());


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

                        }
                    } else if (g.getStatus() == null || g.getStatus().equalsIgnoreCase("NULL") || g.getStatus().equals("")) {
                        // game hasn't started
                        if (!g.getStatus().equals(status)) {
                            //status change game just started
                            Sport s = AppController.getSport(g.getLeague_id());
                            log("game " + gameid + "..just started");
                            int id = s.getParentleague_id();
                            if (id == 9) {
                                AppController.moveGameToThisHeader(g, "Soccer In Progress");
                            } else {
                                AppController.moveGameToThisHeader(g, "In Progress");
                            }


                            String prefs = AppController.getUser().getStartedAlert();
                            String[] arr = prefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
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

                            final int popupsecs = parseInt(arr[2], 15);
                            final int location = parseInt(arr[3], 6);

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

                                    String popalertname = "Alert at:" + hrmin + "\nSTARTED :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;

                                    AppController.alertsVector.addElement(popalertname);

                                    new UrgentMessage("<HTML><H1>STARTED</H1><FONT COLOR=BLUE>" +
                                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD></TR>" +
                                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD></TR>" +
                                            "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());

                                }

                                if (sound) {
                                    if (audiofile.equals("")) {
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
                            Sport s = AppController.getSport(g.getLeague_id());
                            int id = s.getParentleague_id();
                            if (id == 9) {
                                AppController.moveGameToThisHeader(g, "Soccer Halftime");
                            } else {
                                AppController.moveGameToThisHeader(g, "Halftime");
                            }

                            String prefs = AppController.getUser().getHalftimeAlert();
                            String[] arr = prefs.split("\\|");
                            boolean popup = false;
                            boolean sound = false;
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

                            final int popupsecs = parseInt(arr[2], 15);
                            final int location = parseInt(arr[3], 6);

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

                                    String popalertname = "Alert at:" + hrmin + "\nHALFTIME :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                                    AppController.alertsVector.addElement(popalertname);


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
                    } else if ("Time".equalsIgnoreCase(g.getStatus()))    // its already halftime // instead may make index of a :?
                    {
                        if (!g.getStatus().equals(status)) // halftime just finished
                        {
                            log("game " + gameid + "..just went out of halftime");
                            Sport s = AppController.getSport(g.getLeague_id());
                            int id = s.getParentleague_id();
                            if (id == 9) {
                                AppController.moveGameToThisHeader(g, "Soccer In Progress");
                            } else {
                                AppController.moveGameToThisHeader(g, "In Progress");
                            }


                        }
                    }


                    g.updateScore(period, timer, status, new java.sql.Timestamp(gamestatuslong), currentvisitorscore, visitorscoresupplemental,
                            new java.sql.Timestamp(scorets), currenthomescore, homescoresupplemental);

                } else {
                    Game g2 = new Game();
                    g2.updateScore(period, timer, status, new java.sql.Timestamp(gamestatuslong), currentvisitorscore, visitorscoresupplemental,
                            new java.sql.Timestamp(scorets), currenthomescore, homescoresupplemental);
                    AppController.addGame(g2);
                }

            }

            //com.sia.client.ui.AppController.fireAllTableDataChanged();


        } catch (Exception e) {
            log("exception scores consumer " + e);
            log(mapMessage.toString());
            log(e);

        }
        try {
            AppController.fireAllTableDataChanged("" + gameid);
        } catch (Exception ex) {
            log(ex);
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


}
