package com.sia.client.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Bookie;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.Spreadline;
import com.sia.client.model.User;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.ui.lineseeker.AlertAttrManager;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.UUID;

import static com.sia.client.config.Utils.log;

// bam 79106... 79092
// res 9767...9764
// hrk 1086...
// gnu 74409..74356
// fan 66549
public class LoginClient implements MessageListener {

    //private String brokerUrl = "failover:(ssl://localhost:61617)";
    //private String requestQueue = "spankodds.LOGIN";

    public boolean loginresultback = false;
    Connection connection;
    boolean loggedin = false;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination tempDest;
    private String ip = "";


    public LoginClient() {
        try {
            AppController.addBookie(new Bookie(990, "Details", "Dtals", "", ""));
            AppController.addBookie(new Bookie(991, "Time", "Time", "", ""));
            AppController.addBookie(new Bookie(992, "Gm#", "Gm#", "", ""));
            AppController.addBookie(new Bookie(993, "Team", "Team", "", ""));
            AppController.addBookie(new Bookie(994, "Chart", "Chart", "", ""));
            AppController.addBookie(new Bookie(996, "*Best", "*Best", "", ""));
            //should i add chart bookie here????
            connection = AppController.getGuestConnection();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination adminQueue = session.createQueue(AppController.getLoginQueue());

            producer = session.createProducer(adminQueue);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            tempDest = session.createTemporaryQueue();

            consumer = session.createConsumer(tempDest);

            consumer.setMessageListener(this);
            connection.start();

        } catch (Exception ex) {
            log(ex);
        }
    }
    public LoginClient(String ip)
    {
        this();
        this.ip = ip;

    }
    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME") + "\\conf\\client.ts");
        //String servername = "failover:(ssl://localhost:61617)";
        //String servername = "failover:(ssl://71.172.25.164:61617)";
        String servername = "failover:(tcp/71.172.25.164:61616)";
        String un = "guest";
        String pw = "spank0dds4ever";
        String queuename = "spankodds.LOGIN";
        LoginClient client = new LoginClient();

        log(new java.util.Date());
        client.login(args[0], args[1]);

        while (!client.getLoginResultBack()) //wait for login
        {
            //log(client.loginresultback+" "+new java.util.Date());
            log("");
        }

        log("LOGIN=" + client.isloggedin());
        log(String.valueOf(new java.util.Date()));

    }

    //called by login button
    public void login(String username, String password) throws JMSException {
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("username", username);
        mapMessage.setString("password", password);
        mapMessage.setString("ip", ip);

        mapMessage.setJMSReplyTo(tempDest);

        String correlationId = UUID.randomUUID().toString();
        mapMessage.setJMSCorrelationID(correlationId);
        this.producer.send(mapMessage);
    }

    public boolean getLoginResultBack() {
        return loginresultback;
    }

    public boolean isloggedin() {
        return loggedin;

    }

    public void setLoginResultBack(boolean b) {
        loginresultback = b;
    }

    public void stop() throws JMSException {
        producer.close();
        consumer.close();
        session.close();
        connection.close();
    }
    @Override
    public void onMessage(Message message) {
        Utils.ensureNotEdtThread();
        try {
            String messageType = message.getStringProperty("messageType");
            //log("Messagetype is: "+message.getStringProperty("messageType"));

            if (messageType.equals("loginResult")) {
                log("login back start " + new java.util.Date());
                TextMessage response = (TextMessage) message;
                String text = response.getText();
                log("TEXTRECEIVED=" + text);
                if (text.equals("")) {
                    this.setloggedin(false);
                    setLoginResultBack(true);
                    log("LOGIN NO GOOD");
                } else {
                    this.setloggedin(true);
                    String[] array = text.split(SiaConst.MessageDelimiter);

                    User.instance().init(array[0], array[1], array[2], array[3], array[4], array[5], array[6], array[7], array[8], array[9], array[10], array[11], Boolean.parseBoolean(array[12]), array[13], array[14],
                            array[15],
                            array[16],
                            array[17],
                            array[18],
                            array[19],
                            array[20],
                            array[21],
                            array[22],
                            array[23],
                            array[24],
                            array[25],
                            array[26],
                            array[27],
                            array[28],
                            array[29],
                            array[30],
                            array[31],
                            array[32],
                            array[33],
                            array[34],
                            array[35],
                            Integer.parseInt(array[36]),
                            Integer.parseInt(array[37]),
                            array[38],
                            array[39]
                    );
                    log("ABOUT TO CALL SETUSER!!!");
                    AppController.enrichUserProperties(User.instance());

                    AppController.initializeSportsTabPaneVectorFromUser();
                    try {
                        AppController.initializeLineAlertVectorFromUser();
                    } catch (Exception ex) {
                        log(ex);

                    }

                }


            } else if ( messageType.equals("bookiecolumnchanges")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                AppController.getUser().setBookieColumnsChanged(text);
            } else if ( messageType.equals("loginkey")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                AppController.getUser().setLoginKey(text);
            }else if (SiaConst.Serialization.LineSeekerAlert.equals(messageType)) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                AlertAttrManager.deSerializeAlertAlertAttColl(text);
            } else if (SiaConst.Serialization.Font.equals(messageType)) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                Utils.checkAndRunInEDT(()-> {
                    try {
                        FontConfig.deSerialize(text);
                    } catch (JsonProcessingException e) {
                        Utils.log(e);
                    }
                });
            } else if (messageType.equals("QueueCredentials")) {
                setLoginResultBack(true);
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);

                AppController.createLoggedInConnection(array[0], array[1]);

                log("just created loginconnection!");


            } else if (messageType.equals("Bookie")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);

                int bookieid = Integer.parseInt(array[0]);
                Bookie bookie = new Bookie(bookieid, array[1], array[2], array[3], array[4]);
                Bookie openerbookie = new Bookie(1000 + bookieid, array[1] + "-OPEN", array[2] + "-O", array[3], array[4]);
                AppController.addBookie(bookie);
                AppController.addBookie(openerbookie);

            } else if (messageType.equals("Sport")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);
                Sport sport = new Sport(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]),
                        Integer.parseInt(array[3]), Integer.parseInt(array[4]), Double.parseDouble(array[5]),
                        Double.parseDouble(array[6]), array[7], array[8], array[9], array[10], Boolean.parseBoolean(array[11]));
                AppController.addSport(sport);

            } else if (messageType.equals("Game")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                //log game messages  -- 2021-10-09
                InitialGameMessages.addText(text);
                //allow reading games from log for test purpose  -- 2021-10-09
                if (!SpankOdds.getMessagesFromLog) {
                    AppController.pushGameToCache(GameUtils.parseGameText(text));
                }
            } else if (messageType.equals("Spreadline")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);

                Spreadline line = new Spreadline(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Double.parseDouble(array[2]),
                        Double.parseDouble(array[3]), Double.parseDouble(array[4]), Double.parseDouble(array[5]),
                        Utils.parseTimestamp(array[6]),
                        Double.parseDouble(array[7]), Double.parseDouble(array[8]), Double.parseDouble(array[9]), Double.parseDouble(array[10]), Utils.parseTimestamp(array[11]),
                        Double.parseDouble(array[12]), Double.parseDouble(array[13]), Double.parseDouble(array[14]), Double.parseDouble(array[15]), Utils.parseTimestamp(array[16]),
                        Integer.parseInt(array[17]),Integer.parseInt(array[18]));
                AppController.addSpreadline(line);
                //setLoginResultBack(true);
                //owen took out 7/11/20 and moved back to loginconnection

            } else if (messageType.equals("Totalline")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);

                Totalline line = new Totalline(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Double.parseDouble(array[2]),
                        Double.parseDouble(array[3]), Double.parseDouble(array[4]), Double.parseDouble(array[5]),
                        Utils.parseTimestamp(array[6]), Double.parseDouble(array[7]), Double.parseDouble(array[8]), Double.parseDouble(array[9]),
                        Double.parseDouble(array[10]), Utils.parseTimestamp(array[11]), Double.parseDouble(array[12]), Double.parseDouble(array[13]),
                        Double.parseDouble(array[14]), Double.parseDouble(array[15]), Utils.parseTimestamp(array[16]),

                        Integer.parseInt(array[17]),Integer.parseInt(array[18]));
                int period = Integer.parseInt(array[17]);

                AppController.addTotalline(line);


            } else if (messageType.equals("TeamTotalline")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);


                TeamTotalline line = new TeamTotalline(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Double.parseDouble(array[2]),
                        Double.parseDouble(array[3]), Double.parseDouble(array[4]), Double.parseDouble(array[5]),
                        Double.parseDouble(array[6]),
                        Double.parseDouble(array[7]),
                        Double.parseDouble(array[8]),
                        Double.parseDouble(array[9]),

                        Utils.parseTimestamp(array[10]),

                        Double.parseDouble(array[11]),
                        Double.parseDouble(array[12]),
                        Double.parseDouble(array[13]),
                        Double.parseDouble(array[14]),
                        Double.parseDouble(array[15]),
                        Double.parseDouble(array[16]),
                        Double.parseDouble(array[17]),
                        Double.parseDouble(array[18]),

                        Utils.parseTimestamp(array[19]),

                        Double.parseDouble(array[20]),
                        Double.parseDouble(array[21]),
                        Double.parseDouble(array[22]),
                        Double.parseDouble(array[23]),
                        Double.parseDouble(array[24]),
                        Double.parseDouble(array[25]),
                        Double.parseDouble(array[26]),
                        Double.parseDouble(array[27]),

                        Utils.parseTimestamp(array[28]),
                        Integer.parseInt(array[29]),
                        Integer.parseInt(array[30]));
                int period = Integer.parseInt(array[29]);

                AppController.addTeamTotalline(line);


            } else if (messageType.equals("Moneyline")) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                String[] array = text.split(SiaConst.MessageDelimiter);

                Moneyline line = new Moneyline(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Double.parseDouble(array[2]),
                        Double.parseDouble(array[3]), Double.parseDouble(array[4]), Utils.parseTimestamp(array[5]),
                        Double.parseDouble(array[6]), Double.parseDouble(array[7]), Double.parseDouble(array[8]),
                        Utils.parseTimestamp(array[9]), Double.parseDouble(array[10]), Double.parseDouble(array[11]),
                        Double.parseDouble(array[12]), Utils.parseTimestamp(array[13]),

                        Integer.parseInt(array[14]),
                        Integer.parseInt(array[15]));
                int period = Integer.parseInt(array[14]);

                AppController.addMoneyline(line);


            } else if (messageType.equals("Unsubscribe")) {

                AppController.createGamesConsumer();
                AppController.createScoresConsumer();
                AppController.createUrgentsConsumer();
                AppController.createLinesConsumer();
                AppController.createChartChecker();
                log("Unsubscribing....." + new java.util.Date());
                consumer.close();

            }

        } catch (Exception ex) {
            log(ex);

        }
    }

    public void setloggedin(boolean loggedin) {
        this.loggedin = loggedin;

    }

}
