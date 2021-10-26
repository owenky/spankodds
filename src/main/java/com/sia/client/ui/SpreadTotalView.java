package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.Line;
import com.sia.client.model.LineData;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.Spreadline;
import com.sia.client.model.ViewValue;

import java.awt.Color;
import java.util.Random;

import static com.sia.client.config.Utils.log;

public class SpreadTotalView extends ViewValue  {
    public static String ICON_UP = ImageFile.ICON_UP;
    public static String ICON_DOWN = ImageFile.ICON_DOWN;
    public static String ICON_BLANK = ImageFile.ICON_BLANK;
    public String display = "default";
    public int period = 0;
    Spreadline sl;
    Totalline tl;
    Moneyline ml;
    TeamTotalline ttl;
    LineData[] boxes = new LineData[2];
    LineData[] priorboxes = new LineData[2];
    LineData[] openerboxes = new LineData[2];
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
    private String topicon = ICON_BLANK;
    private String bottomicon = ICON_BLANK;
    private long clearts;
    int id;
    Bookie bookie;
    String topborder;
    String bottomborder = "";
    LinesTableData ltd;
    boolean showcomebacks = false;


    boolean isopenerbookie = false;

    public SpreadTotalView(int bid, int gid, long cleartime, LinesTableData ltd) {
        if (bid > 1000) {
            isopenerbookie = true;
            bid = bid - 1000;
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

        if (display.equals("default")) {
            Sport sp = getSport();
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

        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
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
        double visitover;
        double homeover;


        String whowasbetspread = "";
        String whowasbettotal = "";
        String whowasbetmoney = "";
        String whowasbetteamtotal = "";
        long tsnow = System.currentTimeMillis();
        try {
            if (null != sl) {
                visitspread = sl.getCurrentvisitspread();
                visitjuice = sl.getCurrentvisitjuice();
                homejuice = sl.getCurrenthomejuice();


                whowasbetspread = sl.getWhowasbet();
                if ( shouldGoRed(sl) ) {
                    spreadcolor = Color.RED;
                } else if (clearts < sl.getCurrentts()) {
                    spreadcolor = Color.BLACK;
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
            if ( null != tl) {
                over = tl.getCurrentover();
                whowasbettotal = tl.getWhowasbet();
                if (shouldGoRed(tl)) {
                    totalcolor = Color.RED;
                } else if (clearts < tl.getCurrentts()) {
                    totalcolor = Color.BLACK;
                    //owen took out cuz maionscreen refreshes every sec
                    //FireThreadManager.remove("T"+id);
                } else {
                    totalcolor = Color.WHITE;
                }
                priortotalcolor = totalcolor;
            }else {
                over = 99999;
            }
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }


        try {
            if ( null != ml) {
                visitmljuice = ml.getCurrentvisitjuice();
                homemljuice = ml.getCurrenthomejuice();
                whowasbetmoney = ml.getWhowasbet();

                if (shouldGoRed(ml)) {
                    moneycolor = Color.RED;
                } else if (clearts < ml.getCurrentts()) {
                    moneycolor = Color.BLACK;
                } else {
                    moneycolor = Color.WHITE;
                }
                priormoneycolor = moneycolor;
            }else {
                visitmljuice = homemljuice = -99999;
            }

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = -99999;
            log(e);
        }


        try {
            if ( null != ttl) {
                visitover = ttl.getCurrentvisitover();
                homeover = ttl.getCurrenthomeover();
                whowasbetteamtotal = ttl.getWhowasbet();
                if (shouldGoRed(ttl)) {
                    teamtotalcolor = Color.RED;
                } else if (clearts < ttl.getCurrentts()) {
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

                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }

                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                    } else {
                        bottomicon = ICON_BLANK;
                    }

                }
            } else if (visitspread < 0) // visitor is the favorite
            {
                topboxS = sl.getShortPrintedCurrentSpread();
                topcolor = spreadcolor;
                if (sl.isBestVisitSpread()) {
                    topborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    topborder += "besthomespread";
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
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
                    }
                    if (whowasbettotal.equals("u")) {
                        bottomicon = ICON_DOWN;
                    } else if (whowasbettotal.equals("o")) {
                        bottomicon = ICON_UP;
                    } else {
                        bottomicon = ICON_BLANK;
                    }
                }
            } else if (visitspread > 0) {
                if (over == 99999) {
                    topboxS = "";
                    topcolor = Color.WHITE;
                } else {
                    topboxS = tl.getShortPrintedCurrentTotal();
                    topcolor = totalcolor;
                    if (tl.isBestOver()) {
                        topborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        topborder += "bestunder";
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
                if (sl.isBestVisitSpread()) {
                    bottomborder += "bestvisitspread";
                }
                if (sl.isBestHomeSpread()) {
                    bottomborder += "besthomespread";
                }
                if (whowasbetspread.equals("h")) {
                    bottomicon = ICON_UP;
                } else if (whowasbetspread.equals("v")) {
                    bottomicon = ICON_DOWN;
                } else {
                    bottomicon = ICON_BLANK;
                }
            } else // game is a pickem
            {
                if (visitjuice < homejuice) {
                    topboxS = sl.getShortPrintedCurrentSpread();
                    topcolor = spreadcolor;
                    if (sl.isBestVisitSpread()) {
                        topborder += "bestvisitspread";
                    }
                    if (sl.isBestHomeSpread()) {
                        topborder += "besthomespread";
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
                        if (tl.isBestOver()) {
                            bottomborder += "bestover";
                        }
                        if (tl.isBestUnder()) {
                            bottomborder += "bestunder";
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
                        if (tl.isBestOver()) {
                            topborder += "bestover";
                        }
                        if (tl.isBestUnder()) {
                            topborder += "bestunder";
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
                    if (sl.isBestVisitSpread()) {
                        bottomborder += "bestvisitspread";
                    }
                    if (sl.isBestHomeSpread()) {
                        bottomborder += "besthomespread";
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
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
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
                if (ml.isBestVisitMoney()) {
                    topborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    topborder += "besthomemoney";
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
                    if (tl.isBestOver()) {
                        bottomborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        bottomborder += "bestunder";
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
                    if (tl.isBestOver()) {
                        topborder += "bestover";
                    }
                    if (tl.isBestUnder()) {
                        topborder += "bestunder";
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
                if (ml.isBestVisitMoney()) {
                    bottomborder += "bestvisitmoney";
                }
                if (ml.isBestHomeMoney()) {
                    bottomborder += "besthomemoney";
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
                topicon = bottomicon = null;
                topcolor = bottomcolor = Color.WHITE; //moneycolor;

            }
            //else if(visitmljuice < 0)
            else if (visitmljuice < homemljuice) {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                topcolor = bottomcolor = moneycolor;
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
            } else {
                topboxS = ml.getPrintedJuiceLine(visitmljuice);
                bottomboxS = ml.getPrintedJuiceLine(homemljuice);
                topcolor = bottomcolor = moneycolor;
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
        ld1.setData(topboxS);
        ld2.setData(bottomboxS);
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        ld1.setBorder(topborder);
        ld2.setBorder(bottomborder);


        boxes[0] = ld1;
        boxes[1] = ld2;
        setCurrentBoxes(boxes);
        //if(sl == null) sl = Spreadline.sl;
        //if(tl == null) tl = Totalline.tl;
        //if(ml == null) ml = Moneyline.ml;

        try {
            Game game = getGame();
            if (game != null && (!topboxS.equals("") || !bottomboxS.equals(""))) {
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
                    appendTooltipText( "<td align='left' >" + makeOverNullBlank(tl.getCurrentover() + "") + makeNullBlank(tl.getCurrentoverjuice() + "") + "</td>");
				}
                appendTooltipText( "</tr><tr>");

				if (sl != null) {
                    appendTooltipText("<td align='left' >" + makeNullBlank(sl.getCurrenthomespread() + "") + makeNullBlank(sl.getCurrenthomejuice() + "") + "</td>");
				}
				if (ml != null) {
                    appendTooltipText( "<td align='left' >" + makeNullBlank(ml.getCurrenthomejuice() + "") + "</td>");
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
            Sport sp = getSport();
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
        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
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
        double visitover;
        double homeover;

        try {
            if ( null != sl) {
                visitspread = sl.getOpenervisitspread();
                visitjuice = sl.getOpenervisitjuice();
                homejuice = sl.getOpenerhomejuice();
            }else {
                visitspread = 99999;
            }

        } catch (Exception e) // no line
        {
            visitspread = 99999;
            log(e);
        }

        try {
            if ( null != tl) {
                over = tl.getOpenerover();
            } else {
                over = 99999;
            }
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }
        try {
            if ( null != ttl) {
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
            if ( null != ml) {
                visitmljuice = ml.getOpenervisitjuice();
                homemljuice = ml.getOpenerhomejuice();
            }else {
                visitmljuice = homemljuice = 99999;
            }

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = 99999;
            log(e);
        }
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
            }
            else if (visitmljuice < homemljuice) // visitor is the favorite
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
            ld1.setBackgroundColor(Color.GRAY);
            ld2.setBackgroundColor(Color.GRAY);
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
        if (display.equals("default")) {
            Sport sp = getSport();
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
        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
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
        double visitover;
        double homeover;
        try {
            if ( null != sl) {
                visitspread = sl.getPriorvisitspread();
                visitjuice = sl.getPriorvisitjuice();
                homejuice = sl.getPriorhomejuice();
            } else {
                visitspread = 99999;
            }

        } catch (Exception e) // no line
        {
            visitspread = 99999;
            log(e);
        }

        if (bid == 204 && gid == 465) {
            //log.println("prior spread for 465 cris "+visitspread+".."+visitjuice);
        }

        try {
            if ( null != tl) {
                over = tl.getPriorover();
            } else {
                over = 99999;
            }
        } catch (Exception ex) {
            over = 99999;
            log(ex);
        }


        try {
            if ( null != ml) {
                visitmljuice = ml.getPriorvisitjuice();
                homemljuice = ml.getPriorhomejuice();
            } else {
                visitmljuice = homemljuice = 99999;
            }

        } catch (Exception e) // no line
        {
            visitmljuice = homemljuice = 99999;
            log(e);
        }


        try {
            if ( null != ttl) {
                visitover = ttl.getPriorvisitover();
                homeover = ttl.getPriorhomeover();
            } else {
                visitover = homeover = 99999;
            }
        } catch (Exception ex) {
            visitover = homeover = 99999;
            log(ex);
        }


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
            }
            else if (visitmljuice < homemljuice) // visitor is the favorite
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
        ld1.setBackgroundColor(Color.BLUE);
        ld2.setBackgroundColor(Color.BLUE);
        ld1.setBorder("");
        ld2.setBorder("");
        priorboxes[0] = ld1;
        priorboxes[1] = ld2;
        setPriorBoxes(priorboxes);
        return priorboxes;
    }
    private boolean shouldGoRed(Line line) {
        long tsnow = System.currentTimeMillis();
        long curTime = line.getCurrentts();
        boolean isRed=(tsnow - curTime) <= 30000 && clearts <= curTime;
        Game game = getGame();
        return isRed;
    }
    public void setPriorBoxes(LineData[] boxes) {
        this.priorboxes = boxes;
    }
    public Game getGame() {
        return AppController.getGame(gid);
    }
    public Sport getSport() {
        return AppController.getSportByLeagueId(getGame().getLeague_id());
    }
}
