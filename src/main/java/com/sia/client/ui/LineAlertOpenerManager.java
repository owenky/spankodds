package com.sia.client.ui;



import com.sia.client.media.SoundPlayer;
import com.sia.client.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LineAlertOpenerManager
{
    static Vector<String> leagueids = new Vector();
    static Hashtable<String,Vector> bookieidsBySport = new Hashtable<String,Vector>();
    static Hashtable<String,Long> lastAlertForThisLeagueHash = new Hashtable<String,Long>();
    static Hashtable<String,LineOpenerAlertNode> lans= new Hashtable<String,LineOpenerAlertNode>();

    static String storageString = "";

    public static String getStorageString()
    {
        return storageString;
    }
    public static void setStorageString(String s)
    {
        storageString = s;
    }

    public static void reloadprefs()
    {
        storageString = "";
        leagueids.clear();
        bookieidsBySport.clear();

        for (int i = 0; i < AppController.LineOpenerAlertNodeList.size(); i++) {
            LineOpenerAlertNode lan = AppController.LineOpenerAlertNodeList.get(i);


            String sport = lan.getSport();
            leagueids.addAll(lan.checkedLeagueNodes);
            bookieidsBySport.put(sport,lan.checkedBookieNodes);
            lans.put(sport,lan);
            storageString = storageString+lan.toString()+"~";
        }
        AppController.getUser().setOpeneralert(storageString);

    }


    public static String[] gameperiod = new String[]{"Full Game", "1st Half", "2nd Half", "All Halfs", " ", "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter", "Live", "All Periods"};

    public static void openerAlert(int gid, int bid, int period, Line line)
    {
        reloadprefs();
        Game game = AppController.getGame(gid);
        Bookie b = AppController.getBookie(bid);
        Sport sport = AppController.getSportByLeagueId(game.getLeague_id());
        if(sport == null)
        {
            return;
        }
        String sportname = sport.getSportname();
        LineOpenerAlertNode lan = lans.get(sportname);
        //step 1 check if gameid is involved in a league id we have checked off

        if(!leagueids.contains(""+game.getLeague_id()))
        {
            log("Opener but leagueid not included "+game.getLeague_id());
            return;
        }
        //step2 check if bookie id is checked off in sport
         Vector bookieschecked = bookieidsBySport.get(sportname);
        if(!bookieschecked.contains(bid+""))
        {
            log("Opener but bookie not included "+b);
            return;
        }
        //step 3 check if game period is checked off

        if(     (period == 0 && lan.isFullGameCheck) ||
                    (period == 1 && lan.is1stHafCheck) ||
                        (period == 2 && lan.is2ndHalfCheck) ||
                                (period == 5 && lan.is1stQutCheck) ||
                                        (period == 6 && lan.is2ndQutCheck) ||
                                                (period == 7 && lan.is3rdQutCheck) ||
                                                        (period == 8 && lan.is4thQutCheck)
        )
        {
            //good
        }
        else
        {
            log("Opener but period not included "+period);
            return;
        }
        //step4 check if linetype is good
        if( ( line instanceof Spreadline && lan.isSpreadCheck) ||
                ( line instanceof Totalline && lan.isTotalCheck) ||
                ( line instanceof Moneyline && lan.isMoneyCheck) ||
                ( line instanceof TeamTotalline && lan.isTeamTotalCheck)

        )
        {
            // good;
        }
        else
        {
            log("Opener but type not included "+line.getType());
            return;
        }
        // step 5 make sure we don't violate the renotify
        Long lastupdate = lastAlertForThisLeagueHash.get(""+game.getLeague_id()+"-"+bid);
        long nowms = System.currentTimeMillis();
        if(lastupdate == null || nowms - lastupdate.longValue() > lan.renotifyvalue*60*1000)
        {
            // time to notify
            lastAlertForThisLeagueHash.put(""+game.getLeague_id()+"-"+bid,new Long(nowms));

            // audio
            if(lan.isAudioChecks)
            {
                try {
                    SoundPlayer.playSound(lan.soundfile);
                } catch (Exception ex) {
                    showMessageDialog(null, "Error Playing Opener Sound File!");
                    log(ex);
                }

            }
            // popup
            if(lan.isShowpopChecks)
            {
                String mesg = "Opener:" + b+" "+getLeagueAbbr(game.getLeague_id()) + " " + line.getType()+" "+game.getGame_id()+" "+game.getGameString();
                String hrmin = AppController.getCurrentHoursMinutes();
                AppController.addAlert(hrmin,mesg);

                //log(com.sia.client.ui.AppController.alertsVector.size());

                new UrgentMessage("<HTML><H1>"+line.getType().toUpperCase()+" Openers " + getLeagueAbbr(game.getLeague_id()) + "</H1><FONT COLOR=BLUE>" +
                        b + "<BR><TABLE cellspacing=5 cellpadding=5>" +
                        "<TR><TD COLSPAN=4>" + game.getGame_id()+" "+game.getGameString() + "</TD><TD>"+line.getOpener()+"</TD></TR>" +
                        "</TABLE></FONT></HTML>", lan.popupsec * 1000, lan.popuplocationint, AppController.getMainTabPane());

            }
            // text message
            if(lan.isTextChecks)
            {
                // put in email text message here..

            }

        }
        else
        {
            return;
        }





    }





    public static String getSportName(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String sportname = "";
        for (Sport sp : Sports) {
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                sportname = sp.getSportname();
                break;

            }
        }
        return sportname;

    }

    public static String getLeagueName(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String leaguename = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leaguename = sp.getLeaguename();
                break;

            }
        }

        return leaguename;
    }
    public static String getLeagueAbbr(int lid) {
        List<Sport> Sports = AppController.getSportsVec();
        String leagueabbr = "";
        for (int i = 0; i < Sports.size(); i++) {
            Sport sp = Sports.get(i);
            int LeagueId = sp.getLeague_id();
            if (LeagueId == lid) {
                leagueabbr = sp.getLeagueabbr();
                break;

            }
        }

        return leagueabbr;
    }

}
