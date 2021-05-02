package com.sia.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class Games   {
    private final List<Integer> gameIdList = new ArrayList<>();
    private final Map<Integer,Game> gamesIdMap = new ConcurrentHashMap<>();

    public Game getGame(int gameId) {
       return gamesIdMap.get(gameId);
    }
    public Game getByIndex(int index) {
        //TODO remove debug check
        if ( 0 > index || index > (gameIdList.size()-1)) {
            log(new Exception("Index is out of bound: list size="+gameIdList.size()+", index="+index));
        }
        return gamesIdMap.get(gameIdList.get(index));
    }
    public Game getGame(String gameId) {
        return getGame(Integer.parseInt(gameId));
    }
    public Game removeGame(Integer gameId) {
        gameIdList.remove(gameId);
        return gamesIdMap.remove(gameId);
    }
    public Game removeGame(String gameId) {
        return removeGame(Integer.parseInt(gameId));
    }
    public void updateOrAdd(Game g) {
        int gameId = g.getGame_id();
        if ( ! gamesIdMap.containsKey(gameId)) {
            gameIdList.add(gameId);
        }
        gamesIdMap.put(gameId,g);
    }
    public boolean containsGameId(int gameId) {
        return gamesIdMap.containsKey(gameId);
    }
    public boolean addAll(final Collection<? extends Game> games) {
        for(Game g: games) {
            this.add(g);
        }
        return true;
    }
    public void sort(Comparator<? super Game> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            Game g1 = gamesIdMap.get(id1);
            Game g2 = gamesIdMap.get(id2);
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
    public boolean contains(final Game g) {
        return gamesIdMap.containsKey(g.getGame_id());

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
                return gamesIdMap.get(nextId);
            }

            @Override
            public void remove() {
                idIterator.remove();
                gamesIdMap.remove(nextId);
            }

            @Override
            public void forEachRemaining(final Consumer action) {

            }
        };
    }
    public Object[] toArray() {
        return gameIdList.stream().map(gamesIdMap::get).toArray();
    }
    public <T> T[] toArray(final T[] a) {
        return gameIdList.stream().map(gamesIdMap::get).collect(Collectors.toList()).toArray(a);
    }
    public boolean add(final Game game) {
        gamesIdMap.put(game.getGame_id(),game);
        return gameIdList.add(game.getGame_id());
    }
    public Game removeIndex(int index) {
        Integer gameId = gameIdList.remove(index);
        return gamesIdMap.remove(gameId);
    }
    public Game removeGameId(Integer gameId) {
        gameIdList.remove(gameId);
        return gamesIdMap.remove(gameId);
    }
    public boolean removeGame(final Game o) {
        Game g  = gamesIdMap.remove(o.getGame_id());
        if ( null != g) {
            return gameIdList.remove(new Integer(o.getGame_id()));
        } else {
            return false;
        }

    }
    public boolean containsAll(final Collection<?> games) {
        boolean status = true;
        for (Object obj: games) {
            if ( obj instanceof Game) {
                Integer gameId = ((Game)obj).getGame_id();
                if ( ! gameIdList.contains(gameId) ){
                    status = false;
                    break;
                }
                if ( ! gamesIdMap.containsKey(gameId) ){
                    status = false;
                    break;
                }
            } else {
                status = false;
                break;
            }
        }
        return status;
    }
    public boolean addAll(final int index, final Collection<? extends Game> games) {
        List<Integer> gameIds = new ArrayList<>();
        for(Game g:games) {
            gamesIdMap.put(g.getGame_id(),g);
            gameIds.add(g.getGame_id());
        }
        return gameIdList.addAll(index,gameIds);
    }
    public boolean removeAll(final Collection<?> games) {
        boolean status = true;
        for(Object o: games) {
            if ( o instanceof Game) {
                Integer gameId = ((Game)o).getGame_id();
                gameIdList.remove(gameId);
                gamesIdMap.remove(gameId);
            } else {
                status = false;
            }
        }
        return status;
    }
    public boolean retainAll(final Collection<?> c) {
        clear();
        boolean status = true;
        for ( Object obj: c) {
            if ( obj instanceof Game) {
                this.add((Game)obj);
            } else {
                status = false;
            }
        }
        return status;
    }
    public void clear() {
        gameIdList.clear();
        gamesIdMap.clear();
    }
//
//    @Override
//    public Game get(final int index) {
//        Integer gameId = gameIdList.get(index);
//        return gamesIdMap.get(gameId);
//    }
    public Game set(final int index, final Game game) {
        Integer oldGameId = gameIdList.set(index,game.getGame_id());
        gamesIdMap.put(game.getGame_id(),game);
        return gamesIdMap.remove(oldGameId);
    }
    public void add(final int index, final Game game) {
        gameIdList.add(index,game.getGame_id());
        gamesIdMap.put(game.getGame_id(),game);
    }
//    public Game remove(final int index) {
//        Integer gameId = gameIdList.remove(index);
//        return gamesIdMap.remove(gameId);
//    }
    public int indexOf(final Game g) {
        return gameIdList.indexOf(g.getGame_id());

    }
    public int lastIndexOf(final Game o) {
        return gameIdList.lastIndexOf(o.getGame_id());

    }
    public List<Game> subList(final int fromIndex, final int toIndex) {
        List<Integer> gameIds = gameIdList.subList(fromIndex,toIndex);
        return gameIds.stream().map(gamesIdMap::get).collect(Collectors.toList());
    }
}
