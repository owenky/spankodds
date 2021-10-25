package com.sia.client.ui;

import com.sia.client.model.BestLines;
import com.sia.client.model.Line;

import static com.sia.client.config.Utils.log;

public class TeamTotalline extends Line {


    boolean isbestvisitover = false;
    boolean isbestvisitunder = false;
    boolean isbesthomeover = false;
    boolean isbesthomeunder = false;
    double currentvisitover;
    double currentvisitoverjuice;
    double currentvisitunder;
    double currentvisitunderjuice;
    double currenthomeover;
    double currenthomeoverjuice;
    double currenthomeunder;
    double currenthomeunderjuice;
    double priorvisitover;
    double priorvisitoverjuice;
    double priorvisitunder;
    double priorvisitunderjuice;
    double priorhomeover;
    double priorhomeoverjuice;
    double priorhomeunder;
    double priorhomeunderjuice;
    double openervisitover;
    double openervisitoverjuice;
    double openervisitunder;
    double openervisitunderjuice;
    double openerhomeover;
    double openerhomeoverjuice;
    double openerhomeunder;
    double openerhomeunderjuice;

    public TeamTotalline(int gid, int bid, double vo, double voj, double vu, double vuj, double ho, double hoj, double hu, double huj, long ts, int p) {
        this();

        currentvisitover = priorvisitover = openervisitover = vo;
        currenthomeover = priorhomeover = openerhomeover = ho;
        currentvisitoverjuice = priorvisitoverjuice = openervisitoverjuice = voj;
        currenthomeoverjuice = priorhomeoverjuice = openerhomeoverjuice = hoj;
        currentvisitunder = priorvisitunder = openervisitunder = vu;
        currenthomeunder = priorhomeunder = openerhomeunder = hu;
        currentvisitunderjuice = priorvisitunderjuice = openervisitunderjuice = vuj;
        currenthomeunderjuice = priorhomeunderjuice = openerhomeunderjuice = huj;
        currentts = priorts = openerts = ts;
        gameid = gid;
        bookieid = bid;
        period = p;


    }

    public TeamTotalline() {
        type = "teamtotal";
    }

    public TeamTotalline(int gid, int bid, double vo, double voj, double vu, double vuj, double ho, double hoj, double hu, double huj, long ts,
                         double pvo, double pvoj, double pvu, double pvuj, double pho, double phoj, double phu, double phuj, long pts,
                         double ovo, double ovoj, double ovu, double ovuj, double oho, double ohoj, double ohu, double ohuj, long ots, int p) {
        this();
        currentvisitover = vo;
        currentvisitoverjuice = voj;
        currentvisitunder = vu;
        currentvisitunderjuice = vuj;
        currenthomeover = ho;
        currenthomeoverjuice = hoj;
        currenthomeunder = hu;
        currenthomeunderjuice = huj;
        currentts = ts;

        priorvisitover = pvo;
        priorvisitoverjuice = pvoj;
        priorvisitunder = pvu;
        priorvisitunderjuice = pvuj;
        priorhomeover = pho;
        priorhomeoverjuice = phoj;
        priorhomeunder = phu;
        priorhomeunderjuice = phuj;
        priorts = pts;

        openervisitover = ovo;
        openervisitoverjuice = ovoj;
        openervisitunder = ovu;
        openervisitunderjuice = ovuj;
        openerhomeover = oho;
        openerhomeoverjuice = ohoj;
        openerhomeunder = ohu;
        openerhomeunderjuice = ohuj;
        openerts = ots;

        gameid = gid;
        bookieid = bid;
        period = p;


    }

    public boolean isBestVisitOver() {
        return isbestvisitover;
    }

    public void setBestVisitOver(boolean b) {
        isbestvisitover = b;
    }

    public boolean isBestVisitUnder() {
        return isbestvisitunder;
    }

    public void setBestVisitUnder(boolean b) {
        isbestvisitunder = b;
    }

    public boolean isBestHomeOver() {
        return isbesthomeover;
    }

    public void setBestHomeOver(boolean b) {
        isbesthomeover = b;
    }

    public boolean isBestHomeUnder() {
        return isbesthomeunder;
    }

    public void setBestHomeUnder(boolean b) {
        isbesthomeunder = b;
    }

    public String recordMove(double visitover, double visitoverjuice, double visitunder, double visitunderjuice,
                             double homeover, double homeoverjuice, double homeunder, double homeunderjuice, long ts, boolean isopener) {

      //  if (visitoverjuice != 0)
       // {
            this.setCurrentvisitover(visitover);
            this.setCurrentvisitoverjuice(visitoverjuice);
            this.setCurrentts(ts);


            if (isopener) {
                this.setOpenervisitover(visitover);
                this.setOpenervisitoverjuice(visitoverjuice);
                this.setOpenerts(ts);
            }
       // }
       // if (visitunderjuice != 0) {
            this.setCurrentvisitunder(visitunder);
            this.setCurrentvisitunderjuice(visitunderjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenervisitunder(visitunder);
                this.setOpenervisitunderjuice(visitunderjuice);
                this.setOpenerts(ts);
            }
      //  }

       // if (homeoverjuice != 0) {
            this.setCurrenthomeover(homeover);
            this.setCurrenthomeoverjuice(homeoverjuice);
            this.setCurrentts(ts);


            if (isopener) {
                this.setOpenerhomeover(homeover);
                this.setOpenerhomeoverjuice(homeoverjuice);
                this.setOpenerts(ts);
            }
       // }
      //  if (homeunderjuice != 0) {
            this.setCurrenthomeunder(homeunder);
            this.setCurrenthomeunderjuice(homeunderjuice);
            this.setCurrentts(ts);

            if (isopener) {
                this.setOpenerhomeunder(homeunder);
                this.setOpenerhomeunderjuice(homeunderjuice);
                this.setOpenerts(ts);
            }
      //  }


        try {
            if (getPriorvisitover() < getCurrentvisitover()) // 215 to 216
            {
                whowasbet = "o";
            } else if (getPriorvisitover() > getCurrentvisitover()) // 216 to 215
            {
                whowasbet = "u";
            } else //totals equal
            {
                if (getPriorvisitoverjuice() < getCurrentvisitoverjuice() && this.getPriorvisitunderjuice() != this.getCurrentvisitunderjuice()) //-110 to -105 , +105 to +110
                {
                    whowasbet = "u";
                } else if (getPriorvisitoverjuice() > getCurrentvisitoverjuice() && this.getPriorvisitunderjuice() != this.getCurrentvisitunderjuice())// priorjuice > currentjuice -105 to -110 110 to 105
                {
                    whowasbet = "o";
                } else {
                    //log("NO CHANGE!");
                    whowasbet = "";

                }

            }
        } catch (Exception ex) {
            whowasbet = "";
        }

        try {
            if (!this.whowasbet.equals("")) {
                LineAlertManager.checkMove(this);
            }
        } catch (Exception ex) {
            log(ex);
        }


        BestLines.calculatebestteamtotal(gameid, period);
        return whowasbet;

    }

    public double getPriorvisitover() {
        return priorvisitover;
    }

    public double getCurrentvisitover() {
        return currentvisitover;
    }

    public void setCurrentvisitover(double currentvisitover) {
        setPriorvisitover(getCurrentvisitover());
        this.currentvisitover = currentvisitover;
    }

    public double getPriorvisitoverjuice() {
        return priorvisitoverjuice;
    }

    public double getCurrentvisitoverjuice() {
        return currentvisitoverjuice;
    }

    public void setCurrentvisitoverjuice(double currentvisitoverjuice) {
        setPriorvisitoverjuice(getCurrentvisitoverjuice());
        this.currentvisitoverjuice = currentvisitoverjuice;
    }

    public double getPriorvisitunderjuice() {
        return priorvisitunderjuice;
    }

    public double getCurrentvisitunderjuice() {
        return currentvisitunderjuice;
    }

    public double getCurrentvisitunder() {
        return currentvisitunder;
    }

    public void setCurrentvisitunder(double currentvisitunder) {
        setPriorvisitunder(getCurrentvisitunder());
        this.currentvisitunder = currentvisitunder;
    }

    public double getCurrenthomeover() {
        return currenthomeover;
    }

    public void setCurrenthomeover(double currenthomeover) {
        setPriorhomeover(getCurrenthomeover());
        this.currenthomeover = currenthomeover;
    }

    public double getCurrenthomeoverjuice() {
        return currenthomeoverjuice;
    }

    public void setCurrenthomeoverjuice(double currenthomeoverjuice) {
        setPriorhomeoverjuice(getCurrenthomeoverjuice());
        this.currenthomeoverjuice = currenthomeoverjuice;
    }

    public double getCurrenthomeunder() {
        return currenthomeunder;
    }

    public void setCurrenthomeunder(double currenthomeunder) {
        setPriorhomeunder(getCurrenthomeunder());
        this.currenthomeunder = currenthomeunder;
    }

    public double getCurrenthomeunderjuice() {
        return currenthomeunderjuice;
    }

    public void setCurrenthomeunderjuice(double currenthomeunderjuice) {
        setPriorhomeunderjuice(getCurrenthomeunderjuice());
        this.currenthomeunderjuice = currenthomeunderjuice;
    }

    public void setCurrentvisitunderjuice(double currentvisitunderjuice) {
        setPriorvisitunderjuice(getCurrentvisitunderjuice());
        this.currentvisitunderjuice = currentvisitunderjuice;
    }

    public void setPriorvisitunderjuice(double priorvisitunderjuice) {
        this.priorvisitunderjuice = priorvisitunderjuice;
    }

    public void setPriorvisitoverjuice(double priorvisitoverjuice) {
        this.priorvisitoverjuice = priorvisitoverjuice;
    }

    public void setPriorvisitover(double priorvisitover) {
        this.priorvisitover = priorvisitover;
    }

    public String getShortPrintedCurrentVisitTotal() {

        return getShortPrintedTotal(currentvisitover, currentvisitoverjuice, currentvisitunder, currentvisitunderjuice);
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

    public String getShortPrintedCurrentHomeTotal() {

        return getShortPrintedTotal(currenthomeover, currenthomeoverjuice, currenthomeunder, currenthomeunderjuice);
    }

    public String getShortPrintedPriorVisitTotal() {

        return getShortPrintedTotal(priorvisitover, priorvisitoverjuice, priorvisitunder, priorvisitunderjuice);
    }

    public String getShortPrintedPriorHomeTotal() {

        return getShortPrintedTotal(priorhomeover, priorhomeoverjuice, priorhomeunder, priorhomeunderjuice);
    }

    public String getShortPrintedOpenerVisitTotal() {

        return getShortPrintedTotal(openervisitover, openervisitoverjuice, openervisitunder, openervisitunderjuice);
    }

    public String getShortPrintedOpenerHomeTotal() {

        return getShortPrintedTotal(openerhomeover, openerhomeoverjuice, openerhomeunder, openerhomeunderjuice);
    }

    public String getOtherPrintedCurrentVisitTotal() {

        return getOtherPrintedTotal(currentvisitover, currentvisitoverjuice, currentvisitunder, currentvisitunderjuice);
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

    public String getOtherPrintedCurrentHomeTotal() {

        return getOtherPrintedTotal(currenthomeover, currenthomeoverjuice, currenthomeunder, currenthomeunderjuice);
    }

    public String getOtherPrintedPriorVisitTotal() {

        return getOtherPrintedTotal(priorvisitover, priorvisitoverjuice, priorvisitunder, priorvisitunderjuice);
    }

    public String getOtherPrintedPriorHomeTotal() {

        return getOtherPrintedTotal(priorhomeover, priorhomeoverjuice, priorhomeunder, priorhomeunderjuice);
    }

    public String getOtherPrintedOpenerVisitTotal() {

        return getOtherPrintedTotal(openervisitover, openervisitoverjuice, openervisitunder, openervisitunderjuice);
    }

    public String getOtherPrintedOpenerHomeTotal() {

        return getOtherPrintedTotal(openerhomeover, openerhomeoverjuice, openerhomeunder, openerhomeunderjuice);
    }

    public boolean isOpener() {

        if (priorvisitover == 0 && priorvisitoverjuice == 0 && priorvisitunder == 0 && priorvisitunderjuice == 0) {
            return true;
        } else {
            return false;
        }
    }

    public double getPriorvisitunder() {
        return priorvisitunder;
    }

    public void setPriorvisitunder(double priorvisitunder) {
        this.priorvisitunder = priorvisitunder;
    }

    public double getOpenervisitover() {
        return openervisitover;
    }

    public void setOpenervisitover(double openervisitover) {
        this.openervisitover = openervisitover;
    }

    public double getOpenervisitoverjuice() {
        return openervisitoverjuice;
    }

    public void setOpenervisitoverjuice(double openervisitoverjuice) {
        this.openervisitoverjuice = openervisitoverjuice;
    }

    public double getOpenervisitunder() {
        return openervisitunder;
    }

    public void setOpenervisitunder(double openervisitunder) {
        this.openervisitunder = openervisitunder;
    }

    public double getOpenervisitunderjuice() {
        return openervisitunderjuice;
    }

    public void setOpenervisitunderjuice(double openervisitunderjuice) {
        this.openervisitunderjuice = openervisitunderjuice;
    }

    public double getPriorhomeover() {
        return priorhomeover;
    }

    public void setPriorhomeover(double priorhomeover) {
        this.priorhomeover = priorhomeover;
    }

    public double getPriorhomeoverjuice() {
        return priorhomeoverjuice;
    }

    public void setPriorhomeoverjuice(double priorhomeoverjuice) {
        this.priorhomeoverjuice = priorhomeoverjuice;
    }

    public double getPriorhomeunder() {
        return priorhomeunder;
    }

    public void setPriorhomeunder(double priorhomeunder) {
        this.priorhomeunder = priorhomeunder;
    }

    public double getPriorhomeunderjuice() {
        return priorhomeunderjuice;
    }

    public void setPriorhomeunderjuice(double priorhomeunderjuice) {
        this.priorhomeunderjuice = priorhomeunderjuice;
    }


    public double getOpenerhomeover() {
        return openerhomeover;
    }

    public void setOpenerhomeover(double openerhomeover) {
        this.openerhomeover = openerhomeover;
    }

    public double getOpenerhomeoverjuice() {
        return openerhomeoverjuice;
    }

    public void setOpenerhomeoverjuice(double openerhomeoverjuice) {
        this.openerhomeoverjuice = openerhomeoverjuice;
    }

    public double getOpenerhomeunder() {
        return openerhomeunder;
    }

    public void setOpenerhomeunder(double openerhomeunder) {
        this.openerhomeunder = openerhomeunder;
    }

    public double getOpenerhomeunderjuice() {
        return openerhomeunderjuice;
    }

    public void setOpenerhomeunderjuice(double openerhomeunderjuice) {
        this.openerhomeunderjuice = openerhomeunderjuice;
    }


}


