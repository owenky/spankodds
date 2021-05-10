package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.TableRowHeaderManager.ColumnHeaderStruct;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class SpankOddsTest {

    private static final Integer [] barRowIndex = new Integer [] {1,3,5};
    private static final Integer [] lockColumnIndex = new Integer[]{0,1,2};
    public static void main(String [] argv) {

        JFrame jFrame = new JFrame();
        ColumnLockableTable jtable = new ColumnLockableTable(false);
        jtable.setRowHeight(60);

        TableRowHeaderManager tableRowHeaderManager = new TableRowHeaderManager(jtable,GameGropHeaderManager.DefaultTitleColor,GameGropHeaderManager.DefaultTitleFont,SiaConst.GameGroupHeaderHeight+20);
        final List<ColumnHeaderStruct> columnHeaderStructList = Arrays.stream(barRowIndex).map(ind -> new ColumnHeaderStruct("TEST "+ind,ind)).collect(Collectors.toList());
        tableRowHeaderManager.setColumnHeaderList(()->columnHeaderStructList);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int columnCount=46;
        Vector<String> colIden = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            colIden.add("col"+i);
        }

        Vector<Vector<String>> dataVector = new Vector<>();
        for(int i=0;i<100;i++) {
            dataVector.add(makeRow(i,columnCount));
        }

        ((DefaultTableModel)jtable.getModel()).setDataVector(dataVector, colIden);
        jtable.removeLockedColumnIndex(lockColumnIndex);
        JComponent tableContainer = TableUtils.configTableLockColumns(jtable);
        jFrame.getContentPane().add(tableContainer);

        tableRowHeaderManager.installListeners();

        jFrame.setSize(new Dimension(250,100));
        jFrame.pack();
        jFrame.show();
    }
    private static Vector<String> makeRow(int seed,int colCount) {
        Vector<String> row = new Vector<>();
        for(int i=0;i<colCount;i++) {
            row.add(""+seed+"_"+i);
        }

        return row;
    }
}
