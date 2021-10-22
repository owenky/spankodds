package com.sia.client.ui;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.MarginProvider;
import com.sia.client.model.TableCellRendererProvider;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private static final Color userRenderBorderColr = Color.GRAY;
    private static final int userRenderBorderThick = 1;
    private static final Border userRenderBorderNormal = new MatteBorder(0, 0, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderFirstCol = new MatteBorder(0, userRenderBorderThick, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderLastCol = new MatteBorder(0, 0, userRenderBorderThick, 1, userRenderBorderColr);
    private final ColumnHeaderProperty columnHeaderProperty;
    private final TableCellRendererProvider tableCellRendererProvider;
    private final MarginProvider marginProvider;
    private final JLabel headerCellRender = new JLabel();

    public ColumnHeaderCellRenderer(TableCellRendererProvider tableCellRendererProvider, ColumnHeaderProperty columnHeaderProperty, MarginProvider marginProvider) {
        this.tableCellRendererProvider = tableCellRendererProvider;
        this.columnHeaderProperty = columnHeaderProperty;
        this.marginProvider = marginProvider;
        this.headerCellRender.setOpaque(true);
        this.headerCellRender.setBackground(columnHeaderProperty.getHeaderBackground());
        this.headerCellRender.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {

        if ( null != ColumnCustomizableDataModel.retrieveGameGroupHeader(value) ) {
            return headerCellRender;
        }
        Component userComponent = tableCellRendererProvider.apply(row, column).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return createJPanelWithPadding((JComponent) userComponent, table.getRowCount(), table.getColumnCount(), row, column);
    }
    private JPanel createJPanelWithPadding(JComponent userComponent, int rowCount, int colCount, int row, int col) {

        JPanel render = new JPanel();

        BorderLayout bl = new BorderLayout();
        render.setLayout(bl);

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
}
