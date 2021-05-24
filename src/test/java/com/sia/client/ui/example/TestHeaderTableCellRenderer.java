package com.sia.client.ui.example;

import com.sia.client.config.SiaConst;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

public class TestHeaderTableCellRenderer extends DefaultTableCellRenderer {

    private final JPanel headerRender;
    private final JPanel dataRender;

    public TestHeaderTableCellRenderer() {
        headerRender = new JPanel();
        headerRender.setBackground(SiaConst.DefaultHeaderColor);
        dataRender = new JPanel();
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if ( table.getRowHeight(row)==RowHeaderTableTest.headerRowHeight){
            return headerRender;
        }
        return dataRender;
    }
}
