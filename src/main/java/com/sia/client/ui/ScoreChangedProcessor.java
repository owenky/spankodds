package com.sia.client.ui;

import com.sia.client.config.GameUtils;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.Game;
import com.sia.client.model.GameStatus;
import com.sia.client.model.Sport;

import static com.sia.client.config.Utils.log;

public abstract class ScoreChangedProcessor {

    public static void process(GameStatus gameStatus, Game g,  int currentvisitorscore, int currenthomescore) {

        //owen 8/11 moved final as first block since grand salami was causing started and final to both execute
        if (! gameStatus.isSame(g.getStatus()) ) {
            Sport s = AppController.getSportByLeagueId(g.getLeague_id());
            log("game "+ GameUtils.getGameDebugInfo(g)+" is about to move from "+g.getStatus()+" to "+gameStatus.name());
            AppController.moveGameToThisHeader(g, gameStatus.getGroupHeader());

            String finalprefs = gameStatus.getAlertPrefSupplier().get();
            String[] arr  = finalprefs.split("\\|");
            boolean popup = false;
            boolean sound = false;
            int popupsecs = 15;
            int location = 6;
            String audiofile = "";
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
            try {
                audiofile = arr[4];
            } catch (Exception ex) {
                log(ex);
            }
            try {
                sports = arr[5].split(",");
                for (String sportid : sports) {
                    if (sportid.equals("" + s.getLeague_id()) || sportid.equals(s.getSportname())
                            || sportid.equalsIgnoreCase("All Sports")) {
                        goodsport = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                log(ex);
            }

            if (goodsport) {
                if (popup) {
                    String hrmin = AppController.getCurrentHoursMinutes();
                    String teaminfo = g.getVisitorgamenumber() + "-" + g.getShortvisitorteam() + "-" + currentvisitorscore + "@" + g.getHomegamenumber() + "-" + g.getShorthometeam() + "-" + currenthomescore;

                    String mesg = gameStatus.getGroupHeader() + " :" + s.getSportname() + "," + s.getLeaguename() + "," + teaminfo;
                    AppController.addAlert(hrmin, mesg);

                    new UrgentMessage("<HTML><H2>" + gameStatus.getGroupHeader() + " " + s.getLeaguename() + "</H2>" +
                            "<TABLE cellspacing=1 cellpadding=1>" +

                            "<TR><TD>" + g.getVisitorgamenumber() + "</TD><TD>" + g.getVisitorteam() + "</TD><TD>" + currentvisitorscore + "</TR>" +
                            "<TR><TD>" + g.getHomegamenumber() + "</TD><TD>" + g.getHometeam() + "</TD><TD>" + currenthomescore + "</TR>" +
                            "</TABLE></HTML>", popupsecs * 1000, location, AppController.getMainTabPane());
                }

                if (sound) {
                    if (audiofile.equals("")) {
                        new SoundPlayer(gameStatus.getSoundFile());
                    } else {
                        //playSound(audiofile);
                        new SoundPlayer(audiofile);
                    }
                }


            }
        }
    }
}
