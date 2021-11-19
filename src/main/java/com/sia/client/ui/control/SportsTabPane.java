package com.sia.client.ui.control;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.GameIdSorter;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.model.SportType;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.MainScreenRefresh;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.TestExecutor;
import com.sia.client.ui.AppController;
import com.sia.client.ui.CustomTab2;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.SportCustomTab;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class SportsTabPane extends JTabbedPane implements Cloneable {

//    public long cleartime = 1000;
    private ImageIcon loadgif = null;
    private JLabel loadlabel;
    private SportsTabPane thispane;
    private int previousTabIndex = -1;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private boolean dragging = false;
    private final Map<String, MainScreen> mainScreenMap = new HashMap<>();
    private final SpankyWindowConfig spankyWindowConfig;

    public SportsTabPane(int windowIndex) {
        thispane = this;
        loadlabel = new JLabel("loading...", loadgif, JLabel.CENTER);
        loadlabel.setOpaque(true);
        spankyWindowConfig = new SpankyWindowConfig(windowIndex,false,false);
        spankyWindowConfig.setGameComparator(new GameIdSorter());
        addListeners();
    }
    public void showCurrent() {
        spankyWindowConfig.showCurrent();
    }
    public void showPrior() {
        spankyWindowConfig.showPrior();
    }
    public void showOpener() {
        spankyWindowConfig.showOpener();
    }
    public int getWindowIndex() {
        return spankyWindowConfig.getWindowIndex();
    }
    public void populateComponents() {

        List<String> maintabs = AppController.getMainTabVec();
        for (String title : maintabs) {
            SportType st = SportType.findBySportName(title);
            if (null != st) {
                URL imgResource = Utils.getMediaResource(st.getIcon());
                MainScreen ms = new MainScreen(st,spankyWindowConfig);
                addMainScreenToCache(ms);
                addTab(title, new ImageIcon(imgResource),ms , title);
            }
        }

        List<String> customtabs = AppController.getCustomTabsVec();
        for (int i = 0; i < customtabs.size(); i++) {
            String name = "";
            boolean showheaders = true;
            boolean showingame = true;
            boolean showseries = true;
            boolean showadded = true;
            boolean showextra = true;
            boolean showprops = true;
            String msstring = customtabs.get(i);
            log("customtab=" + msstring);
            String[] items = msstring.split("\\*");
            List<String> customheaders = new ArrayList<>();
            for (int j = 0; j < items.length; j++) {
                log(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (String header : headers) {
                        if ( ! "".equals(header)) {
                            customheaders.add(header);
                            log("adding header=" + header);
                        }
                    }
                } else if (j == 1) {
                    name = items[j];
                } else if (j == 2) {
                    showheaders = Boolean.parseBoolean(items[j]);
                } else if (j == 3) {
                    showseries = Boolean.parseBoolean(items[j]);
                } else if (j == 4) {
                    showingame = Boolean.parseBoolean(items[j]);
                } else if (j == 5) {
                    showadded = Boolean.parseBoolean(items[j]);
                } else if (j == 6) {
                    showextra = Boolean.parseBoolean(items[j]);
                } else if (j == 7) {
                    showprops = Boolean.parseBoolean(items[j]);
                }

            }
            List<String> customheaderStrList = GameUtils.convertLeagueIdHeaderToGameGroupHeaderStr(customheaders);
            SportType customerizedSportType =  SportType.createCustomizedSportType(name,customheaderStrList);
            MainScreen msnew = new MainScreen(customerizedSportType,spankyWindowConfig,showheaders, showseries, showingame, showadded, showextra, showprops);
            addMainScreenToCache(msnew);
            addTab(name, null, msnew, name);
        }
        addTab("+", null, null, "+");

        log("initializing SportTabPane instance:"+ spankyWindowConfig.getWindowIndex());
        doTest();
    }
    private void doTest() {
        if (InitialGameMessages.shouldRunSimulator) {
            TestExecutor testExecutor;
//                testExecutor= new MoveToFinal(model);
//                testExecutor = new ScoreChangeProcessorTest(null);
            testExecutor = new MainScreenRefresh(this);
            if ( testExecutor.isValid()) {
                testExecutor.start();
            }
        }
        if (InitialGameMessages.getMessagesFromLog) {
            Executors.newFixedThreadPool(1).submit(OngoingGameMessages::loadMessagesFromLog);
        }
    }
    public MainScreen createMainScreen(SportType st) {
        MainScreen ms = new MainScreen(st,spankyWindowConfig);
        addMainScreenToCache(ms);
        return ms;
    }
    private void addMainScreenToCache(MainScreen ms) {
        mainScreenMap.put(ms.getSportType().getSportName(),ms);
    }
    public MainScreen findMainScreen(String sportName) {
        return mainScreenMap.get(sportName);
    }
    private void addListeners() {

        this.addChangeListener(ce -> {

            int currentTabIndex = thispane.getSelectedIndex();
            boolean isPlusTab = currentTabIndex == thispane.getTabCount() - 1;
            if ( previousTabIndex >=0 && previousTabIndex < thispane.getTabCount() - 1 && ! isPlusTab) {
                Component previousComp = getComponentAt(previousTabIndex);
                if ( previousComp instanceof MainScreen) {
                    MainScreen oldms = (MainScreen) previousComp;
                    oldms.destroyMe();
                }
            }

            int restoredIdex = previousTabIndex;
            //previousTabIndex must be set before restore to previouse index
            previousTabIndex = currentTabIndex;
            log(" Current tab is:" + currentTabIndex + "..tabcount=" + thispane.getTabCount());
            log(" Previous tab is:" + previousTabIndex);
            if (thispane.getTabCount() > 1 && isPlusTab) {
                log(currentTabIndex + "stateChanged" + (currentTabIndex == getTabCount() - 1));
                new CustomTab2(getWindowIndex());
                //restore to previouse index -- 2021-11-18
                setSelectedIndex(restoredIdex);
            } else if (getTabCount() > 1 && previousTabIndex == getTabCount() - 1) {
                // do nothing
            } else {

                Component currentComp = getComponentAt(currentTabIndex);
                if ( currentComp instanceof MainScreen) {
                    MainScreen newms = (MainScreen) currentComp;
                    log("changelistener create!");
                    reCreateMainScreen(newms);
                }
            }
        });


        this.addMouseListener(new MouseAdapter() {


            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {
                    log("button 1 clicked");
                } else if (e.getButton() == MouseEvent.BUTTON3) {

                    TabbedPaneUI ui = thispane.getUI();
                    int tabindex = ui.tabForCoordinate(thispane, e.getX(), e.getY());
                    log("button 3 clicked " + tabindex);
                    String TabName = thispane.getTitleAt(tabindex);


                    if (!(TabName.equals("Football") || TabName.equals("Basketball") || TabName.equals("Baseball") || TabName.equals("Hockey") || TabName.equals("Fighting") || TabName.equals(SiaConst.SoccerStr) || TabName.equals("Auto Racing") || TabName.equals("Golf") || TabName.equals("Tennis")) && !thispane.getTitleAt(tabindex).equals("+") && !thispane.getTitleAt(tabindex).equals("Today")) {
                        log("tab clicked is " + tabindex + "src/main" + thispane.getTitleAt(tabindex));
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem editItem = new JMenuItem("Edit " + thispane.getTitleAt(tabindex));
                        JMenuItem removeItem = new JMenuItem("Remove " + thispane.getTitleAt(tabindex));
                        jPopupMenu.add(editItem);
                        jPopupMenu.add(removeItem);
                        editItem.addActionListener(e1 -> checkAndRunInEDT(() -> {
                            new CustomTab2(getWindowIndex(),thispane.getTitleAt(tabindex), tabindex);

                        }));
                        removeItem.addActionListener(e12 -> checkAndRunInEDT(() -> {
                            AppController.removeCustomTab(thispane.getTitleAt(tabindex));
                            SpankyWindow.applyToAllWindows((tp)-> {
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            });

                        }));
                        jPopupMenu.show(thispane, e.getX(), e.getY());


                    }
                    if (( SportType.isPredefinedSport(TabName)) && !thispane.getTitleAt(tabindex).equals("+") && !thispane.getTitleAt(tabindex).equals(SportType.Today.getSportName())) {
                        log("tab clicked is " + tabindex + "src/main" + thispane.getTitleAt(tabindex));
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem manageItem = new JMenuItem("Manage " + thispane.getTitleAt(tabindex));
                        JMenuItem hideItem = new JMenuItem("Hide " + thispane.getTitleAt(tabindex));
                        jPopupMenu.add(manageItem);
                        jPopupMenu.add(hideItem);
                        manageItem.addActionListener(e13 -> checkAndRunInEDT(() -> new SportCustomTab(thispane.getTitleAt(tabindex), tabindex)));

                        hideItem.addActionListener(e14 -> checkAndRunInEDT(() -> {
                            //new SportCustomTab(thispane.getTitleAt(tabindex),tabindex);
                            AppController.SpotsTabPaneVector.remove(tabindex);
                            //AppController.SportsTabPaneVector.remove(TabName);

//                            Vector tabpanes = AppController.getTabPanes();
//                            for (Object tabpane : tabpanes) {
//                                SportsTabPane tp = (SportsTabPane) tabpane;
//                                tp.setSelectedIndex(0);
//                                tp.remove(tabindex);
//                            }
                            SpankyWindow.applyToAllWindows((tp)-> {
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            });

                        }));
                        jPopupMenu.show(thispane, e.getX(), e.getY());
                    }
                }
            }
        });

    }

    public boolean isTabNameAvailable(String name) {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            String title = getTitleAt(i);
            if (title.equalsIgnoreCase(name)) {
                return false;
            }

        }
        return true;
    }
    // only gets called when adding new game into system
//    public void addGame(Game g, boolean repaint)   {
//        int totalTabs = getTabCount();
//        for (int i = 0; i < totalTabs; i++) {
//                Component c = getComponentAt(i);
//                if (c instanceof MainScreen) {
//                    MainScreen ms = (MainScreen) c;
//                if ( ms.isShowing()) {
//                    Utils.checkAndRunInEDT(()-> ms.addGame(g, ms.isShowing()));
//                        //there might be more than one screen is showing ( multiple windows)
////                    break;
//                }
//            }
//        }
//
//    }
    public void addGame(Game g)   {
        int selectedIndex = getSelectedIndex();
        if ( 0 > selectedIndex) {
            //game comes in when UI has not been initialized -- 2021-11-16
            return;
        }
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if (ms.shouldAddToScreen(g) ) {
                ms.addGame(g);
            }
        }
    }
//    public void removeGame(int gameid, boolean repaint) {
//        int totalTabs = getTabCount();
//        for (int i = 0; i < totalTabs; i++) {
//            Component c = getComponentAt(i);
//            if (c instanceof MainScreen) {
//                MainScreen ms = (MainScreen) c;
//                if ( ms.isShowing()) {
//                    Utils.checkAndRunInEDT(()-> ms.removeGame(gameid, ms.isShowing()));
//                    break;
//                }
//            }
//        }
//    }
//    public void removeGame(int gameid, boolean repaint) {
//        int selectedIndex = getSelectedIndex();
//        Component c = getComponentAt(selectedIndex);
//        if (c instanceof MainScreen) {
//            MainScreen ms = (MainScreen) c;
//            Utils.checkAndRunInEDT(()-> ms.removeGame(gameid, true));
//        }
//
//    }
//
//    public void disableTabs() {
//        int totalTabs = getTabCount();
//        for (int i = 0; i < totalTabs; i++) {
//            setEnabledAt(i, false);
//        }
//    }

    public void enableTabs() {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            setEnabledAt(i, true);
        }
    }

//    public void removeGames(String[] gameids) {
//        int totalTabs = getTabCount();
//        for (int i = 0; i < totalTabs; i++) {
//
//            Component c = getComponentAt(i);
//            if (c instanceof MainScreen) {
//                MainScreen ms = (MainScreen) c;
//                if ( ms.isShowing()) {
//                    Utils.checkAndRunInEDT(()-> ms.removeGames(gameids));
//                    break;
//                }
//            }
//        }
//    }
    public void removeGamesAndCleanup(Set<Integer> gameIdRemovedSet, CountDownLatch latch) {
        Component c = getSelectedComponent();
        if (c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                Utils.checkAndRunInEDT(() -> {
                    try {
                        ms.removeGamesAndCleanup(gameIdRemovedSet);
                    }finally {
                        latch.countDown();
                    }
                });
            }

    }
    public void moveGameToThisHeader(Game g, GameGroupHeader header) {
        Component c = getSelectedComponent();
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if ( ms.containsGame(g)) {
                Utils.checkAndRunInEDT(() -> ms.moveGameToThisHeader(g, header));
            }
        }
    }
    public void setOpener(boolean opener) {
        spankyWindowConfig.setOpener(opener);
    }
    public void setSort(boolean sort) {
        spankyWindowConfig.setTimesort(sort);
//        timesort = sort;
    }
    public void setShort(boolean shortteam) {
        spankyWindowConfig.setShortteam(shortteam);
//        this.shortteam = shortteam;
    }
    public void setLast(boolean b) {
        spankyWindowConfig.setLast(b);
//        last = b;
    }
    public void setDisplay(String b) {
        spankyWindowConfig.setDisplay(b);
//        display = b;
    }
    public void setPeriod(int b) {
        spankyWindowConfig.setPeriod(b);
//        period = b;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Are we dragging?
        if (dragging && currentMouseLocation != null && tabImage != null) {
            g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
        }
    }

    public void refreshCurrentTab() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            refreshMainScreen((MainScreen)comp);
        }
    }
    public void refreshMainScreen(MainScreen thisms) {
        thisms.destroyMe();
        log("refreshing MainScreen "+thisms.getName()+" !");
        reCreateMainScreen(thisms);
        MainGameTableModel model = thisms.getDataModels();
//        AppController.addDataModels(model);
        model.processTableModelEvent(new TableModelEvent(model, 0, Integer.MAX_VALUE, 0, TableModelEvent.UPDATE));
    }

    public void cleanup() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            MainScreen thisms = (MainScreen) comp;
            thisms.destroyMe();
        }

    }
//
//    public List<LinesTableData> getAllDataModels() {
//        int totalTabs = getTabCount();
//        List<LinesTableData> v = new ArrayList<>();
//        for (int i = 0; i < totalTabs; i++) {
//            Component c = getComponentAt(i);
//            if (c instanceof MainScreen) {
//                MainScreen ms = (MainScreen) c;
//                MainGameTableModel model = ms.getDataModels();
//                if ( null != model) {
//                    model.copyTo(v);
//                }
//            }
//        }
//        return v;
//    }

    public void clearAll() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            //it modifies values, not display. It is necessary to clear hidden main screen. -- 06/01/2021
            ((MainScreen)comp).setClearTime(System.currentTimeMillis());
        }
    }

    public void fireAllTableDataChanged(Collection<Game> games) {
        Component selectedComp = getSelectedComponent();
        if ( selectedComp instanceof MainScreen) {
            MainScreen ms = (MainScreen)selectedComp;
            ms.checktofire(games);
        }
    }
    private void reCreateMainScreen(MainScreen ms) {
        ms.createMe(loadlabel);
    }
}
