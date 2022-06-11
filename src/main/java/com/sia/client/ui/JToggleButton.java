package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.ui.comps.ActionableOnChanged;
import com.sia.client.ui.comps.CompValueChangedListener;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class JToggleButton extends JButton implements ActionableOnChanged {

    private static final Map<String,Boolean> initialEnableStatus = new HashMap<>();
    private boolean enabled;
    private final String enabledIcon;
    private final String enabledTip;
    private final String disabledIcon;
    private final String disabledTip;
    private final Map<CompValueChangedListener, ActionListener> listenerWrapperMap = new HashMap<>();

    public JToggleButton(String name,String enabledIcon, String enabledTip, String disabledIcon, String disabledTip) {
        this.enabledIcon = enabledIcon;
        this.enabledTip = enabledTip;
        this.disabledIcon = disabledIcon;
        this.disabledTip = disabledTip;
        setName(name);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
        //set initial status to the one that have already been set by same button in previous SpankyWindow 05/01/2022
        Boolean initialStatus = initialEnableStatus.computeIfAbsent(name,key->true);
        setEnable(initialStatus);
    }
    public void toggle() {
        setEnable( !enabled);
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnable(boolean enable) {
        if (enable) {
            setIcon(Utils.getImageIcon(enabledIcon));
            setToolTipText(enabledTip);

        } else {
            setIcon(Utils.getImageIcon(disabledIcon));
            setToolTipText(disabledTip);
        }
        enabled = enable;
        initialEnableStatus.put(getName(),enabled);
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        ActionListener listenerWrapper = e -> {
            JToggleButton.this.toggle();
            l.actionPerformed(e);
        };
        listenerWrapperMap.put(l,listenerWrapper);
        this.addActionListener(listenerWrapper);
    }
    @Override
    public void rmListener(CompValueChangedListener l) {
        ActionListener wrapper = listenerWrapperMap.remove(l);
        if ( null != wrapper) {
            this.removeActionListener(wrapper);
        } else {
            this.removeActionListener(l);
        }
    }
    @Override
    public void setValue(Object obj) {
        Boolean value;
        if ( obj instanceof Boolean) {
            value = (Boolean)obj;
        } else {
            value = Boolean.parseBoolean(String.valueOf(obj));
        }
        setEnable(value);
    }

    @Override
    public Object getValue() {
//        return isSelected();
        return isEnabled();
    }
}
