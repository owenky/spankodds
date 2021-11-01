package com.sia.client.model;

import java.util.Objects;

public class GameGroupHeader {

    private final String gameGroupHeader;

    public GameGroupHeader(String gameGroupHeader) {
        this.gameGroupHeader = gameGroupHeader;
    }
    public String getGameGroupHeader() {
        return gameGroupHeader;
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
        return gameGroupHeader.equals(that.gameGroupHeader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameGroupHeader);
    }
}
