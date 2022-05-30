package com.sia.client.ui;

import com.sia.client.config.SiaConst.LayedPaneIndex;
import com.sia.client.media.SoundPlayer;
import com.sia.client.model.AlertStruct;
import com.sia.client.ui.control.MainScreen;
import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.sia.client.config.Utils.checkAndRunInEDT;
import static com.sia.client.config.Utils.log;

public class TopView extends JPanel implements ItemListener, Cloneable {
    private static final String mutedImgFile="muted.png";
    private static final String unMutedImgFile="unmuted.jpg";
    private static final String blockingPopupImgFile="blocking.jpg";
    private static final String unBlockingPopupImgFile="unblocking.jpg";
    private final SportsTabPane stb;
    private JButton clearBut;
    private JButton clearAllBut;
    private JButton lastBut;
    private JButton openerBut;
    private JButton sortBut;
    private JButton addBookieBut;
    private JButton shrinkTeamBut;
    private JButton newWindowBut;
    private JButton alertBut;
    private JButton adjustcolsBut;
    private JButton chartBut;
    private JToggleButton muteBut;
    private JToggleButton blockAlertPopupBut;
    private JComboBox cb;
    private JComboBox periodcb;
    private String[] display = new String[9];
    private String[] display2 = new String[9];
    private int[] perioddisplay = new int[8];
    private String[] perioddisplay2 = new String[8];
    private MutableItemContainer<AlertStruct> alertsComp;

    public TopView(SportsTabPane stb) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.stb = stb;
    }

    public void initComponents() {
        display[0] = "default";
        display[1] = "spreadtotal";
        display[2] = "totalmoney";
        display[3] = "totalbothmoney";
        display[4] = "justspread";
        display[5] = "justtotal";
        display[6] = "justmoney";
        display[7] = "awayteamtotal";
        display[8] = "hometeamtotal";


        display2[0] = "Default View";
        display2[1] = "Sides & Totals";
        display2[2] = "Totals & ML";
        display2[3] = "Totals & Both ML";
        display2[4] = "Just Sides";
        display2[5] = "Just Totals";
        display2[6] = "Just ML";
        display2[7] = "Away TT";
        display2[8] = "Home TT";

        perioddisplay[0] = 0;
        perioddisplay[1] = 1;
        perioddisplay[2] = 2;
        perioddisplay[3] = 5;
        perioddisplay[4] = 6;
        perioddisplay[5] = 7;
        perioddisplay[6] = 8;
        perioddisplay[7] = 9;


        perioddisplay2[0] = "Full Game";
        perioddisplay2[1] = "1st Half";
        perioddisplay2[2] = "2nd Half";
        perioddisplay2[3] = "1st Q/P";
        perioddisplay2[4] = "2nd Q/P";
        perioddisplay2[5] = "3rd Q/P";
        perioddisplay2[6] = "4th Q/P";
        perioddisplay2[7] = "Live";


        cb = new JComboBox(display2);

        cb.setSelectedIndex(0);
        cb.addItemListener(this);

        periodcb = new JComboBox(perioddisplay2);

        periodcb.setSelectedIndex(0);
        periodcb.addItemListener(this);


        sortBut = new JButton("Time sort");

        addBookieBut = new JButton("Add Bookie");
        shrinkTeamBut = new JButton("Short Team");
        newWindowBut = new JButton("New Window");
        alertBut = new JButton("Alert");
        adjustcolsBut = new JButton("Adj Cols");
        chartBut = new JButton("Chart");
        chartBut.addActionListener(e -> new ChartHome(stb).show());
        muteBut = new JToggleButton("muteAlert",unMutedImgFile,"Click to mute audio plays",mutedImgFile,"Click to un-mute audio plays");
        ToggerButtonListener muteButListener = (button)-> SoundPlayer.enableSound = button.isEnabled();
        muteBut.addActionListener(muteButListener);

        blockAlertPopupBut = new JToggleButton("blockAlertPopup",unBlockingPopupImgFile,"Click to block popup alert",blockingPopupImgFile,"Click to un-block popup alert");
        ToggerButtonListener blockAlertPopupListener = (button)-> UrgentMessage.popupEnabled = button.isEnabled();
        blockAlertPopupBut.addActionListener(blockAlertPopupListener);

        alertsComp = new UrgentMesgHistComp();
        AppController.alertsVector.bind(alertsComp);


        // this.getChildren().addAll(cb,clearBut,clearAllBut,lastBut,openerBut,addBookieBut,remBookieBut,newWindowBut,alertBut);
        initEvents();
        add(cb);
        add(periodcb);
        add(clearBut);
        add(clearAllBut);
        add(lastBut);
        add(openerBut);
        add(sortBut);
        add(shrinkTeamBut);
        add(adjustcolsBut);
        add(chartBut);
        add(alertsComp.getComponent());
        add(new JToolBar.Separator());
        add(muteBut);
        add(blockAlertPopupBut);
    }

    public void initEvents() {

        newWindowBut.addActionListener(AppController.getNewWindowAction());

// 1st HALF NOT VISIBLE USED TO SELECT 1st HALF FROM COMBO BOX!

        Action firsthalfaction = new AbstractAction("1st half") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("1st half button pressed");
                periodcb.setSelectedIndex(1);


            }
        };
        firsthalfaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));

        this.getActionMap().put("1sthalfAction", firsthalfaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) firsthalfaction.getValue(Action.ACCELERATOR_KEY), "1sthalfAction");

// FULL GAME NOT VISIBLE USED TO SELECT FULL GAME FROM COMBO BOX!

        Action fullgameaction = new AbstractAction("Full Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("full game button pressed");
                periodcb.setSelectedIndex(0);


            }
        };
        fullgameaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G, 0));

        this.getActionMap().put("fullgameAction", fullgameaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) fullgameaction.getValue(Action.ACCELERATOR_KEY), "fullgameAction");

// USED TO SELECT DEFAULT VIEW FROM COMBO BOX!

        Action defaultviewaction = new AbstractAction("DefaultView") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("defaultview button pressed");
                cb.setSelectedIndex(0);


            }
        };
        defaultviewaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));

        this.getActionMap().put("defaultviewaction", defaultviewaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) defaultviewaction.getValue(Action.ACCELERATOR_KEY), "defaultviewaction");

// USED TO SELECT SIDES ONLY FROM COMBO BOX!

        Action sidesonlyaction = new AbstractAction("SidesOnly") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("sides only button pressed");
                cb.setSelectedIndex(4);


            }
        };
        sidesonlyaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));

        this.getActionMap().put("sidesonlyaction", sidesonlyaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) sidesonlyaction.getValue(Action.ACCELERATOR_KEY), "sidesonlyaction");

// USED TO SELECT TOTALS ONLY FROM COMBO BOX!

        Action totalsonlyaction = new AbstractAction("TotalsOnly") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("totals only button pressed");
                cb.setSelectedIndex(5);


            }
        };
        totalsonlyaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));

        this.getActionMap().put("totalsonlyaction", totalsonlyaction);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) totalsonlyaction.getValue(Action.ACCELERATOR_KEY), "totalsonlyaction");


// CLEAR BUTTON

        Action clearaction = new AbstractAction("Clear") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("clear button pressed");
                Component comp = stb.getSelectedComponent();
                if (comp instanceof MainScreen) {
                    ((MainScreen) comp).setClearTime(new java.util.Date().getTime());
                }
            }
        };
        clearaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
        clearBut = new JButton(clearaction);
        clearBut.getActionMap().put("clearAction", clearaction);
        clearBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) clearaction.getValue(Action.ACCELERATOR_KEY), "clearAction");


// CLEAR ALL BUTTON

        Action clearallaction = new AbstractAction("Clear All") {
            @Override
            public void actionPerformed(ActionEvent e) {
                log("clear all button pressedAAA");
                long ct = (new java.util.Date()).getTime();
                MainScreen ms = (MainScreen) stb.getSelectedComponent();
                ms.setClearTime(ct);
                AppController.setClearAllTime(ct);
                AppController.clearAll();
            }
        };
        clearallaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, 0));
        clearAllBut = new JButton(clearallaction);
        clearAllBut.getActionMap().put("clearAllAction", clearallaction);
        clearAllBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) clearallaction.getValue(Action.ACCELERATOR_KEY), "clearAllAction");


        adjustcolsBut.addActionListener(ae -> {
            log("ajustcols button pressed");
            MainScreen ms = (MainScreen) stb.getSelectedComponent();
            ms.adjustcols();
        });
        Action lastaction = new AbstractAction("Last") {
            @Override
            public void actionPerformed(ActionEvent e) {
//                log("last button pressed");
                if (lastBut.getText().equals("Current")) {
                    lastBut.setText("Last");
                    stb.setLast(false);

                } else {
                    lastBut.setText("Current");
                    openerBut.setText("Opener");
                    stb.setLast(true);
                }
                stb.resetCurrentScreenStates();

            }
        };
        lastBut = new JButton(lastaction);

        lastaction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0));

        lastBut.getActionMap().put("lastAction", lastaction);
        lastBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) lastaction.getValue(Action.ACCELERATOR_KEY), "lastAction");

        Action openeraction = new AbstractAction("Opener") {
            @Override
            public void actionPerformed(ActionEvent e) {
//                log("opener button pressed");
                if (openerBut.getText().equals("Current")) {
                    openerBut.setText("Opener");
                    stb.setOpener(false);
                    stb.showCurrent();
                } else {
                    openerBut.setText("Current");
                    lastBut.setText("Last");
                    stb.setOpener(true);
                }
                stb.resetCurrentScreenStates();
            }
        };
        openerBut = new JButton(openeraction);
        openeraction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));

        openerBut.getActionMap().put("openerAction", openeraction);
        openerBut.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) openeraction.getValue(Action.ACCELERATOR_KEY), "openerAction");
        sortBut.addActionListener(ae -> {
            log("sort button pressed");
            if (sortBut.getText().equals("Time sort")) {
                stb.setSort(true);
                sortBut.setText("Gm# sort");
            } else {
                stb.setSort(false);
                sortBut.setText("Time sort");
            }
            ((MainScreen) stb.getSelectedComponent()).getDataModels().sortGamesForAllTableSections();
        });

        addBookieBut.addActionListener(ae -> {
            checkAndRunInEDT(() -> {
                AnchoredLayeredPane anchoredLayeredPane = new AnchoredLayeredPane(stb, stb,LayedPaneIndex.SportConfigIndex);
                BookieColumnController2 bcc2 = new BookieColumnController2(anchoredLayeredPane);
            });
        });

        //owen this one we will have to repaint somehow
        shrinkTeamBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                log("shrinkTeamBut button pressed");
                if (shrinkTeamBut.getText().equals("Short Team")) {
                    stb.setShort(true);
                    shrinkTeamBut.setText("Long Team");
                    stb.rebuildMainScreen();
                } else {
                    stb.setShort(false);
                    shrinkTeamBut.setText("Short Team");
                    stb.rebuildMainScreen();

                }
            }
        });


        alertBut.addActionListener(ae -> {
            UrgentMessage urgent = new UrgentMessage("THIS IS SO URGENT!!!!!!");
        });

    }

    public void unBindAlertsComp() {
        if (null != alertsComp) {
            AppController.alertsVector.unBind(alertsComp);
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        // if the state combobox is changed
        if (e.getSource() == cb) {
            stb.setDisplay(display[cb.getSelectedIndex()]);
//            stb.refreshCurrentTab();
            stb.updateMainScreen();
            stb.requestFocusInWindow();
        } else if (e.getSource() == periodcb) {
            stb.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            stb.updateMainScreen();
            stb.requestFocusInWindow();
            //owen took out getting period and siaplay from ltd
            //	com.sia.client.ui.ChartView.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            //	SoccerChartView.setPeriod(perioddisplay[periodcb.getSelectedIndex()]);
            log("JUST SET PERIOD=" + perioddisplay[periodcb.getSelectedIndex()]);
        }
    }
}