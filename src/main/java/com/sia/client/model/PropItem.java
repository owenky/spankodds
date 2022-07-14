package com.sia.client.model;

public class PropItem extends SelectionItem<String> {
    public PropItem(String keyValue,String display) {
        super(keyValue);
        withDisplay(display);
    }
}
