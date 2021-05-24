package com.sia.client.ui;

import com.sia.client.ui.simulator.EventGenerator;
import com.sia.client.ui.simulator.NewGameCreator;
import com.sia.client.ui.simulator.TableProperties;
import com.sia.client.ui.simulator.TestGameCache;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

public class SpankOddsTest {

    private static final int testMainTableLastLockedColumnIndex = 3;
    private static final TestGameCache testGameCache = new TestGameCache();
    private static final int secCount = 100;
    private static final int rowCount = 5;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableProperties tbleProps0 = TableProperties.of(testGameCache,secCount,rowCount,testMainTableLastLockedColumnIndex, TestGameCache.colCount, 0);
        TableProperties tbleProps1 = TableProperties.of(testGameCache,secCount,rowCount,1,TestGameCache.colCount,1);
        TableProperties tbleProps2 = TableProperties.of(testGameCache,0,0,1,TestGameCache.colCount,2);
        TableProperties [] tbleProps  = new TableProperties [] {tbleProps0,tbleProps1,tbleProps2};

        JTabbedPane tabbedPane = new JTabbedPane();
        for(TableProperties tblProp:tbleProps) {
            tabbedPane.addTab(tblProp.table.getName(), tblProp.tableContainer);
        }
        jFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        jFrame.setSize(new Dimension(1500, 800));
        jFrame.setLocation(new Point(250,100));
        jFrame.setVisible(true);
        autoUpdateTableData(tbleProps);
    }
    private static void autoUpdateTableData(TableProperties [] tblProps) {
        final EventGenerator eventGenerator;
            eventGenerator = new NewGameCreator();
//            eventGenerator = new ColumnWidthAdjuster();
//            eventGenerator = new GameDeletor();
//            eventGenerator = new NewHeaderCreator();
//            eventGenerator = new GameMover();
//        eventGenerator = new CheckToFire();
        Timer updateTimer = new Timer(3000, (event) -> {
            eventGenerator.generatEvent(tblProps);
        });
        updateTimer.setInitialDelay(3000);
        updateTimer.start();
    }




}
