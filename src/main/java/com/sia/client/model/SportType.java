package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.util.HashMap;
import java.util.Map;

public class SportType {
    private static final Map<String,SportType> instanceMap = new HashMap<>();
    public static SportType Football = new SportType(1,"Football","FB","football.png",-1);
    public static SportType Basketball = new SportType(2,"Basketball","BK","basketball.png",-1);
    public static SportType Baseball = new SportType(3,"Baseball","BB","baseball.png",-1);
    public static SportType Hockey = new SportType(4,"Hockey","HK","hockey.png",-1);
    public static SportType Fighting = new SportType(5,"Fighting","FI","boxing.png",10);
    public static SportType Soccer = new SportType(5,SiaConst.SoccerStr,"OT","soccer.png",9);
    public static SportType AutoRacing = new SportType(5,"Auto Racing","AU","flag.png",14);
    public static SportType Golf = new SportType(5,"Golf","GO","golf.png",11);
    //TODO what is abbr for tennis
    public static SportType Tennis = new SportType(5,"Tennis","??","tennis.png",12);
    public static SportType Today = new SportType(-100,"Today","Today","today.png",-1);

    public static boolean isPredefinedSport(String sportName) {
        SportType st = findBySportName(sportName);
        if ( null == st) {
            return false;
        } else {
            return st.isPredifined();
        }
    }
    public static SportType getSportType(Game game) {
        SportType rtn = null;
        for ( SportType st:instanceMap.values()) {
            if ( st.isMyType(game)) {
                rtn = st;
                break;
            }
        }
        return rtn;
    }
    public static SportType findBySportName(String sportName) {
        return instanceMap.get(normalizeName(sportName));
    }
    private final String sportName;
    private final int sportId;
    private final String icon;
    private final String abbr;
    //if identityLeagueId > 0, SportType is identified by leagueId, not by sportId ( when sportId >=5, SportType is identified by leagueId)
    private final int identityLeagueId;

    public SportType(int sportId,String sportName,String abbr,String icon,int identityLeagueId) {
        this.sportName = sportName;
        this.sportId = sportId;
        this.icon = icon;
        this.abbr = abbr;
        this.identityLeagueId = identityLeagueId;
        instanceMap.put(normalizeName(sportName),this);
    }

    public String getSportName() {
        return sportName;
    }

    public int getSportId() {
        return sportId;
    }

    public String getIcon() {
        return icon;
    }
    public String getAbbr() {
        return abbr;
    }
    public boolean isPredifined() {
        return sportId > 0;
    }
    public boolean isMyType(Game game) {

        if ( sportId < 0 && identityLeagueId < 0 ) {
            //when both negative, it is customized main screen, allow all game to come in. games will not be added
            //to the screen when no group header contains this game.
            return true;
        }
        int identifyingLeagueId = game.getSportIdentifyingLeagueId();
        if ( identityLeagueId > 0) {
            return identityLeagueId == identifyingLeagueId;
        } else {
            Sport sp = AppController.getSport(identifyingLeagueId);
            if (null == sp){
                return false;
            } else {
                return sportId==sp.getSport_id();
            }
        }
    }
    private static String normalizeName(String name) {
        return name.replaceAll("\\s","").toLowerCase();
    }
}
