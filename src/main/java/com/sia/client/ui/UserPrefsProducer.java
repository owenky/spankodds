package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.SportType;
import com.sia.client.model.User;
import com.sia.client.ui.lineseeker.AlertAttrManager;

import javax.jms.*;
import java.awt.*;
import java.util.List;
import java.util.*;

import static com.sia.client.config.Utils.log;

public class UserPrefsProducer {
    User u = AppController.getUser();
    Connection connection;
    private Session session;
    private MessageProducer producer;

    public UserPrefsProducer() {
        try {
            connection = AppController.getLoggedInConnection();
            if (null != connection) {
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                Destination adminQueue = session.createQueue(AppController.getUserPrefsQueue());

                producer = session.createProducer(adminQueue);

                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


                connection.start();
            }

        } catch (Exception ex) {
            log(ex);
        }
    }

    public void sendUserPrefs(boolean logout)
    {

        if(true) // let server know you are logging out
        {
            try {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("username", u.getUsername());
                mapMessage.setString("logout", "true");
                this.producer.send(mapMessage);
            } catch (Exception ex) {
                log(ex);
            }
        }
        sendUserPrefs();

    }

    public void sendUserPrefs() {

        try {
            String colorprefs = "";
            Set<Integer> colorBookieIds = AppController.getColorBookieIds();
            for (Integer bookieid : colorBookieIds) {
                Color color = AppController.getColor(bookieid);
                int rgb = color.getRGB();
                String hex = String.format("#%06X", (0xFFFFFF & rgb));
                colorprefs = colorprefs + bookieid + "=" + hex + ",";
            }
            if (colorprefs.length() > 0) {
                colorprefs = colorprefs.substring(0, colorprefs.length() - 1);
            }
            log("SENDING COLORPREFS=" + colorprefs);
            u.setColumnColors(colorprefs);

        } catch (Exception ex) {
            log(ex);
        }
        try {
            String tabsindex = "";
            Vector<String> tabsvec = AppController.getMainTabVec();
            Enumeration<String> tabsenum = tabsvec.elements();
            while (tabsenum.hasMoreElements()) {
                String tab = "" + tabsenum.nextElement();

                tabsindex = tabsindex + tab + ",";
            }
            if (tabsindex.length() > 0) {
                tabsindex = tabsindex.substring(0, tabsindex.length() - 1);
            }
            log("SENDING TABSINDEX=" + tabsindex);
            u.setTabsIndex(tabsindex);

        } catch (Exception ex) {
            log(ex);
        }


        try {
            String customtabs = "";
            List<String> customtabsvec = AppController.getCustomTabsVec();
            for (String nextCustTab:customtabsvec) {
                String tabinfo = "" + nextCustTab;
                log("CUSTOMTAB INFO=" + tabinfo);
                customtabs = customtabs + tabinfo + "?";
            }
            if (customtabs.length() > 0) {
                customtabs = customtabs.substring(0, customtabs.length() - 1);
            }
            log("SENDING CUSTOMTABS=" + customtabs);
            u.setCustomTabs(customtabs);

        } catch (Exception ex) {
            log(ex);
        }

        try {
            String linealerts = "";

            Vector<LineAlertNode> linealertsvec = AppController.getLineAlertNodes();
            Enumeration<LineAlertNode> enumlinealerts = linealertsvec.elements();
            while (enumlinealerts.hasMoreElements()) {
                LineAlertNode lan = enumlinealerts.nextElement();

                if (lan.getName().equals("Please Select Line Alert")) {
                    continue;
                }
                linealerts = linealerts + lan.toStorageString() + "?";
            }
            if (linealerts.length() > 0) {
                linealerts = linealerts.substring(0, linealerts.length() - 1);
            }
            log("SENDING LINEALERTS=" + linealerts);
            u.setLineAlerts(linealerts);

        } catch (Exception ex) {
            log(ex);
        }


        try {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("username", u.getUsername());
            mapMessage.setString("bookiecolumnprefs", u.getBookieColumnPrefs());
            mapMessage.setString("fixedcolumnprefs", u.getFixedColumnPrefs());
            mapMessage.setString("columncolors", u.getColumnColors());
            mapMessage.setString("bookiecolumnchanges", u.getBookieColumnsChanged());

            Arrays.stream(SportType.getPreDefinedSports()).forEach(st -> {
                try {
                    mapMessage.setString(st.getPrefName(), st.getPerference());
                } catch (JMSException e) {
                    log(e);
                }
            });

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
            mapMessage.setString("openeralert", u.getOpeneralert());

            mapMessage.setString("tabsindex", u.getTabsIndex());

            mapMessage.setString("linealerts", u.getLineAlerts());

            //send line seeker alert
            mapMessage.setString(SiaConst.Serialization.LineSeekerAlert, AlertAttrManager.serializeAlertAlertAttColl());

            //send font configuration
            mapMessage.setString(SiaConst.Serialization.Font, FontConfig.serialize());
            this.producer.send(mapMessage);
        } catch (Exception ex) {
            log(ex);
        }


    }
}
