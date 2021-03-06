package com.sia.client.ui.control;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst.SportName;
import com.sia.client.config.SiaConst.UIProperties;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.GameGroupHeader;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SpankyWindowConfig;
import com.sia.client.model.SportType;
import com.sia.client.simulator.InitialGameMessages;
import com.sia.client.simulator.MainScreenRefresh;
import com.sia.client.simulator.OngoingGameMessages;
import com.sia.client.simulator.TestExecutor;
import com.sia.client.ui.*;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class SportsTabPane extends JTabbedPane implements Cloneable {

//    public long cleartime = 1000;
    private SportsTabPane thispane;
    private int previousTabIndex = -1;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private boolean dragging = false;
    private final Map<String, MainScreen> mainScreenMap = new HashMap<>();
    private final SpankyWindowConfig spankyWindowConfig;

    public SportsTabPane(int windowIndex) {
        thispane = this;
        spankyWindowConfig = new SpankyWindowConfig(windowIndex,false,false);
        spankyWindowConfig.setTimesort(false);
        addListeners();
    }
    public SpankyWindowConfig getSpankyWindowConfig() {
        return this.spankyWindowConfig;
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
                addTab(title, new ImageIcon(imgResource), ms, title);
            }
        }

        List<String> customtabs = AppController.getCustomTabsVec();
        for (String customtab : customtabs) {
            String name = "";
            boolean showheaders = true;
            boolean showingame = true;
            boolean showseries = true;
            boolean showadded = true;
            boolean showextra = true;
            boolean showprops = true;
            String msstring = customtab;
            String[] items = msstring.split("\\*");
            List<String> customheaders = new ArrayList<>();
            for (int j = 0; j < items.length; j++) {
                log(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (String header : headers) {
                        if (!"".equals(header)) {
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
            try {
                List<String> customheaderStrList = GameUtils.convertLeagueIdHeaderToGameGroupHeaderStr(customheaders);
                SportType customerizedSportType = SportType.createCustomizedSportType(name, customheaderStrList,showheaders, showseries, showingame, showadded, showextra, showprops);
                MainScreen msnew = new MainScreen(customerizedSportType, spankyWindowConfig);
                addMainScreenToCache(msnew);
                addTab(name, null, msnew, name);
            } catch ( Exception e) {
                log("Error parsing custom tab:"+customheaders+"|",e);
            }
        }
        //use JPANEL as dummy component, don't use null otherwise AnchoredLayeredPane::openAndCenter stp.getSelectedComponent() return null when "+" tab is clicked -- 2022-01-22
        addTab("+", null, new JPanel(), "Add new custom sport");

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
        if (SpankOdds.getMessagesFromLog) {
            Executors.newFixedThreadPool(1).submit(OngoingGameMessages::loadMessagesFromLog);
        }
    }
    public MainScreen createMainScreen(SportType st) {
        MainScreen ms = new MainScreen(st,spankyWindowConfig);
        addMainScreenToCache(ms);
        return ms;
    }
    public Collection<MainGameTableModel> getAllTableModels() {
        return mainScreenMap.values().stream().map(MainScreen::getDataModels).filter(Objects::nonNull).collect(Collectors.toList());
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
                CustomTab2 customTab2 = new CustomTab2(this,"Custom Tab",getWindowIndex());
                customTab2.show(UIProperties.CustomTab2Dim);
                //restore to previouse index -- 2021-11-18
                setSelectedIndex(restoredIdex);
            } else if (getTabCount() > 1 && previousTabIndex == getTabCount() - 1) {
                // do nothing
            } else {

                Component currentComp = getComponentAt(currentTabIndex);
                if ( currentComp instanceof MainScreen) {
                    MainScreen newms = (MainScreen) currentComp;
                    log("changelistener create!");
                    createMainScreen(newms,null);
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
                    String tabName = thispane.getTitleAt(tabindex);


                    if (!SportType.isPredefinedSport(tabName) && ! "+".equals(tabName) && !SportName.Today.equals(tabName)) {
                        log("tab clicked is " + tabindex + "src/main" + tabName);
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem editItem = new JMenuItem("Edit " + tabName);
                        JMenuItem removeItem = new JMenuItem("Remove " + tabName);
                        jPopupMenu.add(editItem);
                        jPopupMenu.add(removeItem);
                        editItem.addActionListener(e1 -> checkAndRunInEDT(() -> {
                            CustomTab2 customTab2 = new CustomTab2(SportsTabPane.this,"Edit Custom Tab",getWindowIndex(),tabName, tabindex);
                            customTab2.show(UIProperties.CustomTab2Dim);

                        }));
                        removeItem.addActionListener(e12 -> checkAndRunInEDT(() -> {
                            AppController.removeCustomTab(thispane.getTitleAt(tabindex));
                            SpankyWindow.applyToAllWindows((tp)-> {
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            });

                        }));
                        jPopupMenu.show(thispane, e.getX(), e.getY());


                    } else if (SportType.isPredefinedSport(tabName) ) {
                        log("tab clicked is " + tabindex + "src/main" + thispane.getTitleAt(tabindex));
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem manageItem = new JMenuItem("Manage " + thispane.getTitleAt(tabindex));
                        JMenuItem hideItem = new JMenuItem("Hide " + thispane.getTitleAt(tabindex));
                        jPopupMenu.add(manageItem);
                        jPopupMenu.add(hideItem);
                        manageItem.addActionListener(e13 -> new SportCustomTab(SportsTabPane.this,thispane.getTitleAt(tabindex)).show());

                        hideItem.addActionListener(e14 -> checkAndRunInEDT(() -> {
                            AppController.SpotsTabPaneVector.remove(tabindex);
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
            if ( ms.isTableReday() && ms.containsGame(g)) {
                Utils.checkAndRunInEDT(() -> ms.moveGameToThisHeader(g, header),true);
            }
        }
    }
    public void setOpener(boolean opener) {
        spankyWindowConfig.setOpener(opener);
    }
    public void setSort(boolean sort) {
        spankyWindowConfig.setTimesort(sort);
    }
    public void setShort(boolean shortteam) {
        spankyWindowConfig.setShortteam(shortteam);
    }
    public void setLast(boolean b) {
        spankyWindowConfig.setLast(b);
    }
    public void setDisplay(String b) {
        spankyWindowConfig.setDisplay(b);
    }
    public void setPeriod(int b) {
        spankyWindowConfig.setPeriod(b);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Are we dragging?
        if (dragging && currentMouseLocation != null && tabImage != null) {
            g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
        }
    }
    public void resetCurrentScreenStates() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            MainGameTableModel model = ((MainScreen)comp).getDataModels();
            model.resetGameStatesForAllTableSections();
            model.fireTableChanged(new TableModelEvent(model,0,Integer.MAX_VALUE,0,TableModelEvent.UPDATE));
        }
    }
    public void refreshSport(SportType st) {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            MainScreen ms = (MainScreen)comp;
            if ( ms.getSportType().equals(st)) {
                reCreateMainScreen(ms);
            }
        }
    }
    public void updateMainScreen() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            MainGameTableModel model = ((MainScreen)comp).getDataModels();
            model.processTableModelEvent(new TableModelEvent(model, 0, Integer.MAX_VALUE, 0, TableModelEvent.UPDATE));
        }
    }
    public void rebuildMainScreen() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            reCreateMainScreen((MainScreen)comp);
        }
    }
    public void reCreateMainScreen(MainScreen thisms) {
        thisms.destroyMe();
        log("refreshing MainScreen "+thisms.getName()+" !");
        Runnable listener = () ->{
            MainGameTableModel model = thisms.getDataModels();
            model.processTableModelEvent(new TableModelEvent(model, 0, Integer.MAX_VALUE, 0, TableModelEvent.UPDATE));
        };
        createMainScreen(thisms,listener);
    }
    public void cleanup() {
        Component comp = getSelectedComponent();
        if ( comp instanceof MainScreen) {
            MainScreen thisms = (MainScreen) comp;
            thisms.destroyMe();
        }

    }


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
    private static void createMainScreen(MainScreen ms, Runnable listener) {
        ms.createMe(listener);
    }
}
