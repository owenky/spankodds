import java.util.*;
import java.time.Duration;
import java.time.Instant;
import javax.swing.*;
class LineOpenerAlertNode{
	
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
	
	
	Instant start=Instant.now();
	
	int showpopvalue;
	int popuplocationint=SwingConstants.SOUTH_EAST;
	
	String audiovalue="Major Line Move";
	
	double renotifyvalue=0.5;
	ArrayList leagues=new ArrayList();
	ArrayList bookies=new ArrayList();
	ArrayList periods=new ArrayList();
	int sports_id;
	int popupsec=1;
	
	Vector checkedLeagueNodes=new Vector();
	Vector checkedBookieNodes=new Vector();
	
	boolean isAllLeaguesSelected=false;
	boolean isAllBookiesSelected=false;
	
	String soundlabel="DEFAULT";
	String soundfile="openers.wav";
	
	LineOpenerAlertNode(String SportName)
	{
		this.SportName=SportName;
	}
	
	

}