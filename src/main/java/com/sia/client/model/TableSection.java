package com.sia.client.model;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public abstract class TableSection<V extends KeyedObject> {

    private final DefaultTableModel delegator = new DefaultTableModel(); //used for listener purpose, it does not store data anymore
    private final LineGames<V> gamesVec;
    private Map<Integer,List<Object>> rowDataMap = null;
    private int rowHeight;
    private int index;
    private String gameGroupHeader;

//    abstract protected Vector<ColumnData> getColumnData();
    abstract protected void prepareLineGamesForTableModel(LineGames<V> gamesVec);
    abstract protected List<Object> makeRowData(V game);

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

    public TableSection(KeyedObjectCollection<V> gameCache, boolean toAddBlankGameId, List<V> gameVec) {
        gamesVec = new LineGames<>(gameCache,toAddBlankGameId);
        gamesVec.addAll(gameVec);
    }
    private Map<Integer,List<Object>> getRowDataMap() {
        if ( rowDataMap == null) {
            rowDataMap = new HashMap<>();
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
//        return delegator.getValueAt(rowModelIndex,colModelIndex);
        return getRowDataMap().get(rowModelIndex).get(colModelIndex);
    }
    public int getRowCount() {
        return gamesVec.size();
    }
    public int getRowIndex(final Integer rowKey) {
        return gamesVec.getRowIndex(rowKey);
    }
    public TableModelListener[] getTableModelListeners() {
        return delegator.getTableModelListeners();
    }
    public void addTableModelListener(final TableModelListener l) {
        delegator.addTableModelListener(l);
    }
    public void removeTableModelListener(final TableModelListener l) {
        delegator.removeTableModelListener(l);
    }
    public V removeGameId(Integer gameidtoremove) {

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
        V g = gamesVec.removeGameId(gameidtoremove);
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
                fire();
            }
        }
    }
    public void fire() {
        checkAndRunInEDT(()->delegator.fireTableDataChanged());
    }
    public void resetDataVector() {

//        this.getDataVector().clear();
//        this.getDataVector().addAll(populateDataVector());
        //commented out 05/19/2021
//        Vector<Vector<Object>> dataVector = populateDataVector();
//        delegator.setDataVector(dataVector, columnTitles);

        prepareLineGamesForTableModel(gamesVec);
        rowDataMap = null;
    }
    public boolean checktofire(Integer gameid) {

        boolean status =gamesVec.containsGameId(gameid);
        if (status) {
            //TODO suspicous fire() call
            log("In LinesTableData, suspicous fire()");
            fire();
        }
        return status;
    }
}
