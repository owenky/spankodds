package com.sia.client.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FontConfig implements ActionListener {

    @JsonIgnore
    private static final Pattern fontPropSplitorPattern;
    @JsonIgnore
    private static final Font systemDefaultFont;
    @JsonIgnore
    private static final int fontNameMenuWidth=140;
    @JsonIgnore
    private static final int fontSizeMenuWidth=40;
    @JsonIgnore
    private static final int fontStyleMenuWidth=80;
    @JsonIgnore
    private static final int menuItemHeight=20;
    @JsonIgnore
    private static final Integer [] fontArray = new Integer[] {10,11,12,14,16,18,20,22,24,26,28};
    @JsonIgnore
    private static final String [] styleArray = new String[] {"Plain","Bold","Italic"};
    @JsonIgnore
    private static final String [] excludedFontNameArr = new String[] {"ACaslonPro-","AGaramondPro-"};
    @JsonIgnore
    private final JList<String> fontNameList = new JList<>();
    @JsonIgnore
    private final JList<Integer> fontSizeList = new JList<>();
    @JsonIgnore
    private final JList<String> fontStyleList = new JList<>();
    @JsonIgnore
    private JScrollPane fontFamilyPane;
    @JsonIgnore
    private JScrollPane fontSizePane;
    @JsonIgnore
    private JScrollPane fontStylePane;
    @JsonIgnore
    private final JMenu fontFamilyMenu = new JMenu("Font Family");
    @JsonIgnore
    private final JMenu fontSizeMenu = new JMenu("Font Sizes");
    @JsonIgnore
    private final JMenu fontStyleMenu = new JMenu("Font Style");
    @JsonIgnore
    private final JMenuItem applyMenuItem = new JMenuItem("Apply");
    @JsonIgnore
    private final JMenuItem resetMenuItem = new JMenuItem("Reset");
    @JsonIgnore
    private static final Font DefaultHeaderFont = new Font("Verdana", Font.BOLD, 11);
    @JsonIgnore
    private static final Map<Integer,String> fontStyleInt2StrMap = new HashMap<>();
    @JsonIgnore
    private static final Map<String,Integer> fontStyleSting2IntMap = new HashMap<>();

    static {
        fontStyleInt2StrMap.put(Font.BOLD,"Bold");
        fontStyleInt2StrMap.put(Font.PLAIN,"Plain");
        fontStyleInt2StrMap.put(Font.ITALIC,"Italic");

        for(int style: fontStyleInt2StrMap.keySet()) {
            String styleStr = fontStyleInt2StrMap.get(style);
            fontStyleSting2IntMap.put(styleStr,style);
        }
        fontPropSplitorPattern= Pattern.compile("(?i)(.*)(-| |\\.)(Bold|Italic|Regular)");

        systemDefaultFont = new JLabel().getFont();
    }
    @JsonIgnore
    private Font selectedFont;
    private String selectedFontName;
    private int selectedFontSize;
    private String selectedFontStyle;

    public FontConfig() {
        fontSizeList.setModel( new AbstractListModel<Integer>() {
            public int getSize() {
                return fontArray.length;
            }
            public Integer getElementAt(int i) {
                return fontArray[i];
            }
        });
        fontStyleList.setModel( new AbstractListModel<String>() {
            public int getSize() {
                return styleArray.length;
            }
            public String getElementAt(int i) {
                return styleArray[i];
            }
        });
    }
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
    public static Font getDefaultHeaderFont() {
        return DefaultHeaderFont;
    }
    public Font getSelectedFont() {
        return selectedFont;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    private void configSelectedFontProp() {
        if ( null == selectedFont) {

            if (null != selectedFontName) {
                //font name/style/size are deserialized, construct font
                selectedFont = createFont();
            } else {
                selectedFont = systemDefaultFont;
                setSelectedFontAttributes(selectedFont);
            }
        }
    }
    private void setSelectedFontAttributes(Font f) {
        selectedFontName = getFontName(f);
        selectedFontStyle = getFontStyle(f);
        selectedFontSize = f.getSize();
    }
    public JMenu createFontMenu() {


        configSelectedFontProp();
        fontFamilyPane = new JScrollPane(fontNameList);
        fontFamilyPane.setPreferredSize(new Dimension(fontNameMenuWidth,400));
        fontFamilyMenu.add(fontFamilyPane);

        fontSizePane = new JScrollPane(fontSizeList);
        fontSizePane.setPreferredSize(new Dimension(fontSizeMenuWidth,200));
        fontSizeMenu.add(fontSizePane);

        fontStylePane = new JScrollPane(fontStyleList);
        fontStylePane.setPreferredSize(new Dimension(fontStyleMenuWidth,70));
        fontStyleMenu.add(fontStylePane);

        fontSizeMenu.setPreferredSize(new Dimension(fontNameMenuWidth,menuItemHeight));
        fontStyleMenu.setPreferredSize(new Dimension(fontNameMenuWidth,menuItemHeight));
        fontFamilyMenu.setPreferredSize(new Dimension(fontNameMenuWidth,menuItemHeight));
        applyMenuItem.setPreferredSize(new Dimension(fontNameMenuWidth,menuItemHeight));
        resetMenuItem.setPreferredSize(new Dimension(fontNameMenuWidth,menuItemHeight));

        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(fontFamilyMenu);
        fontMenu.add(fontSizeMenu);
        fontMenu.add(fontStyleMenu);
        fontMenu.add(new JMenuItem("---------"));
        fontMenu.add(applyMenuItem);
        fontMenu.add(resetMenuItem);

        fontNameList.addListSelectionListener(this::onFontFamilyName);
        fontSizeList.addListSelectionListener(this::onFontSize);
        fontStyleList.addListSelectionListener(this::onFontStyle);
        applyMenuItem.addActionListener((ae-> setApplicationDefaultFont()));
        resetMenuItem.addActionListener((ae-> reSetApplicationDefaultFont()));

        loadFontNames();
        loadFontStyles(selectedFontName);
        setFontMenuProperties();
        applyMenuItem.setEnabled(false);
        return fontMenu;
    }
    private void setFontMenuProperties() {
        fontFamilyMenu.setText(selectedFontName);
        fontSizeMenu.setText( 0<=selectedFontSize? String.valueOf(selectedFontSize):"Size ?");
        fontStyleMenu.setText( null != selectedFontStyle?selectedFontStyle: "Style ?");

        boolean enableApply = 0 <= selectedFontSize && null != selectedFontStyle;
        applyMenuItem.setEnabled(enableApply);
    }
    private void loadFontNames() {

        List<String> fontFamilyList = getAllFontNames();
        fontNameList.setModel( new AbstractListModel<String>() {
            public int getSize() {
                return fontFamilyList.size();
            }
            public String getElementAt(int i) {
                return fontFamilyList.get(i);
            }
        });

    }
    static List<String> getAllFontNames() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = graphicsEnvironment.getAllFonts();
        Set<String> uniqFontFaimlies = new HashSet<>();
        final List<String> fontFamilyList = new ArrayList<>();
        for(Font f: fonts) {
            String fontFamily = getFontName(f);
            if ( ! isFontNameExcluded(fontFamily) && !uniqFontFaimlies.contains(fontFamily)) {
                uniqFontFaimlies.add(fontFamily);
                fontFamilyList.add(fontFamily);
            }
        }
        return fontFamilyList.stream().sorted().collect(Collectors.toList());
    }
    static boolean isFontNameExcluded(String fontName) {
        boolean isExcluded = false;
        for(String excludedName: excludedFontNameArr) {
            if ( fontName.contains(excludedName)){
                isExcluded = true;
                break;
            }
        }
        return isExcluded;
    }
    private void loadFontStyles(String fontFamilyName) {

//        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        Font[] fonts = graphicsEnvironment.getAllFonts();
//        final List<String> myStyleList = Arrays.stream(fonts).filter(f-> fontFamilyName.equals(getFontName(f)))
//                .map(FontConfig::getFontStyle).filter(Objects::nonNull).distinct().collect(Collectors.toList());
//
//        fontStyleList.setModel( new AbstractListModel<String>() {
//            public int getSize() {
//                return myStyleList.size();
//            }
//            public String getElementAt(int i) {
//                return myStyleList.get(i);
//            }
//        });
        //font style for each font is same, and defined statically.
    }
    public void setApplicationDefaultFont() {
        selectedFont = createFont();
        applyApplicationDefaultFont(selectedFont);
    }
    private void reSetApplicationDefaultFont() {
        selectedFont = systemDefaultFont;
        applyApplicationDefaultFont(selectedFont);
        setSelectedFontAttributes(selectedFont);
        setFontMenuProperties();
    }
    private static void applyApplicationDefaultFont(Font font) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements() ) {
            Object key = keys.nextElement();
            Object value = UIManager.get( key );
            if ( value instanceof Font ) {
                System.out.println("before key="+key+", value="+value);
                UIManager.put( key, font );
                System.out.println("after key="+key+", value="+UIManager.getFont( key ));
            }
        }
        SpankyWindow.applyToAllWindows((tp)-> {
            SpankyWindow sw = SpankyWindow.findSpankyWindow(tp.getWindowIndex());
            sw.revalidate();
            sw.repaint();
        });
    }
    private static <E> int getModelIndex(ListModel<E> model, E value) {
        int index = -1;
        for(int i=0;i<model.getSize();i++) {
            if ( model.getElementAt(i).equals(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
    private void validateStyleAndSize() {
        String lastSelectedStyle = fontStyleMenu.getText();
        int matchedStyleIndex = getModelIndex(fontStyleList.getModel(),lastSelectedStyle);

        if ( null != selectedFontStyle) {
            if (0 <= matchedStyleIndex) {
                fontStyleList.setSelectedIndex(matchedStyleIndex);
                selectedFontStyle = lastSelectedStyle;
            } else {
                fontStyleList.clearSelection();
                selectedFontStyle = null;
            }
        }

        if ( 0 < selectedFontSize) {
            String lastSelectedSize = fontSizeMenu.getText();
            int matchedSizeIndex = getModelIndex(fontSizeList.getModel(), Integer.parseInt(lastSelectedSize));
            if (0 <= matchedSizeIndex) {
                fontSizeList.setSelectedIndex(matchedSizeIndex);
                selectedFontSize = Integer.parseInt(lastSelectedSize);
            } else {
                fontSizeList.clearSelection();
                selectedFontSize = -1;
            }
        }
        setFontMenuProperties();
    }
    private void onFontFamilyName(ListSelectionEvent event) {
        boolean isChanged = !selectedFontName.equals(fontNameList.getSelectedValue());
        if ( isChanged ) {
            selectedFontName = fontNameList.getSelectedValue();
            applyMenuItem.setEnabled(true);
        }
        createFontSizeAndStyleList(selectedFontName);
        validateStyleAndSize();
        fontFamilyMenu.getPopupMenu().setVisible(false);
    }
    private void onFontStyle(ListSelectionEvent event) {
        boolean isChanged = !selectedFontStyle.equals(fontStyleList.getSelectedValue());
        if ( isChanged ) {
            selectedFontStyle = fontStyleList.getSelectedValue();
            applyMenuItem.setEnabled(true);
        }

        setFontMenuProperties();
        fontStyleMenu.getPopupMenu().setVisible(false);
    }
    private void onFontSize(ListSelectionEvent event) {
        boolean isChanged = selectedFontSize != fontSizeList.getSelectedValue();
        if ( isChanged ) {
            selectedFontSize = fontSizeList.getSelectedValue();
            applyMenuItem.setEnabled(true);
        }

        setFontMenuProperties();
        fontSizeMenu.getPopupMenu().setVisible(false);
    }
    private void createFontSizeAndStyleList(String fontFamilyName) {
        loadFontStyles(fontFamilyName);
    }
    static String getFontName(Font f) {
        String fontName = f.getName();
        return deriveFontName(fontName);
    }
    static String deriveFontName(String fontName) {

        Matcher matcher = fontPropSplitorPattern.matcher(fontName);

        while ( matcher.matches()) {
            fontName = matcher.group(1);
            matcher = fontPropSplitorPattern.matcher(fontName);
        }
        return fontName;
    }
    static String getFontStyle(Font f) {
        return fontStyleInt2StrMap.get(f.getStyle());
    }
    Font createFont() {
        return new Font(selectedFontName, fontStyleSting2IntMap.get(selectedFontStyle), selectedFontSize);
    }
}
