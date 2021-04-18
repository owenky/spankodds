package com.sia.client.ui;

import com.sia.client.model.User;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class UserPrefsProducer {
    public boolean loginresultback = false;
    User u = AppController.getUser();
    Connection connection;
    boolean loggedin = false;
    String delimiter = "~";
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Destination tempDest;

    public UserPrefsProducer() {
        try {
            connection = AppController.getLoggedInConnection();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination adminQueue = session.createQueue(AppController.getUserPrefsQueue());

            producer = session.createProducer(adminQueue);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


            connection.start();

        } catch (Exception ex) {
            System.out.println("problem with constructor " + ex);
        }
    }

    public void sendUserPrefs() {

        try {
            String colorprefs = "";
            Hashtable bookiecolors = AppController.getBookieColors();
            Enumeration enumcolors = bookiecolors.keys();
            while (enumcolors.hasMoreElements()) {
                String bookieid = "" + enumcolors.nextElement();
                java.awt.Color color = (java.awt.Color) bookiecolors.get(bookieid);
                int rgb = color.getRGB();
                String hex = String.format("#%06X", (0xFFFFFF & rgb));
                colorprefs = colorprefs + bookieid + "=" + hex + ",";
            }
            if (colorprefs.length() > 0) {
                colorprefs = colorprefs.substring(0, colorprefs.length() - 1);
            }
            System.out.println("SENDING COLORPREFS=" + colorprefs);
            u.setColumnColors(colorprefs);

        } catch (Exception ex) {
            System.out.println("exception preparing column colors! " + ex);
        }

        try {
            String customtabs = "";

            Vector customtabsvec = AppController.getCustomTabsVec();
            Enumeration enumtabs = customtabsvec.elements();
            while (enumtabs.hasMoreElements()) {
                String tabinfo = "" + enumtabs.nextElement();
                System.out.println("CUSTOMTAB INFO=" + tabinfo);
                customtabs = customtabs + tabinfo + "?";
            }
            if (customtabs.length() > 0) {
                customtabs = customtabs.substring(0, customtabs.length() - 1);
            }
            System.out.println("SENDING CUSTOMTABS=" + customtabs);
            u.setCustomTabs(customtabs);

        } catch (Exception ex) {
            System.out.println("exception preparing customtabs! " + ex);
        }

        try {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("username", u.getUsername());
            mapMessage.setString("bookiecolumnprefs", u.getBookieColumnPrefs());
            mapMessage.setString("fixedcolumnprefs", u.getFixedColumnPrefs());
            mapMessage.setString("columncolors", u.getColumnColors());

            mapMessage.setString("footballpref", u.getFootballPref());
            mapMessage.setString("basketballpref", u.getBasketballPref());
            mapMessage.setString("baseballpref", u.getBaseballPref());
            mapMessage.setString("hockeypref", u.getHockeyPref());
            mapMessage.setString("fightingpref", u.getFightingPref());
            mapMessage.setString("soccerpref", u.getSoccerPref());
            mapMessage.setString("autoracingpref", u.getAutoracingPref());
            mapMessage.setString("golfpref", u.getGolfPref());
            mapMessage.setString("tennispref", u.getTennisPref());
/*
        mapMessage.setString("footballopenpref",u.getUserName());
        mapMessage.setString("basketballopenpref",u.getUserName());
        mapMessage.setString("baseballopenpref",u.getUserName());
        mapMessage.setString("hockeyopenpref",u.getUserName());
        mapMessage.setString("fightingopenpref",u.getUserName());
        mapMessage.setString("socceropenpref",u.getUserName());
        mapMessage.setString("autoracingopenpref",u.getUserName());
        mapMessage.setString("golfopenpref",u.getUserName());
        mapMessage.setString("tennisopenpref",u.getUserName());
*/
            mapMessage.setString("chartfilename", u.getChartFileName());
            mapMessage.setString("chartminamtnotify", "" + u.getChartMinAmtNotify());
            mapMessage.setString("chartsecsrefresh", "" + u.getChartSecsRefresh());

            //     mapMessage.setString("lineseekers",u.getUserName());

            //    mapMessage.setString("hotkeyprefs",u.getUserName());

            mapMessage.setString("customtabs", u.getCustomTabs());

            mapMessage.setString("finalalert", u.getFinalAlert());
            mapMessage.setString("halftimealert", u.getHalftimeAlert());
            mapMessage.setString("lineupalert", u.getLineupAlert());
            mapMessage.setString("injuryalert", u.getInjuryAlert());
            mapMessage.setString("timechangealert", u.getTimechangeAlert());
            mapMessage.setString("limitchangealert", u.getLimitchangeAlert());
            mapMessage.setString("bigearnalert", u.getBigearnAlert());
            mapMessage.setString("officialalert", u.getOfficialAlert());
            mapMessage.setString("startedalert", u.getStartedAlert());

            this.producer.send(mapMessage);
        } catch (Exception ex) {
            System.out.println("EXCPETION SENDING USER PREFS MESSAGE " + ex);
        }


    }
}
