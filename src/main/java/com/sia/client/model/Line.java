package com.sia.client.model;

import com.sia.client.ui.AppController;
import com.sia.client.ui.LimitAlertManager;

import java.text.SimpleDateFormat;

public class Line {

    private final LineIdentity lineIdentity;
    private final String type;
    protected int limit;
    public String whowasbet = "";
    protected long priorts = 1000;
    protected long currentts = 1000;
    protected long openerts = 1000;
    protected int leagueid = 0;
    static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd HH:mm:ss");
    protected boolean scalp = false;

    public Line(LineIdentity lineIdentity,String type) {
        this.lineIdentity = lineIdentity;
        this.type = type;
    }
    public String formatts(long ts) {
        if (ts == 1000) {
            return "";
        } else {
            String s = sdf3.format(new java.util.Date(ts));
            return s;
        }
    }
    public LineIdentity getLineIdentity() {
        return lineIdentity;
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
    public String getWhowasbet() {
        return whowasbet;
    }

    public void setWhowasbet(String w) {
        whowasbet = w;
    }

    public int getGameid() {
        return lineIdentity.getGameId();
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int newlimit) {
        int oldlimit = this.limit;
        this.limit = newlimit;
        Game g = getGameObject();

        if (oldlimit == newlimit) {
            return;
        } else if (g.isIngame() || !g.getStatus().equals("")) {
            return;
        } else if (oldlimit == 0 || newlimit == 0) {
            return;
        }

        // if we get here lets see if its worthy of an alert
        LimitAlertManager.limitChangeAlert(lineIdentity.getGameId(), lineIdentity.getBookieId(), lineIdentity.getPeriod(), oldlimit, newlimit, this);
    }
    public int getBookieid() {
        return lineIdentity.getBookieId();
    }
    public int getPeriod() {
        return lineIdentity.getPeriod();
    }


    public String getVisitorTeam() {
        return AppController.getGame(lineIdentity.getGameId()).getVisitorteam();
    }

    public String getHomeTeam() {
        return AppController.getGame(lineIdentity.getGameId()).getHometeam();
    }

    public final String getGame() {
        Game g = AppController.getGame(lineIdentity.getGameId());
        return g.getVisitorteam() + "@" + g.getHometeam();
    }

    public Game getGameObject() {
        Game g = AppController.getGame(lineIdentity.getGameId());
        return g;
    }

    public Bookie getBookieObject() {
        Bookie b = AppController.getBookie(lineIdentity.getBookieId());
        return b;
    }

    public int getLeague_id() {
        if (leagueid == 0) // look up
        {
            Game g = AppController.getGame(lineIdentity.getGameId());
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

    public String getOpener() {
        return "";
    }
}