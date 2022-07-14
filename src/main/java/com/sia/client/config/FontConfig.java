package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sia.client.ui.SpankyWindow;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
    private boolean changed = false;
    @JsonIgnore
    private Integer fontHeight = 15;
    @JsonIgnore
    private boolean needToCalculateFontHeight = true;
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
//    @JsonIgnore
//    private static final Font DefaultHeaderFont = new Font("Verdana", Font.BOLD, 11);
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

    FontConfig() {
        Utils.checkAndRunInEDT(this::init);
    }
    private void init() {
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
        loadFontNames();
    }
    //getter and setter for JSON serialization/deserialization
    public String getSelectedFontName() {
        return selectedFontName;
    }

    public void setSelectedFontName(String selectedFontName) {
        this.selectedFontName = selectedFontName;
        needToCalculateFontHeight = true;
    }

    public int getSelectedFontSize() {
        return selectedFontSize;
    }

    public void setSelectedFontSize(int selectedFontSize) {
        this.selectedFontSize = selectedFontSize;
        needToCalculateFontHeight = true;
    }

    public String getSelectedFontStyle() {
        return selectedFontStyle;
    }

    public void setSelectedFontStyle(String selectedFontStyle) {
        this.selectedFontStyle = selectedFontStyle;
        needToCalculateFontHeight = true;
    }
    @JsonIgnore
    public Font getDefaultHeaderFont() {
        return getSelectedFont().deriveFont(Font.BOLD);
    }
    @JsonIgnore
    public Font getSelectedFont() {
        if ( null == selectedFont) {
            selectedFont = systemDefaultFont;
            setSelectedFontAttributes(selectedFont);
            needToCalculateFontHeight = true;
        }
        return selectedFont;
    }
    @JsonIgnore
    public Integer getFontHeight() {
        if (needToCalculateFontHeight) {
            fontHeight = changeFontHeight();
        }
        return fontHeight;
    }
    @JsonIgnore
    public Integer getNormalRowHeight() {
        return getFontHeight() * 2;
    }
    @JsonIgnore
    public Integer getSoccerRowHeight() {
        return getFontHeight() * 4;
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
            setSelectedValuesOfFontList();
            needToCalculateFontHeight = true;
        }
    }
    private void setSelectedFontAttributes(Font f) {
        selectedFontName = getFontName(f);
        selectedFontStyle = getFontStyle(f);
        selectedFontSize = f.getSize();
        needToCalculateFontHeight = true;
    }
    private void setSelectedValuesOfFontList() {
        fontNameList.setSelectedValue(selectedFontName,true);
        fontStyleList.setSelectedValue(selectedFontStyle,true);
        fontSizeList.setSelectedValue(selectedFontSize,true);
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

        loadFontStyles(selectedFontName);
        setFontMenuProperties();
        applyMenuItem.setEnabled(false);
        return fontMenu;
    }
    private void setFontMenuProperties() {
        fontFamilyMenu.setText(selectedFontName);
        fontSizeMenu.setText( 0<=selectedFontSize? String.valueOf(selectedFontSize):"Size ?");
        fontStyleMenu.setText( null != selectedFontStyle?selectedFontStyle: "Style ?");

        boolean enableApply = 0 <= selectedFontSize && null != selectedFontStyle && changed;
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
        needToCalculateFontHeight = true;
    }
    private void reSetApplicationDefaultFont() {
        selectedFont = systemDefaultFont;
        applyApplicationDefaultFont(selectedFont);
        setSelectedFontAttributes(selectedFont);
        changed = false;
        setFontMenuProperties();
        needToCalculateFontHeight = true;
    }
    private static void applyApplicationDefaultFont(Font font) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements() ) {
            Object key = keys.nextElement();
            Object value = UIManager.get( key );
            if ( value instanceof Font ) {
//                System.out.println("before key="+key+", value="+value);
                UIManager.put( key, font );
//                System.out.println("after key="+key+", value="+UIManager.getFont( key ));
            }
        }
        SpankyWindow.applyToAllWindows((tp)-> {
            SpankyWindow sw = SpankyWindow.findSpankyWindow(tp.getWindowIndex());
            sw.revalidate();
            sw.repaint();
            sw.getSportsTabPane().rebuildMainScreen();
        });
        FontConfig fontConfig = Config.instance().getFontConfig();
        fontConfig.changed = false;
        fontConfig.setFontMenuProperties();
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
            needToCalculateFontHeight = true;
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
        needToCalculateFontHeight = true;
    }
    private void onFontFamilyName(ListSelectionEvent event) {
        changed = !selectedFontName.equals(fontNameList.getSelectedValue());
        if ( changed ) {
            selectedFontName = fontNameList.getSelectedValue();
        }
//        createFontSizeAndStyleList(selectedFontName);
//        validateStyleAndSize();
        setFontMenuProperties();
        fontFamilyMenu.getPopupMenu().setVisible(false);
        needToCalculateFontHeight = true;
    }
    private void onFontStyle(ListSelectionEvent event) {
        changed = !selectedFontStyle.equals(fontStyleList.getSelectedValue());
        if ( changed ) {
            selectedFontStyle = fontStyleList.getSelectedValue();
        }

        setFontMenuProperties();
        fontStyleMenu.getPopupMenu().setVisible(false);
        needToCalculateFontHeight = true;
    }
    private void onFontSize(ListSelectionEvent event) {
        changed = selectedFontSize != fontSizeList.getSelectedValue();
        if ( changed ) {
            selectedFontSize = fontSizeList.getSelectedValue();
        }

        setFontMenuProperties();
        fontSizeMenu.getPopupMenu().setVisible(false);
        needToCalculateFontHeight = true;
    }
    private void createFontSizeAndStyleList(String fontFamilyName) {
        loadFontStyles(fontFamilyName);
    }
    private int changeFontHeight() {
        AtomicReference<FontMetrics> fmRef = new AtomicReference<>();
        if ( ! SwingUtilities.isEventDispatchThread() ) {

            try {
                SwingUtilities.invokeAndWait(()-> fmRef.set(SwingUtilities2.getFontMetrics(new JLabel(), getSelectedFont())));
            } catch (InterruptedException | InvocationTargetException e) {
                Utils.log(e);
            }
        } else {
            fmRef.set(SwingUtilities2.getFontMetrics(new JLabel(), getSelectedFont()));
        }
        FontMetrics fm = fmRef.get();
        int fontHeight;
        if ( null != fm) {
            fontHeight = fm.getHeight();// +fm.getDescent();
        } else {
            fontHeight = 15;
        }
        needToCalculateFontHeight = false;
        System.out.println("fontHeight="+fontHeight);
        return fontHeight;
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
