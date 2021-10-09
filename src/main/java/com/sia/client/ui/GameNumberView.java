package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;

import java.awt.Color;

public class GameNumberView {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    LineData topbox;
    LineData bottombox;
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    int bid;
    int gid;
    Game g;


    public GameNumberView(int gid) {


        this.gid = gid;
        g = AppController.getGame(gid);
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
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        String topicon = ICON_BLANK;
        String bottomicon = ICON_BLANK;

        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);
        ld1.setData("" + g.getVisitorgamenumber());
        ld2.setData("" + g.getHomegamenumber());
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        boxes[0] = ld1;
        boxes[1] = ld2;
        //return boxes;
    }


}
