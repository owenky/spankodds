package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.BestLines;
import com.sia.client.model.Game;
import com.sia.client.model.GameDateSorter;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameNumSorter;
import com.sia.client.model.Games;
import com.sia.client.model.LineGames;
import com.sia.client.model.SportType;
import com.sia.client.model.TableSection;

import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class LinesTableData extends TableSection<Game> {

    private final boolean shortteam;
    private final SportType sportType;
    boolean showingOpener = false;
    boolean showingPrior = false;
    private String display;
    private int period;
    private boolean timesort;
    private final List<TableColumn> columns;
    private long cleartime;
    private boolean last;
    private boolean opener;

    public LinesTableData(SportType sportType,String display, int period, long cleartime, Vector<Game> gameVec, boolean timesort, boolean shortteam, boolean opener, boolean last, GameGroupHeader gameGroupHeader,List<TableColumn> columns) {
        this(sportType,display, period, cleartime, gameVec, timesort, shortteam, opener, last, gameGroupHeader, AppController.getGames(), columns);
    }
    public LinesTableData(SportType sportType, String display, int period, long cleartime, Vector<Game> gameVec, boolean timesort, boolean shortteam, boolean opener, boolean last, GameGroupHeader gameGroupHeader, Games gameCache, List<TableColumn> columns) {
        super(gameGroupHeader,gameCache, null != gameGroupHeader, gameVec);
        this.sportType = sportType;
        this.cleartime = cleartime;
        this.timesort = timesort;
        this.shortteam = shortteam;
        this.display = display;
        this.period = period;
        this.columns = columns;
        this.last = last;
        this.opener = opener;
        if (opener) {
            showOpener();
        } else if (last) {
            showPrior();
        }
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LinesTableData that = (LinesTableData) o;
        return sportType.equals(that.sportType) && gameGroupHeader.equals(that.gameGroupHeader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sportType, gameGroupHeader);
    }
    public SportType getSportType() {
        return sportType;
    }
    public void showOpener() {
        showingOpener = true;
        showingPrior = false;
    }

    public void showPrior() {
        showingOpener = false;
        showingPrior = true;
    }
    @Override
    protected void setHowHeighIfAbsent(Game g) {
        if ( ! containsDataRow() ) {
            List<Game> games = new ArrayList<>();
            games.add(g);
            int rowHeight = TableUtils.calTableSectionRowHeight(games);
            setRowHeight(rowHeight);
        }
    }
    @Override
    protected void prepareLineGamesForTableModel(LineGames<Game> gamesVec) {
        if (timesort) {
            gamesVec.sort(new GameDateSorter().thenComparing(new GameTimeSorter()).thenComparing(new GameNumSorter()));
        } else {
            gamesVec.sort(new GameDateSorter().thenComparing(new GameNumSorter()));
        }
    }

    @Override
    protected List<Object> makeRowData(Game g) {
        List<Object> rowData = makeGameRowData(g);
        try {
            if (SiaConst.BlankGameId != g.getGame_id()) {
                BestLines.calculatebestall(g.getGame_id(), 0);
                BestLines.calculatebestall(g.getGame_id(), 1);
                BestLines.calculatebestall(g.getGame_id(), 2);
                BestLines.calculatebestall(g.getGame_id(), 5);
                BestLines.calculatebestall(g.getGame_id(), 6);
                BestLines.calculatebestall(g.getGame_id(), 7);
                BestLines.calculatebestall(g.getGame_id(), 8);
                BestLines.calculatebestall(g.getGame_id(), 9);
            }
        } catch (Exception ex) {
            log("error calculating best all " + ex);
        }

        return rowData;
    }

    private List<Object> makeGameRowData(Game g) {
        List<Object> rowData = new ArrayList<>(columns.size());
        int gameid = g.getGame_id();
        for (TableColumn tc : columns) {
            Object value;
            if (SiaConst.BlankGameId == gameid) {
                value = getGameGroupHeader();
            } else {
                value = getCellValue(tc, g);
            }
            rowData.add(value);
        }
        return rowData;
    }

    private Object getCellValue(TableColumn tc, Game g) {
        Integer bookieid = (Integer)tc.getIdentifier();
        if ( null == bookieid) {
            throw new IllegalArgumentException("bookie id not defined for column headerValue:"+tc.getHeaderValue()+", index="+tc.getModelIndex());
        }
        int gameid = g.getGame_id();
        int leagueID = g.getLeague_id();
        Object value;
        if ( 990 == bookieid) {
            value = new InfoView(gameid);
        } else if (bookieid == 991) {
            value = new TimeView(gameid);
        } else if (bookieid == 992) {
            if (leagueID == 9) {
                value = new SoccerGameNumberView(gameid);
            } else {
                value = new GameNumberView(gameid);
            }

        } else if (bookieid == 993) {
            value = new TeamView(gameid, shortteam);
        } else if (bookieid == 994) {
            if (leagueID == 9) {
                value = new SoccerChartView(gameid);
            } else {
                value = new ChartView(gameid);
            }
        } else {
            if (leagueID == SiaConst.SoccerLeagueId) {
                value = new SoccerSpreadTotalView(bookieid, gameid, cleartime, this);
            } else {
                value = new SpreadTotalView(bookieid, gameid, cleartime, this);
            }

        }
        return value;
    }
    public long getClearTime() {
        return cleartime;
    }

    public boolean isShowingPrior() {
        return showingPrior;
    }

    public boolean isShowingOpener() {
        return showingOpener;
    }

    public void showCurrent() {
        showingOpener = false;
        showingPrior = false;
    }

    public String getDisplayType() {
        return display;

    }

    public void setDisplayType(String d) {
        display = d;
        //call fire() outside loop -- 07/03/2021
//        log("suspecious fire() call in setDisplayType()");
//        fire(null);
    }

    public int getPeriodType() {
        return period;

    }

    public void setPeriodType(int d) {
        period = d;
//        log("suspecious fire() call in setPeriodType");
//        fire(null);
    }

    public void clearColors() {
        log("=============cleared at " + new java.util.Date() + "..cols=" + getColumnCount());
        cleartime = new java.util.Date().getTime();
        for (int i = 0; i < getRowCount(); i++) {

            for (int j = 0; j < getColumnCount(); j++) {
                Object obj = getValueAt(i, j);
                if (obj instanceof SpreadTotalView) {
                    SpreadTotalView stv = (SpreadTotalView) obj;
                    try {
                        stv.clearColors(cleartime);
                    } catch (Exception ex) {
                        log("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }
                if (obj instanceof SoccerSpreadTotalView) {
                    SoccerSpreadTotalView stv = (SoccerSpreadTotalView) obj;
                    try {
                        stv.clearColors(cleartime);
                    } catch (Exception ex) {
                        log("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }
                if (obj instanceof ChartView) {
                    ChartView stv = (ChartView) obj;
                    try {
                        stv.clearColors();
                        //log("iam chart");
                    } catch (Exception ex) {
                        log("clear exception row=" + i + "..col=" + j);
                        log(ex);
                    }
                }
                if (obj instanceof SoccerChartView) {
                    SoccerChartView stv = (SoccerChartView) obj;
                    try {
                        stv.clearColors();
                        //log("iam chart");
                    } catch (Exception ex) {
                        log("clear exception row=" + i + "..col=" + j);
                        log(ex);
                    }
                }

            }
        }
//        fire(null);

    }

    public void timesort() {
        timesort = true;
        resetDataVector(); //including sorting gamesVec

    }

    public void gmnumsort() {
        timesort = false;
        resetDataVector(); //including sorting gamesVec
    }
    @Override
    public void removeGameIdsAndCleanup(Collection<Integer> gameidstoremove){
        if ( 1 == gameidstoremove.size()  && gameidstoremove.iterator().next().equals(-1)) {
            removeYesterdaysGames();
        } else {
             super.removeGameIdsAndCleanup(gameidstoremove);
        }
    }

    public void removeYesterdaysGames() {
        java.util.Date today = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d1 = sdf.format(today);
        boolean removal = false;
        //TODO unnecessary call?
log("WARNING: In LinesTableData::removeYesterdaysGames, skip AppController.disableTabs();");
//        AppController.disableTabs();
        for (Iterator<Game> iterator = getGamesIterator(); iterator.hasNext(); ) {

            Game g = iterator.next();
            String d2 = sdf.format(g.getGamedate());

            if (d1.compareTo(d2) > 0) {
                try {
                    iterator.remove();
                    removal = true;
                } catch (Exception ex) {
                    log("error removing from vector!");
                }
            }
        }
        if (removal) {
            resetDataVector(); //including sorting gamesVec
            fire(null);
        }
        AppController.enableTabs();
    }
    public boolean getTimesort() {
        return timesort;
    }
    public boolean getShortteam() {
        return shortteam;
    }
    public boolean getOpener() {
        return opener;
    }
    public boolean getLast() {
        return last;
    }
}
