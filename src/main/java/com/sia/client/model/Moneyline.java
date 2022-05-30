package com.sia.client.model;

import com.sia.client.ui.AppController;
import com.sia.client.ui.LineAlertManager;
import com.sia.client.ui.LineAlertOpenerManager;

import java.io.Serializable;

import static com.sia.client.config.Utils.log;

public class Moneyline extends Line implements Serializable {

    boolean isbestvisitmoney = false;
    boolean isbesthomemoney = false;
    boolean isbestdrawmoney = false;
    double currentvisitjuice;
    double currenthomejuice;
    double currentdrawjuice;
    double priorvisitjuice;
    double priorhomejuice;
    double priordrawjuice;
    double openervisitjuice;
    double openerhomejuice;
    double openerdrawjuice;
    public Moneyline(int gid, int bid, double vj, double hj, double dj, long ts, int p) {
        this();
        currentvisitjuice = priorvisitjuice = openervisitjuice = vj;
        currenthomejuice = priorhomejuice = openerhomejuice = hj;
        currentdrawjuice = priordrawjuice = openerdrawjuice = dj;
        currentts = priorts = openerts = ts;

        gameid = gid;
        bookieid = bid;
        period = p;


    }


    public Moneyline() {
        type = "moneyline";
    }

    public Moneyline(int gid, int bid, double vj, double hj, double dj, long ts, double pvj, double phj, double pdj, long pts, int p) {
        this();
        currentvisitjuice = vj;
        currenthomejuice = hj;
        currentdrawjuice = dj;
        currentts = ts;

        priorvisitjuice = pvj;
        priorhomejuice = phj;
        priordrawjuice = pdj;
        priorts = pts;

        gameid = gid;
        bookieid = bid;
        period = p;


    }

    public Moneyline(int gid, int bid, double vj, double hj, double dj, long ts, double pvj, double phj, double pdj, long pts, double ovj, double ohj, double odj, long ots, int p) {
        this();
        currentvisitjuice = vj;
        currenthomejuice = hj;
        currentdrawjuice = dj;
        currentts = ts;

        priorvisitjuice = pvj;
        priorhomejuice = phj;
        priordrawjuice = pdj;
        priorts = pts;

        openervisitjuice = ovj;
        openerhomejuice = ohj;
        openerdrawjuice = odj;
        openerts = ots;

        gameid = gid;
        bookieid = bid;
        period = p;


    }
    public Moneyline(int gid, int bid, double vj, double hj, double dj, long ts, double pvj, double phj, double pdj, long pts, double ovj, double ohj, double odj, long ots, int p,int mb) {
        this();
        currentvisitjuice = vj;
        currenthomejuice = hj;
        currentdrawjuice = dj;
        currentts = ts;

        priorvisitjuice = pvj;
        priorhomejuice = phj;
        priordrawjuice = pdj;
        priorts = pts;

        openervisitjuice = ovj;
        openerhomejuice = ohj;
        openerdrawjuice = odj;
        openerts = ots;

        gameid = gid;
        bookieid = bid;
        period = p;
        limit = mb;

    }
    public boolean isBestVisitMoney() {
        return isbestvisitmoney;
    }

    public void setBestVisitMoney(boolean b)
    {
        isbestvisitmoney = b;
       if(b)
       {
           AppController.bestvisitml.put(period+"-"+gameid,getBookieObject());
       }
    }

    public boolean isBestHomeMoney() {
        return isbesthomemoney;
    }

    public void setBestHomeMoney(boolean b)
    {
        isbesthomemoney = b;
        if(b)
        {
            AppController.besthomeml.put(period+"-"+gameid,getBookieObject());
        }
    }

    public boolean isBestDrawMoney() {
        return isbestdrawmoney;
    }

    public void setBestDrawMoney(boolean b)
    {
        isbestdrawmoney = b;
        if(b)
        {
            AppController.bestdrawml.put(period+"-"+gameid,getBookieObject());
        }
    }

    public String recordMove(double visitjuice, double homejuice, double drawjuice, long ts, boolean isopener) {




            if (isopener)
            {
                this.setOpenervisitjuice(visitjuice);
                this.setOpenerhomejuice(homejuice);
                this.setOpenerts(ts);
            }
            else if(gameid < 10000)
            // if this is a half move i will throw away
            {
                if( visitjuice == this.getCurrentvisitjuice() || homejuice == this.getCurrenthomejuice())

                {
                    if(drawjuice != 0 && drawjuice == this.getCurrentdrawjuice() )
                    {
                      //  log("Throwout half moneyline =" + gameid + "..bookie=" +this.getBookieObject()+".."+visitjuice+"/"+homejuice+"/"+drawjuice+" vs."+this.getCurrentvisitjuice()+"/"+this.getCurrenthomejuice()+"/"+this.getCurrentdrawjuice());
                      //  return "";
                    }
                }

            }

            this.setCurrentvisitjuice(visitjuice);

            this.setCurrentts(ts);
            this.setCurrenthomejuice(homejuice);

        if (drawjuice != 0)
        {
            if (isopener)
            {
                this.setOpenerdrawjuice(drawjuice);
                this.setOpenerts(ts);

            }
            this.setCurrentdrawjuice(drawjuice);
        }


        try {
            if (this.getPriorvisitjuice() < this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) //-110 to -105 , +105 to +110
            {
                this.whowasbet = "h";
            } else if (this.getPriorvisitjuice() > this.getCurrentvisitjuice() && this.getPriorhomejuice() != this.getCurrenthomejuice()) {
                this.whowasbet = "v";
            } else {
                this.whowasbet = "";
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

        BestLines.calculatebestmoney(gameid, period);
        return this.whowasbet;


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


    public double getCurrentdrawjuice() {
        return currentdrawjuice;
    }

    public void setCurrentdrawjuice(double currentdrawjuice) {
        setPriordrawjuice(getCurrentdrawjuice());
        this.currentdrawjuice = currentdrawjuice;
    }
    public void setPriorhomejuice(double priorhomejuice) {
        this.priorhomejuice = priorhomejuice;
    }

    public void setPriorvisitjuice(double priorvisitjuice) {
        this.priorvisitjuice = priorvisitjuice;
    }

    public String getShortPrintedMoneyline() {
        double juice = min(currentvisitjuice, currenthomejuice);
        return juice + "";
    }

    public boolean isOpener() {

        if (priorvisitjuice == 0 && priorhomejuice == 0) {
            return true;
        } else {
            return false;
        }
    }

    public double getPriordrawjuice() {
        return priordrawjuice;
    }

    public void setPriordrawjuice(double priordrawjuice) {
        this.priordrawjuice = priordrawjuice;
    }

    public double getOpenervisitjuice() {
        return openervisitjuice;
    }

    public void setOpenervisitjuice(double openervisitjuice) {
        this.openervisitjuice = openervisitjuice;
    }


    public double getOpenerhomejuice() {
        return openerhomejuice;
    }

    public void setOpenerhomejuice(double openerhomejuice) {
        this.openerhomejuice = openerhomejuice;
    }

    public double getOpenerdrawjuice() {
        return openerdrawjuice;
    }

    public void setOpenerdrawjuice(double openerdrawjuice) {
        this.openerdrawjuice = openerdrawjuice;
    }

    public String getShortPrintedCurrentMoneyline()
    {
        return getShortPrintedMoneyline(currentvisitjuice, currenthomejuice, currentdrawjuice);
    }
    public String getShortPrintedPriorMoneyline()
    {
        return getShortPrintedMoneyline(priorvisitjuice, priorhomejuice, priordrawjuice);
    }
    public String getShortPrintedOpenerMoneyline()
    {
        return getShortPrintedMoneyline(openervisitjuice, openerhomejuice, openerdrawjuice);
    }
    public String getShortPrintedMoneyline(double vjuice, double hjuice, double djuice)
    {
        double juice = min(vjuice,hjuice);
        if(juice == -100 || juice == 100)
        {
            return "EVEN";
        }
        String juiceS = juice+"";
        juiceS = juiceS.replace(".0", "");
        return juiceS;

    }

    @Override
    public String getOpener()
    {
        return getOpenervisitjuice()+"<br>"+getOpenerhomejuice();
    }

    public String showHistory() {
        try {
            String s =
                    "<tr><td>C:</td><td>" + getShortPrintedCurrentMoneyline() + "</td><td>" + formatts(getCurrentts()) + "</td></tr>" +
                    "<tr><td>P:</td><td>" + getShortPrintedPriorMoneyline() + "</td><td>" + formatts(getPriorts()) + "</td></tr>" +
                    "<tr><td>O:</td><td>" + getShortPrintedOpenerMoneyline() + "</td><td>" + formatts(getOpenerts()) + "</td></tr>";

            return s;
        } catch (Exception ex) {
            log("ml show history exception " + ex);
        }
        return "";
    }
}