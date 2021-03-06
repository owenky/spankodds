package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.sia.client.config.Utils.log;

public class Game implements KeyedObject,Cloneable {

    //TODO remove StatusSet
    private static final Set<String> StatusSet = new HashSet<>();
    private int league_id;
    int game_id;
    int visitorgamenumber;
    int homegamenumber;
    int visitoraltgamenumber;
    int homealtgamenumber;
    private LocalDate gameDate;
    private LocalTime gameTime;
    String visitorteam;
    String hometeam;
    String shortvisitorteam;
    String shorthometeam;
    int currentvisitorscore;
    int currenthomescore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return league_id == game.league_id && game_id == game.game_id && visitorgamenumber == game.visitorgamenumber && homegamenumber == game.homegamenumber && visitoraltgamenumber == game.visitoraltgamenumber && homealtgamenumber == game.homealtgamenumber && currentvisitorscore == game.currentvisitorscore && currenthomescore == game.currenthomescore && finalvisitorscore == game.finalvisitorscore && finalhomescore == game.finalhomescore && eventnumber == game.eventnumber && subleague_id == game.subleague_id && forprop == game.forprop && circled == game.circled && started == game.started && neutrallocation == game.neutrallocation && ingame == game.ingame && seriesprice == game.seriesprice && addedgame == game.addedgame && extragame == game.extragame && tba == game.tba && visitor_id == game.visitor_id && home_id == game.home_id && visitorlefthanded == game.visitorlefthanded && homelefthanded == game.homelefthanded && visitorscheduled == game.visitorscheduled && homescheduled == game.homescheduled && gamestatusts == game.gamestatusts && scorets == game.scorets && Objects.equals(gameDate, game.gameDate) && Objects.equals(gameTime, game.gameTime) && Objects.equals(visitorteam, game.visitorteam) && Objects.equals(hometeam, game.hometeam) && Objects.equals(shortvisitorteam, game.shortvisitorteam) && Objects.equals(shorthometeam, game.shorthometeam) && Objects.equals(injurynotes, game.injurynotes) && Objects.equals(refsumpires, game.refsumpires) && Objects.equals(lineups, game.lineups) && Objects.equals(weather, game.weather) && Objects.equals(location, game.location) && Objects.equals(status, game.status) && Objects.equals(period, game.period) && Objects.equals(specialnotes, game.specialnotes) && Objects.equals(description, game.description) && Objects.equals(visitornickname, game.visitornickname) && Objects.equals(homenickname, game.homenickname) && Objects.equals(visitorcity, game.visitorcity) && Objects.equals(homecity, game.homecity) && Objects.equals(visitorscoresupplemental, game.visitorscoresupplemental) && Objects.equals(homescoresupplemental, game.homescoresupplemental) && Objects.equals(visitorpitcher, game.visitorpitcher) && Objects.equals(homepitcher, game.homepitcher) && Objects.equals(timeremaining, game.timeremaining) && Objects.equals(tvstations, game.tvstations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game_id);
    }

    int finalvisitorscore;
    int finalhomescore;


    @Override
    public Game clone() {
        try {
            return (Game)super.clone();
        } catch (CloneNotSupportedException e) {
            log(e);
            return null;
        }
    }
    String injurynotes;
    String refsumpires;
    String lineups;
    String weather;
    String location;
    String status;
    String period;
    String specialnotes;

    int eventnumber;
    int subleague_id;
    String description;
    boolean forprop;
    boolean circled;
    boolean started;
    boolean neutrallocation;
    boolean ingame;
    boolean seriesprice;
    boolean addedgame;
    boolean extragame;
    boolean tba;
    String visitornickname;
    String homenickname;
    String visitorcity;
    String homecity;
    int visitor_id;
    int home_id;
    String visitorscoresupplemental;
    String homescoresupplemental;
    String visitorpitcher;
    String homepitcher;
    boolean visitorlefthanded;
    boolean homelefthanded;
    boolean visitorscheduled;
    boolean homescheduled;
    String timeremaining;

    String tvstations;
    long gamestatusts;
    long scorets;




    int currentvisitorscore2;
    int currenthomescore2;
    String visitorscoresupplemental2;
    String homescoresupplemental2;
    String status2;
    String timeremaining2;
    long gamestatusts2;
    long scorets2;

    public Game() {
        setStatus("");
        timeremaining = "";
    }


    public Game(int game_id,
                int visitorgamenumber,
                int homegamenumber,
                int visitoraltgamenumber,
                int homealtgamenumber,
                java.sql.Date gamedate,
                Long gametime,
                String visitorteam,
                String hometeam,
                String shortvisitorteam,
                String shorthometeam,
                int league_id,
                int currentvisitorscore,
                int currenthomescore,
                int finalvisitorscore,
                int finalhomescore,
                String injurynotes,
                String refsumpires,
                String lineups,
                String weather,
                String location,
                String status,
                String period,
                String specialnotes, int subleague_id, String description, boolean forprop, boolean circled, boolean started, boolean neutrallocation, boolean ingame,
                boolean seriesprice, boolean addedgame, boolean extragame, boolean tba, String visitornickname, String homenickname, String visitorcity, String homecity,
                int visitor_id, int home_id, String visitorscoresupplemental, String homescoresupplemental, String visitorpitcher, String homepitcher,
                String timeremaining, boolean visitorlefthanded, boolean homelefthanded, boolean visitorscheduled, boolean homescheduled,
                String tvstations, long gamestatusts, long scorets) {
        this.game_id = game_id;
        this.visitorgamenumber = visitorgamenumber;
        this.homegamenumber = homegamenumber;
        this.visitoraltgamenumber = visitoraltgamenumber;
        this.homealtgamenumber = homealtgamenumber;
        setGameDateTime(gamedate,gametime);
        this.visitorteam = visitorteam;
        this.hometeam = hometeam;
        this.shortvisitorteam = shortvisitorteam;
        this.shorthometeam = shorthometeam;
        this.currentvisitorscore = currentvisitorscore;
        this.currenthomescore = currenthomescore;
        this.finalvisitorscore = finalvisitorscore;
        this.finalhomescore = finalhomescore;
        this.injurynotes = injurynotes;
        this.refsumpires = refsumpires;
        this.lineups = lineups;
        this.weather = weather;
        this.location = location;
        setStatus(status);
        this.period = period;
        this.specialnotes = specialnotes;
        this.subleague_id = subleague_id;
        this.description = description;
        this.forprop = forprop;
        this.circled = circled;
        this.started = started;
        this.neutrallocation = neutrallocation;
        setIngame(ingame);
        this.seriesprice = seriesprice;
        this.addedgame = addedgame;
        this.extragame = extragame;
        this.tba = tba;
        this.visitornickname = visitornickname;
        this.homenickname = homenickname;
        this.visitorcity = visitorcity;
        this.homecity = homecity;
        this.visitor_id = visitor_id;
        this.home_id = home_id;
        this.visitorscoresupplemental = visitorscoresupplemental;
        this.homescoresupplemental = homescoresupplemental;
        this.visitorpitcher = visitorpitcher;
        this.homepitcher = homepitcher;
        this.timeremaining = timeremaining;
        this.visitorlefthanded = visitorlefthanded;
        this.homelefthanded = homelefthanded;
        this.visitorscheduled = visitorscheduled;
        this.homescheduled = homescheduled;
        this.tvstations = tvstations;
        this.gamestatusts = gamestatusts;
        this.scorets = scorets;
        this.league_id = league_id;
    }


    public void updateScore(String period, String timer, String status, long gamestatusts, int currentvisitorscore, String visitorscoresupplemental,
                            long scorets, int currenthomescore, String homescoresupplemental) {
        this.period = period;
        this.timeremaining = timer;
        setStatus(status);
        this.gamestatusts = gamestatusts;
        this.currentvisitorscore = currentvisitorscore;
        this.visitorscoresupplemental = visitorscoresupplemental;
        this.scorets = scorets;
        this.currenthomescore = currenthomescore;
        this.homescoresupplemental = homescoresupplemental;

    }
    public void updateScore2(String period, String timer, String status, long gamestatusts, int currentvisitorscore, String visitorscoresupplemental,
                            long scorets, int currenthomescore, String homescoresupplemental) {
        //this.period = period;
        this.timeremaining2 = timer;
        setStatus2(status);
        this.gamestatusts2 = gamestatusts;
        this.currentvisitorscore2 = currentvisitorscore;
        this.visitorscoresupplemental2 = visitorscoresupplemental;
        this.scorets2 = scorets;
        this.currenthomescore2 = currenthomescore;
        this.homescoresupplemental2 = homescoresupplemental;

    }
    public long getGamestatusts() {
        return gamestatusts;
    }

    public void setGamestatusts(long gamestatusts) {
        this.gamestatusts = gamestatusts;
    }

    public long getScorets() {
        return scorets;
    }

    public void setScorets(long scorets) {
        this.scorets = scorets;
    }

    public String getTvstations() {
        return tvstations;
    }

    public void setTvstations(String tvstations) {
        this.tvstations = tvstations;
    }

	@Override
    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getEventnumber() {
        return eventnumber;
    }

    public void setEventnumber(int eventnumber) {
        this.eventnumber = eventnumber;
    }

    public int getSubleague_id() {
        return subleague_id;
    }

    public void setSubleague_id(int subleague_id) {
        this.subleague_id = subleague_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isForprop() {
        return forprop;
    }

    public void setForprop(boolean forprop) {
        this.forprop = forprop;
    }

    public boolean isCircled() {
        return circled;
    }

    public void setCircled(boolean circled) {
        this.circled = circled;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isNeutrallocation() {
        return neutrallocation;
    }

    public void setNeutrallocation(boolean neutrallocation) {
        this.neutrallocation = neutrallocation;
    }

    public boolean isIngame() {
        return ingame;
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public boolean isSeriesprice() {
        return seriesprice;
    }

    public void setSeriesprice(boolean seriesprice) {
        this.seriesprice = seriesprice;
    }

    public boolean isAddedgame() {
        return addedgame;
    }

    public void setAddedgame(boolean addedgame) {
        this.addedgame = addedgame;
    }

    public boolean isExtragame() {
        return extragame;
    }

    public void setExtragame(boolean extragame) {
        this.extragame = extragame;
    }

    public boolean isTba() {
        return tba;
    }

    public void setTba(boolean tba) {
        this.tba = tba;
    }

    public String getVisitornickname() {
        return visitornickname;
    }

    public void setVisitornickname(String visitornickname) {
        this.visitornickname = visitornickname;
    }

    public String getHomenickname() {
        return homenickname;
    }

    public void setHomenickname(String homenickname) {
        this.homenickname = homenickname;
    }

    public String getVisitorcity() {
        return visitorcity;
    }

    public void setVisitorcity(String visitorcity) {
        this.visitorcity = visitorcity;
    }

    public String getHomecity() {
        return homecity;
    }

    public void setHomecity(String homecity) {
        this.homecity = homecity;
    }

    public int getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(int visitor_id) {
        this.visitor_id = visitor_id;
    }

    public int getHome_id() {
        return home_id;
    }

    public void setHome_id(int home_id) {
        this.home_id = home_id;
    }

    public String getVisitorscoresupplemental() {
        return visitorscoresupplemental;
    }

    public void setVisitorscoresupplemental(String visitorscoresupplemental) {
        this.visitorscoresupplemental = visitorscoresupplemental;
    }

    public String getHomescoresupplemental() {
        return homescoresupplemental;
    }

    public void setHomescoresupplemental(String homescoresupplemental) {
        this.homescoresupplemental = homescoresupplemental;
    }

    public String getVisitorpitcher() {
        return visitorpitcher;
    }

    public void setVisitorpitcher(String visitorpitcher) {
        this.visitorpitcher = visitorpitcher;
    }

    public String getHomepitcher() {
        return homepitcher;
    }

    public void setHomepitcher(String homepitcher) {
        this.homepitcher = homepitcher;
    }

    public boolean isVisitorlefthanded() {
        return visitorlefthanded;
    }

    public void setVisitorlefthanded(boolean visitorlefthanded) {
        this.visitorlefthanded = visitorlefthanded;
    }

    public boolean isHomelefthanded() {
        return homelefthanded;
    }

    public void setHomelefthanded(boolean homelefthanded) {
        this.homelefthanded = homelefthanded;
    }

    public boolean isVisitorscheduled() {
        return visitorscheduled;
    }

    public void setVisitorscheduled(boolean visitorscheduled) {
        this.visitorscheduled = visitorscheduled;
    }

    public boolean isHomescheduled() {
        return homescheduled;
    }

    public void setHomescheduled(boolean homescheduled) {
        this.homescheduled = homescheduled;
    }

    public String getTimeremaining() {
        return timeremaining;
    }

    public void setTimeremaining(String timeremaining) {
        this.timeremaining = timeremaining;
    }


    public int getVisitorgamenumber() {
        return visitorgamenumber;
    }

    public void setVisitorgamenumber(int visitorgamenumber) {
        this.visitorgamenumber = visitorgamenumber;
    }

    public int getHomegamenumber() {
        return homegamenumber;
    }

    public void setHomegamenumber(int homegamenumber) {
        this.homegamenumber = homegamenumber;
    }

    public int getVisitoraltgamenumber() {
        return visitoraltgamenumber;
    }

    public void setVisitoraltgamenumber(int visitoraltgamenumber) {
        this.visitoraltgamenumber = visitoraltgamenumber;
    }

    public int getHomealtgamenumber() {
        return homealtgamenumber;
    }

    public void setHomealtgamenumber(int homealtgamenumber) {
        this.homealtgamenumber = homealtgamenumber;
    }

    public int getCurrentvisitorscore() {
        return currentvisitorscore;
    }

    public void setCurrentvisitorscore(int currentvisitorscore) {
        this.currentvisitorscore = currentvisitorscore;
    }

    public int getCurrenthomescore() {
        return currenthomescore;
    }

    public void setCurrenthomescore(int currenthomescore) {
        this.currenthomescore = currenthomescore;
    }

    public int getFinalvisitorscore() {
        return finalvisitorscore;
    }

    public void setFinalvisitorscore(int finalvisitorscore) {
        this.finalvisitorscore = finalvisitorscore;
    }

    public int getFinalhomescore() {
        return finalhomescore;
    }

    public void setFinalhomescore(int finalhomescore) {
        this.finalhomescore = finalhomescore;
    }

    public String getGameString() {
        return getVisitorteam() + "@" + getHometeam();

    }

    public String getVisitorteam() {
        return visitorteam;
    }

    public void setVisitorteam(String visitorteam) {
        this.visitorteam = visitorteam;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getShortvisitorteam() {
        return shortvisitorteam;
    }

    public void setShortvisitorteam(String shortvisitorteam) {
        this.shortvisitorteam = shortvisitorteam;
    }

    public String getShorthometeam() {
        return shorthometeam;
    }

    public void setShorthometeam(String shorthometeam) {
        this.shorthometeam = shorthometeam;
    }


    public int getLeague_id() {
        return league_id;
    }
    public int getSportIdentifyingLeagueId() {
        int sportIdentifyingLeagueId =  getLeague_id();
        if (sportIdentifyingLeagueId == SiaConst.SoccerLeagueId) {
            sportIdentifyingLeagueId = getSubleague_id();
        }
        return sportIdentifyingLeagueId;
    }
    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }

    public String getInjurynotes() {
        return injurynotes;
    }

    public void setInjurynotes(String injurynotes) {
        this.injurynotes = injurynotes;
    }

    public String getRefsumpires() {
        return refsumpires;
    }

    public void setRefsumpires(String refsumpires) {
        this.refsumpires = refsumpires;
    }

    public String getLineups() {
        return lineups;
    }

    public void setLineups(String lineups) {
        this.lineups = lineups;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if ( null == status ) {
            status = "";
        }
        this.status = status;
        StatusSet.add(status);
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSpecialnotes() {
        return specialnotes;
    }

    public void setSpecialnotes(String specialnotes) {
        this.specialnotes = specialnotes;
    }

    public LocalDate getGamedate() {
        return gameDate;
    }

    public void setGameDateTime(java.sql.Date gamedate,Long time) {
        if (null == time) {
            if ( game_id != SiaConst.BlankGameId) {
                log("game time null for gameid=" + game_id);
            }
            time = 1000L;
        }

        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault());
        this.gameDate = zonedDateTime.toLocalDate();
        this.gameTime = zonedDateTime.toLocalTime();
    }

    public LocalTime getGametime() {
        return gameTime;
    }
    @Override
    public String getTeams() {
        return getVisitorteam()+"/"+this.getHometeam()+" ";
    }
    public boolean isInFinal() {
        String status = getStatus();
        return GameStatus.Final.isSame(status);
    }
    public boolean isHalfTime() {
        String status = getStatus();
        return  GameStatus.HalfTime.isSame(status);
    }
    public boolean isInProgress() {
        String status = getStatus();
        return  GameStatus.InProgress.isSame(status);
    }
    public boolean isInStage() {
        return isInFinal() || isHalfTime() || isInProgress() || isSeriesprice() ||  isIngame();
    }

    public int getCurrentvisitorscore2() {
        return currentvisitorscore2;
    }

    public void setCurrentvisitorscore2(int currentvisitorscore2) {
        this.currentvisitorscore2 = currentvisitorscore2;
    }

    public int getCurrenthomescore2() {
        return currenthomescore2;
    }

    public void setCurrenthomescore2(int currenthomescore2) {
        this.currenthomescore2 = currenthomescore2;
    }

    public String getVisitorscoresupplemental2() {
        return visitorscoresupplemental2;
    }

    public void setVisitorscoresupplemental2(String visitorscoresupplemental2) {
        this.visitorscoresupplemental2 = visitorscoresupplemental2;
    }

    public String getHomescoresupplemental2() {
        return homescoresupplemental2;
    }

    public void setHomescoresupplemental2(String homescoresupplemental2) {
        this.homescoresupplemental2 = homescoresupplemental2;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getTimeremaining2() {
        return timeremaining2;
    }

    public void setTimeremaining2(String timeremaining2) {
        this.timeremaining2 = timeremaining2;
    }

    public long getScorets2() {
        return scorets2;
    }

    public void setScorets2(long scorets2) {
        this.scorets2 = scorets2;
    }
    public long getGamestatusts2() {
        return gamestatusts2;
    }

    public void setGamestatusts2(long gamestatusts2) {
        this.gamestatusts2 = gamestatusts2;
    }




    public static void main(String [] argv) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        java.util.Date date = new java.util.Date(1651879800000L);
        System.out.println(date);
        Game g = new Game();
        g.setGameDateTime(new java.sql.Date(date.getTime()),date.getTime());
        LocalDateTime localDateTime = GameUtils.getGameDateTime(g, ZoneId.of(SiaConst.DefaultGameTimeZone));
        System.out.println(localDateTime);
        System.out.println(formatter.format(g.getGametime()));
    }






}