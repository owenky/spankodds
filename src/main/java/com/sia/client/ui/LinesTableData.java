package com.sia.client.ui;

import com.sia.client.model.BestLines;
import com.sia.client.model.Bookie;
import com.sia.client.model.ColumnData;
import com.sia.client.model.Game;
import com.sia.client.model.GameDateSorter;
import com.sia.client.model.GameNumSorter;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Container;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class LinesTableData extends DefaultTableModel implements TableColumnModelListener {

    public Vector<ColumnData> m_columns;
    public String display = AppController.getDisplayType();
    public int period = 0;
    public String sport = "";
    protected SimpleDateFormat m_frm;
    protected Date m_date;
    Vector<Game> gamesVec;
    boolean timesort = false;
    boolean shortteam = false;
    boolean inview = false;
    Vector<String> gamesIdVec;
    //    Object[][] stv;
    boolean showingOpener = false;
    boolean showingPrior = false;
    int lastbookieremoved = 0;
    long cleartime = 100;
    JTable thistable;

    public LinesTableData() {
    }

    public LinesTableData(long cleartime, Vector<Game> gamesVec) {
        m_frm = new SimpleDateFormat("MM/dd/yyyy");
        this.cleartime = cleartime;
        this.gamesVec = gamesVec;

        setInitialData();
    }

    private void setInitialData() {
        try {
            m_date = m_frm.parse("12/18/2004");
        } catch (java.text.ParseException ex) {
            m_date = null;
        }
        m_columns = new Vector<>();

        for (int j = 0; j < LazyInitializer.bookiesVec.size(); j++) {
            Bookie b = LazyInitializer.bookiesVec.get(j);
            int bookieid = b.getBookie_id();

            if (bookieid == 990) {

                m_columns.add(new ColumnData(bookieid, "Details", 30, JLabel.RIGHT));

            } else if (bookieid == 991) {
                m_columns.add(new ColumnData(bookieid, "Time", 40, JLabel.RIGHT));

            } else if (bookieid == 992) {
                m_columns.add(new ColumnData(bookieid, "Gm#", 40, JLabel.LEFT));

            } else if (bookieid == 993) {
                m_columns.add(new ColumnData(bookieid, "Team", 100, JLabel.LEFT));

            } else if (bookieid == 994) {
                m_columns.add(new ColumnData(bookieid, "Chart", 80, JLabel.CENTER));

            } else // 81 is precise for 225.5o-07 icon border thickness of 2
            {
                m_columns.add(new ColumnData(bookieid, b.toString(), 50, JLabel.RIGHT));

            }
        }
        if (timesort) {
            gamesVec.sort(new GameDateSorter().thenComparing(new GameTimeSorter()).thenComparing(new GameNumSorter()));
        } else {
            gamesVec.sort(new GameDateSorter().thenComparing(new GameNumSorter()));
        }

        gamesIdVec = new Vector<>();
        Vector<Vector<Object>> dataVector = new Vector<>(gamesVec.size());
        for (int i = 0; i < gamesVec.size(); i++) {
            Vector<Object> rowData = new Vector<>(LazyInitializer.bookiesVec.size());
            dataVector.add(rowData);
            Game g = gamesVec.get(i);
            String gameid = "" + g.getGame_id();

            gamesIdVec.add(gameid);
            int leagueID = g.getLeague_id();

            for (int j = 0; j < LazyInitializer.bookiesVec.size(); j++) {
                Bookie b = LazyInitializer.bookiesVec.get(j);
                Object value = "";
                try {
                    int bookieid = b.getBookie_id();
                    if (bookieid == 990) {
                        value = new InfoView(Integer.parseInt(gameid));
                    } else if (bookieid == 991) {
                        value = new TimeView(Integer.parseInt(gameid));
                    } else if (bookieid == 992) {
                        if (leagueID == 9) {
                            value = new SoccerGameNumberView(Integer.parseInt(gameid));
                        } else {
                            value = new GameNumberView(Integer.parseInt(gameid));
                        }

                    } else if (bookieid == 993) {
                        value = new TeamView(Integer.parseInt(gameid), shortteam);
                    } else if (bookieid == 994) {
                        if (leagueID == 9) {
                            value = new SoccerChartView(Integer.parseInt(gameid));
                        } else {
                            value = new ChartView(Integer.parseInt(gameid));
                        }
                    } else {
                        if (leagueID == 9) {
                            value = new SoccerSpreadTotalView(bookieid, Integer.parseInt(gameid), cleartime, this);
                        } else {
                            value = new SpreadTotalView(bookieid, Integer.parseInt(gameid), cleartime, this);
                        }

                    }
                } catch (Exception ex) {
                    log(ex);
                }
                rowData.add(value);
            }

            try {

                BestLines.calculatebestall(g.getGame_id(), 0);
                BestLines.calculatebestall(g.getGame_id(), 1);
                BestLines.calculatebestall(g.getGame_id(), 2);
                BestLines.calculatebestall(g.getGame_id(), 5);
                BestLines.calculatebestall(g.getGame_id(), 6);
                BestLines.calculatebestall(g.getGame_id(), 7);
                BestLines.calculatebestall(g.getGame_id(), 8);
                BestLines.calculatebestall(g.getGame_id(), 9);
            } catch (Exception ex) {
                log("error calculating best all " + ex);
            }
        }
        setDataVector(dataVector, m_columns);
    }

    public LinesTableData(String display, int period, long cleartime, Vector<Game> gamesVec, JTable thetable, boolean timesort, boolean shortteam, boolean opener, boolean last) {
        m_frm = new SimpleDateFormat("MM/dd/yyyy");
        this.cleartime = cleartime;
        this.gamesVec = gamesVec;
        this.timesort = timesort;
        this.shortteam = shortteam;
        this.display = display;
        this.period = period;
        thistable = thetable;
        setInitialData();
        if (opener) {
            showOpener();
        } else if (last) {
            showPrior();
        }
    }

    public void showOpener() {
        showingOpener = true;
        showingPrior = false;
        checkAndRunInEDT(() -> fireTableDataChanged());

    }

    public void showPrior() {
        showingOpener = false;
        showingPrior = true;
        checkAndRunInEDT(() -> fireTableDataChanged());
    }

    public boolean isInView() {
        return inview;
    }

    public void setInView(boolean b) {
        inview = b;
    }

    public long getClearTime() {
        return cleartime;
    }

    public void columnAdded(TableColumnModelEvent e) {
        log("COLUMN ADDED!!! ");
        printStv(3);
    }

    public void printStv(int gameid) {
        for (int j = 0; j < getColumnCount(); j++) {
            log("j=" + j + "...." + m_columns.get(j));
        }

    }

    public void columnRemoved(TableColumnModelEvent e) {
        System.out.println("COLUMN Removed!!! " + lastbookieremoved);
        printStv(3);
    }

    public void columnMoved(TableColumnModelEvent e) {
        System.out.println("Column Moved!!!");

    }

    public void columnMarginChanged(ChangeEvent e) {
        log("Column MarginChanged!!!");
    }

    public void columnSelectionChanged(ListSelectionEvent e) {
        System.out.println("Column SelectionChanged!!!");
    }

    public Bookie getBookie(int bookieid) {
        return LazyInitializer.bookies.get("" + bookieid);

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
        checkAndRunInEDT(() -> fireTableDataChanged());
    }

    public String getDisplayType() {
        return display;

    }

    public void setDisplayType(String d) {
        display = d;
        checkAndRunInEDT(() -> fireTableDataChanged());
    }

    public int getPeriodType() {
        return period;

    }

    public void setPeriodType(int d) {
        period = d;
        checkAndRunInEDT(() -> fireTableDataChanged());
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
                        System.out.println("clear exception row=" + i + "..col=" + j + ".." + ex);
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
                        System.out.println("clear exception row=" + i + "..col=" + j + ".." + ex);
                    }
                }

            }
        }
        checkAndRunInEDT(() -> fireTableDataChanged());

    }

    @Override
    public boolean isCellEditable(int nRow, int nCol) {
        return false;
    }

    public void timesort() {
        timesort = true;
        setInitialData();
        JViewport parent = (JViewport) thistable.getParent();
        JScrollPane scrollpane = (JScrollPane) parent.getParent();
        scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
        thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
        Container comp = scrollpane.getParent();
        comp.revalidate();
        checkAndRunInEDT(() -> fireTableDataChanged());

    }

    public void gmnumsort() {
        timesort = false;
        setInitialData();
        JViewport parent = (JViewport) thistable.getParent();
        JScrollPane scrollpane = (JScrollPane) parent.getParent();
        scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
        thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
        Container comp = scrollpane.getParent();
        comp.revalidate();
        checkAndRunInEDT(() -> fireTableDataChanged());
    }

    public void checktofire(String gameid) {

        if (gamesIdVec.contains(gameid)) {
            fire();
        }

    }

    public void fire() {
        //m_table.selectAll();

        checkAndRunInEDT(() -> fireTableDataChanged());


        //System.out.println("fired change inside ltd "+new java.util.Date());
    }

    public Game removeGameId(String gameidtoremove) {

        for (int i = 0; i < gamesVec.size(); i++) {
            Game g = gamesVec.get(i);
            String gameid = "" + g.getGame_id();
            if (gameid.equals(gameidtoremove)) {
                //	System.out.println("will delete it at row "+i);
                //	System.out.println("games size is "+gamesVec.size());

                try {
                    gamesVec.remove(i);
                } catch (Exception ex) {
                    System.out.println("error removing from vector!");
                }

                setInitialData();
                JViewport parent = (JViewport) thistable.getParent();
                JScrollPane scrollpane = (JScrollPane) parent.getParent();
                //	System.out.println("games size after remove "+gamesVec.size());
                scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
                scrollpane.revalidate();
                thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
                //System.out.println("removerow d");
                Container comp = scrollpane.getParent();
                comp.revalidate();

                return g;
            }
        }
        return null; // didn't find it
    }

    public void removeGameIds(String[] gameidstoremove) {
        if (gameidstoremove.length == 1 && gameidstoremove[0].equals("-1")) {
            removeYesterdaysGames();
        } else {

            AppController.disableTabs();

            java.util.List<String> list = Arrays.asList(gameidstoremove);
            Vector gameidstoremovevec = new Vector(list);

            for (Iterator<Game> iterator = gamesVec.iterator(); iterator.hasNext(); ) {

                Game g = iterator.next();
                String gameid = "" + g.getGame_id();

                if (gameidstoremovevec.contains(gameid)) {
                    try {
                        iterator.remove();
                    } catch (Exception ex) {
                        System.out.println("error removing from vector!");
                    }
                }

            }
            //	System.out.println("games size end is "+gamesVec.size());

            setInitialData();
            JViewport parent = (JViewport) thistable.getParent();
            JScrollPane scrollpane = (JScrollPane) parent.getParent();

            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
            scrollpane.revalidate();
            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
            Container comp = scrollpane.getParent();
            comp.revalidate();


            AppController.enableTabs();

        }
    }

    public void removeYesterdaysGames() {
        java.util.Date today = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String d1 = sdf.format(today);
        boolean removal = false;
        AppController.disableTabs();
        //System.out.println("games size start is "+gamesVec.size());
        for (Iterator<Game> iterator = gamesVec.iterator(); iterator.hasNext(); ) {

            Game g = iterator.next();
            String d2 = sdf.format(g.getGamedate());

            if (d1.compareTo(d2) > 0) {
                try {
                    iterator.remove();
                    removal = true;
                } catch (Exception ex) {
                    System.out.println("error removing from vector!");
                }
            }
        }
        //	System.out.println("games size end is "+gamesVec.size()+"..removal="+removal);
        if (removal) {
            setInitialData();
            JViewport parent = (JViewport) thistable.getParent();
            JScrollPane scrollpane = (JScrollPane) parent.getParent();

            scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
            scrollpane.revalidate();
            thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
            Container comp = scrollpane.getParent();
            comp.revalidate();
        }

        AppController.enableTabs();


    }

    public void addGame(Game g, boolean repaint) {
        Hashtable<String, Game> games = AppController.getGames();
        Game g2 = games.get("" + g.getGame_id());

        //if(!gamesVec.contains(g))
        if (g2 != null) {
            System.out.println("games size before add " + gamesVec.size());


            gamesVec.add(g);

            if (repaint) {
                setInitialData();
                JViewport parent = (JViewport) thistable.getParent();
                JScrollPane scrollpane = (JScrollPane) parent.getParent();
                //System.out.println("games size after add "+gamesVec.size());
                scrollpane.setPreferredSize(new Dimension(700, thistable.getRowHeight() * gamesVec.size()));
                scrollpane.revalidate();
                thistable.setPreferredScrollableViewportSize(thistable.getPreferredSize());
                Container comp = scrollpane.getParent();
                comp.revalidate();
            }


        }

    }

    public void rebuild() {
        setInitialData();
    }

    public String getTitle() {
        if (m_date == null) {
            return "Stock Quotes";
        }
        return "Stock Quotes at " + m_frm.format(m_date);
    }

    private static abstract class LazyInitializer {
        private static final Hashtable<String, Bookie> bookies = AppController.getBookies();
        private static final Vector<Bookie> bookiesVec = AppController.getBookiesVec();
    }
}
