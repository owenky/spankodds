package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;

import java.awt.Color;

public class SoccerGameNumberView {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    LineData topbox;
    LineData bottombox;
    LineData drawbox;
    LineData totalbox;
    LineData[] boxes = new LineData[4];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld3 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld4 = new LineData(ICON_BLANK, "", Color.WHITE);
    int gid;
    Game g;
    public SoccerGameNumberView(int gid) {
        this.gid = gid;
        g = AppController.getGame(gid);
    }

    public void clearColors() {
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

    public LineData getdrawbox() {
        return drawbox;
    }

    public LineData gettotalbox() {
        return totalbox;
    }


    public LineData[] getCurrentBoxes() {
        setCurrentBoxes();
        return boxes;
    }

    public void setCurrentBoxes() {
        Color totalcolor = Color.WHITE;
        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        String topicon = ICON_BLANK;
        String bottomicon = ICON_BLANK;

        Color drawcolor = Color.WHITE;
        String drawicon = ICON_BLANK;
        String totalicon = ICON_BLANK;

        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);

        ld3.setIconPath(drawicon);
        ld4.setIconPath(totalicon);

        ld1.setData("" + g.getVisitorgamenumber());
        ld2.setData("" + g.getHomegamenumber());
        ld3.setData("" + (g.getHomegamenumber() + 1));
        ld4.setData("" + (g.getHomegamenumber() + 2));
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        ld3.setBackgroundColor(drawcolor);
        ld4.setBackgroundColor(totalcolor);
        boxes[0] = ld1;
        boxes[1] = ld2;
        boxes[2] = ld3;
        boxes[3] = ld4;
    }


}
