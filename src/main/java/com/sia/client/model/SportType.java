package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.control.MainScreen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * customized sport tab pane might also include the game, so return result is a list
     */
    public static List<SportType> findAllTypesByGame(Game game) {
        return instanceMap.values().stream().filter(st->st.isMyType(game)).collect(Collectors.toList());
    }
    public static SportType findPredefinedByGame(Game game) {
        Optional<SportType> stOpt = instanceMap.values().stream().filter(st->st.isMyType(game)).findFirst();
        return stOpt.orElse(null);
    }
    public static SportType createCustomizedSportType(String name) {
        return new SportType(-200,name,name,null,-1);
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

    private SportType(int sportId,String sportName,String abbr,String icon,int identityLeagueId) {
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
            //for customized sport type, use containsGameLeague to check -- 2021-10-26
            return containsGameLeague(game);
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
    private static final int PurgeOldGameCutOffTime = 10;
    public boolean isGameNear(Game game) {
        if (InitialGameMessages.getMessagesFromLog) {
            //if messages come from local, then all messages should be processed and added to table -- 2021-10-11
            return true;
        }

        if ( ! isPredifined()) {
            //for customized sport, always add to screen ( or always near game) -- 2021-10-25
            return true;
        }
        Date gd = game.getGamedate();
        LocalDate gmDate = LocalDate.of(gd.getYear()+1900, gd.getMonth()+1,gd.getDate());
        Calendar c = Calendar.getInstance();
        LocalDate today = LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
        if ( gmDate.isBefore(today) ) {
           LocalDateTime now = LocalDateTime.now();
           return PurgeOldGameCutOffTime > now.getHour();
        }

        LocalDate upperEndDate = today.plusDays(getComingDays());
        return ! gmDate.isAfter(upperEndDate);
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
    private boolean containsGameLeague(Game g ) {
        MainScreen ms = SpankyWindow.getSpankyWindow(0).getSportsTabPane().findMainScreen(this.getSportName());
        return null != ms.getDataModels().findLeagueSection(g);
    }
}
