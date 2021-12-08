package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LineRenderer;
import com.sia.client.ui.LinesTableData;

import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.sia.client.config.Utils.log;
import static java.lang.Boolean.parseBoolean;

public class ScreenGameModel {

    private final ScreenProperty screenProperty;
    private final SportType sportType;
    private Vector<TableColumn> allColumns;
    private Map<GameGroupHeader, LinesTableData> headerMap;
    private int maxlength;

    public ScreenGameModel(ScreenProperty screenProperty,SportType sportType) {
        this.screenProperty = screenProperty;
        this.sportType = sportType;
    }
    public void build() {

        GameGroupAggregator gameGroupAggregator = new GameGroupAggregator(sportType,true);
        Map<GameGroupHeader, Vector<Game>> headerListMap = gameGroupAggregator.aggregate();
        updateCurrentMaxLength(headerListMap);
        allColumns = createColumns();
        headerMap = new HashMap<>(headerListMap.size());

        boolean timesort = GameUtils.isTimeSort(screenProperty.getSpankyWindowConfig().isTimesort(),sportType.isTimeSort());
        log("timesort?=" + timesort + "..allgames size=" + headerListMap.size());

        headerListMap.keySet().forEach(gameGroupHeader-> {
            Vector<Game> games = headerListMap.get(gameGroupHeader);
            LinesTableData tableSection = createLinesTableData(games, gameGroupHeader);
            headerMap.put(gameGroupHeader,tableSection);
        });

        long ct = AppController.getClearAllTime();
        if (ct > screenProperty.getCleartime()) {
            screenProperty.setCleartime(ct);
        }
    }
    private void updateCurrentMaxLength(Map<GameGroupHeader, Vector<Game>> headerMap) {
        screenProperty.setCurrentmaxlength(0);
        headerMap.values().stream().flatMap(Collection::stream)
                .forEach(g-> {
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
    private Vector<TableColumn> createColumns() {
        Vector<Bookie> newBookiesVec = AppController.getBookiesVec();
        List<Bookie> hiddencols = AppController.getHiddenCols();
        Vector<TableColumn> result = new Vector<>(newBookiesVec.size());

        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = newBookiesVec.get(k);

            if (hiddencols.contains(b)) {
                continue;
            }
            TableColumn column;

            column = new TableColumn(k, 30, new LineRenderer(), null);

            column.setHeaderValue(b.getShortname());
            column.setIdentifier(b.getBookie_id());
            if (b.getBookie_id() == 990) {
                column.setPreferredWidth(60);
            } else if (b.getBookie_id() == 994) {
                column.setPreferredWidth(80);
            } else if (b.getBookie_id() == 991) {
                column.setPreferredWidth(40);
            } else if (b.getBookie_id() == 992) {
                column.setPreferredWidth(45);
            } else if (b.getBookie_id() == 993) {
                if (screenProperty.getSpankyWindowConfig().isShortteam()) {
                    column.setPreferredWidth(30);
                } else {
                    column.setPreferredWidth(screenProperty.getCurrentmaxlength() * 7);
                }

            } else if (b.getBookie_id() > 1000) {
                column.setMinWidth(10);
                column.setPreferredWidth(65);
            } else {
                column.setMinWidth(10);
                column.setPreferredWidth(30);
            }
            result.add(column);
        }

        return result;
    }
    public Collection<LinesTableData> getTableSections() {
        return headerMap.values();
    }
    public Vector<TableColumn> getAllTableColumns() {
        return allColumns;
    }
    private void setShowProperties(String[] prefs) {
        screenProperty.setShowheaders(parseBoolean(prefs[3]));
        sportType.setShowseries(parseBoolean(prefs[4]));
        sportType.setShowingame(parseBoolean(prefs[5]));
        sportType.setShowAdded(parseBoolean(prefs[6]));
        sportType.setShowExtra(parseBoolean(prefs[7]));
        sportType.setShowProps(parseBoolean(prefs[8]));
    }
    public LinesTableData createLinesTableData(Vector<Game> newgamegroupvec, GameGroupHeader gameGroupHeader) {
        LinesTableData tableSection = new LinesTableData(sportType,screenProperty, newgamegroupvec, gameGroupHeader, allColumns);
        if (sportType.equals(SportType.Soccer)) {
            tableSection.setRowHeight(SiaConst.SoccerRowheight);
        }
        return tableSection;
    }
    public Map<GameGroupHeader, LinesTableData> getHeaderMap() {
        return this.headerMap;
    }
    public int getMaxlength() {
        return maxlength;
    }
    private int calcmaxlength(String s1, String s2) {
        return Math.max(s1.length(), s2.length());
    }
}