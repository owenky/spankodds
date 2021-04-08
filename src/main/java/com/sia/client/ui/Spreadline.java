package com.sia.client.ui;

import java.sql.Timestamp;


public class Spreadline extends Line
{
static Spreadline sl = new Spreadline(99999,99999,99999,99999,99999,99999,new Timestamp(1),0);

boolean isbestvisitspread = false;
boolean isbesthomespread = false;

double currentvisitspread;
double currentvisitjuice;
double currenthomespread;
double currenthomejuice;
java.sql.Timestamp currentts = new java.sql.Timestamp(1000);

double priorvisitspread;
double priorvisitjuice;
double priorhomespread;
double priorhomejuice;
java.sql.Timestamp priorts = new java.sql.Timestamp(1000);

double openervisitspread;
double openervisitjuice;
double openerhomespread;
double openerhomejuice;
java.sql.Timestamp openerts = new java.sql.Timestamp(1000);



public static void main(String args[])
{
	
	
	Spreadline sl = new Spreadline(109,29,5,-5,-110,-110,new java.sql.Timestamp(new java.util.Date().getTime()),0);
	System.out.println("hi "+sl.isOpener());
	System.out.println(sl.getPriorvisitspread());
}

public boolean isBestVisitSpread()
{
	return isbestvisitspread;
}


public boolean isBestHomeSpread()
{
	return isbesthomespread;
}

public void setBestVisitSpread(boolean b)
{
	isbestvisitspread = b;
}

public void setBestHomeSpread(boolean b)
{
	isbesthomespread = b;
}

public String recordMove(double visitspread,double visitjuice,double homespread,double homejuice,java.sql.Timestamp ts,boolean isopener)
{
		
	if(visitjuice != 0)
	{
		this.setCurrentvisitspread(visitspread);
		this.setCurrentvisitjuice(visitjuice);
		this.setCurrentts(ts);

		if(isopener)
		{
			this.setOpenervisitspread(visitspread);
			this.setOpenervisitjuice(visitjuice);
			this.setOpenerts(ts);
		}
	}
	if(homejuice != 0)
	{	
		this.setCurrenthomespread(homespread);
		this.setCurrenthomejuice(homejuice);
		this.setCurrentts(ts);

		if(isopener)
		{
			this.setOpenerhomespread(homespread);
			this.setOpenerhomejuice(homejuice);
			this.setOpenerts(ts);
		}
	}	
	try
	{
			if(this.getPriorvisitspread() < this.getCurrentvisitspread()) // -6 to -5  5 to 6
			{
				this.whowasbet = "h";
			}
			else if(this.getPriorvisitspread() > this.getCurrentvisitspread()) // -5 to -6 6 to 5
			{
				this.whowasbet = "v";
			}
			
			else //spreads equal
			{
				if(this.getPriorvisitjuice() < this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) //-110 to -105 , +105 to +110
				{
					this.whowasbet = "h";
				}
				else if(this.getPriorvisitjuice() > this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) // priorjuice > currentjuice -105 to -110 110 to 105
				{
					this.whowasbet = "v";
				}
				else
				{
					//nochange!
					this.whowasbet = "";
				}
				
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
		System.out.println("exception checking spread for linealert! "+ex);
	}
	
	BestLines.calculatebestspread(gameid,period);
	return sl.whowasbet;
	
	
}

public String getShortPrintedCurrentSpread()
	{
		return getShortPrintedSpread(currentvisitspread,currentvisitjuice,currenthomespread,currenthomejuice);
	}	
public String getShortPrintedPriorSpread()
	{
		return getShortPrintedSpread(priorvisitspread,priorvisitjuice,priorhomespread,priorhomejuice);
	}			
public String getShortPrintedOpenerSpread()
	{
		return getShortPrintedSpread(openervisitspread,openervisitjuice,openerhomespread,openerhomejuice);
	}				
		
public String getOtherPrintedCurrentSpread()
	{
		return getOtherPrintedSpread(currentvisitspread,currentvisitjuice,currenthomespread,currenthomejuice);
	}	
public String getOtherPrintedPriorSpread()
	{
		return getOtherPrintedSpread(priorvisitspread,priorvisitjuice,priorhomespread,priorhomejuice);
	}			
public String getOtherPrintedOpenerSpread()
	{
		return getOtherPrintedSpread(openervisitspread,openervisitjuice,openerhomespread,openerhomejuice);
	}		

	public String getShortPrintedSpread(double vspread,double vjuice,double hspread,double hjuice)
	{

		
		String retvalue = "";
		if(vjuice == 0) return "";
		
		
		double spreadd  = 0;
		double juice = 0;
		if(vspread < hspread)
		{
			spreadd = vspread;
			juice = vjuice;
		}
		else if(vspread > hspread)
		{
			spreadd = hspread;
			juice = hjuice;
		}
		else // game is a pk
		{
			juice = min(vjuice,hjuice);
			
		}
		spreadd = spreadd*-1; // take out minus sign
		
		String spread = spreadd+"";
		if(spreadd < 1 && spreadd != 0 && spread.startsWith("0"))
		{
			spread = spread.substring(1);
		}

		
		
		if(spreadd == 0)
		{
			spread = "pk";
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
	char half = AsciiChar.getAscii(170);
		
		retvalue = retvalue.replace(".25","\u00BC");			
		retvalue = retvalue.replace(".5","\u00BD");			
		retvalue = retvalue.replace(".75","\u00BE");			
	return retvalue;
		
	}

	public String getOtherPrintedSpread(double vspread,double vjuice,double hspread,double hjuice)
	{

 
		String retvalue = "";
		if(vjuice == 0) return "";
		double spreadd  = 0;
		double juice = 0;
		if(vspread < hspread)
		{
			spreadd = hspread;
			juice = hjuice;
		}
		else if(vspread > hspread)
		{
			spreadd = vspread;
			juice = vjuice;
		}
		else // game is a pk
		{
			juice = max(vjuice,hjuice);
			
		}
		
		

		String spread = spreadd+"";
		if(spreadd < 1 && spreadd != 0 && spread.startsWith("0"))
		{
			spread = spread.substring(1);
		}		
		
		if(spreadd == 0)
		{
			spread = "pk";
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

public Spreadline()
{
	
	type = "spread";
}
public Spreadline(int gid,int bid, double vs,double vj,double hs,double hj,java.sql.Timestamp ts,int p)
{
	this();

	currentvisitspread = priorvisitspread = openervisitspread = vs;
	currentvisitjuice = priorvisitjuice = openervisitjuice = vj;
	currenthomespread = priorhomespread = openerhomespread = hs;
	currenthomejuice = priorhomejuice = openerhomejuice = hj;
	currentts = priorts = openerts = ts;
	gameid = gid;
	bookieid = bid;
   period = p;
	
	
	
}


public Spreadline(int gid,int bid,double vs,double vj,double hs,double hj,java.sql.Timestamp ts,double pvs,double pvj,double phs,double phj,java.sql.Timestamp pts,int p)
{
	this();

	currentvisitspread = vs;
	currentvisitjuice = vj;
	currenthomespread = hs;
	currenthomejuice = hj;
	currentts = ts;
	
	priorvisitspread = pvs;
	priorvisitjuice = pvj;
	priorhomespread = phs;
	priorhomejuice = phj;
	priorts = pts;
	
	gameid = gid;
	bookieid = bid;
	period = p;

}

public Spreadline(int gid,int bid,double vs,double vj,double hs,double hj,java.sql.Timestamp ts,double pvs,double pvj,double phs,double phj,java.sql.Timestamp pts,double ovs,double ovj,double ohs,double ohj,java.sql.Timestamp ots,int p)
{
	this();

	currentvisitspread = vs;
	currentvisitjuice = vj;
	currenthomespread = hs;
	currenthomejuice = hj;
	currentts = ts;
	
	priorvisitspread = pvs;
	priorvisitjuice = pvj;
	priorhomespread = phs;
	priorhomejuice = phj;
	priorts = pts;
	
	openervisitspread = ovs;
	openervisitjuice = ovj;
	openerhomespread = ohs;
	openerhomejuice = ohj;
	openerts = ots;
		
	gameid = gid;
	bookieid = bid;
	period = p;
	


		
}


public boolean isOpener()
{
	
	if(priorvisitspread == 0 && priorvisitjuice == 0 && priorhomespread == 0 && priorhomejuice == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
}

public double getCurrentvisitspread()
{
	return currentvisitspread;
}
public void setCurrentvisitspread(double currentvisitspread)
{
	setPriorvisitspread(getCurrentvisitspread());
	this.currentvisitspread = currentvisitspread;
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

public double getCurrenthomespread()
{
	return currenthomespread;
}
public void setCurrenthomespread(double currenthomespread)
{
	setPriorhomespread(getCurrenthomespread());
	this.currenthomespread = currenthomespread;
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

public double getPriorvisitspread()
{
	return priorvisitspread;
}
public void setPriorvisitspread(double priorvisitspread)
{
	this.priorvisitspread = priorvisitspread;
}

public double getPriorvisitjuice()
{
	return priorvisitjuice;
}
public void setPriorvisitjuice(double priorvisitjuice)
{
	this.priorvisitjuice = priorvisitjuice;
}

public double getPriorhomespread()
{
	return priorhomespread;
}
public void setPriorhomespread(double priorhomespread)
{
	this.priorhomespread = priorhomespread;
}

public double getPriorhomejuice()
{
	return priorhomejuice;
}
public void setPriorhomejuice(double priorhomejuice)
{
	this.priorhomejuice = priorhomejuice;
}

public java.sql.Timestamp getOpenerts()
{
	return openerts;
}
public void setOpenerts(java.sql.Timestamp openerts)
{
	this.openerts = openerts;
}

public double getOpenervisitspread()
{
	return openervisitspread;
}
public void setOpenervisitspread(double openervisitspread)
{
	this.openervisitspread = openervisitspread;
}

public double getOpenervisitjuice()
{
	return openervisitjuice;
}
public void setOpenervisitjuice(double openervisitjuice)
{
	this.openervisitjuice = openervisitjuice;
}

public double getOpenerhomespread()
{
	return openerhomespread;
}
public void setOpenerhomespread(double openerhomespread)
{
	this.openerhomespread = openerhomespread;
}

public double getOpenerhomejuice()
{
	return openerhomejuice;
}
public void setOpenerhomejuice(double openerhomejuice)
{
	this.openerhomejuice = openerhomejuice;
}




}
