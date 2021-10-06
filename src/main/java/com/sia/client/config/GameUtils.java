package com.sia.client.config;

import com.sia.client.model.Game;
import com.sia.client.model.Sport;
import com.sia.client.model.SportType;
import com.sia.client.ui.AppController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class GameUtils {

    public static final DateTimeFormatter gameDateFormatter = DateTimeFormatter.ofPattern("MM/dd");
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
            return gameGroupHeader;
        } else {
            return null;
        }
    }
    public static String getGameDateStr(Game game) {
        LocalDateTime ldt = new Date(game.getGamedate().getTime()).toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        return gameDateFormatter.format(ldt);
    }
    public static String getGameGroupHeader(Game game) {
        int sportIdentifyingLeagueId = game.getSportIdentifyingLeagueId();
        return AppController.getSportByLeagueId(sportIdentifyingLeagueId).getLeaguename() + " " + getGameDateStr(game);
    }
    public static boolean isGameNear(Game game) {
        SportType sportType = SportType.findByGame(game);
        if ( null != sportType ) {
            return sportType.isGameNear(game);
        } else {
            return false;
        }
    }
    public static void main(String [] argvb ) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2021-02-29");
        LocalDate ld = LocalDate.of(date.getYear()+1900,date.getMonth()+1,date.getDate());

        System.out.println("ld="+ld);
    }
}
