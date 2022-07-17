package com.sia.client.ui;

import com.sia.client.model.BestLines;
import com.sia.client.model.Line;
import com.sia.client.model.LineIdentity;

import java.io.Serializable;

import static com.sia.client.config.Utils.log;


public class Totalline extends Line implements Serializable {

    private boolean isbestover = false;
    private boolean isbestunder = false;
    private double currentover;
    private double currentoverjuice;
    private double currentunder;
    private double currentunderjuice;
    private double priorover;
    private double prioroverjuice;
    private double priorunder;
    private double priorunderjuice;
    private double openerover;
    private double openeroverjuice;
    private double openerunder;
    private double openerunderjuice;
    private String shortPrintedTotal_current = null;
    private String shortPrintedTotal_prior = null;
    private String shortPrintedTotal_opener = null;

    public Totalline(LineIdentity lineIdentity, double o, double oj, double u, double uj, long ts) {
        this(lineIdentity);
        currentover = priorover = openerover = o;
        currentoverjuice = prioroverjuice = openeroverjuice = oj;
        currentunder = priorunder = openerunder = u;
        currentunderjuice = priorunderjuice = openerunderjuice = uj;
        currentts = priorts = openerts = ts;
    }

    public Totalline(LineIdentity lineIdentity) {
        super(lineIdentity, "total");
    }


    public Totalline(LineIdentity lineIdentity, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts) {
        this(lineIdentity);
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
    }

    public Totalline(LineIdentity lineIdentity, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts, double oo, double ooj, double ou, double ouj, long ots) {
        this(lineIdentity);
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
    }

    public Totalline(LineIdentity lineIdentity, double o, double oj, double u, double uj, long ts, double po, double poj, double pu, double puj, long pts, double oo, double ooj, double ou, double ouj, long ots, int mb) {
        this(lineIdentity);
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

        limit = mb;
    }

    public boolean isBestOver() {
        return isbestover;
    }

    public void setBestOver(boolean b) {
        isbestover = b;
        if (b) {
            AppController.bestover.put(getPeriod() + "-" + getGameid(), getBookieObject());
        }
    }

    public boolean isBestUnder() {
        return isbestunder;
    }

    public void setBestUnder(boolean b) {
        isbestunder = b;
        if (b) {
            AppController.bestunder.put(getPeriod() + "-" + getGameid(), getBookieObject());
        }
    }

    public String recordMove(double over, double overjuice, double under, double underjuice, long ts, boolean isopener) {
        this.setCurrentover(over);
        this.setCurrentoverjuice(overjuice);
        this.setCurrentts(ts);


        if (isopener) {
            this.setOpenerover(over);
            this.setOpeneroverjuice(overjuice);
            this.setOpenerunder(under);
            this.setOpenerunderjuice(underjuice);
            this.setOpenerts(ts);
        }
        // }
        // if (underjuice != 0)
        // {
        this.setCurrentunder(under);
        this.setCurrentunderjuice(underjuice);
        //why call this twice this.setCurrentts(ts);

        if (isopener) {

            LineAlertOpenerManager.openerAlert(this.getGameid(), this.getBookieid(), this.getPeriod(), this);
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
            log(ex);
        }


        BestLines.calculatebesttotal(getGameid(), getPeriod());
        BestLines.calculateconsensustotal(getGameid(), getPeriod());
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
        shortPrintedTotal_current = null;
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
        shortPrintedTotal_current = null;
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
        shortPrintedTotal_current = null;
    }

    public void setCurrentunderjuice(double currentunderjuice) {
        setPriorunderjuice(getCurrentunderjuice());
        this.currentunderjuice = currentunderjuice;
        shortPrintedTotal_current = null;
    }

    public void setPriorunderjuice(double priorunderjuice) {
        this.priorunderjuice = priorunderjuice;
        shortPrintedTotal_prior = null;
    }

    public void setPrioroverjuice(double prioroverjuice) {
        this.prioroverjuice = prioroverjuice;
        shortPrintedTotal_prior = null;
    }

    public void setPriorover(double priorover) {
        this.priorover = priorover;
        shortPrintedTotal_prior = null;
    }

    public String getShortPrintedCurrentTotal() {
        if (null == shortPrintedTotal_current) {
            shortPrintedTotal_current = getShortPrintedTotal(currentover, currentoverjuice, currentunder, currentunderjuice);
        }
        return shortPrintedTotal_current;
    }

    private String getShortPrintedTotal(double o, double oj, double u, double uj) {

        String retvalue = "";

        if (oj == 0) {
            return "";
        }

//        if(getGameid() > 900 && getGameid() < 999 && getBookieid() == 997)
//        {
//           // System.out.println("short in "+o+""+oj+".."+u+""+uj);
//        }

        double juice = 0;
        if (oj == uj && oj == -110 && o == u) {
            retvalue = o + "";
            if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".5", "\u00BD");
            if (AppController.getUserDisplaySettings().getSoccerquarter()) {
                retvalue = retvalue.replace(".25", "\u00BC");
                retvalue = retvalue.replace(".75", "\u00BE");
            }
            return retvalue;
        } else if (oj < uj) {
            retvalue = o + "";
            if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }

            retvalue = retvalue + "o";
            juice = oj;
        } else {
            retvalue = u + "";
            if (Math.abs(u) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }

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

        retvalue = retvalue.replace(".5", "\u00BD");
        if (AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }

        if (getGameid() > 900 && getGameid() < 999 && getBookieid() == 997) {
            // System.out.println("short out"+retvalue);
        }
        return retvalue;

    }

    public String getShortPrintedPriorTotal() {

        if (null == shortPrintedTotal_prior) {
            shortPrintedTotal_prior = getShortPrintedTotal(priorover, prioroverjuice, priorunder, priorunderjuice);
        }
        return shortPrintedTotal_prior;
    }

    public String getShortPrintedOpenerTotal() {

        if (null == shortPrintedTotal_opener) {
            shortPrintedTotal_opener = getShortPrintedTotal(openerover, openeroverjuice, openerunder, openerunderjuice);
        }
        return shortPrintedTotal_opener;
    }

    public String getOtherPrintedCurrentTotal() {

        return getOtherPrintedTotal(currentover, currentoverjuice, currentunder, currentunderjuice);
    }

    public String getOtherPrintedTotal(double o, double oj, double u, double uj) {

        if (getGameid() > 900 && getGameid() < 999 && getBookieid() == 997) {
            //System.out.println("other in "+o+""+oj+".."+u+""+uj);
        }

        String retvalue = "";

        if (uj == 0) {
            return "";
        }


        double juice = 0;
        if (oj == uj && uj == -110 && o == u) {
            retvalue = u + "";
            if (Math.abs(u) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".5", "\u00BD");
            if (AppController.getUserDisplaySettings().getSoccerquarter()) {
                retvalue = retvalue.replace(".25", "\u00BC");
                retvalue = retvalue.replace(".75", "\u00BE");
            }
            return retvalue;
        } else if (oj < uj) {
            retvalue = u + "";
            if (Math.abs(u) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
            retvalue = retvalue + "u";
            juice = uj;
        } else {
            retvalue = o + "";
            if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
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

        retvalue = retvalue.replace(".5", "\u00BD");
        if (AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }
        if (getGameid() > 900 && getGameid() < 999 && getBookieid() == 997) {
            //  System.out.println("other out"+retvalue);
        }


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
        shortPrintedTotal_prior = null;
    }

    public double getOpenerover() {
        return openerover;
    }

    public void setOpenerover(double openerover) {
        this.openerover = openerover;
        shortPrintedTotal_opener = null;
    }

    public double getOpeneroverjuice() {
        return openeroverjuice;
    }

    public void setOpeneroverjuice(double openeroverjuice) {
        this.openeroverjuice = openeroverjuice;
        shortPrintedTotal_opener = null;
    }

    public double getOpenerunder() {
        return openerunder;
    }

    public void setOpenerunder(double openerunder) {
        this.openerunder = openerunder;
        shortPrintedTotal_opener = null;
    }

    public double getOpenerunderjuice() {
        return openerunderjuice;
    }

    public void setOpenerunderjuice(double openerunderjuice) {
        this.openerunderjuice = openerunderjuice;
        shortPrintedTotal_opener = null;
    }

    @Override
    public String getOpener() {
        String s = getCurrentover() + "o" + getCurrentoverjuice() + "<br>" + getCurrentunder() + "u" + getCurrentunderjuice();


        return s;
    }

    public String showHistory() {
        try {
            String s =
                    "<tr><td>C:</td><td>" + getShortPrintedCurrentTotal() + "</td><td>" + formatts(getCurrentts()) + "</td></tr>" +
                            "<tr><td>P:</td><td>" + getShortPrintedPriorTotal() + "</td><td>" + formatts(getPriorts()) + "</td></tr>" +
                            "<tr><td>O:</td><td>" + getShortPrintedOpenerTotal() + "</td><td>" + formatts(getOpenerts()) + "</td></tr>";

            return s;
        } catch (Exception ex) {
            log("total show history exception " + ex);
        }
        return "";
    }

}