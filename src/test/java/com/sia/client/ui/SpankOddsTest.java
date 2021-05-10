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

    private static final Integer [] barRowIndex = new Integer [] {1,3,5,10};
    public static void main(String [] argv) {

        JFrame jFrame = new JFrame();
        ColumnLockableTable jtable = new ColumnLockableTable(true);
        jtable.setRowHeight(60);

        TableRowHeaderManager tableRowHeaderManager = new TableRowHeaderManager(jtable,GameGropHeaderManager.DefaultTitleColor,GameGropHeaderManager.DefaultTitleFont,SiaConst.GameGroupHeaderHeight);
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
        JComponent tableContainer = TableUtils.configTableLockColumns(jtable,2);
        jFrame.getContentPane().add(tableContainer);

        tableRowHeaderManager.installListeners();

        jFrame.setSize(new Dimension(250,100));
        jFrame.pack();
        jFrame.show();
    }
    private static Vector<String> makeRow(int row,int colCount) {
        Vector<String> rowData = new Vector<>();
        for(int i=0;i<colCount;i++) {
            if ( isHeaderRow(row)) {
                rowData.add(SiaConst.GameGroupHeaderIden);
            } else {
                rowData.add("" + row + "_" + i);
            }
        }

        return rowData;
    }
    private static boolean isHeaderRow(int row) {
        boolean status = false;
        for ( int barRow:barRowIndex) {
            if ( barRow == row) {
                status = true;
                break;
            }
        }
        return status;
    }
}
