package com.sia.client.ui;

import javax.swing.SwingConstants;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
public class LineOpenerAlertNode{
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

	boolean isTextChecks=false;
	
	
	Instant start=Instant.now();
	
	int showpopvalue = 5;
	int popuplocationint;
	
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
	String soundfile="openers.wav";
	
	LineOpenerAlertNode(String SportName)
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
				+isAudioChecks+delimiter
				+isShowpopChecks+delimiter
				+popuplocationint+delimiter
				+popupsec+delimiter
				+renotifyvalue+delimiter
				+soundfile+delimiter
				+isTextChecks+delimiter;


		return str;
	}

	public LineOpenerAlertNode(String SportName,
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
	boolean isAudioChecks,
	boolean isShowpopChecks,
	int popuplocationint,
	int popupsec,
	double renotifyvalue,
				String soundfile,boolean isTextChecks)
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
		this.isAudioChecks = isAudioChecks;
		this.isShowpopChecks = isShowpopChecks;
		this.popuplocationint = popuplocationint;
		this.popupsec = popupsec;
		this.renotifyvalue = renotifyvalue;
		this.soundfile = soundfile;
		this.isTextChecks = isTextChecks;

	}


}