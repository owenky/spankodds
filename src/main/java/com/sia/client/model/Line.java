package com.sia.client.model;

import com.sia.client.ui.AppController;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.sia.client.config.Utils.log;

public class Line {

    private static final Integer [] testGameArr = {}; //{208453,209273,219133,200201,200205,200209,200213,200217,200221};
    public static final Set<Integer> testGameIds = new HashSet<>(Arrays.asList(testGameArr));

    protected int bookieid;
	protected int gameid;
	protected int period;
    protected int limit;
	protected String type;
	public String whowasbet = "";
	protected Timestamp priorts = new Timestamp(1000);
	protected Timestamp currentts = new Timestamp(1000);
	protected Timestamp  openerts = new Timestamp(1000);

    //public String getPrintedSpreadLine(double ml)
//{
//	String retvalue = "";
//	if(ml >0)
//	{
//		retvalue =  "+"+ml;
//	}
//	else if(ml <0)
//	{
//		retvalue = ml+"";
//	}
//	else if(ml == 0)
//	{
//		retvalue =  "pk";
//	}
//	else
//	{
//		retvalue = "";
//	}
//	retvalue = retvalue.replace(".0","");
//	char half = AsciiChar.getAscii(170);
//	retvalue = retvalue.replace(".5",half+"");
//	return retvalue;
//}
    public String getPrintedJuiceLine(double ml) {
        String retvalue = "";
        if (ml > 0) {
            retvalue = "+" + ml;
        } else if (ml < 0) {
            retvalue = ml + "";
        } else {
            retvalue = "";
        }
        retvalue = retvalue.replace(".0", "");
        return retvalue;

    }


    public double min(double d1, double d2) {
        return Math.min(d1, d2);
    }
	public double max(double d1, double d2) {
        return Math.max(d1, d2);
    }

    public String getType() {
        return type;
    }
	public void setType(final String type) {
		this.type = type;
	}
    public String getWhowasbet() {
        return whowasbet;
    }

    public void setWhowasbet(String w) {
        whowasbet = w;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getBookieid() {
        return bookieid;
    }

    public void setBookieid(int bookieid) {
        this.bookieid = bookieid;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getVisitorTeam() {
        return AppController.getGame(gameid).getVisitorteam();
    }

    public String getHomeTeam() {
        return AppController.getGame(gameid).getHometeam();
    }

    public final String getGame() {
        Game g = AppController.getGame(gameid);
        return g.getVisitorteam() + "@" + g.getHometeam();
    }
	public final Timestamp getCurrentts() {
		return currentts;
	}
	public final void setCurrentts(Timestamp currentts) {
        this.priorts = this.currentts;
		this.currentts = currentts;
if ( testGameIds.contains(gameid)) {
			log("gameid="+gameid+", current="+currentts+", value="+currentts.getTime())       ;
}
	}
	public final void setOpenerts(Timestamp openerts) {
		this.openerts = openerts;
	}
}