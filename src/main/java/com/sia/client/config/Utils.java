package com.sia.client.config;

import java.net.URL;

public abstract class Utils {

    public static URL getMediaResource(String resourceName) {
        return Utils.class.getClassLoader().getResource(SiaConst.ImgPath+resourceName);
    }
    public static URL getConfigResource(String resourceName) {
        return Utils.class.getClassLoader().getResource(SiaConst.ConfigPath+resourceName);
    }
}
