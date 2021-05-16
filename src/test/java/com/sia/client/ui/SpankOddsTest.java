package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnAdjustScheduler;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;
import com.sia.client.model.TableModelRowData;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.swing.event.TableModelEvent.ALL_COLUMNS;

public class SpankOddsTest {

    private static final Integer[] barRowIndex = new Integer[]{1, 3, 5, 10, 20, 30, 40, 50, 60, 70, 80, 90};
    private static final Set<Integer> barRowSet = new HashSet<>(Arrays.asList(barRowIndex));
    private static final int testMainTableLastLockedColumnIndex = 3;
    private static final int testMainTableModelRowCount = 100;
    private static final int testMainTableModelColumnCount = 46;
    private static final ColumnAdjustScheduler COLUMN_ADJUST_SCHEDULER = new ColumnAdjustScheduler();
    private static int updatedRow = 0;

    public static void main(String[] argv) {

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TableProperties testMainTableProps = createTestTable(testMainTableModelRowCount,testMainTableModelColumnCount,testMainTableLastLockedColumnIndex);
        TableProperties blankTableProps = createTestTable(0,0,0);



        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab("Blank", blankTableProps.tableContainer);
        tabbedPane.addTab("Second", testMainTableProps.tableContainer);
        jFrame.getContentPane().add(tabbedPane);

        jFrame.setSize(new Dimension(1500, 800));
        jFrame.pack();
        jFrame.show();
//        autoUpdateTableData(testMainTableProps);
    }
    private static TableProperties createTestTable(int rowCount, int columnCount,int boundaryIndex) {

        TableProperties rtn = new TableProperties() ;
        rtn.rowCount = rowCount;
        rtn.columnCount = columnCount;
        rtn.dataVector = new ArrayList<>();
        ColumnHeaderProvider columnHeaderProvider = createColumnHeaderProvider(rtn.dataVector);
        rtn.table = new ColumnCustomizableTable(false, columnHeaderProvider) {

            @Override
            public TableCellRenderer getUserCellRenderer(final int rowViewIndex, final int colViewIndex) {
                return new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                };
            }

            @Override
            public ColumnCustomizableDataModel createDefaultDataModel() {
                return new ColumnCustomizableDataModel() {

                    @Override
                    public int getRowCount() {
                        return rtn.dataVector.size();
                    }

                    @Override
                    public Object getValueAt(final int rowIndex, final int columnIndex) {
                        return rtn.dataVector.get(rowIndex).get(columnIndex);
                    }

                    @Override
                    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {

                    }
                };
            }
        };
        JTableHeader tableHeader = rtn.table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        rtn.table.setRowHeight(60);
        rtn.table.setIntercellSpacing(new Dimension(2, 2));
        buildModels(rtn.table, rtn.dataVector,rowCount,columnCount);
        
        rtn.tableContainer = TableUtils.configTableLockColumns(rtn.table, boundaryIndex);
        return rtn;
    }
    private static void buildModels(ColumnCustomizableTable table,List<LabeledList> dataVector,int rowCount, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = new TableColumn(i, 30);
            column.setHeaderValue("col" + i);
            table.addColumn(column);
        }

        for (int i = 0; i < rowCount; i++) {
            dataVector.add(makeRow(i, columnCount,true));
        }
    }
    private static LabeledList makeRow(int row, int colCount,boolean toSetHeader) {
        LabeledList rowData = new LabeledList();
        for (int i = 0; i < colCount; i++) {
            rowData.add("" + row + "_" + i);
        }
        if (toSetHeader && barRowSet.contains(row)) {
            rowData.setHeader("TEST ROW "+row);
        }
        return rowData;
    }

    private static void autoUpdateTableData(TableProperties tblProp) {
        Timer updateTimer = new Timer(8000, (event) -> {
//            testColumnAdjuster();
            testColumnHeaderWithRowInserted(tblProp.table,tblProp.dataVector,tblProp.columnCount);
//            testColumnHeaderWithRowDeleted(tblProp.table,tblProp.dataVector);
        });
        updateTimer.setInitialDelay(3000);
        updateTimer.start();
    }
    private static void testColumnHeaderWithRowDeleted(ColumnCustomizableTable table,List<LabeledList> dataVector) {
        int deletedRow = -1;
        for ( int i=0;i<dataVector.size();i++) {
            LabeledList row = dataVector.get(i);
            if ( null == row.getHeader()) {
                deletedRow = i;
                break;
            }
        }
        if ( deletedRow>=0) {
            dataVector.remove(deletedRow);
            TableModelEvent e = new TableModelEvent(table.getModel(), deletedRow, deletedRow, ALL_COLUMNS, TableModelEvent.DELETE);
            table.getModel().fireTableChanged(e);
        }
    }
    private static void testColumnHeaderWithRowInserted(ColumnCustomizableTable table,List<LabeledList> dataVector,int columnCount) {
        int insertedRow = 2;
        LabeledList newRow = makeRow(insertedRow, columnCount,false);
        dataVector.add(insertedRow, newRow);
        TableModelEvent e = new TableModelEvent(table.getModel(), insertedRow, insertedRow, ALL_COLUMNS, TableModelEvent.INSERT);
        table.getModel().fireTableChanged(e);
    }

    private static void testColumnAdjuster(ColumnCustomizableTable table,List<LabeledList> dataVector) {
        int col = 0;
        String value = dataVector.get(updatedRow).get(col);
        dataVector.get(updatedRow).set(col, value + "XX");

        col = 5;
        value = dataVector.get(updatedRow).get(col);
        dataVector.get(updatedRow).set(col, value + "XX");

        TableModelEvent te = new TableModelEvent(table.getModel(), updatedRow);
        table.getModel().fireTableChanged(te);
        COLUMN_ADJUST_SCHEDULER.addRowData(new TestRowData(table, updatedRow));
    }

    private static ColumnHeaderProvider createColumnHeaderProvider(List<LabeledList> dataVector) {
        return () -> {
            int columnHeaderHeight = SiaConst.GameGroupHeaderHeight;
            Map<Integer, Object> columnHeaderIndexMap = new HashMap<>();
            for (int index = 0; index < dataVector.size(); index++) {
                LabeledList rowData = dataVector.get(index);
                if (isHeaderRow(rowData)) {
                    columnHeaderIndexMap.put(index, rowData.getHeader());
                }
            }
            return new ColumnHeaderProperty(SiaConst.DefaultHeaderColor, SiaConst.DefaultHeaderFontColor,SiaConst.DefaultHeaderFont,columnHeaderHeight, columnHeaderIndexMap);
        };
    }

    private static boolean isHeaderRow(LabeledList rowData) {
       return  null !=rowData.getHeader();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class TestRowData implements TableModelRowData {

        private final ColumnCustomizableTable testTable;
        private final int rowModelIndex;

        public TestRowData(ColumnCustomizableTable testTable, int rowModelIndex) {
            this.testTable = testTable;
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    private static class LabeledList extends ArrayList<String> {


        private String header;

        public void setHeader(String header) {
            this.header = header;
        }

        public String getHeader() {
            return this.header;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private static class TableProperties {
        public TableColumnHeaderManager tableColumnHeaderManager;
        ColumnCustomizableTable table;
        JComponent tableContainer;
        List<LabeledList> dataVector;
        int rowCount;
        int columnCount;
    }
}
