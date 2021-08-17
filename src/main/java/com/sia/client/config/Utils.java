package com.sia.client.config;

import javax.jms.MapMessage;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public abstract class Utils {

    private static final ExecutorService executorService =Executors.newWorkStealingPool(2);
    public static URL getMediaResource(String resourceName) {
        return getResource(SiaConst.ImgPath+resourceName);
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
    public static void log(Throwable e) {
        e.printStackTrace();
    }
    public static void log(String mesg) {
        System.out.println(mesg);
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
    public static void checkAndRunInEDT(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
    public static void showMessageDialog(Component parentComponent, Object message) throws HeadlessException {
        checkAndRunInEDT(()-> {
            JOptionPane.showMessageDialog(parentComponent, message);
        });
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
    public static void removeItemListeners(JMenuItem menuItem) {
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
}
