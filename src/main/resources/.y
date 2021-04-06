import java.sql.Timestamp;
import java.io.Serializable;

public class Spreadline extends Line implements Serializable
{


double currentvisitspread;
double currentvisitjuice;
double currenthomespread;
double currenthomejuice;
java.sql.Timestamp currentts;

double priorvisitspread;
double priorvisitjuice;
double priorhomespread;
double priorhomejuice;
java.sql.Timestamp priorts;

double openervisitspread;
double openervisitjuice;
double openerhomespread;
double openerhomejuice;
java.sql.Timestamp openerts;



public static void main(String args[])
{
	
	
	Spreadline sl = new Spreadline(109,29,5,-5,-110,-110,new java.sql.Timestamp(new java.util.Date().getTime()));
	System.out.println("hi "+sl.isOpener());
	System.out.println(sl.getPriorvisitspread());
}
public String recordMove(double visitspread,double visitjuice,double homespread,double homejuice,java.sql.Timestamp ts)
{
	
	this.setCurrentvisitspread(visitspread);
	this.setCurrentvisitjuice(visitjuice);
	this.setCurrenthomespread(homespread);
	this.setCurrenthomejuice(homejuice);
	this.setCurrentts(ts);
	
	if(getPriorvisitspread() < getCurrentvisitspread()) // -6 to -5  5 to 6
	{
		whowasbet = "h";
	}
	else if(getPriorvisitspread() > getCurrentvisitspread()) // -5 to -6 6 to 5
	{
		whowasbet = "v";
	}
	
	else //spreads equal
	{
		if(getPriorvisitjuice() < getCurrentvisitjuice()) //-110 to -105 , +105 to +110
		{
			whowasbet = "h";
		}
		else // priorjuice > currentjuice -105 to -110 110 to 105
		{
			whowasbet = "v";
		}
		
	}
	
	return whowasbet;
	
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
		
	

	public String getShortPrintedSpread(double vspread,double vjuice,double hspread,double hjuice)
	{


		String retvalue = "";
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
			String juicestr = "";
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
	char half = getAscii(171);
	retvalue = retvalue.replace(".5",half+"");
	return retvalue;
		
	}



public Spreadline()
{
	
	type = "spread";
}
public Spreadline(int gid,int bid, double vs,double vj,double hs,double hj,java.sql.Timestamp ts)
{
	this();
	currentvisitspread = priorvisitspread = openervisitspread = vs;
	currentvisitjuice = priorvisitjuice = openervisitjuice = vj;
	currenthomespread = priorhomespread = openerhomespread = hs;
	currenthomejuice = priorhomejuice = openerhomejuice = hj;
	currentts = priorts = openerts = ts;
	gameid = gid;
	bookieid = bid;
}


public Spreadline(int gid,int bid,double vs,double vj,double hs,double hj,java.sql.Timestamp ts,double pvs,double pvj,double phs,double phj,java.sql.Timestamp pts)
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
}

public Spreadline(int gid,int bid,double vs,double vj,double hs,double hj,java.sql.Timestamp ts,double pvs,double pvj,double phs,double phj,java.sql.Timestamp pts,double ovs,double ovj,double ohs,double ohj,java.sql.Timestamp ots)
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
