import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.MessageListener;
import javax.jms.MapMessage;
import javax.jms.Message;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.time.*;

public class ScoresConsumer implements MessageListener {

   private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
   // private static String brokerURL = "failover:(ssl://localhost:61617)";
	//private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
	
    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private MapMessage mapMessage;
	int gameid = 0;
    public ScoresConsumer(ActiveMQConnectionFactory factory,Connection connection,String scoresconsumerqueue) throws JMSException {
	
    	this.factory = factory;
    	this.connection = connection;
        
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createTopic(scoresconsumerqueue+"?consumer.retroactive=true");
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
		//AppController.createLoggedInConnection("guest","spank0dds4ever");
    	LinesConsumer consumer = new LinesConsumer(AppController.getConnectionFactory(),AppController.getLoggedInConnection(),"spankoddsin.LINECHANGE");
    	
    }
	
	public void onMessage(Message message) {
		try {
			
			mapMessage = (MapMessage)message;

			String changetype = mapMessage.getStringProperty("messageType");
			

			if(changetype.equals("ScoreChange"))
			{
				gameid= mapMessage.getInt("eventnumber");
				
				
				String period = "";
				String timer = "";
				String status = "";
				long gamestatuslong = 100;
				int currentvisitorscore = 0;
				String visitorscoresupplemental = "";
				long scorets =  100;
				int currenthomescore = 0;
				String homescoresupplemental = "";
				
				
								
				try {period = mapMessage.getString("period");} catch(Exception ex) {}
				try {timer = mapMessage.getString("timer");} catch(Exception ex) {}
				try {status = mapMessage.getString("status");} catch(Exception ex) {}
				try {gamestatuslong = mapMessage.getLong("gamestatusts");	} catch(Exception ex) {}			
				try {currentvisitorscore = mapMessage.getInt("currentvisitorscore");} catch(Exception ex) {}
				try {visitorscoresupplemental = mapMessage.getString("visitorscoresupplemental");} catch(Exception ex) {}
				try {scorets = mapMessage.getLong("scorets");} catch(Exception ex) {}
				try {currenthomescore = mapMessage.getInt("currenthomescore");} catch(Exception ex) {}
				try {homescoresupplemental = mapMessage.getString("homescoresupplemental");} catch(Exception ex) {}
								
				//scorets =scorets +1000*60*60*3;
				//gamestatuslong =gamestatuslong +1000*60*60*3;
			
				Game g = AppController.getGame(gameid);
				boolean refreshtabs = false;
				if(g != null)
				{
				
				//owen 8/11 moved final as first block since grand salami was causing started and final to both execute
					if(status.equalsIgnoreCase("Final") || status.equalsIgnoreCase("Win"))
					{
						if(!g.getStatus().equals(status)) // just became final
						{
							
							Sport s = AppController.getSport(g.getLeague_id());
						    
							int id=s.getParentleague_id();
							if(id == 9)
							{
								AppController.moveGameToThisHeader(g,"Soccer FINAL");
							}
							else
							{
									AppController.moveGameToThisHeader(g,"FINAL");
							}
							refreshtabs = true;
							String finalprefs = AppController.getUser().getFinalAlert();
							System.out.println("game "+gameid+"..just went final");
							String finalarr[] = finalprefs.split("\\|");
							boolean popup = false;
							boolean sound = false;
							int popupsecs = 15;
							int location = 6;
							String audiofile = "";
							String[] sports;
							boolean goodsport = false;
							
							try {popup = Boolean.parseBoolean(finalarr[0]); } catch(Exception ex)  {}
							try {sound = Boolean.parseBoolean(finalarr[1]); } catch(Exception ex)  {}
							try {popupsecs = Integer.parseInt(finalarr[2]); } catch(Exception ex)  {}
							try {location = Integer.parseInt(finalarr[3]); } catch(Exception ex)  {}
							try {audiofile = finalarr[4]; } catch(Exception ex)  {}
							try {
									sports = finalarr[5].split(",");
									for(int j=0; j < sports.length;j++)
									{
										String sportid = sports[j];
										if(sportid.equals(""+s.getLeague_id()) || sportid.equals(s.getSportname()) 
											|| sportid.equalsIgnoreCase("All Sports"))
										{
											goodsport = true;
											break;
										}
									}
								} 
							catch(Exception ex)  {}
							
							if(goodsport)
							{
									if(popup)
									{
										String hrmin=AppController.getCurrentHoursMinutes();
										String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"-"+currentvisitorscore+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam()+"-"+currenthomescore;

										String popalertname="Alert at:"+hrmin+"\nFINAL :"+s.getSportname()+","+s.getLeaguename()+","+teaminfo;
										
										AppController.alertsVector.addElement(popalertname);
										
										new UrgentMessage("<HTML><H1>FINAL</H1><FONT COLOR=BLUE>" +
										s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
										
										"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD><TD>"+currentvisitorscore+"</TR>"+
										"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD><TD>"+currenthomescore+"</TR>"+
										"</TABLE></FONT></HTML>",popupsecs*1000,location,AppController.getMainTabPane());							
									}
									
									if(sound)
									{
										if(audiofile.equals(""))
										{
											//playSound("final.wav");
											new SoundPlayer("final.wav");
										}
										else
										{
											//playSound(audiofile);
											new SoundPlayer(audiofile);
										}
									}	


							}
							refreshtabs = true;

							
						}
					}					
				
				
					else if(g.getStatus() == null || g.getStatus().equalsIgnoreCase("NULL") || g.getStatus().equals(""))
						{
							// game hasn't started
							if(!g.getStatus().equals(status))
							{
								//status change game just started
								Sport s = AppController.getSport(g.getLeague_id());
								System.out.println("game "+gameid+"..just started");
								int id=s.getParentleague_id();
							if(id == 9)
							{
								AppController.moveGameToThisHeader(g,"Soccer In Progress");
							}
							else
							{
							    AppController.moveGameToThisHeader(g,"In Progress");
							}
						
					
								refreshtabs = true;
								
								
								
								
								
								
								String prefs = AppController.getUser().getStartedAlert();
								String arr[] = prefs.split("\\|");
								boolean popup = false;
								boolean sound = false;
								int popupsecs = 15;
								int location = 6;
								String audiofile = "";
								String[] sports;
								boolean goodsport = false;
								
								try {popup = Boolean.parseBoolean(arr[0]); } catch(Exception ex)  {}
								try {sound = Boolean.parseBoolean(arr[1]); } catch(Exception ex)  {}
								try {popupsecs = Integer.parseInt(arr[2]); } catch(Exception ex)  {}
								try {location = Integer.parseInt(arr[3]); } catch(Exception ex)  {}
								try {audiofile = arr[4]; } catch(Exception ex)  {}
								try {
										sports = arr[5].split(",");
										for(int j=0; j < sports.length;j++)
										{
											String sportid = sports[j];
											if(sportid.equals(""+s.getLeague_id()) || sportid.equals(s.getSportname()) || sportid.equalsIgnoreCase("All Sports"))
											{
												goodsport = true;
												break;
											}
										}
									} 
								catch(Exception ex)  {}
								
								if(goodsport)
								{
										if(popup)
										{
											String hrmin=AppController.getCurrentHoursMinutes();
										String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"-"+currentvisitorscore+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam()+"-"+currenthomescore;

										String popalertname="Alert at:"+hrmin+"\nSTARTED :"+s.getSportname()+","+s.getLeaguename()+","+teaminfo;
										
										AppController.alertsVector.addElement(popalertname);
										
											new UrgentMessage("<HTML><H1>STARTED</H1><FONT COLOR=BLUE>" +
											s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
											
											"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD></TR>"+
											"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD></TR>"+
											"</TABLE></FONT></HTML>",popupsecs*1000,location,AppController.getMainTabPane());							
										}
										
										if(sound)
										{
											if(audiofile.equals(""))
											{
												//playSound("started.wav");
												new SoundPlayer("started.wav");
											}
											else
											{
												//playSound(audiofile);
												new SoundPlayer(audiofile);
											}
										}	

								}								
								
								
							}
						}	
					else if(status.equalsIgnoreCase("Time"))	// its halftime
					{
						if(!g.getStatus().equals(status)) // just became halftime
						{
							System.out.println("game "+gameid+"..just went to halftime");
							refreshtabs = true;
							Sport s = AppController.getSport(g.getLeague_id());
							int id=s.getParentleague_id();
							if(id == 9)
							{
								AppController.moveGameToThisHeader(g,"Soccer Halftime");
							}
							else
							{
							   AppController.moveGameToThisHeader(g,"Halftime");
							}
							refreshtabs = true;
							
							String prefs = AppController.getUser().getHalftimeAlert();
							String arr[] = prefs.split("\\|");
							boolean popup = false;
							boolean sound = false;
							int popupsecs = 15;
							int location = 6;
							String audiofile = "";
							String[] sports;
							boolean goodsport = false;
							
							try {popup = Boolean.parseBoolean(arr[0]); } catch(Exception ex)  {}
							try {sound = Boolean.parseBoolean(arr[1]); } catch(Exception ex)  {}
							try {popupsecs = Integer.parseInt(arr[2]); } catch(Exception ex)  {}
							try {location = Integer.parseInt(arr[3]); } catch(Exception ex)  {}
							try {audiofile = arr[4]; } catch(Exception ex)  {}
							try {
									sports = arr[5].split(",");
									for(int j=0; j < sports.length;j++)
									{
										String sportid = sports[j];
										if(sportid.equals(""+s.getLeague_id()) || sportid.equals(s.getSportname()) || sportid.equalsIgnoreCase("All Sports"))
										{
											goodsport = true;
											break;
										}
									}
								} 
							catch(Exception ex)  {}
							
							if(goodsport)
							{
									if(popup)
									{
										String hrmin=AppController.getCurrentHoursMinutes();
										String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"-"+currentvisitorscore+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam()+"-"+currenthomescore;

										String popalertname="Alert at:"+hrmin+"\nHALFTIME :"+s.getSportname()+","+s.getLeaguename()+","+teaminfo;
										AppController.alertsVector.addElement(popalertname);
										
										
										new UrgentMessage("<HTML><H1>HALFTIME</H1><FONT COLOR=BLUE>" +
										s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
										
										"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD><TD>"+currentvisitorscore+"</TR>"+
										"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD><TD>"+currenthomescore+"</TR>"+
										"</TABLE></FONT></HTML>",popupsecs*1000,location,AppController.getMainTabPane());							
									}
									
									if(sound)
									{
										if(audiofile.equals(""))
										{
											//playSound("halftime.wav");
											new SoundPlayer("halftime.wav");
										}
										else
										{
											//playSound(audiofile);
											new SoundPlayer(audiofile);
										}
									}	


							}
							

							
						}
					}
					else if(g.getStatus().equalsIgnoreCase("Time"))	// its already halftime // instead may make index of a :?
					{
						if(!g.getStatus().equals(status)) // halftime just finished
						{
							System.out.println("game "+gameid+"..just went out of halftime");
							refreshtabs = true;
							Sport s = AppController.getSport(g.getLeague_id());
							int id=s.getParentleague_id();
							if(id == 9)
							{
								AppController.moveGameToThisHeader(g,"Soccer In Progress");
							}
							else
							{
							  	AppController.moveGameToThisHeader(g,"In Progress");
							}
						
							
						}
					}
				
					
					g.updateScore(period,timer,status,new java.sql.Timestamp(gamestatuslong),currentvisitorscore,visitorscoresupplemental,
							 new java.sql.Timestamp(scorets),currenthomescore,homescoresupplemental);
							 
					if(refreshtabs)			 
					{
						
					}
					else
					{

					}	
				}
				else
				{
				g = new Game();	
				g.updateScore(period,timer,status,new java.sql.Timestamp(gamestatuslong),currentvisitorscore,visitorscoresupplemental,
							 new java.sql.Timestamp(scorets),currenthomescore,homescoresupplemental);				
				AppController.addGame(g);
				}
				
			}

			//AppController.fireAllTableDataChanged();
			

			} 
			catch (Exception e)
			{
				System.out.println("exception scores consumer "+e);
				System.out.println(mapMessage.toString());
				
			}
			try
			{
				AppController.fireAllTableDataChanged(""+gameid);
			}
			catch(Exception ex)
			{
				//System.out.println("exception scoressconsumer firetabledatachanged "+ex);
			}			
	}

public void playSound(String file) {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch(Exception ex) {
        System.out.println("Error with playing sound.");
        ex.printStackTrace();
    }
}


}
