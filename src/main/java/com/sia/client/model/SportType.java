package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public static SportType Tennis = new SportType(5,"Tennis","TE","tennis.png",12);
    public static SportType Today = new SportType(-100,"Today","Today","today.png",-1);

    public static boolean isPredefinedSport(String sportName) {
        SportType st = findBySportName(sportName);
        if ( null == st) {
            return false;
        } else {
            return st.isPredifined();
        }
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
    private int comingDays;
    private LeagueFilter leagueFilter;
    private boolean showseries = true;
    private boolean showingame = true;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SportType sportType = (SportType) o;
        return Objects.equals(sportName, sportType.sportName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(sportName);
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
    public int getComingDays() {
        return this.comingDays;
    }
    public void setComingDays(int comingDays) {
        this.comingDays = comingDays;
    }
    public void setLeagueFilter(LeagueFilter leagueFilter) {
        this.leagueFilter = leagueFilter;
    }

    public boolean isShowseries() {
        return showseries;
    }

    public void setShowseries(final boolean showseries) {
        this.showseries = showseries;
    }
    public boolean isLeagueSelected(int leagueId) {
        return  null !=leagueFilter && leagueFilter.isSelected(leagueId);
    }
    public boolean isShowingame() {
        return showingame;
    }

    public void setShowingame(final boolean showingame) {
        this.showingame = showingame;
    }
    public boolean isMyType(Game game) {

        if ( sportId < 0 && identityLeagueId < 0 ) {
            //when both negative, it is customized main screen, allow all game to come in. games will not be added
            //to the screen when no group header contains this game.
            return true;
        }
        int identifyingLeagueId = game.getSportIdentifyingLeagueId();
        if ( identityLeagueId > 0) {
            //this condition is for sport id == 5 which is shared by many different sports
            return identityLeagueId == identifyingLeagueId || identityLeagueId == game.getLeague_id(); //fsecond condition is for soccer
        } else {
            Sport sp = AppController.getSportByLeagueId(identifyingLeagueId);
            if (null == sp){
                return false;
            } else {
                return sportId==sp.getSport_id();
            }
        }
    }
    public boolean isGameNear(Game game) {
        Date gmDate = game.getGamedate();
        LocalDate gmLocalDate = LocalDate.of(gmDate.getYear()+1900, gmDate.getMonth()+1,gmDate.getDate());
        Calendar c = Calendar.getInstance();
        LocalDate todayLocalDate = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
        if ( gmLocalDate.isBefore(todayLocalDate) ) {
            return false;
        }

        LocalDate upperEndLocalDate = todayLocalDate.plusDays(getComingDays());
        return ! gmLocalDate.isAfter(upperEndLocalDate);
    }
    public boolean isLeagueSelected(Game game) {
        if ( isFilteredByConfig(game)) {
            return false;
        }
        return  null == leagueFilter || leagueFilter.isSelected(game.getLeague_id());
    }
    public boolean shouldSelect(Game game) {
        return  isLeagueSelected(game) && isGameNear(game) && isMyType(game);
    }
    private boolean isFilteredByConfig(Game g) {
        if ( g.isSeriesprice() && ! isShowseries()) {
            return true;
        }

        return g.isInGame2() && ! isShowingame();
    }
    private static String normalizeName(String name) {
        return name.replaceAll("\\s","").toLowerCase();
    }
}
