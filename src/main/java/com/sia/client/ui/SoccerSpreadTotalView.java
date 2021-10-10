package com.sia.client.ui;

import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.Spreadline;

import java.awt.Color;
import java.util.Random;

import static com.sia.client.config.Utils.log;

public class SoccerSpreadTotalView {
    public static String ICON_UP = ChartView.ICON_UP;
    public static String ICON_DOWN = ChartView.ICON_DOWN;
    public static String ICON_BLANK = "blank.gif";
    public String display = "default";
    public int period = 0;
    Spreadline sl;
    Totalline tl;
    Moneyline ml;
    TeamTotalline ttl;
    LineData topbox;
    LineData bottombox;
    LineData drawbox;
    LineData totalbox;
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
    private long clearts;
    int id;
    Game game;
    Bookie bookie;
    Sport sp;
    String topborder = "";
    String bottomborder = "";
    String drawborder;
    String totalborder = "";
    String tooltiptext;
    LinesTableData ltd;
    boolean showcomebacks = false;


    boolean isopenerbookie = false;

    public SoccerSpreadTotalView(int bid, int gid, long cleartime, LinesTableData ltd) {
        if (bid > 1000) {
            isopenerbookie = true;
            bid = bid - 1000;
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
        if (isopenerbookie) {
            boxes = getOpenerBoxes();
            return boxes;

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
        double visitjuice;
        double homejuice;
        double over;

        double visitmljuice;
        double homemljuice;
        double drawmljuice;


        double visitover;
        double homeover;
        String whowasbetspread = "";
        String whowasbettotal = "";
        String whowasbetmoney = "";

        String whowasbetteamtotal = "";
        long tsnow = System.currentTimeMillis();
        try {
            visitspread = sl.getCurrentvisitspread();
            visitjuice = sl.getCurrentvisitjuice();
            homejuice = sl.getCurrenthomejuice();

            whowasbetspread = sl.getWhowasbet();
            if (tsnow - sl.getCurrentts() <= 30000 && clearts < sl.getCurrentts()) {
                spreadcolor = Color.RED;
            }
            else if (clearts < sl.getCurrentts()) {
                spreadcolor = Color.BLACK;
                //owen took out cuz maionscreen refreshes every sec
                //FireThreadManager.remove("S"+id);
            } else {
                spreadcolor = Color.WHITE;
            }
            priorspreadcolor = spreadcolor;

        } catch (Exception e) // no line
        {
            visitspread = -99999;
            visitjuice = homejuice = -99999;
        }


        try {
            over = tl.getCurrentover();
            whowasbettotal = tl.getWhowasbet();
            if (tsnow - tl.getCurrentts()<= 30000 && clearts< tl.getCurrentts()) {
                totalcolor = Color.RED;
            }
            else if (clearts < tl.getCurrentts()) {
                totalcolor = Color.BLACK;
                //owen took out cuz maionscreen refreshes every sec
                //FireThreadManager.remove("T"+id);
            } else {
                totalcolor = Color.WHITE;
            }
            priortotalcolor = totalcolor;
        } catch (Exception ex) {
            over = 99999;
        }


        try {
            visitmljuice = ml.getCurrentvisitjuice();
            homemljuice = ml.getCurrenthomejuice();
            drawmljuice = ml.getCurrentdrawjuice();
            whowasbetmoney = ml.getWhowasbet();

            if (tsnow - ml.getCurrentts() <= 30000 && clearts < ml.getCurrentts()) {
                moneycolor = Color.RED;
            }
            else if (clearts < ml.getCurrentts()) {
                moneycolor = Color.BLACK;
            } else {
                moneycolor = Color.WHITE;
            }
            priormoneycolor = moneycolor;

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = -99999;
            drawmljuice = -99999;

        }


        try {
            visitover = ttl.getCurrentvisitover();
            homeover = ttl.getCurrenthomeover();
            whowasbetteamtotal = ttl.getWhowasbet();
            if (tsnow - ttl.getCurrentts() <= 30000 && clearts < ttl.getCurrentts()) {
                teamtotalcolor = Color.RED;
            } else if (clearts < ttl.getCurrentts()) {
                teamtotalcolor = Color.BLACK;
            } else {
                teamtotalcolor = Color.WHITE;
            }
            priorteamtotalcolor = teamtotalcolor;
        } catch (Exception ex) {
            visitover = 99999;
            homeover = 99999;
            log(ex);
        }


        if (display.equals("spreadtotal")) {
            ///-adding just spread code here
            topboxS = "";
            bottomboxS = "";
            //totalcolor = drawcolor = Color.WHITE;

            if (visitspread == -99999) {
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

                if (tot1.indexOf("o") != -1) {
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
            else if (visitmljuice < homemljuice) {

                topboxS = ml.getPrintedJuiceLine(visitmljuice);

                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                //adding draw box

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
                    topicon = ICON_DOWN;
                    //bottomicon = ICON_BLANK;
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

            //System.out.println("09");
        } else if (display.equals("justspread")) {
            drawboxS = "";
            totalboxS = "";
            //totalcolor = drawcolor = Color.WHITE;

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

                if (tot1.indexOf("o") != -1) {
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

                //System.out.println("bookie="+bid+".."+tot1+".."+tot2);

                if (tot1.indexOf("o") != -1) {
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

                if (tot1.indexOf("o") != -1) {
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
                tooltiptext = "<html><body>" +
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
                        "<tr>";

                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getCurrentvisitspread() + "") + makeNullBlank(sl.getCurrentvisitjuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getCurrentvisitjuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeOverNullBlank(tl.getCurrentover() + "") + makeNullBlank(tl.getCurrentoverjuice() + "") + "</td>";
                }
                tooltiptext = tooltiptext + "</tr>" +
                        "<tr>";

                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getCurrenthomespread() + "") + makeNullBlank(sl.getCurrenthomejuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getCurrenthomejuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeUnderNullBlank(tl.getCurrentunder() + "") + makeNullBlank(tl.getCurrentunderjuice() + "") + "</td>";
                }


                tooltiptext = tooltiptext + "</tr>" +
                        "</table></td>" +


                        "<td><table>" +
                        "<tr>";
                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getPriorvisitspread() + "") + makeNullBlank(sl.getPriorvisitjuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getPriorvisitjuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeOverNullBlank(tl.getPriorover() + "") + makeNullBlank(tl.getPrioroverjuice() + "") + "</td>";
                }

                tooltiptext = tooltiptext + "</tr>" +
                        "<tr>";
                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getPriorhomespread() + "") + makeNullBlank(sl.getPriorhomejuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getPriorhomejuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeUnderNullBlank(tl.getPriorunder() + "") + makeNullBlank(tl.getPriorunderjuice() + "") + "</td>";
                }
                tooltiptext = tooltiptext + "</tr>" +
                        "</table></td>" +

                        "<td><table>" +
                        "<tr>";
                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getOpenervisitspread() + "") + makeNullBlank(sl.getOpenervisitjuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getOpenervisitjuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeOverNullBlank(tl.getOpenerover() + "") + makeNullBlank(tl.getOpeneroverjuice() + "") + "</td>";
                }

                tooltiptext = tooltiptext + "</tr>" +

                        "<tr>";
                if (sl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(sl.getOpenerhomespread() + "") + makeNullBlank(sl.getOpenerhomejuice() + "") + "</td>";
                }
                if (ml != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeNullBlank(ml.getOpenerhomejuice() + "") + "</td>";
                }
                if (tl != null) {
                    tooltiptext = tooltiptext + "<td align='left' >" + makeUnderNullBlank(tl.getOpenerunder() + "") + makeNullBlank(tl.getOpenerunderjuice() + "") + "</td>";
                }

                tooltiptext = tooltiptext + "</tr>" +
                        "</table></td>" +
                        "</tr>" +


                        "</table></body></html>";

            }
        } catch (Exception ex) {
            System.out.println(ex + "err");
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
        try {
            visitspread = sl.getOpenervisitspread();
            visitjuice = sl.getOpenervisitjuice();
            homejuice = sl.getOpenerhomejuice();


        } catch (Exception e) // no line
        {
            visitspread = 99999;

        }

        try {
            over = tl.getOpenerover();
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }
        try {
            visitover = ttl.getOpenervisitover();
            homeover = ttl.getOpenerhomeover();

        } catch (Exception ex) {
            visitover = homeover = 99999;
            log(ex);
        }

        try {
            visitmljuice = ml.getOpenervisitjuice();
            homemljuice = ml.getOpenerhomejuice();
            drawmljuice = ml.getOpenerdrawjuice();

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = 99999;
            drawmljuice = 99999;

        }
        if (display.equals("spreadtotal")) {

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

            showcomebacks = display.equals("totalbothmoney");
            if (visitmljuice == 99999) {
                topboxS = "";
                bottomboxS = "";//owen took out ml.getPrintedJuiceLine(homemljuice);
                drawboxS = "";//owen took out ml.getPrintedJuiceLine(drawmljuice);

                if (over == 99999) {
                    totalboxS = "";
                } else {
                    totalboxS = tl.getShortPrintedOpenerTotal();

                }

            } else if (visitmljuice < homemljuice) // visitor is the favorite
            {
                //		System.out.println("y0");
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                //	System.out.println("y1");
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                //	System.out.println("y2");
                drawboxS = ml.getPrintedJuiceLine(drawmljuice);
                //	System.out.println("y3");

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

                if (tot1.indexOf("o") != -1) {
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
        ld3.setIconPath(null);
        ld4.setIconPath(null);

        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld3.setData(drawboxS);
        ld4.setData(totalboxS);
        if (!isopenerbookie) {
            ld1.setBackgroundColor(Color.GRAY);
            ld2.setBackgroundColor(Color.GRAY);
            ld3.setBackgroundColor(Color.GRAY);
            ld4.setBackgroundColor(Color.GRAY);
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

    public String getToolTip() {
        return tooltiptext;
    }

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
        double homejuice = -110;
        double over;
        double visitmljuice;
        double homemljuice;
        double drawmljuice;

        double visitover;
        double homeover;
        try {
            visitspread = sl.getPriorvisitspread();
            visitjuice = sl.getPriorvisitjuice();
            homejuice = sl.getPriorhomejuice();


        } catch (Exception e) // no line
        {
            visitspread = 99999;
            log(e);
        }

        if (bid == 204 && gid == 465) {
            //System.out.println("prior spread for 465 cris "+visitspread+".."+visitjuice);
        }

        try {
            over = tl.getPriorover();
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }


        try {
            visitmljuice = ml.getPriorvisitjuice();
            homemljuice = ml.getPriorhomejuice();
            drawmljuice = ml.getPriordrawjuice();

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = 99999;
            drawmljuice = 99999;

        }


        try {
            visitover = ttl.getPriorvisitover();
            homeover = ttl.getPriorhomeover();

        } catch (Exception ex) {
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
            showcomebacks = display.equals("totalbothmoney");

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

                if (tot1.indexOf("o") != -1) {
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

                if (tot1.indexOf("o") != -1) {
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

        if (bid == 204 && gid == 6829) {
            //	System.out.println("boxes 6829 cris "+topboxS+".."+bottomboxS);
        }


        ld1.setIconPath(null);
        ld2.setIconPath(null);
        ld3.setIconPath(null);
        ld4.setIconPath(null);


        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld3.setData(drawboxS);
        ld4.setData(totalboxS);

        ld1.setBackgroundColor(Color.BLUE);
        ld2.setBackgroundColor(Color.BLUE);
        ld3.setBackgroundColor(Color.BLUE);
        ld4.setBackgroundColor(Color.BLUE);


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
