package com.sia.client.ui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;

public abstract class TableUtils {

    public static JComponent configTableLockColumns(ColumnLockableTable mainTable,int lockedColumnLastIndex) {

        mainTable.removeLockedColumnIndex(lockedColumnLastIndex);
        mainTable.createUnlockedColumns();
//        mainTable.setPreferredScrollableViewportSize(mainTable.getPreferredSize());

        mainTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF ); //necessary for horizontal scroll bar showing up.
        RowHeaderTable rowHeaderTable = mainTable.getRowHeaderTable();
        rowHeaderTable.createDefaultColumnModel();

        JScrollPane tableScrollPane = new JScrollPane(mainTable);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setRowHeaderView(rowHeaderTable);
        tableScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowHeaderTable.getTableHeader());
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(tableScrollPane,BorderLayout.CENTER);
        container.add(tableScrollPane.getHorizontalScrollBar(),BorderLayout.SOUTH);
        container.setBorder(BorderFactory.createLineBorder(Color.RED,3));
        mainTable.setCoordinateContainer(container);
        return container;
    }
    public static TableColumn cloneTableColumn(TableColumn sourceTc,int columnIndex) {

        TableColumn rtn = new TableColumn(columnIndex);
        rtn.setCellRenderer(sourceTc.getCellRenderer());
        rtn.setCellEditor(sourceTc.getCellEditor());
        rtn.setHeaderRenderer(sourceTc.getHeaderRenderer());
        rtn.setHeaderValue(sourceTc.getHeaderValue());
        rtn.setIdentifier(sourceTc.getIdentifier());
        rtn.setMaxWidth(sourceTc.getMaxWidth());
        rtn.setMinWidth(sourceTc.getMinWidth());
        rtn.setWidth(sourceTc.getWidth());
        rtn.setPreferredWidth(sourceTc.getPreferredWidth());
        rtn.setResizable(sourceTc.getResizable());
        return rtn;
    }
}
