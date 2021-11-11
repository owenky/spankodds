package com.sia.client.ui.control;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SportType;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.MainScreenRefresh;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.TestExecutor;
import com.sia.client.ui.AppController;
import com.sia.client.ui.CustomTab2;
import com.sia.client.ui.LinesTableData;
import com.sia.client.ui.SpankyWindow;
import com.sia.client.ui.SportCustomTab;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class SportsTabPane extends JTabbedPane implements Cloneable {


    public String display = "default";
    public int period = 0;
    public boolean timesort = false;
    public boolean shortteam = false;
    public boolean opener = false;
    public boolean last = false;
//    public long cleartime = 1000;
    public ImageIcon loadgif = null;
    public JLabel loadlabel;
    public SportsTabPane thispane;
    private int currentTabIndex = 0, previousTabIndex = 0;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private boolean dragging = false;
    private final Map<String, MainScreen> mainScreenMap = new HashMap<>();
    private final int counter;
    private static AtomicInteger instanceCounter = new AtomicInteger(0);

    public SportsTabPane() {
        counter = instanceCounter.getAndAdd(1);
        thispane = this;
        loadlabel = new JLabel("loading...", loadgif, JLabel.CENTER);
        loadlabel.setOpaque(true);
        //        AppController.addTabPane(this);
        addListeners();
    }
    public void populateComponents() {

        List<String> maintabs = AppController.getMainTabVec();
        for (String title : maintabs) {
            SportType st = SportType.findBySportName(title);
            if (null != st) {
                URL imgResource = Utils.getMediaResource(st.getIcon());
                MainScreen ms = new MainScreen(st);
                addMainScreenToCache(ms);
                addTab(title, new ImageIcon(imgResource),ms , title);
            }
        }

        Vector<String> customtabs = AppController.getCustomTabsVec();
        //adding=|851 07/03|851 07/04|852 07/04|853 07/04*new1*true*false*false*true*true?
        for (int i = 0; i < customtabs.size(); i++) {
            String name = "";
            boolean showheaders = true;
            boolean showingame = true;
            boolean showseries = true;
            boolean showadded = true;
            boolean showextra = true;
            boolean showprops = true;
            String msstring = customtabs.elementAt(i);
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
            SportType customerizedSportType =  SportType.createCustomizedSportType(name);
            MainScreen msnew = new MainScreen(customerizedSportType, customheaders, showheaders, showseries, showingame, showadded, showextra, showprops);
            addMainScreenToCache(msnew);
            addTab(name, null, msnew, name);
        }
        addTab("+", null, null, "+");

        //plus.setPreferredSize(new Dimension(30, 20));
        //setTabComponentAt(getTabCount() - 1, p);
        //setSelectedIndex(getTabCount() - 1);

        log("initializing SportTabPane instance:"+counter);
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
    public MainScreen createMainScreen(SportType st, Vector<String> customvec) {
        MainScreen ms = new MainScreen(st, customvec);
        addMainScreenToCache(ms);
        return ms;
    }
    public MainScreen createMainScreen(SportType st) {
        MainScreen ms = new MainScreen(st);
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

        this.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {

                previousTabIndex = currentTabIndex;
                currentTabIndex = thispane.getSelectedIndex();

                log(" Current tab is:" + currentTabIndex + "..tabcount=" + thispane.getTabCount());
                log(" Previous tab is:" + previousTabIndex);
                if (thispane.getTabCount() > 1 && currentTabIndex == thispane.getTabCount() - 1) {
                    checkAndRunInEDT(() -> {
                        log(currentTabIndex + "stateChanged" + (currentTabIndex == getTabCount() - 1));
                        new CustomTab2();

                    });
                    setSelectedIndex(previousTabIndex);

                } else if (getTabCount() > 1 && previousTabIndex == getTabCount() - 1) {
                    // do nothing
                } else {
                    Component previousComp = getComponentAt(previousTabIndex);
                    if ( previousComp instanceof MainScreen) {
                        MainScreen oldms = (MainScreen) previousComp;
                        oldms.destroyMe();
                    }
                    Component currentComp = getComponentAt(currentTabIndex);
                    if ( currentComp instanceof MainScreen) {
                        MainScreen newms = (MainScreen) currentComp;
                        log("changelistener create!");
                        newms.createMe(display, period, timesort, shortteam, opener, last, loadlabel);
                        log(" timesort is:" + timesort + "...now=" + new java.util.Date());
                    }
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
                            log(currentTabIndex + "mouseClicked" + (currentTabIndex == getTabCount() - 1));
                            new CustomTab2(thispane.getTitleAt(tabindex), tabindex);

                        }));
                        removeItem.addActionListener(e12 -> checkAndRunInEDT(() -> {
                            AppController.removeCustomTab(thispane.getTitleAt(tabindex));
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
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if (ms.shouldAddToScreen(g) ) {
                Runnable r = () -> ms.addGame(g);
                Utils.checkAndRunInEDT(() -> {
                    try {
                        r.run();
                    }catch (Exception e) {
                        log(e);
                    }
                });
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
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
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
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if ( ms.containsGame(g)) {
                Utils.checkAndRunInEDT(() -> ms.moveGameToThisHeader(g, header));
            }
        }
    }
    public void setSort(boolean sort) {
        timesort = sort;
    }

    public void setShort(boolean shortteam) {
        this.shortteam = shortteam;
    }

    public void setOpener(boolean b) {
        opener = b;
    }

    public void setLast(boolean b) {
        last = b;
    }

    public void setDisplay(String b) {
        display = b;
    }

    public void setPeriod(int b) {
        period = b;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Are we dragging?
        if (dragging && currentMouseLocation != null && tabImage != null) {
            g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
        }
    }

    public void refreshCurrentTab() {
        MainScreen thisms = (MainScreen) getComponentAt(currentTabIndex);
        refreshMainScreen(thisms);
    }
    public void refreshMainScreen(MainScreen thisms) {
        thisms.destroyMe();
        log("refreshing MainScreen "+thisms.getName()+" !");
        thisms.createData();
        AppController.addDataModels(thisms.getDataModels());
    }

    public void cleanup() {
        Component comp = getComponentAt(currentTabIndex);
        if ( comp instanceof MainScreen) {
            MainScreen thisms = (MainScreen) comp;
            thisms.destroyMe();
        }

    }

    public List<LinesTableData> getAllDataModels() {
        int totalTabs = getTabCount();
        List<LinesTableData> v = new ArrayList<>();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if (c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                MainGameTableModel model = ms.getDataModels();
                if ( null != model) {
                    model.copyTo(v);
                }
            }
        }
        return v;
    }

    public void clearAll() {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                //it modifies values, not display. It is necessary to clear hidden main screen. -- 06/01/2021
                ms.clearColors();
            }
        }

    }

    public void fireAllTableDataChanged(Collection<Game> games) {
        Component selectedComp = getSelectedComponent();
        if ( selectedComp instanceof MainScreen) {
            MainScreen ms = (MainScreen)selectedComp;
            ms.checktofire(games);
        }
    }
//
//    public void refreshTabs() {
//
//        int currenttabindex = getSelectedIndex();
//        int numtabs = getTabCount();
//        if (currenttabindex == 0) {
//            setSelectedIndex(currenttabindex + 1);
//            setSelectedIndex(currenttabindex);
//        } else {
//            setSelectedIndex(currenttabindex - 1);
//            setSelectedIndex(currenttabindex);
//        }
//
//    }
//

}
