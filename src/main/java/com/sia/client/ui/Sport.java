package com.sia.client.ui;

public class Sport 
{
	int	sport_id;
	int league_id;
	int parentleague_id; 
	int region_id; 
	int numperiods; 
	double timeperperiod; 
	double halftimeminutes;

	String leaguename;
	String leagueabbr;
	String sportname;
	String sportabbr;
	boolean moneylinedefault;

	

	
	
	
	public Sport(int sport_id,int league_id,int parentleague_id,int region_id,int numperiods,double timeperperiod,double halftimeminutes,
				 String leaguename,String leagueabbr,String sportname,String sportabbr,boolean moneylinedefault)
{
	this.sport_id = sport_id;
	this.league_id = league_id;
	this.parentleague_id = parentleague_id;
	this.region_id = region_id;
	this.numperiods = numperiods;
	this.timeperperiod = timeperperiod;
	this.halftimeminutes = halftimeminutes;
	this.leaguename = leaguename;
	this.leagueabbr = leagueabbr;
	this.sportname = sportname;
	this.sportabbr = sportabbr;
	this.moneylinedefault = moneylinedefault;
}
	public int getSport_id()
	{
			return sport_id;
	}
	public void setSport_id(int sport_id)
	{
			this.sport_id = sport_id;
	}	
	public int getLeague_id()
	{
			return league_id;
	}
	public void setLeague_id(int league_id)
	{
			this.league_id = league_id;
	}	
	public int getParentleague_id()
	{
			return parentleague_id;
	}
	public void setParentleague_id(int parentleague_id)
	{
			this.parentleague_id = parentleague_id;
	}	
	public int getRegion_id()
	{
			return region_id;
	}
	public void setRegion_id(int region_id)
	{
			this.region_id = region_id;
	}	
	public int getNumperiods()
	{
			return numperiods;
	}
	public void setNumperiods(int numperiods)
	{
			this.numperiods = numperiods;
	}		
	public double getTimeperperiod()
	{
			return timeperperiod;
	}
	public void setTimeperperiod(double timeperperiod)
	{
			this.timeperperiod = timeperperiod;
	}		
	public double getHalftimeminutes()
	{
			return halftimeminutes;
	}
	public void setHalftimeminutes(double halftimeminutes)
	{
			this.halftimeminutes = halftimeminutes;
	}			
	public String getLeaguename()
	{
			return leaguename;
	}
	public void setLeaguename(String leaguename)
	{
			this.leaguename = leaguename;
	}			
	public String getLeagueabbr()
	{
			return leagueabbr;
	}
	public void setLeagueabbr(String leagueabbr)
	{
			this.leagueabbr = leagueabbr;
	}	
	public String getSportname()
	{
			return sportname;
	}
	public void setSportname(String sportname)
	{
			this.sportname = sportname;
	}			
	public String getSportabbr()
	{
			return sportabbr;
	}
	public void setSportabbr(String sportabbr)
	{
			this.sportabbr = sportabbr;
	}
	public boolean getMoneylinedefault()
	{
			return moneylinedefault;
	}
	public void setMoneylinedefault(boolean moneylinedefault)
	{
			this.moneylinedefault = moneylinedefault;
	}				
	
}