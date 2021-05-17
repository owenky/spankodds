package com.sia.client.ui;

import com.sia.client.ui.simulator.EventGenerator;
import com.sia.client.ui.simulator.NewHeaderCreator;
import com.sia.client.ui.simulator.TableProperties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpankOddsTest {

    private static final Integer[] barRowIndex = new Integer[]{1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90};
    private static final Set<Integer> barRowSet = new HashSet<>(Arrays.asList(barRowIndex));
    private static final int testMainTableLastLockedColumnIndex = 3;
    private static final int testMainTableModelRowCount = 100;
    private static final int testMainTableModelColumnCount = 46;
    private static int updatedRow = 0;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableProperties testMainTableProps = TableProperties.of(testMainTableModelRowCount,testMainTableModelColumnCount,testMainTableLastLockedColumnIndex,barRowSet);
        TableProperties blankTableProps = TableProperties.of(testMainTableModelRowCount,testMainTableModelColumnCount,1,barRowSet);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Blank", blankTableProps.tableContainer);
        tabbedPane.addTab("Second", testMainTableProps.tableContainer);
        jFrame.getContentPane().add(tabbedPane);

        jFrame.setPreferredSize(new Dimension(1500, 800));
        jFrame.setLocation(new Point(250,100));
        jFrame.pack();
        jFrame.setVisible(true);
//        autoUpdateTableData(testMainTableProps);
    }
    private static void autoUpdateTableData(TableProperties tblProp) {
        Timer updateTimer = new Timer(8000, (event) -> {

            EventGenerator eventGenerator;
//            eventGenerator = new NewDataRowCreator(barRowSet);
//            eventGenerator = new ColumnWidthAdjuster(updatedRow);
//            eventGenerator = new DataRowDeletor();
            eventGenerator = new NewHeaderCreator();
            eventGenerator.generatEvent(tblProp);
        });
        updateTimer.setInitialDelay(3000);
        updateTimer.start();
    }




}
