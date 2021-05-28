package com.sia.client.model;

public class Game implements KeyedObject {

    private int league_id;
    int game_id;
    int visitorgamenumber;
    int homegamenumber;
    int visitoraltgamenumber;
    int homealtgamenumber;
    java.sql.Date gamedate;
    java.sql.Time gametime;
    String visitorteam;
    String hometeam;
    String shortvisitorteam;
    String shorthometeam;
    int currentvisitorscore;
    int currenthomescore;
    int finalvisitorscore;
    int finalhomescore;
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
    java.sql.Timestamp gamestatusts;
    java.sql.Timestamp scorets;

    public Game() {
        status = "";
        timeremaining = "";
    }


    public Game(int game_id,
                int visitorgamenumber,
                int homegamenumber,
                int visitoraltgamenumber,
                int homealtgamenumber,
                java.sql.Date gamedate,
                java.sql.Time gametime,
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
                String tvstations, java.sql.Timestamp gamestatusts, java.sql.Timestamp scorets) {
        this.game_id = game_id;
        this.visitorgamenumber = visitorgamenumber;
        this.homegamenumber = homegamenumber;
        this.visitoraltgamenumber = visitoraltgamenumber;
        this.homealtgamenumber = homealtgamenumber;
        this.gamedate = gamedate;
        this.gametime = gametime;
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
        this.status = status;
        this.period = period;
        this.specialnotes = specialnotes;
        this.subleague_id = subleague_id;
        this.description = description;
        this.forprop = forprop;
        this.circled = circled;
        this.started = started;
        this.neutrallocation = neutrallocation;
        this.ingame = ingame;
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


    public void updateScore(String period, String timer, String status, java.sql.Timestamp gamestatusts, int currentvisitorscore, String visitorscoresupplemental,
                            java.sql.Timestamp scorets, int currenthomescore, String homescoresupplemental) {
        this.period = period;
        this.timeremaining = timer;
        this.status = status;
        this.gamestatusts = gamestatusts;
        this.currentvisitorscore = currentvisitorscore;
        this.visitorscoresupplemental = visitorscoresupplemental;
        this.scorets = scorets;
        this.currenthomescore = currenthomescore;
        this.homescoresupplemental = homescoresupplemental;

    }
    public java.sql.Timestamp getGamestatusts() {
        return gamestatusts;
    }

    public void setGamestatusts(java.sql.Timestamp gamestatusts) {
        this.gamestatusts = gamestatusts;
    }

    public java.sql.Timestamp getScorets() {
        return scorets;
    }

    public void setScorets(java.sql.Timestamp scorets) {
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
        this.status = status;
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

    public java.sql.Date getGamedate() {
        if (gamedate == null) {
            System.out.println("gamedate null for gameid=" + game_id);
            gamedate = new java.sql.Date(1000);
        }
        return gamedate;
    }

    public void setGamedate(java.sql.Date gamedate) {
        this.gamedate = gamedate;
    }

    public java.sql.Time getGametime() {
        if (gametime == null) {
            System.out.println("gametime null for gameid=" + game_id);
            gametime = new java.sql.Time(1000);
        }
        return gametime;
    }

    public void setGametime(java.sql.Time gametime) {
        this.gametime = gametime;
    }


}