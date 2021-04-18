package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Sport;

import javax.swing.ImageIcon;
import java.awt.Color;

public class InfoView {

    public static ImageIcon ICON_UP = new ImageIcon(Utils.getMediaResource("ArrUp.gif"));
    public static ImageIcon ICON_DOWN = new ImageIcon(Utils.getMediaResource("ArrDown.gif"));
    public static ImageIcon ICON_BLANK = null;//new ImageIcon("blank.gif");
    Spreadline sl;
    Totalline tl;
    LineData topbox;
    LineData bottombox;
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    int bid;
    int gid;
    Game g;
    Sport s;

    public InfoView(int gid) {


        this.gid = gid;
        g = AppController.getGame(gid);

        s = AppController.getSport(g.getLeague_id());
        //this.setAndGetPriorBoxes(bid,gid);
        //this.setAndGetOpenerBoxes(bid,gid);


    }

    public void clearColors() {
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);

    }

    public LineData gettopbox() {
        return topbox;
    }

    public LineData getbottombox() {
        return bottombox;
    }


    public LineData[] getCurrentBoxes() {
        setCurrentBoxes();
        return boxes;
    }

    public void setCurrentBoxes() {
        String topboxS = "";
        String bottomboxS = "";
        Color spreadcolor = Color.WHITE;
        Color totalcolor = Color.WHITE;
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        ImageIcon topicon = ICON_BLANK;
        ImageIcon bottomicon = ICON_BLANK;
        Color green = new Color(0, 102, 0);

        //g.gettimerremaining = half... g.getstatus == time means its halftime
        //this is where i need to start my own countdown based on sport...

        ld1.setIcon(topicon);
        ld2.setIcon(bottomicon);

        if ((g.getStatus() == null || g.getStatus().equalsIgnoreCase("NULL") || g.getStatus().equals("")) && (g.getTimeremaining() == null || g.getTimeremaining().equalsIgnoreCase("") || g.getTimeremaining().equalsIgnoreCase("NULL"))) {


            ld1.setData(s.getLeagueabbr());
            ld2.setData("I W L");
            ld1.setBackgroundColor(topcolor);
            ld2.setBackgroundColor(bottomcolor);
        } else if (g.getStatus().equalsIgnoreCase("Cncld")) {
            ld1.setData("CAN-");
            ld2.setData("CELLED");
            ld1.setBackgroundColor(Color.RED);
            ld2.setBackgroundColor(Color.RED);

        } else if (g.getStatus().equalsIgnoreCase("Poned")) {
            ld1.setData("POST");
            ld2.setData(g.getStatus().toUpperCase());
            ld1.setBackgroundColor(Color.RED);
            ld2.setBackgroundColor(Color.RED);

        } else {
            if (g.getStatus().equalsIgnoreCase("Final")) {
                ld1.setData(" " + g.getCurrentvisitorscore() + " " + makenullblank(g.getTimeremaining()));
                ld2.setData(" " + g.getCurrenthomescore() + " " + g.getStatus().toUpperCase());
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);
            } else if (g.getStatus().equalsIgnoreCase("Win")) {
                ld1.setData(" ");
                ld2.setData(" " + g.getStatus().toUpperCase());
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);
            } else if (g.getTimeremaining() != null && g.getTimeremaining().equalsIgnoreCase("Win")) {
                ld1.setData(" " + g.getTimeremaining().toUpperCase());
                ld2.setData(" ");
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);

            } else if (g.getTimeremaining() != null && g.getTimeremaining().equalsIgnoreCase("Tie")) {
                ld1.setData(" " + g.getTimeremaining().toUpperCase());
                ld2.setData(" " + g.getTimeremaining().toUpperCase());
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);

            } else {
                if (g.getLeague_id() == 928 || g.getLeague_id() == 929 || g.getLeague_id() == 930) {
                    ld1.setData(" " + g.getVisitorscoresupplemental());
                    ld2.setData(" " + g.getHomescoresupplemental());
                    ld1.setBackgroundColor(green);
                    ld2.setBackgroundColor(green);

                } else if (g.getStatus().equalsIgnoreCase("Time")) {

                    ld1.setData(" " + g.getCurrentvisitorscore() + " " + displayHalftimeCountdown());
                    ld2.setData(" " + g.getCurrenthomescore() + " H/T");
                    ld1.setBackgroundColor(green);
                    ld2.setBackgroundColor(green);


                } else {
                    ld1.setData(" " + g.getCurrentvisitorscore() + " " + g.getTimeremaining());
                    ld2.setData(" " + g.getCurrenthomescore() + " " + g.getStatus());
                    ld1.setBackgroundColor(green);
                    ld2.setBackgroundColor(green);
                }


            }
        }
        boxes[0] = ld1;
        boxes[1] = ld2;
        //return boxes;
    }

    public String makenullblank(String s) {
        if (s == null || s.equalsIgnoreCase("null")) {
            return "";
        } else {
            return s;
        }
    }

    public String displayHalftimeCountdown() {
        String retstring = "";
        java.text.SimpleDateFormat timerFormat = new java.text.SimpleDateFormat("m:ss");
        long halftimestart = g.getScorets().getTime();
        long halftimelength = (long) (s.getHalftimeminutes() * 60 * 1000);
        long startTime = halftimestart + halftimelength;
        long now = System.currentTimeMillis();
        long diff = startTime - now;
//	System.out.println("ht start = "+g.getScorets());
//	System.out.println("ht length = "+halftimelength);
//	System.out.println("diff = "+diff);

        if (diff <= 0) {
            retstring = "x";
            diff = now + 1000 - startTime;
        }
        retstring = retstring + timerFormat.format(new java.util.Date(diff));
        return retstring;
    }

}
