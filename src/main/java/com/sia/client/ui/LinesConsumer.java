package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameMessageProcessor;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Spreadline;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.OngoingGameMessages.MessageType;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.consoleLogPeekGameId;

public class LinesConsumer implements MessageListener {

    //private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private transient Connection connection;
    private transient Session session;
    //TODO: need to fine tune GameMessageProcessor constructor parameters.
    private final GameMessageProcessor gameMessageProcessor = new GameMessageProcessor("LineConsumer",2000L,10L);

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
//        synchronized (SiaConst.GameLock) {
            if (! InitialGameMessages.getMessagesFromLog) {
                Utils.ensureNotEdtThread();
                processMessage((MapMessage) message);
                OngoingGameMessages.addMessage(MessageType.Line, message);
            }
//        }
    }
    public void processMessage(MapMessage mapMessage) {
        int gameid = 0;
        try {
            gameid = mapMessage.getInt("gameid");
            if  ( 0 == gameid) {
                return;
            }
            if ( null == AppController.getGame(gameid)) {
                if ( ! AppController.BadGameIds.contains(gameid)) {
                    log("LinesConsumer Warning: null game detected:" + gameid);
                    AppController.BadGameIds.add(gameid);
                }
                return;
            }
            log("LinesConsumer::processMessage: gameid="+gameid);
            consoleLogPeekGameId("LinesConsumer::processMessage",gameid);
            int bookieid = mapMessage.getInt("bookieid");
            int period = mapMessage.getInt("period");
            String isopenerS = mapMessage.getString("isopener");
            String changetype = mapMessage.getStringProperty("messageType");
            long newlongts;

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
                newlongts = mapMessage.getJMSTimestamp();

                Spreadline sl = AppController.getSpreadline(bookieid, gameid, period);
                if (null != sl) {
                    sl.recordMove(newvisitorspread, newvisitorjuice, newhomespread, newhomejuice, newlongts, isopener);
                } else {
                    sl = new Spreadline(gameid, bookieid, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice,newlongts, period);
                    //log("***************************************spreadxyzabc******************************");
                    if (isopener) {
                        LineAlertOpeners.spreadOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice);
                        //log("***************************************"+sportname+"******************************");
                    }

                    AppController.addSpreadline(sl);
                }
            }
            else if ("LineChangeTotal".equals(changetype)) {

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
                newlongts = mapMessage.getJMSTimestamp();


                Totalline tl = AppController.getTotalline(bookieid, gameid, period);
                if (null != tl) {
                    tl.recordMove(newover, newoverjuice, newunder, newunderjuice, newlongts, isopener);
                } else {
                    tl = new Totalline(gameid, bookieid, newover, newoverjuice, newunder, newunderjuice, newlongts, period);
                    if (isopener) {
                        LineAlertOpeners.totalOpenerAlert(gameid, bookieid, period, isopenerS, newover, newoverjuice, newunder, newunderjuice);
                        //log("***************************************"+sportname+"******************************");
                    }
                    //	log("***************************************totalOpenerAlert******************************");
                    AppController.addTotalline(tl);
                }

            }
            else if ("LineChangeTeamTotal".equals(changetype)) {
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
                newlongts = mapMessage.getJMSTimestamp();


                TeamTotalline ttl = AppController.getTeamTotalline(bookieid, gameid, period);
                if (null != ttl) {

                    ttl.recordMove(newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice,
                            newlongts, isopener);
                } else {
                    ttl = new TeamTotalline(gameid, bookieid, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice, newlongts, period);
                    if (isopener) {
                        LineAlertOpeners.teamTotalOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice);
                    }
                    AppController.addTeamTotalline(ttl);
                }

            }
            else if ("LineChangeMoney".equals(changetype)) {
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
                newlongts = mapMessage.getJMSTimestamp();
                Moneyline ml = AppController.getMoneyline(bookieid, gameid, period);

                // owen hack this since draw change comes in seperately
                // this is a draw move only so use old visit and home lines
                if (period == 0 && bookieid == 17 && gameid >= 200000 && gameid <= 300000) {
                    //log("MONEY..gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
                }
                if (newdrawjuice == 0 && ml != null && newvisitorjuice != 0 && newhomejuice != 0) {
                    newdrawjuice = ml.getCurrentdrawjuice();
                    if (period == 0 && bookieid == 17) {
                        //log("gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
                    }
                } else if (newdrawjuice != 0 && ml != null && newvisitorjuice == 0 && newhomejuice == 0) {
                    //newvisitorjuice = ml.getCurrentvisitjuice();
                    //newhomejuice = ml.getCurrenthomejuice();
                }


                if (null != ml) {
                    ml.recordMove(newvisitorjuice, newhomejuice, newdrawjuice, newlongts, isopener);
                } else {
                    ml = new Moneyline(gameid, bookieid, newvisitorjuice, newhomejuice, newdrawjuice, newlongts, period);
                    if (isopener) {
                        LineAlertOpeners.moneyOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorjuice, newhomejuice);
                        //log("***************************************"+sportname+"******************************");
                    }
                    //	log("***************************************moneyOpenerAlert******************************");
                    AppController.addMoneyline(ml);
                }


                // {gameid=207277, newdrawjuice=244.0, isopener=1, newlongts=1590351296000, bookieid=140}
            }
            else if ("LimitChange".equals(changetype)) {
                int newlimit = 0;
                int oldlimit = 0;
                String linetype = "";

                try {
                    linetype = mapMessage.getString("linetype");
                } catch (Exception ex) {
                    log(ex);
                }
                try {
                    newlimit = mapMessage.getInt("newlimit");
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
                newlongts = mapMessage.getJMSTimestamp();

                if(linetype.equalsIgnoreCase("Spread"))
                {
                    Spreadline sl = AppController.getSpreadline(bookieid, gameid, period);
                    if(sl != null)
                    {
                        sl.setLimit(newlimit);
                    }
                }
                else if(linetype.equalsIgnoreCase("Total"))
                {
                    Totalline tl = AppController.getTotalline(bookieid, gameid, period);
                    if(tl != null)
                    {
                        tl.setLimit(newlimit);
                    }
                }
                else if(linetype.equalsIgnoreCase("Moneyline"))
                {
                    Moneyline ml = AppController.getMoneyline(bookieid, gameid, period);
                    if(ml != null)
                    {
                        ml.setLimit(newlimit);
                    }
                }
                else if(linetype.equalsIgnoreCase("TeamTotal"))
                {
                    TeamTotalline ttl = AppController.getTeamTotalline(bookieid, gameid, period);
                    if(ttl != null)
                    {
                        ttl.setLimit(newlimit);
                    }
                }







                // {gameid=207277, newdrawjuice=244.0, isopener=1, newlongts=1590351296000, bookieid=140}
            }
            //com.sia.client.ui.AppController.getLinesTableData().fireTableDataChanged();


        } catch (Exception e) {
            log(mapMessage.toString());
            log(e);

        }
        Game game = AppController.getGame(gameid);
        if ( null == game) {
            if ( mapMessage instanceof ActiveMQMapMessage) {
//                log(new Exception("null game detected...gameid=" + gameid + ", message=" + OngoingGameMessages.convert((ActiveMQMapMessage)mapMessage)));
                log("LinesConsumer: null game detected...gameid=" + gameid);
            } else {
                log("LinesConsumer: null game detected...gameid=" + gameid);
            }
        } else {
            gameMessageProcessor.addGame(game);;
        }
    }
}
