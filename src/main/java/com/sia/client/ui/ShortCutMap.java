package com.sia.client.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ShortCutMap {

    private static final Map<String, KeyStroke> shortCutMap = new HashMap<>();

    public static void registerShortCutAction(JComponent jcomp, String actionName, ActionListener al) {

        KeyStroke keyStroke = shortCutMap.get(actionName);
        if( null == keyStroke) {
            throw new IllegalArgumentException("short cut key stroke not defined for action "+actionName);
        }
    }
}
