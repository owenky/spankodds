package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.ColumnHeaderProvider.ColumnHeaderProperty;
import com.sia.client.model.MarginProvider;
import com.sia.client.model.TableCellRendererProvider;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private static final Color userRenderBorderColr = Color.GRAY;
    private static final int userRenderBorderThick = 1;
    private static final Border userRenderBorderNormal = new MatteBorder(0, 0, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderFirstCol = new MatteBorder(0, userRenderBorderThick, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderLastCol = new MatteBorder(0, 0, userRenderBorderThick, 1, userRenderBorderColr);
    private final ColumnHeaderProvider columnHeaderProvider;
    private final TableCellRendererProvider tableCellRendererProvider;
    private final MarginProvider marginProvider;

    public ColumnHeaderCellRenderer(TableCellRendererProvider tableCellRendererProvider, ColumnHeaderProvider columnHeaderProvider, MarginProvider marginProvider) {
        this.tableCellRendererProvider = tableCellRendererProvider;
        this.columnHeaderProvider = columnHeaderProvider;
        this.marginProvider = marginProvider;
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        int rowModelIndex = table.convertRowIndexToModel(row);
        ColumnHeaderProperty headerProp = columnHeaderProvider.get();
        if (headerProp.columnHeaderIndexSet.contains(rowModelIndex)) {
            JPanel render = new JPanel();
            render.setBackground(headerProp.haderBackground);
            render.setBorder(BorderFactory.createEmptyBorder());
            return render;
        }
        Component userComponent = tableCellRendererProvider.apply(row, column).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return createJPanelWithPadding((JComponent) userComponent, table.getRowCount(), table.getColumnCount(), row, column);
    }
    private JPanel createJPanelWithPadding(JComponent userComponent, int rowCount, int colCount, int row, int col) {

        JPanel render = new JPanel();

        BorderLayout bl = new BorderLayout();
        render.setLayout(bl);

//        JComponent topPadding = createVPaddingComponent(userComponent.getBackground());
//        JComponent bottomPadding = createVPaddingComponent(userComponent.getBackground());
//        JComponent leftPadding = createHPaddingComponent(userComponent.getBackground());
//        JComponent rightPadding = createHPaddingComponent(userComponent.getBackground());
//
//        render.add(topPadding,BorderLayout.NORTH);
//        render.add(bottomPadding,BorderLayout.SOUTH);
//        render.add(leftPadding,BorderLayout.WEST);
//        render.add(rightPadding,BorderLayout.EAST);

        render.add(userComponent,BorderLayout.CENTER);

        Border renderBorder;
        if (0 == col) {
            renderBorder = userRenderBorderFirstCol;
        } else if ((colCount - 1) == col) {
            renderBorder = userRenderBorderLastCol;
        } else {
            renderBorder = userRenderBorderNormal;
        }
        userComponent.setBorder(BorderFactory.createEmptyBorder());
        render.setBorder(renderBorder);
        return render;
    }
    private JComponent createHPaddingComponent(Color bck) {
        JComponent padding = new JPanel();
        Dimension size = new Dimension( (int)marginProvider.get().getWidth(),-1);
        padding.setPreferredSize(size);
        padding.setMaximumSize(size);
        padding.setMinimumSize(size);
        padding.setBackground(bck);
        padding.setBorder(BorderFactory.createEmptyBorder());
        return padding;
    }
    private JComponent createVPaddingComponent(Color bck) {
        JComponent padding = new JPanel();
        Dimension size = new Dimension(-1,(int)marginProvider.get().getHeight());
        padding.setPreferredSize(size);
        padding.setMaximumSize(size);
        padding.setMinimumSize(size);
        padding.setBackground(bck);
        padding.setBorder(BorderFactory.createEmptyBorder());
        return padding;
    }
}
