package com.sia.client.ui;

import java.sql.Timestamp;
import java.util.Vector;
public class User 
{
	String email;
	String password;
	String username;
	String address;
	String city;
	String state;
	String country;
	String phoneumber;
	String timezone;
	String oddstype;
	String notificationmethod;
	String subscriptiontype;
	boolean isloggedin;
	String bookiecolumnprefs;
	String fixedcolumnprefs;
	
	
	String columncolors;
	String customtabs;
	String finalalert;
	String startedalert;
	String halftimealert;
	String lineupalert;
	String officialalert;
	String injuryalert;
	String timechangealert;
	String limitchangealert;
	String bigearnalert;
	
	String footballpref;
	String basketballpref;
	String baseballpref;
	String hockeypref;
	String fightingpref;
	String soccerpref;
	String autoracingpref;
	String golfpref;
	String tennispref;
	

	
	Vector bookiecolumnsvector;
	Vector colorcolumnvector;
	java.sql.Timestamp logintime;
	

	String chartfilename;
	int chartminamtnotify=25;
	int chartsecsrefresh =30;
	
	
	public User(String username,String password,String email,String address,String city,String state,String country,String phoenumber,
				String timezone,String oddstype,String notificationmethod,String subscriptiontype,boolean isloggedin,
				String bookiecolumnprefs,String fixedcolumnprefs,
				String columncolors,
				String customtabs,
				String finalalert,
				String startedalert,
				String halftimealert,
				String lineupalert,
				String officialalert,
				String injuryalert,
				String timechangealert,
				String limitchangealert,
				String bigearnalert,
				String footballpref,
				String basketballpref,
				String baseballpref,
				String hockeypref,
				String fightingpref,
				String soccerpref,
				String autoracingpref,
				String golfpref,
				String tennispref,
				String chartfilename,
				int chartminamtnotify,
				int chartsecsrefresh
				)
				
				
				
{
	this.email = email;
	this.password = password;
	this.username = username;
	this.address = address;
	this.city = city;
	this.state = state;
	this.country = country;
	this.phoneumber = phoneumber;
	this.timezone = timezone;
	this.oddstype = oddstype;
	this.notificationmethod = notificationmethod;
	this.subscriptiontype = subscriptiontype;
	this.isloggedin = isloggedin;
	this.bookiecolumnprefs = bookiecolumnprefs;
	this.fixedcolumnprefs = fixedcolumnprefs;
	
	
	this.columncolors = columncolors;
	this.customtabs = customtabs;
	this.finalalert = finalalert;
	this.startedalert = startedalert;
	this.halftimealert = halftimealert;
	this.lineupalert = lineupalert;
	this.officialalert = officialalert;
	this.injuryalert = injuryalert;
	this.timechangealert = timechangealert;
	this.limitchangealert = limitchangealert;
	this.bigearnalert = bigearnalert;
	
	
	this.footballpref = footballpref;
	this.basketballpref = basketballpref;
	this.baseballpref = baseballpref;
	this.hockeypref = hockeypref;
	this.fightingpref = fightingpref;
	this.soccerpref = soccerpref;
	this.autoracingpref = autoracingpref;
	this.golfpref = golfpref;
	this.tennispref =  tennispref;
	this.chartfilename =  chartfilename;
	this.chartminamtnotify =  chartminamtnotify;
	this.chartsecsrefresh =  chartsecsrefresh;
	
	
	
	
	logintime = new Timestamp(new java.util.Date().getTime());
	
}
	

	public int getChartMinAmtNotify()
   {
	   return chartminamtnotify;
   }   
   public void setChartMinAmtNotify(int s)
   {
	  chartminamtnotify = s;
   }    
	
	public int getChartSecsRefresh()
   {
	   return chartsecsrefresh;
   }   
   public void setChartSecsRefresh(int s)
   {
	  chartsecsrefresh = s;
   }    
	
	public String getChartFileName()
   {
	   return chartfilename;
   }   
   public void setChartFileName(String s)
   {
	  chartfilename = s;
   }    
	
	 public String getFootballPref()
   {
	   return footballpref;
   }   
   public void setFootballPref(String s)
   {
	  footballpref = s;
   }    
   
   public String getBasketballPref()
   {
	   return basketballpref;
   }   
   public void setBasketballPref(String s)
   {
	  basketballpref = s;
   }    
   
    public String getBaseballPref()
   {
	   return baseballpref;
   }   
   public void setBaseballPref(String s)
   {
	  baseballpref = s;
   }    
   
    public String getHockeyPref()
   {
	   return hockeypref;
   }   
   public void setHockeyPref(String s)
   {
	  hockeypref = s;
   }   

	public String getFightingPref()
   {
	   return fightingpref;
   }   
   public void setFightingPref(String s)
   {
	  fightingpref = s;
   }  

	public String getSoccerPref()
   {
	   return soccerpref;
   }   
   public void setSoccerPref(String s)
   {
	  soccerpref = s;
   }       
   
   public String getAutoracingPref()
   {
	   return autoracingpref;
   }   
   public void setAutoracingPref(String s)
   {
	  autoracingpref = s;
   }     
   
   public String getGolfPref()
   {
	   return golfpref;
   }   
   public void setGolfPref(String s)
   {
	  golfpref = s;
   }     
   
    public String getTennisPref()
   {
	   return tennispref;
   }   
   public void setTennisPref(String s)
   {
	  tennispref = s;
   }     
	
	
	
   public Timestamp	getLoginTime()
   {
	   
	   return logintime;
   }
   public String getBookieColumnPrefs()
   {
	   
	   return bookiecolumnprefs;
   }
   public void setBookieColumnPrefs(String s)
   {
	   
	   bookiecolumnprefs = s;
   }   
   public String getFixedColumnPrefs()
   {
	   return fixedcolumnprefs;
   }   
   public void setFixedColumnPrefs(String s)
   {
	  fixedcolumnprefs = s;
   }  
   
   public String getColumnColors()
   {
	   return columncolors;
   }   
   public void setColumnColors(String s)
   {
	  columncolors = s;
   }  
   public String getCustomTabs()
   {
	   return customtabs;
   }   
   public void setCustomTabs(String s)
   {
	  customtabs = s;
   }  
   
   public String getFinalAlert()
   {
	   return finalalert;
   }   
   public void setFinalAlert(String s)
   {
	  finalalert = s;
   }    
	
   public String getStartedAlert()
   {
	   return startedalert;
   }   
   public void setStartedAlert(String s)
   {
	  startedalert = s;
   }    
   
   public String getHalftimeAlert()
   {
	   return halftimealert;
   }   
   public void setHalftimeAlert(String s)
   {
	  halftimealert = s;
   }    

   public String getLineupAlert()
   {
	   return lineupalert;
   }   
   public void setLineupAlert(String s)
   {
	  lineupalert = s;
   }    
   
   public String getOfficialAlert()
   {
	   return officialalert;
   }   
   public void setOfficialAlert(String s)
   {
	  officialalert = s;
   }    
   
   public String getInjuryAlert()
   {
	   return injuryalert;
   }   
   public void setInjuryAlert(String s)
   {
	  injuryalert = s;
   }       
     
   public String getTimechangeAlert()
   {
	   return timechangealert;
   }   
   public void setTimechangeAlert(String s)
   {
	  timechangealert = s;
   }    
   
   public String getLimitchangeAlert()
   {
	   return limitchangealert;
   }   
   public void setLimitchangeAlert(String s)
   {
	  limitchangealert = s;
   }       
   
   public String getBigearnAlert()
   {
	   return bigearnalert;
   }   
   public void setBigearnAlert(String s)
   {
	  bigearnalert = s;
   }       

   
}