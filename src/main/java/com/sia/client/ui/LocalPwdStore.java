package com.sia.client.ui;

import com.sia.client.config.LocalConfig;
import com.sia.client.config.Utils;
import org.jdesktop.swingx.auth.PasswordStore;

import java.io.IOException;

public class LocalPwdStore extends PasswordStore {

    private final LocalConfig config;
    public LocalPwdStore(LocalConfig config) {
        this.config = config;
    }
    @Override
    public boolean set(final String s, final String s1, final char[] chars) {
        config.setProperty(LocalConfig.PwdKey,new String(chars));
        try {
            config.save();
            return true;
        } catch (IOException e) {
            Utils.log(e);
            return false;
        }
    }

    @Override
    public char[] get(final String s, final String s1) {
        String pwd = config.getProperty(LocalConfig.PwdKey);
        return null==pwd?null:pwd.toCharArray();
    }

    @Override
    public void removeUserPassword(final String s) {
        config.removeKey(LocalConfig.PwdKey);
    }
}
