package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.MainGameTableModel;
import com.sia.client.ui.control.MainScreen;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.sia.client.config.Utils.log;

public class MainScreenLoader extends SwingWorker<Void,Void> {

    private final MainScreen mainScreen;
    private MainGameTableModel mainGameTableModel;
    private static Map<Integer,MainScreenLoader> activeLoaders = new ConcurrentHashMap<>(4);
    private Runnable listener;
    private String err;

    public MainScreenLoader(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public void load() {
//        Utils.log(new Exception("debug screen reloading...."));
        synchronized ( MainScreenLoader.class) {
            MainScreenLoader activeLoaderOfWindow = activeLoaders.get(mainScreen.getWindowIndex());
            if ( null != activeLoaderOfWindow ) {
                log("Cancelling "+activeLoaderOfWindow.mainScreen.getSportType().getSportName()+" loading...");
                activeLoaderOfWindow.cancel(true);
            }
            activeLoaders.put(mainScreen.getWindowIndex(),this);
            Utils.checkAndRunInEDT(()-> {
                err = null;
                showLoadingPrompt();
                this.execute();
            });
        }
    }
    public void setListener(Runnable listener) {
        this.listener = listener;
    }
    @Override
    protected Void doInBackground()  {
        try {
            AppController.signalWindowLoading();
            mainGameTableModel = mainScreen.buildModel();
        }catch(Exception e) {
            log(e);
            err = e.getMessage();
        }
        return null;
    }
    @Override
    public void done() {
        try {
            if ( null != err) {
                showPrompt(err);
            } else if ( ! isCancelled()) {
                mainScreen.createColumnCustomizableTable(mainGameTableModel);
                MainGameTable mainGameTable = mainScreen.getColumnCustomizableTable();
                ScrollablePanel tablePanel = new ScrollablePanel();
                tablePanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
                //changed this to stretch for Vertical Scroll Bar to appear if frame is resized and data can not fit in viewport
                tablePanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);

                tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
                JScrollPane scrollPane = new JScrollPane(tablePanel);
                scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
                scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

                mainScreen.removeAll();
                JComponent mainTableContainer = makeMainTableScrollPane(mainGameTable);
                mainScreen.add(mainTableContainer, BorderLayout.CENTER);
                TableUtils.selectRowsFromConfig(mainGameTable);
                mainScreen.validate();
                if ( null != listener) {
                    listener.run();
                }
                log("done drawing");
            } else {
                log("loading "+mainScreen.getSportType().getSportName()+" has been cancelled.");
            }
        } catch(Exception e) {
            log(e);
            showLoadingError();
        }
        finally {
            AppController.notifyWindowLoadingComplete();
        }
    }
    private JComponent makeMainTableScrollPane(MainGameTable table) {
        return TableUtils.configTableLockColumns(table, AppController.getNumFixedCols());
    }
    private void showLoadingPrompt() {
        showPrompt("loading...");
    }
    private void showLoadingError() {
        showPrompt("Error occurred, Please try again");
    }
    private void showPrompt(String prompt) {
        mainScreen.removeAll();
        ImageIcon loadgif = null;
        JLabel loadlabel = new JLabel(prompt, loadgif, JLabel.CENTER);
        loadlabel.setOpaque(true);
        mainScreen.setLayout(new BorderLayout(0, 0));
        mainScreen.setOpaque(true);
        mainScreen.add(loadlabel);
        mainScreen.validate();
    }
}
