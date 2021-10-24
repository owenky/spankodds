package com.sia.client.model;

import com.sia.client.ui.AppController;

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
	public final long getCurrentts() {
		return currentts;
	}
	public final void setCurrentts(long currentts) {
        this.priorts = this.currentts;
		this.currentts = currentts;
	}
	public final void setOpenerts(long openerts) {
		this.openerts = openerts;
	}
}