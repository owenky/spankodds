package com.sia.client.config;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SiaConst {

    public static final String Version="(v21.20.8.20)";
    public static final int StageGroupAnchorOffset = 10000;
    public static final ReentrantLock GameLock = new ReentrantLock();
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
    public static final int InGameFlagId = 9000;
    public static final Integer BlankGameId = -10000000; //Integer.MIN_VALUE does not work in sorting
    public static final Integer SoccerRowheight = 60;
    public static final Integer NormalRowheight = 30;
    public static final Integer GameGroupHeaderHeight = 20;
//    public static final String GameGroupHeaderIden = "@~@";
    public static final Color DefaultHeaderColor = new Color(0,0,128); //Color.BLUE;
    public static final Color DefaultHeaderFontColor = Color.WHITE;
    public static final Font DefaultHeaderFont = new Font("Verdana", Font.BOLD, 11);
    public static final double DefaultSpread = -99999d;
    public static final double DefaultOver = 99999d;
    public static final int ColumnWidthRefreshRate = 2500;
    public static final int DataRefreshRate = 500;
    public static final String DefaultGameTimeZone = "US/Eastern";
    public static final long diffBetweenEasternAndUTC = 5*3600*1000L;

    public interface LayedPaneIndex {
        int TableColumnMenuIndex = 10;
    }
    public interface ImageFile {
        String ICON_BLANK = "blank.gif";
        String ICON_UP = "moveup.png";
        String ICON_DOWN = "movedown.png";
        String ICON_BLANK2 = "blank2.gif";
        String ARR_UP = "arrup.gif";
        String ARR_DOWN = "arrdown.gif";
    }
    public interface TestProperties {
        String DefaultMesgDir = "c:\\temp\\OngingGameMessages";
        String InitialLoadingFileName = "initGameMesgs.txt";
    }
    public static final String MessageDelimiter = "~";
    public static final String PropertyDelimiter = "@#_#_#@";
    public static final String logFileName = "c:\\temp\\spankyOddsLog.txt";
    public static final String errFileName = "c:\\temp\\spankyOddsErr.txt";
}
