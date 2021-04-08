package com.sia.client.ui;

import java.util.Hashtable;
import java.io.Serializable;
public class Bookie implements java.io.Serializable
{
	
int bookie_id;
String name;
String shortname;
String website;
String mlbmoneytype;


public Bookie(int bookie_id,String name,String shortname,String website,String mlbmoneytype) 
{
	
	this.bookie_id = bookie_id;
	this.name = name;
	this.shortname = shortname;
	this.website = website;
	this.mlbmoneytype = mlbmoneytype;
	
	
}

/*
// this is all instead handled by BookieGameController
Hashtable<Game,Spreadline> spreads = new Hashtable<Game,Spreadline>();
Hashtable<Game,Totalline> totals = new Hashtable<Game,Totalline>();
Hashtable<Game,Moneyline> moneylines = new Hashtable<Game,Moneyline>();

public Spreadline getSpreadlineForThisGame(Game gm)
{
	return spreads.get(gm);
}
public Totalline getTotallineForThisGame(Game gm)
{
	return totals.get(gm);
}
public Moneyline getMoneylineForThisGame(Game gm)
{
	return moneylines.get(gm);
}
*/
public int getBookie_id()
{
	return bookie_id;
}
public void setBookie_id(int bookie_id)
{
	this.bookie_id = bookie_id;
}

public String getName()
{
	return name;
}
public void setName(String name)
{
	this.name = name;
}

public String getWebsite()
{
	return website;
}
public void setWebsite(String website)
{
	this.website = website;
}

public String getShortname()
{
	return shortname;
}
public void setShortname(String shortname)
{
	this.shortname = shortname;
}

public String getMlbmoneytype()
{
	return mlbmoneytype;
}
public void setMlbmoneytype(String mlbmoneytype)
{
	this.mlbmoneytype = mlbmoneytype;
}

public String toString()
{
	return getName();
}
	
}