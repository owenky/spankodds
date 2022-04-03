package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.Map;

public class AlertAttributes {

    private int gameId;
    private String period;
    private final Map<AlertSectionName,SectionAtrribute> sectionMap = new HashMap<>();

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}
