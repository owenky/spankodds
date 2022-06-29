package com.sia.client.ui;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.TableCellRendererProvider;
import com.sia.client.model.UserDisplaySettings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColumnHeaderCellRenderer implements TableCellRenderer {

    private static final Color userRenderBorderColr = Color.GRAY;
    private static final int userRenderBorderThick = 1;
    private static final Border userRenderBorderNormal = new MatteBorder(0, 0, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderFirstCol = new MatteBorder(0, userRenderBorderThick, userRenderBorderThick, userRenderBorderThick, userRenderBorderColr);
    private static final Border userRenderBorderLastCol = new MatteBorder(0, 0, userRenderBorderThick, 1, userRenderBorderColr);
    private final TableCellRendererProvider tableCellRendererProvider;
    private static final JLabel headerCellRender = new JLabel();
    private static final JPanel render = new JPanel();
    private static final NoteCellRenderer noteRender = new NoteCellRenderer();
    private static final Color [] rowAltColors;

    static {
        UserDisplaySettings uds = AppController.getUserDisplaySettings();
        rowAltColors = new Color[] {Color.WHITE,uds.getAltcolor()};
    }

    public ColumnHeaderCellRenderer(TableCellRendererProvider tableCellRendererProvider, ColumnHeaderProperty columnHeaderProperty ){
        this.tableCellRendererProvider = tableCellRendererProvider;
        headerCellRender.setOpaque(true);
        headerCellRender.setBackground(columnHeaderProperty.getHeaderBackground());
        headerCellRender.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {

        JComponent cellRenderComp;
        if ( null != ColumnCustomizableDataModel.retrieveGameGroupHeader(value) ) {
            return headerCellRender;
        }   else if ( TableUtils.isNoteColumn(table,column) ) {
            cellRenderComp = noteRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        } else {
            Component userComponent = tableCellRendererProvider.apply(row, column).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cellRenderComp = createJPanelWithPadding((JComponent) userComponent, table.getRowCount(), table.getColumnCount(), row, column);
        }
        TableUtils.highLightCellWhenRowSelected(table,cellRenderComp,row, AppController.getUserDisplaySettings().getRowhighlightcolor());

        return cellRenderComp;
    }
    private static JPanel createJPanelWithPadding(JComponent userComponent, int rowCount, int colCount, int row, int col) {

        render.removeAll();
        BorderLayout bl = new BorderLayout();
        render.setLayout(bl);
        render.add(userComponent,BorderLayout.CENTER);
        userComponent.setBorder(BorderFactory.createEmptyBorder());
        setBorder(render,colCount,col);
        return render;
    }
    private static void setBorder(JComponent jcomponent, int colCount, int col) {

        Border renderBorder;
        if (0 == col) {
            renderBorder = userRenderBorderFirstCol;
        } else if ((colCount - 1) == col) {
            renderBorder = userRenderBorderLastCol;
        } else {
            renderBorder = userRenderBorderNormal;
        }
        jcomponent.setBorder(renderBorder);
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    private static class NoteCellRenderer implements TableCellRenderer {
        private final JLabel renderComp;
        public NoteCellRenderer() {
            renderComp = new JLabel();
            renderComp.setOpaque(true);
            renderComp.setBorder(BorderFactory.createLineBorder(Color.gray));
        }

        @Override
        public JComponent getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            renderComp.setText(null==value?"":String.valueOf(value));
            int colorIndex = (row-1)%rowAltColors.length;
            if ( colorIndex < 0) colorIndex = 0;
            renderComp.setBackground(rowAltColors[colorIndex]);
            setBorder(renderComp,table.getColumnCount(),column);
            if ( ! isSelected ) {
                renderComp.setForeground(Color.BLACK);
            }
            return renderComp;
        }
    }
}
