package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.TableCellRendererProvider;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private final TableCellRendererProvider tableCellRendererProvider;
    private final ColumnHeaderProvider columnHeaderProvider;

    public ColumnHeaderCellRenderer(TableCellRendererProvider tableCellRendererProvider, ColumnHeaderProvider columnHeaderProvider) {
        this.tableCellRendererProvider = tableCellRendererProvider;
        this.columnHeaderProvider = columnHeaderProvider;
    }
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        int rowModelIndex = table.convertRowIndexToModel(row);
        if ( columnHeaderProvider.get().columnHeaderIndexSet.contains(rowModelIndex)) {
            JPanel render = new JPanel();
            render.setBackground(columnHeaderProvider.get().haderBackground);
            return render;
        }
        return tableCellRendererProvider.apply(row,column).getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
    }
}
