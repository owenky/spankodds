package com.sia.client.ui.comps;

public interface ActionableOnChanged {

    void addListener(CompValueChangedListener l);
    void setValue(Object str);
    Object getValue();
}
