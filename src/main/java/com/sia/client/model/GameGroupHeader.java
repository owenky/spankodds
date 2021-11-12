package com.sia.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GameGroupHeader {

    public static final DateTimeFormatter gameDateFormatter = DateTimeFormatter.ofPattern("MM/dd");
    private final String gameGroupHeaderStr;
    private final String leagueName;
    private final int subLeagueId;
    private final int leagueId;
    private final int anchorPos;
    private final LocalDateTime gameTime;
    private final LocalDate gameDate;
    private final String gameDateStr;

    public static GameGroupHeader create(String leagueName,LocalDateTime gameTime,int subLeagueId,int leagueId) {
        return new GameGroupHeader(gameTime,leagueName,subLeagueId,leagueId,0);
    }
    public static GameGroupHeader createStageGroupHeader(String stageName,int anchorPos) {
        return new GameGroupHeader(null,stageName,0,0,anchorPos);
    }
    public GameGroupHeader(LocalDateTime gameTime, String leagueName, int subLeagueId, int leagueId,int anchorPos) {
        this.subLeagueId = subLeagueId;
        this.leagueId = leagueId;
        this.anchorPos = anchorPos;
        this.leagueName = leagueName;
        this.gameTime = gameTime;
        this.gameDate = null==gameTime?LocalDate.now():LocalDate.of(gameTime.getYear(), gameTime.getMonth(),gameTime.getDayOfMonth());
        this.gameDateStr = null==gameTime?"":gameDateFormatter.format(gameTime);
        this.gameGroupHeaderStr = constructGameGroupHeaderString(leagueName,gameDateStr);
    }
    public static String constructGameGroupHeaderString(String leagueName,String gameDateStr) {
        return "".equals(gameDateStr)?leagueName:leagueName + " " + gameDateStr;
    }
    public int getSubLeagueId() {
        return subLeagueId;
    }
    public int getLeagueId() {
        return leagueId;
    }
    public int getAnchorPos() {
        return anchorPos;
    }
    public String getGameGroupHeaderStr() {
        return gameGroupHeaderStr;
    }
    public String getLeagueName() {
        return leagueName;
    }
    public String getGameDateStr() {
        return gameDateStr;
    }
    public LocalDateTime getGameTime() {
        return gameTime;
    }
    public LocalDate getGameDate() {
        return gameDate;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameGroupHeader that = (GameGroupHeader) o;
        return gameGroupHeaderStr.equals(that.gameGroupHeaderStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameGroupHeaderStr);
    }
    @Override
    public String toString() {
        return gameGroupHeaderStr;
    }
}
