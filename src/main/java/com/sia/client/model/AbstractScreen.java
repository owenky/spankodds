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
    void addGame(T game);
    default void checktofire(Collection<T> games) {
        ColumnCustomizableTable<T> table = getColumnCustomizableTable();
        if ( null == table) {
            return;
        }
        ColumnCustomizableDataModel<T> model = table.getModel();
        if ( null == model ) {
            return;

        }
        List<UpdateGameStruct<T>> updateList = new ArrayList<>(games.size());
        List<T> insertList = new ArrayList<>(5);
        for(T game:games) {
            if ( shouldAddToScreen(game)) {
                TableSection<T> ts = model.checktofire(game);
                if (null != ts) {
                    updateList.add(new UpdateGameStruct<>(game.getGame_id(),ts));
                } else {
                    insertList.add(game);
                }
            }
        }
        Runnable edtThreadRunner = () -> {
            //do insert before update
            for(T game: insertList) {
                addGame(game);
            }
            //next do update
            List<Integer> rowModelIndexList = updateList.stream().map(struct->model.getRowModelIndexByGameId(struct.ts, struct.gameId))
                    .collect(Collectors.toList());
            Integer [] rowViewIndexArr = rowModelIndexList.stream().map(table::convertRowIndexToView).toArray(Integer[]::new);
            for( int rowViewIndex: rowViewIndexArr) {
                table.adjustColumnsOnRows(rowViewIndex);
                TableModelEvent e = new TableModelEvent(model, rowViewIndex, rowViewIndex, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
                model.processTableModelEvent(e);
            }
        };
        Utils.checkAndRunInEDT(edtThreadRunner);

    }
    default void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet) {
        getColumnCustomizableTable().getModel().removeGamesAndCleanup(gameIdRemovedSet);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    class UpdateGameStruct<T extends KeyedObject> {
        private int gameId;
        private TableSection<T> ts;
        private UpdateGameStruct(int gameId,TableSection<T> ts) {
            this.gameId = gameId;
            this.ts = ts;
        }
    }
}
