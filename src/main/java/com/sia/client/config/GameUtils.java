package com.sia.client.config;

import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.ui.AppController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class GameUtils {

    private static final Set<String> soccerSpecialGroups;
    static {
        soccerSpecialGroups = new HashSet<> ();
        soccerSpecialGroups.add("Soccer Halftime");
        soccerSpecialGroups.add("Soccer Final");
        soccerSpecialGroups.add("Soccer In Progress");
    }
    public static boolean isGameNear(int comingdays,Game game) {
        Date gmDate = game.getGamedate();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, comingdays);
        Date x = c.getTime();

        c.add(Calendar.DATE, -1*comingdays-1);
        Date y = c.getTime();
        //why not filtering out yesterday's game ( i.e. gmDate.after(y)) originally
//        return gmDate.before(x) && gmDate.after(y);
        return gmDate.before(x) ;
    }
    public static Sport getSport(Game game) {
        return AppController.getSport(game.getSportIdentifyingLeagueId());
    }
}
