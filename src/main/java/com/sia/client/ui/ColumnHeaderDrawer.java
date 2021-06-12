package com.sia.client.ui;

import com.sia.client.model.ColumnHeaderProvider;
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
import java.awt.Font;
import java.awt.FontMetrics;
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
        Font headerFont = columnHeaderProvider.getHeaderFont();
        JComponent headerComponent = columnHeaderComponentMap.computeIfAbsent(String.valueOf(headerValue), header -> makeColumnHeaderComp(mainTable, header,columnHeaderProvider.getHeaderForeground()
                ,headerFont));
        layOutColumnHeader(rowViewIndex, mainTable, headerComponent, String.valueOf(headerValue),columnHeaderProvider.getColumnHeaderHeight(), horizontalScrollBarAdjustmentValue,headerFont);
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

    private static <V extends KeyedObject> void layOutColumnHeader(int rowViewIndex, ColumnCustomizableTable<V> mainTable, JComponent header, String headerStr,int headerHeight, int diffByScroll, Font titleFont) {
        if ( 0 > diffByScroll ) {
            diffByScroll = 0;
        }
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainTable);
        Rectangle r1 = mainTable.getCellRect(rowViewIndex-1, 0, true);
        int stringWidth = getStringWidth(header,titleFont,headerStr);
//        int x1 = 5; //keep leading space from last locked column
        int x1 = (int)topFrame.getSize().getWidth()/2 - mainTable.getRowHeaderTable().getWidth();
        x1 = x1 - stringWidth/2;
        int x2;
        if ( x1 < 0) {
            x2 = (mainTable.getRowHeaderTable().getWidth() - stringWidth)/2;
            mainTable.remove(header);
            mainTable.getRowHeaderTable().add(header);
        } else {
            x2 = x1;
            mainTable.getRowHeaderTable().remove(header);
            mainTable.add(header);
        }
        int y1 = (int) (r1.getY() + r1.getHeight());
        int width = (int)header.getPreferredSize().getWidth();
        header.setBounds(x2+diffByScroll, y1, width, headerHeight);
    }
    private static int getStringWidth(JComponent jComponent,Font font, String text) {
        FontMetrics fm = jComponent.getFontMetrics(font);
        return SwingUtilities2.stringWidth(jComponent, fm, text);
    }
}
