package com.sia.client.config;

import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class GameUtils {

    private static final Set<String> soccerSpecialGroups;
    static {
        soccerSpecialGroups = new HashSet<> ();
        soccerSpecialGroups.add("Soccer Halftime");
        soccerSpecialGroups.add("Soccer Final");
        soccerSpecialGroups.add("Soccer In Progress");
    }
    public static Sport getSport(Game game) {
        return AppController.getSportByLeagueId(game.getSportIdentifyingLeagueId());
    }
    public static List<String> convertLeagueIdHeaderToGameGroupHeaderStr(List<String> headerStrList) {

        List<String> result = new ArrayList<>();
        for (String headerStr : headerStrList) {
            String[] headerProp = headerStr.split(" +");
            int leagueId = Integer.parseInt(headerProp[0]);
            Sport sport = AppController.getSportByLeagueId(leagueId);
            if (null != sport) {
                result.add(GameGroupHeader.constructGameGroupHeaderString(sport.getNormalizedLeaguename(),headerProp[1]));
            }
        }
        return result;
    }
    public static String normalizeGameHeader(String gameGroupHeader) {
        if ( null != gameGroupHeader) {
            gameGroupHeader = gameGroupHeader.replace(SiaConst.SoccerStr,"").trim();
            gameGroupHeader = Utils.replaceIgnoreCase(gameGroupHeader,SiaConst.FinalStr);
            gameGroupHeader = Utils.replaceIgnoreCase(gameGroupHeader,SiaConst.InProgresStr);
            gameGroupHeader = gameGroupHeader.replaceAll("\\s+"," ");
            return gameGroupHeader;
        } else {
            return null;
        }
    }
    public static GameGroupHeader createGameGroupHeader(Game game) {
        int sportIdentifyingLeagueId = game.getSportIdentifyingLeagueId();
        Sport sport = AppController.getSportByLeagueId(sportIdentifyingLeagueId);
        if ( null == sport) {
            return null;
        }
        return createGameGroupHeader(sport.getNormalizedLeaguename(), game.getSubleague_id(),game.getLeague_id(),game.getGamedate());
    }
    public static GameGroupHeader createGameGroupHeader(String leagueName, int subLeagueId,int leagueId,Date gameDate) {

        LocalDateTime ldt = new Date(gameDate.getTime()).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        return GameGroupHeader.create(leagueName,ldt,subLeagueId,leagueId);
    }
    public static boolean isGameNear(Game game) {
        SportType sportType = SportType.findPredefinedByGame(game);
        if ( null != sportType ) {
            return sportType.isGameNear(game);
        } else {
            return false;
        }
    }
    public static String checkError(Game game) {
        if ( ! AppController.existLeagueId(game.getSportIdentifyingLeagueId()) ) {
            return "League id:"+game.getLeague_id()+", identifyingLeagueId:"+game.getSportIdentifyingLeagueId()+" does not exist...";
        } else {
            return null;
        }
    }
    public static Game parseGameText(String text) {
        String[] array = text.split(SiaConst.MessageDelimiter);
        //log("gametext="+text);
        // here in 1st entry i made game_id visitorgamenumber
        Game g = new Game(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]),
                Integer.parseInt(array[4]), new java.sql.Date(Long.parseLong(array[5])), new java.sql.Time(Long.parseLong(array[6])),
                array[7], array[8], array[9], array[10], Integer.parseInt(array[11]), Integer.parseInt(array[12]), Integer.parseInt(array[13]),
                Integer.parseInt(array[14]), Integer.parseInt(array[15]), array[16], array[17], array[18], array[19], array[20], array[21],
                array[22], array[23], Integer.parseInt(array[24]), array[25],
                Boolean.parseBoolean(array[26]),
                Boolean.parseBoolean(array[27]),
                Boolean.parseBoolean(array[28]),
                Boolean.parseBoolean(array[29]),
                Boolean.parseBoolean(array[30]),
                Boolean.parseBoolean(array[31]),
                Boolean.parseBoolean(array[32]),
                Boolean.parseBoolean(array[33]),
                Boolean.parseBoolean(array[34]),
                array[35],
                array[36],
                array[37],
                array[38],
                Integer.parseInt(array[39]),
                Integer.parseInt(array[40]),
                array[41],
                array[42],
                array[43],
                array[44],
                array[45],
                Boolean.parseBoolean(array[46]),
                Boolean.parseBoolean(array[47]),
                Boolean.parseBoolean(array[48]),
                Boolean.parseBoolean(array[49]),
                array[50],
                Long.parseLong(array[51]),
                Long.parseLong(array[52]));

        GameUtils.validateGame(g);
        return g;
    }
    public static String getGameDebugInfo(Game game) {
        Sport sport = AppController.getSportByLeagueId(game.getSportIdentifyingLeagueId());
        String sportName = null == sport?"Unknown sport ":sport.getSportname();
        return  "DEBUGING Game, sport=" + sportName
                + ", header=" + createGameGroupHeader(game)+", teams="+game.getVisitorteam()+"/"+game.getHometeam()
                +", gameid=" + game.getGame_id() + ", leagueId=" + game.getLeague_id() + ", identifyingLeagueId="+game.getSportIdentifyingLeagueId()
                + ", status=" + game.getStatus() + ", isSeriecPrice=" + game.isSeriesprice() + ", isInGame2=" + game.isInGame2();
    }
    public static void validateGame(Game g) {
        if ( "WIN".equalsIgnoreCase(g.getTimeremaining())){
            g.setStatus(SiaConst.FinalStr);
        }
    }
    private static final int GameBasicFieldCnt = 29;
    public static void setGameProperty(Game g,String data) {
        String[] items = data.split("~");

        for(int x=0;x<GameBasicFieldCnt;) {
            String eventnumber = items[x++];
            String visitorgamenumber = items[x++];
            String homegamenumber = items[x++];
            String gamedatelong = items[x++];
            String gametimelong = items[x++];
            String visitorteamname = items[x++];
            String hometeamname = items[x++];
            String visitorabbr = items[x++];
            String homeabbr = items[x++];
            String league_id = items[x++];
            String visitscore = items[x++];
            String homescore = items[x++];
            String eventnumberalso = items[x++];
            String subleague_id = items[x++];
            String addedgamebool = items[x++];
            String extragamebool = items[x++];
            String tba = items[x++];
            String visitornickname = items[x++];
            String homenickname = items[x++];
            String visitorcity = items[x++];
            String homecity = items[x++];
            String visitorteamid = items[x++];
            String hometeamid = items[x++];
            String visitorpitcher = items[x++];
            String homepitcher = items[x++];
            String visitorlefthandedbool = items[x++];
            String homelefthandedbool = items[x++];
            String status = items[x++];
            String timeremaining = items[x++];

            int gameid = Integer.parseInt(eventnumber);
            g.setGame_id(gameid);
            g.setVisitorgamenumber(Integer.parseInt(visitorgamenumber));
            g.setHomegamenumber(Integer.parseInt(homegamenumber));
            g.setGamedate(new java.sql.Date(Long.parseLong(gamedatelong)));
            g.setGametime(new java.sql.Time(Long.parseLong(gametimelong)));
            g.setVisitorteam(visitorteamname);
            g.setHometeam(hometeamname);
            g.setShortvisitorteam(visitorabbr);
            g.setShorthometeam(homeabbr);
            g.setLeague_id(Integer.parseInt(league_id));

            // owen reput these 2 scores in
            g.setCurrentvisitorscore(Integer.parseInt(visitscore));
            g.setCurrenthomescore(Integer.parseInt(homescore));

            g.setSubleague_id(Integer.parseInt(subleague_id));
            g.setAddedgame(Boolean.parseBoolean(addedgamebool));
            g.setExtragame(Boolean.parseBoolean(extragamebool));
            g.setTba(Boolean.parseBoolean(tba));
            g.setVisitornickname(visitornickname);
            g.setHomenickname(homenickname);
            g.setVisitorcity(visitorcity);
            g.setHomecity(homecity);
            g.setVisitor_id(Integer.parseInt(visitorteamid));
            g.setHome_id(Integer.parseInt(hometeamid));
            g.setVisitorpitcher(visitorpitcher);
            g.setHomepitcher(homepitcher);
            g.setVisitorlefthanded(Boolean.parseBoolean(visitorlefthandedbool));
            g.setHomelefthanded(Boolean.parseBoolean(homelefthandedbool));

            g.setStatus(status);
            g.setTimeremaining(timeremaining);


        }
        //for optional fields -- 2021-11-17
        for(int x=GameBasicFieldCnt;x<items.length;){

            String injurynotes = items[x++];
            String refsumpires = items[x++];
            String lineups = items[x++];
            String weather = items[x++];
            String specialnotes = items[x++];
            String forprop = items[x++];
            String circled = items[x++];
            String started = items[x++];
            String neutrallocation = items[x++];
            String ingame = items[x++];
            String seriesprice = items[x++];
            String gamestatusts = items[x++];
            String scorets = items[x++];


            g.setInjurynotes(injurynotes);
            g.setRefsumpires(refsumpires);
            g.setLineups(lineups);
            g.setWeather(weather);
            g.setSpecialnotes(specialnotes);
            g.setForprop(Boolean.parseBoolean(forprop));
            g.setCircled(Boolean.parseBoolean(circled));
            g.setStarted(Boolean.parseBoolean(started));
            g.setNeutrallocation(Boolean.parseBoolean(neutrallocation));
            g.setIngame(Boolean.parseBoolean(ingame));
            g.setSeriesprice(Boolean.parseBoolean(seriesprice));

            g.setGamestatusts(Long.parseLong(gamestatusts));
            g.setScorets(Long.parseLong(scorets));
        }
        validateGame(g);
    }
    public static boolean isTimeSort(Boolean windowConfigTimeSort, boolean userDefinedTimesort) {
        return null == windowConfigTimeSort? userDefinedTimesort:windowConfigTimeSort;
    }
}
