package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.config.SiaConst.UIProperties;
import com.sia.client.model.SportType;
import com.sia.client.ui.control.SportsTabPane;
import com.sia.client.ui.lineseeker.AlertLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

import static com.sia.client.config.Utils.checkAndRunInEDT;

public class SportsMenuBar extends JMenuBar {

    private final SportsTabPane stb;
    private final JMenu filemenu = new JMenu("File");
    private final JMenu bookiemenu = new JMenu("Columns");
    private final JMenu linealertsmenu = new JMenu("Line Alerts");
    private final JMenu gamealertsmenu = new JMenu("Game Alerts");
    private final JMenu tabsmenu = new JMenu("Tabs");
    private final JMenu windowmenu = new JMenu("Window");
    private final JMenu settingmenu = new JMenu("Setting");
    private final JMenu reportmenu = new JMenu("Report");
    private final static Dimension defaultDialogSize = new Dimension(840,840);

    public SportsMenuBar(SportsTabPane stb, TopView tv) {
        super();
        this.stb = stb;
        this.init();
        AppController.addMenuBar(this);
    }
    public void init() {

        add(filemenu);

        JMenuItem storeprefs = new JMenuItem("Store User Prefs");
        storeprefs.addActionListener(ev -> AppController.getUserPrefsProducer().sendUserPrefs(false));
        filemenu.add(storeprefs);

        JMenuItem logout = new JMenuItem("Exit...");
        logout.addActionListener(ev -> {
            // need to store user prefs
            AppController.getUserPrefsProducer().sendUserPrefs(true);
            System.exit(0);
        });
        filemenu.add(logout);


        add(bookiemenu);

        JMenuItem bookiecolumn = new JMenuItem("Manage");
        bookiecolumn.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
            AnchoredLayeredPane anchoredLayeredPane = new AnchoredLayeredPane(stb);
            anchoredLayeredPane.setTitle("Bookie Management");
            BookieColumnController2 bcc2 = new BookieColumnController2(anchoredLayeredPane);
            bcc2.openAndCenter(new Dimension(700,700),false);
        }));
        JMenuItem bookiecolumn1 = new JMenuItem("Chart");
        bookiecolumn1.addActionListener(ae -> checkAndRunInEDT(() -> new ChartHome(stb).show()));
        bookiemenu.add(bookiecolumn);
        bookiemenu.add(bookiecolumn1);

        add(linealertsmenu);


        JMenuItem generallinealert = new JMenuItem("Line Moves");
        generallinealert.addActionListener(ae -> new LineAlert(stb).show(UIProperties.LineAlertDim));

        JMenu majorlinemove = new JMenu("Line Seekers");
        JMenuItem lineSeekerConfig = new JMenuItem("Configuration");
        lineSeekerConfig.addActionListener(ae -> new AlertLayout(stb).show(AlertLayout.dialogPreferredSize));
        JMenuItem lineSeekerMethod = new JMenuItem("Alert Method");
        lineSeekerMethod.addActionListener(this::openAlertMediaSettingDialog);
        majorlinemove.add(lineSeekerConfig);
        majorlinemove.add(lineSeekerMethod);

        JMenuItem openers = new JMenuItem("Openers");
        openers.addActionListener(ae -> new LineAlertOpeners(stb).show(UIProperties.LineAlertDim));


        JMenuItem limitchange = new JMenuItem("Limit Changes");
        limitchange.addActionListener(ae -> new LimitGui(stb).show(UIProperties.LineAlertDim));

        linealertsmenu.add(generallinealert);
        linealertsmenu.add(majorlinemove);
        linealertsmenu.add(openers);
        linealertsmenu.add(limitchange);

        add(gamealertsmenu);

        JMenuItem started = createGameAlertMenuItem("Started");
        JMenuItem finals = createGameAlertMenuItem(SiaConst.FinalStr);
        JMenuItem halftimes = createGameAlertMenuItem(SiaConst.HalfTimeStr);
        JMenuItem lineups = createGameAlertMenuItem("Lineups");
        JMenuItem officials = createGameAlertMenuItem("Officials");
        JMenuItem injuries = createGameAlertMenuItem("Injuries");
        JMenuItem timechange = createGameAlertMenuItem("Time Changes");
        //JMenuItem limitchange = createGameAlertMenuItem("Limit Changes");

        JMenuItem test = new JMenuItem("Test");
        test.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
        });

        JMenuItem test2 = new JMenuItem("Test NW");
        test2.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.NORTH_WEST, AppController.getMainTabPane());
        });
        JMenuItem test3 = new JMenuItem("Test SW");
        test3.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_WEST, AppController.getMainTabPane());
        });
        JMenuItem test4 = new JMenuItem("Test SE");
        test4.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!", 10000, SwingConstants.SOUTH_EAST, AppController.getMainTabPane());
        });
        gamealertsmenu.add(started);
        gamealertsmenu.add(finals);
        gamealertsmenu.add(halftimes);
        gamealertsmenu.add(lineups);
        gamealertsmenu.add(officials);
        gamealertsmenu.add(injuries);
        gamealertsmenu.add(timechange);
       // gamealertsmenu.add(limitchange);
       // gamealertsmenu.add(test);
        add(tabsmenu);
        populateTabsMenu();


        add(windowmenu);
        JMenuItem newwindow = new JMenuItem("New..");
        newwindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0)); // 0 means no modifiers
        newwindow.addActionListener(AppController.getNewWindowAction());
        windowmenu.add(newwindow);

        add(settingmenu);
        JMenu fontconfig = FontConfig.instance().createFontMenu();
        settingmenu.add(fontconfig);
    }
    private void openAlertMediaSettingDialog(ActionEvent actionEvent) {
        LineSeekerAlertMethodDialog lineSeekerAlertMethodDialog = new LineSeekerAlertMethodDialog(this.stb);
        lineSeekerAlertMethodDialog.show(SiaConst.UIProperties.LineAlertMethodDim);
        lineSeekerAlertMethodDialog.updateAlertMethodAttr();
    }
    private JMenuItem createGameAlertMenuItem(String command) {
        JMenuItem menuItem = new JMenuItem(command);
        menuItem.addActionListener(ae -> new GameAlert(stb,command).show(defaultDialogSize));
        return menuItem;
    }
    public void populateTabsMenu() {
        tabsmenu.removeAll();
        /*
         * for pre defined sports.
         */
        for(SportType st: SportType.getPreDefinedSports()) {
            JMenu menu = new PredefinedSportMenu(stb,st).getMenu();
            tabsmenu.add(menu);
        }
        /*
         * for custom sports
         */
        for (int i = 0; i < AppController.customTabsVec.size(); i++) {
            String tabinfo =  AppController.customTabsVec.elementAt(i);
            String[] tabarr = tabinfo.split("\\*");
            String tabname = tabarr[1];
            JMenu temp = new JMenu(tabname);
            tabsmenu.add(temp);
            JMenuItem go = new JMenuItem("Goto");
            JMenuItem manage = new JMenuItem("Edit");
            JMenuItem hide = new JMenuItem("Remove");


            temp.add(go);
            go.addActionListener(ae -> stb.setSelectedIndex(stb.indexOfTab(temp.getText())));
            temp.add(manage);
            manage.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
                int idx = stb.indexOfTab(temp.getText());
                CustomTab2 customTab2 = new CustomTab2(stb,"Edit Custom Tab",stb.getWindowIndex(),stb.getTitleAt(idx), idx);
                customTab2.show(UIProperties.CustomTab2Dim);
            }));

            temp.add(hide);
            hide.addActionListener(ae -> SwingUtilities.invokeLater(() -> {
                int idx = stb.indexOfTab(temp.getText());
                AppController.removeCustomTab(stb.getTitleAt(idx));
                SpankyWindow.applyToAllWindows((tp)-> {
                    tp.setSelectedIndex(0);
                    tp.remove(idx);
                });
                go.setEnabled(false);
                hide.setEnabled(false);
            }));

        }
        JMenuItem addnew = new JMenuItem("Add New...");
        tabsmenu.add(addnew);
        addnew.addActionListener(ae ->  {
            CustomTab2 customTab2 = new CustomTab2(stb,"Custom Tab",stb.getWindowIndex());
            customTab2.show(UIProperties.CustomTab2Dim);
             }
        );
    }
}