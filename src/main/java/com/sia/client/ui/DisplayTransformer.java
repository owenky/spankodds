package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.model.UserDisplaySettings;

public class DisplayTransformer {

    private final int gameId;

    public DisplayTransformer(int gameId) {
        this.gameId = gameId;
    }
    public String transformDefault(String display) {
        if (SiaConst.DefaultViewName.equals(display))
        {
            Sport sp = getSport();
            /*
            if (sp.getSport_id() == SportType.Baseball.getSportId()){
                display = "totalbothmoney";
            } else if (sp.getParentleague_id() == SportType.Soccer.getIdentityLeagueId()) {
                display = "totalmoney";
            } else if (sp.getMoneylinedefault()) {
                display = "justmoney";
            } else {
                display = "spreadtotal";
            }
            */

            if (sp.getSportname().equals(SiaConst.SportName.Football))
            {
                display = UserDisplaySettings.getFootballdefault();

            }
            else if (sp.getSportname().equals(SiaConst.SportName.Basketball))
            {
                display = UserDisplaySettings.getBasketballdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Baseball))
            {
                display = UserDisplaySettings.getBaseballdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Hockey))
            {
                display = UserDisplaySettings.getHockeydefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Fighting))
            {
                display = UserDisplaySettings.getFightingdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Soccer))
            {
                display = UserDisplaySettings.getSoccerdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Auto_Racing))
            {
                display = UserDisplaySettings.getAutoracingdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Golf))
            {
                display = UserDisplaySettings.getGolfdefault();
            }
            else if (sp.getSportname().equals(SiaConst.SportName.Tennis))
            {
                display = UserDisplaySettings.getTennisdefault();
            }
            else
            {
                System.out.println("NEVER ENTER");
                display="spreadtotal";
            }


                // want to force display on certain prop types
            Game g = getGame();
            String team = g.getHometeam();
            if(team.indexOf("Score") != -1)
            {
                display = "justmoney";
            }
            else if(team.indexOf("Points") != -1
                    || team.indexOf("Rebounds") != -1
                    || team.indexOf("Assists") != -1
                    || team.indexOf("Assists") != -1
                    || team.indexOf("Pts + Reb") != -1
                    || team.indexOf("Reb + Ast") != -1
                    || team.indexOf("Pts + Ast") != -1
                    || team.indexOf("3 Pt FG") != -1
                    || team.indexOf("Run+Hit+Err") != -1
                       )
            {
                display = "justtotal";
            }

        }
        return display;
    }
    public int transformPeriod(int period) {
        if (getGame().getStatus().equalsIgnoreCase("Time") && period == 0) {
            period = 2;
        }
        return period;
    }
    public Game getGame() {
        return AppController.getGame(gameId);
    }

    public Sport getSport() {
        return AppController.getSportByLeagueId(getGame().getLeague_id());
    }
}
