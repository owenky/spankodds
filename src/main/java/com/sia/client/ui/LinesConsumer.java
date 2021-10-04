package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameMessageProcessor;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Spreadline;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import java.text.SimpleDateFormat;

import static com.sia.client.config.Utils.log;

public class LinesConsumer implements MessageListener {

    //private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    int gameid = 0;
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
    //TODO: need to fine tune GameMessageProcessor constructor parameters.
    private final GameMessageProcessor gameMessageProcessor = new GameMessageProcessor(2000L,-1500L);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public LinesConsumer(ActiveMQConnectionFactory factory, Connection connection, String linesconsumerqueue) throws JMSException {

        this.connection = connection;
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(linesconsumerqueue + "?consumer.retroactive=true");
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
        Utils.ensureNotEdtThread();
        processMessage((MapMessage)message);
    }
    private void processMessage(MapMessage message) {
        try {

            mapMessage =  message;
            gameid = mapMessage.getInt("gameid");
            int bookieid = mapMessage.getInt("bookieid");
            int period = mapMessage.getInt("period");
            String isopenerS = mapMessage.getString("isopener");
            String changetype = mapMessage.getStringProperty("messageType");
            long newlongts = 0;


            boolean isopener = false;
            if ("1".equals(isopenerS)) {
                isopener = true;
            }

            if ("LineChangeSpread".equals(changetype)) {
                double newvisitorspread = 0;
                double newhomespread = 0;
                double newvisitorjuice = 0;
                double newhomejuice = 0;
                try {
                    newvisitorspread = mapMessage.getDouble("newvisitorspread");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomespread = mapMessage.getDouble("newhomespread");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newvisitorjuice = mapMessage.getDouble("newvisitorjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomejuice = mapMessage.getDouble("newhomejuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newlongts = mapMessage.getLong("newlongts");
                    newlongts = newlongts + (1000 * 60 * 60 * 3);
                } catch (Exception ex) {
                    log(ex);
                }

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = message.getJMSTimestamp();

                Spreadline sl = AppController.getSpreadline(bookieid, gameid, period);
                if (null != sl) {
                    sl.recordMove(newvisitorspread, newvisitorjuice, newhomespread, newhomejuice, new java.sql.Timestamp(newlongts), isopener);
                } else {
                    sl = new Spreadline(gameid, bookieid, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice, new java.sql.Timestamp(newlongts), period);
                    //System.out.println("***************************************spreadxyzabc******************************");
                    if (isopener) {
                        LineAlertOpeners.spreadOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice);
                        //System.out.println("***************************************"+sportname+"******************************");
                    }

                    AppController.addSpreadline(sl);
                }
            } else if ("LineChangeTotal".equals(changetype)) {


                double newover = 0;
                double newunder = 0;
                double newoverjuice = 0;
                double newunderjuice = 0;
                try {
                    newover = mapMessage.getDouble("newover");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newunder = mapMessage.getDouble("newunder");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newoverjuice = mapMessage.getDouble("newoverjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newunderjuice = mapMessage.getDouble("newunderjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newlongts = mapMessage.getLong("newlongts");
                    newlongts = newlongts + (1000 * 60 * 60 * 3);
                } catch (Exception ex) {
                    log(ex);
                }

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = message.getJMSTimestamp();


                Totalline tl = AppController.getTotalline(bookieid, gameid, period);
                if (null != tl) {
                    tl.recordMove(newover, newoverjuice, newunder, newunderjuice, new java.sql.Timestamp(newlongts), isopener);
                } else {
                    tl = new Totalline(gameid, bookieid, newover, newoverjuice, newunder, newunderjuice, new java.sql.Timestamp(newlongts), period);
                    if (isopener) {
                        LineAlertOpeners.totalOpenerAlert(gameid, bookieid, period, isopenerS, newover, newoverjuice, newunder, newunderjuice);
                        //System.out.println("***************************************"+sportname+"******************************");
                    }
                    //	System.out.println("***************************************totalOpenerAlert******************************");
                    AppController.addTotalline(tl);
                }

            } else if ("LineChangeTeamTotal".equals(changetype)) {
                double newvisitorover = 0;
                double newvisitorunder = 0;
                double newvisitoroverjuice = 0;
                double newvisitorunderjuice = 0;

                double newhomeover = 0;
                double newhomeunder = 0;
                double newhomeoverjuice = 0;
                double newhomeunderjuice = 0;
                try {
                    newvisitorover = mapMessage.getDouble("newvisitorover");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newvisitorunder = mapMessage.getDouble("newvisitorunder");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newvisitoroverjuice = mapMessage.getDouble("newvisitoroverjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newvisitorunderjuice = mapMessage.getDouble("newvisitorunderjuice");
                } catch (Exception ex) {
                    log(ex);
                }

                try {
                    newhomeover = mapMessage.getDouble("newhomeover");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomeunder = mapMessage.getDouble("newhomeunder");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomeoverjuice = mapMessage.getDouble("newhomeoverjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomeunderjuice = mapMessage.getDouble("newhomeunderjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newlongts = mapMessage.getLong("newlongts");
                    newlongts = newlongts + 1000 * 60 * 60 * 3;
                } catch (Exception ex) {
                    log(ex);
                }

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = message.getJMSTimestamp();


                TeamTotalline ttl = AppController.getTeamTotalline(bookieid, gameid, period);
                if (null != ttl) {

                    ttl.recordMove(newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice,
                            new java.sql.Timestamp(newlongts), isopener);
                } else {
                    ttl = new TeamTotalline(gameid, bookieid, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice, new java.sql.Timestamp(newlongts), period);
                    if (isopener) {
                        LineAlertOpeners.teamTotalOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice);
                    }
                    AppController.addTeamTotalline(ttl);
                }

            } else if ("LineChangeMoney".equals(changetype)) {
                double newvisitorjuice = 0;
                double newhomejuice = 0;
                double newdrawjuice = 0;
                try {
                    newvisitorjuice = mapMessage.getDouble("newvisitorjuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newhomejuice = mapMessage.getDouble("newhomejuice");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newdrawjuice = mapMessage.getDouble("newdrawjuice");
                } catch (Exception ex) {
                    newdrawjuice = 0;
                    log(ex);
                }
                try {
                    newlongts = mapMessage.getLong("newlongts");
                    newlongts = newlongts + 1000 * 60 * 60 * 3;
                } catch (Exception ex) {
                    log(ex);
                }

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = message.getJMSTimestamp();
                Moneyline ml = AppController.getMoneyline(bookieid, gameid, period);

                // owen hack this since draw change comes in seperately
                // this is a draw move only so use old visit and home lines
                if (period == 0 && bookieid == 17 && gameid >= 200000 && gameid <= 300000) {
                    //System.out.println("MONEY..gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
                }
                if (newdrawjuice == 0 && ml != null && newvisitorjuice != 0 && newhomejuice != 0) {
                    newdrawjuice = ml.getCurrentdrawjuice();
                    if (period == 0 && bookieid == 17) {
                        //System.out.println("gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
                    }
                } else if (newdrawjuice != 0 && ml != null && newvisitorjuice == 0 && newhomejuice == 0) {
                    //newvisitorjuice = ml.getCurrentvisitjuice();
                    //newhomejuice = ml.getCurrenthomejuice();
                }


                if (null != ml) {
                    ml.recordMove(newvisitorjuice, newhomejuice, newdrawjuice, new java.sql.Timestamp(newlongts), isopener);
                } else {
                    ml = new Moneyline(gameid, bookieid, newvisitorjuice, newhomejuice, newdrawjuice, new java.sql.Timestamp(newlongts), period);
                    if (isopener) {
                        LineAlertOpeners.moneyOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorjuice, newhomejuice);
                        //System.out.println("***************************************"+sportname+"******************************");
                    }
                    //	System.out.println("***************************************moneyOpenerAlert******************************");
                    AppController.addMoneyline(ml);
                }


                // {gameid=207277, newdrawjuice=244.0, isopener=1, newlongts=1590351296000, bookieid=140}
            }
            //com.sia.client.ui.AppController.getLinesTableData().fireTableDataChanged();


        } catch (Exception e) {
            log(mapMessage.toString());
            log(e);

        }
        Game game = AppController.getGame(gameid);
        gameMessageProcessor.addGame(game);
    }
}
