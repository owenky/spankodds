package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Sport;

import java.awt.Color;

public class SoccerChartView {

    public static String ICON_UP = ImageFile.ICON_UP;
    public static String ICON_DOWN = ImageFile.ICON_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    public static LineData[] boxes = new LineData[4];
    public static LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    public static LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    public static LineData ld3 = new LineData(ICON_BLANK, "", Color.WHITE);
    public static LineData ld4 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData topbox;
    LineData bottombox;
    int gid;
    Game g;
    Sport sp;
    int period;
    String item = "spreadtotal";

    public SoccerChartView(int gid) {


        this.gid = gid;

        g = AppController.getGame(gid);
        sp = AppController.getSportByLeagueId(g.getLeague_id());
        this.getCurrentBoxes();
        //this.setAndGetPriorBoxes(bid,gid);
        //this.setAndGetOpenerBoxes(bid,gid);


    }

    public LineData[] getCurrentBoxes() {
        if ("Time".equalsIgnoreCase(g.getStatus()) && (0 == period)) {
            period = 2;
        }
        setCurrentBoxes();
        return boxes;
    }

    public void setCurrentBoxes() {

        ld1.setData("");
        ld2.setData("");
        ld3.setData("");
        ld4.setData("");

        for (int i = 0; i < ChartChecker.getCl1().size(); i++) {
            if ((ChartChecker.getCl1().get(i).gn == gid) && (ChartChecker.getCl1().get(i).p == period)) {
                if (!ChartChecker.getCl1().get(i).dataexists) {
                    break;
                }
                if ("default".equals(item)) {

                    if (3 == sp.getSport_id()) // baseball
                    {
                        item = "totalbothmoney";
                    } else if (9 == sp.getParentleague_id()) // soccer
                    {
                        item = "totalmoney";
                    } else if (sp.getMoneylinedefault()) {
                        item = "justmoney";
                    } else {
                        item = "spreadtotal";
                    }
                }

                if ("spreadtotal".equals(item)) {
                    ld1.setIconPath(ChartChecker.getCl1().get(i).spreadicon);
                    ld4.setIconPath(ChartChecker.getCl1().get(i).totalicon);

                    ld1.setBackgroundColor(ChartChecker.getCl1().get(i).spreadcolor);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(ChartChecker.getCl1().get(i).totalcolor);

                    ld1.setData(ChartChecker.getCl1().get(i).NDS);
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData(ChartChecker.getCl1().get(i).NDT);

                    //boxes[0] = ld1;
                    //boxes[1] = ld2;
                } else if ("totalmoney".equals(item) || "totalbothmoney".equals(item)) {
                    ld1.setIconPath(ChartChecker.getCl1().get(i).moneyicon);
                    ld4.setIconPath(ChartChecker.getCl1().get(i).totalicon);
                    ld1.setBackgroundColor(ChartChecker.getCl1().get(i).moneycolor);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(ChartChecker.getCl1().get(i).totalcolor);

                    ld1.setData(ChartChecker.getCl1().get(i).NDM);
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData(ChartChecker.getCl1().get(i).NDT);

                    //boxes[0] = ld1;
                    //boxes[1] = ld2;
                } else if ("justspread".equals(item)) {
                    ld1.setIconPath(ChartChecker.getCl1().get(i).spreadicon);
                    ld1.setBackgroundColor(ChartChecker.getCl1().get(i).spreadcolor);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(Color.WHITE);

                    ld1.setData(ChartChecker.getCl1().get(i).NDS);
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData("");

                    //boxes[0] = ld1;
                    //boxes[1] = ld2;
                } else if ("justtotal".equals(item)) {
                    ld4.setIconPath(ChartChecker.getCl1().get(i).totalicon);
                    ld1.setBackgroundColor(Color.WHITE);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(ChartChecker.getCl1().get(i).totalcolor);

                    ld1.setData("");
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData(ChartChecker.getCl1().get(i).NDT);

                    //boxes[0] = ld1;
                    //boxes[1] = ld2;
                } else if ("justmoney".equals(item)) {
                    ld1.setIconPath(ChartChecker.getCl1().get(i).moneyicon);
                    ld1.setBackgroundColor(ChartChecker.getCl1().get(i).moneycolor);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(Color.WHITE);

                    ld1.setData(ChartChecker.getCl1().get(i).NDM);
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData("");

                    //	boxes[0] = ld1;
                    //	boxes[1] = ld2;
                } else if ("awayteamtotal".equals(item)) {
                    ld4.setIconPath(ChartChecker.getCl1().get(i).awayicon);
                    ld1.setBackgroundColor(Color.WHITE);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(ChartChecker.getCl1().get(i).awaycolor);

                    ld1.setData("");
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData(ChartChecker.getCl1().get(i).NDA);

                } else if ("hometeamtotal".equals(item)) {
                    ld4.setIconPath(ChartChecker.getCl1().get(i).homeicon);
                    ld1.setBackgroundColor(Color.WHITE);
                    ld2.setBackgroundColor(Color.WHITE);
                    ld3.setBackgroundColor(Color.WHITE);
                    ld4.setBackgroundColor(ChartChecker.getCl1().get(i).homecolor);

                    ld1.setData("");
                    ld2.setData("");
                    ld3.setData(ChartChecker.getCl().get(i).drawMamt + "");
                    ld4.setData(ChartChecker.getCl1().get(i).NDH);

                }


            }
        }
        boxes[0] = ld1;
        boxes[1] = ld2;
        boxes[2] = ld3;
        boxes[3] = ld4;

        //	ld1.setBackgroundColor(topcolor);
        //ld2.setBackgroundColor(bottomcolor);

        //return boxes;
    }

    /*
    public static void setItem(String itm){
        item=itm;
    }
    public static void setPeriod(int per){
        period=per;
    }
    */
    public static void clearColors() {
        for (int i = 0; i < ChartChecker.getCl1().size(); i++) {

            ChartChecker.getCl1().get(i).spreadcolor = Color.WHITE;


            ChartChecker.getCl1().get(i).totalcolor = Color.WHITE;


            ChartChecker.getCl1().get(i).moneycolor = Color.WHITE;


            ChartChecker.getCl1().get(i).awaycolor = Color.WHITE;


            ChartChecker.getCl1().get(i).homecolor = Color.WHITE;
        }
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);

    }

    public String getDisplayType() {
        return item;
    }

    public void setDisplayType(String d) {
        item = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
    }

    public int getPeriodType() {
        return period;
    }

    public void setPeriodType(int d) {
        period = d;
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);
        boxes[2].setBackgroundColor(Color.WHITE);
        boxes[3].setBackgroundColor(Color.WHITE);
    }

    public LineData gettopbox() {
        return topbox;
    }

    public LineData getbottombox() {
        return bottombox;
    }
}