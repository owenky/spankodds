package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Sport;

import java.awt.Color;

public class InfoView2 {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    private static final Color green = new Color(0, 102, 0);
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    private final int gid;
    private final Sport s;

    public InfoView2(int gid) {


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
        if ((g.getStatus2() == null || g.getStatus2().equalsIgnoreCase("NULL") || g.getStatus2().equals("")) && (g.getTimeremaining2() == null || g.getTimeremaining2().equalsIgnoreCase("") || g.getTimeremaining2().equalsIgnoreCase("NULL"))) {
            ld1.setData(s.getLeagueabbr());

            ld2.setData("I W L");
            ld2.setTooltip("Injury/Weather/Lineups Coming soon");
            ld1.setBackgroundColor(topcolor);
            ld2.setBackgroundColor(bottomcolor);
        } else if (g.getStatus2().equalsIgnoreCase("Cncld")) {
            ld1.setData("CAN-");
            ld2.setData("CELLED");
            ld1.setBackgroundColor(Color.RED);
            ld2.setBackgroundColor(Color.RED);

        } else if (g.getStatus2().equalsIgnoreCase("Poned")) {
            ld1.setData("POST");
            ld2.setData(g.getStatus().toUpperCase());
            ld1.setBackgroundColor(Color.RED);
            ld2.setBackgroundColor(Color.RED);

        } else {
            ld1.setTooltip(g.getVisitorscoresupplemental2()+g.getHomescoresupplemental2());
            ld2.setTooltip(g.getVisitorscoresupplemental2()+g.getHomescoresupplemental2());
            if (g.getStatus2().equalsIgnoreCase("Win")) {
                ld1.setData(" ");
                ld2.setData(" " + g.getStatus2().toUpperCase());
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);
            } else if (g.getTimeremaining2() != null && g.getTimeremaining2().equalsIgnoreCase("Win")) {
                ld1.setData(" " + g.getTimeremaining2().toUpperCase());
                ld2.setData(" ");
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);

            } else if (g.getTimeremaining2() != null && g.getTimeremaining2().equalsIgnoreCase("Tie")) {
                ld1.setData(" " + g.getTimeremaining2().toUpperCase());
                ld2.setData(" " + g.getTimeremaining2().toUpperCase());
                ld1.setBackgroundColor(Color.RED);
                ld2.setBackgroundColor(Color.RED);

            }
            else if (g.getStatus2().equalsIgnoreCase(SiaConst.FinalStr)) {
                if(g.getLeague_id() == 12)
                {
                    ld1.setTooltip("");
                    ld2.setTooltip("");
                    String[] vscores = g.getVisitorscoresupplemental2().split(",");
                    String[] hscores = g.getHomescoresupplemental2().split(",");
                    int vwins = 0;
                    int hwins = 0;
                    String vwin = "";
                    String hwin = "";
                    for(int i = 0; i < vscores.length; i++)
                    {
                        try {
                            if (Integer.parseInt(vscores[i]) > Integer.parseInt(hscores[i])) {
                                vwins++;
                            } else {
                                hwins++;
                            }
                        }
                        catch(Exception ex) { System.out.println("not int"+ex);}
                    }
                    if(vwins >  hwins)
                    {
                        vwin = "WIN";
                        hwin = "   ";
                    }
                    else if(hwins > vwins)
                    {
                        vwin = "   ";
                        hwin = "WIN";
                    }
                    else
                    {
                        vwin = hwin = "TIE";
                    }
                    ld1.setData(" " + (g.getVisitorscoresupplemental2().replaceAll(",","-"))+" "+vwin);
                    ld2.setData(" " + (g.getHomescoresupplemental2().replaceAll(",","-"))+" "+hwin);
                    ld1.setBackgroundColor(Color.RED);
                    ld2.setBackgroundColor(Color.RED);

                }
                else {


                    ld1.setData(" " + g.getCurrentvisitorscore2() + " " + makenullblank(g.getTimeremaining2()));
                    ld2.setData(" " + g.getCurrenthomescore2() + " " + g.getStatus2().toUpperCase());
                    ld1.setBackgroundColor(Color.RED);
                    ld2.setBackgroundColor(Color.RED);
                }
            }  else {
                //if (g.getLeague_id() == 928 || g.getLeague_id() == 929 || g.getLeague_id() == 930)
                if (g.getStatus2().equalsIgnoreCase("Time")) {

                    ld1.setData(" " + g.getCurrentvisitorscore2() + " " + displayHalftimeCountdown());
                    ld2.setData(" " + g.getCurrenthomescore2() + " H/T");
                    ld1.setBackgroundColor(green);
                    ld2.setBackgroundColor(green);


                } else {
                    if(g.getLeague_id() == 12)
                    {
                        ld1.setTooltip("");
                        ld2.setTooltip("");
                        ld1.setData(" " + (g.getVisitorscoresupplemental2().replaceAll(",","-"))+ " " + g.getTimeremaining2());
                        ld2.setData(" " + (g.getHomescoresupplemental2().replaceAll(",","-"))+ " " + g.getStatus2());
                        ld1.setBackgroundColor(green);
                        ld2.setBackgroundColor(green);

                    }
                    else {


                        ld1.setData(" " + g.getCurrentvisitorscore2() + " " + g.getTimeremaining2());
                        ld2.setData(" " + g.getCurrenthomescore2() + " " + g.getStatus2());
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
        long halftimestart = g.getScorets2();
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
