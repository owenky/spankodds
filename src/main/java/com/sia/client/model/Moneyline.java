package com.sia.client.model;

import com.sia.client.ui.LineAlertManager;

import java.io.Serializable;

import static com.sia.client.config.Utils.log;

public class Moneyline extends Line implements Serializable {

    boolean isbestvisitmoney = false;
    boolean isbesthomemoney = false;
    boolean isbestdrawmoney = false;

    double currentvisitjuice;
    double currenthomejuice;
    double currentdrawjuice;
    private long currentts = 1000;  //timestamp

    double priorvisitjuice;
    double priorhomejuice;
    double priordrawjuice;
    private long priorts = 1000; //timestamp

    double openervisitjuice;
    double openerhomejuice;
    double openerdrawjuice;
    private long openerts = 1000;  //timestamp


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

    public boolean isBestVisitMoney() {
        return isbestvisitmoney;
    }

    public void setBestVisitMoney(boolean b) {
        isbestvisitmoney = b;
    }

    public boolean isBestHomeMoney() {
        return isbesthomemoney;
    }

    public void setBestHomeMoney(boolean b) {
        isbesthomemoney = b;
    }

    public boolean isBestDrawMoney() {
        return isbestdrawmoney;
    }

    public void setBestDrawMoney(boolean b) {
        isbestdrawmoney = b;
    }

    public String recordMove(double visitjuice, double homejuice, double drawjuice, long ts, boolean isopener) {

        if (visitjuice != 0) {
            this.setCurrentvisitjuice(visitjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenervisitjuice(visitjuice);
                this.setOpenerts(ts);
            }
        }
        if (homejuice != 0) {
            this.setCurrenthomejuice(homejuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenerhomejuice(homejuice);
                this.setOpenerts(ts);
            }
        }
        if (drawjuice != 0) {
            this.setCurrentdrawjuice(drawjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenerdrawjuice(drawjuice);
                this.setOpenerts(ts);
            }
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

    public long getCurrentts() {
        return currentts;
    }

    public double getCurrentdrawjuice() {
        return currentdrawjuice;
    }

    public void setCurrentdrawjuice(double currentdrawjuice) {
        setPriordrawjuice(getCurrentdrawjuice());
        this.currentdrawjuice = currentdrawjuice;
    }

    public void setCurrentts(long currentts) {
        setPriorts(getCurrentts());
        this.currentts = currentts;
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

        return priorvisitjuice == 0 && priorhomejuice == 0;
    }

    public long getPriorts() {
        return priorts;
    }

    public void setPriorts(long priorts) {
        this.priorts = priorts;
    }

    public double getPriordrawjuice() {
        return priordrawjuice;
    }

    public void setPriordrawjuice(double priordrawjuice) {
        this.priordrawjuice = priordrawjuice;
    }

    public long getOpenerts() {
        return openerts;
    }

    public void setOpenerts(long openerts) {
        this.openerts = openerts;
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

}