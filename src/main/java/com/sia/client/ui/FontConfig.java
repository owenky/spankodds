package com.sia.client.ui;

import com.jidesoft.swing.CheckBoxList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontConfig implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public JMenu createFontMenu() {
        JMenu fontFamilyMenu = new JMenu("Font Family");
        fontFamilyMenu.add(getFontFamilies());

        JMenu fontSizeMenu = new JMenu("Font Sizes");
        fontSizeMenu.add(getFontSizes());

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(fontFamilyMenu);
        fontMenu.add(fontSizeMenu);
        return fontMenu;
    }
    private JComponent getFontFamilies() {
        String fontFamilies[]= { "Times","Serif"};
        JScrollPane jScrollPane = new JScrollPane(new JList(fontFamilies));
        jScrollPane.setPreferredSize(new Dimension(100,100));
        return jScrollPane;
    }
    private JComponent getFontSizes() {
//        JComboBox<String> fontSizes = new JComboBox<>();
//        fontSizes.addItem("12");
//        fontSizes.addItem("30");
//        return fontSizes;
        String week[]= { "Monday","Tuesday","Wednesday",
                "Thursday","Friday","Saturday","Sunday"};

        //create list
        JScrollPane jScrollPane = new JScrollPane(new JList(week));
        jScrollPane.setPreferredSize(new Dimension(100,100));
        return jScrollPane;
    }
}
