package com.sia.client.ui;

import com.sia.client.model.BestLines;
import com.sia.client.model.Line;

import java.io.Serializable;

import static com.sia.client.config.Utils.log;


public class Totalline extends Line implements Serializable {

    boolean isbestover = false;
    boolean isbestunder = false;
    double currentover;
    double currentoverjuice;
    double currentunder;
    double currentunderjuice;
    double priorover;
    double prioroverjuice;
    double priorunder;
    double priorunderjuice;
    double openerover;
    double openeroverjuice;
    double openerunder;
    double openerunderjuice;

    public Totalline(int gid, int bid, double o, double oj, double u, double uj, long ts, int p) {
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


    public Totalline() {
        type = "total";
    }


    public Totalline(int gid, int bid, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts, int p) {
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

    public Totalline(int gid, int bid, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts, double oo, double ooj, double ou, double ouj, long ots, int p) {
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
    public Totalline(int gid, int bid, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts, double oo, double ooj, double ou, double ouj, long ots, int p,int mb) {
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
        limit = mb;

    }
    public boolean isBestOver() {
        return isbestover;
    }

    public void setBestOver(boolean b) {
        isbestover = b;
    }

    public boolean isBestUnder() {
        return isbestunder;
    }

    public void setBestUnder(boolean b) {
        isbestunder = b;
    }

    public String recordMove(double over, double overjuice, double under, double underjuice, long ts, boolean isopener) {

       // if (overjuice != 0)
       // {
            this.setCurrentover(over);
            this.setCurrentoverjuice(overjuice);
            this.setCurrentts(ts);


            if (isopener) {
                this.setOpenerover(over);
                this.setOpeneroverjuice(overjuice);
                this.setOpenerts(ts);
            }
       // }
       // if (underjuice != 0)
       // {
            this.setCurrentunder(under);
            this.setCurrentunderjuice(underjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenerunder(under);
                this.setOpenerunderjuice(underjuice);
                this.setOpenerts(ts);
            }
       // }

        try {
            if (this.getPriorover() < this.getCurrentover()) // 215 to 216
            {
                this.whowasbet = "o";
            } else if (this.getPriorover() > this.getCurrentover()) // 216 to 215
            {
                this.whowasbet = "u";
            } else //totals equal
            {
                if (this.getPrioroverjuice() < this.getCurrentoverjuice() && this.getPriorunderjuice() != this.getCurrentunderjuice()) //-110 to -105 , +105 to +110
                {
                    this.whowasbet = "u";
                } else if (this.getPrioroverjuice() > this.getCurrentoverjuice() && this.getPriorunderjuice() != this.getCurrentunderjuice())// priorjuice > currentjuice -105 to -110 110 to 105
                {
                    this.whowasbet = "o";
                } else {
                    //log("NO CHANGE!");
                    this.whowasbet = "";

                }

            }
        } catch (Exception ex) {
            this.whowasbet = "";
        }

        try {
            if (!this.whowasbet.equals("")) {
                LineAlertManager.checkMove(this);
            }
        } catch (Exception ex) {
            log( ex);
        }


        BestLines.calculatebesttotal(gameid, period);
        return this.whowasbet;

    }

    public double getPriorover() {
        return priorover;
    }

    public double getCurrentover() {
        return currentover;
    }

    public void setCurrentover(double currentover) {
        setPriorover(getCurrentover());
        this.currentover = currentover;
    }

    public double getPrioroverjuice() {
        return prioroverjuice;
    }

    public double getCurrentoverjuice() {
        return currentoverjuice;
    }

    public void setCurrentoverjuice(double currentoverjuice) {
        setPrioroverjuice(getCurrentoverjuice());
        this.currentoverjuice = currentoverjuice;
    }

    public double getPriorunderjuice() {
        return priorunderjuice;
    }

    public double getCurrentunderjuice() {
        return currentunderjuice;
    }

    public double getCurrentunder() {
        return currentunder;
    }

    public void setCurrentunder(double currentunder) {
        setPriorunder(getCurrentunder());
        this.currentunder = currentunder;
    }

    public void setCurrentunderjuice(double currentunderjuice) {
        setPriorunderjuice(getCurrentunderjuice());
        this.currentunderjuice = currentunderjuice;
    }

    public void setPriorunderjuice(double priorunderjuice) {
        this.priorunderjuice = priorunderjuice;
    }

    public void setPrioroverjuice(double prioroverjuice) {
        this.prioroverjuice = prioroverjuice;
    }

    public void setPriorover(double priorover) {
        this.priorover = priorover;
    }

    public String getShortPrintedCurrentTotal() {

        return getShortPrintedTotal(currentover, currentoverjuice, currentunder, currentunderjuice);
    }

    public String getShortPrintedTotal(double o, double oj, double u, double uj) {

        String retvalue = o + "";
        if (oj == 0) {
            return "";
        }
        if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
            retvalue = retvalue.substring(1);
        }


        double juice = 0;
        if (oj == uj && oj == -110) {

            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".5", "\u00BD");
            retvalue = retvalue.replace(".75", "\u00BE");
            return retvalue;
        } else if (oj < uj) {
            retvalue = retvalue + "o";
            juice = oj;
        } else {
            retvalue = retvalue + "u";
            juice = uj;
        }


        if (juice < 0) {
            juice = juice + 100;
            String juicestr = "" + juice;
            if (juice == 0) {
                retvalue = retvalue + "ev";
            } else if (juice > -10) {
                juice = juice * -1;
                juicestr = "-0" + juice;
                retvalue = retvalue + "" + juicestr;
            } else if (juice <= -100) // initial juice was  -200 or worse
            {

                juice = juice - 100;
                juicestr = "" + juice;
                retvalue = retvalue + "" + juicestr;
            } else {
                retvalue = retvalue + "" + juicestr;


            }

        } else {
            juice = juice - 100;

            if (juice == 0) {
                retvalue = retvalue + "ev";
            } else if (juice < 10) {
                retvalue = retvalue + "+0" + juice;
            } else {
                retvalue = retvalue + "+" + juice;

            }

        }
        retvalue = retvalue.replace(".0", "");
        char half = AsciiChar.getAscii(170);

        retvalue = retvalue.replace(".25", "\u00BC");
        retvalue = retvalue.replace(".5", "\u00BD");
        retvalue = retvalue.replace(".75", "\u00BE");
        return retvalue;

    }

    public String getShortPrintedPriorTotal() {

        return getShortPrintedTotal(priorover, prioroverjuice, priorunder, priorunderjuice);
    }

    public String getShortPrintedOpenerTotal() {

        return getShortPrintedTotal(openerover, openeroverjuice, openerunder, openerunderjuice);
    }

    public String getOtherPrintedCurrentTotal() {

        return getOtherPrintedTotal(currentover, currentoverjuice, currentunder, currentunderjuice);
    }

    public String getOtherPrintedTotal(double o, double oj, double u, double uj) {
        String retvalue = o + "";
        if (oj == 0) {
            return "";
        }
        if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
            retvalue = retvalue.substring(1);
        }


        double juice = 0;
        if (oj == uj && oj == -110) {

            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".5", "\u00BD");
            retvalue = retvalue.replace(".75", "\u00BE");
            return retvalue;
        } else if (oj < uj) {
            retvalue = retvalue + "u";
            juice = uj;
        } else {
            retvalue = retvalue + "o";
            juice = oj;
        }


        if (juice < 0) {
            juice = juice + 100;
            String juicestr = "" + juice;
            if (juice == 0) {
                retvalue = retvalue + "ev";
            } else if (juice > -10) {
                juice = juice * -1;
                juicestr = "-0" + juice;
                retvalue = retvalue + "" + juicestr;
            } else if (juice <= -100) // initial juice was  -200 or worse
            {

                juice = juice - 100;
                juicestr = "" + juice;
                retvalue = retvalue + "" + juicestr;
            } else {
                retvalue = retvalue + "" + juicestr;

            }

        } else {
            juice = juice - 100;

            if (juice == 0) {
                retvalue = retvalue + "ev";
            } else if (juice < 10) {
                retvalue = retvalue + "+0" + juice;
            } else {
                retvalue = retvalue + "+" + juice;

            }

        }
        retvalue = retvalue.replace(".0", "");

        char half = AsciiChar.getAscii(170);

        retvalue = retvalue.replace(".25", "\u00BC");
        retvalue = retvalue.replace(".5", "\u00BD");
        retvalue = retvalue.replace(".75", "\u00BE");
        return retvalue;

    }

    public String getOtherPrintedPriorTotal() {

        return getOtherPrintedTotal(priorover, prioroverjuice, priorunder, priorunderjuice);
    }

    public String getOtherPrintedOpenerTotal() {

        return getOtherPrintedTotal(openerover, openeroverjuice, openerunder, openerunderjuice);
    }

    public boolean isOpener() {

        if (priorover == 0 && prioroverjuice == 0 && priorunder == 0 && priorunderjuice == 0) {
            return true;
        } else {
            return false;
        }
    }

    public double getPriorunder() {
        return priorunder;
    }

    public void setPriorunder(double priorunder) {
        this.priorunder = priorunder;
    }

    public double getOpenerover() {
        return openerover;
    }

    public void setOpenerover(double openerover) {
        this.openerover = openerover;
    }

    public double getOpeneroverjuice() {
        return openeroverjuice;
    }

    public void setOpeneroverjuice(double openeroverjuice) {
        this.openeroverjuice = openeroverjuice;
    }

    public double getOpenerunder() {
        return openerunder;
    }

    public void setOpenerunder(double openerunder) {
        this.openerunder = openerunder;
    }

    public double getOpenerunderjuice() {
        return openerunderjuice;
    }

    public void setOpenerunderjuice(double openerunderjuice) {
        this.openerunderjuice = openerunderjuice;
    }

}