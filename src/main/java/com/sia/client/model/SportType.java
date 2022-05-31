package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst.SportName;
import com.sia.client.ui.AppController;
import com.sia.client.ui.SpankOdds;
import com.sia.client.ui.SportConfigurator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;
import static java.lang.Boolean.parseBoolean;

public class SportType {
    private static final Map<String,SportType> instanceMap = new HashMap<>();
    public static SportType Football = new SportType(1, SportName.Football,"FB","football.png",-1,null);
    public static SportType Basketball = new SportType(2,SportName.Basketball,"BK","basketball.png",-1,null);
    public static SportType Baseball = new SportType(3,SportName.Baseball,"BB","baseball.png",-1,null);
    public static SportType Hockey = new SportType(4,SportName.Hockey,"HK","hockey.png",-1,null);
    public static SportType Fighting = new SportType(5,SportName.Fighting,"FI","boxing.png",10,null);
    public static SportType Soccer = new SportType(5,SportName.Soccer,"OT","soccer.png",9,null);
    public static SportType AutoRacing = new SportType(5,SportName.Auto_Racing,"AU","flag.png",14,null);
    public static SportType Golf = new SportType(5,SportName.Golf,"GO","golf.png",11,null);
    public static SportType Tennis = new SportType(5,SportName.Tennis,"TE","tennis.png",12,null);

    public static SportType Today = new SportType(-100,"Today","Today","today.png",-1,getTodayMyTypeSelector());
    //array is in the same order as the sport's tab position in SportTabPane -- 2012-12-31
    private static final SportType [] PreDefinedSports = {SportType.Football,SportType.Basketball,SportType.Baseball,SportType.Hockey,SportType.Fighting,SportType.Soccer,SportType.AutoRacing,SportType.Golf,SportType.Tennis};

    public static SportType [] getPreDefinedSports() {
        return PreDefinedSports.clone();
    }
    public static int getPreDefinedSportTabIndex(SportType st) {
        int index = -1;
        for(int i=0;i<PreDefinedSports.length;i++) {
            if ( PreDefinedSports[i].equals(st)) {
                index = i;
                break;
            }
        }
        return index;
    }
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
        Optional<SportType> stOpt = instanceMap.values().stream().filter(st->st.isMyType(game)).filter(SportType::isPredifined).findFirst();
        return stOpt.orElse(null);
    }
    public static SportType createCustomizedSportType(String name,List<String> customizedHeaders,boolean showheaders, boolean showseries, boolean showingame, boolean showadded, boolean showextra, boolean showprops) {
        SportType rtn = new SportType(-200,name,name,null,-1,getCustomizedHeaderMyTypeSelector(customizedHeaders));
        rtn.setShowheaders(showheaders);
        rtn.setShowseries(showseries);
        rtn.setShowingame(showingame);
        rtn.setShowAdded(showadded);
        rtn.setShowExtra(showextra);
        rtn.setShowProps(showprops);
        rtn.customheaders = customizedHeaders;
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
    private int comingDays;
    private LeagueFilter leagueFilter;
    private boolean showseries = true;
    private boolean showingame = true;
    private boolean showAdded = true;
    private boolean showExtra = true;
    private boolean showProps = true;
    private boolean showHeaders = true;
    private boolean isTimeSort;
    private Function<Game,Boolean> myTypeSelector;
    private List<String> customheaders = new ArrayList<>();

    private SportType(int sportId,String sportName,String abbr,String icon,int identityLeagueId,Function<Game,Boolean> myTypeSelector) {
        this.sportName = sportName;
        this.sportId = sportId;
        this.icon = icon;
        this.abbr = abbr;
        this.identityLeagueId = identityLeagueId;
        this.myTypeSelector = null == myTypeSelector?getDefaultMyTypeSelector():myTypeSelector;
        instanceMap.put(normalizeName(sportName),this);
        enrichSportType();
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
    public void setCustomheaders(List<String> customheaders) {
        this.customheaders = customheaders;
        setMyTypeSelector(this.customheaders);
    }
    private void setMyTypeSelector(Collection<String> customizedHeaders) {
        Function<Game,Boolean> selector = getCustomizedHeaderMyTypeSelector(customizedHeaders);
        this.myTypeSelector = null==selector?getDefaultMyTypeSelector():selector;
    }
    public List<String> getCustomheaders() {
        return this.customheaders;
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
    public int getComingDays() {
        return this.comingDays;
    }
    public void setComingDays(int comingDays) {
        this.comingDays = SportConfigurator.getEffectiveMaxShowDays(comingDays);
    }
    public void setLeagueFilter(LeagueFilter leagueFilter) {
        this.leagueFilter = leagueFilter;
    }

    public boolean isShowseries() {
        return showseries;
    }
    public boolean isShowHeaders() {
        return showHeaders;
    }
    public void setShowheaders(final boolean showHeader) {
        this.showHeaders = showHeader;
    }
    public void setShowseries(final boolean showseries) {
        this.showseries = showseries;
    }
    public boolean isShowAdded() {
        return showAdded;
    }

    public void setShowAdded(final boolean showAdded) {
        this.showAdded = showAdded;
    }

    public boolean isShowExtra() {
        return showExtra;
    }

    public void setShowExtra(final boolean showExtra) {
        this.showExtra = showExtra;
    }

    public boolean isShowProps() {
        return showProps;
    }

    public void setShowProps(final boolean showProps) {
        this.showProps = showProps;
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
    public boolean isTimeSort() {
        return isTimeSort;
    }

    public void setTimeSort(final boolean timeSort) {
        isTimeSort = timeSort;
    }
    public boolean isMyType(Game game) {
        return myTypeSelector.apply(game);
    }
    private static final int PurgeOldGameCutOffTime = 10;
    public boolean isGameNear(Game game) {
        if (SpankOdds.getMessagesFromLog) {
            //if messages come from local, then all messages should be processed and added to table -- 2021-10-11
            return true;
        }

        if ( ! isPredifined()) {
            //for customized sport, always add to screen ( or always near game) -- 2021-10-25
            return true;
        }
        LocalDate gmDate = game.getGamedate();
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
        return  null == leagueFilter || leagueFilter.isSelected(game.getSportIdentifyingLeagueId());
    }
    public boolean shouldSelect(Game game) {
       String err = GameUtils.checkError(game);
       if ( null != err) {
          if(!err.equals("")) {
              log(err);
          }
           return false;
       }
        return  isLeagueSelected(game) && isGameNear(game) && isMyType(game);
    }
    @Override
    public String toString() {
        return sportName+"/"+sportId+":"+identityLeagueId;
    }
    public String getPrefName() {
        return getSportName().toLowerCase().replaceAll("\\s+","")+"pref";
    }
    public String getPerference() {
        return AppController.getUser().getSportPreference(this.getSportName());
    }
    public void setPerference(String pref) {
        AppController.getUser().setSportPreference(this.getSportName(),pref);
    }
    public void enrichSportType() {
        String userPrefStr = getPerference();
        if ( null != userPrefStr) { // predefined sport type
            String[] prefs = userPrefStr.split("\\|");
            if ( prefs.length < 2) {
                return;
            }
            String[] tmp = {};
            boolean all = false;
            setComingDays(Integer.parseInt(prefs[1]));
            if (parseBoolean(prefs[0])) {
                setTimeSort(true);
            }

            try {
                if (prefs.length > 2) {
                    tmp = prefs[2].split(",");
                    if (tmp[0].equalsIgnoreCase(getSportName())) {
                        all = true;
                    }
                    setShowProperties(prefs);
                }

            } catch (Exception ex) {
                log(ex);
            }
            setLeagueFilter(new LeagueFilter(tmp, all));
        } else { // customized sport type
            setComingDays(-1);
            setLeagueFilter(null);
        }
    }
    private void setShowProperties(String[] prefs) {
        setShowheaders(parseBoolean(prefs[3]));
        setShowseries(parseBoolean(prefs[4]));
        setShowingame(parseBoolean(prefs[5]));
        setShowAdded(parseBoolean(prefs[6]));
        setShowExtra(parseBoolean(prefs[7]));
        setShowProps(parseBoolean(prefs[8]));
    }
    private boolean isFilteredByConfig(Game g) {
//        if (!  isPredifined()) {
//            SportType preDefinedSt = SportType.findPredefinedByGame(g);
//            return preDefinedSt.isFilteredByConfig(g);
//        }
        //predefined sport type filter
        if ( seriesPriceConfig(g) || addedConfig(g) || extraConfig(g) || forPropConfig(g)) {
            return true;
        }

        return g.isIngame() && ! isShowingame();
    }
    private boolean seriesPriceConfig(Game g) {
        return g.isSeriesprice() && ! isShowseries();
    }
    private boolean addedConfig(Game g) {
        return g.isAddedgame() && !isShowAdded();
    }
    private boolean extraConfig(Game g) {
        return g.isExtragame() && !isShowExtra();
    }
    private boolean forPropConfig(Game g) {
        return g.isForprop() && !isShowProps();
    }
    private static String normalizeName(String name) {
        return name.replaceAll("\\s","").toLowerCase();
    }
    private Function<Game,Boolean> getDefaultMyTypeSelector() {
        return (game)-> {
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
        };
    }
    private static Function<Game,Boolean> getTodayMyTypeSelector() {
        return (game)-> {
            if ( null == game.getGamedate()) {
                return false;
            }
            LocalDate today = LocalDate.now();
            LocalDate gameDate = game.getGamedate();
            return gameDate.compareTo(today) <= 0;
        };
    }
    private static Function<Game,Boolean> getCustomizedHeaderMyTypeSelector(Collection<String> customHeaders) {
        if ( null == customHeaders || 0 == customHeaders.size()) {
            return null;
        }
        Set<String> headerSet = new HashSet<>(customHeaders);
        return (game)-> {
            GameGroupHeader gameGroupHeader = GameUtils.createGameGroupHeader(game);
            if ( null == gameGroupHeader) {
                return false;
            }
            return headerSet.contains(gameGroupHeader.getGameGroupHeaderStr());
        };
    }
}
