package com.sia.client.config;

import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.ui.AppController;

import java.util.Calendar;
import java.util.Date;

public abstract class GameUtils {

    public static boolean isGameNear(int comingdays,Game game) {
        Date gmDate = game.getGamedate();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, comingdays);
        Date x = c.getTime();

        c.add(Calendar.DATE, -1*comingdays-1);
        Date y = c.getTime();
        return gmDate.before(x) && gmDate.after(y);
    }
    public static Sport getSport(Game game) {
        return AppController.getSport(game.getSportIdentifyingLeagueId());
    }
}
