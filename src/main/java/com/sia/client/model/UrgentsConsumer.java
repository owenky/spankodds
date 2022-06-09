package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.UrgentMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.swing.*;

import static com.sia.client.config.Utils.log;

public class UrgentsConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    // private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;

    public UrgentsConsumer(ActiveMQConnectionFactory factory, Connection connection, String urgentsconsumerqueue) throws JMSException {

        this.connection = connection;

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(urgentsconsumerqueue + "?consumer.retroactive=true");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(this);
        connection.start();
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
    @Override
    public void onMessage(Message message) {
        String messageType = "";
        Utils.ensureNotEdtThread();
        try {
            mapMessage = (MapMessage) message;
            messageType = mapMessage.getStringProperty("messageType");
            if(messageType.equals("Logout"))
            {
                String username = mapMessage.getString("username");
                String loginkey = mapMessage.getString("loginkey");
                //long thislogintime = mapMessage.getLong("logintime");
                if(AppController.getUser().getUsername().equals(username)) // duplicate login?
                {
                    long userlogintime = AppController.getUser().getLoginTime();
                    long nowms = System.currentTimeMillis();
                    System.out.println(userlogintime+".."+nowms);
                    long diff = Math.abs(userlogintime - nowms);
                    //if(diff > 30000)
                    System.out.println("loginkey="+loginkey);
                    System.out.println("prevloginkey="+AppController.getUser().getLoginKey());
                    if(!AppController.getUser().getLoginKey().equals(loginkey))
                    {
                        // no longer doing this
                      //  AppController.getUserPrefsProducer().sendUserPrefs(true);



                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    Thread.sleep(30000);
                                    System.out.println("exit by timeout..");
                                    System.exit(0);

                                }
                                catch ( Exception ex )
                                {
                                    log(ex);
                                }
                            }
                        }).start();
                        JOptionPane.showMessageDialog(SpankyWindow.getFirstSpankyWindow(), "Another instance of SpankOdds has logged in. You will now be logged out.",
                                "Attention!", JOptionPane.WARNING_MESSAGE);
                        System.out.println("exit by confirmation..");
                        System.exit(0);

                    }

                }
            }
            else if(messageType.equals("Urgent")) // force show!
            {
                log("messageType:" + messageType + ", message=" + message);
                String mesg = mapMessage.getString("urgentmessage");

                addMessageToAlertVector(mesg);

                new UrgentMessage("<HTML><H2>URGENT MESSAGE</H2>" +mesg+
                        "</HTML>", 20000, 2, AppController.getMainTabPane());

            }



        } catch (Exception e)
        {
            log(e);
        }
    }
    private void addMessageToAlertVector(String str) {
        Utils.ensureNotEdtThread();
        try {
            String hrmin = AppController.getCurrentHoursMinutes();
            AppController.addAlert(hrmin,str);

        } catch (Exception e) {
            log(e);
        }
    }
}
