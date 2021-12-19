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

import static com.sia.client.config.Utils.log;

public class MainScreenLoader extends SwingWorker<Void,Void> {

    private final MainScreen mainScreen;
    private MainGameTableModel mainGameTableModel;
    private static MainScreenLoader activeLoader;
    private Runnable listener;

    public MainScreenLoader(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public void load() {
        synchronized ( MainScreenLoader.class) {
            if ( null != activeLoader) {
                activeLoader.cancel(true);
            }
            activeLoader = this;
            Utils.checkAndRunInEDT(()-> {
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
        AppController.signalWindowLoading();
        mainGameTableModel = mainScreen.buildModel();
        return null;
    }
    @Override
    public void done() {
        try {
            if ( ! isCancelled()) {
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
            showLoadingError("Error occurred, Please try again");
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
    private void showLoadingError(String err) {
        showPrompt(err);
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
