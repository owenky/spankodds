package com.sia.client.ui;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
/**
 * @author DaveTheDane, based on a suggestion from Adam Gawne-Cain
 */
public final class HintTextField extends JTextField  {

    private final String hint;
    public HintTextField(String hint) {
       this(hint,true);
    }
    public HintTextField(String hint,boolean editable) {
        this.hint = hint;
        this.setEditable(editable);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getText().length() == 0) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.getContentPane().add(new HintTextField("this is a test"));
            f.pack();
            f.setVisible(true);
        });
    }
}