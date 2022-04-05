package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.Map;

public class AlertAttributes {

    private int gameId;
    private String period;
    private Map<AlertSectionName, SectionAttribute> sectionMap = new HashMap<>();

    public int getGameId() {
        return gameId;
    }

    public Map<AlertSectionName, SectionAttribute> getSectionMap() {
        return sectionMap;
    }

    public void setSectionMap(Map<AlertSectionName, SectionAttribute> sectionMap) {
        this.sectionMap = sectionMap;
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
    public SectionAttribute getSectionAtrribute(AlertSectionName alertSectionName) {
        return sectionMap.computeIfAbsent(alertSectionName, (name)-> {
            SectionAttribute sectionAtrribute = new SectionAttribute();
            sectionAtrribute.setSectionName(alertSectionName);
            return sectionAtrribute;
        });
    }
}
