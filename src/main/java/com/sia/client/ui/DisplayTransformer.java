package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;

public class DisplayTransformer {

    private final int gameId;

    public DisplayTransformer(int gameId) {
        this.gameId = gameId;
    }
    public String transformDefault(String display) {
        if (SiaConst.DefaultViewName.equals(display)) {
            Sport sp = getSport();
            if (sp.getSport_id() == SportType.Baseball.getSportId()){
                display = "totalbothmoney";
            } else if (sp.getParentleague_id() == SportType.Soccer.getIdentityLeagueId()) {
                display = "totalmoney";
            } else if (sp.getMoneylinedefault()) {
                display = "justmoney";
            } else {
                display = "spreadtotal";
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
