package com.sia.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;

public class RowHeaderTableTest extends JFrame {
    private static final int colCount = 47;
    private static final int rowCount = 1000;
    public RowHeaderTableTest() {
        super("Row Header Test");
        setSize(1500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        TableModel tm = new AbstractTableModel() {
            String data[] = createDataPrefix(colCount);
            String headers[] = createColumnTitles(colCount);

            public int getRowCount() {
                return rowCount;
            }

            public int getColumnCount() {
                return data.length;
            }

            // Synthesize some entries using the data values and the row number.
            public Object getValueAt(int row, int col) {
                return data[col] + row;
            }

            public String getColumnName(int col) {
                return headers[col];
            }
        };
        // Create a column model for the main table. This model ignores the first
        // column added and sets a minimum width of 150 pixels for all others.
        TableColumnModel cm = new DefaultTableColumnModel() {
            boolean first = true;

            public void addColumn(TableColumn tc) {
                // Drop the first column, which will be the row header.
                if (first) {
                    first = false;
                    return;
                }
                tc.setMinWidth(150);  // Just for looks, really...
                super.addColumn(tc);
            }
        };
        // Create a column model that will serve as our row header table. This model
        // picks a maximum width and stores only the first column.
        TableColumnModel rowHeaderModel = new DefaultTableColumnModel() {
            boolean first = true;

            public void addColumn(TableColumn tc) {
                if (first) {
                    tc.setMaxWidth(tc.getPreferredWidth());
                    super.addColumn(tc);
                    first = false;
                }
                // Drop the rest of the columns; this is the header column only.
            }
        };
        JTable jt = new JTable(tm, cm);
        // Set up the header column and hook it up to everything.
        JTable headerColumn = new JTable(tm, rowHeaderModel);
        jt.createDefaultColumnsFromModel();
        headerColumn.createDefaultColumnsFromModel();
        // Make sure that selections between the main table and the header stay in sync     // (by sharing the same model).
        jt.setSelectionModel(headerColumn.getSelectionModel());
        // Make the header column look pretty.     //
        headerColumn.setBorder(BorderFactory.createEtchedBorder());
        headerColumn.setBackground(Color.lightGray);
        headerColumn.setColumnSelectionAllowed(false);
        headerColumn.setCellSelectionEnabled(false);
        // Put it in a viewport that we can control.
        JViewport jv = new JViewport();
        jv.setView(headerColumn);
        jv.setPreferredSize(headerColumn.getMaximumSize());
        // Without shutting off autoResizeMode, our tables won't scroll correctly     // (horizontally, anyway).
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // We have to manually attach the row headers, but after that, the scroll
        // pane keeps them in sync.
        JScrollPane jsp = new JScrollPane(jt);
        jsp.setRowHeader(jv);
        jsp.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());
        getContentPane().add(jsp, BorderLayout.CENTER);
    }
    private static String [] createColumnTitles(int colCount) {
        String [] columns = new String [colCount];
        for(int i=1;i<colCount;i++) {
            columns[i]="Column "+i;
        }
        columns[0] = "Row #";
        return columns;
    }
    private static String [] createDataPrefix(int colCount) {
        String [] dataPrefix = new String [colCount];
        for(int i=1;i<colCount;i++) {
            dataPrefix[i]="col"+i;
        }
        dataPrefix[0] = "";
        return dataPrefix;
    }
    public static void main(String args[]) {
        RowHeaderTableTest rht = new RowHeaderTableTest();
        rht.setVisible(true);
    }
}