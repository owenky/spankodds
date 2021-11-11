package com.sia.client.config;

import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
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
        return new Game(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]),
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
    }
    public static String getGameDebugInfo(Game game) {
        Sport sport = AppController.getSportByLeagueId(game.getSportIdentifyingLeagueId());
        String sportName = null == sport?"Unknown sport ":sport.getSportname();
        return  "DEBUGING Game, sport=" + sportName
                + ", header=" + createGameGroupHeader(game)+", teams="+game.getVisitorteam()+"/"+game.getHometeam()
                +", gameid=" + game.getGame_id() + ", leagueId=" + game.getLeague_id() + ", identifyingLeagueId="+game.getSportIdentifyingLeagueId()
                + ", status=" + game.getStatus() + ", isSeriecPrice=" + game.isSeriesprice() + ", isInGame2=" + game.isInGame2();
    }
}
