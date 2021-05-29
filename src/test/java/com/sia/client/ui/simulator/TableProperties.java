package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.ui.ColumnCustomizableTable;
import com.sia.client.ui.RowHeaderTable;
import com.sia.client.ui.TableUtils;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

public class TableProperties {

    public static TableProperties of(TestGameCache testGameCache,int sectionCount, int sectionRowCount,int boundaryIndex,int colCount, int tableIndex) {

        TableProperties rtn = new TableProperties() ;
        rtn.sectionCount = sectionCount;
        rtn.sectionRowCount = sectionRowCount;
        rtn.tableIndex = tableIndex;
        rtn.testGameCache = testGameCache;
        rtn.boundaryIndex = boundaryIndex;
        ColumnCustomizableDataModel<TestGame> tm = new ColumnCustomizableDataModel<>(makeColumns(colCount));
        rtn.table = new ColumnCustomizableTable<TestGame>(false,tm) {

            @Override
            public TableCellRenderer getUserCellRenderer(final int rowViewIndex, final int colViewIndex) {
                return new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        ColumnCustomizableTable<?> mainTable;
                        if ( table instanceof RowHeaderTable) {
                            mainTable = ((RowHeaderTable<?>)table).getMainTable();
                        } else {
                            mainTable = (ColumnCustomizableTable<?>)table;
                        }
                        int gameId = mainTable.getModel().getRowKey(rowViewIndex);
                        Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if ( gameId == GameMover.movedGameId) {
                            renderer.setBackground(Color.gray);
                        } else {
                            renderer.setBackground(Color.white);
                        }
                        return renderer;
                    }
                };
            }
        };
        JTableHeader tableHeader = rtn.table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        rtn.table.setRowHeight(60);
        rtn.table.setIntercellSpacing(new Dimension(2, 2));
        rtn.table.setName("Table "+tableIndex);
        rtn.mainScreen = new MainScreenTest(rtn);
        rtn.rebuild();
        return rtn;
    }
    private static Vector<TableColumn> makeColumns(int columnCount) {

        Vector<TableColumn> rtn = new Vector<>();
        for(int i=0;i<columnCount;i++) {
            TableColumn column = new TableColumn();
            column.setHeaderValue("COLUMN_"+i);
            rtn.add(column);
        }
        return rtn;
    }
    public void rebuild() {
        buildModels(sectionCount,sectionRowCount,tableIndex);
        JComponent tableContainer = TableUtils.configTableLockColumns(table, boundaryIndex);
        mainScreen.removeAll();
        mainScreen.setLayout(new BorderLayout());
        mainScreen.add(tableContainer,BorderLayout.CENTER);
    }
    public MainScreenTest getMainScreen() {
        return mainScreen;
    }
    private void buildModels(int sectionCount, int sectionRowCount, int tableIndex) {
        for(int secIndex=0;secIndex<sectionCount;secIndex++) {
            TestTableSection testTableSection = TestTableSection.createTestTableSection(testGameCache,secIndex,sectionRowCount,tableIndex);
            table.getModel().addGameLine(testTableSection);
        }
        table.getModel().buildIndexMappingCache();
    }
    private MainScreenTest mainScreen;
    private int sectionCount;
    private int sectionRowCount;
    private int tableIndex;
    private int boundaryIndex;
    public TestGameCache testGameCache;
    public ColumnCustomizableTable<TestGame> table;
}
