package com.sia.client.ui;

import com.sia.client.model.AlertStruct;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class UrgentMesgHistBox extends JComboBox<AlertStruct> {

    public UrgentMesgHistBox() {
        setRenderer(new MesgBoxRender());
        addItem(new AlertStruct("", "<html><B><center>RECENT ALERTS LIST</center></B><html>"));
    }
//////////////////////////////////////////////////////////////////////////////////////////
    private static class MesgBoxRender implements ListCellRenderer<AlertStruct> {

        private final JPanel renderer;
        private final JEditorPane mesgArea;
        private final JLabel timeLabel;
        private final Border paddingBorder = new EmptyBorder(2,3,2,3);
        private final Dimension timelabelDim = new Dimension(40,20);
        public MesgBoxRender() {
            renderer = new JPanel();
            mesgArea = new JEditorPane();
            HTMLEditorKit kit = new HTMLEditorKit();
            mesgArea.setEditorKit(kit);
            mesgArea.setEditable(false);
            timeLabel = new JLabel();
            renderer.setLayout(new BorderLayout());
            renderer.add(mesgArea,BorderLayout.CENTER);
            renderer.add(timeLabel,BorderLayout.EAST);
            timeLabel.setBorder(paddingBorder );
            timeLabel.setPreferredSize(timelabelDim);
        }
        @Override
        public Component getListCellRendererComponent(final JList<? extends AlertStruct> list, final AlertStruct value, final int index, final boolean isSelected, final boolean cellHasFocus) {
            mesgArea.setText(value.getMesg());
            mesgArea.setBorder(BorderFactory.createLineBorder(Color.RED));
            timeLabel.setText(value.getTime());
            return renderer;
        }
    }
}
