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

    private ColumnCustomizableDataModel<V> containingTableModel;
    private final LineGames<V> gamesVec;
    private final Map<Integer,List<Object>> rowDataMap = new ConcurrentHashMap<>();
    private int rowHeight;
    private int index;
    private String gameGroupHeader;

    abstract protected void prepareLineGamesForTableModel(LineGames<V> gamesVec);
    abstract protected List<Object> makeRowData(V game);

    public TableSection(KeyedObjectCollection<V> gameCache, boolean toAddBlankGameId, List<V> gameVec) {
        gamesVec = new LineGames<>(gameCache,toAddBlankGameId);
        gamesVec.addAll(gameVec);
        rowHeight = SiaConst.NormalRowheight;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    public int getRowHeight() {
        return rowHeight;
    }
    public void setRowHeight(int rowHeight){
        this.rowHeight = rowHeight;
    }
    public void setContainingTableModel(ColumnCustomizableDataModel<V> containingTableModel) {
        this.containingTableModel = containingTableModel;
    }
    private Map<Integer,List<Object>> getRowDataMap() {
        if ( rowDataMap.size() != gamesVec.size()) {
            rowDataMap.clear();
            for(int i=0;i<gamesVec.size();i++) {
                rowDataMap.put(i,makeRowData(gamesVec.getByIndex(i)));
            }
        }
        return rowDataMap;
    }
    protected Iterator<V> getGamesIterator() {
        return gamesVec.iterator();
    }
    public String getGameGroupHeader() {
        return this.gameGroupHeader;
    }
    public void setGameGroupHeader(String gameGroupHeader) {
        this.gameGroupHeader = gameGroupHeader;
    }
    public boolean hasHeader() {
        return null != getGameGroupHeader();
    }
    public Integer getRowKey(final int rowModelIndex) {
        return gamesVec.getGameId(rowModelIndex);
    }
    public V getGame(final int rowModelIndex) {
        int gameId =gamesVec.getGameId(rowModelIndex);
        return gamesVec.getGame(gameId);
    }
    public boolean isCellEditable(int nRow, int nCol) {
        return false;
    }
    /**
     *
     * @return table model column count
     */
    public int getColumnCount(){
        if ( 0 == getRowDataMap().size()) {
            return 0;
        }
        return getRowDataMap().get(0).size();
    }
    public Object getValueAt(final int rowModelIndex, final int colModelIndex) {

        List<Object> rowData = getRowDataMap().get(rowModelIndex);
        if ( null == rowData) {
            log(new Exception("rowData is null: rowModelIndex="+rowModelIndex));
        }
        return rowData.get(colModelIndex);
    }
    public int getRowCount() {
        return gamesVec.size();
    }
    public int getRowIndex(final Integer rowKey) {
        return gamesVec.getRowIndex(rowKey);
    }
    public V removeGameId(Integer gameidtoremove,boolean repaint) {

//        for (int i = 0; i < gamesVec.size(); i++) {
//            Game g = gamesVec.getByIndex(i);
//            int gameid = g.getGame_id();
//            if (gameid == gameidtoremove) {
//                try {
//                    gamesVec.remove(i);
//                } catch (Exception ex) {
//                    log("error removing from vector!");
//                }
//
//                setInitialData();
//                JViewport parent = (JViewport) thistable.getParent();
//                JScrollPane scrollpane = (JScrollPane) parent.getParent();
//                //	System.out.println("games size after remove "+gamesVec.size());
//                scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//                scrollpane.revalidate();
//                thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//                //System.out.println("removerow d");
//                Container comp = scrollpane.getParent();
//                comp.revalidate();
//
//                return g;
//            }
//        }
//        return null; // didn't find it
        int gameModelIndex= containingTableModel.getRowModelIndex(this,gameidtoremove);
        V g;
        if ( gameModelIndex >=0) {
//        if ( null != g) {
//            setInitialData();
//            JViewport parent = (JViewport) thistable.getParent();
//            JScrollPane scrollpane = (JScrollPane) parent.getParent();
//            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//            scrollpane.revalidate();
//            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//            Container comp = scrollpane.getParent();
//            comp.revalidate();
//            resetDataVector(); //including sorting gamesVec
//            fire();
//        }
            g = gamesVec.removeGame(gameidtoremove);
            resetDataVector();
            if (repaint) {
                TableModelEvent e = new TableModelEvent(containingTableModel, gameModelIndex, gameModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
                fire(e);
            }
        } else {
            g = null;
        }
        return g;
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
                int insertedRowModelIndex= containingTableModel.getRowModelIndex(this,g.getGame_id());
                TableModelEvent e = new TableModelEvent(containingTableModel,insertedRowModelIndex, insertedRowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
                fire(e);
            }
        }
    }
    public void fire(TableModelEvent e) {
        if ( null != containingTableModel) {
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
    public void resetDataVector() {

//        this.getDataVector().clear();
//        this.getDataVector().addAll(populateDataVector());
        //commented out 05/19/2021
//        Vector<Vector<Object>> dataVector = populateDataVector();
//        delegator.setDataVector(dataVector, columnTitles);

        prepareLineGamesForTableModel(gamesVec);
        rowDataMap.clear();
        getRowDataMap();
    }
    public boolean checktofire(Integer gameId,boolean repaint) {

        int rowIndex= gamesVec.getRowIndex(gameId);
        boolean status = rowIndex>=0;
        if (status) {
            V game = gamesVec.getGame(gameId);
            List<Object> rowData = makeRowData(game);
            rowDataMap.put(rowIndex,rowData);
System.out.println("checktofire, gamId="+gameId+", repaint="+repaint);
            if ( repaint ) {
                //TODO suspicous fire() call
                log("In TableSection, suspicous fire()");
                int rowModelIndex = containingTableModel.getRowModelIndex(this, gameId);
                TableModelEvent e = new TableModelEvent(containingTableModel, rowModelIndex, rowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
                fire(e);
            }
        }
        return status;
    }
}
