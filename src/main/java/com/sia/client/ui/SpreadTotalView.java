package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.*;

import java.awt.*;
import java.util.Random;

import static com.sia.client.config.Utils.log;

public class SpreadTotalView extends ViewValue {
    public static String ICON_UP = ImageFile.ICON_UP;
    public static String ICON_DOWN = ImageFile.ICON_DOWN;
    public static String ICON_BLANK = ImageFile.ICON_BLANK;
    public String displayType = SiaConst.DefaultViewName;
    public int period = 0;
    Spreadline sl;
    Totalline tl;
    Moneyline ml;
    TeamTotalline ttl;
    LineData[] boxes = new LineData[2];
    LineData[] priorboxes = new LineData[2];
    LineData[] openerboxes = new LineData[2];
    String toptooltip;
    String bottomtooltip;
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
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
    int id;
    Bookie bookie;
    String topborder;
    String bottomborder = "";
    LinesTableData ltd;
    boolean showcomebacks = false;
    boolean isopenerbookie = false;
    private String topicon = ICON_BLANK;
    private String bottomicon = ICON_BLANK;
    private long clearts;
    private String linehistoryurl ="http://sof300732.com:9998/gamedetails/linehistory.jsp?";
    private final DisplayTransformer displayTransformer;



    public SpreadTotalView(int bid, int gid, long cleartime, LinesTableData ltd) {
        displayTransformer = new DisplayTransformer(gid);
        if (bid >= 1000) {
            isopenerbookie = true;
            if(bid != 1000) {
                bid = bid - 1000;
            }
        }
        this.bid = bid;
        this.gid = gid;
        topborder = bottomborder = "";
        bookie = AppController.getBookie(bid);
        setClearts(cleartime);
        id = new Random().nextInt();
        this.ltd = ltd;
        this.getCurrentBoxes();
    }

    public LineData[] getCurrentBoxes() {
        topborder = bottomborder = "";
        if (isopenerbookie) {
            boxes = getOpenerBoxes();
            return boxes;

        }

//        if (display.equals("default")) {
//            Sport sp = getSport();
//            if (sp.getSport_id() == 3) // baseball
//            {
//                display = "totalbothmoney";
//            } else if (sp.getParentleague_id() == 9) // soccer
//            {
//                display = "totalmoney";
//            } else if (sp.getMoneylinedefault()) {
//                display = "justmoney";
//            } else {
//                display = "spreadtotal";
//            }
//        }
        String topboxS = "";
        String bottomboxS = "";

        period = displayTransformer.transformPeriod(period);
//        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
//            period = 2;
//        }

        sl = AppController.getSpreadline(bid, gid, period);
        tl = AppController.getTotalline(bid, gid, period);
        ml = AppController.getMoneyline(bid, gid, period);
        ttl = AppController.getTeamTotalline(bid, gid, period);


        double visitspread;
        double homespread = 9999;
        double visitjuice;
        double homejuice;
        double over;
        double under = 1;
        double overjuice = -110;
        double underjuice = -110;
        double visitmljuice;
        double homemljuice;
        double visitover;
        double homeover;
        double visitunder = 1;
        double homeunder = 1;
        double visitoverjuice =-110;
        double homeoverjuice =-110;
        double visitunderjuice =-110;
        double homeunderjuice =-110 ;

        String whowasbetspread = "";
        String whowasbettotal = "";
        String whowasbetmoney = "";
        String whowasbetteamtotal = "";
//        long tsnow = System.currentTimeMillis();
        try {
            if (null != sl) {
                visitspread = sl.getCurrentvisitspread();
                homespread = sl.getCurrenthomespread();
                visitjuice = sl.getCurrentvisitjuice();
                homejuice = sl.getCurrenthomejuice();


                whowasbetspread = sl.getWhowasbet();
                if (shouldGoRed(sl))
                {
                    spreadcolor = UserDisplaySettings.getFirstcolor();
                }
                else if (shouldGoSecondColor(sl))
                {
                    spreadcolor = UserDisplaySettings.getSecondcolor();
                }
                else if (clearts < sl.getCurrentts()) {
                    spreadcolor = UserDisplaySettings.getThirdcolor();
                    //owen took out cuz maionscreen refreshes every sec
                    //FireThreadManager.remove("S"+id);
                } else {
                    spreadcolor = Color.WHITE;
                }
                priorspreadcolor = spreadcolor;
            } else {
                visitspread = -99999;
                visitjuice = homejuice = -99999;
            }

        } catch (Exception e) // no line
        {
            visitspread = -99999;
            visitjuice = homejuice = -99999;
            log(e);
        }


        try {
            if (null != tl) {
                over = tl.getCurrentover();
                under = tl.getCurrentunder();
                overjuice = tl.getCurrentoverjuice();
                underjuice = tl.getCurrentunderjuice();
                whowasbettotal = tl.getWhowasbet();

                if (shouldGoRed(tl))
                {
                    totalcolor = UserDisplaySettings.getFirstcolor();
                }
                else if (shouldGoSecondColor(tl))
                {
                    totalcolor = UserDisplaySettings.getSecondcolor();
                }
                 else if (clearts < tl.getCurrentts()) {
                    totalcolor = Color.BLACK;
                    //owen took out cuz maionscreen refreshes every sec
                    //FireThreadManager.remove("T"+id);
                } else {
                    totalcolor = Color.WHITE;
                }
                priortotalcolor = totalcolor;
            } else {
                over = 99999;
            }
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }


        try {
            if (null != ml) {
                visitmljuice = ml.getCurrentvisitjuice();
                homemljuice = ml.getCurrenthomejuice();
                whowasbetmoney = ml.getWhowasbet();

                if (shouldGoRed(ml))
                {
                    moneycolor = UserDisplaySettings.getFirstcolor();
                }
                else if (shouldGoSecondColor(ml))
                {
                    moneycolor = UserDisplaySettings.getSecondcolor();
                }
                else if (clearts < ml.getCurrentts())
                {
                    moneycolor = Color.BLACK;
                } else {
                    moneycolor = Color.WHITE;
                }
                priormoneycolor = moneycolor;
            } else {
                visitmljuice = homemljuice = -99999;
            }

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = -99999;
            log(e);
        }


        try {
            if (null != ttl) {
                visitover = ttl.getCurrentvisitover();
                homeover = ttl.getCurrenthomeover();
                visitunder = ttl.getCurrentvisitunder();
                homeunder = ttl.getCurrenthomeunder();
                visitoverjuice = ttl.getCurrentvisitoverjuice();
                homeoverjuice = ttl.getCurrenthomeoverjuice();
                visitunderjuice = ttl.getCurrentvisitunderjuice();
                homeunderjuice = ttl.getCurrenthomeunderjuice();
                whowasbetteamtotal = ttl.getWhowasbet();

                if (shouldGoRed(ttl))
                {
                    teamtotalcolor = UserDisplaySettings.getFirstcolor();
                }
                else if (shouldGoSecondColor(ttl))
                {
                    teamtotalcolor = UserDisplaySettings.getSecondcolor();
                }
               else if (clearts < ttl.getCurrentts()) {
                    teamtotalcolor = Color.BLACK;
                    //owen took out cuz maionscreen refreshes every sec
                    //FireThreadManager.remove("TT"+id);
                } else {
                    teamtotalcolor = Color.WHITE;
                }
                priorteamtotalcolor = teamtotalcolor;
            } else {
                visitover = 99999;
                homeover = 99999;
            }
        } catch (Exception ex) {
            visitover = 99999;
            homeover = 99999;
            log(ex);
        }

        String display = displayTransformer.transformDefault(displayType);
        if (display.equals("spreadtotal")) {
            if (visitspread == -99999) {
                topboxS = "";
                topcolor = Color.WHITE;
                if (over == 99999) {
                    bottomboxS = "";
                    bottomcolor = Color.WHITE;

                } else {
                    bottomboxS = tl.getShortPrintedCurrentTotal();
                    bottomcolor = totalcolor;
                    bottomtooltip = tl.showHistory();

                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }
                    if(overjuice+underjuice>0 && under >= over)
                    {
                        bottomborder += "scalp";
                    }

                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                    } else {
                        bottomicon = ICON_BLANK;
                    }

                }
            } else if (visitspread < 0 && homespread > 0) // visitor is the favorite
            {
                topboxS = sl.getShortPrintedCurrentSpread();
                topcolor = spreadcolor;
                toptooltip = sl.showHistory();
                if (sl.isBestVisitSpread()) {
                    topborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    topborder += "besthomespread";
                }
                if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                {
                    topborder += "scalp";
                }
                if (whowasbetspread.equals("h")) {
                    topicon = ICON_DOWN;
                } else if (whowasbetspread.equals("v")) {
                    topicon = ICON_UP;
                } else {
                    topicon = ICON_BLANK;
                }


                if (over == 99999) {
                    bottomboxS = "";
                    bottomcolor = Color.WHITE;
                } else {
                    bottomboxS = tl.getShortPrintedCurrentTotal();
                    bottomcolor = totalcolor;
                    bottomtooltip = tl.showHistory();
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }
                    if(overjuice+underjuice>0 && under >= over)
                    {
                        bottomborder += "scalp";
                    }
                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                    } else {
                        bottomicon = ICON_BLANK;
                    }
                }
            } else if (visitspread > 0 && homespread < 0) {
                if (over == 99999) {
                    topboxS = "";
                    topcolor = Color.WHITE;
                } else {
                    topboxS = tl.getShortPrintedCurrentTotal();
                    topcolor = totalcolor;
                    toptooltip = tl.showHistory();
                    if (tl.isBestOver()) {
                        topborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        topborder += "bestunder";
                    }
                    if(overjuice+underjuice > 0 && under >= over)
                    {
                        topborder += "scalp";
                    }
                    if (whowasbettotal.equals("u")) {
                        topicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        topicon = ICON_UP;
                        //log("BET MADE!!!"+bid+".."+gid+".."+whowasbettotal);
                    } else {
                        topicon = ICON_BLANK;
                    }
                }
                bottomboxS = sl.getShortPrintedCurrentSpread();
                bottomcolor = spreadcolor;
                bottomtooltip = sl.showHistory();
                if (sl.isBestVisitSpread()) {
                    bottomborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    bottomborder += "besthomespread";
                }
                if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                {
                    bottomborder += "scalp";
                }
                if (whowasbetspread.equals("h")) {
                    bottomicon = ICON_UP;
                } else if (whowasbetspread.equals("v")) {
                    bottomicon = ICON_DOWN;
                } else {
                    bottomicon = ICON_BLANK;
                }
            } else if(visitspread == homespread && visitspread == 0) // game is a pickem ....not nececsaiirry with best column!
            {
                if (visitjuice < homejuice) {
                    topboxS = sl.getShortPrintedCurrentSpread();
                    topcolor = spreadcolor;
                    toptooltip = sl.showHistory();
                    if (sl.isBestVisitSpread()) {
                        topborder += "bestvisitspread";
                    }
                    if (sl.isBestHomeSpread()) {
                        topborder += "besthomespread";
                    }
                    if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                    {
                        topborder += "scalp";
                    }
                    if (whowasbetspread.equals("h")) {
                        topicon = ICON_DOWN;
                    } else if (whowasbetspread.equals("v")) {
                        topicon = ICON_UP;
                    } else {
                        topicon = ICON_BLANK;
                    }
                    if (over == 99999) {
                        bottomboxS = "";
                        bottomcolor = Color.WHITE;
                    } else {
                        bottomboxS = tl.getShortPrintedCurrentTotal();
                        bottomcolor = totalcolor;
                        bottomtooltip = tl.showHistory();
                        if (tl.isBestOver()) {
                            bottomborder += "bestover";
                        }
                        if (tl.isBestUnder()) {
                            bottomborder += "bestunder";
                        }
                        if(overjuice+underjuice > 0 && under >= over)
                        {
                            bottomborder += "scalp";
                        }
                        if (whowasbettotal.equals("u")) {
                            bottomicon = ICON_DOWN;
                        } else if (whowasbettotal.equals("o")) {
                            bottomicon = ICON_UP;
                        } else {
                            bottomicon = ICON_BLANK;
                        }

                    }
                } else {
                    if (over == 99999) {
                        topboxS = "";
                        topcolor = Color.WHITE;
                    } else {
                        topboxS = tl.getShortPrintedCurrentTotal();
                        topcolor = totalcolor;
                        toptooltip = tl.showHistory();
                        if (tl.isBestOver()) {
                            topborder += "bestover";
                        }
                        if (tl.isBestUnder()) {
                            topborder += "bestunder";
                        }
                        if(overjuice+underjuice > 0 && under >= over)
                        {
                            topborder += "scalp";
                        }
                        if (whowasbettotal.equals("u")) {
                            topicon = ICON_DOWN;
                        } else if (whowasbettotal.equals("o")) {
                            topicon = ICON_UP;
                        } else {
                            topicon = ICON_BLANK;
                        }
                    }


                    bottomboxS = sl.getShortPrintedCurrentSpread();
                    bottomcolor = spreadcolor;
                    bottomtooltip = sl.showHistory();
                    if (sl.isBestVisitSpread()) {
                        bottomborder += "bestvisitspread";
                    }
                    if (sl.isBestHomeSpread()) {
                        bottomborder += "besthomespread";
                    }
                    if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                    {
                        bottomborder += "scalp";
                    }
                    if (whowasbetspread.equals("h")) {
                        bottomicon = ICON_UP;
                    } else if (whowasbetspread.equals("v")) {
                        bottomicon = ICON_DOWN;
                    } else {
                        bottomicon = ICON_BLANK;
                    }
                }

            }
            else
                { // two positives or twonegatvles
                    String sign = "";
                    if(visitspread > 0)
                    {
                        sign= "+";
                    }
                    topboxS = sign+sl.getShortPrintedCurrentSpread();
                    topcolor = spreadcolor;
                    toptooltip = sl.showHistory();
                    if (sl.isBestVisitSpread()) {
                        topborder += "bestvisitspread";
                    }
                    if (sl.isBestHomeSpread()) {
                        topborder += "besthomespread";
                    }
                    if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                    {
                        topborder += "scalp";
                    }
                    if (whowasbetspread.equals("h")) {
                        topicon = ICON_DOWN;
                    } else if (whowasbetspread.equals("v")) {
                        topicon = ICON_UP;
                    } else {
                        topicon = ICON_BLANK;
                    }


                    if (over == 99999) {
                        bottomboxS = "";
                        bottomcolor = Color.WHITE;
                    } else {
                        bottomboxS = tl.getShortPrintedCurrentTotal();
                        bottomcolor = totalcolor;
                        bottomtooltip = tl.showHistory();
                        if (tl.isBestOver()) {
                            bottomborder += "bestover";
                        }
                        if (tl.isBestUnder()) {
                            bottomborder += "bestunder";
                        }
                        if(overjuice+underjuice>0 && under >= over)
                        {
                            bottomborder += "scalp";
                        }
                        if (whowasbettotal.equals("u")) {
                            bottomicon = ICON_DOWN;
                        } else if (whowasbettotal.equals("o")) {
                            bottomicon = ICON_UP;
                        } else {
                            bottomicon = ICON_BLANK;
                        }
                    }
            }
        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {
            showcomebacks = display.equals("totalbothmoney");
            if (visitmljuice == -99999) {
                topboxS = "";
                topcolor = moneycolor;
                if (over == 99999) {
                    bottomboxS = "";
                    bottomcolor = Color.WHITE;
                } else {
                    bottomboxS = tl.getShortPrintedCurrentTotal();
                    bottomcolor = totalcolor;
                    bottomtooltip = tl.showHistory();
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }
                    if(overjuice+underjuice > 0 && under >= over)
                    {
                        bottomborder += "scalp";
                    }
                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                        topicon = null;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                        topicon = null;
                    } else {
                        bottomicon = ICON_BLANK;
                        topicon = null;
                    }

                }
            }
            //else if(visitmljuice < 0) // visitor is the favorite
            else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                if (showcomebacks && visitmljuice != 0) {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice) + "/" + ml.getPrintedJuiceLine(homemljuice);
                } else {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice);
                }

                topcolor = moneycolor;
                toptooltip = ml.showHistory();
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    topborder += "besthomemoney";
                }
                if(visitmljuice+homemljuice > 0)
                {
                    topborder += "scalp";
                }
                if (whowasbetmoney.equals("h")) {
                    topicon = ICON_DOWN;
                } else if (whowasbetmoney.equals("v")) {
                    topicon = ICON_UP;
                } else {
                    topicon = ICON_BLANK;
                }


                if (over == 99999) {
                    bottomboxS = "";
                    bottomcolor = Color.WHITE;
                } else {
                    bottomboxS = tl.getShortPrintedCurrentTotal();
                    bottomcolor = totalcolor;
                    bottomtooltip = tl.showHistory();
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }
                    if(overjuice+underjuice > 0 && under >= over)
                    {
                        bottomborder += "scalp";
                    }
                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                    } else {
                        bottomicon = ICON_BLANK;
                    }
                }
            }
            //else if(visitmljuice > 0)
            else {
                if (over == 99999) {
                    topboxS = "";
                    topcolor = Color.WHITE;
                } else {
                    topboxS = tl.getShortPrintedCurrentTotal();
                    topcolor = totalcolor;
                    toptooltip = tl.showHistory();
                    if (tl.isBestOver()) {
                        topborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        topborder += "bestunder";
                    }
                    if(overjuice+underjuice > 0 && under >= over)
                    {
                        topborder += "scalp";
                    }
                    if (whowasbettotal.equals("u")) {
                        topicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        topicon = ICON_UP;
                        //log.println("BET MADE!!!"+bid+".."+gid+".."+whowasbettotal);
                    } else {
                        topicon = ICON_BLANK;
                    }
                }

                if (showcomebacks && homemljuice != 0) {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice) + "/" + ml.getPrintedJuiceLine(visitmljuice);
                } else {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                }

                bottomcolor = moneycolor;
                bottomtooltip = ml.showHistory();
                if (ml.isBestVisitMoney()) {
                    bottomborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if(visitmljuice+homemljuice > 0)
                {
                    bottomborder += "scalp";
                }
                if (whowasbetmoney.equals("h")) {
                    bottomicon = ICON_UP;
                } else if (whowasbetmoney.equals("v")) {
                    bottomicon = ICON_DOWN;
                } else {
                    bottomicon = ICON_BLANK;
                }
            }

        } else if (display.equals("justspread")) {
            if (visitspread == -99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE; //spreadcolor;
            } else
                {
                if (visitspread == 0 && homespread == 0)
                {
                    if (visitjuice < homejuice)
                    {
                        topboxS = sl.getShortPrintedCurrentSpread();
                        bottomboxS = sl.getOtherPrintedCurrentSpread();
                    }
                    else
                    {
                        bottomboxS = sl.getShortPrintedCurrentSpread();
                        topboxS = sl.getOtherPrintedCurrentSpread();
                    }
                }
                else if (visitspread < 0 && homespread > 0)
                {
                    topboxS = "-" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }
                else if(visitspread > 0 && homespread < 0)
                {
                    bottomboxS = "-" + sl.getShortPrintedCurrentSpread();
                    topboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }
                // here are new cases for best column
                else if(visitspread < 0 && homespread < 0)
                {
                    topboxS = "-" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "-" + sl.getOtherPrintedCurrentSpread();
                }
                else if(visitspread > 0 && homespread > 0)
                {
                    topboxS = "+" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }
                else if(visitspread < homespread )
                {
                    topboxS = "+" + sl.getShortPrintedCurrentSpread();
                    bottomboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }
                else if(homespread <= visitspread)
                {
                    bottomboxS = "+" + sl.getShortPrintedCurrentSpread();
                    topboxS = "+" + sl.getOtherPrintedCurrentSpread();
                }
                if (sl.isBestVisitSpread()) {
                    topborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    bottomborder += "besthomespread";
                }
                if(visitjuice+homejuice > 0 && visitspread+homespread >= 0)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }


                topcolor = bottomcolor = spreadcolor;
                toptooltip = bottomtooltip = sl.showHistory();
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
        } else if (display.equals("justmoney"))
        {
            if (visitmljuice == -99999) {
                topboxS = "";
                bottomboxS = "";
                topicon = bottomicon = null;
                topcolor = bottomcolor = Color.WHITE; //moneycolor;


            }
            //else if(visitmljuice < 0)
            else if (visitmljuice < homemljuice) {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                topcolor = bottomcolor = moneycolor;
                toptooltip = bottomtooltip = ml.showHistory();
                if (whowasbetmoney.equals("h")) {
                    topicon = ICON_DOWN;
                    bottomicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    topicon = ICON_UP;
                    bottomicon = ICON_BLANK;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if(visitmljuice+homemljuice > 0)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }
            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                topcolor = bottomcolor = moneycolor;
                toptooltip = bottomtooltip = ml.showHistory();
                if (whowasbetmoney.equals("h")) {
                    bottomicon = ICON_UP;
                    topicon = ICON_BLANK;
                } else if (whowasbetmoney.equals("v")) {
                    bottomicon = ICON_DOWN;
                    topicon = ICON_BLANK;
                } else {
                    topicon = bottomicon = ICON_BLANK;
                }
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
                }
                if(visitmljuice+homemljuice > 0)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }

            }
        } else if (display.equals("justtotal")) {
            if (over == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
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
                toptooltip = bottomtooltip = tl.showHistory();
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
                if(overjuice+underjuice > 0 && under >= over)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }

            }
        } else if (display.equals("awayteamtotal")) {
            if (visitover == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
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
                toptooltip = bottomtooltip = ttl.showAwayHistory();
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
                if(visitoverjuice+visitunderjuice > 0 && visitunder >= visitover)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }
            }
        } else if (display.equals("hometeamtotal")) {
            if (homeover == 99999) {
                topboxS = "";
                bottomboxS = "";
                topcolor = bottomcolor = Color.WHITE;//totalcolor;
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
                toptooltip = bottomtooltip = ttl.showHomeHistory();
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
                if(homeoverjuice+homeunderjuice > 0 && homeunder >= homeover)
                {
                    topborder += "scalp";
                    bottomborder += "scalp";
                }

            }
        }

        try {
            Game game = getGame();
            if (game != null && (!topboxS.equals("") || !bottomboxS.equals("")))
            {
                if(bid == 996 ) // seperate tooltip for best
                {
                    //System.out.println("best gmid"+gid+"..topborder="+topborder+".."+bottomborder);
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
                   // setTooltiptext("<html><body>" +besthtml + "</body></html>");
                    toptooltip = bottomtooltip = "<html><body>" +besthtml + "</body></html>";
                }
                else
                {
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

                    limithtml = ""+sidelimit + " / " + totallimit + " / " + moneylimit;



                    if (!limithtml.equals("0 / 0 / 0"))
                    {
                        if(!UserDisplaySettings.isShowcpo())
                        {
                            toptooltip = "";
                            bottomtooltip = "";
                        }
                        toptooltip = "<table><tr><td colspan=2>S/T/M:</td><td align=right><b>"+limithtml+"</b></td></tr>"+toptooltip+"</table>";
                        bottomtooltip = "<table><tr><td colspan=2>S/T/M:</td><td align=right><b>"+limithtml+"</b></td></tr>"+bottomtooltip+"</table>";
                        //setTooltiptext("<html><body>" + limithtml + "</body></html>");
                    }
                    else if(UserDisplaySettings.isShowcpo())
                    {
                        toptooltip = "<table>"+toptooltip+"</table>";
                        bottomtooltip = "<table>"+bottomtooltip+"</table>";
                    }
                    else
                    {
                        toptooltip = bottomtooltip = "";
                    }
                }
                linehistoryurl = linehistoryurl+"gameNum="+gid+"&bookieID="+bid+"&period="+period+"&lineType="+display+"&strgameDate=";
                setUrl(linehistoryurl);

                if(toptooltip != null && !toptooltip.equals("") && bottomtooltip != null && !bottomtooltip.equals(""))
                {
                    toptooltip = "<html><body>" + toptooltip + "</body></html>";
                    bottomtooltip = "<html><body>" + bottomtooltip + "</body></html>";
                }
                if(topboxS.equals(""))
                {
                    toptooltip ="";

                }
                if(bottomboxS.equals(""))
                {
                    bottomtooltip ="";
                }

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
                    appendTooltipText("<td align='left' >" + sl.getLimit()+".."+makeNullBlank(sl.getCurrentvisitspread() + "") + makeNullBlank(sl.getCurrentvisitjuice() + "") + "</td>");
                }
                if (ml != null) {
                    appendTooltipText("<td align='left' >" + ml.getLimit()+".."+ makeNullBlank(ml.getCurrentvisitjuice() + "") + "</td>");
                }
                if (tl != null) {
                    appendTooltipText("<td align='left' >" + tl.getLimit()+".."+ makeOverNullBlank(tl.getCurrentover() + "") + makeNullBlank(tl.getCurrentoverjuice() + "") + "</td>");
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
            else
            {
                toptooltip ="";
                bottomtooltip ="";

            }
        } catch (Exception ex) {
            log(ex);
        }














        //testing
        //topicon = ICON_UP;
        //bottomicon = ICON_DOWN;



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
        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        ld1.setBorder(topborder);
        ld2.setBorder(bottomborder);
        ld1.setTooltip(toptooltip);
        ld2.setTooltip(bottomtooltip);


        boxes[0] = ld1;
        boxes[1] = ld2;
        setCurrentBoxes(boxes);



        return boxes;
    }

    public void setCurrentBoxes(LineData[] boxes) {
        this.boxes = boxes;
    }

    public LineData[] getOpenerBoxes() {
//        if (display.equals("default")) {
//            Sport sp = getSport();
//            if (sp.getSport_id() == 3) // baseball
//            {
//                display = "totalbothmoney";
//            } else if (sp.getParentleague_id() == 9) // soccer
//            {
//                display = "totalmoney";
//            } else if (sp.getMoneylinedefault()) {
//                display = "justmoney";
//            } else {
//                display = "spreadtotal";
//            }
//        }
        String topboxS = "";
        String bottomboxS = "";
        period = displayTransformer.transformPeriod(period);
//        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
//            period = 2;
//        }
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
        double visitover;
        double homeover;

        try {
            if (null != sl) {
                visitspread = sl.getOpenervisitspread();
                visitjuice = sl.getOpenervisitjuice();
                homejuice = sl.getOpenerhomejuice();
            } else {
                visitspread = 99999;
            }

        } catch (Exception e) // no line
        {
            visitspread = 99999;
            log(e);
        }

        try {
            if (null != tl) {
                over = tl.getOpenerover();
            } else {
                over = 99999;
            }
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }
        try {
            if (null != ttl) {
                visitover = ttl.getOpenervisitover();
                homeover = ttl.getOpenerhomeover();
            } else {
                visitover = homeover = 99999;
            }

        } catch (Exception ex) {
            visitover = homeover = 99999;
            log(ex);
        }

        try {
            if (null != ml) {
                visitmljuice = ml.getOpenervisitjuice();
                homemljuice = ml.getOpenerhomejuice();
            } else {
                visitmljuice = homemljuice = 99999;
            }

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = 99999;
            log(e);
        }

        String display = displayTransformer.transformDefault(displayType);
        if (display.equals("spreadtotal")) {
            if (visitspread == 99999) {
                topboxS = "";
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedOpenerTotal();
                }
            } else if (visitspread < 0) // visitor is the favorite
            {
                topboxS = sl.getShortPrintedOpenerSpread();
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedOpenerTotal();
                }
            } else if (visitspread > 0) {
                if (over == 99999) {
                    topboxS = "";
                } else {
                    topboxS = tl.getShortPrintedOpenerTotal();
                }
                bottomboxS = sl.getShortPrintedOpenerSpread();

            } else // game is a pickem
            {
                if (visitjuice < homejuice) {
                    topboxS = sl.getShortPrintedOpenerSpread();
                    if (over == 99999) {
                        bottomboxS = "";
                    } else {
                        bottomboxS = tl.getShortPrintedOpenerTotal();
                    }
                } else {
                    if (over == 99999) {
                        topboxS = "";
                    } else {
                        topboxS = tl.getShortPrintedOpenerTotal();
                    }


                    bottomboxS = sl.getShortPrintedOpenerSpread();
                }

            }
        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {
            showcomebacks = display.equals("totalbothmoney");
            if (visitmljuice == 99999) {
                topboxS = "";
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedOpenerTotal();
                }
            } else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                if (showcomebacks && visitmljuice != 0) {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice) + "/" + ml.getPrintedJuiceLine(homemljuice);
                } else {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice);
                }

                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedOpenerTotal();
                }
            }
            //else if(visitmljuice > 0)
            else {
                if (over == 99999) {
                    topboxS = "";
                } else {
                    topboxS = tl.getShortPrintedOpenerTotal();
                }

                if (showcomebacks && homemljuice != 0) {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice) + "/" + ml.getPrintedJuiceLine(visitmljuice);
                } else {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                }


            }
        } else if (display.equals("justspread")) {
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
            if (visitmljuice == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
            }
        } else if (display.equals("justtotal")) {
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
            if (homeover == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = ttl.getShortPrintedOpenerHomeTotal();
                String tot2 = ttl.getOtherPrintedOpenerHomeTotal();

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

        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        if (!isopenerbookie) {
            ld1.setBackgroundColor(UserDisplaySettings.getOpenercolor());
            ld2.setBackgroundColor(UserDisplaySettings.getOpenercolor());
        } else {
            ld1.setBackgroundColor(Color.LIGHT_GRAY);
            ld2.setBackgroundColor(Color.LIGHT_GRAY);
        }

        ld1.setBorder("");
        ld2.setBorder("");
        openerboxes[0] = ld1;
        openerboxes[1] = ld2;
        setOpenerBoxes(openerboxes);
        return openerboxes;
    }

    public void setOpenerBoxes(LineData[] boxes) {
        this.openerboxes = boxes;
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

    public void setDisplayType(String d) {
        displayType = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
    }

    public void setPeriodType(int d) {
        period = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
    }

    public String toString() {
        return boxes[0].getData();
    }

    public void clearColors(long cleartime) {
        priorspreadcolor = Color.WHITE;
        priortotalcolor = Color.WHITE;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);

        setClearts(cleartime);
    }

    private void setClearts(long clearTime) {
        this.clearts = clearTime;
    }

    public LineData[] getPriorBoxes() {
        if (isopenerbookie) {
            priorboxes = getOpenerBoxes();
            return priorboxes;
        }
//        if (display.equals("default")) {
//            Sport sp = getSport();
//            if (sp.getSport_id() == 3) // baseball
//            {
//                display = "totalbothmoney";
//            } else if (sp.getParentleague_id() == 9) // soccer
//            {
//                display = "totalmoney";
//            } else if (sp.getMoneylinedefault()) {
//                display = "justmoney";
//            } else {
//                display = "spreadtotal";
//            }
//        }
        String topboxS = "";
        String bottomboxS = "";
        period = displayTransformer.transformPeriod(period);
//        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
//            period = 2;
//        }
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
        double visitover;
        double homeover;


        if (null != sl) {
            visitspread = sl.getPriorvisitspread();
            visitjuice = sl.getPriorvisitjuice();
            homejuice = sl.getPriorhomejuice();
        } else {
            visitspread = 99999;
        }


        if (null != tl) {
            over = tl.getPriorover();
        } else {
            over = 99999;
        }


        if (null != ml) {
            visitmljuice = ml.getPriorvisitjuice();
            homemljuice = ml.getPriorhomejuice();
        } else {
            visitmljuice = homemljuice = 99999;
        }


        if (null != ttl) {
            visitover = ttl.getPriorvisitover();
            homeover = ttl.getPriorhomeover();
        } else {
            visitover = homeover = 99999;
        }

        String display = displayTransformer.transformDefault(displayType);
        if (display.equals("spreadtotal")) {
            if (visitspread == 99999) {
                topboxS = "";
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedPriorTotal();
                }
            } else if (visitspread < 0) // visitor is the favorite
            {
                topboxS = sl.getShortPrintedPriorSpread();
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedPriorTotal();
                }
            } else if (visitspread > 0) {
                if (over == 99999) {
                    topboxS = "";
                } else {
                    topboxS = tl.getShortPrintedPriorTotal();
                }
                bottomboxS = sl.getShortPrintedPriorSpread();

            } else // game is a pickem
            {
                if (visitjuice < homejuice) {
                    topboxS = sl.getShortPrintedPriorSpread();
                    if (over == 99999) {
                        bottomboxS = "";
                    } else {
                        bottomboxS = tl.getShortPrintedPriorTotal();
                    }
                } else {
                    if (over == 99999) {
                        topboxS = "";
                    } else {
                        topboxS = tl.getShortPrintedPriorTotal();
                    }


                    bottomboxS = sl.getShortPrintedPriorSpread();
                }

            }
        } else if (display.equals("totalmoney") || display.equals("totalbothmoney")) {
            showcomebacks = display.equals("totalbothmoney");
            if (visitmljuice == 99999) {
                topboxS = "";
                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedPriorTotal();
                }
            } else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                if (showcomebacks && visitmljuice != 0) {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice) + "/" + ml.getPrintedJuiceLine(homemljuice);
                } else {
                    topboxS = ml.getPrintedJuiceLine(visitmljuice);
                }

                if (over == 99999) {
                    bottomboxS = "";
                } else {
                    bottomboxS = tl.getShortPrintedPriorTotal();
                }
            }
            //else if(visitmljuice > 0)
            else {
                if (over == 99999) {
                    topboxS = "";
                } else {
                    topboxS = tl.getShortPrintedPriorTotal();
                }

                if (showcomebacks && homemljuice != 0) {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice) + "/" + ml.getPrintedJuiceLine(visitmljuice);
                } else {
                    bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                }


            }
        } else if (display.equals("justspread")) {
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
            if (visitmljuice == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
            }
        } else if (display.equals("justtotal")) {
            if (over == 99999) {
                topboxS = "";
                bottomboxS = "";

            } else {
                String tot1 = tl.getShortPrintedPriorTotal();
                String tot2 = tl.getOtherPrintedPriorTotal();

                if (tot1.indexOf("o") != -1) {
                    topboxS = tot1;
                    bottomboxS = tot2;
                } else {
                    topboxS = tot2;
                    bottomboxS = tot1;
                }
            }
        } else if (display.equals("awayteamtotal")) {
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
        if (bid == 204 && gid == 6829) {
            //	log.println("boxes 6829 cris "+topboxS+".."+bottomboxS);
        }


        ld1.setIconPath(null);
        ld2.setIconPath(null);
        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld1.setBackgroundColor(UserDisplaySettings.getLastcolor());
        ld2.setBackgroundColor(UserDisplaySettings.getLastcolor());
        ld1.setBorder("");
        ld2.setBorder("");
        priorboxes[0] = ld1;
        priorboxes[1] = ld2;
        setPriorBoxes(priorboxes);
        return priorboxes;
    }

    public void setPriorBoxes(LineData[] boxes) {
        this.priorboxes = boxes;
    }

    private boolean shouldGoRed(Line line) {
        long tsnow = System.currentTimeMillis();
        long curTime = line.getCurrentts();
        int ms = UserDisplaySettings.getFirstmoveseconds()*1000;
        boolean isRed = (tsnow - curTime) <= ms && clearts <= curTime;
        Game game = getGame();
        return isRed;
    }

    private boolean shouldGoSecondColor(Line line) {
        long tsnow = System.currentTimeMillis();
        long curTime = line.getCurrentts();
        int ms = UserDisplaySettings.getFirstmoveseconds()*1000 + UserDisplaySettings.getSecondmoveseconds()*1000;
        boolean issecondcolor = (tsnow - curTime) <= ms && clearts <= curTime;
        Game game = getGame();
        return issecondcolor;
    }

    public Game getGame() {
        return AppController.getGame(gid);
    }

    public Sport getSport() {
        return AppController.getSportByLeagueId(getGame().getLeague_id());
    }


}
