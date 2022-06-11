package com.sia.client.ui.comps;

import com.sia.client.config.Utils;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyVetoException;
import java.util.concurrent.Callable;

public class EmbededFrame extends JInternalFrame {

    private Callable<Boolean> closeAction;
    public EmbededFrame() {
        init();
    }
    public EmbededFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title,resizable, closable, maximizable,iconifiable);
        init();
    }
    public void setCloseAction(Callable<Boolean> closeAction) {
        this.closeAction = closeAction;
    }
    private void init() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameAdapter() {
//            @Override
//            public void internalFrameIconified(InternalFrameEvent e) {
//
//            }
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {

                    boolean toClose;
                    if ( null != closeAction) {
                        try {
                            toClose = closeAction.call();
                        } catch (Exception ex) {
                            Utils.log(ex);
                            toClose = true;
                        }
                    } else {
                        toClose = true;
                    }

                    if (toClose) {
                        dispose();
                    }
                }

        });
    }
}
