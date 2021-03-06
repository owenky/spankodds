package com.sia.client.model;


import com.sia.client.ui.AppController;
import com.sia.client.ui.AsciiChar;
import com.sia.client.ui.LineAlertManager;
import com.sia.client.ui.LineAlertOpenerManager;

import static com.sia.client.config.Utils.log;


public class Spreadline extends Line {
    static Spreadline sl = new Spreadline(99999, 99999, 99999, 99999, 99999, 99999, 1L, 0);

    boolean isbestvisitspread = false;
    boolean isbesthomespread = false;
    double currentvisitspread;
    double currentvisitjuice;
    double currenthomespread;
    double currenthomejuice;
    double priorvisitspread;
    double priorvisitjuice;
    double priorhomespread;
    double priorhomejuice;
    double openervisitspread;
    double openervisitjuice;
    double openerhomespread;
    double openerhomejuice;
    private String shortPrintedSpread_current = null;
    private String shortPrintedSpread_prior = null;
    private String shortPrintedSpread_opener = null;
    private String otherPrintedSpread_current = null;
    private String otherPrintedSpread_prior = null;
    private String otherPrintedSpread_opener = null;

    public Spreadline(int gid, int bid, double vs, double vj, double hs, double hj, long ts, int p) {
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
        priorts = pts;

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
    public Spreadline(int gid, int bid, double vs, double vj, double hs, double hj, long ts, double pvs, double pvj, double phs, double phj, long pts, double ovs, double ovj, double ohs, double ohj, long ots, int p,int mb) {
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
        limit  = mb;

    }
    public static void main(String args[]) {
//
//
//        Spreadline sl = new Spreadline(109, 29, 5, -5, -110, -110, System.currentTimeMillis(), 0);
//        log("hi " + sl.isOpener());
//        log(sl.getPriorvisitspread());
    }

    public boolean isOpener() {

        if (priorvisitspread == 0 && priorvisitjuice == 0 && priorhomespread == 0 && priorhomejuice == 0) {
            return true;
        } else {
            return false;
        }
    }

    public double getPriorvisitspread() {
        return priorvisitspread;
    }

    public void setPriorvisitspread(double priorvisitspread) {
        this.priorvisitspread = priorvisitspread;
        otherPrintedSpread_prior = null;
        shortPrintedSpread_prior = null;
    }

    public boolean isBestVisitSpread() {
        return isbestvisitspread;
    }

    public void setBestVisitSpread(boolean b)
    {
        isbestvisitspread = b;
        if(b)
        {
            AppController.bestvisitspread.put(period+"-"+gameid,getBookieObject());
        }
    }

    public boolean isBestHomeSpread() {
        return isbesthomespread;
    }

    public void setBestHomeSpread(boolean b)
    {
        isbesthomespread = b;
        if(b)
        {
            AppController.besthomespread.put(period+"-"+gameid,getBookieObject());
        }
    }

    public String recordMove(double visitspread, double visitjuice, double homespread, double homejuice, long ts, boolean isopener) {

        if (isopener)
        {
            this.setOpenervisitspread(visitspread);
            this.setOpenervisitjuice(visitjuice);
            this.setOpenerhomespread(homespread);
            this.setOpenerhomejuice(homejuice);
            this.setOpenerts(ts);

        }
        else if(gameid < 10000) // if this is a half move i will throw away
        {
            if( (visitspread == this.getCurrentvisitspread() && visitjuice == this.getCurrentvisitjuice())
            ||  (homespread == this.getCurrenthomespread() && homejuice == this.getCurrenthomejuice())
            )
            {
               // log("Throw out half spread=" + gameid + "..bookie=" +this.getBookieObject()+".."+visitspread+visitjuice+"/"+homespread+homejuice+"/ vs."+this.getCurrentvisitspread()+"/"+this.getCurrentvisitjuice()+"/"+this.getCurrenthomespread()+"/"+this.getCurrenthomejuice());
               // return "";
            }

        }


            this.setCurrentvisitspread(visitspread);
            this.setCurrentvisitjuice(visitjuice);
        this.setCurrenthomespread(homespread);
        this.setCurrenthomejuice(homejuice);
            this.setCurrentts(ts);



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

        if (isopener)
        {
            LineAlertOpenerManager.openerAlert(this.getGameid(),this.getBookieid(),this.getPeriod(), this);
        }


        try {
            if (!this.whowasbet.equals("")) {
                LineAlertManager.checkMove(this);
            }
        } catch (Exception ex) {
            log(ex);
        }

        BestLines.calculatebestspread(gameid, period);
        BestLines.calculateconsensusspread(gameid, period);
        return sl.whowasbet;


    }

    public double getCurrentvisitspread() {
        return currentvisitspread;
    }

    public void setCurrentvisitspread(double currentvisitspread) {
        setPriorvisitspread(getCurrentvisitspread());
        this.currentvisitspread = currentvisitspread;
        otherPrintedSpread_current = null;
        shortPrintedSpread_current = null;
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
        otherPrintedSpread_current = null;
        shortPrintedSpread_current = null;
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
        otherPrintedSpread_current = null;
        shortPrintedSpread_current = null;
    }
    public double getCurrenthomespread() {
        return currenthomespread;
    }

    public void setCurrenthomespread(double currenthomespread) {
        setPriorhomespread(getCurrenthomespread());
        this.currenthomespread = currenthomespread;
        otherPrintedSpread_current = null;
        shortPrintedSpread_current = null;
    }



    public void setPriorhomejuice(double priorhomejuice) {
        this.priorhomejuice = priorhomejuice;
        otherPrintedSpread_prior = null;
        shortPrintedSpread_prior = null;
    }

    public void setPriorvisitjuice(double priorvisitjuice) {
        this.priorvisitjuice = priorvisitjuice;
        otherPrintedSpread_prior = null;
        shortPrintedSpread_prior = null;
    }

    public String getShortPrintedCurrentSpread() {
        if ( null == shortPrintedSpread_current) {
            shortPrintedSpread_current = getShortPrintedSpread(currentvisitspread, currentvisitjuice, currenthomespread, currenthomejuice);
        }
        return shortPrintedSpread_current;
    }

    private String getShortPrintedSpread(double vspread, double vjuice, double hspread, double hjuice) {
        boolean debug = false;
        if(period == 0 && gameid == 951 && bookieid==599 && debug)
        {
            System.out.println(bookieid+"...leagueid="+leagueid+"...SHORTPRINTEDSPREAD="+vspread+".."+vjuice+".vs."+hspread+".."+hjuice);
        }
        String retvalue = "";
		if (vjuice == 0) {
			return "";
		}


        double spreadd = 0;
        double juice = 0;
        if(vspread > 0 && hspread >0)
        {
            spreadd = vspread;
            juice = vjuice;
        }
        else if (vspread < hspread) {
            spreadd = vspread;
            juice = vjuice;
        } else if (vspread > hspread) {
            spreadd = hspread;
            juice = hjuice;
        } else if(vspread == hspread && vspread == 0)// game is a pk
        {
            juice = min(vjuice, hjuice);
        }
        else // two positives most like two +.5
        {
            spreadd = vspread;
            juice = vjuice;

        }
        if(spreadd < 0)
        {
            spreadd = spreadd * -1; // take out minus sign
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

        }
        else if (juice < 0)
        {
            juice = juice + 100;
            String juicestr = juice + "";
            if (juice == 0) {
                retvalue = spread + "ev";
            }
            else if (juice > -10) {
                juice = juice * -1;
                juicestr = "-0" + juice;
                retvalue = spread + "" + juicestr;
            }
            else if (juice <= -100) // initial juice was  -200 or worse
            {

                juice = juice - 100;
                juicestr = "" + juice;
                retvalue = spread + "" + juicestr;
            }
            else {
                retvalue = spread + "" + juicestr;

            }

        } else
            {
            juice = juice - 100;

            if (juice == 0)
            {
                retvalue = spread + "ev";
            } else if (juice < 10)
            {
                retvalue = spread + "+0" + juice;
            }
            else if(juice >= 100) // even after conversion then addback
            {
                juice = juice+100;
                retvalue = spread + "+" + juice;

            }
            else
                {
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
        char half = AsciiChar.getAscii(170);

        if(period == 0 && gameid == 951 && debug && bookieid==599)
        {
            System.out.println("retvalue="+retvalue);
        }


        retvalue = retvalue.replace(".5", "\u00BD");
        if(AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }
        return retvalue;

    }

    public String getShortPrintedPriorSpread() {
        if ( null == shortPrintedSpread_prior) {
            shortPrintedSpread_prior = getShortPrintedSpread(priorvisitspread, priorvisitjuice, priorhomespread, priorhomejuice);
        }
        return shortPrintedSpread_prior;
    }

    public String getShortPrintedOpenerSpread() {
        if ( null == shortPrintedSpread_opener) {
            shortPrintedSpread_opener = getShortPrintedSpread(openervisitspread, openervisitjuice, openerhomespread, openerhomejuice);
        }
        return shortPrintedSpread_opener;
    }

    public String getOtherPrintedCurrentSpread() {
        if ( null == otherPrintedSpread_current) {
            otherPrintedSpread_current = getOtherPrintedSpread(currentvisitspread, currentvisitjuice, currenthomespread, currenthomejuice);
        }
        return otherPrintedSpread_current;
    }

    private String getOtherPrintedSpread(double vspread, double vjuice, double hspread, double hjuice) {
        boolean debug = false;
        if(period == 0 && gameid == 951 && bookieid==599 && debug)
        {
            System.out.println(bookieid+"OTHERPRINTEDSPREAD="+vspread+".."+vjuice+".vs."+hspread+".."+hjuice);
        }
        String retvalue = "";
		if (vjuice == 0) {
			return "";
		}



        double spreadd = 0;
        double juice = 0;
        if(vspread > 0 && hspread >0)
        {
            spreadd = hspread;
            juice = hjuice;
        }

        else if (vspread < hspread) {
            spreadd = hspread;
            juice = hjuice;
        } else if (vspread > hspread) {
            spreadd = vspread;
            juice = vjuice;

        }
        else if(vspread == hspread && vspread == 0)// game is a pk
        {
            juice = max(vjuice, hjuice);
        }
        else // two positives or two negatives  most likely two +.5 for best column
        {
            spreadd = hspread;
            juice = hjuice;

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
            }
            else if(juice >= 100) // even after conversion then addback
            {
                juice = juice+100;
                retvalue = spread + "+" + juice;

            }
            else {
                retvalue = spread + "+" + juice;

            }

        }

        retvalue = retvalue.replace(".0", "");

        char half = AsciiChar.getAscii(170);
        if (retvalue.equals("0.5") || retvalue.equals("0.50")) {
            retvalue = ".5";
        }
        if (retvalue.equals("0.25") || retvalue.equals("0.250")) {
            retvalue = ".25";
        }
        if (retvalue.equals("0.75") || retvalue.equals("0.750")) {
            retvalue = ".75";
        }
        if(period == 0 && gameid == 951 && bookieid==599 && debug)
        {
            System.out.println("retvalue="+retvalue);
        }

        retvalue = retvalue.replace(".5", "\u00BD");
        if(AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }

        return retvalue;

    }

    public String getOtherPrintedPriorSpread() {
        if ( null == otherPrintedSpread_prior) {
            otherPrintedSpread_prior = getOtherPrintedSpread(priorvisitspread, priorvisitjuice, priorhomespread, priorhomejuice);
        }
        return otherPrintedSpread_prior;
    }

    public String getOtherPrintedOpenerSpread() {
        if ( null == otherPrintedSpread_opener) {
            otherPrintedSpread_opener = getOtherPrintedSpread(openervisitspread, openervisitjuice, openerhomespread, openerhomejuice);
        }
        return otherPrintedSpread_opener;
    }
    public double getPriorhomespread() {
        return priorhomespread;
    }

    public void setPriorhomespread(double priorhomespread) {
        this.priorhomespread = priorhomespread;
        otherPrintedSpread_prior = null;
        shortPrintedSpread_prior = null;
    }



    public double getOpenervisitspread() {
        return openervisitspread;
    }

    public void setOpenervisitspread(double openervisitspread) {
        this.openervisitspread = openervisitspread;
        otherPrintedSpread_opener = null;
        shortPrintedSpread_opener = null;
    }

    public double getOpenervisitjuice() {
        return openervisitjuice;
    }

    public void setOpenervisitjuice(double openervisitjuice) {
        this.openervisitjuice = openervisitjuice;
        otherPrintedSpread_opener = null;
        shortPrintedSpread_opener = null;
    }

    public double getOpenerhomespread() {
        return openerhomespread;
    }

    public void setOpenerhomespread(double openerhomespread) {
        this.openerhomespread = openerhomespread;
        otherPrintedSpread_opener = null;
        shortPrintedSpread_opener = null;
    }

    public double getOpenerhomejuice() {
        return openerhomejuice;
    }

    public void setOpenerhomejuice(double openerhomejuice) {
        this.openerhomejuice = openerhomejuice;
        otherPrintedSpread_opener = null;
        shortPrintedSpread_opener = null;
    }

    @Override
    public String getOpener()
    {
        String s;
        s = ""+getOpenervisitspread()+getOpenervisitjuice()+"<br>"+getOpenerhomespread()+getOpenerhomejuice();
        return s;
    }

    public String showHistory()
    {
        try {
            String s =
                    "<tr><td>C:</td><td>" + getShortPrintedCurrentSpread() + "</td><td>" + formatts(getCurrentts()) + "</td></tr>" +
                    "<tr><td>P:</td><td>" + getShortPrintedPriorSpread() + "</td><td>" + formatts(getPriorts()) + "</td></tr>" +
                    "<tr><td>O:</td><td>" + getShortPrintedOpenerSpread() + "</td><td>" + formatts(getOpenerts()) + "</td></tr>";

            return s;
        }
        catch(Exception ex)
        {
            log("spread show history exception "+ex);
        }
        return "";
    }

}
