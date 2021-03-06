package com.sia.client.ui;

import com.sia.client.ui.simulator.EmptySection;
import com.sia.client.ui.simulator.EventGenerator;
import com.sia.client.ui.simulator.SportsTabPaneTest;
import com.sia.client.ui.simulator.TableProperties;
import com.sia.client.ui.simulator.TestGameCache;
import com.sia.client.ui.simulator.UrgentMessageComponent;

import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;

public class SpankOddsTest {

    public static TableProperties[] tbleProps;
    public static final TestGameCache testGameCache = new TestGameCache();
    private static final int testMainTableLastLockedColumnIndex = 3;
    private static final int secCount = 100;
    private static final int rowCount = 5;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableProperties tbleProps0 = TableProperties.of("pane 0",testGameCache, secCount, rowCount, testMainTableLastLockedColumnIndex, TestGameCache.colCount, 0);
        TableProperties tbleProps1 = TableProperties.of("pane 1",testGameCache, secCount, rowCount, 1, TestGameCache.colCount, 1);
        TableProperties tbleProps2 = TableProperties.of("pane 2",testGameCache, 0, 0, 1, TestGameCache.colCount, 2);
        tbleProps = new TableProperties[]{tbleProps0, tbleProps1, tbleProps2};


        SportsTabPaneTest tabbedPane = new SportsTabPaneTest();
        for (TableProperties tblProp : tbleProps) {
            tabbedPane.addTab(tblProp.table.getName(), tblProp.getMainScreen());
        }
        tabbedPane.addTab("UrgentMessageBox",new UrgentMessageComponent());
        jFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        jFrame.setSize(new Dimension(1500, 800));
        jFrame.setLocation(new Point(250, 100));
        jFrame.setVisible(true);
        autoUpdateTableData(tbleProps);
    }

    private static void autoUpdateTableData(TableProperties[] tblProps) {
        final EventGenerator eventGenerator;
//            eventGenerator = new NewGameCreator();
//            eventGenerator = new ColumnWidthAdjuster();
//            eventGenerator = new GameDeletor();
//            eventGenerator = new NewHeaderCreator();
//            eventGenerator = new GameMover();
        eventGenerator = new EmptySection();
//        eventGenerator = new CheckToFileTest();
//        eventGenerator = new CheckToFileTest();
        Timer updateTimer = new Timer(3000, (event) -> {
            try {
                eventGenerator.generatEvent(tblProps);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        updateTimer.setInitialDelay(3000);
        updateTimer.start();
    }


}
