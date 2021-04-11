package com.sia.client.model;

import com.sia.client.ui.LineAlertManager;

import java.sql.Timestamp;
import java.io.Serializable;

public class Moneyline extends Line implements Serializable
{
static Moneyline ml = new Moneyline(99999,99999,99999,99999,99999,new Timestamp(1),0);




boolean isbestvisitmoney = false;
boolean isbesthomemoney = false;
boolean isbestdrawmoney = false;

double currentvisitjuice;
double currenthomejuice;
double currentdrawjuice;
java.sql.Timestamp currentts = new java.sql.Timestamp(1000);

double priorvisitjuice;
double priorhomejuice;
double priordrawjuice;
java.sql.Timestamp priorts = new java.sql.Timestamp(1000);;

double openervisitjuice;
double openerhomejuice;
double openerdrawjuice;
java.sql.Timestamp openerts =new java.sql.Timestamp(1000);


public boolean isBestVisitMoney()
{
	return isbestvisitmoney;
}


public boolean isBestHomeMoney()
{
	return isbesthomemoney;
}

public void setBestVisitMoney(boolean b)
{
	isbestvisitmoney = b;
}

public void setBestHomeMoney(boolean b)
{
	isbesthomemoney = b;
}

public void setBestDrawMoney(boolean b)
{
	isbestdrawmoney = b;
}


public boolean isBestDrawMoney()
{
	return isbestdrawmoney;
}
public Moneyline()
{
	type = "moneyline";
}

public Moneyline(int gid, int bid, double vj,double hj,double dj,java.sql.Timestamp ts,int p)
{
	this();
	currentvisitjuice = priorvisitjuice = openervisitjuice = vj;
	currenthomejuice = priorhomejuice = openerhomejuice = hj;
	currentdrawjuice = priordrawjuice = openerdrawjuice = dj;
	currentts = priorts = openerts = ts;
	
	gameid = gid;
	bookieid = bid;
	period = p;
	
	
}

public Moneyline(int gid, int bid, double vj,double hj,double dj,java.sql.Timestamp ts,double pvj,double phj,double pdj,java.sql.Timestamp pts,int p)

{
	this();
	currentvisitjuice = vj;
	currenthomejuice = hj;
	currentdrawjuice = dj;
	currentts = ts;
	
	priorvisitjuice = pvj;
	priorhomejuice = phj;
	priordrawjuice = pdj;
	priorts = pts;
	
	gameid = gid;
	bookieid = bid;
	period = p;
	
		
}

public Moneyline(int gid, int bid, double vj,double hj,double dj,java.sql.Timestamp ts,double pvj,double phj,double pdj,java.sql.Timestamp pts,double ovj,double ohj,double odj,java.sql.Timestamp ots,int p)

{
	this();
	currentvisitjuice = vj;
	currenthomejuice = hj;
	currentdrawjuice = dj;
	currentts = ts;
	
	priorvisitjuice = pvj;
	priorhomejuice = phj;
	priordrawjuice = pdj;
	priorts = pts;
	
	openervisitjuice = ovj;
	openerhomejuice = ohj;
	openerdrawjuice = odj;
	openerts = ots;
	
	gameid = gid;
	bookieid = bid;
	period = p;
			
	
	
}
public String recordMove(double visitjuice,double homejuice,double drawjuice,java.sql.Timestamp ts,boolean isopener)
{
		
	if(visitjuice != 0)
	{
		this.setCurrentvisitjuice(visitjuice);
		this.setCurrentts(ts);

		if(isopener)
		{
			this.setOpenervisitjuice(visitjuice);
			this.setOpenerts(ts);
		}
	}
	if(homejuice != 0)
	{
		this.setCurrenthomejuice(homejuice);
		this.setCurrentts(ts);
	
		if(isopener)
		{
			this.setOpenerhomejuice(homejuice);
			this.setOpenerts(ts);
		}
	}
	if(drawjuice != 0)
	{	
		this.setCurrentdrawjuice(drawjuice);
		this.setCurrentts(ts);
				
		if(isopener)
		{
			this.setOpenerdrawjuice(drawjuice);
			this.setOpenerts(ts);
		}
	}	
	
	
	try
	{
		if(this.getPriorvisitjuice() < this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) //-110 to -105 , +105 to +110
		{
			this.whowasbet = "h";
		}
		else if(this.getPriorvisitjuice() > this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice())
		{
			this.whowasbet = "v";
		}
		else
		{
			this.whowasbet = "";
		}
	}
	catch(Exception ex)	 { this.whowasbet = "";}
	
	try
	{
		if(!this.whowasbet.equals(""))
		{
			LineAlertManager.checkMove(this);
		}
	}
	catch(Exception ex)
	{
		System.out.println("exception checking moneyline for linealert! "+ex);
	}
	
	BestLines.calculatebestmoney(gameid,period);
	return this.whowasbet;
	
	
}


public String getShortPrintedMoneyline()
{
	double juice = min(currentvisitjuice,currenthomejuice);
	return juice+"";	
}	

public boolean isOpener()
{
	
	if(priorvisitjuice == 0 && priorhomejuice == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
}
public double getCurrentvisitjuice()
{
	return currentvisitjuice;
}
public void setCurrentvisitjuice(double currentvisitjuice)
{
	setPriorvisitjuice(getCurrentvisitjuice());
	this.currentvisitjuice = currentvisitjuice;
}


public double getCurrenthomejuice()
{
	return currenthomejuice;
}
public void setCurrenthomejuice(double currenthomejuice)
{
	setPriorhomejuice(getCurrenthomejuice());
	this.currenthomejuice = currenthomejuice;
}

public double getCurrentdrawjuice()
{
	return currentdrawjuice;
}
public void setCurrentdrawjuice(double currentdrawjuice)
{
	setPriordrawjuice(getCurrentdrawjuice());
	this.currentdrawjuice = currentdrawjuice;
}

public java.sql.Timestamp getCurrentts()
{
	return currentts;
}
public void setCurrentts(java.sql.Timestamp currentts)
{
	setPriorts(getCurrentts());
	this.currentts = currentts;
}
public java.sql.Timestamp getPriorts()
{
	return priorts;
}
public void setPriorts(java.sql.Timestamp priorts)
{
	this.priorts = priorts;
}

public double getPriorvisitjuice()
{
	return priorvisitjuice;
}
public void setPriorvisitjuice(double priorvisitjuice)
{
	this.priorvisitjuice = priorvisitjuice;
}


public double getPriorhomejuice()
{
	return priorhomejuice;
}
public void setPriorhomejuice(double priorhomejuice)
{
	this.priorhomejuice = priorhomejuice;
}

public double getPriordrawjuice()
{
	return priordrawjuice;
}
public void setPriordrawjuice(double priordrawjuice)
{
	this.priordrawjuice = priordrawjuice;
}

public java.sql.Timestamp getOpenerts()
{
	return openerts;
}
public void setOpenerts(java.sql.Timestamp openerts)
{
	this.openerts = openerts;
}

public double getOpenervisitjuice()
{
	return openervisitjuice;
}
public void setOpenervisitjuice(double openervisitjuice)
{
	this.openervisitjuice = openervisitjuice;
}


public double getOpenerhomejuice()
{
	return openerhomejuice;
}
public void setOpenerhomejuice(double openerhomejuice)
{
	this.openerhomejuice = openerhomejuice;
}

public double getOpenerdrawjuice()
{
	return openerdrawjuice;
}
public void setOpenerdrawjuice(double openerdrawjuice)
{
	this.openerdrawjuice = openerdrawjuice;
}

}