package com.sia.client.model;

public class NumberSelectionItem extends SelectionItem<Number>{
    public NumberSelectionItem(Number keyValue) {
        super(keyValue);
        withDisplay(String.valueOf(keyValue));
    }
}
