package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.ui.ColumnCustomizableTable;
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
import java.util.Vector;

public class TableProperties {

    public static TableProperties of(int sectionCount, int sectionRowCount,int boundaryIndex,int colCount) {

        TableProperties rtn = new TableProperties() ;
        rtn.testGameCache = new TestGameCache();
        rtn.table = new ColumnCustomizableTable<TestGame>(false,makeColumns(colCount)) {

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
            public ColumnCustomizableDataModel<TestGame> createModel(Vector<TableColumn> allColumns) {
                return new TestDataModel(allColumns);
            }
        };
        JTableHeader tableHeader = rtn.table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        rtn.table.setRowHeight(60);
        rtn.table.setIntercellSpacing(new Dimension(2, 2));
        buildModels(rtn, sectionCount,sectionRowCount);

        rtn.tableContainer = TableUtils.configTableLockColumns(rtn.table, boundaryIndex);
        return rtn;
    }
    private static void buildModels(TableProperties tblProp,int sectionCount, int sectionRowCount) {
        for(int secIndex=0;secIndex<sectionCount;secIndex++) {
            TestTableSection testTableSection = TestTableSection.createTestTableSection(tblProp.testGameCache,secIndex,sectionRowCount);
            tblProp.table.getModel().addGameLine(testTableSection);
        }
        tblProp.table.getModel().buildIndexMappingCache();
    }
    private static Vector<TableColumn> makeColumns(int columnCount) {

        Vector<TableColumn> rtn = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            TableColumn column = new TableColumn();
            column.setHeaderValue("col_"+i);
            rtn.add(column);
        }
        return rtn;
    }
    public TestGameCache testGameCache;
    public ColumnCustomizableTable<TestGame> table;
    public JComponent tableContainer;
}
