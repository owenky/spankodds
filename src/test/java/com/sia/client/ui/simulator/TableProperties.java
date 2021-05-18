package com.sia.client.ui.simulator;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.ui.ColumnCustomizableTable;
import com.sia.client.ui.TableColumnHeaderManager;
import com.sia.client.ui.TableUtils;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableProperties {

    public static TableProperties of(int rowCount, int columnCount, int boundaryIndex, Set<Integer> barRowSet) {

        TableProperties rtn = new TableProperties() ;
        rtn.rowCount = rowCount;
        rtn.columnCount = columnCount;
        rtn.dataVector = new ArrayList<>();
        ColumnHeaderProvider columnHeaderProvider = createColumnHeaderProvider(rtn.dataVector);
        rtn.table = new ColumnCustomizableTable<TestGame>(false, columnHeaderProvider) {

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
            public ColumnCustomizableDataModel<TestGame> createDefaultDataModel() {
                return new TestDataModel();
            }
        };
        JTableHeader tableHeader = rtn.table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        rtn.table.setRowHeight(60);
        rtn.table.setIntercellSpacing(new Dimension(2, 2));
        buildModels(rtn.table, rtn.dataVector,rowCount,columnCount,barRowSet);

        rtn.tableContainer = TableUtils.configTableLockColumns(rtn.table, boundaryIndex);
        return rtn;
    }
    private static void buildModels(ColumnCustomizableTable<TestGame> table,List<LabeledList> dataVector,int rowCount, int columnCount,Set<Integer> barRowSet) {
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = new TableColumn(i, 30);
            column.setHeaderValue("col" + i);
            table.addColumn(column);
        }

        for (int i = 0; i < rowCount; i++) {
            dataVector.add(EventGenerator.makeRow(i, columnCount,barRowSet));
        }
    }
    private static ColumnHeaderProvider createColumnHeaderProvider(List<LabeledList> dataVector) {
        return new ColumnHeaderProvider() {

            @Override
            protected ColumnHeaderProperty provide() {
                int columnHeaderHeight = SiaConst.GameGroupHeaderHeight;
                Map<Integer, Object> columnHeaderIndexMap = new HashMap<>();
                for (int index = 0; index < dataVector.size(); index++) {
                    LabeledList rowData = dataVector.get(index);
                    if (isHeaderRow(rowData)) {
                        columnHeaderIndexMap.put(index, rowData.getHeader());
                    }
                }
                return new ColumnHeaderProperty(SiaConst.DefaultHeaderColor, SiaConst.DefaultHeaderFontColor,SiaConst.DefaultHeaderFont,columnHeaderHeight, columnHeaderIndexMap);
            }
        };
    }
    private static boolean isHeaderRow(LabeledList rowData) {
        return  null !=rowData.getHeader();
    }
    public TableColumnHeaderManager tableColumnHeaderManager;
    public ColumnCustomizableTable<TestGame> table;
    public JComponent tableContainer;
    public List<LabeledList> dataVector;
    public int rowCount;
    public int columnCount;
}
