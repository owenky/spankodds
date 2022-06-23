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
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class LocalConfig {

    private static final String UserKey = "users";
    private static final String userSeparateStr = ";";
    private static final String userPwdSeparator = ":";
    private static final String configFileName=System.getProperty("java.io.tmpdir")+"spankodds.config";
    private final Properties properties;
    private final Vector<String> users; // changed from HashSet to maintain order of items read
    private final Map<String,String> user2pwd;

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

        String credentialStr = properties.getProperty(UserKey,"");
        String [] credentials = credentialStr.split(userSeparateStr);
        users = new Vector(credentials.length);
        user2pwd = new HashMap<>(credentials.length);
        for (final String credential : credentials) {
            if ( ! "".equals(credential.trim())) {
                String[] usrPwd = credential.split(userPwdSeparator);
                if ( 2 == usrPwd.length) {
                    String user = usrPwd[0].trim();
                    users.add(user);
                    user2pwd.put(user, usrPwd[1].trim());
                }
            }
        }
    }
    public String [] getUserNames() {
        return users.toArray(new String [0]);
    }
    public void setUserNames(String [] userNames) {
        users.clear();
        Collections.addAll(users, userNames);
    }
    public boolean containsUserName(String userName) {
        return users.contains(userName);
    }
    public void updateCredential(String user, String pwd) {
        users.add(user);
        user2pwd.put(user,pwd);
    }
    public String getPassword(String user) {
        return user2pwd.get(user);
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
    public void removeUser(String user) {
        users.remove(user);
        user2pwd.remove(user);
    }
    public void save() throws IOException {
        StringBuilder credentialStr = new StringBuilder();
        for(String usr: users) {
            if ( ! "".equals(usr)) {
                credentialStr.append(usr).append(userPwdSeparator).append(user2pwd.get(usr)).append(userSeparateStr);
            }
        }
        properties.setProperty(UserKey,credentialStr.toString());
        try (final OutputStream outputstream
                     = new FileOutputStream(configFileName)) {
            properties.store(outputstream,null);
        }
    }
    public void save(String username) throws IOException { // this will put last logged username in front
        StringBuilder credentialStr = new StringBuilder();
        credentialStr.append(username).append(userPwdSeparator).append(user2pwd.get(username)).append(userSeparateStr);
        for(String usr: users) {
            if ( ! "".equals(usr) && !username.equals(usr)) {
                credentialStr.append(usr).append(userPwdSeparator).append(user2pwd.get(usr)).append(userSeparateStr);
            }
        }
        properties.setProperty(UserKey,credentialStr.toString());
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
