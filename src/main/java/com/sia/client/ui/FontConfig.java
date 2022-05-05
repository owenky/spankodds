package com.sia.client.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class FontConfig implements ActionListener {

    @JsonIgnore
    private static final String fontPropSplitor="[- .]";
    @JsonIgnore
    private static final int fontNameMenuWidth=140;
    @JsonIgnore
    private static final int fontSizeMenuWidth=40;
    @JsonIgnore
    private static final int fontStyleMenuWidth=80;
    @JsonIgnore
    private static final int maxFontSize=30;
    @JsonIgnore
    private final JList<String> fontNameList = new JList<>();
    @JsonIgnore
    private final JList<Integer> fontSizeList = new JList<>();
    @JsonIgnore
    private final JList<String> fontStyleList = new JList<>();
    @JsonIgnore
    private final JMenu fontFamilyMenu = new JMenu("Font Family");
    @JsonIgnore
    private final JMenu fontSizeMenu = new JMenu("Font Sizes");
    @JsonIgnore
    private final JMenu fontStyleMenu = new JMenu("Font Style");
    @JsonIgnore
    private Font selectedFont;
    private String selectedFontName;
    private int selectedFontSize;
    private String selectedFontStyle;

    //getter and setter for JSON serialization/deserialization
    public String getSelectedFontName() {
        return selectedFontName;
    }

    public void setSelectedFontName(String selectedFontName) {
        this.selectedFontName = selectedFontName;
    }

    public int getSelectedFontSize() {
        return selectedFontSize;
    }

    public void setSelectedFontSize(int selectedFontSize) {
        this.selectedFontSize = selectedFontSize;
    }

    public String getSelectedFontStyle() {
        return selectedFontStyle;
    }

    public void setSelectedFontStyle(String selectedFontStyle) {
        this.selectedFontStyle = selectedFontStyle;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    private void configSelectedFontProp() {
        if ( null == selectedFont) {

            if (null != selectedFontName) {
                //font name/style/size are deserialized, construct font
                selectedFont = getSelectedFont();
            } else {
                selectedFont = new JLabel().getFont();
                selectedFontName = getFontFamilyName(selectedFont);
                selectedFontStyle = getFontStyle(selectedFont);
                selectedFontSize = selectedFont.getSize();
            }
        }
    }
    public JMenu createFontMenu() {


        configSelectedFontProp();
        JScrollPane fontFamilyPane = new JScrollPane(fontNameList);
        fontFamilyPane.setPreferredSize(new Dimension(fontNameMenuWidth,400));
        fontFamilyMenu.add(fontFamilyPane);

        JScrollPane fontSizePane = new JScrollPane(fontSizeList);
        fontSizePane.setPreferredSize(new Dimension(fontSizeMenuWidth,200));
        fontSizeMenu.add(fontSizePane);

        JScrollPane fontStylePane = new JScrollPane(fontStyleList);
        fontStylePane.setPreferredSize(new Dimension(fontStyleMenuWidth,100));
        fontStyleMenu.add(fontStylePane);

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(fontFamilyMenu);
        fontMenu.add(fontSizeMenu);
        fontMenu.add(fontStyleMenu);

        fontNameList.addListSelectionListener(this::onFontFamilyName);
        fontSizeList.addListSelectionListener(this::onFontSize);
        fontStyleList.addListSelectionListener(this::onFontStyle);
        loadFontNames();
        loadFontSizes(selectedFontName);
        loadFontStyles(selectedFontName);
        setFontMenuDisplay();
        return fontMenu;
    }
    private void setFontMenuDisplay() {
        fontFamilyMenu.setText(selectedFontName);
        fontSizeMenu.setText(String.valueOf(selectedFontSize));
        fontStyleMenu.setText(selectedFontStyle);
    }
    private void loadFontNames() {

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

        fontSizeList.setModel( new AbstractListModel<Integer>() {
            public int getSize() {
                return maxFontSize;
            }
            public Integer getElementAt(int i) {
                return i+1;
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
        selectedFontName = fontNameList.getSelectedValue();
        createFontSizeAndStyleList(selectedFontName);
        setFontMenuDisplay();
        fontFamilyMenu.setPopupMenuVisible(false);
        selectedFont = getSelectedFont();
    }
    private void onFontStyle(ListSelectionEvent event) {
        selectedFontStyle = fontStyleList.getSelectedValue();
        setFontMenuDisplay();
        fontStyleMenu.setPopupMenuVisible(false);
        selectedFont = getSelectedFont();
    }
    private void onFontSize(ListSelectionEvent event) {
        selectedFontSize = fontSizeList.getSelectedValue();
        setFontMenuDisplay();
        fontSizeMenu.setPopupMenuVisible(false);
        selectedFont = getSelectedFont();
    }
    private void createFontSizeAndStyleList(String fontFamilyName) {
        loadFontSizes(fontFamilyName);
        loadFontStyles(fontFamilyName);
    }
    private static String getFontFamilyName(Font f) {
        return f.getFontName().split(fontPropSplitor)[0];
    }
    private static String getFontStyle(Font f) {
        String [] parts = f.getFontName().split(fontPropSplitor);
        if ( 1 < parts.length) {
            return parts[1];
        } else {
            return null;
        }
    }
    @NotNull
    private Font getSelectedFont() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        return Arrays.stream(fonts)
                .filter(f -> selectedFontName.equals(getFontFamilyName(f)))
                .filter(f -> selectedFontStyle.equals(getFontStyle(f)))
                .filter(f -> selectedFontSize == f.getSize())
                .findAny()
                .orElse(new JLabel().getFont());
    }
}
