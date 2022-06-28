package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sia.client.model.Games;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameNotes {

    @JsonProperty
    private Map<Integer,String> gameNodes = new ConcurrentHashMap<>();

    @JsonProperty
    public Map<Integer, String> getGameNodes() {
        return gameNodes;
    }
    @JsonProperty
    public void setGameNodes(Map<Integer, String> gameNodes) {
        this.gameNodes = gameNodes;
    }
    @JsonIgnore
    public void addNote(int gameId, String note) {
        gameNodes.put(gameId,note);
    }
    @JsonIgnore
    public void removeNote(int gameId) {
        gameNodes.remove(gameId);
    }
    @JsonIgnore
    public void clearNotes() {
        gameNodes.clear();
    }
    @JsonIgnore
    public void syncWithGames() {
        List<Integer> obsoleteGameIds = gameNodes.keySet().stream()
                .filter(key-> ! Games.instance().containsGameId(key))
                .collect(Collectors.toList());

        obsoleteGameIds.forEach(key-> {
            gameNodes.remove(key);
        });
    }
    @JsonIgnore
    public String getNote(Integer gameId) {
        return gameNodes.get(gameId);
    }
}
