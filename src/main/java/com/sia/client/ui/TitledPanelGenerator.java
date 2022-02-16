package com.sia.client.ui;

import com.sia.client.config.Utils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class TitledPanelGenerator {

    private final String title;
    private final int width;
    private final int height;
    private JComponent bodyComponent;
    private JPanel panel;
    public TitledPanelGenerator(String title,int width,int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }
    public void setBodyComponent(JComponent bodyComponent) {
        this.bodyComponent = bodyComponent;
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setLayout(new BorderLayout());
        if ( null != title && ! title.trim().equals("")) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(3,1,3,1));
            titleLabel.setForeground(Color.WHITE);
            JPanel titlePanel = Utils.createCompCenteredPanel(titleLabel);
            titlePanel.setBackground(Color.DARK_GRAY);
            panel.add(titlePanel,BorderLayout.NORTH);
        }
        panel.add(bodyComponent,BorderLayout.CENTER);
//        panel.setBorder(BorderFactory.createMatteBorder(0,1,0,0,Color.GRAY));
    }
    public JPanel getPanel() {
        return panel;
    }
    public JComponent getBodyComponent() {
        return bodyComponent;
    }
}
