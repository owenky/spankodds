package com.sia.client.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;

public class LocalConfig {

    public static final String UserNameKey = "user";
    public static final String PwdKey = "pwd";
    private static final String configFileName=System.getProperty("java.io.tmpdir")+"spankodds.config";
    private final Properties properties;

    public static LocalConfig instance() {
        return LazyInitHolder.instance;
    }
    private LocalConfig()  {
        Properties tmp;
        try {
            tmp = load(configFileName);
        } catch (IOException e) {
            Utils.log(e);
            tmp = new Properties();
        }
        properties = tmp;
    }
    private Properties load(String filePath) throws IOException {

        Properties result = new Properties();
        if ( new File(filePath).exists()) {

            InputStream inputStream = new FileInputStream(filePath);
            Reader reader = new BufferedReader(new InputStreamReader(inputStream));
            result.load(reader);
        } else {
            Utils.log("Warning: configuration file "+filePath+" does not exist.");
        }
        return result;
    }
    public String getProperty(String key) {
        return properties.getProperty(key,"");
    }
    public void setProperty(String key,String value) {
        properties.put(key,value);
    }
    public Set<String> getAllPropertyNames(){
        return properties.stringPropertyNames();
    }
    public void removeKey(String key) {
        properties.remove(key);
    }
    public void save() throws IOException {
        try (final OutputStream outputstream
                     = new FileOutputStream(configFileName)) {
            properties.store(outputstream,null);
        }
    }
//////////////////////////////////////////////////////////////////////////////
    private static abstract class LazyInitHolder {
        private static final LocalConfig instance = new LocalConfig();
    }
}
