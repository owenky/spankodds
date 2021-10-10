package com.sia.client.model;

import com.sia.client.ui.LineAlertManager;

import static com.sia.client.config.Utils.log;


public class Spreadline extends Line {
    static Spreadline sl = new Spreadline(99999, 99999, 99999, 99999, 99999, 99999, 1L, 0);

    boolean isbestvisitspread = false;
    boolean isbesthomespread = false;

    double currentvisitspread;
    double currentvisitjuice;
    double currenthomespread;
    double currenthomejuice;
    private long currentts = 1000;

    double priorvisitspread;
    double priorvisitjuice;
    double priorhomespread;
    double priorhomejuice;

    double openervisitspread;
    double openervisitjuice;
    double openerhomespread;
    double openerhomejuice;

    public Spreadline(int gid, int bid, double vs, double vj, double hs, double hj, long ts, int p) {
        this();

        currentvisitspread = priorvisitspread = openervisitspread = vs;
        currentvisitjuice = priorvisitjuice = openervisitjuice = vj;
        currenthomespread = priorhomespread = openerhomespread = hs;
        currenthomejuice = priorhomejuice = openerhomejuice = hj;
        currentts = ts;
        gameid = gid;
        bookieid = bid;
        period = p;


    }

    public Spreadline() {

        type = "spread";
    }


    public Spreadline(int gid, int bid, double vs, double vj, double hs, double hj, long ts, double pvs, double pvj, double phs, double phj, long pts, int p) {
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

        gameid = gid;
        bookieid = bid;
        period = p;

    }

    public Spreadline(int gid, int bid, double vs, double vj, double hs, double hj, long ts, double pvs, double pvj, double phs, double phj, long pts, double ovs, double ovj, double ohs, double ohj, long ots, int p) {
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

        openervisitspread = ovs;
        openervisitjuice = ovj;
        openerhomespread = ohs;
        openerhomejuice = ohj;
        gameid = gid;
        bookieid = bid;
        period = p;


    }

    public static void main(String args[]) {


        Spreadline sl = new Spreadline(109, 29, 5, -5, -110, -110, System.currentTimeMillis(), 0);
        log("hi " + sl.isOpener());
//        log(sl.getPriorvisitspread());
    }

    public boolean isOpener() {

        return priorvisitspread == 0 && priorvisitjuice == 0 && priorhomespread == 0 && priorhomejuice == 0;
    }

    public double getPriorvisitspread() {
        return priorvisitspread;
    }

    public void setPriorvisitspread(double priorvisitspread) {
        this.priorvisitspread = priorvisitspread;
    }

    public boolean isBestVisitSpread() {
        return isbestvisitspread;
    }

    public void setBestVisitSpread(boolean b) {
        isbestvisitspread = b;
    }

    public boolean isBestHomeSpread() {
        return isbesthomespread;
    }

    public void setBestHomeSpread(boolean b) {
        isbesthomespread = b;
    }

    public String recordMove(double visitspread, double visitjuice, double homespread, double homejuice, long ts, boolean isopener) {

        if (visitjuice != 0) {
            this.setCurrentvisitspread(visitspread);
            this.setCurrentvisitjuice(visitjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenervisitspread(visitspread);
                this.setOpenervisitjuice(visitjuice);
            }
        }
        if (homejuice != 0) {
            this.setCurrenthomespread(homespread);
            this.setCurrenthomejuice(homejuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenerhomespread(homespread);
                this.setOpenerhomejuice(homejuice);
            }
        }
        try {
            if (this.getPriorvisitspread() < this.getCurrentvisitspread()) // -6 to -5  5 to 6
            {
                this.whowasbet = "h";
            } else if (this.getPriorvisitspread() > this.getCurrentvisitspread()) // -5 to -6 6 to 5
            {
                this.whowasbet = "v";
            } else //spreads equal
            {
                if (this.getPriorvisitjuice() < this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) //-110 to -105 , +105 to +110
                {
                    this.whowasbet = "h";
                } else if (this.getPriorvisitjuice() > this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) // priorjuice > currentjuice -105 to -110 110 to 105
                {
                    this.whowasbet = "v";
                } else {
                    //nochange!
                    this.whowasbet = "";
                }

            }
        } catch (Exception ex) {
            this.whowasbet = "";
            log(ex);
        }

        try {
            if (!this.whowasbet.equals("")) {
                LineAlertManager.checkMove(this);
            }
        } catch (Exception ex) {
            log(ex);
        }

        BestLines.calculatebestspread(gameid, period);
        return sl.whowasbet;


    }

    public double getCurrentvisitspread() {
        return currentvisitspread;
    }

    public void setCurrentvisitspread(double currentvisitspread) {
        setPriorvisitspread(getCurrentvisitspread());
        this.currentvisitspread = currentvisitspread;
    }

    public double getPriorvisitjuice() {
        return priorvisitjuice;
    }

    public double getCurrentvisitjuice() {
        return currentvisitjuice;
    }

    public void setCurrentvisitjuice(double currentvisitjuice) {
        setPriorvisitjuice(getCurrentvisitjuice());
        this.currentvisitjuice = currentvisitjuice;
    }

    public double getPriorhomejuice() {
        return priorhomejuice;
    }

    public double getCurrenthomejuice() {
        return currenthomejuice;
    }

    public void setCurrenthomejuice(double currenthomejuice) {
        setPriorhomejuice(getCurrenthomejuice());
        this.currenthomejuice = currenthomejuice;
    }

    public long getCurrentts() {
        return currentts;
    }

    public double getCurrenthomespread() {
        return currenthomespread;
    }

    public void setCurrenthomespread(double currenthomespread) {
        setPriorhomespread(getCurrenthomespread());
        this.currenthomespread = currenthomespread;
    }

    public void setCurrentts(long currentts) {
        this.currentts = currentts;
    }

    public void setPriorhomejuice(double priorhomejuice) {
        this.priorhomejuice = priorhomejuice;
    }

    public void setPriorvisitjuice(double priorvisitjuice) {
        this.priorvisitjuice = priorvisitjuice;
    }

    public String getShortPrintedCurrentSpread() {
        return getShortPrintedSpread(currentvisitspread, currentvisitjuice, currenthomespread, currenthomejuice);
    }

    public String getShortPrintedSpread(double vspread, double vjuice, double hspread, double hjuice) {


        String retvalue;
		if (vjuice == 0) {
			return "";
		}


        double spreadd = 0;
        double juice;
        if (vspread < hspread) {
            spreadd = vspread;
            juice = vjuice;
        } else if (vspread > hspread) {
            spreadd = hspread;
            juice = hjuice;
        } else // game is a pk
        {
            juice = min(vjuice, hjuice);

        }
        spreadd = spreadd * -1; // take out minus sign

        String spread = spreadd + "";
        if (spreadd < 1 && spreadd != 0 && spread.startsWith("0")) {
            spread = spread.substring(1);
        }


        if (spreadd == 0) {
            spread = "pk";
        }

        if (juice == -110) {
            retvalue = spread;

        } else if (juice < 0) {
            juice = juice + 100;
            String juicestr = juice + "";
            if (juice == 0) {
                retvalue = spread + "ev";
            } else if (juice > -10) {
                juice = juice * -1;
                juicestr = "-0" + juice;
                retvalue = spread + "" + juicestr;
            } else if (juice <= -100) // initial juice was  -200 or worse
            {

                juice = juice - 100;
                juicestr = "" + juice;
                retvalue = spread + "" + juicestr;
            } else {
                retvalue = spread + "" + juicestr;

            }

        } else {
            juice = juice - 100;

            if (juice == 0) {
                retvalue = spread + "ev";
            } else if (juice < 10) {
                retvalue = spread + "+0" + juice;
            } else {
                retvalue = spread + "+" + juice;

            }

        }

        retvalue = retvalue.replace(".0", "");
        if (retvalue.equals("0.5") || retvalue.equals("0.50")) {
            retvalue = ".5";
        }
        if (retvalue.equals("0.25") || retvalue.equals("0.250")) {
            retvalue = ".25";
        }
        if (retvalue.equals("0.75") || retvalue.equals("0.750")) {
            retvalue = ".75";
        }
        retvalue = retvalue.replace(".25", "\u00BC");
        retvalue = retvalue.replace(".5", "\u00BD");
        retvalue = retvalue.replace(".75", "\u00BE");
        return retvalue;

    }

    public String getShortPrintedPriorSpread() {
        return getShortPrintedSpread(priorvisitspread, priorvisitjuice, priorhomespread, priorhomejuice);
    }

    public String getShortPrintedOpenerSpread() {
        return getShortPrintedSpread(openervisitspread, openervisitjuice, openerhomespread, openerhomejuice);
    }

    public String getOtherPrintedCurrentSpread() {
        return getOtherPrintedSpread(currentvisitspread, currentvisitjuice, currenthomespread, currenthomejuice);
    }

    public String getOtherPrintedSpread(double vspread, double vjuice, double hspread, double hjuice) {


        String retvalue;
		if (vjuice == 0) {
			return "";
		}
        double spreadd = 0;
        double juice;
        if (vspread < hspread) {
            spreadd = hspread;
            juice = hjuice;
        } else if (vspread > hspread) {
            spreadd = vspread;
            juice = vjuice;
        } else // game is a pk
        {
            juice = max(vjuice, hjuice);

        }


        String spread = spreadd + "";
        if (spreadd < 1 && spreadd != 0 && spread.startsWith("0")) {
            spread = spread.substring(1);
        }

        if (spreadd == 0) {
            spread = "pk";
        }

        if (juice == -110) {
            retvalue = spread;

        } else if (juice < 0) {
            juice = juice + 100;
            String juicestr = juice + "";
            if (juice == 0) {
                retvalue = spread + "ev";
            } else if (juice > -10) {
                juice = juice * -1;
                juicestr = "-0" + juice;
                retvalue = spread + "" + juicestr;
            } else if (juice <= -100) // initial juice was  -200 or worse
            {

                juice = juice - 100;
                juicestr = "" + juice;
                retvalue = spread + "" + juicestr;
            } else {
                retvalue = spread + "" + juicestr;

            }

        } else {
            juice = juice - 100;

            if (juice == 0) {
                retvalue = spread + "ev";
            } else if (juice < 10) {
                retvalue = spread + "+0" + juice;
            } else {
                retvalue = spread + "+" + juice;

            }

        }

        retvalue = retvalue.replace(".0", "");

        if (retvalue.equals("0.5") || retvalue.equals("0.50")) {
            retvalue = ".5";
        }
        if (retvalue.equals("0.25") || retvalue.equals("0.250")) {
            retvalue = ".25";
        }
        if (retvalue.equals("0.75") || retvalue.equals("0.750")) {
            retvalue = ".75";
        }
        retvalue = retvalue.replace(".25", "\u00BC");
        retvalue = retvalue.replace(".5", "\u00BD");
        retvalue = retvalue.replace(".75", "\u00BE");
        return retvalue;

    }

    public String getOtherPrintedPriorSpread() {
        return getOtherPrintedSpread(priorvisitspread, priorvisitjuice, priorhomespread, priorhomejuice);
    }

    public String getOtherPrintedOpenerSpread() {
        return getOtherPrintedSpread(openervisitspread, openervisitjuice, openerhomespread, openerhomejuice);
    }

    public double getPriorhomespread() {
        return priorhomespread;
    }

    public void setPriorhomespread(double priorhomespread) {
        this.priorhomespread = priorhomespread;
    }

    public double getOpenervisitspread() {
        return openervisitspread;
    }

    public void setOpenervisitspread(double openervisitspread) {
        this.openervisitspread = openervisitspread;
    }

    public double getOpenervisitjuice() {
        return openervisitjuice;
    }

    public void setOpenervisitjuice(double openervisitjuice) {
        this.openervisitjuice = openervisitjuice;
    }

    public double getOpenerhomespread() {
        return openerhomespread;
    }

    public void setOpenerhomespread(double openerhomespread) {
        this.openerhomespread = openerhomespread;
    }

    public double getOpenerhomejuice() {
        return openerhomejuice;
    }

    public void setOpenerhomejuice(double openerhomejuice) {
        this.openerhomejuice = openerhomejuice;
    }


}
