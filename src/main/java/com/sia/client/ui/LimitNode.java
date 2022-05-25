package com.sia.client.ui;

import javax.swing.SwingConstants;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
public class LimitNode{
    String delimiter = "|";
    String SportName;
    boolean isSpreadCheck=false;
    boolean isTotalCheck=false;
    boolean isMoneyCheck=false;
    boolean isTeamTotalCheck=false;
    boolean isAllLinesCheck=false;
    boolean isFullGameCheck=false;
    boolean is1stHafCheck=false;
    boolean is2ndHalfCheck=false;
    boolean isAllHalfsCheck=false;
    boolean is1stQutCheck=false;
    boolean is2ndQutCheck=false;
    boolean is3rdQutCheck=false;
    boolean is4thQutCheck=false;
    boolean isAllQutCheck=false;
    boolean isAllPerCheck=false;

    boolean isUpperRight=false;
    boolean isUpperLeft=false;
    boolean isLowerRight=false;
    boolean isLowerLeft=true;

    boolean isAudioChecks=false;
    boolean isShowpopChecks=false;


    boolean isIncAudioChecks=false;
    boolean isDecAudioChecks=false;
    boolean isIncShowpopChecks=false;
    boolean isDecShowpopChecks=false;



    boolean isTextChecks=false;


    Instant start=Instant.now();

    int showpopvalue = 5;
    int popuplocationint =8;

    String audiovalue="Major Line Move";

    double renotifyvalue=0.5;
    List<Integer> leagues=new ArrayList<>();
    ArrayList bookies=new ArrayList();
    ArrayList periods=new ArrayList();
    int sports_id;
    int popupsec=10;

    Vector checkedLeagueNodes=new Vector();
    Vector checkedBookieNodes=new Vector();


    List<String> sportcodes = new ArrayList<>();
    List<String> bookiecodes = new ArrayList<>();

    boolean isAllLeaguesSelected=false;
    boolean isAllBookiesSelected=false;

    String soundlabel="DEFAULT";
    String soundfile="limitchange.wav";
    String incsoundfile="limitchange.wav";
    String decsoundfile="limitchange.wav";

    int minpercentagechange = 0;

    LimitNode(String SportName)
    {
        this.SportName=SportName;
    }


    public String getSport()
    {
        return SportName;
    }
    public void setSport(String SportName)
    {
        this.SportName = SportName;
    }
    public String toString()
    {
        String str = SportName+delimiter
                //+checkedLeagueNodes.toString()+delimiter
                //+checkedBookieNodes.toString()+delimiter
                +((sportcodes.toString()).replace("[","")).replace("]","")+delimiter
                +((bookiecodes.toString()).replace("[","")).replace("]","")+delimiter
                +isFullGameCheck+delimiter
                +is1stHafCheck+delimiter
                +is2ndHalfCheck+delimiter
                +is1stQutCheck+delimiter
                +is2ndQutCheck+delimiter
                +is3rdQutCheck+delimiter
                +is4thQutCheck+delimiter
                +isSpreadCheck+delimiter
                +isTotalCheck+delimiter
                +isMoneyCheck+delimiter
                +isTeamTotalCheck+delimiter
                +isIncAudioChecks+delimiter
                +isDecAudioChecks+delimiter
                +isIncShowpopChecks+delimiter
                +isDecShowpopChecks+delimiter
                +popuplocationint+delimiter
                +popupsec+delimiter
                +renotifyvalue+delimiter
                +incsoundfile+delimiter
                +decsoundfile+delimiter
                +minpercentagechange+delimiter;


        return str;
    }

    public LimitNode(String SportName,
                               ArrayList leagues,
                               ArrayList bookies,
                               boolean isFullGameCheck,
                               boolean is1stHafCheck,
                               boolean is2ndHalfCheck,
                               boolean is1stQutCheck,
                               boolean is2ndQutCheck,
                               boolean is3rdQutCheck,
                               boolean is4thQutCheck,
                               boolean isSpreadCheck,
                               boolean isTotalCheck,
                               boolean isMoneyCheck,
                               boolean isTeamTotalCheck,
                               boolean isIncAudioChecks,
                               boolean isDecAudioChecks,
                               boolean isIncShowpopChecks,
                               boolean isDecShowpopChecks,
                               int popuplocationint,
                               int popupsec,
                               double renotifyvalue,
                               String incsoundfile,String decsoundfile,int minpercentagechange)
    {
        this.SportName = SportName;
        this.sportcodes = leagues;
        this.bookiecodes = bookies;
        this.isFullGameCheck = isFullGameCheck;
        this.is1stHafCheck = is1stHafCheck;
        this.is2ndHalfCheck = is2ndHalfCheck;
        this.is1stQutCheck = is1stQutCheck;
        this.is2ndQutCheck = is2ndQutCheck;
        this.is3rdQutCheck = is3rdQutCheck;
        this.is4thQutCheck = is4thQutCheck;
        this.isSpreadCheck = isSpreadCheck;
        this.isTotalCheck = isTotalCheck;
        this.isMoneyCheck = isMoneyCheck;
        this.isTeamTotalCheck = isTeamTotalCheck;
        this.isIncAudioChecks = isIncAudioChecks;
        this.isDecAudioChecks = isDecAudioChecks;
        this.isIncShowpopChecks = isIncShowpopChecks;
        this.isDecShowpopChecks = isDecShowpopChecks;
        this.popuplocationint = popuplocationint;
        this.popupsec = popupsec;
        this.renotifyvalue = renotifyvalue;
        this.incsoundfile = incsoundfile;
        this.decsoundfile = decsoundfile;
        this.minpercentagechange = minpercentagechange;

    }


}