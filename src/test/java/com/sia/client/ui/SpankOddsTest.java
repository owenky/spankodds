package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;
import com.sia.client.ui.TableRowHeaderManager.ColumnHeaderStruct;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class SpankOddsTest {

    private static final Integer [] barRowIndex = new Integer [] {1,3,5,10,20,30,40,50,60,70,80,90};
    private static final int lastLockedColumnIndex = 3;
    private static final int totalRowCount = 100;
    private static final int totalColumnCount = 46;
    public static void main(String [] argv) {

        ColumnHeaderProvider columnHeaderProvider = createColumnHeaderProvider();
        JFrame jFrame = new JFrame();
        ColumnCustomizableTable jtable = new ColumnCustomizableTable(false, columnHeaderProvider) {

            @Override
            public TableCellRenderer getUserCellRenderer(final int rowViewIndex, final int colViewIndex) {
               return new DefaultTableCellRenderer();
            }
        };
        jtable.setRowHeight(60);
        jtable.setIntercellSpacing(new Dimension(4,2));

        TableRowHeaderManager tableRowHeaderManager = new TableRowHeaderManager(jtable,GameGropHeaderManager.DefaultTitleColor,GameGropHeaderManager.DefaultTitleFont,SiaConst.GameGroupHeaderHeight);
        final List<ColumnHeaderStruct> columnHeaderStructList = Arrays.stream(barRowIndex).map(ind -> new ColumnHeaderStruct("TEST "+ind,ind)).collect(Collectors.toList());
        tableRowHeaderManager.setColumnHeaderList(()->columnHeaderStructList);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int columnCount=totalColumnCount;
        Vector<String> colIden = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            colIden.add("col"+i);
        }

        Vector<Vector<String>> dataVector = new Vector<>();
        for(int i=0;i<totalRowCount;i++) {
            dataVector.add(makeRow(i,columnCount));
        }

        ((DefaultTableModel)jtable.getModel()).setDataVector(dataVector, colIden);
        JComponent tableContainer = TableUtils.configTableLockColumns(jtable,lastLockedColumnIndex);
        jFrame.getContentPane().add(tableContainer);

        tableRowHeaderManager.installListeners();

        jFrame.setSize(new Dimension(1500,800));
        jFrame.pack();
        jFrame.show();
    }
    private static Vector<String> makeRow(int row,int colCount) {
        Vector<String> rowData = new Vector<>();
        for(int i=0;i<colCount;i++) {
            if ( isHeaderRow(row)) {
//                rowData.add(SiaConst.GameGroupHeaderIden);
                rowData.add("" + row + "_" + i);
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
    private static ColumnHeaderProvider createColumnHeaderProvider() {
        return () -> {
            Color haderBackground = SiaConst.DefaultHeaderColor;
            int columnHeaderHeight = SiaConst.GameGroupHeaderHeight;
            Set<Integer> columnHeaderIndexSet = Arrays.stream(barRowIndex).collect(Collectors.toSet());
            return new ColumnHeaderProperty(haderBackground,columnHeaderHeight,columnHeaderIndexSet);
        };
    }
}
