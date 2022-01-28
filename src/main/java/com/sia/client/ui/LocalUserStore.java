package com.sia.client.ui;

import com.sia.client.config.LocalConfig;
import org.jdesktop.swingx.auth.UserNameStore;

public class LocalUserStore extends UserNameStore {

    private final LocalConfig config;
    public LocalUserStore(LocalConfig config) {
        this.config = config;
    }
    @Override
    public String[] getUserNames() {
        String [] users = new String[1];
        users[0]=config.getProperty(LocalConfig.UserNameKey);
        return users;
    }

    @Override
    public void setUserNames(final String[] strings) {
        config.setProperty(LocalConfig.UserNameKey,strings[0]);
    }

    @Override
    public void loadUserNames() {
        //user names already loaded in config.
    }

    @Override
    public void saveUserNames() {
//        try {
//            config.save();
//        } catch (IOException e) {
//            Utils.log(e);
//        }
        //LocalpwdStore.set() saves both user name and password. -- 2022-01-27
    }

    @Override
    public boolean containsUserName(final String s) {
        return s.equals(config.getProperty(LocalConfig.UserNameKey));
    }

    @Override
    public void addUserName(final String s) {
        config.setProperty(LocalConfig.UserNameKey,s);
    }

    @Override
    public void removeUserName(final String s) {
        config.removeKey(LocalConfig.UserNameKey);
    }
}
