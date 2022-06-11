package com.sia.client.model;

import java.awt.*;

public  class UserDisplaySettings
{
    static String footballdefault = "spreadtotal";
    static String basketballdefault =  "spreadtotal";
    static String baseballdefault =  "totalbothmoney";
    static String hockeydefault = "totalbothmoney";
    static String fightingdefault = "totalbothmoney";
    static String soccerdefault = "spreadtotal";
    static String autoracingdefault = "justmoney";
    static String golfdefault ="justmoney";
    static String tennisdefault = "justmoney";

    static int firstmoveseconds =30;
    static int secondmoveseconds = 5;

    static Color firstcolor = Color.RED;
    static Color secondcolor = Color.BLACK;
    static Color thirdcolor = Color.BLACK;

    static boolean autofitdata = true;
    static boolean showcpo = true;
    static boolean showborderbestline = true;
    static boolean showdirectionicons = true;

    static boolean showaltcolor = true;
    static Color altcolor = new Color(215, 215, 215);


    static Color openercolor = Color.GRAY;
    static Color lastcolor = Color.BLUE;

    public static boolean isShowaltcolor() {
        return showaltcolor;
    }

    public static void setShowaltcolor(boolean showaltcolor) {
        UserDisplaySettings.showaltcolor = showaltcolor;
    }

    public static Color getAltcolor() {
        return altcolor;
    }

    public static void setAltcolor(Color altcolor) {
        UserDisplaySettings.altcolor = altcolor;
    }

    public static Color getOpenercolor() {
        return openercolor;
    }

    public static void setOpenercolor(Color openercolor) {
        UserDisplaySettings.openercolor = openercolor;
    }

    public static Color getLastcolor() {
        return lastcolor;
    }

    public static void setLastcolor(Color lastcolor) {
        UserDisplaySettings.lastcolor = lastcolor;
    }



    public static boolean isShowdirectionicons() {
        return showdirectionicons;
    }

    public static void setShowdirectionicons(boolean showdirectionicons) {
        UserDisplaySettings.showdirectionicons = showdirectionicons;
    }



    public static boolean isAutofitdata() {
        return autofitdata;
    }

    public static void setAutofitdata(boolean autofitdata) {
        UserDisplaySettings.autofitdata = autofitdata;
    }

    public static boolean isShowcpo() {
        return showcpo;
    }

    public static void setShowcpo(boolean showcpo) {
        UserDisplaySettings.showcpo = showcpo;
    }

    public static boolean isShowborderbestline() {
        return showborderbestline;
    }

    public static void setShowborderbestline(boolean showborderbestline) {
        UserDisplaySettings.showborderbestline = showborderbestline;
    }



    public static String getFootballdefault() {
        return footballdefault;
    }

    public static void setFootballdefault(String footballdefault) {
        UserDisplaySettings.footballdefault = footballdefault;
    }

    public static String getBasketballdefault() {
        return basketballdefault;
    }

    public static void setBasketballdefault(String basketballdefault) {
        UserDisplaySettings.basketballdefault = basketballdefault;
    }

    public static String getBaseballdefault() {
        return baseballdefault;
    }

    public static void setBaseballdefault(String baseballdefault) {
        UserDisplaySettings.baseballdefault = baseballdefault;
    }

    public static String getHockeydefault() {
        return hockeydefault;
    }

    public static void setHockeydefault(String hockeydefault) {
        UserDisplaySettings.hockeydefault = hockeydefault;
    }

    public static String getFightingdefault() {
        return fightingdefault;
    }

    public static void setFightingdefault(String fightingdefault) {
        UserDisplaySettings.fightingdefault = fightingdefault;
    }

    public static String getSoccerdefault() {
        return soccerdefault;
    }

    public static void setSoccerdefault(String soccerdefault) {
        UserDisplaySettings.soccerdefault = soccerdefault;
    }

    public static String getAutoracingdefault() {
        return autoracingdefault;
    }

    public static void setAutoracingdefault(String autoracingdefault) {
        UserDisplaySettings.autoracingdefault = autoracingdefault;
    }

    public static String getGolfdefault() {
        return golfdefault;
    }

    public static void setGolfdefault(String golfdefault) {
        UserDisplaySettings.golfdefault = golfdefault;
    }

    public static String getTennisdefault() {
        return tennisdefault;
    }

    public static void setTennisdefault(String tennisdefault) {
        UserDisplaySettings.tennisdefault = tennisdefault;
    }

    public static int getFirstmoveseconds() {
        return firstmoveseconds;
    }

    public static void setFirstmoveseconds(int firstmoveseconds) {
        UserDisplaySettings.firstmoveseconds = firstmoveseconds;
    }

    public static int getSecondmoveseconds() {
        return secondmoveseconds;
    }

    public static void setSecondmoveseconds(int secondmoveseconds) {
        UserDisplaySettings.secondmoveseconds = secondmoveseconds;
    }

    public static Color getFirstcolor() {
        return firstcolor;
    }

    public static void setFirstcolor(Color firstcolor) {
        UserDisplaySettings.firstcolor = firstcolor;
    }

    public static Color getSecondcolor() {
        return secondcolor;
    }

    public static void setSecondcolor(Color secondcolor) {
        UserDisplaySettings.secondcolor = secondcolor;
    }

    public static Color getThirdcolor() {
        return thirdcolor;
    }

    public static void setThirdcolor(Color thirdcolor) {
        UserDisplaySettings.thirdcolor = thirdcolor;
    }





}
