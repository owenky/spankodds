package com.sia.client.ui.comps;

public class SimpleValueWraper<T> {

    public SimpleValueWraper(){

    }
    public SimpleValueWraper(T initValue){
        value = initValue;
    }
    private T value;
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }
}
