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
    private final List<Integer> gamesVec = new ArrayList<>();
    //used to be Hashtable<String, Game> games = new Hashtable() -- 06/11/2021
    private final Map<Integer,V> idToGameMap = new ConcurrentHashMap<>();
    private final Class<V> classType;

    abstract protected V createInstance();

    public KeyedObjectList() {
        gamesVec.add(SiaConst.BlankGameId);
        V blankGame = createInstance();
        blankGame.setGame_id(SiaConst.BlankGameId);
        idToGameMap.put(SiaConst.BlankGameId,blankGame);
        classType = (Class<V>)createInstance().getClass();
    }
    public V getGame(int gameId) {
       return idToGameMap.get(gameId);
    }
    public V getByIndex(int index) {
        return idToGameMap.get(gamesVec.get(index));
    }
    public V getGame(String gameId) {
        return getGame(Integer.parseInt(gameId));
    }
    public V removeGame(Integer gameId) {
        gamesVec.remove(gameId);
        return idToGameMap.remove(gameId);
    }
    public V removeGame(String gameId) {
        return removeGame(Integer.parseInt(gameId));
    }
    /*
        return true if it is add
     */
    public boolean updateOrAdd(V g) {
        boolean isAdd;
        int gameId = g.getGame_id();
        if ( ! idToGameMap.containsKey(gameId)) {
            gamesVec.add(gameId);
            isAdd = true;
        } else {
            isAdd = false;
        }
        idToGameMap.put(gameId,g);
        return isAdd;
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
    public void sort(Comparator<? super V> comparator) {
        Comparator<Integer> idComparator = (id1,id2)-> {
            V g1 = idToGameMap.get(id1);
            V g2 = idToGameMap.get(id2);
            return comparator.compare(g1,g2);
        };
        gamesVec.sort(idComparator);
    }
    public int size() {
        return gamesVec.size();
    }
    public boolean isEmpty() {
        return gamesVec.isEmpty();
    }
    public boolean contains(final Game g) {
        return idToGameMap.containsKey(g.getGame_id());

    }
    public Iterator<V> iterator() {
        Iterator<Integer> idIterator = gamesVec.iterator();
        return new Iterator<V>() {
            private Integer nextId;
            @Override
            public boolean hasNext() {
                return idIterator.hasNext();
            }

            @Override
            public V next() {
                nextId = idIterator.next();
                return idToGameMap.get(nextId);
            }

            @Override
            public void remove() {
                idIterator.remove();
                idToGameMap.remove(nextId);
            }

            @Override
            public void forEachRemaining(final Consumer action) {

            }
        };
    }
    public Object[] toArray() {
        return gamesVec.stream().map(idToGameMap::get).toArray();
    }
    public <T> T[] toArray(final T[] a) {
        return gamesVec.stream().map(idToGameMap::get).collect(Collectors.toList()).toArray(a);
    }
    public boolean add(final V game) {
        idToGameMap.put(game.getGame_id(),game);
        return gamesVec.add(game.getGame_id());
    }
    public V removeIndex(int index) {
        Integer gameId = gamesVec.remove(index);
        return idToGameMap.remove(gameId);
    }
    public V removeGameId(Integer gameId) {
        gamesVec.remove(gameId);
        return idToGameMap.remove(gameId);
    }
    public boolean removeGame(final V o) {
        V g  = idToGameMap.remove(o.getGame_id());
        if ( null != g) {
            return gamesVec.remove(new Integer(o.getGame_id()));
        } else {
            return false;
        }

    }
    public boolean containsAll(final Collection<?> games) {
        boolean status = true;
        for (Object obj: games) {
            if ( obj instanceof Game) {
                Integer gameId = ((Game)obj).getGame_id();
                if ( ! gamesVec.contains(gameId) ){
                    status = false;
                    break;
                }
                if ( ! this.idToGameMap.containsKey(gameId) ){
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
            this.idToGameMap.put(g.getGame_id(),g);
            gameIds.add(g.getGame_id());
        }
        return gamesVec.addAll(index,gameIds);
    }
    public boolean removeAll(final Collection<?> games) {
        boolean status = true;
        for(Object o: games) {
            if ( o instanceof Game) {
                Integer gameId = ((Game)o).getGame_id();
                gamesVec.remove(gameId);
                this.idToGameMap.remove(gameId);
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
        gamesVec.clear();
        idToGameMap.clear();
    }
//
//    @Override
//    public Game get(final int index) {
//        Integer gameId = gameIdList.get(index);
//        return gamesIdMap.get(gameId);
//    }
    public V set(final int index, final V game) {
        Integer oldGameId = gamesVec.set(index,game.getGame_id());
        idToGameMap.put(game.getGame_id(),game);
        return idToGameMap.remove(oldGameId);
    }
    public void add(final int index, final V game) {
        gamesVec.add(index,game.getGame_id());
        idToGameMap.put(game.getGame_id(),game);
    }
//    public Game remove(final int index) {
//        Integer gameId = gameIdList.remove(index);
//        return gamesIdMap.remove(gameId);
//    }
    public int indexOf(final Game g) {
        return gamesVec.indexOf(g.getGame_id());

    }
    public int lastIndexOf(final Game o) {
        return gamesVec.lastIndexOf(o.getGame_id());

    }
    public List<V> subList(final int fromIndex, final int toIndex) {
        List<Integer> gameIds = gamesVec.subList(fromIndex,toIndex);
        return gameIds.stream().map(idToGameMap::get).collect(Collectors.toList());
    }
}
