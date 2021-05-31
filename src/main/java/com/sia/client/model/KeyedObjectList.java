package com.sia.client.model;

import com.sia.client.config.SiaConst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class KeyedObjectList<V extends KeyedObject> {
    private final List<Integer> gameIdList = new ArrayList<>();
    private final Map<Integer,V> gamesIdMap = new ConcurrentHashMap<>();
    private final Class<V> classType;

    abstract protected V createInstance();

    public KeyedObjectList() {
        gameIdList.add(SiaConst.BlankGameId);
        V blankGame = createInstance();
        blankGame.setGame_id(SiaConst.BlankGameId);
        gamesIdMap.put(SiaConst.BlankGameId,blankGame);
        classType = (Class<V>)createInstance().getClass();
    }
    public V getGame(int gameId) {
       return gamesIdMap.get(gameId);
    }
    public V getByIndex(int index) {
        return gamesIdMap.get(gameIdList.get(index));
    }
    public V getGame(String gameId) {
        return getGame(Integer.parseInt(gameId));
    }
    public V removeGame(Integer gameId) {
        gameIdList.remove(gameId);
        return gamesIdMap.remove(gameId);
    }
    public V removeGame(String gameId) {
        return removeGame(Integer.parseInt(gameId));
    }
    public void updateOrAdd(V g) {
        int gameId = g.getGame_id();
        if ( ! gamesIdMap.containsKey(gameId)) {
            gameIdList.add(gameId);
        }
        gamesIdMap.put(gameId,g);
    }
    public boolean containsGameId(int gameId) {
        return gamesIdMap.containsKey(gameId);
    }
    public boolean addAll(final Collection<? extends V> games) {
        for(V g: games) {
            this.add(g);
        }
        return true;
    }
    public void sort(Comparator<? super V> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            V g1 = gamesIdMap.get(id1);
            V g2 = gamesIdMap.get(id2);
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
    public boolean add(final V game) {
        gamesIdMap.put(game.getGame_id(),game);
        return gameIdList.add(game.getGame_id());
    }
    public V removeIndex(int index) {
        Integer gameId = gameIdList.remove(index);
        return gamesIdMap.remove(gameId);
    }
    public V removeGameId(Integer gameId) {
        gameIdList.remove(gameId);
        return gamesIdMap.remove(gameId);
    }
    public boolean removeGame(final V o) {
        V g  = gamesIdMap.remove(o.getGame_id());
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
    public boolean addAll(final int index, final Collection<? extends V> games) {
        List<Integer> gameIds = new ArrayList<>();
        for(V g:games) {
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
            if ( null != obj && obj.getClass().isAssignableFrom(classType) ){
                this.add((V)obj);
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
    public V set(final int index, final V game) {
        Integer oldGameId = gameIdList.set(index,game.getGame_id());
        gamesIdMap.put(game.getGame_id(),game);
        return gamesIdMap.remove(oldGameId);
    }
    public void add(final int index, final V game) {
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
    public List<V> subList(final int fromIndex, final int toIndex) {
        List<Integer> gameIds = gameIdList.subList(fromIndex,toIndex);
        return gameIds.stream().map(gamesIdMap::get).collect(Collectors.toList());
    }
}
