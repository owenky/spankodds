package com.sia.client.ui.lineseeker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AlertComponentListener implements KeyListener, ActionListener {

    private final AlertLayout alertLayout;
    public AlertComponentListener(AlertLayout alertLayout) {
        this.alertLayout = alertLayout;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        checkAndSetEditStatus();
    }
    private void checkAndSetEditStatus() {
        if ( isConfigurationChanged() ) {
            alertLayout.setEditStatus(true);
        }
    }
    private boolean isConfigurationChanged() {
        return true;
    }
}
