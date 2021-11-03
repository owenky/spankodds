package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.ColumnCustomizableTable;

import javax.swing.event.TableModelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface AbstractScreen<T extends KeyedObject> {

    void destroyMe();
    boolean shouldAddToScreen(T game);
    ColumnCustomizableTable<T> getColumnCustomizableTable();
    default void checktofire(Collection<T> games) {
        ColumnCustomizableTable<T> table = getColumnCustomizableTable();
        ColumnCustomizableDataModel<T> model = table.getModel();
        List<Integer> rowModelIndexList = new ArrayList<>();
        for(T game:games) {
            if ( shouldAddToScreen(game)) {
                TableSection<T> ts = model.checktofire(game, table.isShowing());
                if (null != ts) {
                    int rowModelIndex = model.getRowModelIndex(ts, game.getGame_id());
                    rowModelIndexList.add(rowModelIndex);
                }
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
    }
    default void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet) {
        getColumnCustomizableTable().getModel().removeGamesAndCleanup(gameIdRemovedSet);
    }
}
