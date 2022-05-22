package com.sia.client.ui.comps;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class LightComboBox<E> extends JComboBox<E> {

    private Color borderColor = Color.gray.darker();
    public LightComboBox(E[] arr) {
       super(arr);
       init();
    }
    public LightComboBox() {
        init();
    }
    @Override
    public void setFont(Font font) {
        super.setFont(font);
    }
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    private void init() {
        setUI((ComboBoxUI) MyComboBoxUI.createUI(this));
        setBorder(BorderFactory.createLineBorder(borderColor));
    }
    static class MyComboBoxUI extends BasicComboBoxUI {
        public static ComponentUI createUI(JComponent c) {
            return new MyComboBoxUI();
        }
        @Override
        protected JButton createArrowButton() {
            JButton button = new BasicArrowButton(BasicArrowButton.EAST);
            return button;
        }
    }
    public static void main(String [] argv) {

        LightComboBox<String> cb = new LightComboBox();
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        panel.add(cb);
        cb.addItem("1");
        cb.addItem("2");
        cb.setPreferredSize(new Dimension(100,20));
//        cb.setSize(new Dimension(60,60));
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel,BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(300,300));
        frame.pack();
        frame.setVisible(true);
    }
}
