package com.sia.client.model;

public class StringSelectionItem extends SelectionItem<String>{
    public StringSelectionItem(String keyValue) {
        super(keyValue);
        withDisplay(keyValue);
    }
}
