package com.sia.client.ui.comps;

import com.sia.client.ui.JToggleButton;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SbtToggleButton extends JToggleButton implements ActionableOnChanged{

    private final Map<CompValueChangedListener, ActionListener> listenerWrapperMap = new HashMap<>();
    public SbtToggleButton(String name,String enabledIcon, String enabledTip, String disabledIcon, String disabledTip) {
        super(name,enabledIcon,enabledTip,disabledIcon, disabledTip);
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        ActionListener listenerWrapper = e -> {
            SbtToggleButton.this.toggle();
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
//        setSelected(value);
        setEnable(value);
    }

    @Override
    public Object getValue() {
//        return isSelected();
        return isEnabled();
    }
}
