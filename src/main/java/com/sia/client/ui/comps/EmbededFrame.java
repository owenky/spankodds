package com.sia.client.ui.comps;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class EmbededFrame extends JInternalFrame {

    public EmbededFrame() {
//        init();
    }
    public EmbededFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title,resizable, closable, maximizable,iconifiable);
//        init();
    }
//    private void init() {
//        addInternalFrameListener(new InternalFrameAdapter() {
//            @Override
//            public void internalFrameIconified(InternalFrameEvent e) {
//
//            }
//        });
//    }
}
