package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.LineEvent;
import com.sia.client.model.LineIdentity;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Spreadline;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.OngoingGameMessages.MessageType;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static com.sia.client.config.Utils.log;

public class LinesConsumer implements MessageListener {

    //private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private transient Connection connection;
    private transient Session session;
    private long lastmessagets = 0;

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

        if (!SpankOdds.getMessagesFromLog) {
            AppController.waitForSpankyWindowLoaded();
            Utils.ensureNotEdtThread();
            processMessage((MapMessage) message);
            OngoingGameMessages.addMessage(MessageType.Line, message);
        }
    }

    public void processMessage(MapMessage mapMessage) {
        int gameid;

        try {
            LineIdentity lineIdentity = LineIdentity.parse(mapMessage);
            String username = mapMessage.getString("username");
            if (null != username && !username.equals("")) {
                //log("Rescue mission");
                if (!username.equals(AppController.getUser().getUsername())) {
                    return;
                }
            }
            long messagets = mapMessage.getJMSTimestamp();
            if (lastmessagets != 0 && Math.abs(messagets - lastmessagets) > 60000) {
                Utils.log("HOUSTON WE HAVE A PROBLEM!\n\n\n");
                Utils.log("NEED TO LOAD DATA FROM " + lastmessagets);
                // use dishoutinitialdata thread but instead pass flag to use linespublisher instead
                AppController.getUserPrefsProducer().helpsyncme(lastmessagets);
                lastmessagets = messagets;

            } else {
                lastmessagets = messagets;
            }
            gameid = lineIdentity.getGameId();
            if (0 == gameid) {
                return;
            }
            if (null == AppController.getGame(gameid)) {
                if (!AppController.BadGameIds.contains(gameid)) {
                    log("LinesConsumer Warning: null game detected:" + gameid);
                    AppController.BadGameIds.add(gameid);
                }
                return;
            }
            int bookieid = lineIdentity.getBookieId();
            int period = lineIdentity.getPeriod();
            String changetype = mapMessage.getStringProperty("messageType");
            long newlongts;

            String isopenerS = mapMessage.getString("isopener");
            boolean isopener = ("1".equals(isopenerS) || "true".equalsIgnoreCase(isopenerS));

            if ("LineChangeSpread".equals(changetype)) {
                double newvisitorspread;
                double newhomespread;
                double newvisitorjuice;
                double newhomejuice;

                newvisitorspread = Utils.getDouble(mapMessage,"newvisitorspread",0d);
                newhomespread = Utils.getDouble(mapMessage,"newhomespread",0d);
                newvisitorjuice = Utils.getDouble(mapMessage,"newvisitorjuice",0d);
                newhomejuice = Utils.getDouble(mapMessage,"newhomejuice",0d);
                newlongts = Utils.getLong(mapMessage,"newlongts",0L);
                newlongts = newlongts + (1000 * 60 * 60 * 3);

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = mapMessage.getJMSTimestamp();

                Spreadline sl = AppController.getSpreadline(bookieid, gameid, period);
                if (null != sl) {
                    sl.recordMove(newvisitorspread, newvisitorjuice, newhomespread, newhomejuice, newlongts, isopener);
                } else {
                    sl = new Spreadline(lineIdentity, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice, newlongts);
                    //log("***************************************spreadxyzabc******************************");
                    if (isopener) {
                        // LineAlertOpeners.spreadOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorspread, newvisitorjuice, newhomespread, newhomejuice);
                        //log("***************************************"+sportname+"******************************");
                    }
                    AppController.addSpreadline(sl);
                }
            }
            else if ("LineChangeTotal".equals(changetype)) {

                double newover;
                double newunder;
                double newoverjuice;
                double newunderjuice;

                newover = Utils.getDouble(mapMessage,"newover",0d);
                newunder = Utils.getDouble(mapMessage,"newunder",0d);
                newoverjuice = Utils.getDouble(mapMessage,"newoverjuice",0d);
                newunderjuice = Utils.getDouble(mapMessage,"newunderjuice",0d);
                newlongts = Utils.getLong(mapMessage,"newlongts",0L);
                newlongts = newlongts + (1000 * 60 * 60 * 3);

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = mapMessage.getJMSTimestamp();


                Totalline tl = AppController.getTotalline(bookieid, gameid, period);
                if (null != tl) {
                    tl.recordMove(newover, newoverjuice, newunder, newunderjuice, newlongts, isopener);
                } else {
                    tl = new Totalline(lineIdentity, newover, newoverjuice, newunder, newunderjuice, newlongts);
                    if (isopener) {
                        //  LineAlertOpeners.totalOpenerAlert(gameid, bookieid, period, isopenerS, newover, newoverjuice, newunder, newunderjuice);
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

                newvisitorover = Utils.getDouble(mapMessage,"newvisitorover",0d);
                newvisitorunder = Utils.getDouble(mapMessage,"newvisitorunder",0d);
                newvisitoroverjuice = Utils.getDouble(mapMessage,"newvisitoroverjuice",0d);
                newvisitorunderjuice = Utils.getDouble(mapMessage,"newvisitorunderjuice",0d);
                newhomeover = Utils.getDouble(mapMessage,"newhomeover",0d);
                newhomeunder = Utils.getDouble(mapMessage,"newhomeunder",0d);
                newhomeoverjuice = Utils.getDouble(mapMessage,"newhomeoverjuice",0d);
                newhomeunderjuice = Utils.getDouble(mapMessage,"newhomeunderjuice",0d);
                newlongts = Utils.getLong(mapMessage,"newlongts",0L);
                newlongts = newlongts + 1000 * 60 * 60 * 3;

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = mapMessage.getJMSTimestamp();


                TeamTotalline ttl = AppController.getTeamTotalline(bookieid, gameid, period);
                if (null != ttl) {

                    ttl.recordMove(newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice,
                            newlongts, isopener);
                } else {
                    ttl = new TeamTotalline(lineIdentity, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice,
                            newhomeover, newhomeoverjuice, newhomeunder, newhomeunderjuice, newlongts);
                    if (isopener) {
                        //  LineAlertOpeners.teamTotalOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorover, newvisitoroverjuice, newvisitorunder, newvisitorunderjuice);
                    }
                    AppController.addTeamTotalline(ttl);
                }

            }
            else if ("LineChangeMoney".equals(changetype)) {
                double newvisitorjuice;
                double newhomejuice;
                double newdrawjuice;

                newvisitorjuice = Utils.getDouble(mapMessage,"newvisitorjuice",0d);
                newhomejuice = Utils.getDouble(mapMessage,"newhomejuice",0d);
                newdrawjuice = Utils.getDouble(mapMessage,"newdrawjuice",0d);
                newlongts = Utils.getLong(mapMessage,"newlongts",0L);
                newlongts = newlongts + 1000 * 60 * 60 * 3;

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
                    ml = new Moneyline(lineIdentity, newvisitorjuice, newhomejuice, newdrawjuice, newlongts);
                    if (isopener) {
                        //  LineAlertOpeners.moneyOpenerAlert(gameid, bookieid, period, isopenerS, newvisitorjuice, newhomejuice);
                        //log("***************************************"+sportname+"******************************");
                    }
                    //	log("***************************************moneyOpenerAlert******************************");
                    AppController.addMoneyline(ml);
                }


                // {gameid=207277, newdrawjuice=244.0, isopener=1, newlongts=1590351296000, bookieid=140}
            }
            else if ("LimitChange".equals(changetype)) {
                int newlimit = 0;
                String linetype;

                linetype = Utils.getString(mapMessage,"linetype");
                newlimit = Utils.getInt(mapMessage,"newlimit");
                newlongts = Utils.getLong(mapMessage,"newlongts",0L);
                newlongts = newlongts + 1000 * 60 * 60 * 3;

                // owen put this in cuz db sending us garbage timestamps!!!
                newlongts = mapMessage.getJMSTimestamp();

                if (linetype.equalsIgnoreCase("Spread")) {
                    Spreadline sl = AppController.getSpreadline(bookieid, gameid, period);
                    if (sl != null) {
                        sl.setLimit(newlimit);
                    }
                } else if (linetype.equalsIgnoreCase("Total")) {
                    Totalline tl = AppController.getTotalline(bookieid, gameid, period);
                    if (tl != null) {
                        tl.setLimit(newlimit);
                    }
                } else if (linetype.equalsIgnoreCase("Moneyline")) {
                    Moneyline ml = AppController.getMoneyline(bookieid, gameid, period);
                    if (ml != null) {
                        ml.setLimit(newlimit);
                    }
                } else if (linetype.equalsIgnoreCase("TeamTotal")) {
                    TeamTotalline ttl = AppController.getTeamTotalline(bookieid, gameid, period);
                    if (ttl != null) {
                        ttl.setLimit(newlimit);
                    }
                }
            }
//            Game game = AppController.getGame(gameid);
//            if (null == game) {
//                if (mapMessage instanceof ActiveMQMapMessage) {
//                log(new Exception("null game detected...gameid=" + gameid + ", message=" + OngoingGameMessages.convert((ActiveMQMapMessage)mapMessage)));
//                } else {
//                    log("LinesConsumer: null game detected...gameid=" + gameid);
//                }
//            } else {
//                lineIdentity.updateTable();
//            }
            updateTableWithLine(lineIdentity);

        } catch (Exception e) {
            log(mapMessage.toString());
            log(e);

        }
    }
    private void updateTableWithLine(LineIdentity lineIdentity) {
        LineEvent lineEvent = new LineEvent(lineIdentity);
        lineEvent.updateTable();
    }
}
