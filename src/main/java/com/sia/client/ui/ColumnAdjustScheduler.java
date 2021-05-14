package com.sia.client.ui;

import com.sia.client.config.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.log;

public abstract class ColumnAdjustScheduler {

    private static final long updatePeriodInSeconds = 5L;
    private static final ArrayBlockingQueue<ColAdjustStruct> gameQueue = new  ArrayBlockingQueue<>(10000);
    private static final ScheduledExecutorService excutorService = Executors.newSingleThreadScheduledExecutor();

    static {
        excutorService.scheduleAtFixedRate(ColumnAdjustScheduler::executeGameBatch,0,updatePeriodInSeconds, TimeUnit.SECONDS);
    }
    public static void adjustColumn(MainGameTable mainGameTable,LinesTableData ltd,int gameid) {
        ColAdjustStruct colAdjustStr = new ColAdjustStruct(mainGameTable,ltd,gameid);
        try {
            gameQueue.put(colAdjustStr);
        } catch (InterruptedException e) {
            log(e);
        }
    }
    private static void executeGameBatch() {
        Collection<ColAdjustStruct> buffer = new ArrayList<>();
        gameQueue.drainTo(buffer);
        Map<MainGameTable, List<ColAdjustStruct>> map = buffer.stream().collect(Collectors.groupingBy(struct->struct.mainGameTable));
        map.keySet().forEach((mainTable)-> {
            List<ColAdjustStruct> structs = map.get(mainTable);
            Set<Integer> gameIdSet = structs.stream().map(struct-> mainTable.getModel().getRowModelIndex(struct.ltd,struct.gameid)).collect(Collectors.toSet());
            Integer [] gameIdArr = gameIdSet.toArray(new Integer[0]);
            Utils.checkAndRunInEDT( () ->mainTable.adjustColumnsOnRows(gameIdArr));
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////
    private static class ColAdjustStruct {
        public final MainGameTable mainGameTable;
        public final LinesTableData ltd;
        public final int gameid;
        private ColAdjustStruct(MainGameTable mainGameTable, LinesTableData ltd, int gameid) {
            this.mainGameTable = mainGameTable;
            this.ltd = ltd;
            this.gameid = gameid;
        }
    }
}
