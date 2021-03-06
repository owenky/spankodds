package com.sia.client.ui;

import com.sia.client.config.GameUtils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
import com.sia.client.model.GameStatus;
import com.sia.client.model.Sport;
import com.sia.client.simulator.InitialGameMessages;

import static com.sia.client.config.Utils.log;

public abstract class ScoreChangedProcessor {

    public static void process(GameStatus newGameStatus, Game g,  int currentvisitorscore, int currenthomescore) {

        //owen 8/11 moved final as first block since grand salami was causing started and final to both execute
        if (! newGameStatus.isSame(g.getStatus()) ) {
            Sport s = AppController.getSportByLeagueId(g.getLeague_id());
            if ( null == s) {
                log("ScoreChangedProcessor: Can't find sport for league:"+g.getLeague_id());
                return;
            }
            log("game "+ GameUtils.getGameDebugInfo(g)+" is about to move from "+g.getStatus()+" to "+newGameStatus.name());
            AppController.moveGameToThisHeader(g, newGameStatus.getGroupHeader());
            String finalprefs = newGameStatus.getAlertPrefSupplier().get();
            String[] arr  = finalprefs.split("\\|");
            boolean popup = false;
            boolean sound = false;
            int popupsecs = 15;
            int location = 6;
            String audiofile;
            String[] sports;
            boolean goodsport = false;

            try {
                popup = Boolean.parseBoolean(arr[0]);
            } catch (Exception ex) {
                log(ex);
            }
            try {
                sound = Boolean.parseBoolean(arr[1]);
            } catch (Exception ex) {
                log(ex);
            }
            try {
                popupsecs = Integer.parseInt(arr[2]);
            } catch (Exception ex) {
                log(ex);
            }
            try {
                location = Integer.parseInt(arr[3]);
            } catch (Exception ex) {
                log(ex);
            }

            audiofile = 5<=arr.length?arr[4]:"";

            sports = 6<=arr.length?arr[5].split(","):new String [0];
            for (String sportid : sports) {
                if (sportid.equals("" + s.getLeague_id()) || sportid.equals(s.getSportname())
                        || sportid.equalsIgnoreCase("All Sports")) {
                    goodsport = true;
                    break;
                }
            }

            if (goodsport &&  ! SpankOdds.getMessagesFromLog) {
                if (popup) {
                    String hrmin = AppController.getCurrentHoursMinutes();
                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-" + g.getShorthometeam() + "-" + currenthomescore;

                    String mesg = newGameStatus.getGroupHeader() + " :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                    AppController.addAlert(hrmin, mesg);

                    new UrgentMessage("<HTML><H2>" + newGameStatus.getGroupHeader() + " " + s.getLeaguename() + "</H2>" +
                            "<TABLE cellspacing=1 cellpadding=1>" +

                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD><TD>" + currentvisitorscore + "</TR>" +
                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD><TD>" + currenthomescore + "</TR>" +
                            "</TABLE></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());
                }

                if (sound) {
                    if (audiofile.equals("")) {
                        new SoundPlayer(newGameStatus.getSoundFile());
                    } else {
                        //playSound(audiofile);
                        new SoundPlayer(audiofile);
                    }
                }


            }
        }
    }
}
