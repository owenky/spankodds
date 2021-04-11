package com.sia.client.ui;

import com.sia.client.model.Game;

public class Line
{

int bookieid;
int gameid;
int period;
String type;
String whowasbet = "";

public static void main(String args[])
{
	
	
	
}
public String getPrintedSpreadLine(double ml)
{
	String retvalue = "";
	if(ml >0)
	{
		retvalue =  "+"+ml;
	}
	else if(ml <0)
	{
		retvalue = ml+"";
	}	
	else if(ml == 0)
	{
		retvalue =  "pk";
	}
	else
	{
		retvalue = "";
	}
	retvalue = retvalue.replace(".0","");
	char half = AsciiChar.getAscii(170);
	retvalue = retvalue.replace(".5",half+"");
	return retvalue;
}
public String getPrintedJuiceLine(double ml)
{
	String retvalue = "";
	if(ml >0)
	{
		retvalue = "+"+ml;
	}
	else if(ml <0)
	{
		retvalue = ml+"";
	}	
	else
	{
		retvalue = "";
	}
	retvalue = retvalue.replace(".0","");
	return retvalue;
	
}


public double min(double d1,double d2)
{
	if(d1 < d2)
	{
		return d1;
	}
	else 
	{
		return d2;
	}
}
public double max(double d1,double d2)
{
	if(d1 < d2)
	{
		return d2;
	}
	else 
	{
		return d1;
	}
}
public String getType()
{
	return type;
}

public String getWhowasbet()
{
	return whowasbet;
}

public void setWhowasbet(String w)
{
	whowasbet = w;
}

public int getGameid()
{
	return gameid;
}
public void setGameid(int gameid)
{
	this.gameid = gameid;
}

public int getBookieid()
{
	return bookieid;
}
public void setBookieid(int bookieid)
{
	this.bookieid = bookieid;
}

public int getPeriod()
{
	return period;
}
public void setPeriod(int period)
{
	this.period = period;
}

public String getVisitorTeam()
{
	return AppController.getGame(gameid).getVisitorteam();
}
public String getHomeTeam()
{
	return AppController.getGame(gameid).getHometeam();
}
public String getGame()
{
	Game g = AppController.getGame(gameid);
	return g.getVisitorteam()+"@"+g.getHometeam();
}
}