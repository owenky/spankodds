package com.sia.client.ui;

import com.sia.client.ui.control.SportsTabPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public static Action registerShortCutAction(SportsTabPane jcomp, ShortCut shortCut, ActionListener al) {

        sportTabPaneSet.add(jcomp);
        Action action = new AbstractAction(shortCut.name()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                al.actionPerformed(e);
            }
        };
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(shortCut.vk_key, 0));

        jcomp.getActionMap().put(shortCut.name(), action);
        jcomp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                (KeyStroke) action.getValue(Action.ACCELERATOR_KEY), shortCut.name());

        return action;
    }
    public static void removeSportTabPane(SportsTabPane stp) {
        sportTabPaneSet.remove(stp);
        inputMapPerStp.remove(stp);
    }
    public static void disableShortCut() {
        for(SportsTabPane stp: sportTabPaneSet) {
            InputMap inputMap = stp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            InputMap backupInputMap = new InputMap();
            clone(inputMap,backupInputMap);
            inputMapPerStp.put(stp,backupInputMap);
            inputMap.clear();
        }
    }
    public static void restoreShortCut() {
        for(SportsTabPane stp: sportTabPaneSet) {
            InputMap inputMap = stp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            if ( null != inputMap) {
                InputMap backupInputMap = inputMapPerStp.get(stp);
                clone(backupInputMap, inputMap);
            }
        }
    }
    private static void clone(InputMap sourceInputMap,InputMap targetInputMap) {
        if ( null != sourceInputMap) {
            for (KeyStroke key : sourceInputMap.keys()) {
                targetInputMap.put(key, sourceInputMap.get(key));
            }
        }
    }
    private final int vk_key;
    private static final Set<SportsTabPane> sportTabPaneSet = new HashSet<>();
    private static final Map<SportsTabPane,InputMap> inputMapPerStp = new HashMap<>();
}
