package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.LineData;
import com.sia.client.model.Spreadline;

import javax.swing.ImageIcon;
import java.awt.Color;

public class HeaderView {

    public static ImageIcon ICON_UP = new ImageIcon(Utils.getMediaResource("arrup.gif"));
    public static ImageIcon ICON_DOWN = new ImageIcon(Utils.getMediaResource("arrdown.gif"));
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


    public HeaderView() {
        this.gid = gid;
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

        ld1.setIcon(topicon);
        ld2.setIcon(bottomicon);
        ld1.setData("THIS IS A VERY BIG HEADER THAT IS SUPPSED TO SHOW THE DATE AND SPORT TYPE OF STUFF BELOW!!!!!!!!!!!!!!!!!!!");
        ld2.setData("");
        ld1.setBackgroundColor(topcolor);
        ld2.setBackgroundColor(bottomcolor);
        boxes[0] = ld1;
        boxes[1] = ld2;
        //return boxes;
    }


}
