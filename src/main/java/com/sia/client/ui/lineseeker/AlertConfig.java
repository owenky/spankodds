package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.Map;

public class AlertConfig {

    public static final String spreadName = "Spreads";
    public static final String totalsName = "Totals";
    public static final String mLineName = "Money Lines";
    public static final String awayName = "Away TT";
    public static final String homeTTName = "Home TT";

    private int gameId;
    private String period;
    private final Map<String,SectionFieldGroup> sectionMap = new HashMap<>();

    public AlertConfig() {
        addToMap(new SectionFieldGroup(spreadName,"",""));
        addToMap(new SectionFieldGroup(totalsName,"Over","Under"));
        addToMap(new SectionFieldGroup(mLineName,"","").withShowLineInput(false));
        addToMap(new SectionFieldGroup(awayName,"Over","Under"));
        addToMap(new SectionFieldGroup(homeTTName,"Over","Under"));
    }
    public SectionFieldGroup getSectionFieldGroup(String sectionName) {
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
