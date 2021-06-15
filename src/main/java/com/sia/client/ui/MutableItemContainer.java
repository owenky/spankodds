package com.sia.client.ui;

import java.awt.Component;

public interface MutableItemContainer<E> {

    void addItem(E item);
    void insertItemAt(E item, int index);
    Component getComponent();
}
