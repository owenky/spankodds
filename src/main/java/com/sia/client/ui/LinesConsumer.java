package com.sia.client.ui;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.MessageListener;
import javax.jms.MapMessage;
import javax.jms.Message;

import org.apache.activemq.ActiveMQConnectionFactory;

public class LinesConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    //private static String brokerURL = "failover:(ssl://localhost:61617)";
	//private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
	int gameid = 0;
    public LinesConsumer(ActiveMQConnectionFactory factory,Connection connection,String linesconsumerqueue) throws JMSException {
	
    	this.factory = factory;
    	this.connection = connection;
        
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createTopic(linesconsumerqueue+"?consumer.retroactive=true");
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
    	LinesConsumer consumer = new LinesConsumer(AppController.getConnectionFactory(),AppController.getLoggedInConnection(),"spankoddsin.LINECHANGE");
    	
    }
	
	public void onMessage(Message message) {
		try {
			
			mapMessage = (MapMessage)message;
			//String sportname=mapMessage.getInt("sportid")+"";
			gameid = mapMessage.getInt("gameid");
			int bookieid = mapMessage.getInt("bookieid");
			int period = mapMessage.getInt("period");
			String isopenerS = mapMessage.getString("isopener");
			String changetype = mapMessage.getStringProperty("messageType");
			long newlongts = 0;

			
			boolean isopener = false;
			if(isopenerS.equals("1"))
			{
				isopener = true;
			}
			
			
//			time deff claiclation
				

		//	if(gameid >=951 && gameid <= 993 && bookieid == 17 && period==0 && !isopener)
		//	{
			//	System.out.println("received PINNY line change=\n"+mapMessage.toString());
		//	}			
			
			if(changetype.equals("LineChangeSpread"))
			{
			double newvisitorspread = 0;
			double newhomespread = 0;
			double newvisitorjuice = 0;
			double newhomejuice =0;
			try {newvisitorspread = mapMessage.getDouble("newvisitorspread");} catch(Exception ex) {}
			try {newhomespread = mapMessage.getDouble("newhomespread");} catch(Exception ex) {}
			try {newvisitorjuice = mapMessage.getDouble("newvisitorjuice");} catch(Exception ex) {}
			try {newhomejuice = mapMessage.getDouble("newhomejuice");} catch(Exception ex) {}
			try
			{
			newlongts = mapMessage.getLong("newlongts");
			newlongts =newlongts +1000*60*60*3;
			}
			catch(Exception ex) {}
			
			// owen put this in cuz db sending us garbage timestamps!!!
			newlongts = message.getJMSTimestamp();
			
			Spreadline sl = AppController.getSpreadline(bookieid,gameid,period);
				if(sl != null)
				{
				sl.recordMove(newvisitorspread,newvisitorjuice,newhomespread,newhomejuice,new java.sql.Timestamp(newlongts),isopener);
				}
				else
				{
				sl = new Spreadline(gameid,bookieid,newvisitorspread,newvisitorjuice,newhomespread,newhomejuice,new java.sql.Timestamp(newlongts),period);
				//System.out.println("***************************************spreadxyzabc******************************");
				if(isopener)
				{
				LineAlertOpeners.spreadOpenerAlert(gameid,bookieid,period,isopenerS,newvisitorspread,newvisitorjuice,newhomespread,newhomejuice);
				//System.out.println("***************************************"+sportname+"******************************");
				}
				
				AppController.addSpreadline(sl);
				}
			}
			else if(changetype.equals("LineChangeTotal"))
			{
				
			
			double newover =0;
			double newunder = 0;
			double newoverjuice =0;
			double newunderjuice = 0;				
			try {newover = mapMessage.getDouble("newover");} catch(Exception ex) {}
			try {newunder = mapMessage.getDouble("newunder");} catch(Exception ex) {}
			try {newoverjuice = mapMessage.getDouble("newoverjuice");} catch(Exception ex) {}
			try {newunderjuice = mapMessage.getDouble("newunderjuice");} catch(Exception ex) {}
			try
			{
			newlongts = mapMessage.getLong("newlongts");
			newlongts =newlongts +1000*60*60*3;
			}
			catch(Exception ex) {}
			
			// owen put this in cuz db sending us garbage timestamps!!!
			newlongts = message.getJMSTimestamp();

			
			Totalline tl = AppController.getTotalline(bookieid,gameid,period);
				if(tl != null)
				{
					tl.recordMove(newover,newoverjuice,newunder,newunderjuice,new java.sql.Timestamp(newlongts),isopener);				
				}
				else
				{
					tl = new Totalline(gameid,bookieid,newover,newoverjuice,newunder,newunderjuice,new java.sql.Timestamp(newlongts),period);	
					if(isopener)
					{
					LineAlertOpeners.totalOpenerAlert(gameid,bookieid,period,isopenerS,newover,newoverjuice,newunder,newunderjuice);
					//System.out.println("***************************************"+sportname+"******************************");
					}
				//	System.out.println("***************************************totalOpenerAlert******************************");
					AppController.addTotalline(tl);
				}
//{gameid=211013, newunderjuice=-115.0, newunder=2.5, newlongts=1590351272000, bookieid=49, newoverjuice=-115.0, isopener=1, newover=2.5}
				
			}
			else if(changetype.equals("LineChangeTeamTotal"))
			{
			double newvisitorover =0;
			double newvisitorunder = 0;
			double newvisitoroverjuice =0;
			double newvisitorunderjuice = 0;
			
			double newhomeover =0;
			double newhomeunder = 0;
			double newhomeoverjuice =0;
			double newhomeunderjuice = 0;			
			try {newvisitorover = mapMessage.getDouble("newvisitorover");} catch(Exception ex) {}
			try {newvisitorunder = mapMessage.getDouble("newvisitorunder");} catch(Exception ex) {}
			try {newvisitoroverjuice = mapMessage.getDouble("newvisitoroverjuice");} catch(Exception ex) {}
			try {newvisitorunderjuice = mapMessage.getDouble("newvisitorunderjuice");} catch(Exception ex) {}
				
			try {newhomeover = mapMessage.getDouble("newhomeover");} catch(Exception ex) {}
			try {newhomeunder = mapMessage.getDouble("newhomeunder");} catch(Exception ex) {}
			try {newhomeoverjuice = mapMessage.getDouble("newhomeoverjuice");} catch(Exception ex) {}
			try {newhomeunderjuice = mapMessage.getDouble("newhomeunderjuice");} catch(Exception ex) {}			
			try
			{
			newlongts = mapMessage.getLong("newlongts");
			newlongts =newlongts +1000*60*60*3;
			}
			catch(Exception ex) {}
			
			// owen put this in cuz db sending us garbage timestamps!!!
			newlongts = message.getJMSTimestamp();

			
			TeamTotalline ttl = AppController.getTeamTotalline(bookieid,gameid,period);
				if(ttl != null)
				{
 			
					ttl.recordMove(newvisitorover,newvisitoroverjuice,newvisitorunder,newvisitorunderjuice,
					newhomeover,newhomeoverjuice,newhomeunder,newhomeunderjuice,
					new java.sql.Timestamp(newlongts),isopener);				
				}
				else
				{
					ttl = new TeamTotalline(gameid,bookieid,newvisitorover,newvisitoroverjuice,newvisitorunder,newvisitorunderjuice,
					newhomeover,newhomeoverjuice,newhomeunder,newhomeunderjuice,new java.sql.Timestamp(newlongts),period);	
					if(isopener)
					{
					LineAlertOpeners.teamTotalOpenerAlert(gameid,bookieid,period,isopenerS,newvisitorover,newvisitoroverjuice,newvisitorunder,newvisitorunderjuice);
					//System.out.println("***************************************"+sportname+"******************************");
					}
				//	System.out.println("***************************************teamTotalOpenerAlert******************************");
					AppController.addTeamTotalline(ttl);
				}
//{gameid=211013, newunderjuice=-115.0, newunder=2.5, newlongts=1590351272000, bookieid=49, newoverjuice=-115.0, isopener=1, newover=2.5}
				
			}			
			else if(changetype.equals("LineChangeMoney"))
			{
			double newvisitorjuice = 0;
			double newhomejuice = 0;
			double newdrawjuice = 0;				
			try{ newvisitorjuice = mapMessage.getDouble("newvisitorjuice");} catch(Exception ex) {}
			try{ newhomejuice = mapMessage.getDouble("newhomejuice");} catch(Exception ex) {}
			try{ newdrawjuice = mapMessage.getDouble("newdrawjuice");} catch(Exception ex) { newdrawjuice = 0;}
			try
			{
			newlongts = mapMessage.getLong("newlongts");
			newlongts =newlongts +1000*60*60*3;
			}
			catch(Exception ex) {}
	
			// owen put this in cuz db sending us garbage timestamps!!!
			newlongts = message.getJMSTimestamp();			
			Moneyline ml = AppController.getMoneyline(bookieid,gameid,period);
			
			// owen hack this since draw change comes in seperately
			// this is a draw move only so use old visit and home lines 
			if(period == 0 && bookieid == 17 && gameid >= 200000 && gameid <= 300000)
				{
				//System.out.println("MONEY..gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
				}
			 if(newdrawjuice == 0 && ml != null && newvisitorjuice != 0 && newhomejuice != 0 )
			{
				newdrawjuice = ml.getCurrentdrawjuice();
				if(period == 0 && bookieid == 17)
				{
				//System.out.println("gameid="+gameid+"..bookieid="+bookieid+"...cur="+newdrawjuice+"..prior="+ml.getPriordrawjuice());
				}
			}
			else if(newdrawjuice != 0 && ml != null && newvisitorjuice == 0 && newhomejuice == 0)
			{
				//newvisitorjuice = ml.getCurrentvisitjuice();
				//newhomejuice = ml.getCurrenthomejuice();
			}
			
			
				if(ml != null)
				{
					ml.recordMove(newvisitorjuice,newhomejuice,newdrawjuice,new java.sql.Timestamp(newlongts),isopener);
				}
				else
				{
					ml = new Moneyline(gameid,bookieid,newvisitorjuice,newhomejuice,newdrawjuice,new java.sql.Timestamp(newlongts),period);	
					if(isopener)
					{
					LineAlertOpeners.moneyOpenerAlert(gameid,bookieid,period,isopenerS,newvisitorjuice,newhomejuice);
					//System.out.println("***************************************"+sportname+"******************************");
					}
				//	System.out.println("***************************************moneyOpenerAlert******************************");
					AppController.addMoneyline(ml);
				}			
			
				
			// {gameid=207277, newdrawjuice=244.0, isopener=1, newlongts=1590351296000, bookieid=140} 	
			}			
			//com.sia.client.ui.AppController.getLinesTableData().fireTableDataChanged();
			
			

			} 
		catch (Exception e) {
			System.out.println("exception lines consumer "+e);
			System.out.println(mapMessage.toString());
			
		}
		try
		{
			AppController.fireAllTableDataChanged(""+gameid);
		}
		catch(Exception ex)
		{
		//	System.out.println("exception linesconsumer firetabledatachanged "+ex);
		}
	}

}