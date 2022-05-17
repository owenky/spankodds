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

    private static final Color defaultTitleBarBgColor = Color.DARK_GRAY;
    private static final Color defaultTitleBarFgColr = Color.WHITE;
    private final String title;
    private final int width;
    private final int height;
    private JComponent bodyComponent;
    private JPanel panel;
    private JComponent topLeftControl;
    private JComponent topRightControl;
    private Color titleBarBgColor = defaultTitleBarBgColor;
    private Color titleBarFgColor = defaultTitleBarFgColr;

    public TitledPanelGenerator(String title,int width,int height,JComponent topLeftControl,JComponent topRightControl) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.panel = null;
        this.topLeftControl = topLeftControl;
        this.topRightControl = topRightControl;
    }
    public void setBodyComponent(JComponent bodyComponent) {
        this.bodyComponent = bodyComponent;
    }
    public void setTopLeftControl(JComponent topLeftControl) {
        this.topLeftControl = topLeftControl;
    }
    public void setTitleBarBgColor(Color titleBarBgColor) {
        this.titleBarBgColor = titleBarBgColor;
    }
    public void setTitleBarFgColor(Color titleBarFgColor) {
        this.titleBarFgColor = titleBarFgColor;
    }
    public void build() {
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setLayout(new BorderLayout());
        if ( null != title && ! title.trim().equals("")) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(3,1,3,1));
            titleLabel.setForeground(titleBarFgColor);
            JPanel titlePanel;
            if ( null != topLeftControl || null != topRightControl) {

                if ( null == topLeftControl) {
                    topLeftControl = makeDummyComponent(topRightControl.getName(), titleBarBgColor);
                } else {
                    topLeftControl.setOpaque(false);
                    topLeftControl.setForeground(titleBarFgColor);
                }

                if ( null == topRightControl) {
                    topRightControl = makeDummyComponent(topLeftControl.getName(), titleBarBgColor);
                } else {
                    topRightControl.setOpaque(false);
                    topRightControl.setForeground(titleBarFgColor);
                }
                titlePanel = Utils.createCompCenteredPanel(topLeftControl,titleLabel,topRightControl);
            } else {
                titlePanel = Utils.createCompCenteredPanel(titleLabel);
            }

            titlePanel.setBackground(titleBarBgColor);
            panel.add(titlePanel,BorderLayout.NORTH);
        }
        panel.add(bodyComponent,BorderLayout.CENTER);
//        panel.setBorder(BorderFactory.createMatteBorder(0,1,0,0,Color.GRAY));
    }
    //dummy component is used for padding at blank side so that title can be place at center
    private static JComponent makeDummyComponent(String dummyString, Color bgColor) {
        JLabel dummyComp  = new JLabel(dummyString);
        dummyComp.setForeground(bgColor);
        return dummyComp;
    }
    public JPanel getPanel() {
        if ( null == panel) {
            build();
        }
        return panel;
    }
    public JComponent getBodyComponent() {
        return bodyComponent;
    }
}
