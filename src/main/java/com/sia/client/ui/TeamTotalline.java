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
    private String shortPrintedHomeTotal_current_home = null;
    private String shortPrintedHomeTotal_current_visit = null;
    private String shortPrintedHomeTotal_prior_home = null;
    private String shortPrintedHomeTotal_prior_visit = null;
    private String shortPrintedHomeTotal_opener_home = null;
    private String shortPrintedHomeTotal_opener_visit = null;
    private String otherPrintedHomeTotal_current_home = null;
    private String otherPrintedHomeTotal_current_visit = null;
    private String otherPrintedHomeTotal_prior_home = null;
    private String otherPrintedHomeTotal_prior_visit = null;
    private String otherPrintedHomeTotal_opener_home = null;
    private String otherPrintedHomeTotal_opener_visit = null;

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
    public TeamTotalline(int gid, int bid, double vo, double voj, double vu, double vuj, double ho, double hoj, double hu, double huj, long ts,
                         double pvo, double pvoj, double pvu, double pvuj, double pho, double phoj, double phu, double phuj, long pts,
                         double ovo, double ovoj, double ovu, double ovuj, double oho, double ohoj, double ohu, double ohuj, long ots, int p,int mb) {
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
        limit = mb;

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
            //why call this 4x this.setCurrentts(ts);

            if (isopener) {
                this.setOpenervisitunder(visitunder);
                this.setOpenervisitunderjuice(visitunderjuice);
                this.setOpenerts(ts);
                LineAlertOpenerManager.openerAlert(this.getGameid(),this.getBookieid(),this.getPeriod(), this);
            }
      //  }

       // if (homeoverjuice != 0) {
            this.setCurrenthomeover(homeover);
            this.setCurrenthomeoverjuice(homeoverjuice);
        //why call this 4x this.setCurrentts(ts);


            if (isopener) {
                this.setOpenerhomeover(homeover);
                this.setOpenerhomeoverjuice(homeoverjuice);
                this.setOpenerts(ts);
            }
       // }
      //  if (homeunderjuice != 0) {
            this.setCurrenthomeunder(homeunder);
            this.setCurrenthomeunderjuice(homeunderjuice);
        //why call this 4x this.setCurrentts(ts);

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
        BestLines.calculateconsensusteamtotal(gameid, period);
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
        shortPrintedHomeTotal_current_visit = null;
        otherPrintedHomeTotal_current_visit = null;
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
        shortPrintedHomeTotal_current_visit = null;
        otherPrintedHomeTotal_current_visit = null;
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
        shortPrintedHomeTotal_current_visit = null;
        otherPrintedHomeTotal_current_visit = null;
    }

    public double getCurrenthomeover() {
        return currenthomeover;
    }

    public void setCurrenthomeover(double currenthomeover) {
        setPriorhomeover(getCurrenthomeover());
        this.currenthomeover = currenthomeover;
        shortPrintedHomeTotal_current_home = null;
        otherPrintedHomeTotal_current_home = null;
    }

    public double getCurrenthomeoverjuice() {
        return currenthomeoverjuice;
    }

    public void setCurrenthomeoverjuice(double currenthomeoverjuice) {
        setPriorhomeoverjuice(getCurrenthomeoverjuice());
        this.currenthomeoverjuice = currenthomeoverjuice;
        shortPrintedHomeTotal_current_home = null;
        otherPrintedHomeTotal_current_home = null;
    }

    public double getCurrenthomeunder() {
        return currenthomeunder;
    }

    public void setCurrenthomeunder(double currenthomeunder) {
        setPriorhomeunder(getCurrenthomeunder());
        this.currenthomeunder = currenthomeunder;
        shortPrintedHomeTotal_current_home = null;
        otherPrintedHomeTotal_current_home = null;
    }

    public double getCurrenthomeunderjuice() {
        return currenthomeunderjuice;
    }

    public void setCurrenthomeunderjuice(double currenthomeunderjuice) {
        setPriorhomeunderjuice(getCurrenthomeunderjuice());
        this.currenthomeunderjuice = currenthomeunderjuice;
        shortPrintedHomeTotal_current_home = null;
        otherPrintedHomeTotal_current_home = null;
    }

    public void setCurrentvisitunderjuice(double currentvisitunderjuice) {
        setPriorvisitunderjuice(getCurrentvisitunderjuice());
        this.currentvisitunderjuice = currentvisitunderjuice;
        shortPrintedHomeTotal_current_visit = null;
        otherPrintedHomeTotal_current_visit = null;
    }

    public void setPriorvisitunderjuice(double priorvisitunderjuice) {
        this.priorvisitunderjuice = priorvisitunderjuice;
        shortPrintedHomeTotal_prior_visit = null;
        otherPrintedHomeTotal_prior_visit = null;
    }

    public void setPriorvisitoverjuice(double priorvisitoverjuice) {
        this.priorvisitoverjuice = priorvisitoverjuice;
        shortPrintedHomeTotal_prior_visit = null;
        otherPrintedHomeTotal_prior_visit = null;
    }

    public void setPriorvisitover(double priorvisitover) {
        this.priorvisitover = priorvisitover;
        shortPrintedHomeTotal_prior_visit = null;
        otherPrintedHomeTotal_prior_visit = null;
    }

    public String getShortPrintedCurrentVisitTotal() {
        if ( null == shortPrintedHomeTotal_current_visit) {
            shortPrintedHomeTotal_current_visit = getShortPrintedTotal(currentvisitover, currentvisitoverjuice, currentvisitunder, currentvisitunderjuice);
        }
        return shortPrintedHomeTotal_current_visit;
    }

    private String getShortPrintedTotal(double o, double oj, double u, double uj) {

        String retvalue = "";

        if (oj == 0) {
            return "";
        }



        double juice = 0;
        if (oj == uj && oj == -110 && o==u) {
            retvalue = o + "";
            if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".5", "\u00BD");
            if(AppController.getUserDisplaySettings().getSoccerquarter()) {
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
        if(AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }
        return retvalue;

    }

    public String getShortPrintedCurrentHomeTotal() {

        if ( null == shortPrintedHomeTotal_current_home) {
            shortPrintedHomeTotal_current_home = getShortPrintedTotal(currenthomeover, currenthomeoverjuice, currenthomeunder, currenthomeunderjuice);
        }
        return shortPrintedHomeTotal_current_home;
    }

    public String getShortPrintedPriorVisitTotal() {

        if ( null == shortPrintedHomeTotal_prior_visit) {
            shortPrintedHomeTotal_prior_visit = getShortPrintedTotal(priorvisitover, priorvisitoverjuice, priorvisitunder, priorvisitunderjuice);
        }
        return shortPrintedHomeTotal_prior_visit;
    }

    public String getShortPrintedPriorHomeTotal() {

        if ( null == shortPrintedHomeTotal_prior_home) {
            shortPrintedHomeTotal_prior_home = getShortPrintedTotal(priorhomeover, priorhomeoverjuice, priorhomeunder, priorhomeunderjuice);
        }
        return shortPrintedHomeTotal_prior_home;
    }

    public String getShortPrintedOpenerVisitTotal() {

        if ( null == shortPrintedHomeTotal_opener_visit) {
            shortPrintedHomeTotal_opener_visit = getShortPrintedTotal(openervisitover, openervisitoverjuice, openervisitunder, openervisitunderjuice);
        }
        return shortPrintedHomeTotal_opener_visit;
    }

    public String getShortPrintedOpenerHomeTotal() {

        if ( null == shortPrintedHomeTotal_opener_home) {
            shortPrintedHomeTotal_opener_home = getShortPrintedTotal(openerhomeover, openerhomeoverjuice, openerhomeunder, openerhomeunderjuice);
        }
        return shortPrintedHomeTotal_opener_home;
    }

    public String getOtherPrintedCurrentVisitTotal() {
        if ( null == otherPrintedHomeTotal_current_visit) {
            otherPrintedHomeTotal_current_visit = getOtherPrintedTotal(currentvisitover, currentvisitoverjuice, currentvisitunder, currentvisitunderjuice);
        }
        return otherPrintedHomeTotal_current_visit;
    }

    private String getOtherPrintedTotal(double o, double oj, double u, double uj) {
        String retvalue = "";

        if (oj == 0) {
            return "";
        }
        double juice = 0;
        if (oj == uj && oj == -110 && o==u) {
            retvalue = o + "";
            if (Math.abs(o) < 1 && retvalue.startsWith("0")) {
                retvalue = retvalue.substring(1);
            }
            retvalue = retvalue.replace(".0", "");
            char half = AsciiChar.getAscii(170);


            retvalue = retvalue.replace(".5", "\u00BD");
            if(AppController.getUserDisplaySettings().getSoccerquarter()) {
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
        if(AppController.getUserDisplaySettings().getSoccerquarter()) {
            retvalue = retvalue.replace(".25", "\u00BC");
            retvalue = retvalue.replace(".75", "\u00BE");
        }
        return retvalue;

    }

    public String getOtherPrintedCurrentHomeTotal() {
        if ( null == otherPrintedHomeTotal_current_home) {
            otherPrintedHomeTotal_current_home = getOtherPrintedTotal(currenthomeover, currenthomeoverjuice, currenthomeunder, currenthomeunderjuice);
        }
        return otherPrintedHomeTotal_current_home;
    }

    public String getOtherPrintedPriorVisitTotal() {

        if ( null == otherPrintedHomeTotal_prior_visit) {
            otherPrintedHomeTotal_prior_visit = getOtherPrintedTotal(priorvisitover, priorvisitoverjuice, priorvisitunder, priorvisitunderjuice);
        }
        return otherPrintedHomeTotal_prior_visit;
    }

    public String getOtherPrintedPriorHomeTotal() {

        if ( null == otherPrintedHomeTotal_prior_home) {
            otherPrintedHomeTotal_prior_home = getOtherPrintedTotal(priorhomeover, priorhomeoverjuice, priorhomeunder, priorhomeunderjuice);
        }
        return otherPrintedHomeTotal_prior_home;
    }

    public String getOtherPrintedOpenerVisitTotal() {

        if ( null == otherPrintedHomeTotal_opener_visit) {
            otherPrintedHomeTotal_opener_visit = getOtherPrintedTotal(openervisitover, openervisitoverjuice, openervisitunder, openervisitunderjuice);
        }
        return otherPrintedHomeTotal_opener_visit;
    }

    public String getOtherPrintedOpenerHomeTotal() {
        if ( null == otherPrintedHomeTotal_opener_home) {
            otherPrintedHomeTotal_opener_home = getOtherPrintedTotal(openerhomeover, openerhomeoverjuice, openerhomeunder, openerhomeunderjuice);
        }
        return otherPrintedHomeTotal_opener_home;
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
        shortPrintedHomeTotal_prior_visit = null;
        otherPrintedHomeTotal_prior_visit = null;
    }

    public double getOpenervisitover() {
        return openervisitover;
    }

    public void setOpenervisitover(double openervisitover) {
        this.openervisitover = openervisitover;
        shortPrintedHomeTotal_opener_visit = null;
        otherPrintedHomeTotal_opener_visit = null;
    }

    public double getOpenervisitoverjuice() {
        return openervisitoverjuice;
    }

    public void setOpenervisitoverjuice(double openervisitoverjuice) {
        this.openervisitoverjuice = openervisitoverjuice;
        shortPrintedHomeTotal_opener_visit = null;
        otherPrintedHomeTotal_opener_visit = null;
    }

    public double getOpenervisitunder() {
        return openervisitunder;
    }

    public void setOpenervisitunder(double openervisitunder) {
        this.openervisitunder = openervisitunder;
        shortPrintedHomeTotal_opener_visit = null;
        otherPrintedHomeTotal_opener_visit = null;
    }

    public double getOpenervisitunderjuice() {
        return openervisitunderjuice;
    }

    public void setOpenervisitunderjuice(double openervisitunderjuice) {
        this.openervisitunderjuice = openervisitunderjuice;
        shortPrintedHomeTotal_opener_visit = null;
        otherPrintedHomeTotal_opener_visit = null;
    }

    public double getPriorhomeover() {
        return priorhomeover;
    }

    public void setPriorhomeover(double priorhomeover) {
        this.priorhomeover = priorhomeover;
        shortPrintedHomeTotal_prior_home = null;
        otherPrintedHomeTotal_prior_home = null;
    }

    public double getPriorhomeoverjuice() {
        return priorhomeoverjuice;
    }

    public void setPriorhomeoverjuice(double priorhomeoverjuice) {
        this.priorhomeoverjuice = priorhomeoverjuice;
        shortPrintedHomeTotal_prior_home = null;
        otherPrintedHomeTotal_prior_home = null;
    }

    public double getPriorhomeunder() {
        return priorhomeunder;
    }

    public void setPriorhomeunder(double priorhomeunder) {
        this.priorhomeunder = priorhomeunder;
        shortPrintedHomeTotal_prior_home = null;
        otherPrintedHomeTotal_prior_home = null;
    }

    public double getPriorhomeunderjuice() {
        return priorhomeunderjuice;
    }

    public void setPriorhomeunderjuice(double priorhomeunderjuice) {
        this.priorhomeunderjuice = priorhomeunderjuice;
        shortPrintedHomeTotal_prior_home = null;
        otherPrintedHomeTotal_prior_home = null;
    }


    public double getOpenerhomeover() {
        return openerhomeover;
    }

    public void setOpenerhomeover(double openerhomeover) {
        this.openerhomeover = openerhomeover;
        shortPrintedHomeTotal_opener_home = null;
        otherPrintedHomeTotal_opener_home = null;
    }

    public double getOpenerhomeoverjuice() {
        return openerhomeoverjuice;
    }

    public void setOpenerhomeoverjuice(double openerhomeoverjuice) {
        this.openerhomeoverjuice = openerhomeoverjuice;
        shortPrintedHomeTotal_opener_home = null;
        otherPrintedHomeTotal_opener_home = null;
    }

    public double getOpenerhomeunder() {
        return openerhomeunder;
    }

    public void setOpenerhomeunder(double openerhomeunder) {
        this.openerhomeunder = openerhomeunder;
        shortPrintedHomeTotal_opener_home = null;
        otherPrintedHomeTotal_opener_home = null;
    }

    public double getOpenerhomeunderjuice() {
        return openerhomeunderjuice;
    }

    public void setOpenerhomeunderjuice(double openerhomeunderjuice) {
        this.openerhomeunderjuice = openerhomeunderjuice;
        shortPrintedHomeTotal_opener_home = null;
        otherPrintedHomeTotal_opener_home = null;
    }
    @Override
    public String getOpener()
    {
        String s = "";
        if(getOpenervisitoverjuice()!= 0)
        {
            s = getOpenervisitover()+"o"+getOpenervisitoverjuice()+"<br>"+getOpenervisitunder()+"u"+getOpenervisitunderjuice();
        }
        if(getOpenerhomeoverjuice()!= 0)
        {
            s = s+"<br>"+getOpenerhomeover()+"o"+getOpenerhomeoverjuice()+"<br>"+getOpenerhomeunder()+"u"+getOpenerhomeunderjuice();
        }



        return s;
    }

    public String showAwayHistory()
    {
        try
        {
        String s =
                "<tr><td>C:</td><td>"+getShortPrintedCurrentVisitTotal()+"</td><td>"+formatts(getCurrentts())+"</td></tr>"+
                "<tr><td>P:</td><td>"+getShortPrintedPriorVisitTotal()+"</td><td>"+formatts(getPriorts())+"</td></tr>"+
                "<tr><td>O:</td><td>"+getShortPrintedOpenerVisitTotal()+"</td><td>"+formatts(getOpenerts())+"</td></tr>";

        return s;
        }
        catch(Exception ex)
        {
            log("ttlaway show history exception "+ex);
        }
        return "";

    }
    public String showHomeHistory()
    {
        try
        {
        String s =
                "<tr><td>C:</td><td>"+getShortPrintedCurrentHomeTotal()+"</td><td>"+formatts(getCurrentts())+"</td></tr>"+
                "<tr><td>P:</td><td>"+getShortPrintedPriorHomeTotal()+"</td><td>"+formatts(getPriorts())+"</td></tr>"+
                "<tr><td>O:</td><td>"+getShortPrintedOpenerHomeTotal()+"</td><td>"+formatts(getOpenerts())+"</td></tr>";

        return s;
        }
        catch(Exception ex)
        {
            log("ttlhome show history exception "+ex);
        }
        return "";

    }

}


