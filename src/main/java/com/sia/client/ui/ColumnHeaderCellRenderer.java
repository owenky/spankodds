package com.sia.client.ui;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.function.BiFunction;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private final BiFunction<Integer,Integer,TableCellRenderer> userTableCellRendererSppr;
    private final Color columnHeaderBackground;
    private final Object columnHeaderCellIden;

    public ColumnHeaderCellRenderer(BiFunction<Integer,Integer,TableCellRenderer> userTableCellRendererSppr, Color columnHeaderBackground,Object columnHeaderCellIden) {
        this.userTableCellRendererSppr = userTableCellRendererSppr;
        this.columnHeaderBackground = columnHeaderBackground;
        this.columnHeaderCellIden = columnHeaderCellIden;
    }
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if ( columnHeaderCellIden.equals(value)) {
            JPanel render = new JPanel();
            render.setBackground(columnHeaderBackground);
            return render;
        }
        return userTableCellRendererSppr.apply(row,column).getTableCellRendererComponent(table,  value, isSelected,hasFocus, row, column);
    }
}
