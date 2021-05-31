package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.ColumnCustomizableTable;

import javax.swing.event.TableModelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface AbstractScreen<T extends KeyedObject> {

    void destroyMe();
    ColumnCustomizableTable<T> getColumnCustomizableTable();
    default void checktofire(Collection<Integer> gameIds) {
        ColumnCustomizableTable<T> table = getColumnCustomizableTable();
        ColumnCustomizableDataModel<T> model = table.getModel();
        List<Integer> rowModelIndexList = new ArrayList<>();
        for(int gameId:gameIds) {
            TableSection<T> ts =  model.checktofire(gameId,table.isShowing());
            if ( null != ts ) {
                int rowModelIndex = model.getRowModelIndex(ts,gameId);
                rowModelIndexList.add(rowModelIndex);
            }
        }
        List<Integer> sortedRowModelIndexList = rowModelIndexList.stream().sorted().collect(Collectors.toList());
        Integer [] rowViewIndexArr = rowModelIndexList.stream().map(table::convertRowIndexToView).sorted().toArray(Integer[]::new);
        if ( 0 <sortedRowModelIndexList.size() ) {
            Utils.checkAndRunInEDT(()-> {
                table.adjustColumnsOnRows(rowViewIndexArr);
                TableModelEvent e = new TableModelEvent(model, sortedRowModelIndexList.get(0), sortedRowModelIndexList.get(sortedRowModelIndexList.size()-1), TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
                model.fireTableChanged(e);
            });
        }
//        TableSection<T> ltd = model.checktofire(gameid,table.isShowing());
//        boolean status = (null != ltd);
//        if (status) {
//            GameTableAdjustScheduler.adjustColumn(table, ltd, gameid);
//        }
//        if ( repaint ) {
//            int rowModelIndex = containingTableModel.getRowModelIndex(this, gameId);
//            TableModelEvent e = new TableModelEvent(containingTableModel, rowModelIndex, rowModelIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
//            fire(e);
//        }
    }
}
