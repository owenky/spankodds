package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.TableCellRendererProvider;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private static final Color userRenderBorderColr = Color.GRAY;
    private static final int userRenderBorderThick = 1;
    private static final Border userRenderBorderNormal = new MatteBorder(0, 0, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderFirstCol = new MatteBorder(0, userRenderBorderThick, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderLastCol = new MatteBorder(0, 0, userRenderBorderThick, 1, userRenderBorderColr);
    private final TableCellRendererProvider tableCellRendererProvider;
    private static final JLabel headerCellRender = new JLabel();
    private static final JPanel render = new JPanel();
    private static final JLabel noteRender = new JLabel();

    public ColumnHeaderCellRenderer(TableCellRendererProvider tableCellRendererProvider, ColumnHeaderProperty columnHeaderProperty ){
        this.tableCellRendererProvider = tableCellRendererProvider;
        headerCellRender.setOpaque(true);
        headerCellRender.setBackground(columnHeaderProperty.getHeaderBackground());
        headerCellRender.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {

        if ( null != ColumnCustomizableDataModel.retrieveGameGroupHeader(value) ) {
            return headerCellRender;
        }   else if ( TableUtils.isNoteColumn(table,column) ) {
            noteRender.setText(null==value?"":String.valueOf(value));
            return noteRender;
        }
        Component userComponent = tableCellRendererProvider.apply(row, column).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JPanel cellRender = createJPanelWithPadding((JComponent) userComponent, table.getRowCount(), table.getColumnCount(), row, column);
        //old
        //TableUtils.highLightCellWhenRowSelected(table,cellRender,row, SiaConst.Ui.ROW_SELECTED_COLOR);
        //new
        TableUtils.highLightCellWhenRowSelected(table,cellRender,row, AppController.getUserDisplaySettings().getRowhighlightcolor());

        return cellRender;
    }
    private JPanel createJPanelWithPadding(JComponent userComponent, int rowCount, int colCount, int row, int col) {

        render.removeAll();

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
