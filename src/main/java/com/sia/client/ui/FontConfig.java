package com.sia.client.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class FontConfig implements ActionListener {

    private final JList<String> fontNameList = new JList<>();
    private final JList<Integer> fontSizeList = new JList<>();
    private final JList<String> fontStyleList = new JList<>();
    private final JMenu fontFamilyMenu = new JMenu("Font Family");
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public JMenu createFontMenu() {

        JScrollPane fontFamilyPane = new JScrollPane(fontNameList);
        fontFamilyPane.setPreferredSize(new Dimension(140,400));
        fontFamilyMenu.add(fontFamilyPane);

        JMenu fontSizeMenu = new JMenu("Font Sizes");
        JScrollPane fontSizePane = new JScrollPane(fontSizeList);
        fontSizePane.setPreferredSize(new Dimension(15,200));
        fontSizeMenu.add(fontSizePane);

        JMenu fontStyleMenu = new JMenu("Font Style");
        JScrollPane fontStylePane = new JScrollPane(fontStyleList);
        fontStylePane.setPreferredSize(new Dimension(10,20));
        fontSizeMenu.add(fontStylePane);

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(fontFamilyMenu);
        fontMenu.add(fontSizeMenu);
        fontMenu.add(fontStyleMenu);

        fontNameList.addListSelectionListener(this::onFontFamilyName);
        loadFontFamilies();
        return fontMenu;
    }
    private void loadFontFamilies() {

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        Set<String> uniqFontFaimlies = new HashSet<>();
        final List<String> fontFamilyList = new ArrayList<>();
        for(Font f: fonts) {
            String fontFamily = getFontFamilyName(f);
            if ( ! uniqFontFaimlies.contains(fontFamily)) {
                uniqFontFaimlies.add(fontFamily);
                fontFamilyList.add(fontFamily);
            }
        }

        fontNameList.setModel( new AbstractListModel<String>() {
            public int getSize() {
                return fontFamilyList.size();
            }
            public String getElementAt(int i) {
                return fontFamilyList.get(i);
            }
        });

    }
    private void loadFontSizes(String fontFamilyName) {

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        final List<Integer> mySizeList = Arrays.stream(fonts).filter(f-> fontFamilyName.equals(getFontFamilyName(f)))
                        .map(Font::getSize).collect(Collectors.toList());

        fontSizeList.setModel( new AbstractListModel<Integer>() {
            public int getSize() {
                return mySizeList.size();
            }
            public Integer getElementAt(int i) {
                return mySizeList.get(i);
            }
        });
    }
    private void loadFontStyles(String fontFamilyName) {

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        final List<String> myStyleList = Arrays.stream(fonts).filter(f-> fontFamilyName.equals(getFontFamilyName(f)))
                .map(FontConfig::getFontStyle).filter(Objects::nonNull).collect(Collectors.toList());

        fontStyleList.setModel( new AbstractListModel<String>() {
            public int getSize() {
                return myStyleList.size();
            }
            public String getElementAt(int i) {
                return myStyleList.get(i);
            }
        });
    }
    private void onFontFamilyName(ListSelectionEvent event) {
        System.out.println("font===="+fontNameList.getSelectedValue());
        createFontSizeAndStyleList(fontNameList.getSelectedValue());
        fontFamilyMenu.setPopupMenuVisible(false);
    }
    private void createFontSizeAndStyleList(String fontFamilyName) {
        loadFontSizes(fontFamilyName);
        loadFontStyles(fontFamilyName);
    }
    private static String getFontFamilyName(Font f) {
        return f.getFontName().split("[- ]")[0];
    }
    private static String getFontStyle(Font f) {
        String [] parts = f.getFontName().split("[- ]");
        if ( 1 < parts.length) {
            return parts[1];
        } else {
            return null;
        }
    }
}
