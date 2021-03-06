package com.sia.client.ui;

import com.sia.client.config.Config;
import com.sia.client.config.SiaConst;
import com.sia.client.model.*;

import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

import static com.sia.client.config.Utils.log;

public class SoccerSpreadTotalView extends ViewValue  implements ViewWithColor {
    public static String ICON_UP = ChartView.ICON_UP;
    public static String ICON_DOWN = ChartView.ICON_DOWN;
    public static String ICON_BLANK = "blank.gif";
    public String display = "default";
    public int period = 0;
    Spreadline sl;
    Totalline tl;
    Moneyline ml;
    TeamTotalline ttl;
    LineData[] boxes = new LineData[4];
    LineData[] priorboxes = new LineData[4];
    LineData[] openerboxes = new LineData[4];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld3 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld4 = new LineData(ICON_BLANK, "", Color.WHITE);
    int bid;
    int gid;
    Color priorspreadcolor = Color.WHITE;
    Color priortotalcolor = Color.WHITE;
    Color priorteamtotalcolor = Color.WHITE;
    Color priormoneycolor = Color.WHITE;
    Color spreadcolor = Color.WHITE;
    Color totalcolor = Color.WHITE;
    Color teamtotalcolor = Color.WHITE;
    Color moneycolor = Color.WHITE;
    Color topcolor = Color.WHITE;
    Color bottomcolor = Color.WHITE;
    Color drawcolor = Color.WHITE;
    Color totalcolor2 = Color.WHITE;

    String topicon = ICON_BLANK;
    String bottomicon = ICON_BLANK;
    String drawicon = ICON_BLANK;
    String totalicon = ICON_BLANK;
    long clearts;
    int id;
    Game game;
    Bookie bookie;
    Sport sp;
    String topborder = "";
    String bottomborder = "";
    String drawborder = "";
    String totalborder = "";
    LinesTableData ltd;
    boolean showcomebacks = false;
    boolean isopenerbookie = false;
    private String linehistoryurl ="http://sof300732.com:9998/gamedetails/linehistory.jsp?";
    private final UserDisplaySettings userDisplaySettings = Config.instance().getUserDisplaySettings();

    public SoccerSpreadTotalView(int bid, int gid, long cleartime, LinesTableData ltd) {
        if (bid >= 1000) {
            isopenerbookie = true;
            if(bid != 1000)
            {
                bid = bid - 1000;
            }
        }
        this.bid = bid;
        this.gid = gid;
        topborder = bottomborder = drawborder = totalborder = "";
        game = AppController.getGame(gid);
        bookie = AppController.getBookie(bid);
        sp = AppController.getSportByLeagueId(game.getLeague_id());
        this.getCurrentBoxes();
        clearts = cleartime;
        id = new Random().nextInt();
        this.ltd = ltd;


    }

    public LineData[] getCurrentBoxes() {
        topborder = bottomborder = drawborder = totalborder = ""; // owen added drawborder and totalborder
        //log("in getcurrentboxes "+bid+".."+gid);
        if (isopenerbookie) {
            boxes = getOpenerBoxes();
            return boxes;

        }

        if (display.equals("default")) {
            display=userDisplaySettings.getSoccerdefault();
            /*
            if (sp.getSport_id() == 3) // baseball
            {
                display = "totalbothmoney";
            } else if (sp.getParentleague_id() == 9) // soccer
            {
                display = "totalmoney";
            } else if (sp.getMoneylinedefault()) {
                display = "justmoney";
            } else {
                display = "spreadtotal";
            }
            */

        }
        String topboxS = "";
        String bottomboxS = "";
        String drawboxS = "";
        String totalboxS = "";

        boolean bestvisitspread = false;
        boolean besthomespread = false;
        boolean bestvisitmoney = false;
        boolean besthomemoney = false;
        boolean bestdrawmoney = false;
        boolean bestover = false;
        boolean bestunder = false;

        if (game.getStatus().equalsIgnoreCase("Time") && period == 0) {
            period = 2;
        }

        sl = AppController.getSpreadline(bid, gid, period);
        tl = AppController.getTotalline(bid, gid, period);
        ml = AppController.getMoneyline(bid, gid, period);
        ttl = AppController.getTeamTotalline(bid, gid, period);


        double visitspread;
        double visitjuice = -150;
        double homespread;
        double homejuice = -150;
        double over;
        double overjuice;
        double under;
        double underjuice;

        double visitmljuice;
        double homemljuice;
        double drawmljuice;


        double visitover;
        double visitoverjuice;
        double visitunder;
        double visitunderjuice;
        double homeover;
        double homeoverjuice;
        double homeunder;
        double homeunderjuice;


        String whowasbetspread = "";
        String whowasbettotal = "";
        String whowasbetmoney = "";
        String whowasbetteamtotal = "";

        long tsnow = System.currentTimeMillis();
        visitspread = null == sl ? SiaConst.DefaultSpread : sl.getCurrentvisitspread();
        homespread = null == sl ? SiaConst.DefaultSpread : sl.getCurrenthomespread();
        visitjuice = null == sl ? SiaConst.DefaultSpread : sl.getCurrentvisitjuice();
        homejuice = null == sl ? SiaConst.DefaultSpread : sl.getCurrenthomejuice();
        whowasbetspread = null == sl ? "" : sl.getWhowasbet();

        if (null == sl) {
            spreadcolor = Color.WHITE;
        } else if (tsnow - sl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000 && clearts < sl.getCurrentts()) //&& !iswhite(userDisplaySettings.getFirstcolor()))
        {
            spreadcolor = userDisplaySettings.getFirstcolor();
        }
        else if (tsnow - sl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000 + userDisplaySettings.getSecondmoveseconds()*1000  && clearts < sl.getCurrentts())// && !iswhite(userDisplaySettings.getSecondcolor()))
        {
            spreadcolor = userDisplaySettings.getSecondcolor();
        }
        else if (clearts < sl.getCurrentts())// && !iswhite(userDisplaySettings.getThirdcolor()))
        {
            spreadcolor = userDisplaySettings.getThirdcolor();
            //owen took out cuz maionscreen refreshes every sec
            //FireThreadManager.remove("S"+id);
        } else {
            spreadcolor = Color.WHITE;
        }
        priorspreadcolor = spreadcolor;


        over = null == tl ? SiaConst.DefaultOver : tl.getCurrentover();
        overjuice = null == tl ? SiaConst.DefaultOver : tl.getCurrentoverjuice();
        under = null == tl ? SiaConst.DefaultOver : tl.getCurrentunder();
        underjuice = null == tl ? SiaConst.DefaultOver : tl.getCurrentunderjuice();
        whowasbettotal = null == tl ? "" : tl.getWhowasbet();

        if (null == tl) {
            totalcolor = Color.WHITE;
        } else if (tsnow - tl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000  && clearts < tl.getCurrentts()) {
            totalcolor = userDisplaySettings.getFirstcolor();
        }
        else if (tsnow - tl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000 + userDisplaySettings.getSecondmoveseconds()*1000  && clearts < tl.getCurrentts()) {
            totalcolor = userDisplaySettings.getSecondcolor();
        }
        else if (clearts < tl.getCurrentts()) {
            totalcolor = userDisplaySettings.getThirdcolor();
            //owen took out cuz maionscreen refreshes every sec
            //FireThreadManager.remove("T"+id);
        } else {
            totalcolor = Color.WHITE;
        }
        priortotalcolor = totalcolor;


        visitmljuice = null == ml ? SiaConst.DefaultSpread : ml.getCurrentvisitjuice();
        homemljuice = null == ml ? SiaConst.DefaultSpread : ml.getCurrenthomejuice();
        drawmljuice = null == ml ? SiaConst.DefaultSpread : ml.getCurrentdrawjuice();
        whowasbetmoney = null == ml ? "" : ml.getWhowasbet();

        if (null == ml) {
            moneycolor = Color.WHITE;
        } else if (tsnow - ml.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000  && clearts < ml.getCurrentts()) {
            moneycolor = userDisplaySettings.getFirstcolor();
        }
        else if (tsnow - ml.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000 + userDisplaySettings.getSecondmoveseconds()*1000  && clearts < ml.getCurrentts()) {
            moneycolor = userDisplaySettings.getSecondcolor();
        }
        else if (clearts < ml.getCurrentts()) {
            moneycolor = userDisplaySettings.getThirdcolor();
            //owen took out cuz maionscreen refreshes every sec
            //FireThreadManager.remove("M"+id);
        } else {
            moneycolor = Color.WHITE;
        }
        priormoneycolor = moneycolor;

        visitover = null == ttl ? SiaConst.DefaultOver : ttl.getCurrentvisitover();
        homeover = null == ttl ? SiaConst.DefaultOver : ttl.getCurrenthomeover();
        whowasbetteamtotal = null == ttl ? "" : ttl.getWhowasbet();

        if (null == ttl) {
            teamtotalcolor = Color.WHITE;
        } else if (tsnow - ttl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000  && clearts < ttl.getCurrentts()) {
            teamtotalcolor = userDisplaySettings.getFirstcolor();
        }
        else if (tsnow - ttl.getCurrentts() <= userDisplaySettings.getFirstmoveseconds()*1000 + userDisplaySettings.getSecondmoveseconds()*1000  && clearts < ttl.getCurrentts()) {
            teamtotalcolor = userDisplaySettings.getSecondcolor();
        }
        //else if(priortotalcolor != Color.WHITE)
        else if (clearts < ttl.getCurrentts()) {
            teamtotalcolor = userDisplaySettings.getThirdcolor();
            //owen took out cuz maionscreen refreshes every sec
            //FireThreadManager.remove("TT"+id);
        } else {
            teamtotalcolor = Color.WHITE;
        }
        priorteamtotalcolor = teamtotalcolor;


        if (display.equals("spreadtotal")) {
            ///-adding just spread code here
            topboxS = "";
            bottomboxS = "";
            //totalcolor = drawcolor = Color.WHITE;

            if (visitspread == SiaConst.DefaultSpread) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE; //spreadcolor;
                //totalcolor2 = drawcolor = Color.WHITE;
            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedCurrentSpread();
                        bottomboxS = sl.getOtherPrintedCurrentSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedCurrentSpread();
                        topboxS = sl.getOtherPrintedCurrentSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "+" + sl.getOtherPrintedCurrentSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedCurrentSpread();
                    topboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }

                if (sl.isBestVisitSpread()) {
                    topborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    bottomborder += "besthomespread";
                }


                topcolor = bottomcolor = spreadcolor;
                totalcolor2 = drawcolor = spreadcolor; // why is this here????
                if (whowasbetspread.equals("h")) {
                    bottomicon = ICON_DOWN;
                    topicon = null;
                } else if (whowasbetspread.equals("v")) {
                    topicon = ICON_UP;
                    bottomicon = null;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
            }

            ///-adding just money to spread money

            //totalcolor = drawcolor = Color.WHITE;
            if (over == 99999) {
                drawboxS = "";
                totalboxS = "";

                totalcolor2 = drawcolor = Color.WHITE;
            } else {
                String tot1 = tl.getShortPrintedCurrentTotal();
                String tot2 = tl.getOtherPrintedCurrentTotal();

                if (tot1.contains("o")) {
                    drawboxS = tot1;
                    totalboxS = tot2;
                } else {
                    drawboxS = tot2;
                    totalboxS = tot1;
                }


                totalcolor2 = drawcolor = totalcolor; // owen added totalcolor2
                if (whowasbettotal.equals("u")) {
                    totalicon = ICON_DOWN;
                    drawicon = null;
                } else if (whowasbettotal.equals("o")) {
                    drawicon = ICON_UP;
                    totalicon = null;
                } else {
                    drawicon = totalicon = ICON_BLANK;
                }
                if (tl.isBestOver()) {
                    drawborder += "bestover";
                }
                if (tl.isBestUnder()) {
                    totalborder += "bestunder";
                }

            }


        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {

            showcomebacks = display.equals("totalbothmoney");

            if (visitmljuice == -99999) {

                topboxS = "";
                bottomboxS = "";
                drawboxS = "";
                totalboxS = "";
                topicon = bottomicon = drawicon = null;
                topcolor = bottomcolor = drawcolor = Color.WHITE; //moneycolor;
                totalcolor2 = Color.WHITE; // owen may not need this

            }
            //else if(visitmljuice < 0)

            else if (visitmljuice < homemljuice) {

                topboxS = ml.getPrintedJuiceLine(visitmljuice);

                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedCurrentTotal();
                }


                topcolor = bottomcolor = moneycolor;
                drawcolor = moneycolor;
                totalcolor2 = totalcolor;

                if (whowasbetmoney.equals("h")) {
                    topicon = ICON_DOWN;
                    bottomicon = ICON_UP;
                    topicon = bottomicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    topicon = ICON_UP;
                    //bottomicon = ICON_BLANK;
                    bottomicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                } else // draw?
                {
                    topicon = bottomicon = ICON_BLANK;
                    //drawicon = ICON_DOWN;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if (ml.isBestDrawMoney()) {
                    drawborder += "bestdrawmoney";
                }
            } else {

                topboxS = ml.getPrintedJuiceLine(visitmljuice);

                bottomboxS = ml.getPrintedJuiceLine(homemljuice);

                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedCurrentTotal();
                }


                topcolor = bottomcolor = moneycolor;
                drawcolor = moneycolor;
                //totalcolor2 = drawcolor = moneycolor; // owen took this out
                totalcolor2 = totalcolor;
                if (whowasbetmoney.equals("h")) {
                    bottomicon = ICON_UP;
                    topicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    bottomicon = ICON_DOWN;
                    topicon = ICON_UP;
                    topicon = bottomicon = ICON_BLANK;
                } else // draw?
                {
                    topicon = bottomicon = ICON_BLANK;
                    //drawicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if (ml.isBestDrawMoney()) {
                    drawborder += "bestdrawmoney";
                }

            }
        } else if (display.equals("justspread")) {
            drawboxS = "";
            totalboxS = "";
            if (visitspread == -99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE; //spreadcolor;
                totalcolor2 = drawcolor = Color.WHITE;
            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedCurrentSpread();
                        bottomboxS = sl.getOtherPrintedCurrentSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedCurrentSpread();
                        topboxS = sl.getOtherPrintedCurrentSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "+" + sl.getOtherPrintedCurrentSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedCurrentSpread();
                    topboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }

                if (sl.isBestVisitSpread()) {
                    topborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    bottomborder += "besthomespread";
                }


                topcolor = bottomcolor = spreadcolor;
                totalcolor2 = drawcolor = spreadcolor; // seems not needed
                if (whowasbetspread.equals("h")) {
                    bottomicon = ICON_DOWN;
                    topicon = null;
                } else if (whowasbetspread.equals("v")) {
                    topicon = ICON_UP;
                    bottomicon = null;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
            }
        } else if (display.equals("justmoney")) {
            if (visitmljuice == -99999) {
                topboxS = "";
                bottomboxS = "";
                drawboxS = "";
                totalboxS = "";
                topicon = bottomicon = null;
                topcolor = bottomcolor = drawcolor = Color.WHITE; //moneycolor;
                totalcolor2 = drawcolor = Color.WHITE;

            }
            //else if(visitmljuice < 0)
            else if (visitmljuice < homemljuice) {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                //adding draw box
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);
                topcolor = bottomcolor = drawcolor = moneycolor;
                totalcolor2 = Color.WHITE;
                if (whowasbetmoney.equals("h")) {
                    topicon = ICON_DOWN;
                    bottomicon = ICON_UP;
                    topicon = bottomicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    topicon = ICON_UP;
                    bottomicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                    //drawicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if (ml.isBestDrawMoney()) {
                    drawborder += "bestdrawmoney";
                }
            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);
                topcolor = bottomcolor = drawcolor = moneycolor;
                totalcolor2 = Color.WHITE;
                if (whowasbetmoney.equals("h")) {
                    bottomicon = ICON_UP;
                    topicon = ICON_DOWN;
                    topicon = bottomicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    bottomicon = ICON_DOWN;
                    topicon = ICON_UP;
                    topicon = bottomicon = ICON_BLANK;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if (ml.isBestDrawMoney()) {
                    drawborder += "bestdrawmoney";
                }

            }
        } else if (display.equals("justtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (over == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
                totalcolor2 = drawcolor = Color.WHITE;
            } else {
                String tot1 = tl.getShortPrintedCurrentTotal();
                String tot2 = tl.getOtherPrintedCurrentTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }


                topcolor = bottomcolor = totalcolor;
                if (whowasbettotal.equals("u")) {
                    bottomicon = ICON_DOWN;
                    topicon = null;
                } else if (whowasbettotal.equals("o")) {
                    topicon = ICON_UP;
                    bottomicon = null;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
                if (tl.isBestOver()) {
                    topborder += "bestover";
                }
                if (tl.isBestUnder()) {
                    bottomborder += "bestunder";
                }

            }
        } else if (display.equals("awayteamtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (visitover == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
                totalcolor2 = drawcolor = Color.WHITE;
            } else {
                String tot1 = ttl.getShortPrintedCurrentVisitTotal();
                String tot2 = ttl.getOtherPrintedCurrentVisitTotal();
                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }


                topcolor = bottomcolor = totalcolor;
                if (whowasbetteamtotal.equals("u")) {
                    bottomicon = ICON_DOWN;
                    topicon = null;
                } else if (whowasbetteamtotal.equals("o")) {
                    topicon = ICON_UP;
                    bottomicon = null;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ttl.isBestVisitOver()) {
                    topborder += "bestvisitover";
                }
                if (ttl.isBestVisitUnder()) {
                    bottomborder += "bestvisitunder";
                }

            }
        } else if (display.equals("hometeamtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (homeover == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
                totalcolor2 = drawcolor = Color.WHITE;
            } else {
                String tot1 = ttl.getShortPrintedCurrentHomeTotal();
                String tot2 = ttl.getOtherPrintedCurrentHomeTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }


                topcolor = bottomcolor = totalcolor;
                drawcolor = totalcolor2 = totalcolor;
                if (whowasbetteamtotal.equals("u")) {
                    bottomicon = ICON_DOWN;
                    topicon = null;
                } else if (whowasbetteamtotal.equals("o")) {
                    topicon = ICON_UP;
                    bottomicon = null;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ttl.isBestHomeOver()) {
                    topborder += "besthomeover";
                }
                if (ttl.isBestHomeUnder()) {
                    bottomborder += "besthomeunder";
                }

            }
        }
        if (topicon == null) {
            topicon = ICON_BLANK;
        }
        if (bottomicon == null) {
            bottomicon = ICON_BLANK;
        }
        if (topboxS.equals("")) {
            topicon = ICON_BLANK;
        }
        if (bottomboxS.equals("")) {
            bottomicon = ICON_BLANK;
        }

        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);
        ld3.setIconPath(drawicon);
        ld4.setIconPath(totalicon);

        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld3.setData(drawboxS);
        ld4.setData(totalboxS);

        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        ld3.setBackgroundColor(drawcolor);
        ld4.setBackgroundColor(totalcolor2);

        ld1.setBorder(topborder);
        ld2.setBorder(bottomborder);
        ld3.setBorder(drawborder);
        ld4.setBorder(totalborder);


        boxes[0] = ld1;
        boxes[1] = ld2;
        boxes[2] = ld3;
        boxes[3] = ld4;

        setCurrentBoxes(boxes);
        //if(sl == null) sl = Spreadline.sl;
        //if(tl == null) tl = Totalline.tl;
        //if(ml == null) ml = Moneyline.ml;

        try {
            if (game != null && (!topboxS.equals("") || !bottomboxS.equals(""))) {
                if(bid == 996 ) // seperate tooltip for best
                {

                    String besthtml = "<table><th></th><th>v/o</th><th>h/u</th>";
                    Bookie visitspreadbookie = AppController.bestvisitspread.get(period+"-"+gid);
                    Bookie homespreadbookie = AppController.besthomespread.get(period+"-"+gid);
                    Bookie overbookie = AppController.bestover.get(period+"-"+gid);
                    Bookie underbookie = AppController.bestunder.get(period+"-"+gid);
                    Bookie visitmlbookie = AppController.bestvisitml.get(period+"-"+gid);
                    Bookie homemlbookie = AppController.besthomeml.get(period+"-"+gid);

                    String vsb = "";
                    String hsb = "";
                    String ob = "";
                    String ub = "";
                    String vmb = "";
                    String hmb = "";
                    if(visitspreadbookie != null)
                    {
                        vsb = visitspreadbookie.getShortname();
                    }
                    if(homespreadbookie != null)
                    {
                        hsb = homespreadbookie.getShortname();
                    }
                    if(overbookie != null)
                    {
                        ob = overbookie.getShortname();
                    }
                    if(underbookie != null)
                    {
                        ub = underbookie.getShortname();
                    }
                    if(visitmlbookie != null)
                    {
                        vmb = visitmlbookie.getShortname();
                    }
                    if(homemlbookie != null)
                    {
                        hmb = homemlbookie.getShortname();
                    }

                    besthtml = besthtml+"<tr><td>S:</td><td><table border=1><tr><td align=center>"+vsb+"</td><tr><td>"+format(visitspread)+format(visitjuice)+"</td></tr></table></td><td><table border=1><tr><td align=center>"+hsb+"</td><tr><td>"+format(homespread)+format(homejuice)+"</td></tr></table></td></tr>";
                    besthtml = besthtml+"<tr><td>T:</td><td><table border=1><tr><td align=center>"+ob+"</td><tr><td>o"+format(over)+format(overjuice)+"</td></tr></table></td><td><table border=1><tr><td align=center>"+ub+"</td><tr><td>u"+format(under)+format(underjuice)+"</td></tr></table></td></tr>";
                    besthtml = besthtml+"<tr><td>M:</td><td><table border=1><tr><td align=center>"+vmb+"</td><tr><td>"+format(visitmljuice)+"</td></tr></table></td><td><table border=1><tr><td align=center>"+hmb+"</td><tr><td>"+format(homemljuice)+"</td></tr></table></td></tr>";


                    besthtml = besthtml+"</table>";
                    setTooltiptext("<html><body>" +besthtml + "</body></html>");
                }
                else if(bid == 997 )
                {
                    String consensushtml = "" ;
                    ConsensusMakerSettings cms = AppController.getConsensusMakerSettingsForThisGame(gid);
                    if(cms == null)
                    {

                       }
                    else
                    {
                        consensushtml =  cms.gethtmlbreakdown();
                    }

                    setTooltiptext("<html><body>" +consensushtml + "</body></html>");
                }
                else {
                    String limithtml = "";
                    int sidelimit = 0;
                    int totallimit = 0;
                    int moneylimit = 0;
                    if (sl != null) {
                        sidelimit = sl.getLimit();
                    }
                    if (tl != null) {
                        totallimit = tl.getLimit();
                    }
                    if (ml != null) {
                        moneylimit = ml.getLimit();
                    }

                    limithtml = sidelimit + " / " + totallimit + " / " + moneylimit;
                    if (!limithtml.equals("0 / 0 / 0")) {
                        setTooltiptext("<html><body>" + limithtml + "</body></html>");
                    }
                }
                linehistoryurl = linehistoryurl+"gameNum="+gid+"&bookieID="+bid+"&period="+period+"&lineType="+display;
                setUrl(linehistoryurl);
                /*
                setTooltiptext("<html><body>" +
                        "<table border=1>" +
                        "<tr>" +
                        "<td align='center'><b><u>" + bookie.getName() + "</u></b></td>" +
                        "<td align='center'><b>Cur</b></td>" +
                        "<td align='center'><b>Lst</b></td>" +
                        "<td align='center'><b>Opn</b></td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td><table>" +
                        "<tr><td>" + game.getVisitorgamenumber() + "</td><td>" + game.getVisitorteam() + "</td></tr>" +
                        "<tr><td>" + game.getHomegamenumber() + "</td><td>" + game.getHometeam() + "</td></tr>" +
                        "</table></td>" +

                        "<td><table>" +
                        "<tr>");

                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getCurrentvisitspread() + "") + makeNullBlank(sl.getCurrentvisitjuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getCurrentvisitjuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeOverNullBlank(tl.getCurrentover() + "") + makeNullBlank(tl.getCurrentoverjuice() + "") + "</td>");
                }
                appendTooltipText("</tr><tr>");

                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getCurrenthomespread() + "") + makeNullBlank(sl.getCurrenthomejuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getCurrenthomejuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeUnderNullBlank(tl.getCurrentunder() + "") + makeNullBlank(tl.getCurrentunderjuice() + "") + "</td>");
                }


                appendTooltipText("</tr></table></td><td><table><tr>");

                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getPriorvisitspread() + "") + makeNullBlank(sl.getPriorvisitjuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getPriorvisitjuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeOverNullBlank(tl.getPriorover() + "") + makeNullBlank(tl.getPrioroverjuice() + "") + "</td>");
                }

                appendTooltipText("</tr><tr>");

                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getPriorhomespread() + "") + makeNullBlank(sl.getPriorhomejuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getPriorhomejuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeUnderNullBlank(tl.getPriorunder() + "") + makeNullBlank(tl.getPriorunderjuice() + "") + "</td>");
                }
                appendTooltipText("</tr></table></td><td><table><tr>");

                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getOpenervisitspread() + "") + makeNullBlank(sl.getOpenervisitjuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getOpenervisitjuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeOverNullBlank(tl.getOpenerover() + "") + makeNullBlank(tl.getOpeneroverjuice() + "") + "</td>");
                }

                appendTooltipText("</tr><tr>");
                if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getOpenerhomespread() + "") + makeNullBlank(sl.getOpenerhomejuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(ml.getOpenerhomejuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + makeUnderNullBlank(tl.getOpenerunder() + "") + makeNullBlank(tl.getOpenerunderjuice() + "") + "</td>");
                }

                appendTooltipText("</tr></table></td></tr></table></body></html>");
                   */
            }
        } catch (Exception ex) {
            log(ex);
        }
        return boxes;
    }

    public void setCurrentBoxes(LineData[] boxes) {
        this.boxes = boxes;
    }

    public LineData[] getOpenerBoxes() {
        if (display.equals("default")) {

            if (sp.getSport_id() == 3) // baseball
            {
                display = "totalbothmoney";
            } else if (sp.getParentleague_id() == 9) // soccer
            {
                display = "totalmoney";
            } else if (sp.getMoneylinedefault()) {
                display = "justmoney";
            } else {
                display = "spreadtotal";
            }
        }
        String topboxS = "";
        String bottomboxS = "";
        String drawboxS = "";
        String totalboxS = "";

        if (game.getStatus().equalsIgnoreCase("Time") && period == 0) {
            period = 2;
        }
        sl = AppController.getSpreadline(bid, gid, period);
        tl = AppController.getTotalline(bid, gid, period);
        ml = AppController.getMoneyline(bid, gid, period);
        ttl = AppController.getTeamTotalline(bid, gid, period);
        double visitspread;
        double visitjuice = -110;
        double homejuice = -110;
        double over;
        double visitmljuice;
        double homemljuice;
        double drawmljuice;

        double visitover;
        double homeover;

        if (null != sl) {
            visitspread = sl.getOpenervisitspread();
            visitjuice = sl.getOpenervisitjuice();
            homejuice = sl.getOpenerhomejuice();


        } else  // no line
        {
            visitspread = 99999;

        }

        if (null != tl) {
            over = tl.getOpenerover();
        } else {
            over = 99999;

        }

        if (null != ttl) {
            visitover = ttl.getOpenervisitover();
            homeover = ttl.getOpenerhomeover();

        } else {
            visitover = homeover = 99999;

        }

        if (null != ml) {
            visitmljuice = ml.getOpenervisitjuice();
            homemljuice = ml.getOpenerhomejuice();
            drawmljuice = ml.getOpenerdrawjuice();

        } else // no line
        {
            visitmljuice = homemljuice = 99999;
            drawmljuice = 99999;

        }

        if ("spreadtotal".equals(display)) {
            //--adding just spread code

            if (visitspread == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedOpenerSpread();
                        bottomboxS = sl.getOtherPrintedOpenerSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedOpenerSpread();
                        topboxS = sl.getOtherPrintedOpenerSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedOpenerSpread();
                    bottomboxS = "+" + sl.getOtherPrintedOpenerSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedOpenerSpread();
                    topboxS = "+" + sl.getOtherPrintedOpenerSpread();
                }
            }

            if (over == 99999) {
                drawboxS = "";
                totalboxS = "";
            } else {
                String tot1 = tl.getShortPrintedOpenerTotal();
                String tot2 = tl.getOtherPrintedOpenerTotal();

                if (tot1.contains("o")) {
                    drawboxS = tot1;
                    totalboxS = tot2;
                } else {
                    drawboxS = tot2;
                    totalboxS = tot1;
                }
            }

        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {

            //totalcolor = drawcolor = Color.WHITE;
            showcomebacks = display.equals("totalbothmoney");
            if (visitmljuice == 99999) {
                //log("x0");
                topboxS = "";
                //log("x1");
                bottomboxS = "";//owen took out ml.getPrintedJuiceLine(homemljuice);
                //log("x2");
                drawboxS = "";//owen took out ml.getPrintedJuiceLine(drawmljuice);
                //log("x3");

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedOpenerTotal();
                    //log("x4");
                }

            } else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);
                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedOpenerTotal();
                }
            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedOpenerTotal();
                }
            }

        } else if (display.equals("justspread")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (visitspread == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedOpenerSpread();
                        bottomboxS = sl.getOtherPrintedOpenerSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedOpenerSpread();
                        topboxS = sl.getOtherPrintedOpenerSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedOpenerSpread();
                    bottomboxS = "+" + sl.getOtherPrintedOpenerSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedOpenerSpread();
                    topboxS = "+" + sl.getOtherPrintedOpenerSpread();
                }
            }
        } else if (display.equals("justmoney")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (visitmljuice == 99999) {
                topboxS = "";
                bottomboxS = "";
                drawboxS = "";
                totalboxS = "";


            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

            }
        } else if (display.equals("justtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (over == 99999) {
                topboxS = "";
                bottomboxS = "";


            } else {
                String tot1 = tl.getShortPrintedOpenerTotal();
                String tot2 = tl.getOtherPrintedOpenerTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        } else if (display.equals("awayteamtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;
            if (visitover == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = ttl.getShortPrintedOpenerVisitTotal();
                String tot2 = ttl.getOtherPrintedOpenerVisitTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        } else if (display.equals("hometeamtotal")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;

            if (homeover == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = ttl.getShortPrintedOpenerHomeTotal();
                String tot2 = ttl.getOtherPrintedOpenerHomeTotal();

                if (tot1.indexOf("o") != -1) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        }


        //ld1.setIcon(ICON_BLANK);
        //ld2.setIcon(ICON_BLANK);


        ld1.setIconPath(null);
        ld2.setIconPath(null);
        ld3.setIconPath(null);
        ld4.setIconPath(null);

        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld3.setData(drawboxS);
        ld4.setData(totalboxS);
        if (!isopenerbookie) {
            ld1.setBackgroundColor(userDisplaySettings.getOpenercolor());
            ld2.setBackgroundColor(userDisplaySettings.getOpenercolor());
            ld3.setBackgroundColor(userDisplaySettings.getOpenercolor());
            ld4.setBackgroundColor(userDisplaySettings.getOpenercolor());
        } else {
            ld1.setBackgroundColor(Color.LIGHT_GRAY);
            ld2.setBackgroundColor(Color.LIGHT_GRAY);
            ld3.setBackgroundColor(Color.LIGHT_GRAY);
            ld4.setBackgroundColor(Color.LIGHT_GRAY);
        }

        ld1.setBorder("");
        ld2.setBorder("");
        ld3.setBorder("");
        ld4.setBorder("");

        openerboxes[0] = ld1;
        openerboxes[1] = ld2;
        openerboxes[2] = ld3;
        openerboxes[3] = ld4;
        setOpenerBoxes(openerboxes);
        return openerboxes;
    }

    public String makeNullBlank(String s) {
        if (s == null) {
            return "";
        } else if (s.startsWith("99999")) {
            return "";
        } else if (s.equals("0.0")) {
            s = "pk";
        } else if (!s.startsWith("-")) {
            s = "+" + s;

        }

        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;

    }

    public String makeOverNullBlank(String s) {
        if (s == null) {
            return "";
        } else if (s.startsWith("99999")) {
            return "";
        } else {
            s = "o" + s;
        }
        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    public String makeUnderNullBlank(String s) {
        if (s == null) {
            return "";
        } else if (s.startsWith("99999")) {
            return "";
        } else {
            s = "u" + s;
        }
        if (s.endsWith(".0")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    public void setOpenerBoxes(LineData[] boxes) {
        this.openerboxes = boxes;
    }

    public void setDisplayType(String d) {
        display = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
        boxes[2].setBackgroundColor(Color.WHITE);
        boxes[3].setBackgroundColor(Color.WHITE);
    }

    public void setPeriodType(int d) {
        period = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
        boxes[2].setBackgroundColor(Color.WHITE);
        boxes[3].setBackgroundColor(Color.WHITE);
    }

    public String toString() {
        return boxes[0].getData();
    }
    @Override
    public void clearColors(long cleartime) {
        priorspreadcolor = Color.WHITE;
        priortotalcolor = Color.WHITE;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
        boxes[2].setBackgroundColor(Color.WHITE);
        boxes[3].setBackgroundColor(Color.WHITE);
        clearts = cleartime;
    }

    public LineData[] getPriorBoxes() {
        if (isopenerbookie) {
            priorboxes = getOpenerBoxes();
            return priorboxes;
        }
        if (display.equals("default")) {

            if (sp.getSport_id() == 3) // baseball
            {
                display = "totalbothmoney";
            } else if (sp.getParentleague_id() == 9) // soccer
            {
                display = "totalmoney";
            } else if (sp.getMoneylinedefault()) {
                display = "justmoney";
            } else {
                display = "spreadtotal";
            }
        }
        String topboxS = "";
        String bottomboxS = "";
        String drawboxS = "";
        String totalboxS = "";

        if (game.getStatus().equalsIgnoreCase("Time") && period == 0) {
            period = 2;
        }
        sl = AppController.getSpreadline(bid, gid, period);
        tl = AppController.getTotalline(bid, gid, period);
        ml = AppController.getMoneyline(bid, gid, period);
        ttl = AppController.getTeamTotalline(bid, gid, period);
        double visitspread;
        double visitjuice = -110;
        double homespread;
        double homejuice = -110;
        double over;
        double overjuice;
        double under;
        double underjuice;
        double visitmljuice;
        double homemljuice;
        double drawmljuice;

        double visitover;
        double visitoverjuice;
        double visitunder;
        double visitunderjuice;
        double homeover;
        double homeoverjuice;
        double homeunder;
        double homeunderjuice;


        if (null != sl) {
            visitspread = sl.getPriorvisitspread();
            visitjuice = sl.getPriorvisitjuice();
            homespread = sl.getPriorhomespread();
            homejuice = sl.getPriorhomejuice();
        } else {
            visitspread = 99999;
        }


        if (bid == 204 && gid == 465) {
        }

        if ( null != tl) {
            over = tl.getPriorover();
            overjuice = tl.getPrioroverjuice();
            under = tl.getPriorunder();
            underjuice = tl.getPriorunderjuice();

        } else {
            over = 99999;

        }


        if ( null != ml) {
            visitmljuice = ml.getPriorvisitjuice();
            homemljuice = ml.getPriorhomejuice();
            drawmljuice = ml.getPriordrawjuice();

        } else {
            visitmljuice = homemljuice = 99999;
            drawmljuice = 99999;

        }


        if ( null != ttl) {
            visitover = ttl.getPriorvisitover();
            visitoverjuice = ttl.getPriorvisitoverjuice();
            visitunder = ttl.getPriorvisitunder();
            visitunderjuice = ttl.getPriorvisitunderjuice();
            homeover = ttl.getPriorhomeover();
            homeoverjuice = ttl.getPriorhomeoverjuice();
            homeunder = ttl.getPriorhomeunder();
            homeunderjuice = ttl.getPriorhomeunderjuice();

        } else {
            visitover = homeover = 99999;
        }

        if (display.equals("spreadtotal")) {
            //adding just spread code to top 2 boxes

            if (visitspread == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedPriorSpread();
                        bottomboxS = sl.getOtherPrintedPriorSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedPriorSpread();
                        topboxS = sl.getOtherPrintedPriorSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedPriorSpread();
                    bottomboxS = "+" + sl.getOtherPrintedPriorSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedPriorSpread();
                    topboxS = "+" + sl.getOtherPrintedPriorSpread();
                }
            }

            //adding just total code to bottom 2 boxes
            drawboxS = "";
            totalboxS = "";
            if (over == 99999) {
                drawboxS = "";
                totalboxS = "";

            } else {
                String tot1 = tl.getShortPrintedPriorTotal();
                String tot2 = tl.getOtherPrintedPriorTotal();

                if (tot1.contains("o")) {
                    drawboxS = tot1;
                    totalboxS = tot2;
                } else {
                    drawboxS = tot2;
                    totalboxS = tot1;
                }
            }

        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {
            if (display.equals("totalbothmoney")) {
                showcomebacks = true;
            } else {
                showcomebacks = false;
            }
            drawboxS = "";
            totalboxS = "";

            if (visitmljuice == 99999) {

                topboxS = "";
                bottomboxS = "";
                drawboxS = "";
                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedPriorTotal();
                }

            } else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);


                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedPriorTotal();
                }
            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedPriorTotal();
                }


            }


        } else if (display.equals("justspread")) {
            drawboxS = "";
            totalboxS = "";
            if (visitspread == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                if (visitspread == 0) {
                    if (visitjuice < homejuice) {
                        topboxS = sl.getShortPrintedPriorSpread();
                        bottomboxS = sl.getOtherPrintedPriorSpread();
                    } else {
                        bottomboxS = sl.getShortPrintedPriorSpread();
                        topboxS = sl.getOtherPrintedPriorSpread();
                    }
                } else if (visitspread < 0) {
                    topboxS = "-" + sl.getShortPrintedPriorSpread();
                    bottomboxS = "+" + sl.getOtherPrintedPriorSpread();
                } else {
                    bottomboxS = "-" + sl.getShortPrintedPriorSpread();
                    topboxS = "+" + sl.getOtherPrintedPriorSpread();
                }
            }
        } else if (display.equals("justmoney")) {
            drawboxS = "";
            totalboxS = "";

            if (visitmljuice == 99999) {
                topboxS = "";
                bottomboxS = "";
                drawboxS = "";
                totalboxS = "";

            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);

            }
        } else if (display.equals("justtotal")) {
            drawboxS = "";
            totalboxS = "";
            if (over == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = tl.getShortPrintedPriorTotal();
                String tot2 = tl.getOtherPrintedPriorTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        } else if (display.equals("awayteamtotal")) {
            drawboxS = "";
            totalboxS = "";

            if (visitover == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = ttl.getShortPrintedPriorVisitTotal();
                String tot2 = ttl.getOtherPrintedPriorVisitTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        } else if (display.equals("hometeamtotal")) {
            drawboxS = "";
            totalboxS = "";

            if (homeover == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = ttl.getShortPrintedPriorHomeTotal();
                String tot2 = ttl.getOtherPrintedPriorHomeTotal();

                if (tot1.contains("o")) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        }

        ld1.setIconPath(null);
        ld2.setIconPath(null);
        ld3.setIconPath(null);
        ld4.setIconPath(null);


        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld3.setData(drawboxS);
        ld4.setData(totalboxS);

        ld1.setBackgroundColor(userDisplaySettings.getLastcolor());
        ld2.setBackgroundColor(userDisplaySettings.getLastcolor());
        ld3.setBackgroundColor(userDisplaySettings.getLastcolor());
        ld4.setBackgroundColor(userDisplaySettings.getLastcolor());


        ld1.setBorder("");
        ld2.setBorder("");
        ld3.setBorder("");
        ld4.setBorder("");


        priorboxes[0] = ld1;
        priorboxes[1] = ld2;
        priorboxes[2] = ld3;
        priorboxes[3] = ld4;


        setPriorBoxes(priorboxes);
        return priorboxes;
    }

    public void setPriorBoxes(LineData[] boxes) {
        this.priorboxes = boxes;
    }
}
