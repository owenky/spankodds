package com.sia.client.ui;



import com.sia.client.media.SoundPlayer;
import com.sia.client.model.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static com.sia.client.config.Utils.log;
import static com.sia.client.config.Utils.showMessageDialog;

public class LimitAlertManager
{
    static Vector<String> leagueids = new Vector();
    static Hashtable<String,List> bookieidsBySport = new Hashtable<String,List>();
    static Hashtable<String,Long> lastAlertForThisLeagueHash = new Hashtable<String,Long>();
    static Hashtable<String,LimitNode> lans= new Hashtable<String,LimitNode>();
    static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static String storageString = "";

    public static String getStorageString()
    {
        return storageString;
    }
    public static void setStorageString(String s)
    {
        storageString = s;
    }
    public static String[] gameperiod = new String[]{"Full Game", "1st Half", "2nd Half", "All Halfs", " ", "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter", "Live", "All Periods"};
    public static void reloadprefs()
    {
        storageString = "";
        leagueids.clear();
        bookieidsBySport.clear();

        for (int i = 0; i < AppController.LimitNodeList.size(); i++) {
            LimitNode lan = AppController.LimitNodeList.get(i);

            String sport = lan.getSport();
            leagueids.addAll(lan.sportcodes);
            bookieidsBySport.put(sport,lan.bookiecodes);
            lans.put(sport,lan);
            storageString = storageString+lan.toString()+"@";
        }
        AppController.getUser().setLimitchangeAlert(storageString);

    }




    public static void limitChangeAlert(int gid, int bid, int period,double oldlimit,double newlimit, Line line)
    {
        reloadprefs();
        double percentagechange;
        boolean limitincrease = true;
        if(oldlimit == 0 || newlimit == 0 || oldlimit == newlimit)
        {
            return;
        }
        else
        {
            percentagechange = 100*(newlimit-oldlimit)/oldlimit;
            if(newlimit > oldlimit )
            {
                limitincrease = true;
            }
            else
            {
               limitincrease = false;
            }
        }
        reloadprefs();
        Game game = AppController.getGame(gid);
        Bookie b = AppController.getBookie(bid);
        Sport sport = AppController.getSportByLeagueId(game.getLeague_id());
        if(sport == null)
        {
            return;
        }
        String sportname = sport.getSportname();
        LimitNode lan = lans.get(sportname);
        //step 1 check if gameid is involved in a league id we have checked off
        int leagueidtocheck = game.getLeague_id();
        if(leagueidtocheck == 9)
        {
            leagueidtocheck = game.getSubleague_id();
        }
        if(!leagueids.contains(""+leagueidtocheck))
        {
           // log("Limit Change but leagueid not included "+leagueidtocheck);
          //  log(""+leagueids.toString());
            return;
        }
        //step2 check if bookie id is checked off in sport
        ArrayList bookieschecked = (ArrayList)bookieidsBySport.get(sportname);
        if(!bookieschecked.contains(bid+""))
        {
          //  log("Limit Change but bookie not included "+b);
          //  log(""+bookieschecked.toString());
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
         //   log("Limit Change but period not included "+period);
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
          //  log("Limit Change but type not included "+line.getType());
            return;
        }
        //step5 check if % is enough
        if(percentagechange < lan.minpercentagechange )
        {
         //   log("Limit Change but % not high enough "+percentagechange);
            return;
        }

        // step 6 make sure we don't violate the renotify
        Long lastupdate = lastAlertForThisLeagueHash.get(""+game.getLeague_id()+"-"+bid);
        long nowms = System.currentTimeMillis();
        if(lastupdate == null || nowms - lastupdate.longValue() > lan.renotifyvalue*60*1000)
        {
            // time to notify
            lastAlertForThisLeagueHash.put(""+game.getLeague_id()+"-"+bid,new Long(nowms));

            // audio
            if(limitincrease && lan.isIncAudioChecks)
            {
                try {
                    SoundPlayer.playSound(lan.incsoundfile);
                } catch (Exception ex) {
                    showMessageDialog(null, "Error Playing Limit Change Inc Sound File!");
                    log(ex);
                }

            }
            if(!limitincrease && lan.isDecAudioChecks)
            {
                try {
                    SoundPlayer.playSound(lan.decsoundfile);
                } catch (Exception ex) {
                    showMessageDialog(null, "Error Playing Limit Change Dec Sound File!");
                    log(ex);
                }

            }
            // popup
            String ts = sdf3.format(new java.sql.Timestamp(new java.util.Date().getTime()));
            if(limitincrease && lan.isIncShowpopChecks)
            {
                String mesg = "Limit Increase:" + b+" "+getLeagueAbbr(game.getLeague_id()) + " " + line.getType()+" "+game.getGame_id()+" "+game.getGameString()+" "+gameperiod[period]+" From "+oldlimit+" to "+newlimit;
                String hrmin = AppController.getCurrentHoursMinutes();
                AppController.addAlert(hrmin,mesg);

                //log(com.sia.client.ui.AppController.alertsVector.size());

                new UrgentMessage("<HTML><H2>"+line.getType().toUpperCase()+" Limit Increase " + getLeagueAbbr(game.getLeague_id()) + "</H2><FONT COLOR=BLUE>" +
                        b + "<BR><TABLE>" +
                        "<TR><TD COLSPAN=3>" + game.getGame_id()+" "+game.getGameString() +" "+gameperiod[period]+"</TD></TR>"+
                        "<TR><TD COLSPAN=3>From "+oldlimit+" to "+newlimit+"</TD></TR>" +
                       "<TR><TD COLSPAN=3>"+ts+"</TD></TR>"+
                        "</TABLE></FONT></HTML>", lan.popupsec * 1000, lan.popuplocationint, AppController.getMainTabPane());

            }
            if(!limitincrease && lan.isDecShowpopChecks)
            {
                String mesg = "Limit Decrease:" + b+" "+getLeagueAbbr(game.getLeague_id()) + " " + line.getType()+" "+game.getGame_id()+" "+game.getGameString()+" "+gameperiod[period]+" From "+oldlimit+" to "+newlimit;
                String hrmin = AppController.getCurrentHoursMinutes();
                AppController.addAlert(hrmin,mesg);

                //log(com.sia.client.ui.AppController.alertsVector.size());

                new UrgentMessage("<HTML><H2>"+line.getType().toUpperCase()+" Limit Decrease " + getLeagueAbbr(game.getLeague_id()) + "</H2><FONT COLOR=RED>" +
                        b + "<BR><TABLE>" +
                        "<TR><TD COLSPAN=3>" + game.getGame_id()+" "+game.getGameString() +" "+gameperiod[period]+"</TD></TR>"+
                        "<TR><TD COLSPAN=3>From "+oldlimit+" to "+newlimit+"</TD></TR>" +
                        "<TR><TD COLSPAN=3>"+ts+"</TD></TR>"+
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
