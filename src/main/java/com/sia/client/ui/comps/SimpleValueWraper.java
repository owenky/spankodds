package com.sia.client.ui.comps;

public class SimpleValueWraper<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
