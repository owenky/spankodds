package com.sia.client.model;

import com.sia.client.config.Utils;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import static com.sia.client.config.Utils.log;

public class UrgentsConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    // private static String brokerURL = "failover:(ssl://localhost:61617)";
    //private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
    private final GameMessageProcessor gameMessageProcessor = new GameMessageProcessor("UrgentsConsumer",3000L,1500);

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
        Utils.ensureNotEdtThread();
        processMessage(message);
    }
    public void processMessage(Message message) {
        Utils.ensureNotEdtThread();
        try {
            mapMessage = (MapMessage) message;
            String changetype = mapMessage.getStringProperty("messageType");

        } catch (Exception e) {
            log(e);
        }
    }

}
