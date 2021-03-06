package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProperty;
import com.sia.client.model.KeyedObject;
import sun.swing.SwingUtilities2;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class ColumnHeaderDrawer<V extends KeyedObject> {

    private final Map<Integer,JComponent> groupHeaderCompRowIndexCache = new HashMap<>();
    private final Map<String,JComponent> groupHeaderCompNameCache = new HashMap<>();
    public ColumnHeaderDrawer() {
    }
    public void reset() {
        groupHeaderCompNameCache.clear();
    }
    public Component drawOnViewIndex(ColumnCustomizableTable<V> mainTable,int rowViewIndex, String headerValue, int horizontalScrollBarAdjustmentValue) {
        ColumnHeaderProperty columnHeaderProvider = mainTable.getModel().getColumnHeaderProperty();
        Font headerFont = columnHeaderProvider.getHeaderFont();
        JComponent headerComponent = groupHeaderCompNameCache.computeIfAbsent(headerValue,(key)->makeColumnHeaderComp(mainTable, key,columnHeaderProvider.getHeaderForeground(),headerFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, String.valueOf(headerValue),columnHeaderProvider.getColumnHeaderHeight(), horizontalScrollBarAdjustmentValue,headerFont);
        groupHeaderCompRowIndexCache.put(rowViewIndex,headerComponent);
        return headerComponent;
    }
    public JComponent getGameGroupHeaderComponent(int rowViewIndex) {
        return groupHeaderCompRowIndexCache.get(rowViewIndex);
    }
    private static <V extends KeyedObject> JComponent makeColumnHeaderComp(ColumnCustomizableTable<V> jtable, String gameGroupHeader, Color headerForeGround, Font titleFont) {
        JPanel jPanel = new JPanel();
        jPanel.setName(gameGroupHeader);

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

    private static <V extends KeyedObject> void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable<V> mainTable, JComponent header, String headerStr,int headerHeight, int diffByScroll, Font titleFont) {
        if ( 0 > diffByScroll ) {
            diffByScroll = 0;
        }
        Rectangle r1 = mainTable.getCellRect(rowViewIndex-1, 0, true);
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainTable);
        int stringWidth = getStringWidth(header,titleFont,headerStr);
//        int x1 = 5; //keep leading space from last locked column
        int mainTableWidth = 0 == mainTable.getWidth()? (int)mainTable.getPreferredSize().getWidth():mainTable.getWidth();
        int rowHeaderWidth = 0==mainTable.getRowHeaderTable().getWidth()? (int)mainTable.getRowHeaderTable().getPreferredSize().getWidth():mainTable.getRowHeaderTable().getWidth();
        int totalWidth = Math.min((int)topFrame.getSize().getWidth(),mainTableWidth+rowHeaderWidth);
        int x1 = totalWidth/2 - rowHeaderWidth;
        x1 = x1 - stringWidth/2;
        int x2;
        boolean isInMainTable;
        if ( x1 < 0) {
            x2 = (totalWidth - rowHeaderWidth - stringWidth)/2;
            if ( 0 > x2 ) {
                x2 = (rowHeaderWidth - stringWidth) / 2;
                isInMainTable = false;
            } else {
                x2=5;
                isInMainTable = true;
            }

        } else {
            x2 = x1;
            isInMainTable = true;
        }
        mainTable.getRowHeaderTable().remove(header);
        mainTable.remove(header);
        int xDelta;
        if ( isInMainTable ) {
            mainTable.add(header);
            xDelta = diffByScroll;
        } else {
            mainTable.getRowHeaderTable().add(header);
            xDelta = 0;
        }

        int y1 = (int) (r1.getY() + r1.getHeight());
        int width = (int)header.getPreferredSize().getWidth();
        header.setBounds(x2+xDelta, y1, width, headerHeight);
    }
    private static int getStringWidth(JComponent jComponent,Font font, String text) {
        FontMetrics fm = jComponent.getFontMetrics(font);
        return SwingUtilities2.stringWidth(jComponent, fm, text);
    }
}
