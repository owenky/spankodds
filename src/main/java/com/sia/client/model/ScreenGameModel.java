package com.sia.client.model;

import com.sia.client.config.ColumnSettings;
import com.sia.client.config.Config;
import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LineRenderer;
import com.sia.client.ui.LinesTableData;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.*;
import java.util.function.Function;

import static com.sia.client.config.Utils.log;

public class ScreenGameModel {

    private final ScreenProperty screenProperty;
    private final SportType sportType;
    private List<TableColumn> allColumns;
    private Map<GameGroupHeader, LinesTableData> headerMap;
    private int maxlength;

    public ScreenGameModel(ScreenProperty screenProperty, SportType sportType) {
        this.screenProperty = screenProperty;
        this.sportType = sportType;
    }

    public void build() {

        Function<Game,Boolean> gameFilter = sportType::shouldSelect;
        GameGroupAggregator gameGroupAggregator = new GameGroupAggregator(sportType, gameFilter,true);
        Map<GameGroupHeader, Vector<Game>> headerListMap = gameGroupAggregator.aggregate();
        updateCurrentMaxLength(headerListMap);
        allColumns = createColumns();
        headerMap = new HashMap<>(headerListMap.size());

        boolean timesort = GameUtils.isTimeSort(screenProperty.getSpankyWindowConfig().isTimesort(), sportType.isTimeSort());
        log("timesort?=" + timesort + "..allgames size=" + headerListMap.size());

        headerListMap.keySet().forEach(gameGroupHeader -> {
            Vector<Game> games = headerListMap.get(gameGroupHeader);
            LinesTableData tableSection = createLinesTableData(games, gameGroupHeader);
            headerMap.put(gameGroupHeader, tableSection);
        });

        long ct = AppController.getClearAllTime();
        if (ct > screenProperty.getCleartime()) {
            screenProperty.setCleartime(ct);
        }

    }

    private void updateCurrentMaxLength(Map<GameGroupHeader, Vector<Game>> headerMap) {
        screenProperty.setCurrentmaxlength(0);
        headerMap.values().stream().flatMap(Collection::stream)
                .forEach(g -> {
                    if (screenProperty.getSpankyWindowConfig().isShortteam()) {
                        maxlength = calcmaxlength(g.getShortvisitorteam(), g.getShorthometeam());
                    } else {
                        maxlength = calcmaxlength(g.getVisitorteam(), g.getHometeam());
                    }
                    if (maxlength > screenProperty.getCurrentmaxlength()) {
                        screenProperty.setCurrentmaxlength(maxlength);
                    }

                    if (null == g.getStatus()) {
                        g.setStatus("");
                    }
                    if (null == g.getTimeremaining()) {
                        g.setTimeremaining("");
                    }
                });
    }

    private List<TableColumn> createColumns() {
        List<Bookie> newBookiesVec = AppController.getBookiesVec();
        List<Bookie> hiddencols = AppController.getHiddenCols();
        List<TableColumn> result = new ArrayList<>(newBookiesVec.size()+1);

        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = newBookiesVec.get(k);

            if (hiddencols.contains(b)) {
                continue;
            }
            TableColumn column;

            column = new TableColumn(k+1, 30, null, null) {
                @Override
                public TableCellRenderer getCellRenderer() {
                    //deferred construction of TableCellRenderer to avoid EDT vialation when building model in worker thread -- 2021-12-12
                    if (null == cellRenderer) {
                        cellRenderer = new LineRenderer();
                    }
                    return cellRenderer;
                }
            };

            column.setHeaderValue(b.getShortname());
            column.setIdentifier(b.getBookie_id());
            ColumnSettings columnSettings = Config.instance().getColumnSettings();
            column.setPreferredWidth(columnSettings.getColumnWidth(column.getHeaderValue()));
            result.add(column);
        }

        return result;
    }
    public LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType, screenProperty, newgamegroupvec, gameGroupHeader, allColumns);
        if (sportType.equals(SportType.Soccer)) {
            tableSection.setRowHeight(Config.instance().getFontConfig().getSoccerRowHeight());
        }
        return tableSection;
    }

    private int calcmaxlength(String s1, String s2) {
        if ( null == s1) {
            s1="";
        }
        if ( null == s2) {
            s2="";
        }
        return Math.max(s1.length(), s2.length());
    }

    public Collection<LinesTableData> getTableSections() {
        return headerMap.values();
    }

    public List<TableColumn> getAllTableColumns() {
        return allColumns;
    }
    public Map<GameGroupHeader, LinesTableData> getHeaderMap() {
        return this.headerMap;
    }

    public int getMaxlength() {
        return maxlength;
    }
}
