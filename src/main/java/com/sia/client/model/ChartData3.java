package com.sia.client.model;

import com.sia.client.ui.ChartView;

import javax.swing.ImageIcon;
import java.awt.Color;

public class ChartData3 {
    public Color spreadcolor = Color.WHITE;
    public Color totalcolor = Color.WHITE;
    public Color moneycolor = Color.WHITE;
    public Color awaycolor = Color.WHITE;
    public Color homecolor = Color.WHITE;
    public ImageIcon spreadicon = ChartView.ICON_BLANK;
    public ImageIcon totalicon = ChartView.ICON_BLANK;
    public ImageIcon moneyicon = ChartView.ICON_BLANK;
    public ImageIcon awayicon = ChartView.ICON_BLANK;
    public ImageIcon homeicon = ChartView.ICON_BLANK;
    public int gn;
    public int p;
    public int DS;
    public int DT;
    public int DM;
    public int DA;
    public int DH;
    public String NDS;
    public String NDT;
    public String NDM;
    public String NDA;
    public String NDH;
    public boolean isGreat1;
    public boolean isGreat2;
    public boolean isGreat3;
    public boolean isGreat4;
    public boolean isGreat5;
    public boolean dataexists;

    public String toString() {
        return gn + ","
                + p + ","
                + DS + ","
                + DM + ","
                + NDS + ","
                + NDT + ","
                + NDM + ","
                + isGreat1 + ","
                + isGreat2 + ","
                + isGreat3;


    }

}
