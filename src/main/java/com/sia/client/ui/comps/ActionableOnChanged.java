package com.sia.client.ui.comps;

public interface ActionableOnChanged {

    void addListener(CompValueChangedListener l);
    void rmListener(CompValueChangedListener l);
    void setValue(Object obj);
    Object getValue();
    default String checkError() {
        return null;
    }
}
