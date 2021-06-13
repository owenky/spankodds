package com.sia.client.model;

import com.sia.client.config.SiaConst;

import javax.swing.event.TableModelEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public abstract class TableSection<V extends KeyedObject> {

    private final LineGames<V> gamesVec;
    private final Map<Integer, List<Object>> rowDataMap = new ConcurrentHashMap<>();
    private ColumnCustomizableDataModel<V> containingTableModel;
    private int rowHeight;
    private int index;
    private final String gameGroupHeader;

    public TableSection(String gameGroupHeader,KeyedObjectList<V> gameCache, boolean toAddBlankGameId, List<V> gameVec) {
        this.gameGroupHeader = null== gameGroupHeader?null:gameGroupHeader.trim();
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

    public boolean hasHeader() {
        return null != getGameGroupHeader();
    }

    public String getGameGroupHeader() {
        return this.gameGroupHeader;
    }
    public Integer getRowKey(final int rowModelIndex) {
        return gamesVec.getGameId(rowModelIndex);
    }

    public V getGame(final int rowModelIndex) {
        int gameId = gamesVec.getGameId(rowModelIndex);
        return gamesVec.getGame(gameId);
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
                rowDataMap.put(i, makeRowData(gamesVec.getByIndex(i)));
            }
        }
        return rowDataMap;
    }

    abstract protected List<Object> makeRowData(V game);

    public Object getValueAt(final int rowModelIndex, final int colModelIndex) {

        List<Object> rowData = getRowDataMap().get(rowModelIndex);
        if (null == rowData) {
            log(new Exception("rowData is null: rowModelIndex=" + rowModelIndex));
        }
        return rowData.get(colModelIndex);
    }

    public int getRowCount() {
        return gamesVec.size();
    }

    public int getRowIndex(final Integer rowKey) {
        return gamesVec.getRowIndex(rowKey);
    }

    public V removeGameId(Integer gameidtoremove, boolean repaint) {

        int gameModelIndex = containingTableModel.getRowModelIndex(this, gameidtoremove);
        V g;
        if (gameModelIndex >= 0) {
            g = gamesVec.removeGame(gameidtoremove);
            if (repaint) {
            resetDataVector();
            TableModelEvent e = new TableModelEvent(containingTableModel, gameModelIndex, gameModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
            fire(e);
        }
        } else {
            g = null;
        }
        return g;
    }

    public void removeGameIds(Integer[] gameidstoremove) {

        //TODO unnecessary call?
        log("WARNING: In LinesTableData, skip AppController.disableTabs();");
//            AppController.disableTabs();

        for (Integer gameId : gameidstoremove) {
            removeGameId(gameId, false);
        }
//            List<String> list = Arrays.asList(gameidstoremove);
//            Vector<String> gameidstoremovevec = new Vector<>(list);
//            for (Iterator<Game> iterator = getGamesIterator(); iterator.hasNext(); ) {
//
//                Game g = iterator.next();
//                String gameid = "" + g.getGame_id();
//
//                if (gameidstoremovevec.contains(gameid)) {
//                    try {
//                        iterator.remove();
//                    } catch (Exception ex) {
//                        log(ex);
//                    }
//                }
//            }

//            setInitialData();
//            JViewport parent = (JViewport) thistable.getParent();
//            JScrollPane scrollpane = (JScrollPane) parent.getParent();
//
//            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//            scrollpane.revalidate();
//            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//            Container comp = scrollpane.getParent();
//            comp.revalidate();
//            AppController.enableTabs();
        resetDataVector(); //including sorting gamesVec
//            fire(null);
    }

    public void addGame(V g, boolean repaint) {
        boolean exist = null != gamesVec.getGame(g.getGame_id());
        if (exist) {
            gamesVec.addIfAbsent(g);
            resetDataVector(); //including sorting gamesVec
            if (repaint) {
//                setInitialData();
//                JViewport parent = (JViewport) thistable.getParent();
//                JScrollPane scrollpane = (JScrollPane) parent.getParent();
//                scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//                scrollpane.revalidate();
//                thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//                Container comp = scrollpane.getParent();
//                comp.revalidate();
                int insertedRowModelIndex = containingTableModel.getRowModelIndex(this, g.getGame_id());
                TableModelEvent e = new TableModelEvent(containingTableModel, insertedRowModelIndex, insertedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
                fire(e);
            }
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

    public boolean checktofire(Integer gameId, boolean repaint) {

        int rowIndex = gamesVec.getRowIndex(gameId);
        boolean status = rowIndex >= 0;
        if (status) {
            V game = gamesVec.getGame(gameId);
            List<Object> rowData = makeRowData(game);
            rowDataMap.put(rowIndex, rowData);
//            if ( repaint ) {
//                int rowModelIndex = containingTableModel.getRowModelIndex(this, gameId);
//                TableModelEvent e = new TableModelEvent(containingTableModel, rowModelIndex, rowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
//                fire(e);
//            }
        }
        return status;
    }
}
