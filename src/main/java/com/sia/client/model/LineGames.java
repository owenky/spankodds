package com.sia.client.model;

import com.sia.client.ui.AppController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LineGames {
    private final List<Integer> gameIdList = new ArrayList<>();
    private static final Games games = AppController.getGames();

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
       return games.getGame(gameId);
    }
    public Game getByIndex(int index) {
        return games.getGame(gameIdList.get(index));
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
        Integer gameId = g.getGame_id();
        if ( ! containsGameId(gameId)) {
            gameIdList.add(gameId);
        }
    }
    public boolean containsGameId(int gameId) {
        return gameIdList.contains(gameId);
    }
    public void sort(Comparator<? super Game> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            Game g1 = games.getGame(id1);
            Game g2 = games.getGame(id2);
            return comparator.compare(g1,g2);
        };
        gameIdList.sort(idComparator);
    }
    public int size() {
        return gameIdList.size();
    }
    public boolean isEmpty() {
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
                return games.getGame(nextId);
            }

            @Override
            public void remove() {
                idIterator.remove();
            }
        };
    }
    public Integer removeIndex(int index) {
        return gameIdList.remove(index);
    }
    public Game removeGameId(Integer gameId) {
        if ( gameIdList.remove(gameId) ) {
            return games.getGame(gameId);
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
