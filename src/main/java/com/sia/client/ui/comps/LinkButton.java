package com.sia.client.ui.comps;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinkButton extends JButton {

    private static final Color enableUnderlineColor = Color.BLUE.brighter();
    private static final Color disableUnderlineColor = Color.gray.brighter();
    public LinkButton(String text) {
        this();
        setText(text);
    }
    public LinkButton() {
        setContentAreaFilled(false);
        this.setVerticalAlignment(SwingConstants.BOTTOM);
        this.setOpaque(false);
        Border outterBorder = BorderFactory.createMatteBorder(0,0,1,0,enableUnderlineColor);
        Border spacingBorder = BorderFactory.createEmptyBorder(0,4,0,4);
        setBorder(BorderFactory.createCompoundBorder(outterBorder,spacingBorder));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    @Override
    public void setEnabled(boolean b) {
        model.setEnabled(b);
        Color underLineColor = b?enableUnderlineColor:disableUnderlineColor;
        setBorder(BorderFactory.createMatteBorder(0,0,1,0,underLineColor));
    }
    public static void main(String [] argv) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LinkButton b = new LinkButton("disabled");
        b.setEnabled(false);
        jFrame.getContentPane().add(new LinkButton("HyperLink"), BorderLayout.NORTH);
        jFrame.getContentPane().add(b, BorderLayout.SOUTH);

        jFrame.setSize(new Dimension(1500, 800));
        jFrame.setLocation(new Point(250, 100));
        jFrame.setVisible(true);
    }
}
