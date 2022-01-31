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
        return config.getUserNames();
    }

    @Override
    public void setUserNames(final String[] strings) {
        config.setUserNames(strings);
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
        return config.containsUserName(s);
    }

    @Override
    public void addUserName(final String s) {
        config.updateCredential(s,"");
    }

    @Override
    public void removeUserName(final String s) {
        config.removeUser(s);
    }
}
