package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LineGames<V extends KeyedObject> {
    private final List<Integer> gameIdList = new ArrayList<>();
    private final KeyedObjectList<V> gameCache;


    public LineGames(KeyedObjectList<V> gameCache, boolean toAddBlankGameId) {
        this.gameCache = gameCache;
        if ( toAddBlankGameId ) {
            gameIdList.add(SiaConst.BlankGameId);
        }
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
    public int getGameId(int rowModelIndex) {
        return gameIdList.get(rowModelIndex);
    }
    public V getGameFromDataSource(int gameId) {
       return gameCache.getGame(gameId);
    }
    public V getByIndex(int index) {
        return gameCache.getGame(gameIdList.get(index));
    }
    public Integer removeGameIdAt(int index) {
        return gameIdList.remove(index);
    }
    public V removeGame(Integer gameId) {
        V game;
        if ( gameIdList.remove(gameId) ) {
            game = gameCache.getGame(gameId);
        } else {
            game = null;
        }
        return game;
    }
    public void addAll(Collection<? extends V> games) {
        games.forEach(this::addIfAbsent);
    }
    public int addIfAbsent(V g) {
        int gameId = g.getGame_id();
        return addIfAbsent(gameId);
    }

    /**
     *
     * @param gameId
     * @return index of gameId in the list, otherwise return -1, but gameid is appended
     */
    public int addIfAbsent(int gameId) {
        int index = gameIdList.indexOf(gameId);
        if ( index < 0) {
            gameIdList.add(gameId);
        }
        return index;
    }
    public void sort(Comparator<? super V> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            V g1 = gameCache.getGame(id1);
            V g2 = gameCache.getGame(id2);
            return comparator.compare(g1,g2);
        };
        gameIdList.sort(idComparator);
    }
    public int size() {
        return gameIdList.size();
    }
    public boolean isEmpty() {
        if ( 1==size() && SiaConst.BlankGameId.equals(gameIdList.get(0))) {
            return true;
        }
        return gameIdList.isEmpty();
    }
    public Iterator<V> iterator() {
        Iterator<Integer> idIterator = gameIdList.iterator();
        return new Iterator<V>() {
            private Integer nextId;
            @Override
            public boolean hasNext() {
                return idIterator.hasNext();
            }

            @Override
            public V next() {
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
    public V removeGameId(Integer gameId) {
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
    @Override
    public String toString() {
        return "size="+gameIdList.size();
    }
}
