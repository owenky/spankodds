package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnAdjustScheduler;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;
import com.sia.client.model.TableModelRowData;
import com.sia.client.ui.TableColumnHeaderManager.ColumnHeaderStruct;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class SpankOddsTest {

    private static final Integer[] barRowIndex = new Integer[]{1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90};
    private static final int lastLockedColumnIndex = 3;
    private static final int totalRowCount = 100;
    private static final int totalColumnCount = 46;
    private static final List<List<String>> dataVector = new ArrayList<>();
    private static final ColumnAdjustScheduler COLUMN_ADJUST_SCHEDULER = new ColumnAdjustScheduler();
    private static ColumnCustomizableTable theTestTable;
    private static int updatedRow = 0;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        theTestTable = createTestTable();
        theTestTable.setRowHeight(60);
        theTestTable.setIntercellSpacing(new Dimension(2, 2));

        TableColumnHeaderManager tableColumnHeaderManager = new TableColumnHeaderManager(theTestTable, GameGropHeaderManager.DefaultTitleColor, GameGropHeaderManager.DefaultTitleFont, SiaConst.GameGroupHeaderHeight);
        final List<ColumnHeaderStruct> columnHeaderStructList = Arrays.stream(barRowIndex).map(ind -> new ColumnHeaderStruct("TEST " + ind, ind)).collect(Collectors.toList());
        tableColumnHeaderManager.setColumnHeaderList(() -> columnHeaderStructList);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int columnCount = totalColumnCount;
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = new TableColumn(i, 30);
            column.setHeaderValue("col" + i);
            theTestTable.addColumn(column);
        }

        for (int i = 0; i < totalRowCount; i++) {
            dataVector.add(makeRow(i, columnCount));
        }

        JComponent tableContainer = TableUtils.configTableLockColumns(theTestTable, lastLockedColumnIndex);
        jFrame.getContentPane().add(tableContainer);

        tableColumnHeaderManager.installListeners();

        jFrame.setSize(new Dimension(1500, 800));
        jFrame.pack();
        jFrame.show();
//        autoUpdateTableData();
    }

    private static ColumnCustomizableTable createTestTable() {
        ColumnHeaderProvider columnHeaderProvider = createColumnHeaderProvider();
        ColumnCustomizableTable rtn = new ColumnCustomizableTable(false, columnHeaderProvider) {

            @Override
            public TableCellRenderer getUserCellRenderer(final int rowViewIndex, final int colViewIndex) {
                return new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        JComponent rtn = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        return rtn;
                    }
                };
            }

            @Override
            public ColumnCustomizableDataModel createDefaultDataModel() {
                return new ColumnCustomizableDataModel() {

                    @Override
                    public int getRowCount() {
                        return dataVector.size();
                    }

                    @Override
                    public Object getValueAt(final int rowIndex, final int columnIndex) {
                        Object value = dataVector.get(rowIndex).get(columnIndex);
                        return value;
                    }

                    @Override
                    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {

                    }
                };
            }
        };
        JTableHeader tableHeader = rtn.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        return rtn;
    }

    private static List<String> makeRow(int row, int colCount) {
        List<String> rowData = new ArrayList<>();
        for (int i = 0; i < colCount; i++) {
            rowData.add("" + row + "_" + i);
        }

        return rowData;
    }

    private static void autoUpdateTableData() {
        Timer updateTimer = new Timer(8000, (event) -> {
            System.out.println("data updated " + new Date());
//            testColumnAdjuster();
            testColumnHeader();
        });
        updateTimer.setInitialDelay(1000);
        updateTimer.start();

    }
    private static void testColumnHeader() {
        int insertedRow = 0;
        List<String> newRow = makeRow(0, totalColumnCount);
        dataVector.add(insertedRow,newRow);
        TableModelEvent e = new TableModelEvent(theTestTable.getModel(),insertedRow,insertedRow,ALL_COLUMNS,TableModelEvent.INSERT);
        theTestTable.getModel().fireTableChanged(e);
    }
    private static void testColumnAdjuster() {
        int col = 0;
        String value = dataVector.get(updatedRow).get(col);
        dataVector.get(updatedRow).set(col, value + "XX");

        col = 5;
        value = dataVector.get(updatedRow).get(col);
        dataVector.get(updatedRow).set(col, value + "XX");

        TableModelEvent te = new TableModelEvent(theTestTable.getModel(), updatedRow);
        theTestTable.getModel().fireTableChanged(te);
        COLUMN_ADJUST_SCHEDULER.addRowData(new TestRowData(theTestTable,updatedRow));
    }
    private static ColumnHeaderProvider createColumnHeaderProvider() {
        return () -> {
            Color haderBackground = SiaConst.DefaultHeaderColor;
            int columnHeaderHeight = SiaConst.GameGroupHeaderHeight;
            Set<Integer> columnHeaderIndexSet = new HashSet<>();
            for(int index=0;index<dataVector.size();index++) {
               List<String> rowData = dataVector.get(index);
               if ( isHeaderRow(rowData)) {
                   columnHeaderIndexSet.add(index);
               }
            }
            return new ColumnHeaderProperty(haderBackground, columnHeaderHeight, columnHeaderIndexSet);
        };
    }

    private static boolean isHeaderRow(List<String> rowData) {
        boolean status = false;
        String originalIndexStr = rowData.get(0).split("_")[0];
        int originalIndex = Integer.parseInt(originalIndexStr);
        for (int barRow : barRowIndex) {
            if (barRow == originalIndex) {
                status = true;
                break;
            }
        }
        return status;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class TestRowData implements TableModelRowData {

        private final ColumnCustomizableTable testTable;
        private final int rowModelIndex;
        public TestRowData(ColumnCustomizableTable testTable,int rowModelIndex) {
            this.testTable=testTable;
            this.rowModelIndex = rowModelIndex;
        }

        @Override
        public Integer getRowModelIndex() {
            return rowModelIndex;
        }

        @Override
        public ColumnCustomizableTable getTable() {
            return testTable;
        }
    }
}
