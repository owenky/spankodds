package com.sia.client.model;

import com.sia.client.config.Config;
import com.sia.client.config.GameUtils;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LinesTableData;

import java.util.*;
import java.util.function.Function;

import static com.sia.client.config.Utils.log;

public class ScreenGameModel {

    private final ScreenProperty screenProperty;
    private final SportType sportType;
    private BookieColumnModel bookieColumnModel;
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
        bookieColumnModel = BookieColumnsImpl.instance(sportType.getSportName());
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
    public LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType, screenProperty, newgamegroupvec, gameGroupHeader, bookieColumnModel);
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

    public BookieColumnModel getBookieColumnModel() {
        return bookieColumnModel;
    }
    public Map<GameGroupHeader, LinesTableData> getHeaderMap() {
        return this.headerMap;
    }

    public int getMaxlength() {
        return maxlength;
    }
}
