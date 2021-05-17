package com.sia.client.model;

import com.sia.client.ui.LinesTableData;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.sia.client.config.Utils.log;

public class MainGameTableModel extends ColumnCustomizableDataModel {

    private final List<LinesTableData> gameLines = new ArrayList<>();
    private final LinesTableDataListner linesTableDataListner = new LinesTableDataListner();
    //TODO debug flag
    private boolean headerInstalled = false;
    public void setHeaderInstalled(boolean headerInstalled) {this.headerInstalled = headerInstalled;}
    //END OF debug TODO

    public MainGameTableModel() {

    }
    public void clear() {
        for(LinesTableData ltd:gameLines) {
            removeTableModelListener(ltd);
        }
        gameLines.clear();
        headerInstalled = false;
    }
    public void copyTo(Collection<LinesTableData> destCollection) {
        destCollection.addAll(gameLines);
    }
    //refactored from MainScreen::moveGameToThisHeader(Game, String)
    public void moveGameToThisHeader(Game g, String header) {
        Game thisgame = null;

        for (LinesTableData gameLine : gameLines) {
            thisgame = gameLine.removeGameId(g.getGame_id());
            if (thisgame != null) {
                break;
            }
        }
        // now lets see if i found it in either
        if (thisgame != null) // i did find it
        {
            LinesTableData ltd = null;
            if (header.equalsIgnoreCase("In Progress")) {
                ltd = gameLines.get(gameLines.size() - 4);
            } else if (header.equalsIgnoreCase("Soccer In Progress")) {
                ltd = gameLines.get(gameLines.size() - 3);
            } else if (header.equalsIgnoreCase("FINAL")) {
                ltd = gameLines.get(gameLines.size() - 2);
            } else if (header.equalsIgnoreCase("Soccer FINAL")) {
                ltd = gameLines.get(gameLines.size() - 1);
            } else if (header.equalsIgnoreCase("Halftime")) {
                ltd = gameLines.get(0);
            } else if (header.equalsIgnoreCase("Soccer Halftime")) {
                ltd = gameLines.get(1);
            }

            if ( null != ltd) {
                ltd.addGame(thisgame, true);
            } else {
                log( new Exception("can't find LinesTableData for header:"+header));
            }
        }
    }
    //refactored from MainScreen::addGame(Game, boolean)
    public void addGameToGameGroup(String gameGroupHeader,Game game,boolean paint) {
        LinesTableData ltd = getLinesTableDataByHeader(gameGroupHeader);
        if ( null != ltd) {
            ltd.addGame(game, paint);
        }
    }
    public LinesTableData getLinesTableDataByHeader(String gameGroupHeader) {
        LinesTableData rtn = null;
        for (LinesTableData ltd : gameLines) {
            if (gameGroupHeader.equals(ltd.getGameGroupHeader())) {
                rtn = ltd;
                break;
            }
        }
        return rtn;
    }
    public void removeGames(String[] gameids) {
        for (LinesTableData linesTableData : gameLines) {
            try {
                linesTableData.removeGameIds(gameids);
            } catch (Exception ex) {
                log(ex);
            }
        }
    }
    //copied from MainScreen::removeGame(int)
    public void removeGame(int gameid) {
        for (LinesTableData linesTableData : gameLines) {
            //TODO add if logic
            if (null != linesTableData.removeGameId(gameid)) {
                //gameid is removed from a LinesTableData, don't need to continue because a gameid can only be in one LinesTableData
                break;
            }
        }
    }
    public void makeDataModelsVisible(boolean b) {
        for (LinesTableData linesTableData : gameLines) {
            linesTableData.setInView(b);
        }
    }
    public void clearColors() {
        for (LinesTableData linesTableData : gameLines) {
            linesTableData.clearColors();
        }
    }
    public LinesTableData checktofire(int gameId) {
        LinesTableData rtn = null;
        for (final LinesTableData ltd : gameLines) {
            boolean status = ltd.checktofire(gameId);
            if (status) {
                rtn  = ltd;
                break;
            }
        }
        return rtn;
    }
    public int getGameId(int rowModelIndex) {
        LtdSrhStruct ltdSrhStruct = getLinesTableData(rowModelIndex);
        LinesTableData r = ltdSrhStruct.linesTableData;
        int rowIndexInLinesTableData = rowModelIndex-ltdSrhStruct.offset;
        return r.getGameId(rowIndexInLinesTableData);
    }
    public List<BlankGameStruct> getBlankGameIdIndex() {
        List<BlankGameStruct> idIndexList = new ArrayList<>();
        int rowModeIndex=0;
        for(LinesTableData ltd: gameLines) {
            if ( ltd.hasHeader()) {
                idIndexList.add(new BlankGameStruct(rowModeIndex, ltd));
            }
            rowModeIndex+=ltd.getRowCount();
        }
        return idIndexList;
    }
    public void addGameLine(LinesTableData gameLine) {
        gameLine.setIndex(gameLines.size());
        gameLines.add(gameLine);
        removeAndAddTableModelListener(gameLine);
    }
    public int getRowModelIndex(LinesTableData ltd, int gameId) {
        int gameIndex = ltd.getRowIndex(gameId);
        int offset=0;
        for(int i=0;i<ltd.getIndex();i++) {
            offset+=gameLines.get(i).getRowCount();
        }
        return gameIndex+offset;
    }
    @Override
    public Object getValueAt(int rowModelIndex, int colModelIndex) {
        LtdSrhStruct ltdSrhStruct = getLinesTableData(rowModelIndex);
        LinesTableData r = ltdSrhStruct.linesTableData;
        return r.getValueAt((rowModelIndex-ltdSrhStruct.offset),colModelIndex);
    }
    @Override
    public int getRowCount() {

        if (null==gameLines)  {
            //first call to this method is from DefaultTableModel constructor before gameLines is instantialized.
            return 0;
        }
        int rowCount=0;
        for(LinesTableData r: gameLines) {
            rowCount += r.getRowCount();
        }
        return rowCount;
    }
    @Override
    public int getColumnCount() {
        return super.getColumnCount();
    }
    public LtdSrhStruct getLinesTableData(int rowModelIndex) {
        int modelIndex=0;
        LinesTableData rtn = null;
        for(LinesTableData r: gameLines) {
            if ( (modelIndex+r.getRowCount()) <= rowModelIndex) {
                modelIndex += r.getRowCount();
            } else {
                rtn = r;
                break;
            }
        }
        if ( null == rtn) {
            throw new IllegalStateException("rowModeIndex:"+rowModelIndex+" is out of bound");
        }
        return new LtdSrhStruct(rtn,modelIndex);
    }
    private void removeAndAddTableModelListener(LinesTableData gameLine) {
        removeTableModelListener(gameLine);
        gameLine.addTableModelListener(linesTableDataListner);
    }
    private void removeTableModelListener(LinesTableData gameLine) {
        for (TableModelListener l : gameLine.getTableModelListeners()) {
            if ( l instanceof LinesTableDataListner) {
                gameLine.removeTableModelListener(l);
            }
        }
    }
    @Override
    public void setValueAt(Object value,int rowModelIndex, int colModelIndex) {
        throw new IllegalStateException("Pending implementation");
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    public static class LtdSrhStruct {
        public final LinesTableData linesTableData;
        //index of the first row of this linesTableData in tableModel
        public final int offset;
        public LtdSrhStruct(LinesTableData linesTableData,int offset) {
            this.linesTableData = linesTableData;
            this.offset = offset;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////
    public static class BlankGameStruct {
        public final int tableRowModelIndex;
        public final LinesTableData linesTableData;
        public BlankGameStruct(int tableRowModelIndex, LinesTableData linesTableData) {
            this.tableRowModelIndex = tableRowModelIndex;
            this.linesTableData = linesTableData;
        }
    }
    private class LinesTableDataListner implements TableModelListener {

        @Override
        public void tableChanged(final TableModelEvent e) {
            if ( headerInstalled) {
                log("VIALATION:::: table changed event fired AFTER headerIntalled");
//                MainGameTableModel.this.fireTableChanged(e);
            } else {
                MainGameTableModel.this.fireTableChanged(e);
            }
        }
    }
}
