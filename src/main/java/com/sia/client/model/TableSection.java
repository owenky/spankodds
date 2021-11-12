package com.sia.client.model;

import com.sia.client.config.SiaConst;

import javax.swing.event.TableModelEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.sia.client.config.Utils.checkAndRunInEDT;

public abstract class TableSection<V extends KeyedObject> {

    private final LineGames<V> gamesVec;
    private final Map<Integer, List<Object>> rowDataMap = new ConcurrentHashMap<>();
    private ColumnCustomizableDataModel<V> containingTableModel;
    private int rowHeight;
    private int index;
    protected final GameGroupHeader gameGroupHeader;

    abstract protected List<Object> makeRowData(V game);

    public TableSection(GameGroupHeader gameGroupHeader,KeyedObjectList<V> gameCache, boolean toAddBlankGameId, List<V> gameVec) {
        this.gameGroupHeader = gameGroupHeader;
        gamesVec = new LineGames<>(gameCache, toAddBlankGameId);
        gamesVec.addAll(gameVec);
        rowHeight = SiaConst.NormalRowheight;
    }

    public ColumnCustomizableDataModel<V> getContainingTableModel() {
        return containingTableModel;
    }

    public void setContainingTableModel(ColumnCustomizableDataModel<V> containingTableModel) {
        this.containingTableModel = containingTableModel;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    protected Iterator<V> getGamesIterator() {
        return gamesVec.iterator();
    }
    public void sort(Comparator<? super V> gameSorter) {
        gamesVec.sort(gameSorter);
        rowDataMap.clear();
    }

//    public boolean hasHeader() {
//        return ! gameGroupHeader.isEmpty();
//    }

    public GameGroupHeader getGameGroupHeader() {
        return this.gameGroupHeader;
    }
    public Integer getRowKey(final int rowModelIndex) {
        return gamesVec.getGameId(rowModelIndex);
    }

    public V getGame(final int rowModelIndex) {
        int gameId = gamesVec.getGameId(rowModelIndex);
        return gamesVec.getGameFromDataSource(gameId);
    }

    /**
     * @return table model column count
     */
    public int getColumnCount() {
        if (0 == getRowDataMap().size()) {
            return 0;
        }
        return getRowDataMap().get(0).size();
    }

    private Map<Integer, List<Object>> getRowDataMap() {
        if (rowDataMap.size() != gamesVec.size()) {
            rowDataMap.clear();
            for (int i = 0; i < gamesVec.size(); i++) {
                updateRowDataMapAtRow(i);
            }
        }
        return rowDataMap;
    }
    private void updateRowDataMapAtRow(int index) {
        rowDataMap.put(index, makeRowData(gamesVec.getByIndex(index)));
    }
    public Object getValueAt(final int rowModelIndex, final int colModelIndex) {

        List<Object> rowData = getRowDataMap().get(rowModelIndex);
        if (null == rowData) {
            throw new IllegalArgumentException("rowData is null: rowModelIndex=" + rowModelIndex);
        }
        return rowData.get(colModelIndex);
    }

    public int getRowCount() {
        if (  containsDataRow() ) { //contains only one game and this game is group game header
            return gamesVec.size();
        }
        return 0;
    }
    protected boolean containsDataRow() {
        return 1 < gamesVec.size();
    }
    public int getRowIndex(final Integer gameId) {
        return gamesVec.getRowIndex(gameId);
    }

    public V removeGameId(Integer gameidtoremove) {

        int gameModelIndex = containingTableModel.getRowModelIndexByGameId(this, gameidtoremove);
        V g;
        if ( 0 <= gameModelIndex) {
            g = gamesVec.removeGame(gameidtoremove);
            this.rowDataMap.clear();
            //caller responsible for firing table model event -- 2021-11-07
//            TableModelEvent e = new TableModelEvent(containingTableModel, gameModelIndex, gameModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
//            fire(e);
        } else {
            g = null;
        }
        return g;
    }
    public void removeGameIdsAndCleanup(Collection<Integer> gameidstoremove) {
        for(int i=gamesVec.size()-1; 0<=i; i-- ) {
            V game = gamesVec.getByIndex(i);
            boolean toRemove;
            if ( null == game) {
                toRemove = true;
            } else {
                toRemove = gameidstoremove.contains(game.getGame_id());
            }
            if ( toRemove) {
                gamesVec.removeGameIdAt(i);
                this.rowDataMap.clear();
            }
        }
    }

    public int addOrUpdate(V g) {
        setHowHeighIfAbsent(g);
        int index = gamesVec.addIfAbsent(g);
        if ( 0 > index ) {
            //it is adding, game was added to gamesVec, need to clear up rowDataMap -- 2021-11-06
            this.rowDataMap.clear();
        } else {
            updateRowDataMapAtRow(index);
        }
        return index;
    }
    protected void setHowHeighIfAbsent(V v) {
        //for sub class to override
    }
    public void addAllGames(List<V> games) {
        if ( null != games && games.size() > 0) {
            setHowHeighIfAbsent(games.get(0));
            for (V g : games) {
                gamesVec.addIfAbsent(g.getGame_id());
            }
            resetDataVector();
        }
    }
    public void resetDataVector() {
        prepareLineGamesForTableModel(gamesVec);
        rowDataMap.clear();
        getRowDataMap();
    }

    public void fire(TableModelEvent e) {
        if (null != containingTableModel) {
            checkAndRunInEDT(() -> {
                TableModelEvent evt;
                if (null == e) {
                    evt = new TableModelEvent(containingTableModel);
                } else {
                    evt = e;
                }
                containingTableModel.fireTableChanged(evt);
            });
        }
    }

    abstract protected void prepareLineGamesForTableModel(LineGames<V> gamesVec);

    public int size() {
        return gamesVec.size();
    }

    public boolean checktofire(V game, boolean repaint) {

        int rowIndex = gamesVec.getRowIndex(game.getGame_id());
        boolean status = rowIndex >= 0;
        if (status) {
            List<Object> rowData = makeRowData(game);
            rowDataMap.put(rowIndex, rowData);
        }
        return status;
    }
}
