package com.sia.client.config;

import java.awt.Color;
import java.awt.Font;

public abstract class SiaConst {

    public static final String ImgPath = "/media/";
    public static final String ConfigPath = "/config/";
    public static final String SoccerStr = "Soccer";
    public static final String FinalStr = "Final";
    public static final String HalfTimeStr = "Halftime";
    public static final String InProgresStr = "In Progress";
    public static final String InGamePricesStr = "In Game Prices";
    public static final String SeriesPricesStr = "Series Prices";
    public static final String SoccerInGamePricesStr = SoccerStr+InGamePricesStr;
    public static final String SoccerSeriesPricesStr = SoccerStr+SeriesPricesStr;
    public static final String SoccerHalfTimeStr = SoccerStr+" "+HalfTimeStr;
    public static final String SoccerInProgressStr = SoccerStr+ " "+InProgresStr;
    public static final String SoccerInFinalStr = SoccerStr+ " "+FinalStr;
    public static final int SoccerLeagueId = 9;
    public static final Integer BlankGameId = Integer.MIN_VALUE;
    public static final Integer SoccerRowheight = 60;
    public static final Integer NormalRowheight = 30;
    public static final Integer GameGroupHeaderHeight = 20;
    public static final String GameGroupHeaderIden = "@#GameGroupHeaderIden-+|";
    public static final Color DefaultHeaderColor = new Color(0,0,128); //Color.BLUE;
    public static final Color DefaultHeaderFontColor = Color.WHITE;
    public static final Font DefaultHeaderFont = new Font("Verdana", Font.BOLD, 11);

    public interface LayedPaneIndex {
        int TableColumnMenuIndex = 10;
    }
}
