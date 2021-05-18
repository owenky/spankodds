package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.GameTableTableColumnProvider;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainGameTable extends ColumnCustomizableTable<Game> implements LinesTableDataSupplier {

    private boolean isSoccer=false;
    private Map<Integer,Object> rowModelIndex2GameGroupHeaderMap;


    public MainGameTable(GameTableTableColumnProvider columnHeaderProvider) {
        super(false,columnHeaderProvider);
        columnHeaderProvider.setMainGameTable(this);
    }
    @Override
    protected RowHeaderGameTable createNewRowHeaderTable() {
        return new RowHeaderGameTable(this,hasRowNumber());
    }
    public Map<Integer,Object> getRowModelIndex2GameGroupHeaderMap() {
        if ( null == rowModelIndex2GameGroupHeaderMap) {
            Map<Integer,Object> map = getModel().getBlankGameIdIndex().stream().
                    collect(HashMap::new, (m, struct)->m.put(struct.tableRowModelIndex,struct.linesTableData.getGameGroupHeader()), HashMap::putAll);
            rowModelIndex2GameGroupHeaderMap = Collections.unmodifiableMap(map);
        }
        return rowModelIndex2GameGroupHeaderMap;
    }
    @Override
    public MainGameTableModel createDefaultDataModel() {
        return new MainGameTableModel();
    }
    @Override
    public MainGameTableModel getModel() {
        return (MainGameTableModel)super.getModel();
    }
    @Override
    public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colViewIndex) {
        if (isSoccer) {
            return new LineRenderer(SiaConst.SoccerStr);
        } else {
            return new LineRenderer();
        }
    }
    public void addGameLine(LinesTableData gameLine) {
        getModel().addGameLine(gameLine);
    }
    @Override
    public LinesTableData getLinesTableData(int row) {
        return (LinesTableData)getModel().getLinesTableData(row).linesTableData;
    }
    public void optimizeRowHeightsAndGameLineTitles() {
        if (isSoccer) {
            setRowHeight(SiaConst.SoccerRowheight);
        } else {
            setRowHeight(SiaConst.NormalRowheight);
        }
    }
    public boolean isSoccer() {
        return isSoccer;
    }
    @Override
    public void setName(String name) {
        super.setName(name);
        isSoccer = SiaConst.SoccerStr.equalsIgnoreCase(name);
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        if ( e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT ) {
            rowModelIndex2GameGroupHeaderMap = null;
        }
        super.tableChanged(e);
    }
}
