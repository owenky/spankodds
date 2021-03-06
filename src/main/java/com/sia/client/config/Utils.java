package com.sia.client.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sia.client.model.ViewValue;

import javax.jms.MapMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public abstract class Utils {

    private static final ExecutorService executorService =Executors.newWorkStealingPool(2);
    private static final Map<String, SoftReference<ImageIcon>> imageIconCache = new HashMap<>();
    public static Logger logger;

    public static URL getMediaResource(String resourceName) {
        return getResource(SiaConst.ImgPath+resourceName);
    }
    public static Image getImage(String imageFileName) {
        return getImageIcon(imageFileName).getImage();
    }
    public static synchronized ImageIcon getImageIcon(String imageFileName) {
        if( null == imageFileName) {
            return null;
        }
        SoftReference<ImageIcon> imageIconRef = imageIconCache.get(imageFileName);
        ImageIcon imageIcon;
        if ( null == imageIconRef ) {
            imageIcon = new ImageIcon(Utils.getMediaResource(imageFileName));
            imageIconCache.put(imageFileName,new SoftReference<>(imageIcon));
        } else {
            imageIcon = imageIconRef.get();
            if ( null == imageIcon) {
                imageIcon = new ImageIcon(Utils.getMediaResource(imageFileName));
                imageIconCache.put(imageFileName,new SoftReference<>(imageIcon));
            }
        }
        return imageIcon;
    }
    public static URL getConfigResource(String resourceName) {
        return getResource(SiaConst.ConfigPath+resourceName);
    }
    public static URL getResource(String resourceName) {
        URL  url = Utils.class.getResource(resourceName);
        if ( null == url) {
            log("Can't find resource "+resourceName+" using Utils.class.getResource()");
            String resourceName2 = resourceName.substring(1);
            log("Trying locating "+resourceName2+" Utils.class.getClassLoader().getResource()");
            url = Utils.class.getClassLoader().getResource(resourceName2);
            if ( null == url) {
                IllegalArgumentException e = new IllegalArgumentException("Can't find resource " + resourceName2);
                log(e);
                throw e;
            }
        }
        return url;
    }
    public static void consoleLogPeek(String mesg) {
        logger.consoleLogPeek(mesg);
    }
    public static void consoleLogPeek(Exception e) {
        logger.consoleLogPeek(e);
    }
    public static void consoleLogPeekGameId(String keyword,int gameId) {
        logger.consoleLogPeekGameId(keyword,gameId);
    }
    public static void log(Throwable e) {
        log(e.getLocalizedMessage(),e);
    }
    public static void log(String errMsg,Throwable e) {
        logger.log(errMsg,e);
    }
    public static void debug(String mesg) {
        logger.debug(mesg);
    }
    public static void log(String mesg) {
        logger.log(mesg);
    }
    public static void log(Object mesg) {
        logger.log(mesg);
    }
    public static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
    public static String nowShortString() {
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        Instant instant = date.toInstant();

        LocalDateTime ldt = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return ldt.format(sdf);
    }
    public static void main(String [] argv) {
        System.out.println(nowShortString());
    }
    public static int getInt(MapMessage mapMessage, String name) {
        try {
            return mapMessage.getInt(name);
        } catch ( Exception e) {
            log(e);
            return 0;
        }
    }
    public static String getString(MapMessage mapMessage, String name) {
        try {
            return mapMessage.getString(name);
        } catch ( Exception e) {
            log(e);
            return "";
        }
    }
    public static long getLong(MapMessage mapMessage, String name,long defaultValue) {
        try {
            return mapMessage.getLong(name);
        } catch ( Exception e) {
            log(e);
            return defaultValue;
        }
    }
    public static int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch ( Exception e) {
            log(e);
            return defaultValue;
        }
    }
    public static void sleep(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            Utils.log(e);
        }

    }
    public static void checkAndRunInEDT(Runnable r) {
        checkAndRunInEDT(r,false);
    }
    public static void checkAndRunInEDT(Runnable r,boolean toBlock) {
        if (SwingUtilities.isEventDispatchThread()) {
            try {
                r.run();
            }catch( Exception e) {
                log(e);
            }
        } else {
            try {
                if ( toBlock) {
                    SwingUtilities.invokeAndWait(r);
                } else {
                    SwingUtilities.invokeLater(r);
                }
            } catch ( Exception e) {
                log(e);
            }
        }
    }
    public static void showMessageDialog(Component parentComponent, Object message) throws HeadlessException {
        checkAndRunInEDT(()-> {
            JOptionPane.showMessageDialog(parentComponent, message);
        });
    }
    public static void showErrorMessageDialog(Component parentComponent, Object message) throws HeadlessException {
        checkAndRunInEDT(()-> {
            JOptionPane.showMessageDialog(parentComponent, message,"ERROR !",JOptionPane.ERROR_MESSAGE);
        });
    }
    public static int showOptions(Component parentComponent, Object message) throws HeadlessException {

        return JOptionPane.showConfirmDialog(parentComponent,message,"Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
    }
    public static void respectComponentSize(JComponent comp, Dimension dim) {
        comp.setPreferredSize(dim);
        comp.setMaximumSize(dim);
    }
    public static void ensureNotEdtThread() {
        if ( SwingUtilities.isEventDispatchThread()) {
            log(new Exception("Worker Thread Vialation: This action should not happen in EDT"));
        }
    }
    public static void ensureEdtThread() {
        if ( ! SwingUtilities.isEventDispatchThread()) {
            log(new Exception(" This action SHOULD happen in EDT"));
        }
    }
    public static void ensureBackgroundExecution(Runnable r) {
        if ( SwingUtilities.isEventDispatchThread()) {
            executorService.submit(r);
        } else {
            r.run();
        }
    }
    public static boolean parse(String str, boolean defaultValue) {
        Boolean b;
        try {
            b = Boolean.parseBoolean(str);
        } catch( Exception e) {
            log(e);
            b = null;
        }
        return null==b?defaultValue:b;
    }
    public static int parse(String str, int defaultValue) {
        Integer b;
        try {
            b = Integer.parseInt(str);
        } catch( Exception e) {
            log(e);
            b = null;
        }
        return null==b?defaultValue:b;
    }
    public static void resizeAndCenterComponent(Component component){

        Dimension effectiveScreenSize_ = getEffectiveScreenDimension();

        boolean sizeChanged = false;
        int width = component.getSize().width;
        int height = component.getSize().height;

        int effectiveScreenWidth_ = (int)effectiveScreenSize_.getWidth();
        int effectiveScreenHeight_ = (int)effectiveScreenSize_.getHeight();
        if ( width > effectiveScreenWidth_){
            width = effectiveScreenWidth_;
            sizeChanged = true;
        }

        if ( height > effectiveScreenHeight_){
            height = effectiveScreenHeight_;
            sizeChanged = true;
        }

        if ( sizeChanged){
            Dimension newDim_ = new Dimension(width,height);
            setComponentPreferredSize(component,newDim_);
        }
        Point left_top_point = getScreenCenterLocation(component);

//System.err.println("in SBTUiManager$resizeAndCenterComponent(), width="+width+" height="+height);
        component.setBounds(left_top_point.x, left_top_point.y,width ,height );
    }
    private static Dimension effectiveScreenSize;
    public static Dimension getEffectiveScreenDimension() {
        return effectiveScreenSize;
    }
    public static void setComponentPreferredSize(Component comp_,Dimension dim_){
        comp_.setSize(dim_);
        comp_.setPreferredSize(dim_);
    }
    private static Insets screenInsets;
    public static Insets getScreenInsets() {
        return screenInsets;
    }
    public static void computeEffectiveScreenSize(Component visibleComponent) {
        GraphicsConfiguration gcf = visibleComponent.getGraphicsConfiguration();
        if ( gcf != null) {
            screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gcf);

            Dimension screenFullSize_ = Toolkit.getDefaultToolkit().getScreenSize();
            int screen_width_ = (int)screenFullSize_.getWidth();
            int screen_height_ = (int)screenFullSize_.getHeight();

            Insets screen_insets_ = getScreenInsets();

            int screen_effective_width_ = screen_width_  - screen_insets_.left - screen_insets_.right;
            int screen_effective_height_ = screen_height_  - screen_insets_.bottom - screen_insets_.top;
            effectiveScreenSize = new Dimension(screen_effective_width_,screen_effective_height_);
        }
    }
    public static Point getScreenCenterLocation(Component component){

        Insets screenInsets_ = getScreenInsets();
        Dimension effectiveScreenDim_ = getEffectiveScreenDimension();
        int screenWidth = (int)effectiveScreenDim_.getWidth();
        int componentWidth = component.getWidth();
        int x = ( screenWidth-componentWidth ) / 2 + screenInsets_.left;

        int screenHeight = (int) effectiveScreenDim_.getHeight();
        int componentHeight = component.getHeight();
        int y =  (screenHeight - componentHeight) / 2 + screenInsets_.top;

        return new Point(x,y);
    }
    public static void fireVisibilityChangedEvent(Component screenListener,boolean screen_hidden_status_){
        if ( screenListener != null ){
            ComponentListener[] componentListeners = screenListener.getComponentListeners();
            if ( componentListeners != null ){
                int component_id;
                if ( screen_hidden_status_ ){
                    component_id = ComponentEvent.COMPONENT_HIDDEN;
                }else{
                    component_id = ComponentEvent.COMPONENT_SHOWN;
                }

                ComponentEvent event = new ComponentEvent(screenListener,component_id);
                for ( int i=0;i<componentListeners.length;i++){
                    ComponentListener componentHiddenListener = componentListeners[i];
                    if ( screen_hidden_status_ ) {
                        componentHiddenListener.componentHidden(event);
                    }else{
                        componentHiddenListener.componentShown(event);
                    }
                }
            }
        }
    }
    public static void removeItemListeners(AbstractButton menuItem) {
        if ( null != menuItem) {
            ActionListener[] allListeners = menuItem.getActionListeners();
            if (null != allListeners) {
                for (ActionListener l : allListeners) {
                    menuItem.removeActionListener(l);
                }
            }
        }
    }

    /**
     *
     * @return it dest is Final, it replace final and FINAL etc in sourceString with Final
     */
    public static String replaceIgnoreCase(String sourceString, String destStr) {
        Pattern finalPattern = Pattern.compile(destStr,CASE_INSENSITIVE);
        Matcher matcher = finalPattern.matcher(sourceString);
        while (matcher.find()) {
            String matchedStr = matcher.group(0);
            sourceString = sourceString.replace(matchedStr,destStr);
        }
        return sourceString;
    }
    public static long parseTimestamp(String timeStampStr) {
        try {
            return Long.parseLong(timeStampStr);
        }catch(NumberFormatException e) {
            log(e);
            return 0L;
        }
    }
    public static boolean containsOnlyAlphanumeric(String str) {
        return str.matches("[0-9a-zA-Z]+");
    }
    public static String getTableCellToolTipText(JTable table, MouseEvent e) {
        java.awt.Point p = e.getPoint();
        int rowIndex = table.rowAtPoint(p);
        int colIndex = table.columnAtPoint(p);

        String toolTipTxt;
        try {
            Object value = table.getValueAt(rowIndex, colIndex);
            if ( value instanceof ViewValue) {
                toolTipTxt = ((ViewValue)value).getTooltiptext();
                if ( "".equals(toolTipTxt) || "null".equalsIgnoreCase(toolTipTxt)) {
                    toolTipTxt = null;
                }
            } else {
                toolTipTxt = null;
            }
        } catch (RuntimeException ex) {
            log(ex);
            toolTipTxt = null;
        }

        return toolTipTxt;
    }
    public static boolean isEmpty(String str) {
        return ( null == str || 0 ==str.trim().length());
    }
    public static JPanel createTitlePanel(String title) {
        JLabel titleLabel = new JLabel(title,SwingConstants.CENTER);
        return createCompCenteredPanel(titleLabel);
    }

    /**
     *
     * @param cmps: up to 3 components.
     * @return
     */
    public static JPanel createCompCenteredPanel(JComponent ... cmps) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JComponent left = null;
        JComponent center = null;
        JComponent right = null;

        if ( 0 == cmps.length || 3 < cmps.length) {
            throw new IllegalArgumentException("length of input components should be between 1 and 3");
        }
        if ( 1 == cmps.length) {
            center = cmps[0];
        } else if ( 2 == cmps.length) {
            left = cmps[0];
            center = cmps[1];
        } else {
            left = cmps[0];
            center = cmps[1];
            right = cmps[2];
        }
        panel.add(center,BorderLayout.CENTER);
        if ( null != left) {
            panel.add(left,BorderLayout.WEST);
        }
        if ( null != right) {
            panel.add(right,BorderLayout.EAST);
        }
        return panel;
    }
    public static boolean isSameObject(Object o1, Object o2){
        if ( o1 == null && o2 == null){
            return true;
        }else if ( o1 == null && o2 != null){
            return false;
        }else if ( o1 != null && o2 == null){
            return false;
        }else{
            return o1.equals(o2);
        }
    }
    public static boolean isBlank(Object o_) {
        if (o_ == null) {
            return true;
        }
        return 0 == o_.toString().length();
    }
    public static String replaceStr(String sStr, String oldStr, String newStr){
        if (sStr != null && oldStr != null && newStr != null){
            int i1 = 0;
            String s2 = sStr;
            int fromIndex_ = 0;
            while ((i1 = s2.indexOf(oldStr,fromIndex_)) != -1){
                s2 = s2.substring(0, i1) + newStr + s2.substring(i1 + oldStr.length(), s2.length());
                fromIndex_ = i1+newStr.length();
            }
            return s2;
        }else{
            return sStr;
        }
    }
    private static final Pattern integerPattern = Pattern.compile("-?\\d+");
    public static boolean isIntegerString(String str) {
        if ( null == str) {
            return false;
        }
        str = str.trim();
        return integerPattern.matcher(str).matches();
    }
    private static final Pattern numericPattern = Pattern.compile("-?(\\d+)?(\\.)?(\\d+)?");
    public static boolean isNumericString(String str) {
        if ( null == str ) {
            return false;
        }
        str = str.trim();
        if ( 0 == str.length()) {
            return false;
        } else if (".".equals(str)) {
            return false;
        }
        return numericPattern.matcher(str).matches();
    }
    public static void centerComponent ( Component comp) {
        Dimension preferredDim = comp.getPreferredSize();
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Point location = new Point ((screenDim.width-preferredDim.width)/2, (screenDim.height-preferredDim.height)/2);
        comp.setLocation(location);
    }
}
