package com.sia.client.ui.comps;

import javax.swing.*;

public class SbtCheckBox extends JCheckBox implements ActionableOnChanged{

    public SbtCheckBox() {

    }
    public SbtCheckBox(String text) {
        super(text);
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        this.addItemListener(l);
    }

    @Override
    public void setValue(Object obj) {
        Boolean value;
        if ( obj instanceof Boolean) {
            value = (Boolean)obj;
        } else {
            value = Boolean.parseBoolean(String.valueOf(obj));
        }
        setSelected(value);
    }

    @Override
    public Object getValue() {
        return isSelected();
    }
}
