package com.sia.client.ui;

import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;

import java.util.Vector;

public class LineAlertManager
{
	public static String shortenSpread(double spreadd,double juice)
	{
		String retvalue = "";
		String spread = spreadd+"";
		if(spreadd < 1 && spreadd != 0 && spread.startsWith("0"))
		{
			spread = spread.substring(1);
		}		
		
		if(spreadd == 0)
		{
			spread = "pk";
		}
		if(spreadd > 0)
		{
			spread = "+"+spread;
		}
			
		if(juice == -110)
		{
			retvalue = spread;
			
		}
		else if(juice < 0)
		{
			juice = juice+100;
			String juicestr = juice+"";
			if(juice == 0)
			{
				retvalue = spread+"ev";
			}
			else if(juice > -10)
			{
				juice = juice*-1;
				juicestr = "-0"+juice;
				retvalue = spread+""+juicestr;
			}
			else if(juice <= -100) // initial juice was  -200 or worse
			{
				
				juice = juice -100;
				juicestr = ""+juice;
				retvalue = spread+""+juicestr;	
			}			
			else
			{
				retvalue = spread+""+juicestr;	
			
			}
			
		}
		else
		{
			juice = juice -100;
			
			if(juice == 0)
			{
				retvalue = spread+"ev";
			}			
			else if(juice < 10)
			{
				retvalue = spread+"+0"+juice;
			}
			else
			{
				retvalue = spread+"+"+juice;
				
			}
		}
		
		retvalue = retvalue.replace(".0","");
	
		char half = AsciiChar.getAscii(170);
		if(retvalue.equals("0.5") || retvalue.equals("0.50") )
		{
			retvalue= ".5";
		}	
		if(retvalue.equals("0.25") || retvalue.equals("0.250") )
		{
			retvalue= ".25";
		}		
		if(retvalue.equals("0.75") || retvalue.equals("0.750") )
		{
			retvalue= ".75";
		}			
		retvalue = retvalue.replace(".25","\u00BC");			
		retvalue = retvalue.replace(".5","\u00BD");			
		retvalue = retvalue.replace(".75","\u00BE");			
		return retvalue;
		
	}
public static String shortenTotal(double total,double juice)
	{
		String retvalue = total+"";
		if(Math.abs(total) < 1 && retvalue.startsWith("0"))
		{
			retvalue = retvalue.substring(1);
		}				
		retvalue = retvalue.replace(".0","");
		if(juice == -110)
		{
			// dont attach nothing
		}
		else if(juice < 0)
			{
				juice = juice+100;
				String juicestr = ""+juice;
				if(juice == 0)
				{
					retvalue = retvalue+"ev";
				}				
				else if(juice > -10)
				{
					juice = juice*-1;
					juicestr = "-0"+juice;
					retvalue = retvalue+""+juicestr;
				}
				else if(juice <= -100) // initial juice was  -200 or worse
				{
					
					juice = juice -100;
					juicestr = ""+juice;
					retvalue = retvalue+""+juicestr;					
				}
				else
				{
					retvalue = retvalue+""+juicestr;
					
				
				}
				
			}
		else
		{
			juice = juice -100;
			
			if(juice == 0)
			{
				retvalue = retvalue+"ev";
			}						
			else if(juice < 10)
			{
				retvalue = retvalue+"+0"+juice;
			}
			else
			{
				retvalue = retvalue+"+"+juice;
				
			}
			
		}
		retvalue = retvalue.replace(".0","");
		char half = AsciiChar.getAscii(170);

		retvalue = retvalue.replace(".25","\u00BC");			
		retvalue = retvalue.replace(".5","\u00BD");			
		retvalue = retvalue.replace(".75","\u00BE");			
		return retvalue;
		
	}	
	public static String mlfix(double juice)
	{
		String retvalue ="";
		if(juice == -100 || juice == +100)
		{
			retvalue = "EVEN";
		}
		else if(juice > 0)
		{
			retvalue = "+"+juice;
		}
		else
		{
			retvalue = ""+juice;
		}
		retvalue = retvalue.replace(".0","");
		return retvalue;
	}
	
	public static void checkMove(Line line)
	{
		if(line == null)
		{
			return;
		}
		boolean a = false;
		boolean b = false;
		Game g = AppController.getGame(line.getGameid());
		if(g == null)
		{
			return;
		}
		
		Sport s = AppController.getSport(g.getLeague_id());
		if(s == null)
		{
			return;
		}
		if(g.isIngame())
		{
			//System.out.println("skipping check move ingame "+g.getGame_id());
			return;
		}
		Vector linealertnodes = AppController.getLineAlertNodes();
		//System.out.println(linealertnodes.size());
		for(int i=0; i < linealertnodes.size(); i++)
		{
			try
			{
			LineAlertNode lan = (LineAlertNode)linealertnodes.elementAt(i);
			
			if(lan.doesthislinequalify(line))
			{
				System.out.println("about to send alert..");
				
				String html = "<html><body><H1>MAJOR LINE MOVE&nbsp;<FONT COLOR=BLUE>"+lan.getName()+"</H1>"+
				"<br><table><tr><td>"+s.getLeaguename()+"</td><td colspan=2>"+g.getGameString()+"</td></tr></table><table>";
				if(line instanceof Spreadline)
				{
					
					Vector v = lan.spreadlinestonotify;
					for(int j=0; j< v.size(); j++)
					{
						Spreadline l = (Spreadline)v.elementAt(j);	
						if(l.whowasbet.equals("v"))
						{
							a = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getVisitorgamenumber()+"&nbsp;"+l.getVisitorTeam()+"&nbsp;SPREAD</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenSpread(l.getPriorvisitspread(),l.getPriorvisitjuice())+
						"&nbsp;To&nbsp;"+shortenSpread(l.getCurrentvisitspread(),l.getCurrentvisitjuice())+"</td></tr>";
						}
						else
						{
							b = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getHomegamenumber()+"&nbsp;"+l.getHomeTeam()+"&nbsp;SPREAD</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenSpread(l.getPriorhomespread(),l.getPriorhomejuice())+
						"&nbsp;To&nbsp;"+shortenSpread(l.getCurrenthomespread(),l.getCurrenthomejuice())+"</td></tr>";							
						}
					}
					html = html+"</body></html>";
					if(a && b) continue; // this means both sides were hit so ignore it
					if(lan.isspreadplayaudio)
					{
						new SoundPlayer(lan.spreadaudiofilename);
					}
					if(lan.isspreadshowpopup)
					{
						String hrmin=AppController.getCurrentHoursMinutes();
						String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
											
						String popalertname="Alert at:"+hrmin+"Spreadline Move Alert:"+lan.getName()+","+teaminfo;
						AppController.alertsVector.addElement(popalertname);
						
						new UrgentMessage(html,lan.spreadpopupsecs*1000,lan.spreadpopuplocation,AppController.getMainTabPane());			
					}
				}
				else if(line instanceof Totalline)
				{
					
					Vector v = lan.totallinestonotify;
					for(int j=0; j< v.size(); j++)
					{
						Totalline l = (Totalline)v.elementAt(j);	
						if(l.whowasbet.equals("o"))
						{
							a = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getVisitorgamenumber()+"&nbsp;TOTAL OVER</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenTotal(l.getPriorover(),l.getPrioroverjuice())+
						"&nbsp;To&nbsp;"+shortenTotal(l.getCurrentover(),l.getCurrentoverjuice())+"</td></tr>";
						}
						else
						{
							b = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getHomegamenumber()+"&nbsp;TOTAL UNDER</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenTotal(l.getPriorunder(),l.getPriorunderjuice())+
						"&nbsp;To&nbsp;"+shortenTotal(l.getCurrentunder(),l.getCurrentunderjuice())+"</td></tr>";
						}						
						
					}
					html = html+"</body></html>";
					if(a && b) continue; // this means both sides were hit so ignore it
					if(lan.istotalplayaudio)
					{
						new SoundPlayer(lan.totalaudiofilename);
					}
					if(lan.istotalshowpopup)
					{
						String hrmin=AppController.getCurrentHoursMinutes();
						String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
											
						String popalertname="Alert at:"+hrmin+"\nTotalline Move Alert:"+lan.getName()+","+teaminfo;
						AppController.alertsVector.addElement(popalertname);
							
						new UrgentMessage(html,lan.totalpopupsecs*1000,lan.totalpopuplocation,AppController.getMainTabPane());			
					}
				}
				else if(line instanceof TeamTotalline)
				{
					html = html+"<tr><td colspan=2>TEAM TOTAL</td></tr>";
					Vector v = lan.teamtotallinestonotify;
					for(int j=0; j< v.size(); j++)
					{
						TeamTotalline l = (TeamTotalline)v.elementAt(j);
						if(l.whowasbet.equals("o"))
						{
							a = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getVisitorgamenumber()+"&nbsp;TEAMTOTAL OVER</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenTotal(l.getPriorvisitover(),l.getPriorvisitoverjuice())+
						"&nbsp;To&nbsp;"+shortenTotal(l.getCurrentvisitover(),l.getCurrentvisitoverjuice())+"</td></tr>";
						}
						else
						{
							b = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getVisitorgamenumber()+"&nbsp;TEAMTOTAL UNDER</td></tr>";			
							}							
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+shortenTotal(l.getPriorvisitunder(),l.getPriorvisitunderjuice())+
						"&nbsp;To&nbsp;"+shortenTotal(l.getCurrentvisitunder(),l.getCurrentvisitunderjuice())+"</td></tr>";
						}												
						
					}
					html = html+"</body></html>";
					if(a && b) continue; // this means both sides were hit so ignore it
					if(lan.isteamtotalplayaudio)
					{
						new SoundPlayer(lan.teamtotalaudiofilename);
					}
					if(lan.isteamtotalshowpopup)
					{
						String hrmin= AppController.getCurrentHoursMinutes();
						String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
						
						String popalertname="Alert at:"+hrmin+"\nTeamTotalline Alert:"+lan.getName()+","+teaminfo;
						AppController.alertsVector.addElement(popalertname);
							
						new UrgentMessage(html,lan.teamtotalpopupsecs*1000,lan.teamtotalpopuplocation,AppController.getMainTabPane());			
					}
				}
				else if(line instanceof Moneyline)
				{
					
					Vector v = lan.moneylinestonotify;
					for(int j=0; j< v.size(); j++)
					{
						Moneyline l = (Moneyline)v.elementAt(j);	
						if(l.whowasbet.equals("v"))
						{
							a = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getVisitorgamenumber()+"&nbsp;"+l.getVisitorTeam()+"&nbsp;MONEYLINE</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+
						mlfix(l.getPriorvisitjuice())+"&nbsp;To&nbsp;"+mlfix(l.getCurrentvisitjuice())+"</td></tr>";
						}
						else
						{
							b = true;
							if(j == 0)
							{
								html = html+"<tr><td colspan=2>"+g.getHomegamenumber()+"&nbsp;"+l.getHomeTeam()+"&nbsp;MONEYLINE</td></tr>";			
							}
						html = html+"<tr><td>"+AppController.getBookie(l.getBookieid())+"</td><td>"+
						mlfix(l.getPriorhomejuice())+"&nbsp;To&nbsp;"+mlfix(l.getCurrenthomejuice())+"</td></tr>";							
						}
					}
					html = html+"</body></html>";
					if(a && b) continue; // this means both sides were hit so ignore it
					if(lan.ismoneylineplayaudio)
					{
						new SoundPlayer(lan.moneylineaudiofilename);
					}
					if(lan.ismoneylineshowpopup)
					{
						
						String hrmin=AppController.getCurrentHoursMinutes();
						String teaminfo=g.getVisitorgamenumber()+"-"+g.getShortvisitorteam()+"@"+g.getHomegamenumber()+"-"+g.getShorthometeam();
						
						String popalertname="Alert at:"+hrmin+"\nMoneyline Alert:"+lan.getName()+","+teaminfo;
						AppController.alertsVector.addElement(popalertname);
						
						
						new UrgentMessage(html,lan.moneylinepopupsecs*1000,lan.moneylinepopuplocation,AppController.getMainTabPane());			
					}
				}
			}
			} 
			catch(Exception ex)
			{
				System.out.println("error iterating through lan "+ex);
			}
			
			
		}

	}

}