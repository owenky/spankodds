package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Spreadline;
import com.sia.client.model.ViewValue;

import java.awt.Color;

public class TeamView extends ViewValue {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = ImageFile.ICON_BLANK;
    Spreadline sl;
    Totalline tl;
    LineData topbox;
    LineData bottombox;
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    int gid;
    Game g;
    boolean shortteam;

    public TeamView(int gid, boolean shortteam) {


        this.gid = gid;
        this.shortteam = shortteam;
        g = AppController.getGame(gid);
        setTooltiptext(g.getDescription());
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
        //tooltiptext = g.getDescription();
        String topboxS = "";
        String bottomboxS = "";
        Color spreadcolor = Color.WHITE;
        Color totalcolor = Color.WHITE;
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        String topicon = ICON_BLANK;
        String bottomicon = ICON_BLANK;

        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);
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
