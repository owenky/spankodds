package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.Game;
import com.sia.client.model.LinesTableDataSupplier;
import com.sia.client.model.MainGameTableModel;

import javax.swing.table.TableCellRenderer;

public class MainGameTable extends ColumnCustomizableTable<Game> implements LinesTableDataSupplier {

    private boolean isSoccer=false;
    private final LineRenderer soccerLineRenderer = new LineRenderer(SiaConst.SoccerStr);
    private final LineRenderer lineRenderer = new LineRenderer();

    public MainGameTable(MainGameTableModel tm) {
        super(false,tm);
    }
    @Override
    protected RowHeaderGameTable createNewRowHeaderTable() {
        return new RowHeaderGameTable(this,hasRowNumber());
    }
    @Override
    public MainGameTableModel getModel() {
        return (MainGameTableModel)super.getModel();
    }
    @Override
    public TableCellRenderer getUserCellRenderer(int rowViewIndex, int colViewIndex) {
        if (isSoccer) {
            return soccerLineRenderer;
        } else {
            return lineRenderer;
        }
    }
    @Override
    public LinesTableData getLinesTableData(int row) {
        return (LinesTableData)super.getLinesTableData(row);
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
}
