package com.sia.client.ui.example;

import com.sia.client.config.SiaConst;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class TestMainTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Component rtn = super.getTableCellRendererComponent(table,value, isSelected, hasFocus,row, column);
        if (table.getRowHeight(row) == RowHeaderTableTest.headerRowHeight) {
            rtn.setBackground(SiaConst.DefaultHeaderColor);
        } else {
            rtn.setBackground(Color.WHITE);
        }
        return rtn;
    }
}
