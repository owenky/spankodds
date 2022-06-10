package com.sia.client.ui.comps;

import com.sia.client.config.Utils;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.concurrent.Callable;

public class EmbededFrame extends JInternalFrame {

    private Callable<Boolean> closeValidor;
    public EmbededFrame() {
        init();
    }
    public EmbededFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title,resizable, closable, maximizable,iconifiable);
        init();
    }
    public void setCloseValidor(Callable<Boolean> closeValidor) {
        this.closeValidor = closeValidor;
    }
    private void init() {
        addInternalFrameListener(new InternalFrameAdapter() {
//            @Override
//            public void internalFrameIconified(InternalFrameEvent e) {
//
//            }
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {

                    boolean toClose;
                    if ( null != closeValidor) {
                        try {
                            toClose = closeValidor.call();
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
