package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.model.SelectionItem;

import java.util.HashMap;
import java.util.Map;

public class AlertConfig {

    private final int gameId;
    private final AlertPeriod period;
    private Map<AlertSectionName, SectionAttribute> sectionMap = new HashMap<>();
    public static final AlertConfig BlankAlert = new AlertConfig(SelectionItem.SELECT_BLANK_KEY,AlertPeriod.Full);

    public AlertConfig(@JsonProperty("gameId") int gameId, @JsonProperty("period") AlertPeriod period) {
        this.gameId = gameId;
        this.period = period;
    }
    public int getGameId() {
        return gameId;
    }
    public Map<AlertSectionName, SectionAttribute> getSectionMap() {
        return sectionMap;
    }
    public void setSectionMap(Map<AlertSectionName, SectionAttribute> sectionMap) {
        this.sectionMap = sectionMap;
    }
    public AlertPeriod getPeriod() {
        return period;
    }

    public SectionAttribute getSectionAtrribute(AlertSectionName alertSectionName) {
        return sectionMap.computeIfAbsent(alertSectionName, (name)-> {
            SectionAttribute sectionAtrribute = new SectionAttribute();
            sectionAtrribute.setSectionName(alertSectionName);
            return sectionAtrribute;
        });
    }
    @JsonIgnore
    public String getKey() {
        return AlertAttrManager.makeKey(String.valueOf(gameId),period);
    }
    public void cloneAttributes(AlertConfig source) {
        sectionMap.clear();
        sectionMap.putAll(source.sectionMap);
    }
}
