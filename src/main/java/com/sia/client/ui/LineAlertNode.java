package com.sia.client.ui;

import com.sia.client.model.Game;
import com.sia.client.model.Line;
import com.sia.client.model.Moneyline;
import com.sia.client.model.Sport;
import com.sia.client.model.Spreadline;

import java.util.Hashtable;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class LineAlertNode {

    String name = "";
    String sport = "";
    int period = 0;
    Vector<String> sportcodes = new Vector();
    Vector<String> bookiecodes = new Vector();


    boolean isspreadcheck = false;
    int spreadsecs = 0;
    int spreadnumbookies = 0;
    boolean isspreadmoveatall = false;
    boolean isspreadptsmove = false;
    boolean isspreadpercentagemove = false;
    double spreadptsmove = 0.0;
    int spreadjuicemove = 0;
    double spreadpercentagemove = 0.0;
    boolean isspreadplayaudio = false;
    String spreadaudiofilename = "";
    boolean isspreadshowpopup = false;
    int spreadpopuplocation = 0;
    int spreadpopupsecs = 0;
    double spreadminsrenotify = 0.0;

    Vector spreadlinestonotify = new Vector();

    Hashtable spreadmoves = new Hashtable();

    boolean istotalcheck = false;
    int totalsecs = 0;
    int totalnumbookies = 0;
    boolean istotalmoveatall = false;
    boolean istotalptsmove = false;
    boolean istotalpercentagemove = false;
    double totalptsmove = 0.0;
    int totaljuicemove = 0;
    double totalpercentagemove = 0.0;
    boolean istotalplayaudio = false;
    String totalaudiofilename = "";
    boolean istotalshowpopup = false;
    int totalpopuplocation = 0;
    int totalpopupsecs = 0;
    double totalminsrenotify = 0.0;

    Vector totallinestonotify = new Vector();
    Hashtable totalmoves = new Hashtable();

    boolean ismoneylinecheck = false;
    int moneylinesecs = 0;
    int moneylinenumbookies = 0;
    boolean ismoneylinemoveatall = false;
    boolean ismoneylinejuicemove = false;
    boolean ismoneylinepercentagemove = false;
    int moneylinejuicemove = 0;
    double moneylinepercentagemove = 0.0;
    boolean ismoneylineplayaudio = false;
    String moneylineaudiofilename = "";
    boolean ismoneylineshowpopup = false;
    int moneylinepopuplocation = 0;
    int moneylinepopupsecs = 0;
    double moneylineminsrenotify = 0.0;

    Vector moneylinestonotify = new Vector();
    Hashtable moneylinemoves = new Hashtable();

    boolean isteamtotalcheck = false;
    int teamtotalsecs = 0;
    int teamtotalnumbookies = 0;
    boolean isteamtotalmoveatall = false;
    boolean isteamtotalptsmove = false;
    boolean isteamtotalpercentagemove = false;
    double teamtotalptsmove = 0.0;
    int teamtotaljuicemove = 0;
    double teamtotalpercentagemove = 0.0;
    boolean isteamtotalplayaudio = false;
    String teamtotalaudiofilename = "";
    boolean isteamtotalshowpopup = false;
    int teamtotalpopuplocation = 0;
    int teamtotalpopupsecs = 0;
    double teamtotalminsrenotify = 0.0;

    Vector teamtotallinestonotify = new Vector();
    Hashtable teamtotalmoves = new Hashtable();

    public LineAlertNode(String name) {
        this.name = name;
    }


    public LineAlertNode(String name, String sport, int period, Vector sportcodes, Vector bookiecodes, boolean isspreadcheck, int spreadsecs, int spreadnumbookies,
                         boolean isspreadmoveatall, boolean isspreadptsmove, boolean isspreadpercentagemove, double spreadptsmove, int spreadjuicemove,
                         double spreadpercentagemove, boolean isspreadplayaudio, String spreadaudiofilename, boolean isspreadshowpopup, int spreadpopuplocation,
                         int spreadpopupsecs, double spreadminsrenotify, boolean istotalcheck, int totalsecs, int totalnumbookies, boolean istotalmoveatall,
                         boolean istotalptsmove, boolean istotalpercentagemove, double totalptsmove, int totaljuicemove, double totalpercentagemove,
                         boolean istotalplayaudio, String totalaudiofilename, boolean istotalshowpopup, int totalpopuplocation, int totalpopupsecs,
                         double totalminsrenotify, boolean ismoneylinecheck, int moneylinesecs, int moneylinenumbookies, boolean ismoneylinemoveatall,
                         boolean ismoneylinejuicemove, boolean ismoneylinepercentagemove, int moneylinejuicemove, double moneylinepercentagemove,
                         boolean ismoneylineplayaudio, String moneylineaudiofilename, boolean ismoneylineshowpopup, int moneylinepopuplocation, int moneylinepopupsecs,
                         double moneylineminsrenotify, boolean isteamtotalcheck, int teamtotalsecs, int teamtotalnumbookies, boolean isteamtotalmoveatall,
                         boolean isteamtotalptsmove, boolean isteamtotalpercentagemove, double teamtotalptsmove, int teamtotaljuicemove, double teamtotalpercentagemove,
                         boolean isteamtotalplayaudio, String teamtotalaudiofilename, boolean isteamtotalshowpopup, int teamtotalpopuplocation, int teamtotalpopupsecs,
                         double teamtotalminsrenotify) {

        this.name = name;
        this.sport = sport;
        this.period = period;
        this.sportcodes = sportcodes;
        this.bookiecodes = bookiecodes;
        this.isspreadcheck = isspreadcheck;
        this.spreadsecs = spreadsecs;
        this.spreadnumbookies = spreadnumbookies;
        this.isspreadmoveatall = isspreadmoveatall;
        this.isspreadptsmove = isspreadptsmove;
        this.isspreadpercentagemove = isspreadpercentagemove;
        this.spreadptsmove = spreadptsmove;
        this.spreadjuicemove = spreadjuicemove;
        this.spreadpercentagemove = spreadpercentagemove;
        log("SPM=" + spreadpercentagemove);
        this.isspreadplayaudio = isspreadplayaudio;
        this.spreadaudiofilename = spreadaudiofilename;
        this.isspreadshowpopup = isspreadshowpopup;
        this.spreadpopuplocation = spreadpopuplocation;
        this.spreadpopupsecs = spreadpopupsecs;
        this.spreadminsrenotify = spreadminsrenotify;
        this.istotalcheck = istotalcheck;
        this.totalsecs = totalsecs;
        this.totalnumbookies = totalnumbookies;
        this.istotalmoveatall = istotalmoveatall;
        this.istotalptsmove = istotalptsmove;
        this.istotalpercentagemove = istotalpercentagemove;
        this.totalptsmove = totalptsmove;
        this.totaljuicemove = totaljuicemove;
        this.totalpercentagemove = totalpercentagemove;
        this.istotalplayaudio = istotalplayaudio;
        this.totalaudiofilename = totalaudiofilename;
        this.istotalshowpopup = istotalshowpopup;
        this.totalpopuplocation = totalpopuplocation;
        this.totalpopupsecs = totalpopupsecs;
        this.totalminsrenotify = totalminsrenotify;
        this.ismoneylinecheck = ismoneylinecheck;
        this.moneylinesecs = moneylinesecs;
        this.moneylinenumbookies = moneylinenumbookies;
        this.ismoneylinemoveatall = ismoneylinemoveatall;
        this.ismoneylinejuicemove = ismoneylinejuicemove;
        this.ismoneylinepercentagemove = ismoneylinepercentagemove;
        this.moneylinejuicemove = moneylinejuicemove;
        this.moneylinepercentagemove = moneylinepercentagemove;
        this.ismoneylineplayaudio = ismoneylineplayaudio;
        this.moneylineaudiofilename = moneylineaudiofilename;
        this.ismoneylineshowpopup = ismoneylineshowpopup;
        this.moneylinepopuplocation = moneylinepopuplocation;
        this.moneylinepopupsecs = moneylinepopupsecs;
        this.moneylineminsrenotify = moneylineminsrenotify;
        this.isteamtotalcheck = isteamtotalcheck;
        this.teamtotalsecs = teamtotalsecs;
        this.teamtotalnumbookies = teamtotalnumbookies;
        this.isteamtotalmoveatall = isteamtotalmoveatall;
        this.isteamtotalptsmove = isteamtotalptsmove;
        this.isteamtotalpercentagemove = isteamtotalpercentagemove;
        this.teamtotalptsmove = teamtotalptsmove;
        this.teamtotaljuicemove = teamtotaljuicemove;
        this.teamtotalpercentagemove = teamtotalpercentagemove;
        this.isteamtotalplayaudio = isteamtotalplayaudio;
        this.teamtotalaudiofilename = teamtotalaudiofilename;
        this.isteamtotalshowpopup = isteamtotalshowpopup;
        this.teamtotalpopuplocation = teamtotalpopuplocation;
        this.teamtotalpopupsecs = teamtotalpopupsecs;
        this.teamtotalminsrenotify = teamtotalminsrenotify;
    }

    public int getGamePeriod() {
        return period;
    }

    public boolean isSpreadCheck() {
        return isspreadcheck;
    }

    public int getSpreadSecs() {
        return spreadsecs;
    }

    public int getSpreadNumBookies() {
        return spreadnumbookies;
    }

    public boolean isSpreadMoveAtAll() {
        return isspreadmoveatall;
    }

    public boolean isSpreadPtsMove() {
        return isspreadptsmove;
    }

    public boolean isSpreadPercentageMove() {
        return isspreadpercentagemove;
    }

    public double getSpreadPtsMove() {
        return spreadptsmove;
    }

    public int getSpreadJuiceMove() {
        return spreadjuicemove;
    }

    public double getSpreadPercentageMove() {
        return spreadpercentagemove;
    }

    public boolean isSpreadPlayAudio() {
        return isspreadplayaudio;
    }

    public String getSpreadAudioFileName() {
        return spreadaudiofilename;
    }

    public boolean isSpreadShowPopup() {
        return isspreadshowpopup;
    }

    public int getSpreadPopupLocation() {
        return spreadpopuplocation;
    }

    public int getSpreadPopupSecs() {
        return spreadpopupsecs;
    }

    public double getSpreadMinsRenotify() {
        return spreadminsrenotify;
    }

    public boolean isTotalCheck() {
        return istotalcheck;
    }

    public int getTotalSecs() {
        return totalsecs;
    }

    public int getTotalNumBookies() {
        return totalnumbookies;
    }

    public boolean isTotalMoveAtAll() {
        return istotalmoveatall;
    }

    public boolean isTotalPtsMove() {
        return istotalptsmove;
    }

    public boolean isTotalPercentageMove() {
        return istotalpercentagemove;
    }

    public double getTotalPtsMove() {
        return totalptsmove;
    }

    public int getTotalJuiceMove() {
        return totaljuicemove;
    }

    public double getTotalPercentageMove() {
        return totalpercentagemove;
    }

    public boolean isTotalPlayAudio() {
        return istotalplayaudio;
    }

    public String getTotalAudioFileName() {
        return totalaudiofilename;
    }

    public boolean isTotalShowPopup() {
        return istotalshowpopup;
    }

    public int getTotalPopupLocation() {
        return totalpopuplocation;
    }

    public int getTotalPopupSecs() {
        return totalpopupsecs;
    }

    public double getTotalMinsRenotify() {
        return totalminsrenotify;
    }

    public boolean isMoneylineCheck() {
        return ismoneylinecheck;
    }

    public int getMoneylineSecs() {
        return moneylinesecs;
    }

    public int getMoneylineNumBookies() {
        return moneylinenumbookies;
    }

    public boolean isMoneylineMoveAtAll() {
        return ismoneylinemoveatall;
    }

    public boolean isMoneylineJuiceMove() {
        return ismoneylinejuicemove;
    }

    public boolean isMoneylinePercentageMove() {
        return ismoneylinepercentagemove;
    }

    public int getMoneylineJuiceMove() {
        return moneylinejuicemove;
    }

    public double getMoneylinePercentageMove() {
        return moneylinepercentagemove;
    }

    public boolean isMoneylinePlayAudio() {
        return ismoneylineplayaudio;
    }

    public String getMoneylineAudioFileName() {
        return moneylineaudiofilename;
    }

    public boolean isMoneylineShowPopup() {
        return ismoneylineshowpopup;
    }

    public int getMoneylinePopupLocation() {
        return moneylinepopuplocation;
    }

    public int getMoneylinePopupSecs() {
        return moneylinepopupsecs;
    }

    public double getMoneylineMinsRenotify() {
        return moneylineminsrenotify;
    }

    public boolean isTeamtotalCheck() {
        return isteamtotalcheck;
    }

    public int getTeamtotalSecs() {
        return teamtotalsecs;
    }

    public int getTeamtotalNumBookies() {
        return teamtotalnumbookies;
    }

    public boolean isTeamtotalMoveAtAll() {
        return isteamtotalmoveatall;
    }

    public boolean isTeamtotalPtsMove() {
        return isteamtotalptsmove;
    }

    public boolean isTeamtotalPercentageMove() {
        return isteamtotalpercentagemove;
    }

    public double getTeamtotalPtsMove() {
        return teamtotalptsmove;
    }

    public int getTeamtotalJuiceMove() {
        return teamtotaljuicemove;
    }

    public double getTeamtotalPercentageMove() {
        return teamtotalpercentagemove;
    }

    public boolean isTeamtotalPlayAudio() {
        return isteamtotalplayaudio;
    }

    public String getTeamtotalAudioFileName() {
        return teamtotalaudiofilename;
    }

    public boolean isTeamtotalShowPopup() {
        return isteamtotalshowpopup;
    }

    public int getTeamtotalPopupLocation() {
        return teamtotalpopuplocation;
    }

    public int getTeamtotalPopupSecs() {
        return teamtotalpopupsecs;
    }

    public double getTeamtotalMinsRenotify() {
        return teamtotalminsrenotify;
    }

    public String toStorageString() {
        String delimiter = "!";
        String sportscodesstring = sportcodes.toString();
        sportscodesstring = sportscodesstring.replace("[", "");
        sportscodesstring = sportscodesstring.replace("]", "");
        sportscodesstring = sportscodesstring.replaceAll(" ", "");

        String bookiecodesstring = bookiecodes.toString();
        bookiecodesstring = bookiecodesstring.replace("[", "");
        bookiecodesstring = bookiecodesstring.replace("]", "");
        bookiecodesstring = bookiecodesstring.replaceAll(" ", "");
        String retValue = name + delimiter +
                sport + delimiter +
                period + delimiter +
                sportscodesstring + delimiter +
                bookiecodesstring + delimiter +
                isspreadcheck + delimiter +
                spreadsecs + delimiter +
                spreadnumbookies + delimiter +
                isspreadmoveatall + delimiter +
                isspreadptsmove + delimiter +
                isspreadpercentagemove + delimiter +
                spreadptsmove + delimiter +
                spreadjuicemove + delimiter +
                spreadpercentagemove + delimiter +
                isspreadplayaudio + delimiter +
                spreadaudiofilename + delimiter +
                isspreadshowpopup + delimiter +
                spreadpopuplocation + delimiter +
                spreadpopupsecs + delimiter +
                spreadminsrenotify + delimiter +
                istotalcheck + delimiter +
                totalsecs + delimiter +
                totalnumbookies + delimiter +
                istotalmoveatall + delimiter +
                istotalptsmove + delimiter +
                istotalpercentagemove + delimiter +
                totalptsmove + delimiter +
                totaljuicemove + delimiter +
                totalpercentagemove + delimiter +
                istotalplayaudio + delimiter +
                totalaudiofilename + delimiter +
                istotalshowpopup + delimiter +
                totalpopuplocation + delimiter +
                totalpopupsecs + delimiter +
                totalminsrenotify + delimiter +
                ismoneylinecheck + delimiter +
                moneylinesecs + delimiter +
                moneylinenumbookies + delimiter +
                ismoneylinemoveatall + delimiter +
                ismoneylinejuicemove + delimiter +
                ismoneylinepercentagemove + delimiter +
                moneylinejuicemove + delimiter +
                moneylinepercentagemove + delimiter +
                ismoneylineplayaudio + delimiter +
                moneylineaudiofilename + delimiter +
                ismoneylineshowpopup + delimiter +
                moneylinepopuplocation + delimiter +
                moneylinepopupsecs + delimiter +
                moneylineminsrenotify + delimiter +
                isteamtotalcheck + delimiter +
                teamtotalsecs + delimiter +
                teamtotalnumbookies + delimiter +
                isteamtotalmoveatall + delimiter +
                isteamtotalptsmove + delimiter +
                isteamtotalpercentagemove + delimiter +
                teamtotalptsmove + delimiter +
                teamtotaljuicemove + delimiter +
                teamtotalpercentagemove + delimiter +
                isteamtotalplayaudio + delimiter +
                teamtotalaudiofilename + delimiter +
                isteamtotalshowpopup + delimiter +
                teamtotalpopuplocation + delimiter +
                teamtotalpopupsecs + delimiter +
                teamtotalminsrenotify;
        return retValue;
    }

    public Vector getSportCodes() {
        return sportcodes;
    }

    public Vector getBookieCodes() {
        return bookiecodes;
    }

    public String getSport() {
        return sport;
    }

    public boolean doesthislinequalify(Line line) {
        Game g = AppController.getGame(line.getGameid());
        Sport s = AppController.getSportByLeagueId(g.getLeague_id());
        int leagueid = g.getLeague_id();
        int bookieid = line.getBookieid();
        int periodid = line.getPeriod();
        int sportid = s.getSport_id();
        int gameid = g.getGame_id();
        boolean bookiecodehere = false;

        boolean periodgood = false;
        if (period == 125678) {
            periodgood = true;
        } else if (period == 1212 && (periodid >= 1 && periodid <= 2)) {
            periodgood = true;
        } else if (period == 5678 && (periodid >= 5 && periodid <= 8)) {
            periodgood = true;
        } else if (period == periodid) {
            periodgood = true;
        }


		/*
		if(sportid==1 || sportid==2)
		{
			if(line instanceof Spreadline)
			{
				log("ITS A SPREAD!!!!");
				log("periodid="+periodid+"vs"+period+"....bookieid="+bookieid+"....sportid="+sportid+"..."+isspreadcheck);
				for(int k =0; k < bookiecodes.size(); k++)
				{
					String thiscode = bookiecodes.elementAt(k);
					if(thiscode.equals(""+bookieid))
					{
						log("BOOKIECODE HERE!!!!!!"+bookieid);
						bookiecodehere = true;
					}
					log(bookiecodes.elementAt(k)+"....");

				}
				log("");
				for(int k =0; k < sportcodes.size(); k++)
				{
					System.out.print(sportcodes.elementAt(k)+",");
				}
				log("");

				if(bookiecodes.contains(""+bookieid))
				{
					log("WE GOT THIS BOOKIE ID!");
				}
				else
				{
					log("WE DONT GOT THIS BOOKIE ID!");
				}
				if(sportcodes.contains(""+sportid))
				{
					log("WE GOT THIS SPORT ID!");
				}
				else
				{
					log("WE DONT GOT THIS SPORT ID!");
				}
			}

		}
		*/
        //if(periodid == period && bookiecodes.contains(""+bookieid) && sportcodes.contains(""+sportid))
        if (periodgood && bookiecodes.contains("" + bookieid) && sportcodes.contains("" + leagueid)) {

            if (line instanceof Spreadline && isspreadcheck) {
                //log(isspreadcheck+".."+"periodid="+periodid+"vs"+period+"...bid="+bookieid+"...sid="+sportid);
                return documentSpreadMove((Spreadline) line);
            } else if (line instanceof Totalline && istotalcheck) {
                return documentTotalMove((Totalline) line);
            } else if (line instanceof Moneyline && ismoneylinecheck) {
                return documentMoneylineMove((Moneyline) line);
            } else if (line instanceof TeamTotalline && isteamtotalcheck) {
                return documentTeamTotalMove((TeamTotalline) line);
            } else {

                return false;
            }

        } else {
            return false;
        }


    }

    // owen maybe put logic in on which way it moved using whowasbet in line object?
    public boolean documentSpreadMove(Spreadline line) {
        try {
            if (isspreadmoveatall) {
                return shouldIAlertSpreadline(line);
            } else if (isspreadptsmove) {
                if (Math.abs(line.getCurrentvisitspread() - line.getPriorvisitspread()) >= spreadptsmove
                        || juicemovedenough(line.getCurrentvisitjuice(), line.getPriorvisitjuice(), spreadjuicemove)) {
                    return shouldIAlertSpreadline(line);
                } else {
                    return false;
                }
            } else if (isspreadpercentagemove) {
                if (false) // here it will be a complicated percentagemove check based on sport etc...
                {
                    return shouldIAlertSpreadline(line);
                } else {
                    return false;

                }
            } else {
                return false;
            }


        } catch (Exception ex) {
            log(ex);
        }
        return false;
    }

    public boolean documentTotalMove(Totalline line) {
        try {
            if (istotalmoveatall) {
                return shouldIAlertTotalline(line);
            } else if (istotalptsmove) {
                if (Math.abs(line.getCurrentover() - line.getPriorover()) >= totalptsmove
                        || juicemovedenough(line.getCurrentoverjuice(), line.getPrioroverjuice(), totaljuicemove)) {

                    return shouldIAlertTotalline(line);

                } else {
                    return false;
                }
            } else if (istotalpercentagemove) {
                if (false) // here it will be a complicated percentagemove check based on sport etc...
                {
                    return shouldIAlertTotalline(line);
                } else {
                    return false;

                }
            } else {
                return false;
            }


        } catch (Exception ex) {
            log(ex);
        }
        return false;
    }

    public boolean documentMoneylineMove(Moneyline line) {
        try {
            if (ismoneylinemoveatall) {
                return shouldIAlertMoneyline(line);
            } else if (ismoneylinejuicemove) {

                if (juicemovedenough(line.getCurrentvisitjuice(), line.getPriorvisitjuice(), moneylinejuicemove)) {
                    return shouldIAlertMoneyline(line);
                } else {
                    return false;
                }

            } else if (ismoneylinepercentagemove) {
                if (false) // here it will be a complicated percentagemove check based on sport etc...
                {
                    return shouldIAlertMoneyline(line);
                } else {
                    return false;

                }
            } else {
                return false;
            }


        } catch (Exception ex) {
            log(ex);
        }
        return false;
    }

    public boolean documentTeamTotalMove(TeamTotalline line) {
        try {
            if (isteamtotalmoveatall) {
                return shouldIAlertTeamTotalline(line);
            } else if (isteamtotalptsmove) {

                if (Math.abs(line.getCurrentvisitover() - line.getPriorvisitover()) >= teamtotalptsmove
                        ||
                        juicemovedenough(line.getCurrentvisitoverjuice(), line.getPriorvisitoverjuice(), teamtotaljuicemove)) {

                    return shouldIAlertTeamTotalline(line);
                } else {
                    return false;
                }
            } else if (isteamtotalpercentagemove) {
                if (false) // here it will be a complicated percentagemove check based on sport etc...
                {
                    return shouldIAlertTeamTotalline(line);
                } else {
                    return false;

                }
            } else {
                return false;
            }


        } catch (Exception ex) {
            log(ex);
        }
        return false;
    }

    public boolean shouldIAlertSpreadline(Spreadline line) {
        //log("In shouldIAlertSpreadLine...");
        if (spreadmoves.get("" + line.getGameid() + line.whowasbet) == null) {
            spreadmoves.put("" + line.getGameid() + line.whowasbet, new LineAlertQueue(spreadnumbookies));
        }
        LineAlertQueue queue = (LineAlertQueue) spreadmoves.get("" + line.getGameid() + line.whowasbet);
        //log("queue inital "+queue+"..."+queue.size());
        // doing this next line to maker sure there are no duplicate bookie moves in queue
        queue.remove(line);

        if (queue.size() < spreadnumbookies - 1) {
            queue.add(line);
            return false;
        } else if (queue.size() == spreadnumbookies - 1) {
            queue.add(line);
            //log("queue after add "+queue);
            Spreadline old = (Spreadline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            long diff = newsecs - oldsecs;
            long difflastalert = newsecs - queue.getlastalerted();
            //log("diff="+diff+"..spreadsecs="+spreadsecs+"..difflastalert="+difflastalert+"..minsren="+spreadminsrenotify);
            if (newsecs - oldsecs <= spreadsecs * 1000) {

                if (newsecs - queue.getlastalerted() >= spreadminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    spreadlinestonotify.clear();
                    queue.drainTo(spreadlinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    double diffinmins = difflastalert / 60000;
                    log(old.getGameid() + " move qualifies too soon.difflastalert=" + diffinmins + "..minsren=" + spreadminsrenotify);
                    return false;
                }
            } else {
                return false;
            }


        } else if (queue.size() == spreadnumbookies) {
            queue.poll(); // this simply removes oldest element
            queue.add(line);

            Spreadline old = (Spreadline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            long diff = newsecs - oldsecs;
            long difflastalert = newsecs - queue.getlastalerted();

            if (newsecs - oldsecs <= spreadsecs * 1000) {
                if (newsecs - queue.getlastalerted() >= spreadminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    spreadlinestonotify.clear();
                    queue.drainTo(spreadlinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    double diffinmins = difflastalert / 60000;
                    log(old.getGameid() + " move qualifies too soon.difflastalert=" + diffinmins + "..minsren=" + spreadminsrenotify);
                    return false;
                }
            } else {
                return false;
            }
        } else // should be impossible!
        {
            log("HOW DID I GET HERE SHOULDIALERTSPREADLINE!!!!!");
            return false;
        }
    }

    private boolean juicemovedenough(double j1, double j2, int juicemove) {
        if (j1 > 0 && j2 < 0) {
            if ((j1 - 100.00) + (-100.00 - j2) >= juicemove) {
                return true;
            } else {
                return false;
            }

        } else if (j1 < 0 && j2 > 0) {
            if ((j2 - 100.00) + (-100.00 - j1) >= juicemove) {
                return true;
            } else {
                return false;
            }
        } else {
            if (Math.abs(j1 - j2) >= juicemove) {
                return true;
        } else {
                return false;
            }
        }


    }

    public boolean shouldIAlertTotalline(Totalline line) {
        if (totalmoves.get("" + line.getGameid() + line.whowasbet) == null) {
            totalmoves.put("" + line.getGameid() + line.whowasbet, new LineAlertQueue(totalnumbookies));
        }
        LineAlertQueue queue = (LineAlertQueue) totalmoves.get("" + line.getGameid() + line.whowasbet);

        // doing this next line to maker sure there are no duplicate bookie moves in queue
        queue.remove(line);

        if (queue.size() < totalnumbookies - 1) {
            queue.add(line);
            return false;
        } else if (queue.size() == totalnumbookies - 1) {
            queue.add(line);

            Totalline old = (Totalline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= totalsecs * 1000) {
                if (newsecs - queue.getlastalerted() >= totalminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    totallinestonotify.clear();
                    queue.drainTo(totallinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }


        } else if (queue.size() == totalnumbookies) {
            queue.poll(); // this simply removes oldest element
            queue.add(line);

            Totalline old = (Totalline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= totalsecs * 1000) {
                if (newsecs - queue.getlastalerted() >= totalminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    totallinestonotify.clear();
                    queue.drainTo(totallinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }
        } else // should be impossible!
        {
            log("HOW DID I GET HERE SHOULDIALERT TOTALLINE!!!!!");
            return false;
        }
    }

    public boolean shouldIAlertMoneyline(Moneyline line) {
        if (moneylinemoves.get("" + line.getGameid() + line.whowasbet) == null) {
            moneylinemoves.put("" + line.getGameid() + line.whowasbet, new LineAlertQueue(moneylinenumbookies));
        }
        LineAlertQueue queue = (LineAlertQueue) moneylinemoves.get("" + line.getGameid() + line.whowasbet);

        // doing this next line to maker sure there are no duplicate bookie moves in queue
        queue.remove(line);

        if (queue.size() < moneylinenumbookies - 1) {
            queue.add(line);
            return false;
        } else if (queue.size() == moneylinenumbookies - 1) {
            queue.add(line);

            Moneyline old = (Moneyline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= moneylinesecs * 1000) {
                if (newsecs - queue.getlastalerted() >= moneylineminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    moneylinestonotify.clear();
                    queue.drainTo(moneylinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }


        } else if (queue.size() == moneylinenumbookies) {
            queue.poll(); // this simply removes oldest element
            queue.add(line);

            Moneyline old = (Moneyline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= moneylinesecs * 1000) {
                if (newsecs - queue.getlastalerted() >= moneylineminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    moneylinestonotify.clear();
                    queue.drainTo(moneylinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }
        } else // should be impossible!
        {
            log("HOW DID I GET HERE SHOULDIALERT MONEYLINE!!!!!");
            return false;
        }
    }

    public boolean shouldIAlertTeamTotalline(TeamTotalline line) {
        if (teamtotalmoves.get("" + line.getGameid() + line.whowasbet) == null) {
            teamtotalmoves.put("" + line.getGameid() + line.whowasbet, new LineAlertQueue(teamtotalnumbookies));
        }
        LineAlertQueue queue = (LineAlertQueue) teamtotalmoves.get("" + line.getGameid() + line.whowasbet);

        // doing this next line to maker sure there are no duplicate bookie moves in queue
        queue.remove(line);

        if (queue.size() < teamtotalnumbookies - 1) {
            queue.add(line);
            return false;
        } else if (queue.size() == teamtotalnumbookies - 1) {
            queue.add(line);

            TeamTotalline old = (TeamTotalline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= teamtotalsecs * 1000) {
                if (newsecs - queue.getlastalerted() >= teamtotalminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    teamtotallinestonotify.clear();
                    queue.drainTo(teamtotallinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }

        } else if (queue.size() == teamtotalnumbookies) {
            queue.poll(); // this simply removes oldest element
            queue.add(line);

            TeamTotalline old = (TeamTotalline) queue.peek();
            long oldsecs = old.getCurrentts();
            long newsecs = line.getCurrentts();
            if (newsecs - oldsecs <= teamtotalsecs * 1000) {
                if (newsecs - queue.getlastalerted() >= teamtotalminsrenotify * 60 * 1000) {
                    //ok i will clear queue, update last alerted and my handler will send out alert when i return true
                    teamtotallinestonotify.clear();
                    queue.drainTo(teamtotallinestonotify);
                    queue.setlastalerted(newsecs);
                    return true;
                } else // move qualifies but its too soon since last notification
                {
                    return false;
                }
            } else {
                return false;
            }
        } else // should be impossible!
        {
            log("HOW DID I GET HERE SHOULDIALERT TEAMTOTALLINE!!!!!");
            return false;
        }
    }

    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }
}