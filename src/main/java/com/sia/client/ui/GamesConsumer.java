package com.sia.client.ui;

import com.sia.client.config.GameUtils;
import com.sia.client.config.Utils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameStatus;
import com.sia.client.model.MqMessageProcessor;
import com.sia.client.model.Sport;
import com.sia.client.simulator.GameMessageSimulator;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.OngoingGameMessages.MessageType;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.sql.Time;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.sia.client.config.Utils.log;

public class GamesConsumer implements MessageListener {

    //TODO set toSimulateMQ to false for production
    private static boolean toSimulateMQ = false;
    private final AtomicBoolean simulateStatus = new AtomicBoolean(false);
    private transient Connection connection;
    private transient Session session;

    public GamesConsumer(ActiveMQConnectionFactory factory, Connection connection, String gamesconsumerqueue) throws JMSException {

        this.connection = connection;

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(gamesconsumerqueue + "?consumer.retroactive=true");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        connection.start();
    }

    public void close() throws JMSException {
        if (null != connection) {
            connection.close();
        }
    }

    @Override
    public void onMessage(Message message) {
        if (!SpankOdds.getMessagesFromLog) {
            if (toSimulateMQ) {
                if (simulateStatus.compareAndSet(false, true)) {
                    new GameMessageSimulator(this).start();
                }

            } else {
                AppController.waitForSpankyWindowLoaded();
                Utils.ensureNotEdtThread();
                OngoingGameMessages.addMessage(MessageType.Game, message);
                processMessage((TextMessage) message);
            }
        }
    }

    public void processMessage(TextMessage textMessage) {
        try {
            String leagueid;
            String messagetype = textMessage.getStringProperty("messageType");
            String repaintstr = textMessage.getStringProperty("repaint");
            leagueid = textMessage.getStringProperty("leagueid");
            String oldvpitcher = "";
            String oldhpitcher = "";
            Time oldgametime = null;

            if (leagueid == null) {
                leagueid = "";
            }

            if ("NEWORUPDATE".equals(messagetype)) {

                String data = textMessage.getText();
                String [] fields = GameUtils.parseGameString(data);
                int gameid = Integer.parseInt(fields[0]);
                Game oldGame = AppController.getGame(gameid);
                Game game;
                if ( null != oldGame) {
                    game = oldGame.clone();
                } else {
                    game = new Game();
                }
                GameUtils.setGameProperty(game,fields);

                if ( null != oldGame ) {
                    oldvpitcher = oldGame.getVisitorpitcher();
                    oldhpitcher = oldGame.getHomepitcher();
                    oldgametime = oldGame.getGametime();

                }
//                log("new game! " + data);
                AppController.pushGameToCache(game);
                MqMessageProcessor.getInstance().addGame(game);
                Sport s = AppController.getSportByLeagueId(game.getLeague_id());

                boolean seriesprice = false;
                try {
                    seriesprice = game.isSeriesprice();
                } catch (Exception ex) {
                    log(ex);
                }

                if ((!oldvpitcher.equals(game.getVisitorpitcher()) && !oldvpitcher.equals("") && !game.getVisitorpitcher().equalsIgnoreCase("UNDECIDED"))
                        || (!oldhpitcher.equals(game.getHomepitcher()) && !oldhpitcher.equals("") && !game.getHomepitcher().equalsIgnoreCase("UNDECIDED"))
                ) { //pitching change
                    int popupsecs = 15;
                    int location = 2;
                    String hrmin = AppController.getCurrentHoursMinutes();
                    String teaminfo = game.getVisitorgamenumber() + "-" + game.getShortvisitorteam() + "@" + game.getHomegamenumber() + "-" + game.getShorthometeam();

//                    String popalertname = "Alert at:" + hrmin + "\nPitching Change:" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
//                    AppController.alertsVector.addElement(popalertname);
                    String mesg = "Pitching Change:" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                    AppController.addAlert(hrmin, mesg);

                    new UrgentMessage("<HTML><H1>Pitching Change</H1><FONT COLOR=BLUE>" +
                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                            "<TR><TD>" + game.getVisitorgamenumber() + "</TD><TD>" + game.getVisitorteam() + "</TD><TD>" +game.getVisitorpitcher() + "</TD></TR>" +
                            "<TR><TD>" + game.getHomegamenumber() + "</TD><TD>" + game.getHometeam() + "</TD><TD>" + game.getHomepitcher() + "</TD></TR>" +

                            "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());


                    //playSound("pitchingchange.wav");
                    new SoundPlayer("pitchingchange.wav");

                    String tc = new java.util.Date() + "..PITCHING CHANGE!!!<HTML><H1>Pitching Change</H1><FONT COLOR=BLUE>" +
                            s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                            "<TR><TD>" + game.getVisitorgamenumber() + "</TD><TD>" + game.getVisitorteam() + "</TD><TD>" + game.getVisitorpitcher() + "</TD></TR>" +
                            "<TR><TD>" + game.getHomegamenumber() + "</TD><TD>" + game.getHometeam() + "</TD><TD>" + game.getHomepitcher() + "</TD></TR></TABLE></FONT></HTML>";
                    try {
                        //what is the use of the statement? 05/01/2022
//                        writeToFile("c:\\spankoddsclient2\\timechanges.txt", tc, true);
                    } catch (Exception ex) {
                        log(ex);
                    }
                    log(tc);
                }

                if (!seriesprice && oldgametime != null && !"".equals(oldgametime) && !oldgametime.toString().equals(game.getGametime().toString())) // time change
                {

                    String prefs = AppController.getUser().getTimechangeAlert();
                    String[] arr = prefs.split("\\|");
                    boolean popup = false;
                    boolean sound = false;
                    int popupsecs = 15;
                    int location = 6;
                    String audiofile = "";
                    String[] sports;
                    boolean goodsport = false;

                    popup = Utils.parse(arr[0],popup);
                    sound =  Utils.parse(arr[1],sound);
                    popupsecs = Utils.parse(arr[2],popupsecs);
                    location = Utils.parse(arr[3],location);
                    audiofile = arr[4];

                    try {
                        sports = arr[5].split(",");
                        for (String sportid : sports) {
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
                            String teaminfo = game.getVisitorgamenumber() + "-" + game.getShortvisitorteam() + "@" + game.getHomegamenumber() + "-" + game.getShorthometeam();
                            String mesg = "Time Change:" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo + " From " + oldgametime + "to " + game.getGametime();
                            AppController.addAlert(hrmin, mesg);

                            new UrgentMessage("<HTML><H1>Time Change</H1><FONT COLOR=BLUE>" +
                                    s.getLeaguename() + "<BR><TABLE cellspacing=5 cellpadding=5>" +

                                    "<TR><TD>" + game.getVisitorgamenumber() + "</TD><TD>" + game.getVisitorteam() + "</TD></TR>" +
                                    "<TR><TD>" + game.getHomegamenumber() + "</TD><TD>" + game.getHometeam() + "</TD></TR>" +
                                    "<TR><TD>From " + oldgametime + "</TD><TD>to " + game.getGametime() + "</TD></TR>" +
                                    "</TABLE></FONT></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());


                        }

                        if (sound) {
                            if (audiofile.equals("")) {
                                new SoundPlayer("timechange.wav");
                            } else {
                                new SoundPlayer(audiofile);
                            }
                        }

                    }
                    String tc = new java.util.Date() + "..TIME CHANGE!!! " + game.getVisitorgamenumber() + "..old=" + oldgametime.getTime() + "...new=" + game.getGametime().getTime();
                    try {
//                        writeToFile("c:\\spankoddsclient2\\timechanges.txt", tc, true);
                        log(tc);
                    } catch (Exception ex) {
                        log(ex);
                    }
                }

            } else if (messagetype.equals("NEWORUPDATE2")) // this comes from deleteyesterdaygamesandpublish whith a few additional flags
            {

                String data = textMessage.getText();
                String [] fields = GameUtils.parseGameString(data);
                Game g = new Game();
                GameUtils.setGameProperty(g,fields);
                Game oldGame = AppController.getGame(g.getGame_id());
                boolean changed = ! g.equals(oldGame);
                if ( changed) {
                    if ( null != oldGame) {
                        moveIfGameGroupChanged(oldGame, g);
                    }
                    AppController.pushGameToCache(g);
                    MqMessageProcessor.getInstance().addGame(g);
                }
            } else if (messagetype.equals("REMOVE")) {
                String data = textMessage.getText(); // this is gamenumber
                String[] gameidarr = data.split("~");
                log("GamesConsumer: REMOVE game ids " + data);
                AppController.removeGamesAndCleanup(gameidarr);


            } else if (messagetype.equals("REMOVEDATE")) {
                String date = textMessage.getText(); // this is gamenumber
                log("GamesConsumer: REMOVE DATE date=" + date);
                AppController.removeGameDate(date, leagueid);


            }
        } catch (Exception ex) {
            log("textMessage=" + textMessage.toString());
            log(ex);
        }
    }
    private void moveIfGameGroupChanged(Game oldGame,Game newGame) {
        //check if game status changes
        GameStatus oldStatus = GameStatus.getGameStatus(oldGame);
        GameStatus newStatus = GameStatus.getGameStatus(newGame);
        if ( ! Objects.equals(oldStatus,newStatus)) {
            GameGroupHeader newGameGroupHeader = null == newStatus?GameUtils.createGameGroupHeader(newGame):newStatus.getGroupHeader();
            AppController.pushGameToCache(newGame);
            AppController.moveGameToThisHeader(newGame,newGameGroupHeader);
        }
    }
    public static void writeToFile(String fileName, String data, boolean append) {
        DataOutputStream out = null;

        try {
            out = new DataOutputStream(new FileOutputStream(fileName, append));
            out.writeBytes(data + "\n");

        } catch (Exception e) {
            log("Couldn't get I/O for htmlfile " + fileName + "\n" + e);
            log(e);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (Exception ex) {
                log(ex);
            }
        }


    }
//
//    public void playSound(String file) {
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.start();
//        } catch (Exception ex) {
//            log(ex);
//        }
//    }

}
