package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class KeyedObjectList<V extends KeyedObject> {
    private final Map<Integer,V> idToGameMap = new ConcurrentHashMap<>();

    abstract protected V createInstance();

    public KeyedObjectList() {
        V blankGame = createInstance();
        blankGame.setGame_id(SiaConst.BlankGameId);
        idToGameMap.put(SiaConst.BlankGameId,blankGame);
    }
    public List<Integer> toPositiveKeyListSorted() {
        return idToGameMap.keySet().stream().filter(key-> key >=0).sorted().collect(Collectors.toList());
    }
    public V getGame(int gameId) {
       return idToGameMap.get(gameId);
    }
    public V getGame(String gameId) {
        return getGame(Integer.parseInt(gameId));
    }
    public V removeGame(Integer gameId) {
        return idToGameMap.remove(gameId);
    }
    public V removeGame(String gameId) {
        return removeGame(Integer.parseInt(gameId));
    }

    public void updateOrAdd(V g) {
        int gameId = g.getGame_id();
        idToGameMap.put(gameId,g);
    }
    public boolean containsGameId(int gameId) {
        return idToGameMap.containsKey(gameId);
    }
    public boolean addAll(final Collection<? extends V> games) {
        for(V g: games) {
            this.add(g);
        }
        return true;
    }
    public int size() {
        return idToGameMap.size();
    }
    public boolean contains(final Game g) {
        return idToGameMap.containsKey(g.getGame_id());

    }
    public Iterator<V> iterator() {
        return idToGameMap.values().iterator();
    }
    public void add(final V game) {
        idToGameMap.put(game.getGame_id(),game);
    }

    public void clear() {
        idToGameMap.clear();
    }
}
