package com.sia.client.model;

import java.util.Objects;

public class GameGroupNode {

    private final GameGroupHeader gameGroupHeader;
    private final int size;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameGroupNode that = (GameGroupNode) o;
        return Objects.equals(gameGroupHeader, that.gameGroupHeader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameGroupHeader);
    }

    public GameGroupNode(GameGroupHeader gameGroupHeader, int size) {
        this.gameGroupHeader = gameGroupHeader;
        this.size = size;
    }
    public String getDateString() {
        return gameGroupHeader.getGameDateStr();
    }
    public String getGameGroupHeader() {
        return gameGroupHeader.getGameGroupHeaderStr();
    }
    public int getLeagueId() {
        return gameGroupHeader.getLeagueId();
    }
    @Override
    public String toString() {
        String eventStr;
        if ( 1 == size) {
            eventStr = "event";
        } else {
            eventStr = "events";
        }
        return gameGroupHeader.getGameGroupHeaderStr()+" ("+size+" "+eventStr+")";
    }
}
