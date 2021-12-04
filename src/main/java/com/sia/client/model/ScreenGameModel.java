package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;
import com.sia.client.ui.LineRenderer;
import com.sia.client.ui.LinesTableData;

import javax.swing.table.TableColumn;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
        allColumns = createColumns();
        headerMap = new HashMap<>();
        screenProperty.setCurrentmaxlength(0);

        Games allgames = AppController.getGamesVec();
        if (sportType.isPredifined()) {
            enrichSportType();
        } else {
            sportType.setComingDays(-1);
            sportType.setLeagueFilter(null);
        }
        boolean timesort = GameUtils.isTimeSort(screenProperty.getSpankyWindowConfig().isTimesort(),sportType.isTimeSort());
        log("timesort?=" + timesort + "..allgames size=" + allgames.size());
        Iterator<Game> ite = allgames.iterator();
        while ( ite.hasNext()) {
            Game g = ite.next();
            if (!sportType.shouldSelect(g)) {
                continue;
            }
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

            GameGroupHeader gameGroupHeader;
            GameStatus status = GameStatus.getGameStatus(g);
            if ( null != status) {
                gameGroupHeader = status.getGroupHeader();
            } else {
                gameGroupHeader = GameUtils.createGameGroupHeader(g);
            }

            LinesTableData tableSection = headerMap.computeIfAbsent(gameGroupHeader,(key)-> createLinesTableData(new Vector<>(), key));
            tableSection.addOnInit(g);
        }
        long ct = AppController.getClearAllTime();
        if (ct > screenProperty.getCleartime()) {
            screenProperty.setCleartime(ct);
        }
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
    private void enrichSportType() {
        String[] prefs = sportType.getUserPerf().split("\\|");
        String[] tmp = {};
        boolean all = false;
        sportType.setComingDays(Integer.parseInt(prefs[1]));
        if (parseBoolean(prefs[0])) {
            sportType.setTimeSort(true);
//            this.timesort = true;
        }

        try {
            if (prefs.length > 2) {
                tmp = prefs[2].split(",");
                if (tmp[0].equalsIgnoreCase(sportType.getSportName())) {
                    all = true;
                }
                setShowProperties(prefs);
            }

        } catch (Exception ex) {
            log(ex);
        }
        sportType.setLeagueFilter(new LeagueFilter(tmp, all));
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
