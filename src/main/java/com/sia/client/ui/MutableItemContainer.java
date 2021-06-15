package com.sia.client.ui;

public interface MutableItemContainer<E> {

    void addItem(E item);
    void insertItemAt(E item, int index);
}
