package com.sia.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Iterator;

public interface ToggerButtonListener extends ActionListener {

    void action(JToggleButton sourceButton);
    @Override
    default void actionPerformed(ActionEvent e) {
        JToggleButton button = (JToggleButton)e.getSource();
        toggleMuteButsForAllWindows(button);
    }
    default void toggleMuteButsForAllWindows(JToggleButton sourceButton) {
        Iterator<SpankyWindow> ite = SpankyWindow.getAllSpankyWindows();
        while ( ite.hasNext()) {
            SpankyWindow window = ite.next();
            TopView tv= window.getTopView();
            JToggleButton buttonInWin = Arrays.stream(tv.getComponents()).filter(comp->comp instanceof JToggleButton)
                    .map(comp->(JToggleButton)comp).filter(btn->btn.getName().equals(sourceButton.getName()))
                    .findAny().get();
            buttonInWin.toggle();
        }
       action(sourceButton);
    }
}
