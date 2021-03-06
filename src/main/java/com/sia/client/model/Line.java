package com.sia.client.model;

import com.sia.client.ui.AppController;
import com.sia.client.ui.LimitAlertManager;
import com.sia.client.ui.UrgentMessage;

import java.text.SimpleDateFormat;

public class Line {
    protected int bookieid;
	protected int gameid;
	protected int period;
    protected int limit;
	protected String type;
	public String whowasbet = "";
	protected long priorts = 1000;
	protected long currentts = 1000;
	protected long  openerts = 1000;
	protected int leagueid = 0;
    static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd HH:mm:ss");
	protected boolean scalp = false;

	public String formatts(long ts)
    {
        if(ts == 1000)
        {
            return "";
        }
        else
        {
          String s = sdf3.format(new java.util.Date(ts));
          return s;
        }
    }

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

    public void setLimit(int newlimit)
    {
        int oldlimit = this.limit;
        this.limit = newlimit;
        Game g = getGameObject();

        if(oldlimit == newlimit)
        {
            return;
        }
        else if(g.isIngame() || !g.getStatus().equals("") )
        {
            return;
        }
        else if(oldlimit == 0 || newlimit == 0)
        {
            return;
        }

        // if we get here lets see if its worthy of an alert
        LimitAlertManager.limitChangeAlert(gameid,bookieid,period,oldlimit,newlimit,this);

/*
        String message = "";


            if(oldlimit > newlimit)
            {
                message = "Decreased from "+oldlimit+" to "+newlimit;
            }
            else
            {
                message = "Increased from "+oldlimit+" to "+newlimit;
            }
        */
// need to apply filter here before alerting
        /*
        new UrgentMessage("<HTML><H2>"+AppController.getBookie(getBookieid()).getName()+" - "+type.toUpperCase()+" LIMIT CHANGE " + getLeague_id() + "</H2>" +
                "<TABLE cellspacing=1 cellpadding=1>" +

                "<TR><TD>" + getGameid() + "-"+getPeriod()+"</TD></TR>" +
                "<TR><TD>" + message+"</TD></TR>" +
                "</TABLE></HTML>", 40 * 1000,2, AppController.getMainTabPane());
        */

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
    public Game getGameObject() {
        Game g = AppController.getGame(gameid);
        return g;
    }

    public Bookie getBookieObject() {
        Bookie b = AppController.getBookie(bookieid);
        return b;
    }
    public int getLeague_id() {
        if(leagueid == 0) // look up
        {
            Game g = AppController.getGame(gameid);
            leagueid = g.getLeague_id();

        }
        return leagueid;
    }
	public final long getCurrentts() {
		return currentts;
	}
    public final long getPriorts() {
        return priorts;
    }
    public final long getOpenerts() {
        return openerts;
    }
	public final void setCurrentts(long currentts) {
        this.priorts = this.currentts;
		this.currentts = currentts;
	}
	public final void setOpenerts(long openerts) {
		this.openerts = openerts;
	}
	public String getOpener() { return "";}
}