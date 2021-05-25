package com.sia.client.ui.simulator;

import com.sia.client.model.KeyedObject;

import java.util.ArrayList;
import java.util.List;

public class TestGame implements KeyedObject {

    private int gameId;
    public int colCount;
    private List<Object> rowData;
    public TestGame() {

    }
    public void setColCount(int colCount) {
        this.colCount = colCount;
    }
    @Override
    public int getGame_id() {
        return gameId;
    }
    @Override
    public void setGame_id(final int gameId) {
        this.gameId = gameId;
        rowData = new ArrayList<>(colCount);
        for(int i = 0;i<colCount;i++) {
            rowData.add(gameId + "_" + i);
        }
    }
    public List<Object> getRowData() {
        return rowData;
    }
}
