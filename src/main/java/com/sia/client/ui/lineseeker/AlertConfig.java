package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.model.SelectionItem;

import java.util.HashMap;
import java.util.Map;

public class AlertConfig {

    private final int gameId;
    private final AlertPeriod period;
    private Map<AlertSectionName, LineSeekerAttribute> sectionMap = new HashMap<>();

    public static final AlertConfig BlankAlert = new AlertConfig(SelectionItem.SELECT_BLANK_KEY,AlertPeriod.Full);

    public AlertConfig(@JsonProperty("gameId") int gameId, @JsonProperty("period") AlertPeriod period) {
        this.gameId = gameId;
        this.period = period;
    }
    @JsonProperty
    public int getGameId() {
        return gameId;
    }
    @JsonProperty
    public Map<AlertSectionName, LineSeekerAttribute> getSectionMap() {
        return sectionMap;
    }
    @JsonProperty
    public void setSectionMap(Map<AlertSectionName, LineSeekerAttribute> sectionMap) {
        this.sectionMap = sectionMap;
    }
    @JsonProperty
    public AlertPeriod getPeriod() {
        return period;
    }
    public LineSeekerAttribute getSectionAtrribute(AlertSectionName alertSectionName) {
        return sectionMap.computeIfAbsent(alertSectionName, (name)-> {
            LineSeekerAttribute sectionAtrribute = new LineSeekerAttribute();
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
