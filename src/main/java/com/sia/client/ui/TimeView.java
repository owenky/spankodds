package com.sia.client.ui;

import com.sia.client.config.SiaConst.ImageFile;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class TimeView {

    public static String ICON_UP = ImageFile.ARR_UP;
    public static String ICON_DOWN = ImageFile.ARR_DOWN;
    public static String ICON_BLANK = null;//new ImageIcon("blank.gif");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd");
    LineData topbox;
    LineData bottombox;
    LineData[] boxes = new LineData[2];
    LineData ld1 = new LineData(ICON_BLANK, "", Color.WHITE);
    LineData ld2 = new LineData(ICON_BLANK, "", Color.WHITE);
    int gid;
    Game g;
//    SimpleDateFormat sdf = new SimpleDateFormat("h:mma");
//    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");

    String timeformat;
    String dateformat;

    public TimeView(int gid) {


        this.gid = gid;
        g = AppController.getGame(gid);
        dateformat = dateFormatter.format(g.getGamedate());

        timeformat = timeFormatter.format(g.getGametime());
        timeformat = timeformat.replace("PM", "p");
        timeformat = timeformat.replace("AM", "a");

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

        // updated 8/27 to reflect time chnages live!
        dateformat = dateFormatter.format(g.getGamedate());
        timeformat = timeFormatter.format(g.getGametime());
        timeformat = timeformat.replace("PM", "p");
        timeformat = timeformat.replace("AM", "a");

        Color topcolor = Color.WHITE;
        Color bottomcolor = Color.WHITE;
        String topicon = ICON_BLANK;
        String bottomicon = ICON_BLANK;

        ld1.setIconPath(topicon);
        ld2.setIconPath(bottomicon);

        ld1.setData(dateformat);
        ld2.setData(timeformat);

        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        boxes[0] = ld1;
        boxes[1] = ld2;
    }


}
