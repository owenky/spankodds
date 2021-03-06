package com.sia.client.config;

import java.awt.*;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SiaConst {

    public static final String Version="(v21.20.10.52)";
    public static final int StageGroupAnchorOffset = 10000;
    public static final ReentrantLock GameLock = new ReentrantLock();
    public static final String ImgPath = "/media/";
    public static final String ConfigPath = "/config/";
    public static final String FinalStr = "Final";
    public static final String HalfTimeStr = "Halftime";
    public static final String InProgresStr = "In Progress";
    public static final String InGamePricesStr = "In Game Prices";
    public static final String SeriesPricesStr = "Series Prices";
    public static final int SoccerLeagueId = 9;
    public static final int NoteColumnBookieId = 995;
    public static final Integer BlankGameId = -10000000; //Integer.MIN_VALUE does not work in sorting
    public static final Integer GameGroupHeaderHeight = 20;
    public static final Color DefaultHeaderColor = new Color(0,0,128); //Color.BLUE;
    public static final Color DefaultHeaderFontColor = Color.WHITE;
    public static final double DefaultSpread = -99999d;
    public static final double DefaultOver = 99999d;
    public static final int ColumnWidthRefreshRate = 2500;
    public static final int DefaultColumnWidth = 50;
    public static final int DataRefreshRate = 500;
    public static final String DefaultGameTimeZone = "US/Eastern";
    public static final long diffBetweenEasternAndUTC = 5*3600*1000L;

    public interface Ui {
        Color COLOR_WINDOW_BCK = new java.awt.Color(255,255,255);
        Color COLOR_UNDERLINE = new java.awt.Color(121, 124, 128);
        Color COLOR_TEXT_SELECTION_BCK = new java.awt.Color(49, 106, 197);
        Color COLOR_TEXT_SELECTION_FORE = new java.awt.Color(255, 255, 255);
        Color ROW_SELECTED_COLOR = new java.awt.Color(100, 100, 255);
        Cursor CURSOR_DEFAULT = new Cursor(Cursor.DEFAULT_CURSOR);
        Cursor CURSOR_BUSY = new Cursor(Cursor.WAIT_CURSOR);
        Cursor CURSOR_HAND = new Cursor(Cursor.HAND_CURSOR);

        int HiddenRowHeight = 1;
    }
    public interface SportName {
        String Football = "Football";
        String Basketball = "Basketball";
        String Baseball = "Baseball";
        String Hockey = "Hockey";
        String Fighting = "Fighting";
        String Soccer = "Soccer";
        String Auto_Racing = "Auto Racing";
        String Golf = "Golf";
        String Tennis = "Tennis";
        String Today = "Today";
    }
    public interface LayedPaneIndex {
        int TableColumnMenuIndex = 10;
        int SportConfigIndex = TableColumnMenuIndex; //so that Sports Config Pane will hide Table Column Menu Pane, and vice visa. 2022-01-09
        int LineSeekerAlertMethodDialogIndex = TableColumnMenuIndex;
        int SportDetailPaneIndex = TableColumnMenuIndex + 10;
        int NoteColumnEditorIndex = SportDetailPaneIndex + 10;
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
    public static final String EditedIndicator = "***";
    public static final String MessageDelimiter = "~";
    public static final String ColumnDelimiter = ",";
    public static final String PropertyDelimiter = "@#_#_#@";
    public static final String logFileName = "c:\\temp\\spankyOddsLog.txt";
    public static final String errFileName = "c:\\temp\\spankyOddsErr.txt";
    public interface UIProperties {
        int screenXmargin = 50;
        int screenYmargin = 50;
        Dimension LineAlertDim = new Dimension(1200,800);
        Dimension LineAlertMethodDim = new Dimension(900,720);
        Dimension CustomTab2Dim = new Dimension(1000,650);
        Dimension DisplaySettingsDim = new Dimension(820,550);
    }
    public interface Serialization {
        String config = "config";
    }
    public static final String GameNumColIden = "Gm#";
    public static final String DefaultViewName = "default";

    public static final String CONSENSUSHELPURL = "www.spankodds.com";
    public static final String MANAGEBOOKIEHELPURL = "www.spankodds.com";
    public static final String CHARTHELPURL = "www.spankodds.com";
    public static final String LINEMOVESHELPURL = "www.spankodds.com";
    public static final String LINESEEKERHELPURL = "www.spankodds.com";
    public static final String OPENERHELPURL = "www.spankodds.com";
    public static final String LIMITCHANGEHELPURL = "www.spankodds.com";
    public static final String GENERALHELPURL = "www.spankodds.com";
    public static final String CUSTOMTABHELPURL = "www.spankodds.com";


    public static final String GAMEALERTHELPURL = "www.spankodds.com";
    public static final String STARTEDHELPURL = GAMEALERTHELPURL;
    public static final String FINALHELPURL = GAMEALERTHELPURL;
    public static final String HALFTIMEHELPURL = GAMEALERTHELPURL;
    public static final String LINEUPSHELPURL = GAMEALERTHELPURL;
    public static final String OFFICIALSHELPURL = GAMEALERTHELPURL;
    public static final String INJURIESHELPURL = GAMEALERTHELPURL;
    public static final String TIMECHANGESHELPURL = GAMEALERTHELPURL;

    public static final String up = "general:fruitl00ps";

}
