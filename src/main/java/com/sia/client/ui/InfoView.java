package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Sport;

import java.awt.Color;

public class InfoView {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    private static final Color green = new Color(0, 102, 0);
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    private final int gid;
    private final Sport s;

    public InfoView(int gid) {


        this.gid = gid;
        Game g = AppController.getGame(gid);
        if(g.getLeague_id() == 9) {
            s = AppController.getSportByLeagueId(g.getSubleague_id());
        }
        else
        {
            s = AppController.getSportByLeagueId(g.getLeague_id());
        }
    }

    public void clearColors() {
        boxes[0].setBackgroundColor(Color.WHITE);
        boxes[1].setBackgroundColor(Color.WHITE);

    }
    public LineData[] getCurrentBoxes() {
        setCurrentBoxes();
        return boxes;
    }

    private void setCurrentBoxes() {
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        String topicon = ICON_BLANK;
        String bottomicon = ICON_BLANK;
        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);

        Game g = AppController.getGame(gid);
        if ((g.getStatus() == null || g.getStatus().equalsIgnoreCase("NULL") || g.getStatus().equals("")) && (g.getTimeremaining() == null || g.getTimeremaining().equalsIgnoreCase("") || g.getTimeremaining().equalsIgnoreCase("NULL"))) {
                ld1.setData(s.getLeagueabbr());

            ld2.setData("I W L");
            ld2.setTooltip("Injury/Weather/Lineups Coming soon");
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
            ld1.setTooltip(g.getVisitorscoresupplemental()+g.getHomescoresupplemental());
            ld2.setTooltip(g.getVisitorscoresupplemental()+g.getHomescoresupplemental());
            if (g.getStatus().equalsIgnoreCase("Win")) {
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

            }
           else if (g.getStatus().equalsIgnoreCase(SiaConst.FinalStr)) {
                if(g.getLeague_id() == 12)
                {
                    ld1.setTooltip("");
                    ld2.setTooltip("");
                    ld1.setData(" " + (g.getVisitorscoresupplemental().replaceAll(",","-"))+ " " + g.getTimeremaining());
                    ld2.setData(" " + (g.getHomescoresupplemental().replaceAll(",","-"))+ " F");
                    ld1.setBackgroundColor(Color.RED);
                    ld2.setBackgroundColor(Color.RED);

                }
               else {


                    ld1.setData(" " + g.getCurrentvisitorscore() + " " + makenullblank(g.getTimeremaining()));
                    ld2.setData(" " + g.getCurrenthomescore() + " " + g.getStatus().toUpperCase());
                    ld1.setBackgroundColor(Color.RED);
                    ld2.setBackgroundColor(Color.RED);
                }
            }  else {
                //if (g.getLeague_id() == 928 || g.getLeague_id() == 929 || g.getLeague_id() == 930)
                 if (g.getStatus().equalsIgnoreCase("Time")) {

                    ld1.setData(" " + g.getCurrentvisitorscore() + " " + displayHalftimeCountdown());
                    ld2.setData(" " + g.getCurrenthomescore() + " H/T");
                    ld1.setBackgroundColor(green);
                    ld2.setBackgroundColor(green);


                } else {
                     if(g.getLeague_id() == 12)
                     {
                         ld1.setTooltip("");
                         ld2.setTooltip("");
                         ld1.setData(" " + (g.getVisitorscoresupplemental().replaceAll(",","-"))+ " " + g.getTimeremaining());
                         ld2.setData(" " + (g.getHomescoresupplemental().replaceAll(",","-"))+ " " + g.getStatus());
                         ld1.setBackgroundColor(green);
                         ld2.setBackgroundColor(green);

                     }
                     else {


                         ld1.setData(" " + g.getCurrentvisitorscore() + " " + g.getTimeremaining());
                         ld2.setData(" " + g.getCurrenthomescore() + " " + g.getStatus());
                         ld1.setBackgroundColor(green);
                         ld2.setBackgroundColor(green);
                     }
                }


            }
        }
        boxes[0] = ld1;
        boxes[1] = ld2;
    }

    public String makenullblank(String s) {
        if (s == null || s.equalsIgnoreCase("null")) {
            return "";
        } else {
            return s;
        }
    }

    public String displayHalftimeCountdown() {
        Game g = AppController.getGame(gid);
        String retstring = "";
        java.text.SimpleDateFormat timerFormat = new java.text.SimpleDateFormat("m:ss");
        long halftimestart = g.getScorets();
        long halftimelength = (long) (s.getHalftimeminutes() * 60 * 1000);
        long startTime = halftimestart + halftimelength;
        long now = System.currentTimeMillis();
        long diff = startTime - now;
        if (diff <= 0) {
            retstring = "x";
            diff = now + 1000 - startTime;
        }
        retstring = retstring + timerFormat.format(new java.util.Date(diff));
        return retstring;
    }

}
