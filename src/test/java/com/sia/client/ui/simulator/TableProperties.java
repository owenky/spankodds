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

    public static TableProperties of(String name,TestGameCache testGameCache,int sectionCount, int sectionRowCount,int boundaryIndex,int colCount, int tableIndex) {

        TableProperties rtn = new TableProperties() ;
        rtn.name = name;
        rtn.sections = new TestTableSection[sectionCount];
        rtn.sectionRowCount = sectionRowCount;
        rtn.tableIndex = tableIndex;
        rtn.testGameCache = testGameCache;
        rtn.boundaryIndex = boundaryIndex;
        rtn.colCount = colCount;
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
        ColumnCustomizableDataModel<TestGame> tm = new ColumnCustomizableDataModel<>(makeColumns(colCount),"testName");
        table = new ColumnCustomizableTable<TestGame>(false,tm) {

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
        JTableHeader tableHeader = table.getTableHeader();
        Font headerFont = new Font("Verdana", Font.BOLD, 11);
        tableHeader.setFont(headerFont);
        table.setRowHeight(60);
        table.setIntercellSpacing(new Dimension(2, 2));
        table.setName("Table "+tableIndex);
        buildModels(sectionRowCount,tableIndex);
        JComponent tableContainer = TableUtils.configTableLockColumns(table, boundaryIndex);
        mainScreen.removeAll();
        mainScreen.setLayout(new BorderLayout());
        mainScreen.add(tableContainer,BorderLayout.CENTER);
    }
    public MainScreenTest getMainScreen() {
        return mainScreen;
    }
    public TestTableSection [] getSections() {
        return sections;
    }
    public String getName() {
        return this.name;
    }
    private void buildModels(int sectionRowCount, int tableIndex) {
        for(int secIndex=0;secIndex<sections.length;secIndex++) {
            sections[secIndex] = TestTableSection.createTestTableSection(testGameCache,secIndex,sectionRowCount,tableIndex);
            table.getModel().addGameLine(sections[secIndex] );
        }
        table.getModel().buildIndexMappingCache(true);
    }
    private MainScreenTest mainScreen;
    private TestTableSection [] sections;
    private int sectionRowCount;
    private int tableIndex;
    private int boundaryIndex;
    private int colCount;
    private String name;
    public TestGameCache testGameCache;
    public ColumnCustomizableTable<TestGame> table;
}
