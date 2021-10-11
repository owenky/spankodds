package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.model.SportType;

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
import java.util.List;
import java.util.Vector;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class SportsTabPane extends JTabbedPane implements Cloneable {


    public String display = "default";
    public int period = 0;
    public boolean timesort = false;
    public boolean shortteam = false;
    public boolean opener = false;
    public boolean last = false;
    public long cleartime = 1000;
    public ImageIcon loadgif = null;
    public JLabel loadlabel = null;
    public SportsTabPane thispane;
    private int currentTabIndex = 0, previousTabIndex = 0;
    private Image tabImage = null;
    private Point currentMouseLocation = null;
    private boolean dragging = false;

    public SportsTabPane() {

        thispane = this;
        //end for draggable tabpane
        try {
            //loadgif = new ImageIcon(ImageIO.read(getClass().getResource("ajax-loader.gif")));
            loadlabel = new JLabel("loading...", loadgif, JLabel.CENTER);
            loadlabel.setOpaque(true);
            //loadgif = new ImageIcon(ImageIO.read(getClass().getResource("loader2.gif")));
        } catch (Exception ex) {
            log(ex);
        }

        AppController.addTabPane(this);
        addListeners();

        List<String> maintabs = AppController.getMainTabVec();
        log(String.valueOf(maintabs));
        for (String title : maintabs) {
            SportType st = SportType.findBySportName(title);
            if (null != st) {
                URL imgResource = Utils.getMediaResource(st.getIcon());
                MainScreen ms = new MainScreen(st);
                addTab(title, new ImageIcon(imgResource),ms , title);
            }
        }

        Vector customtabs = AppController.getCustomTabsVec();
        //adding=|851 07/03|851 07/04|852 07/04|853 07/04*new1*true*false*false*true*true?
        for (int i = 0; i < customtabs.size(); i++) {
            String name = "";
            boolean showheaders = true;
            boolean showingame = true;
            boolean showseries = true;
            boolean showadded = true;
            boolean showextra = true;
            boolean showprops = true;
            String msstring = (String) customtabs.elementAt(i);
            log("customtab=" + msstring);
            String[] items = msstring.split("\\*");
            Vector customheaders = new Vector();
            for (int j = 0; j < items.length; j++) {
                log(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (int k = 0; k < headers.length; k++) {
                        String header = headers[k];
                        if (header.equals("")) {
                            continue;
                        } else {
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
            SportType customerizedSportType =  new SportType(-200,name,name,null,-1);
            MainScreen msnew = new MainScreen(customerizedSportType, customheaders, showheaders, showseries, showingame, showadded, showextra, showprops);
            addTab(name, null, msnew, name);
        }
        addTab("+", null, null, "+");

        //plus.setPreferredSize(new Dimension(30, 20));
        //setTabComponentAt(getTabCount() - 1, p);
        //setSelectedIndex(getTabCount() - 1);

        log("initializing tabs9");
        //com.sia.client.ui.AppController.doneloadinginitial();


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

                    MainScreen oldms = (MainScreen) getComponentAt(previousTabIndex);
                    oldms.destroyMe();
                    MainScreen newms = (MainScreen) getComponentAt(currentTabIndex);
                    log("changelistener create!");
                    newms.createMe(display, period, timesort, shortteam, opener, last, loadlabel);
                    log(" timesort is:" + timesort + "...now=" + new java.util.Date());
                }

            }
        });


        this.addMouseListener(new MouseAdapter() {


            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("button 1 clicked");
                } else if (e.getButton() == MouseEvent.BUTTON3) {

                    TabbedPaneUI ui = thispane.getUI();
                    int tabindex = ui.tabForCoordinate(thispane, e.getX(), e.getY());
                    System.out.println("button 3 clicked " + tabindex);
                    String TabName = thispane.getTitleAt(tabindex);


                    if (!(TabName.equals("Football") || TabName.equals("Basketball") || TabName.equals("Baseball") || TabName.equals("Hockey") || TabName.equals("Fighting") || TabName.equals(SiaConst.SoccerStr) || TabName.equals("Auto Racing") || TabName.equals("Golf") || TabName.equals("Tennis")) && !thispane.getTitleAt(tabindex).equals("+") && !thispane.getTitleAt(tabindex).equals("Today")) {
                        System.out.println("tab clicked is " + tabindex + "src/main" + thispane.getTitleAt(tabindex));
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem editItem = new JMenuItem("Edit " + thispane.getTitleAt(tabindex));
                        JMenuItem removeItem = new JMenuItem("Remove " + thispane.getTitleAt(tabindex));
                        jPopupMenu.add(editItem);
                        jPopupMenu.add(removeItem);
                        editItem.addActionListener(e1 -> checkAndRunInEDT(() -> {
                            System.out.println(currentTabIndex + "mouseClicked" + (currentTabIndex == getTabCount() - 1));
                            new CustomTab2(thispane.getTitleAt(tabindex), tabindex);

                        }));
                        removeItem.addActionListener(e12 -> checkAndRunInEDT(() -> {
                            AppController.removeCustomTab(thispane.getTitleAt(tabindex));
                            Vector tabpanes = AppController.getTabPanes();
                            for (Object tabpane : tabpanes) {
                                SportsTabPane tp = (SportsTabPane) tabpane;
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            }

                        }));
                        jPopupMenu.show(thispane, e.getX(), e.getY());


                    }
                    if (( SportType.isPredefinedSport(TabName)) && !thispane.getTitleAt(tabindex).equals("+") && !thispane.getTitleAt(tabindex).equals(SportType.Today.getSportName())) {
                        System.out.println("tab clicked is " + tabindex + "src/main" + thispane.getTitleAt(tabindex));
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

                            Vector tabpanes = AppController.getTabPanes();
                            for (Object tabpane : tabpanes) {
                                SportsTabPane tp = (SportsTabPane) tabpane;
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            }

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
    public void addGame(Game g, boolean repaint)   {
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if (ms.shouldAddToScreen(g) ) {
                Utils.checkAndRunInEDT(() -> ms.addGame(g, true,()-> {
                    if ( ms.isPreDefinedSport() ) {
                        synchronized (SiaConst.DataUpdateLock) {
                            refreshMainScreen(ms);
                        }
                    }
                }));
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
    public void removeGame(int gameid, boolean repaint) {
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            Utils.checkAndRunInEDT(()-> ms.removeGame(gameid, true));
        }

    }

    public void disableTabs() {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            setEnabledAt(i, false);
        }
    }

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
    public void removeGames(String[] gameids) {
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            Utils.checkAndRunInEDT(()-> ms.removeGames(gameids));
        }

    }

//    public void moveGameToThisHeader(Game g, String header) {
//
//        int totalTabs = getTabCount();
//        for (int i = 0; i < totalTabs; i++) {
//            Component c = getComponentAt(i);
//            if (c instanceof MainScreen) {
//                MainScreen ms = (MainScreen) c;
//                if ( c.isShowing() ) {
//                    Utils.checkAndRunInEDT(()-> ms.moveGameToThisHeader(g, header));
//                    break;
//                }
//            }
//        }
//
//
//    }
    public void moveGameToThisHeader(Game g, String header) {
        int selectedIndex = getSelectedIndex();
        Component c = getComponentAt(selectedIndex);
        if (c instanceof MainScreen) {
            MainScreen ms = (MainScreen) c;
            if ( ms.getSportType().isPredifined() && ms.getSportType().isMyType(g)) {
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
    public static void refreshMainScreen(MainScreen thisms) {
        thisms.destroyMe();
        log("refreshing MainScreen "+thisms.getName()+" !");
        thisms.createData();
        AppController.addDataModels(thisms.getDataModels());
    }

    public void cleanup() {
        MainScreen thisms = (MainScreen) getComponentAt(currentTabIndex);
        thisms.destroyMe();

    }

    public List<LinesTableData> getAllDataModels() {
        int totalTabs = getTabCount();
        List<LinesTableData> v = new ArrayList<>();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if (c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                MainGameTableModel model = ms.getDataModels();
                model.copyTo(v);
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

        MainScreen ms = (MainScreen) getSelectedComponent();
        ms.checktofire(games);
    }

    public void refreshTabs() {

        int currenttabindex = getSelectedIndex();
        int numtabs = getTabCount();
        if (currenttabindex == 0) {
            setSelectedIndex(currenttabindex + 1);
            setSelectedIndex(currenttabindex);
        } else {
            setSelectedIndex(currenttabindex - 1);
            setSelectedIndex(currenttabindex);
        }

    }


}
