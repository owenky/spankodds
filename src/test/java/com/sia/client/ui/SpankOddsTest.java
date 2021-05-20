package com.sia.client.ui;

import com.sia.client.ui.simulator.EventGenerator;
import com.sia.client.ui.simulator.NewHeaderCreator;
import com.sia.client.ui.simulator.TableProperties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Point;

public class SpankOddsTest {

    private static final int testMainTableLastLockedColumnIndex = 3;
    private static final int secCount = 20;
    private static final int rowCount = 5;
    private static final int testMainTableModelColumnCount = 46;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableProperties testMainTableProps = TableProperties.of(secCount,rowCount,testMainTableModelColumnCount,testMainTableLastLockedColumnIndex);
        TableProperties blankTableProps = TableProperties.of(secCount,rowCount,testMainTableModelColumnCount,1);

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
