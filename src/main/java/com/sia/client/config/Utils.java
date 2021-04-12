package com.sia.client.config;

import javax.jms.MapMessage;
import java.net.URL;

public abstract class Utils {

    public static URL getMediaResource(String resourceName) {
        return Utils.class.getClassLoader().getResource(SiaConst.ImgPath+resourceName);
    }
    public static URL getConfigResource(String resourceName) {
        return Utils.class.getClassLoader().getResource(SiaConst.ConfigPath+resourceName);
    }
    public static void log(Exception e) {
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
}
