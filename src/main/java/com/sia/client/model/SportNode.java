package com.sia.client.model;

import com.sia.client.config.SiaConst;

public class SportNode {

    private final Sport sport;
    public SportNode(Sport sport) {
        this.sport = sport;
    }
    @Override
    public String toString() {
        return sport.getLeaguename();
    }
    public int getLeague_id() {
        int sportIdentifyingLeagueId =  sport.getLeague_id();
        if (sportIdentifyingLeagueId == SiaConst.SoccerLeagueId) {
            sportIdentifyingLeagueId = sport.getSport_id();
        }
        return sportIdentifyingLeagueId;
    }
    public String getSportname() {
        return sport.getSportname();
    }
}