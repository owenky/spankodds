package com.sia.client.ui.example;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;

public class RowHeaderTableTest extends JFrame {
    public static final int colCount = 47;
    public static final int rowCount = 1000;
    public static final int headerRowHeight = 20;

    public RowHeaderTableTest() {
        super("Row Header Test");
        setSize(1500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        TableModel tm = new TestMainTableModel();
        // Create a column model for the main table. This model ignores the first
        // column added and sets a minimum width of 150 pixels for all others.
        TableColumnModel cm = new TestMainTableColumnModel();
        // Create a column model that will serve as our row header table. This model
        // picks a maximum width and stores only the first column.
        TableColumnModel rowHeaderModel = new TestRowHeaderColumnModel();
        JTable jt = new TestMainTable(tm, cm);
        // Set up the header column and hook it up to everything.
        JTable headerColumn = new TestRHTable(tm, rowHeaderModel);
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
        configureRowHeight(jt);
        configureRowHeight(headerColumn);


        JScrollPane jsp = new JScrollPane(jt);
        jsp.setRowHeader(jv);
        jsp.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, headerColumn.getTableHeader());

        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.add(jsp,BorderLayout.CENTER);
        mainContainer.add(jsp.getHorizontalScrollBar(),BorderLayout.SOUTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Pane 1",mainContainer);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    private static void configureRowHeight(JTable table) {
        for (int i = 0; i < rowCount; i++) {
            if (i % 5 == 0) {
                table.setRowHeight(i, headerRowHeight);
            } else {
                table.setRowHeight(i, 60);
            }
        }
    }

    public static void main(String args[]) {
        RowHeaderTableTest rht = new RowHeaderTableTest();
        rht.setVisible(true);
    }
}