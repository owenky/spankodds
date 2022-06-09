package com.sia.client.ui;

import com.sia.client.config.SiaConst.SportName;
import com.sia.client.model.*;
import com.sia.client.ui.control.SportsTabPane;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class AppController {

    public final static AlertVector alertsVector = new AlertVector();
    public final static Set<Integer> BadGameIds = new HashSet<>();
    private static final AtomicReference<CountDownLatch> messageProcessingLatchRef = new AtomicReference<>(new CountDownLatch(1));
    public static Vector<String> customTabsVec = new Vector<>();
    public static Vector<LineAlertNode> linealertnodes = new Vector<>();
    public static Vector<SportsMenuBar> menubars = new Vector<>();
    public static Vector<Sport> sportsVec = new Vector<>();
    public static String brokerURL = "failover:(tcp://71.172.25.164:61616)";
    public static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
    public static Connection guestConnection;
    public static Connection loggedInConnection;
    public static String loginQueue = "spankodds.LOGIN";
    public static String userPrefsQueue = "spankodds.USERPREFS";
    public static String linechangesQueue = "spankoddsin.LINECHANGE";
    public static String gamechangesQueue = "spankoddsin.GAMECHANGE";
    public static String scorechangesQueue = "spankoddsin.SCORECHANGE";
    public static String urgentQueue = "spankoddsin.URGENT";
    public static String displaytype = "default";
    public static LinesConsumer linesConsumer;
    public static GamesConsumer gamesConsumer;
    public static ScoresConsumer scoresConsumer;
    public static UrgentsConsumer urgentsConsumer;
    public static UserPrefsProducer userPrefsProducer;
    public static ChartChecker chartchecker;
    public static long clearalltime;
    public static DefaultTableColumnModel columnmodel;
    public static DefaultTableColumnModel fixedcolumnmodel;
    public static NewWindowAction nwa;
    public static LineOpenerAlertNode football = new LineOpenerAlertNode(SportName.Football);
    public static LineOpenerAlertNode basketball = new LineOpenerAlertNode(SportName.Basketball);
    public static LineOpenerAlertNode baseball = new LineOpenerAlertNode(SportName.Baseball);
    public static LineOpenerAlertNode hockey = new LineOpenerAlertNode(SportName.Hockey);
    public static LineOpenerAlertNode soccer = new LineOpenerAlertNode(SportName.Soccer);
    public static LineOpenerAlertNode fighting = new LineOpenerAlertNode(SportName.Fighting);
    public static LineOpenerAlertNode golf = new LineOpenerAlertNode(SportName.Golf);
    public static LineOpenerAlertNode tennis = new LineOpenerAlertNode(SportName.Tennis);
    public static LineOpenerAlertNode autoracing = new LineOpenerAlertNode(SportName.Auto_Racing);
    public static List<LineOpenerAlertNode> LineOpenerAlertNodeList = new ArrayList<>();

    public static LimitNode footballlimitnode = new LimitNode(SportName.Football);
    public static LimitNode basketballlimitnode = new LimitNode(SportName.Basketball);
    public static LimitNode baseballlimitnode = new LimitNode(SportName.Baseball);
    public static LimitNode hockeylimitnode = new LimitNode(SportName.Hockey);
    public static LimitNode soccerlimitnode = new LimitNode(SportName.Soccer);
    public static LimitNode fightinglimitnode = new LimitNode(SportName.Fighting);
    public static LimitNode golflimitnode = new LimitNode(SportName.Golf);
    public static LimitNode tennislimitnode = new LimitNode(SportName.Tennis);
    public static LimitNode autoracinglimitnode = new LimitNode(SportName.Auto_Racing);
    public static List<LimitNode> LimitNodeList = new ArrayList<>();



    public static SortedMap<Integer, String> SpotsTabPaneVector = new TreeMap<>();
    public static Vector<String> SportsTabPaneVector = new Vector<>();
    private static Map<String, String> customTabsHash = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> h1spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> h1totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> h1moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> h1teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> h2spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> h2totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> h2moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> h2teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> q1spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> q1totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> q1moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> q1teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> q2spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> q2totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> q2moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> q2teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> q3spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> q3totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> q3moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> q3teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> q4spreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> q4totals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> q4moneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> q4teamtotals = new ConcurrentHashMap<>();
    private static Map<String, Spreadline> livespreads = new ConcurrentHashMap<>();
    private static Map<String, Totalline> livetotals = new ConcurrentHashMap<>();
    private static Map<String, Moneyline> livemoneylines = new ConcurrentHashMap<>();
    private static Map<String, TeamTotalline> liveteamtotals = new ConcurrentHashMap<>();
    private static Map<Integer, Color> bookiecolors = new ConcurrentHashMap<>();
    private static Map<Integer, Sport> leagueIdToSportMap = new HashMap<>();

    public static Map<String, Bookie> bestvisitspread = new ConcurrentHashMap<>();
    public static Map<String, Bookie> besthomespread = new ConcurrentHashMap<>();
    public static Map<String, Bookie> bestvisitml = new ConcurrentHashMap<>();
    public static Map<String, Bookie> besthomeml = new ConcurrentHashMap<>();
    public static Map<String, Bookie> bestdrawml = new ConcurrentHashMap<>();
    public static Map<String, Bookie> bestover = new ConcurrentHashMap<>();
    public static Map<String, Bookie> bestunder = new ConcurrentHashMap<>();

    private static final BookieManager bookieManager = BookieManager.instance();
    private static final Games games = Games.instance();
    private static final List<NewLineListener> NEW_LINE_LISTENERS = new ArrayList<>();

    public static void initializeSportsTabPaneVectorFromUser() {
        String[] tabsindex = User.instance().getTabsIndex().split(",");
        for (int i = 0; i < tabsindex.length; i++) {
            SpotsTabPaneVector.put(i, tabsindex[i]);
            // will be using this instead
            SportsTabPaneVector.add(tabsindex[i]);

        }
    }

    public static void signalWindowLoading() {
        synchronized (messageProcessingLatchRef) {
            CountDownLatch messageProcessingLatch = messageProcessingLatchRef.get();
            if (0 == messageProcessingLatch.getCount()) {
                messageProcessingLatchRef.set(new CountDownLatch(1));
            }
        }
    }

    public static boolean isReadyForMessageProcessing() {
        CountDownLatch messageProcessingLatch = messageProcessingLatchRef.get();
        return 0 == messageProcessingLatch.getCount();

    }

    public static void notifyWindowLoadingComplete() {
        synchronized (messageProcessingLatchRef) {
            CountDownLatch messageProcessingLatch = messageProcessingLatchRef.get();
            messageProcessingLatch.countDown();
        }

    }

    public static void waitForSpankyWindowLoaded() {
        CountDownLatch messageProcessingLatch = messageProcessingLatchRef.get();
        try {
            messageProcessingLatch.await();
//            Thread.sleep(1000*3600*5);
        } catch (InterruptedException e) {
            log(e);
        }
    }

    public static boolean existLeagueId(Integer leagueId) {
        return leagueIdToSportMap.containsKey(leagueId);
    }

    public static void initializeLineAlertVectorFromUser() {
        if(User.instance().getLineAlerts() == null || User.instance().getLineAlerts().equals("") || User.instance().getLineAlerts().equals("a"))
        {
            return;
        }
        String[] linealerts = User.instance().getLineAlerts().split("\\?");
        for (final String linealert : linealerts) {
            try {
                String[] lanitems = linealert.split("!");
                String[] sportselected = lanitems[3].split(",");
                String[] bookieselected = lanitems[4].split(",");
                for (int k = 0; k < sportselected.length; k++) {
                    log("sport" + k + "=" + sportselected[k]);
                }
                List<String> sportselectedvec = new ArrayList<>(Arrays.asList(sportselected));
                List<String> bookieselectedvec = new ArrayList<>(Arrays.asList(bookieselected));
                int j = 5;
                LineAlertNode lan2 = new LineAlertNode(
                        lanitems[0],
                        lanitems[1],
                        Integer.parseInt(lanitems[2]),
                        sportselectedvec,
                        bookieselectedvec,
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        lanitems[j++],
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),

                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        lanitems[j++],
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),

                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        lanitems[j++],
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),


                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++]),
                        Boolean.parseBoolean(lanitems[j++]),
                        lanitems[j++],
                        Boolean.parseBoolean(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Integer.parseInt(lanitems[j++]),
                        Double.parseDouble(lanitems[j++])
                );

                linealertnodes.add(lan2);
            } catch (Exception ex) {
                log("exception loading in line alert=" + linealert);
                log(ex);
            }

        }
    }

    public static void initializSpotsTabPaneVector() {
    }

    public static Vector<String> getMainTabVec() {
        Collection<String> al = SpotsTabPaneVector.values();
        Iterator<String> itr = al.iterator();
        Vector<String> vec = new Vector<>();
        while (itr.hasNext()) {
            vec.add(itr.next());

        }
        return vec;


    }

    public static void createLineOpenerAlertNodeList() {
        LineOpenerAlertNodeList.add(football);
        LineOpenerAlertNodeList.add(basketball);
        LineOpenerAlertNodeList.add(baseball);
        LineOpenerAlertNodeList.add(hockey);
        LineOpenerAlertNodeList.add(soccer);
        LineOpenerAlertNodeList.add(fighting);
        LineOpenerAlertNodeList.add(golf);
        LineOpenerAlertNodeList.add(tennis);
        LineOpenerAlertNodeList.add(autoracing);
    }
    public static void createLimitNodeList() {
        LimitNodeList.add(footballlimitnode);
        LimitNodeList.add(basketballlimitnode);
        LimitNodeList.add(baseballlimitnode);
        LimitNodeList.add(hockeylimitnode);
        LimitNodeList.add(soccerlimitnode);
        LimitNodeList.add(fightinglimitnode);
        LimitNodeList.add(golflimitnode);
        LimitNodeList.add(tennislimitnode);
        LimitNodeList.add(autoracinglimitnode);
    }
    public static void createLineOpenerAlertNodeListFromUserPrefs() {

        String openerdata = getUser().getOpeneralert();
        if(openerdata == null || openerdata.equals(""))
        {
            return;
        }
        String lans[] = openerdata.split("~");
        for(int i = 0;i < lans.length; i++)
        {
            String[] items = lans[i].split("\\|");
            if(items.length > 1)
            {

                 String[] sportcodeselements = items[1].split(",\\s* ");
                List<String> sportcodeslist = Arrays.asList(sportcodeselements);
                ArrayList<String> sportcodesarraylist = new ArrayList<String>(sportcodeslist);

                String[] bookieselements = items[2].split(",\\s* ");
                List<String> bookieslist = Arrays.asList(bookieselements);
                ArrayList<String> bookiesarraylist = new ArrayList<String>(bookieslist);


                LineOpenerAlertNode lan = new LineOpenerAlertNode(
                        items[0],
                        sportcodesarraylist,
                        bookiesarraylist,
                        Boolean.parseBoolean(items[3]),
                        Boolean.parseBoolean(items[4]),
                        Boolean.parseBoolean(items[5]),
                        Boolean.parseBoolean(items[6]),
                        Boolean.parseBoolean(items[7]),
                        Boolean.parseBoolean(items[8]),
                        Boolean.parseBoolean(items[9]),
                        Boolean.parseBoolean(items[10]),
                        Boolean.parseBoolean(items[11]),
                        Boolean.parseBoolean(items[12]),
                        Boolean.parseBoolean(items[13]),
                        Boolean.parseBoolean(items[14]),
                        Boolean.parseBoolean(items[15]),
                        Integer.parseInt(items[16]),
                        Integer.parseInt(items[17]),
                        Double.parseDouble(items[18]),
                        items[19],
                        Boolean.parseBoolean(items[20]));
                log("about to add opener="+lan);
                LineOpenerAlertNodeList.set(i,lan);

            }
        }

    }

    public static void createLimitNodeListFromUserPrefs() {

        String limitdata = getUser().getLimitchangeAlert();
        String lans[] = limitdata.split("@");
        for(int i = 0;i < lans.length; i++)
        {
            String[] items = lans[i].split("\\|");
            if(items.length > 1)
            {

                String[] sportcodeselements = items[1].split(",\\s* ");
                List<String> sportcodeslist = Arrays.asList(sportcodeselements);
                ArrayList<String> sportcodesarraylist = new ArrayList<String>(sportcodeslist);

                String[] bookieselements = items[2].split(",\\s* ");
                List<String> bookieslist = Arrays.asList(bookieselements);
                ArrayList<String> bookiesarraylist = new ArrayList<String>(bookieslist);


                LimitNode lan = new LimitNode(
                        items[0],
                        sportcodesarraylist,
                        bookiesarraylist,
                        Boolean.parseBoolean(items[3]), //fg check
                        Boolean.parseBoolean(items[4]),
                        Boolean.parseBoolean(items[5]),
                        Boolean.parseBoolean(items[6]),
                        Boolean.parseBoolean(items[7]),
                        Boolean.parseBoolean(items[8]),
                        Boolean.parseBoolean(items[9]),
                        Boolean.parseBoolean(items[10]),
                        Boolean.parseBoolean(items[11]),
                        Boolean.parseBoolean(items[12]),
                        Boolean.parseBoolean(items[13]), //isttcheck
                        Boolean.parseBoolean(items[14]),
                        Boolean.parseBoolean(items[15]),
                        Boolean.parseBoolean(items[16]),
                        Boolean.parseBoolean(items[17]),
                        Integer.parseInt(items[18]),
                        Integer.parseInt(items[19]),
                        Double.parseDouble(items[20]),
                        items[21],
                        items[22],
                        Integer.parseInt(items[23]));






                log("about to add limitnode="+lan);
                LimitNodeList.set(i,lan);

            }
        }

    }
    public static void addLineAlertNode(LineAlertNode lan) {

        checkAndRunInEDT(() -> linealertnodes.add(lan));
    }

    public static void removeLineAlertNode(LineAlertNode lan) {

        checkAndRunInEDT(() -> linealertnodes.remove(lan));
    }

    public static void replaceLineAlertNode(LineAlertNode oldlan, LineAlertNode newlan) {

        checkAndRunInEDT(() -> Collections.replaceAll(linealertnodes, oldlan, newlan));
    }

    public static boolean isLineAlertNameAvailable(String name) {
        for (int i = 0; i < linealertnodes.size(); i++) {
            String lanname = (linealertnodes.elementAt(i)).getName();
            if (lanname.equalsIgnoreCase(name)) {
                return false;
            }

        }
        return true;
    }

    public static TableColumnModel getColumnModel() {
        if (columnmodel == null) {
            createColumnModel();
        }
        return columnmodel;
    }

    public static void createColumnModel() {
        List<Bookie> newBookiesVec = getBookiesVec();
        columnmodel = new DefaultTableColumnModel();
        fixedcolumnmodel = new DefaultTableColumnModel();
        for (int k = 0; k < newBookiesVec.size(); k++) {
            Bookie b = newBookiesVec.get(k);
            LineRenderer lr = new LineRenderer();
            //renderers.add(lr);
            TableColumn column;

            column = new TableColumn(k, 30, lr, null);
            column.setHeaderValue(b.getShortname() + "");
            column.setIdentifier("" + b.getBookie_id());
            if (b.getBookie_id() == 990) {
                column.setPreferredWidth(54);
            } else if (b.getBookie_id() == 991) {
                column.setPreferredWidth(40);
            } else if (b.getBookie_id() == 992) {
                column.setPreferredWidth(45);
            }
            else if (b.getBookie_id() == 995) {
                column.setPreferredWidth(45);
            }else if (b.getBookie_id() == 993) {

                column.setPreferredWidth(150);

            } else {
                //column.setPreferredWidth(50) ;
                column.setMinWidth(10);
                column.setPreferredWidth(10);
            }
            columnmodel.addColumn(column);

        }
        // here i'm adding a blank column
        TableColumn blankcol = new TableColumn(newBookiesVec.size(), 10, null, null);
        blankcol.setHeaderValue("");
        blankcol.setIdentifier("9999999");

        blankcol.setMaxWidth(30);
        blankcol.setPreferredWidth(30);
        columnmodel.addColumn(blankcol);


        for (int i = 0; i < AppController.getNumFixedCols(); i++) {
            TableColumn column = columnmodel.getColumn(0);
            columnmodel.removeColumn(column);
            fixedcolumnmodel.addColumn(column);
        }


    }

    public static List<Bookie> getBookiesVec() {
        return bookieManager.getBookiesVec();
    }

    public static int getNumFixedCols() {
        return bookieManager.getNumFixedCols();
    }

    public static void setNumFixedCols(int x) {
        bookieManager.setNumFixedCols(x);
    }

    public static int reorderBookiesVec() {
        return bookieManager.reorderBookiesVec();
    }

    public static TableColumnModel getFixedColumnModel() {
        if (fixedcolumnmodel == null) {
            createColumnModel();
        }
        return fixedcolumnmodel;
    }

    public static NewWindowAction getNewWindowAction() {
        if (nwa == null) {
            nwa = new NewWindowAction();
        }
        return nwa;
    }
    public static Vector<String> getCustomTabsVec() {
        return customTabsVec;
    }
    public static Vector<LineAlertNode> getLineAlertNodes() {
        return linealertnodes;
    }
    public static SportsTabPane getMainTabPane() {
        return SpankyWindow.getFirstSpankyWindow().getSportsTabPane();
    }

    public static void enableTabs() {
        SpankyWindow.applyToAllWindows(SportsTabPane::enableTabs);
    }

    public static void addFrame(SpankyWindow f) {
        addMenuBar((SportsMenuBar) f.getJMenuBar());
    }

    public static void addMenuBar(SportsMenuBar smb) {
        menubars.add(smb);

    }

    public static void removeFrame(SpankyWindow f) {
        removeMenuBar((SportsMenuBar) f.getJMenuBar());
        SportsTabPane stb = f.getSportsTabPane();
        stb.cleanup();
//        removeTabPane(stb);
        SpankyWindow.removeWindow(f);
        if (0 == SpankyWindow.openWindowCount()) {
            log("exiting..");
            System.exit(0);
        }
    }

    public static void removeMenuBar(SportsMenuBar smb) {
        menubars.remove(smb);

    }

    public static void removeCustomTab(String key) {
        String val = customTabsHash.get(key);
        if (val != null) {
            int index = customTabsVec.indexOf(val);
            if (index != -1) {
                customTabsVec.removeElementAt(index);
            }
        }
        customTabsHash.remove(key);
        repaintmenubars();
    }

    public static void repaintmenubars() {
        for (int i = 0; i < menubars.size(); i++) {
            SportsMenuBar smb = menubars.elementAt(i);
            log("smb=" + smb);
            smb.populateTabsMenu();

        }

    }

    public static String getTabInfo(String tabname) {
        return customTabsHash.get(tabname);
    }

    public static void refreshTabs3() {
        //when multiple windows opened, there are multiple tabpanes, each window has one tabpane.
        //game need to populated to each window. -- 08/22/2021
        SpankyWindow.applyToAllWindows((stp) -> {
            try {
                stp.rebuildMainScreen();
            } catch (Exception e) {
                log(e);
            }
        });
    }

    public static void clearAll() {
        SpankyWindow.applyToAllWindows(SportsTabPane::clearAll);
    }

    public static void fireAllTableDataChanged(Collection<Game> games) {
        SpankyWindow.applyToAllWindows((stp) -> stp.fireAllTableDataChanged(games));
    }

    public static String getLoginQueue() {
        return loginQueue;
    }

    public static String getUserPrefsQueue() {
        return userPrefsQueue;
    }

    public static ActiveMQConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public static long getClearAllTime() {
        return clearalltime;
    }

    public static void setClearAllTime(long x) {
        clearalltime = x;
    }

    public static Connection createLoggedInConnection(String un, String pw) {
        try {
            if (loggedInConnection == null) {
                loggedInConnection = connectionFactory.createConnection(un, pw);
            }
        } catch (Exception e) {
            log("error establishing loggedin connection! ");
            log(e);
        }
        return loggedInConnection;
    }

    public static Connection getLoggedInConnection() {
        return loggedInConnection;
    }


    public static void createLinesConsumer() {
        try {
            linesConsumer = new LinesConsumer(connectionFactory, loggedInConnection, linechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! lines consumer");
            log(e);
        }

    }

    public static void createScoresConsumer() {
        try {
            scoresConsumer = new ScoresConsumer(connectionFactory, loggedInConnection, scorechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! scores consumer");
            log(e);
        }

    }

    public static void createUrgentsConsumer() {
        try {
            urgentsConsumer = new UrgentsConsumer(connectionFactory, loggedInConnection, urgentQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! urgents consumer");
            log(e);
        }

    }

    public static void createGamesConsumer() {
        try {
            gamesConsumer = new GamesConsumer(connectionFactory, loggedInConnection, gamechangesQueue);
        } catch (Exception e) {
            log("error establishing loggedin connection! games consumer ");
            log(e);
        }

    }

    public static void createChartChecker() {
        try {
            chartchecker = ChartChecker.instance();
        } catch (Exception e) {
            log("error creating chartchecker " + e);
            log(e);
        }

    }

    public static ChartChecker getChartChecker() {
        return chartchecker;
    }

    public static UserPrefsProducer getUserPrefsProducer() {
        if (userPrefsProducer == null) {
            log("creating userprefsproducer!!!");
            createUserPrefsProducer();
        }
        return userPrefsProducer;
    }

    public static void createUserPrefsProducer() {
        try {
            userPrefsProducer = new UserPrefsProducer();
        } catch (Exception e) {
            log(e);
        }

    }

    public static Connection getGuestConnection() {

        try {
            if (guestConnection == null) {
                guestConnection = connectionFactory.createConnection("guest", "spank0dds4ever");
            }
        } catch (Exception e) {
            log("error establishing guest connection! ");
            log(e);
        }
        return guestConnection;
    }

    public static Integer getBookieId(String sn) {
        return bookieManager.getBookieId(sn);
    }

    public static User getUser() {
        return User.instance();
    }

    public static void enrichUserProperties(User u) {

        //here i will load in all prefs
        String columncolors = u.getColumnColors();

        String[] colcolorsarray = columncolors.split(",");
        for (String colcolor : colcolorsarray) {
            if (colcolor.contains("=")) {
                Integer id = Integer.parseInt(colcolor.substring(0, colcolor.indexOf("=")));
                String color = colcolor.substring(colcolor.indexOf("=") + 1);
                try {
                    log("BOOKIECOLORS " + id + "=" + color);
                    log(Color.decode(color));
                    bookiecolors.put(id, Color.decode(color));
                } catch (Exception ex) {
                    log(ex);
                }

            }

        }

        String customtabs = u.getCustomTabs();

        String[] customtabsarray = customtabs.split("\\?");
        for (final String s : customtabsarray) {
            try {
                String[] tabelements = s.split("\\*");
                addCustomTab(tabelements[1], s);
            } catch (Exception ex) {
                log(ex);
            }

        }


    }

    public static void addCustomTab(String key, String s) {
        String val = customTabsHash.get(key);
        if (val != null) {
            int index = customTabsVec.indexOf(val);
            if (index != -1) {
                customTabsVec.set(index, s);
            }
        } else {
            customTabsVec.add(s);
        }
        customTabsHash.put(key, s);
        repaintmenubars();
    }

    public static void addBookie(Bookie b) {
        bookieManager.addBookie(b);
    }
    public static void changeBookieShortName(String oldShortName,String newShortName) {
        bookieManager.changeBookieShortName(oldShortName,newShortName);
    }
    public static void addOpenerBookie(Bookie b) {
        bookieManager.addOpenerBookie(b);
    }

    public static void addSport(Sport s) {

        leagueIdToSportMap.put(s.getLeague_id(), s);
        sportsVec.add(s);
    }

    public static void removeGameDate(String date, String leagueid) {
        //here i will get all teh game ids for a given date and leagueid and
        // then make an array out of it and call removegames
        removeGamesAndCleanup(getAllGamesForThisDateAndLeagueId(date, leagueid));

    }

    public static void removeGamesAndCleanup(String[] gameidarr) {
        //when multiple windows opened, there are multiple tabpanes, each window has one tabpane.
        //game need to populated to each window. -- 08/22/2021
        Set<Integer> gameIdRemovedSet = Arrays.stream(gameidarr).map(Integer::parseInt).collect(Collectors.toSet());
        List<CountDownLatch> latches = new ArrayList<>(SpankyWindow.openWindowCount());
        Iterator<SpankyWindow> spankyWindowIterator = SpankyWindow.getAllSpankyWindows();
        while (spankyWindowIterator.hasNext()) {
            SportsTabPane stb = spankyWindowIterator.next().getSportsTabPane();
            CountDownLatch latch = new CountDownLatch(1);
            latches.add(latch);
            stb.removeGamesAndCleanup(gameIdRemovedSet, latch);
        }
        if (gameidarr.length == 1 && gameidarr[0].equals("-1")) {
            return;
        }
        for (CountDownLatch latch : latches) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log(e);
            }
        }
        for (final String gameIdStr : gameidarr) {
            try {
                games.removeGame(gameIdStr);
                Iterator<Bookie> iterator = bookieManager.iterator();
                while (iterator.hasNext() ) {
                    Bookie b = iterator.next();
                    int bid = b.getBookie_id();
                    final String key = bid + "-" + gameIdStr;
                    spreads.remove(key);
                    totals.remove(key);
                    moneylines.remove(key);
                    teamtotals.remove(key);
                    h1spreads.remove(key);
                    h1totals.remove(key);
                    h1moneylines.remove(key);
                    h1teamtotals.remove(key);
                    h2spreads.remove(key);
                    h2totals.remove(key);
                    h2moneylines.remove(key);
                    h2teamtotals.remove(key);
                    q1spreads.remove(key);
                    q1totals.remove(key);
                    q1moneylines.remove(key);
                    q1teamtotals.remove(key);
                    q2spreads.remove(key);
                    q2totals.remove(key);
                    q2moneylines.remove(key);
                    q2teamtotals.remove(key);
                    q3spreads.remove(key);
                    q3totals.remove(key);
                    q3moneylines.remove(key);
                    q3teamtotals.remove(key);
                    q4spreads.remove(key);
                    q4totals.remove(key);
                    q4moneylines.remove(key);
                    q4teamtotals.remove(key);
                    livespreads.remove(key);
                    livetotals.remove(key);
                    livemoneylines.remove(key);
                    liveteamtotals.remove(key);

                }
            } catch (Exception ex) {
                log(ex);
            }
        }
    }

    public static String[] getAllGamesForThisDateAndLeagueId(String date, String leagueid) {
        Iterator<Game> itr = games.iterator();
        Vector<String> gameidstodelete = new Vector<String>();
        while (itr.hasNext()) {
            Game g = itr.next();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String gamedate = sdf.format(g.getGamedate());
            if (date.equals(gamedate)) // we have a match on date
            {
                if (!leagueid.equals("")) {
                    int lid = g.getLeague_id();
                    int lid2 = Integer.parseInt(leagueid);
                    if (lid == lid2) {
                        gameidstodelete.add(g.getGame_id() + "");
                        log("will be deleting .." + g.getGame_id());
                    }
                } else {
                    gameidstodelete.add(g.getGame_id() + "");
                    log("will be deleting .." + g.getGame_id());
                }
            }
        }
        return (gameidstodelete.toArray(new String[gameidstodelete.size()]));

    }

    public static void pushGameToCache(Game g) {
        games.updateOrAdd(g);
    }

    public static void moveGameToThisHeader(Game g, GameGroupHeader header) {
        SpankyWindow.applyToAllWindows((stp) -> stp.moveGameToThisHeader(g, header));
    }

    public static Bookie getBookie(int bid) {
        return bookieManager.getBookie(bid);
    }

    public static Sport getSportByLeagueId(int leagueId) {
        return leagueIdToSportMap.get(leagueId);
    }

    public static Game getGame(int gid) {
        return games.getGame(gid);
    }


    public static Games getGames() {
        return games;
    }

    public static Color getColor(Integer bookieid) {
        return bookiecolors.get(bookieid);
    }

    public static void putColor(Integer bookieid, Color color) {
        bookiecolors.put(bookieid, color);
    }

    public static Set<Integer> getColorBookieIds() {
        return bookiecolors.keySet();
    }

    public static List<Sport> getSportsVec() {
        return sportsVec;
    }

    public static List<Bookie> getHiddenCols() {
        return bookieManager.getHiddenCols();
    }

    public static List<Bookie> getShownCols() {
        return bookieManager.getShownCols();
    }

    public static List<Bookie> getFixedCols() {
        return bookieManager.getFixedCols();
    }

    public static Games getGamesVec() {
        return games;
    }

    public static void addSpreadline(Spreadline spread) {
        int period = spread.getPeriod();

        if (period == 0) {
            spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 1) {
            h1spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 2) {
            h2spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 5) {
            q1spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 6) {
            q2spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 7) {
            q3spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else if (period == 8) {
            q4spreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
        } else {
            livespreads.put(spread.getBookieid() + "-" + spread.getGameid(), spread);
            log("unknown spread period " + period + "...." + spread.getBookieid() + "-" + spread.getGameid());
        }
        newLineNotify(spread);
        //LineAlertOpeners.spreadOpenerAlert(spread);
    }

    public static void addTotalline(Totalline total) {


        int period = total.getPeriod();
        if (period == 0) {
            totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 1) {
            h1totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 2) {
            h2totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 5) {
            q1totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 6) {
            q2totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 7) {
            q3totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else if (period == 8) {
            q4totals.put(total.getBookieid() + "-" + total.getGameid(), total);
        } else {
            livetotals.put(total.getBookieid() + "-" + total.getGameid(), total);
            log("unknown total period " + period + "...." + total.getBookieid() + "-" + total.getGameid());
        }
        newLineNotify(total);
        //LineAlertOpeners.totalOpenerAlert(total);

    }

    public static void addMoneyline(Moneyline ml) {


        int period = ml.getPeriod();
        if (period == 0) {
            moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 1) {
            h1moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 2) {
            h2moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 5) {
            q1moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 6) {
            q2moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 7) {
            q3moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else if (period == 8) {
            q4moneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
        } else {
            livemoneylines.put(ml.getBookieid() + "-" + ml.getGameid(), ml);
            log("unknown money period " + period + "...." + ml.getBookieid() + "-" + ml.getGameid());
        }
        newLineNotify(ml);
//LineAlertOpeners.moneyOpenerAlert(ml);
    }

    public static void addTeamTotalline(TeamTotalline teamtotal) {

        int period = teamtotal.getPeriod();
        if (period == 0) {
            teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 1) {
            h1teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 2) {
            h2teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 5) {
            q1teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 6) {
            q2teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 7) {
            q3teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else if (period == 8) {
            q4teamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
        } else {
            liveteamtotals.put(teamtotal.getBookieid() + "-" + teamtotal.getGameid(), teamtotal);
            log("unknown tt period " + period + "...." + teamtotal.getBookieid() + "-" + teamtotal.getGameid());
        }
        newLineNotify(teamtotal);
//LineAlertOpeners.teamTotalOpenerAlert(teamtotal);
    }

    public static Spreadline getSpreadline(int b, int g, int period) {
        if (period == 0) {
            return spreads.get(b + "-" + g);
        } else if (period == 1) {
            return h1spreads.get(b + "-" + g);
        } else if (period == 2) {
            return h2spreads.get(b + "-" + g);
        } else if (period == 5) {
            return q1spreads.get(b + "-" + g);
        } else if (period == 6) {
            return q2spreads.get(b + "-" + g);
        } else if (period == 7) {
            return q3spreads.get(b + "-" + g);
        } else if (period == 8) {
            return q4spreads.get(b + "-" + g);
        } else {
            return livespreads.get(b + "-" + g);
        }
    }

    public static Totalline getTotalline(int b, int g, int period) {

        if (period == 0) {
            return totals.get(b + "-" + g);
        } else if (period == 1) {
            return h1totals.get(b + "-" + g);
        } else if (period == 2) {
            return h2totals.get(b + "-" + g);
        } else if (period == 5) {
            return q1totals.get(b + "-" + g);
        } else if (period == 6) {
            return q2totals.get(b + "-" + g);
        } else if (period == 7) {
            return q3totals.get(b + "-" + g);
        } else if (period == 8) {
            return q4totals.get(b + "-" + g);
        } else {
            return livetotals.get(b + "-" + g);
        }
    }

    public static Moneyline getMoneyline(int b, int g, int period) {

        if (period == 0) {
            return moneylines.get(b + "-" + g);
        } else if (period == 1) {
            return h1moneylines.get(b + "-" + g);
        } else if (period == 2) {
            return h2moneylines.get(b + "-" + g);
        } else if (period == 5) {
            return q1moneylines.get(b + "-" + g);
        } else if (period == 6) {
            return q2moneylines.get(b + "-" + g);
        } else if (period == 7) {
            return q3moneylines.get(b + "-" + g);
        } else if (period == 8) {
            return q4moneylines.get(b + "-" + g);
        } else {
            return livemoneylines.get(b + "-" + g);
        }
    }

    public static TeamTotalline getTeamTotalline(int b, int g, int period) {

        TeamTotalline rtn;
        if (period == 0) {
            rtn = teamtotals.get(b + "-" + g);
        } else if (period == 1) {
            rtn = h1teamtotals.get(b + "-" + g);
        } else if (period == 2) {
            rtn = h2teamtotals.get(b + "-" + g);
        } else if (period == 5) {
            rtn = q1teamtotals.get(b + "-" + g);
        } else if (period == 6) {
            rtn = q2teamtotals.get(b + "-" + g);
        } else if (period == 7) {
            rtn = q3teamtotals.get(b + "-" + g);
        } else if (period == 8) {
            rtn = q4teamtotals.get(b + "-" + g);
        } else {
            rtn = liveteamtotals.get(b + "-" + g);
        }
        return rtn;
    }

    public static String getCurrentHoursMinutes() {
        Calendar calendar = Calendar.getInstance();

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        return "" + hours + "hr," + minutes + "min";

    }

    public static void addAlert(String hrmin, String mesg) {
        alertsVector.addAlert(hrmin, mesg);
    }
    public static void addNewLineListener(NewLineListener newLineListener) {
        NEW_LINE_LISTENERS.add(newLineListener);
    }
    public static void rmNewLineListener(NewLineListener newLineListener) {
        NEW_LINE_LISTENERS.remove(newLineListener);
    }
    private static final Set<Integer> gameIdsWithAnyLine = new HashSet<>();
    private static void newLineNotify(Line newLine) {
        //since MainGameTableModel is removed from NEW_LINE_LISTENERS when it is switched off , NEW_LINE_LISTENERS only contains one MainGameTableModel that is currently displayed.
        //So no need to filter for displayed model. 2022-02-27
        for (NewLineListener newLineListener : NEW_LINE_LISTENERS) {
            newLineListener.processNewLines(newLine);
        }
        gameIdsWithAnyLine.add(newLine.getGameid());
    }
    public static boolean containAnyLine(int gameId) {
        return gameIdsWithAnyLine.contains(gameId);
    }
}