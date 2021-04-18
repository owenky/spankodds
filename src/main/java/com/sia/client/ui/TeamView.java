package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;

import javax.swing.ImageIcon;
import java.awt.Color;

public class TeamView {

    public static ImageIcon ICON_UP = new ImageIcon(Utils.getMediaResource("ArrUp.gif"));
    public static ImageIcon ICON_DOWN = new ImageIcon(Utils.getMediaResource("ArrDown.gif"));
    public static ImageIcon ICON_BLANK = new ImageIcon(Utils.getMediaResource("blank.gif"));
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
    String tooltiptext = "";
    boolean shortteam;

    public TeamView(int gid, boolean shortteam) {


        this.gid = gid;
        this.shortteam = shortteam;
        g = AppController.getGame(gid);
        //this.setAndGetPriorBoxes(bid,gid);
        //this.setAndGetOpenerBoxes(bid,gid);
        tooltiptext = g.getDescription();

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

    public String getToolTip() {
        return tooltiptext;
    }


    public LineData[] getCurrentBoxes() {
        setCurrentBoxes();
        return boxes;
    }

    public void setCurrentBoxes() {
        //tooltiptext = g.getDescription();
        String topboxS = "";
        String bottomboxS = "";
        Color spreadcolor = Color.WHITE;
        Color totalcolor = Color.WHITE;
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        ImageIcon topicon = ICON_BLANK;
        ImageIcon bottomicon = ICON_BLANK;

        ld1.setIcon(topicon);
        ld2.setIcon(bottomicon);
        //ld1.setData(g.getVisitorteam());
        //ld2.setData(g.getHometeam());

        if (shortteam) {
            ld1.setData(g.getShortvisitorteam());
            ld2.setData(g.getShorthometeam());
        } else {
            ld1.setData(g.getVisitorteam());
            ld2.setData(g.getHometeam());

        }
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        boxes[0] = ld1;
        boxes[1] = ld2;
        //return boxes;
    }


}
