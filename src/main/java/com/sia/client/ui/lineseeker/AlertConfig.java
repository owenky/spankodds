package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertConfig {

    private final AlertAttributes alertAttributes;
    private final Map<AlertSectionName,SectionFieldGroup> sectionMap = new HashMap<>();

    public AlertConfig(AlertAttributes alertAttributes) {
        this.alertAttributes = alertAttributes;
        List<AlertSectionName> alertSectionNames = AlertSectionName.getSortedSectionNames();
        for(AlertSectionName alertSectionName: alertSectionNames) {
            addToMap(new SectionFieldGroup(alertSectionName));
        }
    }
    public SectionFieldGroup getSectionFieldGroup(AlertSectionName sectionName) {
        return sectionMap.get(sectionName);
    }
    public int getGameId() {
        return alertAttributes.getGameId();
    }

    public void setGameId(int gameId) {
        alertAttributes.setGameId(gameId);
    }

    public String getPeriod() {
        return alertAttributes.getPeriod();
    }

    public void setPeriod(String period) {
        alertAttributes.setPeriod(period);
    }
    private void addToMap(SectionFieldGroup sectionGroup) {
        sectionMap.put(sectionGroup.getSectionName(),sectionGroup);
    }
}
