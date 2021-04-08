package com.sia.client.ui;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.MessageListener;
import javax.jms.MapMessage;
import javax.jms.Message;

import com.sia.client.ui.AppController;
import com.sia.client.ui.LinesConsumer;
import org.apache.activemq.ActiveMQConnectionFactory;

public class UrgentsConsumer implements MessageListener {

     private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
   // private static String brokerURL = "failover:(ssl://localhost:61617)";
	//private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
    public UrgentsConsumer(ActiveMQConnectionFactory factory,Connection connection,String urgentsconsumerqueue) throws JMSException {
	
    	this.factory = factory;
    	this.connection = connection;
        
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createTopic(urgentsconsumerqueue+"?consumer.retroactive=true");
		MessageConsumer messageConsumer = session.createConsumer(destination);
    	messageConsumer.setMessageListener(this);
		connection.start();
    }
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
    public static void main(String[] args) throws JMSException 
	{
		System.setProperty("javax.net.ssl.keyStore",System.getenv("ACTIVEMQ_HOME")+"\\conf\\client.ks");
		System.setProperty("javax.net.ssl.keyStorePassword","password");
		System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME")+"\\conf\\client.ts");
		AppController.createLoggedInConnection("reguser","0hbaby*(");
		//com.sia.client.ui.AppController.createLoggedInConnection("guest","spank0dds4ever");
    	LinesConsumer consumer = new LinesConsumer(AppController.getConnectionFactory(), AppController.getLoggedInConnection(),"spankoddsin.LINECHANGE");
    	
    }
	
	public void onMessage(Message message) {
		try {
			
			mapMessage = (MapMessage)message;

			String changetype = mapMessage.getStringProperty("messageType");
			


			} 
		catch (Exception e) {
			System.out.println("exception urgent message consumer "+e);
			System.out.println(mapMessage.toString());
			
		}
	}

}
