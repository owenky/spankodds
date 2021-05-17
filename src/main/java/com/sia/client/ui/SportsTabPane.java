package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;
import com.sia.client.model.Game;
import com.sia.client.model.MainGameTableModel;

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

//adjustcolumns after line update	

    public SportsTabPane() {

        thispane = this;
        //end for draggable tabpane
        try {
            //loadgif = new ImageIcon(ImageIO.read(getClass().getResource("ajax-loader.gif")));
            loadlabel = new JLabel("loading...", loadgif, JLabel.CENTER);
            loadlabel.setOpaque(true);
            //loadgif = new ImageIcon(ImageIO.read(getClass().getResource("loader2.gif")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("initializing tabs");
        AppController.addTabPane(this);

        System.out.println("initializing tabs2");

        System.out.println("initializing tabs3");

        addListeners();

        List<String> maintabs = AppController.getMainTabVec();
        System.out.println(maintabs);
        for (int i = 0; i < maintabs.size(); i++) {
            String title = maintabs.get(i);
            String img = "";
            if (title.equals("Football")) {
                img = "football.png";
            } else if (title.equals("Basketball")) {
                img = "basketball.png";
            } else if (title.equals("Baseball")) {
                img = "baseball.png";
            } else if (title.equals("Hockey")) {
                img = "hockey.png";
            } else if (title.equals("Fighting")) {
                img = "boxing.png";
            } else if (title.equals(SiaConst.SoccerStr)) {
                img = "soccer.png";
            } else if (title.equals("Auto Racing")) {
                img = "flag.png";
            } else if (title.equals("Golf")) {
                img = "golf.png";
            } else if (title.equals("Tennis")) {
                img = "tennis.png";
            } else if (title.equals("Today")) {
                img = "today.png";
            }

            if (null != img) {
                URL imgResource = Utils.getMediaResource(img);
                addTab(title, new ImageIcon(imgResource), new MainScreen(title), title);
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
            System.out.println("customtab=" + msstring);
            String[] items = msstring.split("\\*");
            Vector customheaders = new Vector();
            for (int j = 0; j < items.length; j++) {
                System.out.println(j + " item=" + items[j]);
                if (j == 0) {
                    String[] headers = items[j].split("\\|");
                    for (int k = 0; k < headers.length; k++) {
                        String header = headers[k];
                        if (header.equals("")) {
                            continue;
                        } else {
                            customheaders.add(header);
                            System.out.println("adding header=" + header);
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
            MainScreen msnew = new MainScreen(name, customheaders, showheaders, showseries, showingame, showadded, showextra, showprops);
            addTab(name, null, msnew, name);
        }
        //addTab(null,null,null,null);
				/*	JButton plus=new JButton("+");
					plus.addActionListener(new ActionListener() { 
							public void actionPerformed(ActionEvent e) { 
							new CustomTab2();
							
							}
					});
					Color clr = new Color(237, 237, 235);
					plus.setBackground(clr);
					JPanel p=new JPanel();
					p.add(plus);*/
        addTab("+", null, null, "+");

        //plus.setPreferredSize(new Dimension(30, 20));
        //setTabComponentAt(getTabCount() - 1, p);
        //setSelectedIndex(getTabCount() - 1);

        System.out.println("initializing tabs9");
        //com.sia.client.ui.AppController.doneloadinginitial();


    }

    private void addListeners() {

        this.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {

                previousTabIndex = currentTabIndex;
                currentTabIndex = thispane.getSelectedIndex();

                System.out.print(" Current tab is:" + currentTabIndex + "..tabcount=" + thispane.getTabCount());
                System.out.print(" Previous tab is:" + previousTabIndex);
                if (thispane.getTabCount() > 1 && currentTabIndex == thispane.getTabCount() - 1) {
                    checkAndRunInEDT(() -> {
                        System.out.println(currentTabIndex + "stateChanged" + (currentTabIndex == getTabCount() - 1));
                        new CustomTab2();

                    });
                    setSelectedIndex(previousTabIndex);

                } else if (getTabCount() > 1 && previousTabIndex == getTabCount() - 1) {
                    // do nothing
                } else {

                    MainScreen oldms = (MainScreen) getComponentAt(previousTabIndex);
                    oldms.makeDataModelsVisible(false);

                    oldms.destroyMe();

                    MainScreen newms = (MainScreen) getComponentAt(currentTabIndex);

                    //SwingUtilities.invokeLater(new Runnable()
                    //{
                    //public void run()
                    //{
                    log("changelistener create!");
                    newms.createMe(display, period, timesort, shortteam, opener, last, loadlabel);
                    newms.makeDataModelsVisible(true);

                    System.out.println(" timesort is:" + timesort + "...now=" + new java.util.Date());
                    //newms.adjustcols(false);
                    //newms.firedatamodels();


                    //}
                    //});
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
                            System.out.println("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
                                tp.setSelectedIndex(0);
                                tp.remove(tabindex);
                            }

                        }));
                        jPopupMenu.show(thispane, e.getX(), e.getY());


                    }
                    if ((TabName.equals("Football") || TabName.equals("Basketball") || TabName.equals("Baseball") || TabName.equals("Hockey") || TabName.equals("Fighting") || TabName.equals(SiaConst.SoccerStr) || TabName.equals("Auto Racing") || TabName.equals("Golf") || TabName.equals("Tennis")) && !thispane.getTitleAt(tabindex).equals("+") && !thispane.getTitleAt(tabindex).equals("Today")) {
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
                            System.out.println("tabpanes size= " + tabpanes.size());
                            for (int i = 0; i < tabpanes.size(); i++) {
                                SportsTabPane tp = (SportsTabPane) tabpanes.get(i);
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

    public void adjustcols(boolean includeheader) {
        MainScreen ms = (MainScreen) getSelectedComponent();
        log("..adjusting cols");
        ms.adjustcols(includeheader);

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
    //TODO, use reference to MainScreen instead of loop through Component, also  ms.addGame(g, repaint) might no tbe added to the MainScreen if it does not belogn to this MainScreen --05/01/2021
    public void addGame(Game g, boolean repaint)    // only gets called when adding new game into system
    {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);

            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                ms.addGame(g, repaint);
            }
        }

    }

    public void removeGame(int gameid) {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                ms.removeGame(gameid);
            }
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

    public void removeGames(String[] gameids) {
        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                ms.removeGames(gameids);
            }
        }
    }

    public void moveGameToThisHeader(Game g, String header) {

        int totalTabs = getTabCount();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                if ( ms.moveGameToThisHeader(g, header) ) {
                    break;
                }
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
        thisms.revalidate();
        thisms.repaint();
        AppController.removeDataModels(thisms.getDataModels());
        thisms.destroyMe();
        System.out.println("refreshing currenttab!");
        thisms.createMe(display, period, timesort, shortteam, opener, last, loadlabel);
        thisms.makeDataModelsVisible(true);
        AppController.addDataModels(thisms.getDataModels());
        thisms.adjustcols(false);
        thisms.firedatamodels();

    }


    public void cleanup() {
        MainScreen thisms = (MainScreen) getComponentAt(currentTabIndex);
        AppController.removeDataModels(thisms.getDataModels());
        thisms.destroyMe();

    }

    public MainGameTableModel getCurrentDataModels() {
        MainScreen ms = (MainScreen) getSelectedComponent();
        return ms.getDataModels();
    }

    public List<LinesTableData> getAllDataModels() {
        int totalTabs = getTabCount();
        List<LinesTableData> v = new ArrayList<>();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            if ( c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                MainGameTableModel model = ms.getDataModels();
                model.copyTo(v);
            }
        }
        return v;
    }

    public void clearAll() {
        int totalTabs = getTabCount();
        Vector v = new Vector();
        for (int i = 0; i < totalTabs; i++) {
            Component c = getComponentAt(i);
            //System.out.println("i="+i+".."+c);
            if (c != null && c instanceof MainScreen) {
                MainScreen ms = (MainScreen) c;
                ms.clearAll();
            }
        }

    }

    public boolean fireAllTableDataChanged(int gameid) {

        MainScreen ms = (MainScreen) getSelectedComponent();
        return ms.checktofire(gameid);

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
