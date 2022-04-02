package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertConfig {

    private int gameId;
    private String period;
    private final Map<AlertSectionName,SectionFieldGroup> sectionMap = new HashMap<>();

    public AlertConfig() {
        List<AlertSectionName> alertSectionNames = AlertSectionName.getSortedSectionNames();
        for(AlertSectionName alertSectionName: alertSectionNames) {
            addToMap(new SectionFieldGroup(alertSectionName));
        }
    }
    public SectionFieldGroup getSectionFieldGroup(AlertSectionName sectionName) {
        return sectionMap.get(sectionName);
    }
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
    private void addToMap(SectionFieldGroup sectionGroup) {
        sectionMap.put(sectionGroup.getSectionName(),sectionGroup);
    }
}
