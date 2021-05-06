package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LineGames {
    private final List<Integer> gameIdList = new ArrayList<>();
    private final Games gameCache;

    public LineGames() {
        this(AppController.getGames());
    }
    public LineGames(Games gameCache) {
        this.gameCache = gameCache;
        gameIdList.add(SiaConst.BlankGameId);
    }
    public int getRowIndex(int gameId) {
        int index = -1;
        for(int i=0;i<gameIdList.size();i++) {
            if ( gameIdList.get(i) == gameId) {
                index = i;
                break;
            }
        }
        return index;
    }
    public Game getGame(int gameId) {
       return gameCache.getGame(gameId);
    }
    public Game getByIndex(int index) {
        return gameCache.getGame(gameIdList.get(index));
    }
    public Game getGame(String gameId) {
        return getGame(Integer.parseInt(gameId));
    }
    public boolean removeGame(Integer gameId) {
        return gameIdList.remove(gameId);
    }
    public boolean removeGame(String gameId) {
        return removeGame(Integer.parseInt(gameId));
    }
    public void addAll(Collection<Game> games) {
        games.forEach(this::addIfAbsent);
    }
    public void addIfAbsent(Game g) {
        int gameId = g.getGame_id();
        if ( ! containsGameId(gameId)) {
            gameIdList.add(gameId);
        }
    }
    public boolean containsGameId(int gameId) {
        return gameIdList.contains(gameId);
    }
    public void sort(Comparator<? super Game> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            Game g1 = gameCache.getGame(id1);
            Game g2 = gameCache.getGame(id2);
            return comparator.compare(g1,g2);
        };
        gameIdList.sort(idComparator);
    }
    public int size() {
        return gameIdList.size();
    }
    public boolean isEmpty() {
        if ( 1==gameIdList.size() && SiaConst.BlankGameId.equals(gameIdList.get(0))) {
            return true;
        }
        return gameIdList.isEmpty();
    }
    public Iterator<Game> iterator() {
        Iterator<Integer> idIterator = gameIdList.iterator();
        return new Iterator<Game>() {
            private Integer nextId;
            @Override
            public boolean hasNext() {
                return idIterator.hasNext();
            }

            @Override
            public Game next() {
                nextId = idIterator.next();
                return gameCache.getGame(nextId);
            }

            @Override
            public void remove() {
                idIterator.remove();
            }
        };
    }
    public Integer removeIndex(int index) {
        if ( 0 == index) {
            throw new IllegalArgumentException("Can't remove blank game id index :"+0);
        }
        return gameIdList.remove(index);
    }
    public Game removeGameId(Integer gameId) {
        if ( gameIdList.remove(gameId) ) {
            return gameCache.getGame(gameId);
        } else {
            return null;
        }
    }
    public boolean removeGame(final Game o) {
        return gameIdList.remove(new Integer(o.getGame_id()));

    }
    public int indexOf(final Game g) {
        return gameIdList.indexOf(g.getGame_id());
    }
}
