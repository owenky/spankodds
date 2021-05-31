package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.BestLines;
import com.sia.client.model.Bookie;
import com.sia.client.model.Game;
import com.sia.client.model.GameDateSorter;
import com.sia.client.model.GameNumSorter;
import com.sia.client.model.Games;
import com.sia.client.model.LineGames;
import com.sia.client.model.TableSection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.log;

public class LinesTableData extends TableSection<Game> {

    protected final SimpleDateFormat m_frm;
    private final boolean shortteam;
//    public Vector<ColumnData> m_columns;
    public String sport = "";
    protected Date m_date;
    boolean showingOpener = false;
    boolean showingPrior = false;
    private String display;
    private int period;
    private boolean timesort;
    private final Vector<Bookie> bookieVector;
    private long cleartime;

//    public LinesTableData(long cleartime, Vector<Game> gameVec, String gameGroupHeader) {
//        this(AppController.getDisplayType(), 0, cleartime, gameVec, false, false, false, false, gameGroupHeader);
//    }

    public LinesTableData(String display, int period, long cleartime, Vector<Game> gameVec, boolean timesort, boolean shortteam, boolean opener, boolean last, String gameGroupHeader) {
        this(display, period, cleartime, gameVec, timesort, shortteam, opener, last, gameGroupHeader, AppController.getGames(), LazyInitializer.bookiesVec);
    }
//    public LinesTableData(Vector<Game> gameVec, Vector<Bookie> bookieVector, Games gameCache, String gameGroupHeader) {
//        this(AppController.getDisplayType(), 0, 100L, gameVec, false, false, false, false, gameGroupHeader, gameCache, bookieVector);
//    }

    public LinesTableData(String display, int period, long cleartime, Vector<Game> gameVec, boolean timesort, boolean shortteam, boolean opener, boolean last, String gameGroupHeader, Games gameCache, Vector<Bookie> bookieVector) {
        super(gameCache, null != gameGroupHeader, gameVec);
        m_frm = new SimpleDateFormat("MM/dd/yyyy");
        this.cleartime = cleartime;
        setGameGroupHeader(gameGroupHeader);
        this.timesort = timesort;
        this.shortteam = shortteam;
        this.display = display;
        this.period = period;
        this.bookieVector = bookieVector;
        if (opener) {
            showOpener();
        } else if (last) {
            showPrior();
        }
    }

    public void showOpener() {
        showingOpener = true;
        showingPrior = false;
//        log("suspecious fire() call in showOpener() ");
//        fire(null);
    }

    public void showPrior() {
        showingOpener = false;
        showingPrior = true;
//        log("suspecious fire call in showPrior()");
//        fire(null);
    }
//    @Override
//    public Vector<ColumnData> getColumnData() {
//        if (null == m_columns) {
//            try {
//                m_date = m_frm.parse("12/18/2004");
//            } catch (java.text.ParseException ex) {
//                m_date = null;
//            }
//            m_columns = new Vector<>();
//
//            for (Bookie b : bookieVector) {
//                int bookieid = b.getBookie_id();
//
//                if (bookieid == 990) {
//
//                    m_columns.add(new ColumnData(bookieid, "Details", 30, JLabel.RIGHT));
//
//                } else if (bookieid == 991) {
//                    m_columns.add(new ColumnData(bookieid, "Time", 40, JLabel.RIGHT));
//
//                } else if (bookieid == 992) {
//                    m_columns.add(new ColumnData(bookieid, "Gm#", 40, JLabel.LEFT));
//
//                } else if (bookieid == 993) {
//                    m_columns.add(new ColumnData(bookieid, "Team", 100, JLabel.LEFT));
//
//                } else if (bookieid == 994) {
//                    m_columns.add(new ColumnData(bookieid, "Chart", 80, JLabel.CENTER));
//
//                } else // 81 is precise for 225.5o-07 icon border thickness of 2
//                {
//                    m_columns.add(new ColumnData(bookieid, b.toString(), 50, JLabel.RIGHT));
//
//                }
//            }
//        }
//        return m_columns;
//    }
    @Override
    public void addGame(Game g, boolean repaint) {
       super.addGame(g,repaint);
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
        List<Object> rowData = new ArrayList<>(bookieVector.size());
        int gameid = g.getGame_id();
        for (Bookie b : bookieVector) {
            Object value;
            if (SiaConst.BlankGameId == gameid) {
                value = SiaConst.GameGroupHeaderIden;
            } else {
                value = getCellValue(b, g);
            }
            rowData.add(value);
        }
        return rowData;
    }

    private Object getCellValue(Bookie b, Game g) {
        int bookieid = b.getBookie_id();
        int gameid = g.getGame_id();
        int leagueID = g.getLeague_id();
        Object value;
        if (bookieid == 990) {
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
            if (leagueID == 9) {
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
//        log("suspecious fire() call in showCurrent()");
//        fire(null);
    }

    public String getDisplayType() {
        return display;

    }

    public void setDisplayType(String d) {
        display = d;
        log("suspecious fire() call in setDisplayType()");
        fire(null);
    }

    public int getPeriodType() {
        return period;

    }

    public void setPeriodType(int d) {
        period = d;
        log("suspecious fire() call in setPeriodType");
        fire(null);
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
                        System.out.println("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }
                if (obj instanceof ChartView) {
                    ChartView stv = (ChartView) obj;
                    try {
                        stv.clearColors();
                        //System.out.println("iam chart");
                    } catch (Exception ex) {
                        System.out.println("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }
                if (obj instanceof SoccerChartView) {
                    SoccerChartView stv = (SoccerChartView) obj;
                    try {
                        stv.clearColors();
                        //System.out.println("iam chart");
                    } catch (Exception ex) {
                        log("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }

            }
        }
//        fire(null);

    }

    public void timesort() {
        timesort = true;
//        setInitialData();
//        JViewport parent = (JViewport) thistable.getParent();
//        JScrollPane scrollpane = (JScrollPane) parent.getParent();
//        scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//        thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//        Container comp = scrollpane.getParent();
//        comp.revalidate();
//        checkAndRunInEDT(() -> fireTableDataChanged());
        resetDataVector(); //including sorting gamesVec
//        fire(null);

    }

    public void gmnumsort() {
        timesort = false;
//        setInitialData();
//        JViewport parent = (JViewport) thistable.getParent();
//        JScrollPane scrollpane = (JScrollPane) parent.getParent();
//        scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//        thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//        Container comp = scrollpane.getParent();
//        comp.revalidate();
//        checkAndRunInEDT(() -> fireTableDataChanged());
        resetDataVector(); //including sorting gamesVec
//        fire(null);
    }

    public void removeGameIds(String[] gameidstoremove) {
        if (gameidstoremove.length == 1 && gameidstoremove[0].equals("-1")) {
            removeYesterdaysGames();
        } else {
                //TODO unnecessary call?
            log("WARNING: In LinesTableData, skip AppController.disableTabs();");
//            AppController.disableTabs();

            List<String> list = Arrays.asList(gameidstoremove);
            Vector<String> gameidstoremovevec = new Vector<>(list);
            for (Iterator<Game> iterator = getGamesIterator(); iterator.hasNext(); ) {

                Game g = iterator.next();
                String gameid = "" + g.getGame_id();

                if (gameidstoremovevec.contains(gameid)) {
                    try {
                        iterator.remove();
                    } catch (Exception ex) {
                        log(ex);
                    }
                }
            }

//            setInitialData();
//            JViewport parent = (JViewport) thistable.getParent();
//            JScrollPane scrollpane = (JScrollPane) parent.getParent();
//
//            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//            scrollpane.revalidate();
//            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//            Container comp = scrollpane.getParent();
//            comp.revalidate();
//            AppController.enableTabs();
            resetDataVector(); //including sorting gamesVec
            fire(null);
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
//            setInitialData();
//            JViewport parent = (JViewport) thistable.getParent();
//            JScrollPane scrollpane = (JScrollPane) parent.getParent();
//
//            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
//            scrollpane.revalidate();
//            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
//            Container comp = scrollpane.getParent();
//            comp.revalidate();
            resetDataVector(); //including sorting gamesVec
            fire(null);
        }
        AppController.enableTabs();
    }

    public String getTitle() {
        if (m_date == null) {
            return "Stock Quotes";
        }
        return "Stock Quotes at " + m_frm.format(m_date);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitializer {
        //        private static final Hashtable<String, Bookie> bookies = AppController.getBookies();
        private static final Vector<Bookie> bookiesVec = AppController.getBookiesVec();
    }
}
