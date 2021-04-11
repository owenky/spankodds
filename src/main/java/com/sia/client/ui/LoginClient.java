package com.sia.client.ui;

import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.User;

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

// bam 79106... 79092
// res 9767...9764
// hrk 1086...
// gnu 74409..74356
// fan 66549
public class LoginClient implements MessageListener {

	//private String brokerUrl = "failover:(ssl://localhost:61617)";
	//private String requestQueue = "spankodds.LOGIN";
	
	Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	
	private Destination tempDest;
	
	boolean loggedin = false;
	public boolean loginresultback = false;
	String delimiter = "~";
	
	public LoginClient()
	{
		try
		{
		AppController.addBookie(new Bookie(990,"Details","Dtals","",""));
		AppController.addBookie(new Bookie(991,"Time","Time","",""));
		AppController.addBookie(new Bookie(992,"Gm#","Gm#","",""));
		AppController.addBookie(new Bookie(993,"Team","Team","",""));		
		AppController.addBookie(new Bookie(994,"Chart","Chart","",""));
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
		
		}
		catch(Exception ex)
		{
			
			System.out.println("propblem with constructor "+ex);
		}
	}

	public void setloggedin(boolean loggedin)
	{
		this.loggedin = loggedin;
		
	}
	public boolean isloggedin()
	{
		return loggedin;
		
	}	
	
	public void stop() throws JMSException {
		producer.close();
		consumer.close();
		session.close();
		connection.close(); 
	}
	
	//called by login button
	public void login(String username,String password) throws JMSException {
		System.out.println("Trying to login with: " + username+ "src/main" +password);

		/*
		TextMessage txtMessage = session.createTextMessage();
		String request = username+"*^&%#)("+password; // one sec maybe i send an object here instead?!?!?
		txtMessage.setText(request);

		txtMessage.setJMSReplyTo(tempDest);

		String correlationId = UUID.randomUUID().toString();
		txtMessage.setJMSCorrelationID(correlationId);
		this.producer.send(txtMessage);
		*/
		MapMessage mapMessage = session.createMapMessage();
		mapMessage.setString("username",username);
		mapMessage.setString("password",password);
		
		mapMessage.setJMSReplyTo(tempDest);

		String correlationId = UUID.randomUUID().toString();
		mapMessage.setJMSCorrelationID(correlationId);
		this.producer.send(mapMessage);
	}

	public void onMessage(Message message) {
		try {
					  String messageType = message.getStringProperty("messageType");
					  //System.out.println("Messagetype is: "+message.getStringProperty("messageType"));
					  
					  if(messageType.equals("loginResult"))
					  {
						   System.out.println("login back start "+new java.util.Date());
						  TextMessage response = (TextMessage) message;
						  String text = response.getText();
						  System.out.println("TEXTRECEIVED="+text);
						  if(text.equals(""))
						  {
							  this.setloggedin(false);
							   setLoginResultBack(true);
							  System.out.println("LOGIN NO GOOD");
						  }
						  else
						  {
							 this.setloggedin(true);
							 String array[] = text.split(delimiter);
						  
							 User user = new User(array[0],array[1],array[2],array[3],array[4],array[5],array[6],array[7],array[8],array[9],array[10],array[11],Boolean.parseBoolean(array[12]),array[13],array[14],
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
												  Integer.parseInt(array[37])
												  );
						     System.out.println("ABOUT TO CALL SETUSER!!!");
						     AppController.setUser(user);							 
							 
						  }

						 
						
						 
						
					  }
					  else if(messageType.equals("QueueCredentials"))
					  {
						  	  setLoginResultBack(true);
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
						  
						  AppController.createLoggedInConnection(array[0],array[1]);
						  
						  System.out.println("just created loginconnection!"); 

						
					  }					  
					  else if(messageType.equals("Bookie"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
						  
						  int bookieid = Integer.parseInt(array[0]);
						  Bookie bookie = new Bookie(bookieid,array[1],array[2],array[3],array[4]);
						  Bookie openerbookie = new Bookie(1000+bookieid,array[1]+"-OPEN",array[2]+"-O",array[3],array[4]);
						  //System.out.println(bookie);
						  AppController.addBookie(bookie);
						 // if(bookieid== 204)
						 // {
							 AppController.addBookie(openerbookie);
						//  } 
						  
						  
						  
						  
					  }
					  else if(messageType.equals("Sport"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
					//Sport
				// String leaguename,String leagueabbr,String sportname,String sportabbr,boolean moneylinedefault)
				

				 
				 
						  Sport sport = new Sport(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),
						  Integer.parseInt(array[3]),Integer.parseInt(array[4]),Double.parseDouble(array[5]),
						  Double.parseDouble(array[6]),array[7],array[8],array[9],array[10],Boolean.parseBoolean(array[11]));
						  AppController.addSport(sport);
						  
					  }					  
					  else if(messageType.equals("Game"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
						  //System.out.println("gametext="+text);
						  // here in 1st entry i made game_id visitorgamenumber 
						  Game game = new Game(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),Integer.parseInt(array[3]),
												Integer.parseInt(array[4]),new java.sql.Date(Long.parseLong(array[5])),new java.sql.Time(Long.parseLong(array[6])),
												array[7],array[8],array[9],array[10],Integer.parseInt(array[11]),Integer.parseInt(array[12]),Integer.parseInt(array[13]),
												Integer.parseInt(array[14]),Integer.parseInt(array[15]),array[16],array[17],array[18],array[19],array[20],array[21],
												array[22],array[23],Integer.parseInt(array[24]),array[25],
												Boolean.parseBoolean(array[26]), 
												Boolean.parseBoolean(array[27]),
												Boolean.parseBoolean(array[28]),
												Boolean.parseBoolean(array[29]),
												Boolean.parseBoolean(array[30]),
												Boolean.parseBoolean(array[31]),
												Boolean.parseBoolean(array[32]),
												Boolean.parseBoolean(array[33]),
												Boolean.parseBoolean(array[34]),
												array[35],
												array[36],
												array[37],
												array[38],
												Integer.parseInt(array[39]),
												Integer.parseInt(array[40]),
												array[41],
												array[42],
												array[43],
												array[44],
												array[45],
												Boolean.parseBoolean(array[46]),
												Boolean.parseBoolean(array[47]),
												Boolean.parseBoolean(array[48]),
												Boolean.parseBoolean(array[49]),
												array[50],
												new Timestamp(Long.parseLong(array[51])),
												new Timestamp(Long.parseLong(array[52])) );
												
												

											



												
						  String pattern = "MM-dd hh:mm";
						  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
						 // System.out.println(simpleDateFormat.format(game.getGamedateandtime()));
						  AppController.addGame(game);	
						 			  
						  
					  }
					  else if(messageType.equals("Spreadline"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
					
						  Spreadline line = new Spreadline(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Double.parseDouble(array[2]),
											Double.parseDouble(array[3]),Double.parseDouble(array[4]),Double.parseDouble(array[5]),
						  new Timestamp(Long.parseLong(array[6])),
						  Double.parseDouble(array[7]),Double.parseDouble(array[8]),Double.parseDouble(array[9]),Double.parseDouble(array[10]),new Timestamp(Long.parseLong(array[11])),
						  Double.parseDouble(array[12]),Double.parseDouble(array[13]),Double.parseDouble(array[14]),Double.parseDouble(array[15]),new Timestamp(Long.parseLong(array[16])),
						  Integer.parseInt(array[17]));
	
						int period = Integer.parseInt(array[17]);
						
						 AppController.addSpreadline(line);						  
						   //setLoginResultBack(true);
							//owen took out 7/11/20 and moved back to loginconnection 
						  
					  }
					  else if(messageType.equals("Totalline"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
					
						  Totalline line = new Totalline(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Double.parseDouble(array[2]),
											Double.parseDouble(array[3]),Double.parseDouble(array[4]),Double.parseDouble(array[5]),
						  new Timestamp(Long.parseLong(array[6])),Double.parseDouble(array[7]),Double.parseDouble(array[8]),Double.parseDouble(array[9]),
						  Double.parseDouble(array[10]),new Timestamp(Long.parseLong(array[11])),Double.parseDouble(array[12]),Double.parseDouble(array[13]),
						  Double.parseDouble(array[14]),Double.parseDouble(array[15]),new Timestamp(Long.parseLong(array[16])),
						  
						  						  Integer.parseInt(array[17]));
						int period = Integer.parseInt(array[17]);
						
						 AppController.addTotalline(line);					  
						
						 				  						  
						  
					  }
					  else if(messageType.equals("TeamTotalline"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
					
			
					
						  TeamTotalline line = new TeamTotalline(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Double.parseDouble(array[2]),
											Double.parseDouble(array[3]),Double.parseDouble(array[4]),Double.parseDouble(array[5]),
											Double.parseDouble(array[6]),
											Double.parseDouble(array[7]),
											Double.parseDouble(array[8]),
											Double.parseDouble(array[9]),
										
						  new Timestamp(Long.parseLong(array[10])),
						  
						  Double.parseDouble(array[11]),
						  Double.parseDouble(array[12]),
						  Double.parseDouble(array[13]),
						  Double.parseDouble(array[14]),
						  Double.parseDouble(array[15]),
						  Double.parseDouble(array[16]),
						  Double.parseDouble(array[17]),
						  Double.parseDouble(array[18]),
						  
						  new Timestamp(Long.parseLong(array[19])),
						  						  
						  Double.parseDouble(array[20]),
						  Double.parseDouble(array[21]),
						  Double.parseDouble(array[22]),
						  Double.parseDouble(array[23]),
						  Double.parseDouble(array[24]),
						  Double.parseDouble(array[25]),
						  Double.parseDouble(array[26]),
						  Double.parseDouble(array[27]),
						  
						  new Timestamp(Long.parseLong(array[28])),
						  Integer.parseInt(array[29]));
						int period = Integer.parseInt(array[29]);
						
						AppController.addTeamTotalline(line);					  
						

						  		  						  
						  
					  }					  
					  else if(messageType.equals("Moneyline"))
					  {
						  TextMessage textMessage = (TextMessage)message;
						  String text = textMessage.getText();
						  String array[] = text.split(delimiter);
						  
						  Moneyline line =  new Moneyline(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Double.parseDouble(array[2]),
											Double.parseDouble(array[3]),Double.parseDouble(array[4]),new Timestamp(Long.parseLong(array[5])),
											Double.parseDouble(array[6]),Double.parseDouble(array[7]),Double.parseDouble(array[8]),
											new Timestamp(Long.parseLong(array[9])),Double.parseDouble(array[10]),Double.parseDouble(array[11]),
											Double.parseDouble(array[12]),new Timestamp(Long.parseLong(array[13])),
											
						Integer.parseInt(array[14]));
						int period = Integer.parseInt(array[14]);
						
						AppController.addMoneyline(line);					  
						
															  
						  
						  				
						  
					  }
					  else if(messageType.equals("Unsubscribe"))
					  {

						  AppController.createGamesConsumer();
						  AppController.createScoresConsumer();
						  AppController.createUrgentsConsumer();
						  AppController.createLinesConsumer();
						  AppController.createChartChecker();
						  System.out.println("Unsubscribing....."+new java.util.Date());
						  consumer.close();
						  
					  }
			          
		    } 
		catch (JMSException e) 
		{
			System.out.println("message prcoessing exception "+e);
		}
		catch (Exception ex) 
		{
			System.out.println("general message prcessing exception "+ex+ "src/main" +message);

		}
	}
	
	public boolean getLoginResultBack()
	{
		return loginresultback;
	}
	public void setLoginResultBack(boolean b)
	{
		loginresultback = b;
	}
	public static void main(String[] args) throws Exception 
	{
		System.setProperty("javax.net.ssl.keyStore",System.getenv("ACTIVEMQ_HOME")+"\\conf\\client.ks");
		System.setProperty("javax.net.ssl.keyStorePassword","password");
		System.setProperty("javax.net.ssl.trustStore", System.getenv("ACTIVEMQ_HOME")+"\\conf\\client.ts");
		//String servername = "failover:(ssl://localhost:61617)";
		//String servername = "failover:(ssl://71.172.25.164:61617)";
		String servername = "failover:(tcp/71.172.25.164:61616)";
		String un = "guest";
		String pw = "spank0dds4ever";
		String queuename = "spankodds.LOGIN";
		LoginClient client = new LoginClient();
				
		System.out.println(new java.util.Date());
		client.login(args[0],args[1]);
		
		while(!client.getLoginResultBack()) //wait for login
		{
			//System.out.println(client.loginresultback+" "+new java.util.Date());
			System.out.print("");
		}
		
		System.out.println("LOGIN="+client.isloggedin());
		System.out.println(new java.util.Date());
		
	}

}
