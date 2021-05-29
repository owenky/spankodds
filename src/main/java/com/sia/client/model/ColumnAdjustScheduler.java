package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.ColumnCustomizableTable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ColumnAdjustScheduler {

    private final MessageConsumingScheduler<TableModelRowData> delegator;
    public ColumnAdjustScheduler() {
        delegator = new MessageConsumingScheduler<>(messageConsumer());
        //this scheduler is ONLY called from GameMessageProcessor::createConsumer, which pools message id. Don't need another layer of pooling so set update period to -1 to disable scheduling and process request immediately. 05/29/2021
        delegator.setUpdatePeriodInMilliSeconds(-1L);
    }
    public void addRowData(TableModelRowData rowData) {
        delegator.addMessage(rowData);
    }
    private Consumer<List<TableModelRowData>> messageConsumer() {

        return (buffer)-> {
            Map<ColumnCustomizableTable<?>, List<TableModelRowData>> map = buffer.stream().distinct()
                    .collect(Collectors.groupingBy(TableModelRowData::getTable));
            map.keySet().forEach((mainTable)-> {
                List<TableModelRowData> structs = map.get(mainTable);
                Set<Integer> gameIdSet = structs.stream().map(TableModelRowData::getRowModelIndex).collect(Collectors.toSet());
                Integer [] rowIndexArr = gameIdSet.toArray(new Integer[0]);
                Utils.checkAndRunInEDT( () ->mainTable.adjustColumnsOnRows(rowIndexArr));
            });
        };
    }
}
