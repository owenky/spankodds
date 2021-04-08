import java.sql.Timestamp;
import java.io.Serializable;



public class Totalline extends Line implements Serializable
{

static Totalline tl = new Totalline(99999,99999,99999,99999,99999,99999,new Timestamp(1),0);



boolean isbestover = false;
boolean isbestunder = false;
double currentover;
double currentoverjuice;
double currentunder;
double currentunderjuice;
java.sql.Timestamp currentts = new java.sql.Timestamp(1000);


double priorover;
double prioroverjuice;
double priorunder;
double priorunderjuice;
java.sql.Timestamp priorts = new java.sql.Timestamp(1000);


double openerover;
double openeroverjuice;
double openerunder;
double openerunderjuice;
java.sql.Timestamp openerts = new java.sql.Timestamp(1000);


public boolean isBestOver()
{
	return isbestover;
}


public boolean isBestUnder()
{
	return isbestunder;
}


public void setBestOver(boolean b)
{
	isbestover = b;
}
public void setBestUnder(boolean b)
{
	isbestunder = b;
}


public Totalline()
{
	type = "total";
}

public Totalline(int gid, int bid,double o,double oj,double u,double uj,java.sql.Timestamp ts,int p)
{
	this();
	
	currentover = priorover = openerover = o;
	currentoverjuice = prioroverjuice = openeroverjuice = oj;
	currentunder = priorunder = openerunder = u;
	currentunderjuice = priorunderjuice = openerunderjuice = uj;
	currentts = priorts = openerts = ts;
	gameid = gid;
	bookieid = bid;
	period = p;


	
}

public Totalline(int gid, int bid, double o,double oj,double u,double uj,java.sql.Timestamp ts,double po,double poj,double pu,double puj,java.sql.Timestamp pts,int p)
{
	this();
	currentover = o;
	currentoverjuice = oj;
	currentunder = u;
	currentunderjuice = uj;
	currentts = ts;
	
	priorover = o;
	prioroverjuice = oj;
	priorunder = u;
	priorunderjuice = uj;
	priorts = ts;
	
	gameid = gid;
	bookieid = bid;
	period = p;

		
}


public Totalline(int gid, int bid, double o,double oj,double u,double uj,java.sql.Timestamp ts,double po,double poj,double pu,double puj,java.sql.Timestamp pts,double oo,double ooj,double ou,double ouj,java.sql.Timestamp ots,int p)
{
	this();
	currentover = o;
	currentoverjuice = oj;
	currentunder = u;
	currentunderjuice = uj;
	currentts = ts;
	
	priorover = po;
	prioroverjuice = poj;
	priorunder = pu;
	priorunderjuice = puj;
	priorts = pts;
	
	openerover = oo;
	openeroverjuice = ooj;
	openerunder = ou;
	openerunderjuice = ouj;
	openerts = ots;
	
	gameid = gid;
	bookieid = bid;
	period = p;

		
}

public String recordMove(double over,double overjuice,double under,double underjuice,java.sql.Timestamp ts,boolean isopener)
{
		
	if(overjuice != 0)
	{
		this.setCurrentover(over);
		this.setCurrentoverjuice(overjuice);
		this.setCurrentts(ts);
		

	
		if(isopener)
		{
			this.setOpenerover(over);
			this.setOpeneroverjuice(overjuice);
			this.setOpenerts(ts);
		}
	}
	if(underjuice != 0)
	{	
		this.setCurrentunder(under);
		this.setCurrentunderjuice(underjuice);
		this.setCurrentts(ts);
	
		if(isopener)
		{
			this.setOpenerunder(under);
			this.setOpenerunderjuice(underjuice);
			this.setOpenerts(ts);
		}
	}	
	
	try
	{
			if(this.getPriorover() < this.getCurrentover()) // 215 to 216
			{
				this.whowasbet = "o";
			}
			else if(this.getPriorover() > this.getCurrentover()) // 216 to 215
			{
				this.whowasbet = "u";
			}
			
			else //totals equal
			{
				if(this.getPrioroverjuice() < this.getCurrentoverjuice() && this.getPriorunderjuice() != this.getCurrentunderjuice()) //-110 to -105 , +105 to +110
				{
					this.whowasbet = "u";
				}
				else if(this.getPrioroverjuice() > this.getCurrentoverjuice() && this.getPriorunderjuice() != this.getCurrentunderjuice())// priorjuice > currentjuice -105 to -110 110 to 105
				{
					this.whowasbet = "o";
				}
				else
				{
					//System.out.println("NO CHANGE!");
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
		System.out.println("exception checking total for linealert! "+ex);
	}
	
	
	BestLines.calculatebesttotal(gameid,period);							
	return this.whowasbet;
	
}

public String getShortPrintedCurrentTotal()
{
	
	return getShortPrintedTotal(currentover,currentoverjuice,currentunder,currentunderjuice);
}

public String getShortPrintedPriorTotal()
{
	
	return getShortPrintedTotal(priorover,prioroverjuice,priorunder,priorunderjuice);
}

public String getShortPrintedOpenerTotal()
{
	
	return getShortPrintedTotal(openerover,openeroverjuice,openerunder,openerunderjuice);
}
public String getOtherPrintedCurrentTotal()
{
	
	return getOtherPrintedTotal(currentover,currentoverjuice,currentunder,currentunderjuice);
}

public String getOtherPrintedPriorTotal()
{
	
	return getOtherPrintedTotal(priorover,prioroverjuice,priorunder,priorunderjuice);
}

public String getOtherPrintedOpenerTotal()
{
	
	return getOtherPrintedTotal(openerover,openeroverjuice,openerunder,openerunderjuice);
}

public String getShortPrintedTotal(double o,double oj,double u,double uj)
	{
		
		String retvalue = o+"";
		if(oj == 0) return "";		
		if(Math.abs(o) < 1 && retvalue.startsWith("0"))
		{
			retvalue = retvalue.substring(1);
		}					
			
			
			double juice = 0;
			if(oj == uj && oj == -110)
			{
				
				retvalue = retvalue.replace(".0","");
				char half = AsciiChar.getAscii(170);

				
				retvalue = retvalue.replace(".25","\u00BC");			
				retvalue = retvalue.replace(".5","\u00BD");			
				retvalue = retvalue.replace(".75","\u00BE");				
				return retvalue;
			}
			else if(oj < uj)
			{
				retvalue = retvalue+"o";
				juice = oj;
			}
			else
			{
				retvalue = retvalue+"u";
				juice = uj;
			}
			
			
				
			if(juice < 0)
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

public String getOtherPrintedTotal(double o,double oj,double u,double uj)
	{
		String retvalue = o+"";
		if(oj == 0) return "";		
		if(Math.abs(o) < 1 && retvalue.startsWith("0"))
		{
			retvalue = retvalue.substring(1);
		}

		

			double juice = 0;
			if(oj == uj && oj == -110)
			{
				
				retvalue = retvalue.replace(".0","");
				char half = AsciiChar.getAscii(170);

				
				retvalue = retvalue.replace(".25","\u00BC");			
				retvalue = retvalue.replace(".5","\u00BD");			
				retvalue = retvalue.replace(".75","\u00BE");			
				return retvalue;
			}
			else if(oj < uj)
			{
				retvalue = retvalue+"u";
				juice = uj;
			}
			else
			{
				retvalue = retvalue+"o";
				juice = oj;
			}
			
			
				
			if(juice < 0)
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

public boolean isOpener()
{
	
	if(priorover == 0 && prioroverjuice == 0 && priorunder == 0 && priorunderjuice == 0)
	{
		return true;
	}
	else
	{
		return false;
	}
}
public double getCurrentover()
{
	return currentover;
}
public void setCurrentover(double currentover)
{
	setPriorover(getCurrentover());
	this.currentover = currentover;
}

public double getCurrentoverjuice()
{
	return currentoverjuice;
}
public void setCurrentoverjuice(double currentoverjuice)
{
	setPrioroverjuice(getCurrentoverjuice());
	this.currentoverjuice = currentoverjuice;
}

public double getCurrentunder()
{
	return currentunder;
}
public void setCurrentunder(double currentunder)
{
	setPriorunder(getCurrentunder());
	this.currentunder = currentunder;
}

public double getCurrentunderjuice()
{
	return currentunderjuice;
}
public void setCurrentunderjuice(double currentunderjuice)
{
	setPriorunderjuice(getCurrentunderjuice());
	this.currentunderjuice = currentunderjuice;
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

public double getPriorover()
{
	return priorover;
}
public void setPriorover(double priorover)
{
	this.priorover = priorover;
}

public double getPrioroverjuice()
{
	return prioroverjuice;
}
public void setPrioroverjuice(double prioroverjuice)
{
	this.prioroverjuice = prioroverjuice;
}

public double getPriorunder()
{
	return priorunder;
}
public void setPriorunder(double priorunder)
{
	this.priorunder = priorunder;
}

public double getPriorunderjuice()
{
	return priorunderjuice;
}
public void setPriorunderjuice(double priorunderjuice)
{
	this.priorunderjuice = priorunderjuice;
}

public java.sql.Timestamp getOpenerts()
{
	return openerts;
}
public void setOpenerts(java.sql.Timestamp openerts)
{
	this.openerts = openerts;
}

public double getOpenerover()
{
	return openerover;
}
public void setOpenerover(double openerover)
{
	this.openerover = openerover;
}

public double getOpeneroverjuice()
{
	return openeroverjuice;
}
public void setOpeneroverjuice(double openeroverjuice)
{
	this.openeroverjuice = openeroverjuice;
}

public double getOpenerunder()
{
	return openerunder;
}
public void setOpenerunder(double openerunder)
{
	this.openerunder = openerunder;
}

public double getOpenerunderjuice()
{
	return openerunderjuice;
}
public void setOpenerunderjuice(double openerunderjuice)
{
	this.openerunderjuice = openerunderjuice;
}

}