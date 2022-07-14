package com.sia.client.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static com.sia.client.config.Utils.log;

public enum ShortCut {

    Opener(KeyEvent.VK_O),
    DefaultView(KeyEvent.VK_1),
    FirstHalf(KeyEvent.VK_2),
    SidesOnly(KeyEvent.VK_3),
    TotalsOnly(KeyEvent.VK_4),
    Sort(KeyEvent.VK_5),    //new
    AddBookie(KeyEvent.VK_6),    //new
    ShrinkTeam(KeyEvent.VK_7),    //new
    Alert(KeyEvent.VK_8),    //new
    Clear(KeyEvent.VK_C),
    FullGame(KeyEvent.VK_G),
    Last(KeyEvent.VK_Q),
    ClearAll(KeyEvent.VK_W)
    ;

    ShortCut(int vk_key) {
        this.vk_key = vk_key;
    }
    public static Action registerShortCutAction(JComponent jcomp, ShortCut shortCut, ActionListener al) {

        Action action = new AbstractAction(shortCut.name()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                al.actionPerformed(e);
            }
        };
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(shortCut.vk_key, 0));

//        jcomp.getActionMap().put(shortCut.name(), action);
//        jcomp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
//                (KeyStroke) action.getValue(Action.ACCELERATOR_KEY), shortCut.name());

        return action;
    }
    private final int vk_key;
}
