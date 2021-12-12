package com.sia.client.ui;

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

    public MainScreenLoader(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }
    public void load() {
//        this.execute();
        showLoadingPrompt();
        done();
    }
    @Override
    protected Void doInBackground() throws Exception {
        showLoadingPrompt();
        Thread.sleep(5000L);
        return null;
    }
    @Override
    public void done() {
        ScrollablePanel tablePanel = new ScrollablePanel();
        tablePanel.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
        //changed this to stretch for Vertical Scroll Bar to appear if frame is resized and data can not fit in viewport
        tablePanel.setScrollableHeight(ScrollablePanel.ScrollableSizeHint.STRETCH);

        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.getViewport().putClientProperty("EnableWindowBlit", Boolean.TRUE);
        scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

        mainScreen.removeAll();
//        mainScreen.revalidate();
        JComponent mainTableContainer = makeMainTableScrollPane(mainScreen.getColumnCustomizableTable());
        mainScreen.add(mainTableContainer, BorderLayout.CENTER);
        mainScreen.validate();
        log("done drawing");
        AppController.notifyUIComplete();
    }
    private JComponent makeMainTableScrollPane(MainGameTable table) {
        return TableUtils.configTableLockColumns(table, AppController.getNumFixedCols());
    }
    private void showLoadingPrompt() {
        ImageIcon loadgif = null;
        JLabel loadlabel = new JLabel("loading...", loadgif, JLabel.CENTER);
        loadlabel.setOpaque(true);
        mainScreen.setLayout(new BorderLayout(0, 0));
        mainScreen.setOpaque(true);
        mainScreen.add(loadlabel);
    }
}
