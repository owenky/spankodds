package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
import com.sia.client.model.KeyedObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class ColumnHeaderDrawer<V extends KeyedObject> {

    private final Map<String, JComponent> columnHeaderComponentMap = new HashMap<>();
    private final ColumnCustomizableTable<V> mainTable;

    public ColumnHeaderDrawer(ColumnCustomizableTable<V> mainTable) {
        this.mainTable = mainTable;
    }
    public void drawOnViewIndex(int rowViewIndex, Object headerValue,int horizontalScrollBarAdjustmentValue) {
        ColumnHeaderProvider<V> columnHeaderProvider = mainTable.getModel().getColumnHeaderProvider();
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(String.valueOf(headerValue), header -> makeColumnHeaderComp(mainTable, header,columnHeaderProvider.getHeaderForeground()
                , columnHeaderProvider.getHeaderFont()));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, columnHeaderProvider.getColumnHeaderHeight(), horizontalScrollBarAdjustmentValue);
    }
    private static <V extends KeyedObject> JComponent makeColumnHeaderComp(ColumnCustomizableTable<V> jtable, String gameGroupHeader, Color headerForeGround, Font titleFont) {
        JPanel jPanel = new JPanel();
        jPanel.setOpaque(false);
        BorderLayout bl = new BorderLayout();
        jPanel.setLayout(bl);
        JLabel titleLabel = new JLabel(gameGroupHeader);
        titleLabel.setFont(titleFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setForeground(headerForeGround);
        jPanel.add(BorderLayout.CENTER, titleLabel);
        jtable.add(jPanel);

        return jPanel;
    }

    private static <V extends KeyedObject> void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable<V> mainTable, JComponent header, int headerHeight, int diffByScroll) {
        Rectangle r1 = mainTable.getCellRect(rowViewIndex-1, 0, true);
        int x1 = 5; //keep leading space from last locked column
        int y1 = (int) (r1.getY() + r1.getHeight());
        int width = (int)header.getPreferredSize().getWidth();
        header.setBounds(x1+diffByScroll, y1, width, headerHeight);

    }
}
