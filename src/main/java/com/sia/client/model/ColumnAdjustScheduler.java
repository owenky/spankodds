package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.ColumnCustomizableTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public class ColumnAdjustScheduler {

    private static final long DefaultUpdatePeriodInSeconds = 5L;
    private final ArrayBlockingQueue<TableModelRowData> rowDataQueue = new  ArrayBlockingQueue<>(10000);
    private final ScheduledExecutorService excutorService = Executors.newSingleThreadScheduledExecutor();
    private long updatePeriodInSeconds;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    public ColumnAdjustScheduler() {
        updatePeriodInSeconds =  DefaultUpdatePeriodInSeconds;
    }
    public void setUpdatePeriodInSeconds(long updatePeriodInSeconds){
        this.updatePeriodInSeconds = updatePeriodInSeconds;
    }
    public void addRowData(TableModelRowData rowData) {
        if ( isStarted.compareAndSet(false,true)) {
            excutorService.scheduleAtFixedRate(this::executeGameBatch,0,updatePeriodInSeconds, TimeUnit.SECONDS);
        }
        try {
            rowDataQueue.put(rowData);
        } catch (InterruptedException e) {
            log(e);
        }
    }
    private void executeGameBatch() {
        Collection<TableModelRowData> buffer = new ArrayList<>();
        rowDataQueue.drainTo(buffer);
        Map<ColumnCustomizableTable, List<TableModelRowData>> map = buffer.stream().distinct()
                .collect(Collectors.groupingBy(TableModelRowData::getTable));
        map.keySet().forEach((mainTable)-> {
            List<TableModelRowData> structs = map.get(mainTable);
            Set<Integer> gameIdSet = structs.stream().map(TableModelRowData::getRowModelIndex).collect(Collectors.toSet());
            Integer [] rowIndexArr = gameIdSet.toArray(new Integer[0]);
            Utils.checkAndRunInEDT( () ->mainTable.adjustColumnsOnRows(rowIndexArr));
        });
    }
}
