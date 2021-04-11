package com.sia.client.ui;

import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;

public class GamesConsumer implements MessageListener {

    private static String brokerURL = "failover:(tcp://sof300732.com:61616)";
    //private static String brokerURL = "failover:(ssl://localhost:61617)";
	//private static String brokerURL = "failover:(ssl://71.172.25.164:61617)";
    private static ActiveMQConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private TextMessage textMessage;
    public GamesConsumer(ActiveMQConnectionFactory factory,Connection connection,String gamesconsumerqueue) throws JMSException {
	
    	this.factory = factory;
    	this.connection = connection;
        
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createTopic(gamesconsumerqueue+"?consumer.retroactive=true");
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
    	GamesConsumer consumer = new GamesConsumer(AppController.getConnectionFactory(),AppController.getLoggedInConnection(),"spankoddsin.GAMECHANGE");
    	
    }
	
	public void onMessage(Message message) 
	{
		try {
			boolean repaint = true;
			String leagueid = "";
			textMessage = (TextMessage)message;
			String messagetype = textMessage.getStringProperty("messageType");
			String repaintstr = textMessage.getStringProperty("repaint");
			leagueid = textMessage.getStringProperty("leagueid");
			String oldvpitcher = "";
			String oldhpitcher = "";
			Time oldgametime = null;
		
			if(repaintstr != null && repaintstr.equals("false"))
			{
				repaint = false;
			}
			if(leagueid == null)
			{
				leagueid = "";
			}
			
			if(messagetype.equals("NEWORUPDATE"))
			{
			
					String data = textMessage.getText();
					System.out.println("new game! "+data);
					String items[] = data.split("~");
					int x = 0;
//"483~483~484~1599980400000~1600016400000~TEST1~TEST2~PHI~WAS~1~0~0~483~1~false~false~false~Eagles~Redskins~Philadelphia~Washington~0~0~~~false~false";					

// need to merge both of these
//loginclient
/*
						  Game game = new Game(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),Integer.parseInt(array[3]),
												Integer.parseInt(array[4]),new java.sql.Date(Long.parseLong(array[5])),new java.sql.Time(Long.parseLong(array[6])),
												array[7],array[8],array[9],array[10],Integer.parseInt(array[11]),Integer.parseInt(array[12]),Integer.parseInt(array[13]),
												Integer.parseInt(array[14]),Integer.parseInt(array[15]),array[16],array[17],array[18],array[19],array[20],array[21],
												array[22],array[23]);
						Game g = new Game(game_id,visitorgamenumber,homegamenumber,visitoraltgamenumber,homealtgamenumber,gamedateandtime,
										  visitorteam,hometeam,shortvisitorteam,shorthometeam,sport,league,currentvisitorscore,
										  currenthomescore,finalvisitorscore,finalhomescore,injurynotes,refsumpires,lineups,weather,
										  location,status,period,specialnotes);												
												
						int game_id = rs.getInt("game_id");
							
						int visitorgamenumber = rs.getInt("visitorgamenumber");
						int homegamenumber = rs.getInt("homegamenumber");
						int visitoraltgamenumber = rs.getInt("visitoraltgamenumber");
						int homealtgamenumber = rs.getInt("homealtgamenumber");
						java.sql.Date gamedate = rs.getDate("gamedate");
						java.sql.Time gametime = rs.getTime("gametime");
						
						
						
						String visitorteam = rs.getString("visitorteam");
						String hometeam = rs.getString("hometeam");
						String shortvisitorteam = rs.getString("shortvisitorteam");
						String shorthometeam = rs.getString("shorthometeam");
						int league_id = rs.getInt("league_id");
						int currentvisitorscore = rs.getInt("currentvisitorscore");
						int currenthomescore = rs.getInt("currenthomescore");
						int finalvisitorscore = rs.getInt("finalvisitorscore");
						int finalhomescore = rs.getInt("finalhomescore");
						Blob injurynotesblob = rs.getBlob("injurynotes");
						Blob refsumpiresblob = rs.getBlob("refsumpires");
						Blob lineupsblob = rs.getBlob("lineups");
						Blob weatherblob = rs.getBlob("weather");
						Blob specialnotesblob = rs.getBlob("specialnotes");
						
						String location = rs.getString("location");
						String status = rs.getString("status");
						String period = rs.getString("period");
						
						if(location == null) location = "";
						if(period == null) period = "";
						String injurynotes = "";
						String refsumpires = "";
						String lineups = "";
						String weather = "";
						String specialnotes ="A";



						try {injurynotes = new String(injurynotesblob.getBytes(1, (int) injurynotesblob.length()));} catch(Exception ex) {}
						try {refsumpires = new String(refsumpiresblob.getBytes(1, (int) refsumpiresblob.length()));} catch(Exception ex) {}
						try {lineups = new String(lineupsblob.getBytes(1, (int) lineupsblob.length()));} catch(Exception ex) {}
						try {weather = new String(weatherblob.getBytes(1, (int) weatherblob.length()));} catch(Exception ex) {}
						try {specialnotes = new String(specialnotesblob.getBytes(1, (int) specialnotesblob.length()));} catch(Exception ex) {}
						long gamedatelong = gamedate.getTime();
						long gametimelong = gametime.getTime();

						text = ""+game_id+delimiter+visitorgamenumber+delimiter+homegamenumber+delimiter+visitoraltgamenumber+delimiter+homealtgamenumber+
						delimiter+gamedatelong+delimiter+gametimelong+delimiter+visitorteam+delimiter+hometeam+delimiter+shortvisitorteam+delimiter+shorthometeam+
						delimiter+league_id+delimiter+currentvisitorscore+delimiter+currenthomescore+delimiter+finalvisitorscore+
						delimiter+finalhomescore+delimiter+injurynotes+delimiter+refsumpires+delimiter+lineups+delimiter+weather+delimiter+
						location+delimiter+status+delimiter+period+delimiter+specialnotes;														
												
*/												

					String eventnumber = items[x++];
					String visitorgamenumber = items[x++];
					String homegamenumber = items[x++];
					String gamedatelong = items[x++];
					String gametimelong = items[x++];
					String visitorteamname = items[x++];
					String hometeamname = items[x++];
					String visitorabbr = items[x++];
					String homeabbr = items[x++];
					String league_id = items[x++];
					String visitscore = items[x++];
					String homescore = items[x++];
					String eventnumberalso = items[x++];
					String subleague_id = items[x++];
					String addedgamebool = items[x++];
					String extragamebool = items[x++];
					String tba = items[x++];
					String visitornickname = items[x++];
					String homenickname = items[x++];
					String visitorcity = items[x++];
					String homecity = items[x++];
					String visitorteamid = items[x++];
					String hometeamid = items[x++];
					String visitorpitcher = items[x++];
					String homepitcher = items[x++];
					String visitorlefthandedbool = items[x++];
					String homelefthandedbool = items[x++];
					String status = items[x++];
					String timeremaining = items[x++];
					
					Game g = AppController.getGame(eventnumber);
					if(g == null) 
					{
						g = new Game();
					}
					else
					{
						oldvpitcher = g.getVisitorpitcher();
						oldhpitcher = g.getHomepitcher();
						oldgametime = g.getGametime();
						
					}						
					g.setGame_id(Integer.parseInt(eventnumber));
					g.setVisitorgamenumber(Integer.parseInt(visitorgamenumber));
					g.setHomegamenumber(Integer.parseInt(homegamenumber));
					g.setGamedate(new java.sql.Date(Long.parseLong(gamedatelong)));
					g.setGametime(new java.sql.Time(Long.parseLong(gametimelong)));
					g.setVisitorteam(visitorteamname);
					g.setHometeam(hometeamname);
					g.setShortvisitorteam(visitorabbr);
					g.setShorthometeam(homeabbr);
					g.setLeague_id(Integer.parseInt(league_id));
					
					// owen reput these 2 scores in
					g.setCurrentvisitorscore(Integer.parseInt(visitscore));
					g.setCurrenthomescore(Integer.parseInt(homescore));
					
					g.setSubleague_id(Integer.parseInt(subleague_id));
					g.setAddedgame(Boolean.parseBoolean(addedgamebool));
					g.setExtragame(Boolean.parseBoolean(extragamebool));
					g.setTba(Boolean.parseBoolean(tba));
					g.setVisitornickname(visitornickname);
					g.setHomenickname(homenickname);
					g.setVisitorcity(visitorcity);
					g.setHomecity(homecity);
					g.setVisitor_id(Integer.parseInt(visitorteamid));
					g.setHome_id(Integer.parseInt(hometeamid));
					g.setVisitorpitcher(visitorpitcher);
					g.setHomepitcher(homepitcher);
					g.setVisitorlefthanded(Boolean.parseBoolean(visitorlefthandedbool));
					g.setHomelefthanded(Boolean.parseBoolean(homelefthandedbool));
					
					g.setStatus(status);
					g.setTimeremaining(timeremaining);
					
					AppController.addGame(g,repaint);
					Sport s = AppController.getSport(g.getLeague_id());
					
					
					boolean seriesprice = false;
					try 
					{
						seriesprice = g.isSeriesprice();
					}
					catch(Exception ex) {}
					
					if( (!oldvpitcher.equals(g.getVisitorpitcher()) && !oldvpitcher.equals("")  && !g.getVisitorpitcher().equalsIgnoreCase("UNDECIDED")  ) 
						|| (!oldhpitcher.equals(g.getHomepitcher()) && !oldhpitcher.equals("")  && !g.getHomepitcher().equalsIgnoreCase("UNDECIDED")                                                         ) 
					  ) //pitching change
					{
						
						
								boolean popup = false;
								boolean sound = false;
								int popupsecs = 15;
								int location = 2;
								String audiofile = "";
								String[] sports;
								boolean goodsport = false;		
											String hrmin=AppController.getCurrentHoursMinutes();
											String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
											
											String popalertname="Alert at:"+hrmin+"\nPitching Change:"+s.getSportname()+","+s.getLeaguename()+","+teaminfo;
											AppController.alertsVector.addElement(popalertname);
																		
						new UrgentMessage("<HTML><H1>Pitching Change</H1><FONT COLOR=BLUE>" +
							s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
											
											"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD><TD>"+g.getVisitorpitcher()+"</TD></TR>"+
											"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD><TD>"+g.getHomepitcher()+"</TD></TR>"+
											
											"</TABLE></FONT></HTML>",popupsecs*1000,location,AppController.getMainTabPane());							
										
										
										
												//playSound("pitchingchange.wav");
												new SoundPlayer("pitchingchange.wav");
														
String tc = new java.util.Date()+"..PITCHING CHANGE!!!<HTML><H1>Pitching Change</H1><FONT COLOR=BLUE>" +
							s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
											
											"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD><TD>"+g.getVisitorpitcher()+"</TD></TR>"+
											"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD><TD>"+g.getHomepitcher()+"</TD></TR></TABLE></FONT></HTML>";
						try { writeToFile("c:\\spankoddsclient2\\timechanges.txt",tc,true); } catch(Exception ex) {}
						System.out.println(tc);						
					}
					
					if(!seriesprice && oldgametime != null && !oldgametime.equals("") && !oldgametime.toString().equals(g.getGametime().toString())) // time change
					{
						
								String prefs = AppController.getUser().getTimechangeAlert();
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
											String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
											
											String popalertname="Alert at:"+hrmin+"\nTime Change:"+s.getSportname()+","+s.getLeaguename()+","+teaminfo+" From "+oldgametime+"to "+g.getGametime();
											AppController.alertsVector.addElement(popalertname);
											
											new UrgentMessage("<HTML><H1>Time Change</H1><FONT COLOR=BLUE>" +
											s.getLeaguename()+"<BR><TABLE cellspacing=5 cellpadding=5>"+
											
											"<TR><TD>"+g.getVisitorgamenumber()+"</TD><TD>"+g.getVisitorteam()+"</TD></TR>"+
											"<TR><TD>"+g.getHomegamenumber()+"</TD><TD>"+g.getHometeam()+"</TD></TR>"+
											"<TR><TD>From "+oldgametime+"</TD><TD>to "+g.getGametime()+"</TD></TR>"+
											"</TABLE></FONT></HTML>",popupsecs*1000,location,AppController.getMainTabPane());							
										}
										
										if(sound)
										{
											if(audiofile.equals(""))
											{
												//playSound("timechange.wav");
												new SoundPlayer("timechange.wav");
											}
											else
											{
												new SoundPlayer(audiofile);
												//playSound(audiofile);
											}
										}	

								}		
						String tc = new java.util.Date()+"..TIME CHANGE!!! "+g.getVisitorgamenumber()+"..old="+oldgametime.getTime()+"...new="+g.getGametime().getTime();
						try { writeToFile("c:\\spankoddsclient2\\timechanges.txt",tc,true); } catch(Exception ex) {}
						System.out.println(tc);
					}
					
					//AppController.refreshTabs();
				} 
			else if(messagetype.equals("NEWORUPDATE2")) // this comes from deleteyesterdaygamesandpublish whith a few additional flags
			{
			
					String data = textMessage.getText();
					System.out.println("new game! "+data);
					String items[] = data.split("~");
					int x = 0;
//"483~483~484~1599980400000~1600016400000~TEST1~TEST2~PHI~WAS~1~0~0~483~1~false~false~false~Eagles~Redskins~Philadelphia~Washington~0~0~~~false~false";					

// need to merge both of these
//loginclient
/*
						  Game game = new Game(Integer.parseInt(array[0]),Integer.parseInt(array[1]),Integer.parseInt(array[2]),Integer.parseInt(array[3]),
												Integer.parseInt(array[4]),new java.sql.Date(Long.parseLong(array[5])),new java.sql.Time(Long.parseLong(array[6])),
												array[7],array[8],array[9],array[10],Integer.parseInt(array[11]),Integer.parseInt(array[12]),Integer.parseInt(array[13]),
												Integer.parseInt(array[14]),Integer.parseInt(array[15]),array[16],array[17],array[18],array[19],array[20],array[21],
												array[22],array[23]);
						Game g = new Game(game_id,visitorgamenumber,homegamenumber,visitoraltgamenumber,homealtgamenumber,gamedateandtime,
										  visitorteam,hometeam,shortvisitorteam,shorthometeam,sport,league,currentvisitorscore,
										  currenthomescore,finalvisitorscore,finalhomescore,injurynotes,refsumpires,lineups,weather,
										  location,status,period,specialnotes);												
												
						int game_id = rs.getInt("game_id");
							
						int visitorgamenumber = rs.getInt("visitorgamenumber");
						int homegamenumber = rs.getInt("homegamenumber");
						int visitoraltgamenumber = rs.getInt("visitoraltgamenumber");
						int homealtgamenumber = rs.getInt("homealtgamenumber");
						java.sql.Date gamedate = rs.getDate("gamedate");
						java.sql.Time gametime = rs.getTime("gametime");
						
						
						
						String visitorteam = rs.getString("visitorteam");
						String hometeam = rs.getString("hometeam");
						String shortvisitorteam = rs.getString("shortvisitorteam");
						String shorthometeam = rs.getString("shorthometeam");
						int league_id = rs.getInt("league_id");
						int currentvisitorscore = rs.getInt("currentvisitorscore");
						int currenthomescore = rs.getInt("currenthomescore");
						int finalvisitorscore = rs.getInt("finalvisitorscore");
						int finalhomescore = rs.getInt("finalhomescore");
						Blob injurynotesblob = rs.getBlob("injurynotes");
						Blob refsumpiresblob = rs.getBlob("refsumpires");
						Blob lineupsblob = rs.getBlob("lineups");
						Blob weatherblob = rs.getBlob("weather");
						Blob specialnotesblob = rs.getBlob("specialnotes");
						
						String location = rs.getString("location");
						String status = rs.getString("status");
						String period = rs.getString("period");
						
						if(location == null) location = "";
						if(period == null) period = "";
						String injurynotes = "";
						String refsumpires = "";
						String lineups = "";
						String weather = "";
						String specialnotes ="A";



						try {injurynotes = new String(injurynotesblob.getBytes(1, (int) injurynotesblob.length()));} catch(Exception ex) {}
						try {refsumpires = new String(refsumpiresblob.getBytes(1, (int) refsumpiresblob.length()));} catch(Exception ex) {}
						try {lineups = new String(lineupsblob.getBytes(1, (int) lineupsblob.length()));} catch(Exception ex) {}
						try {weather = new String(weatherblob.getBytes(1, (int) weatherblob.length()));} catch(Exception ex) {}
						try {specialnotes = new String(specialnotesblob.getBytes(1, (int) specialnotesblob.length()));} catch(Exception ex) {}
						long gamedatelong = gamedate.getTime();
						long gametimelong = gametime.getTime();

						text = ""+game_id+delimiter+visitorgamenumber+delimiter+homegamenumber+delimiter+visitoraltgamenumber+delimiter+homealtgamenumber+
						delimiter+gamedatelong+delimiter+gametimelong+delimiter+visitorteam+delimiter+hometeam+delimiter+shortvisitorteam+delimiter+shorthometeam+
						delimiter+league_id+delimiter+currentvisitorscore+delimiter+currenthomescore+delimiter+finalvisitorscore+
						delimiter+finalhomescore+delimiter+injurynotes+delimiter+refsumpires+delimiter+lineups+delimiter+weather+delimiter+
						location+delimiter+status+delimiter+period+delimiter+specialnotes;														
												
*/												

					String eventnumber = items[x++];
					String visitorgamenumber = items[x++];
					String homegamenumber = items[x++];
					String gamedatelong = items[x++];
					String gametimelong = items[x++];
					String visitorteamname = items[x++];
					String hometeamname = items[x++];
					String visitorabbr = items[x++];
					String homeabbr = items[x++];
					String league_id = items[x++];
					String visitscore = items[x++];
					String homescore = items[x++];
					String eventnumberalso = items[x++];
					String subleague_id = items[x++];
					String addedgamebool = items[x++];
					String extragamebool = items[x++];
					String tba = items[x++];
					String visitornickname = items[x++];
					String homenickname = items[x++];
					String visitorcity = items[x++];
					String homecity = items[x++];
					String visitorteamid = items[x++];
					String hometeamid = items[x++];
					String visitorpitcher = items[x++];
					String homepitcher = items[x++];
					String visitorlefthandedbool = items[x++];
					String homelefthandedbool = items[x++];
					String status = items[x++];
					String timeremaining = items[x++];
					
					String injurynotes = items[x++];
					String refsumpires = items[x++];
					String lineups = items[x++];
					String weather = items[x++];
					String specialnotes = items[x++];
					String forprop = items[x++];
					String circled = items[x++];
					String started = items[x++];
					String neutrallocation = items[x++];
					String ingame = items[x++];
					String seriesprice = items[x++];
					String gamestatusts = items[x++];
					String scorets = items[x++];
					
				
					
					Game g = AppController.getGame(eventnumber);
					if(g == null) g = new Game();
					g.setGame_id(Integer.parseInt(eventnumber));
					g.setVisitorgamenumber(Integer.parseInt(visitorgamenumber));
					g.setHomegamenumber(Integer.parseInt(homegamenumber));
					g.setGamedate(new java.sql.Date(Long.parseLong(gamedatelong)));
					g.setGametime(new java.sql.Time(Long.parseLong(gametimelong)));
					g.setVisitorteam(visitorteamname);
					g.setHometeam(hometeamname);
					g.setShortvisitorteam(visitorabbr);
					g.setShorthometeam(homeabbr);
					g.setLeague_id(Integer.parseInt(league_id));
					
					// owen reput these 2 scores in
					g.setCurrentvisitorscore(Integer.parseInt(visitscore));
					g.setCurrenthomescore(Integer.parseInt(homescore));
					
					g.setSubleague_id(Integer.parseInt(subleague_id));
					g.setAddedgame(Boolean.parseBoolean(addedgamebool));
					g.setExtragame(Boolean.parseBoolean(extragamebool));
					g.setTba(Boolean.parseBoolean(tba));
					g.setVisitornickname(visitornickname);
					g.setHomenickname(homenickname);
					g.setVisitorcity(visitorcity);
					g.setHomecity(homecity);
					g.setVisitor_id(Integer.parseInt(visitorteamid));
					g.setHome_id(Integer.parseInt(hometeamid));
					g.setVisitorpitcher(visitorpitcher);
					g.setHomepitcher(homepitcher);
					g.setVisitorlefthanded(Boolean.parseBoolean(visitorlefthandedbool));
					g.setHomelefthanded(Boolean.parseBoolean(homelefthandedbool));
					
					g.setStatus(status);
					g.setTimeremaining(timeremaining);
					


					g.setInjurynotes(injurynotes);
					g.setRefsumpires(refsumpires);
					g.setLineups(lineups);
					g.setWeather(weather);
					g.setSpecialnotes(specialnotes);
					g.setForprop(Boolean.parseBoolean(forprop));
					g.setCircled(Boolean.parseBoolean(circled));
					g.setStarted(Boolean.parseBoolean(started));
					g.setNeutrallocation(Boolean.parseBoolean(neutrallocation));
					g.setIngame(Boolean.parseBoolean(ingame));
					g.setSeriesprice(Boolean.parseBoolean(seriesprice));
					
					g.setGamestatusts(new Timestamp(Long.parseLong(gamestatusts)));
					g.setScorets(new Timestamp(Long.parseLong(scorets)));
					
					AppController.addGame(g,repaint);
					//AppController.refreshTabs();
				} 				
			else if(messagetype.equals("REMOVE"))
			{
				String data = textMessage.getText(); // this is gamenumber
			//	System.out.print("ABOUT TO REMOVE="+data+".");
				String[] gameidarr = data.split("~");
				//AppController.removeGame(Integer.parseInt(data));
				AppController.removeGames(gameidarr);


			}	
			else if(messagetype.equals("REMOVEDATE"))
			{
				String date = textMessage.getText(); // this is gamenumber
			//	System.out.print("ABOUT TO REMOVE="+data+".");
				
					AppController.removeGameDate(date,leagueid);
				


			}				
		}	
		catch (Exception e) {
			System.out.println("exception games consumer "+e);
			System.out.println(textMessage.toString());
			
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

public static void writeToFile(String fileName, String data,boolean append) 
	{
		//System.out.println("Data is "+data);
		 //System.out.println("Writing to "+fileName);
		DataOutputStream out = null;
		
		try
		{
			out = new DataOutputStream(new FileOutputStream(fileName,append));
			out.writeBytes(data+"\n");

		}
		catch (IOException e)
        {
			System.err.println("IOException Error");
			System.out.println("Couldn't get I/O for htmlfile "+fileName+"\n"+e);
        }
		finally
		{
			try
			{
				out.close();
			}
			catch(Exception ex)
			{
				System.err.println("exception in closing file");
			}
		}


	}
	
}
