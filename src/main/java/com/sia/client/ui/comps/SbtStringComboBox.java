package com.sia.client.ui.comps;

import com.sia.client.model.StringSelectionItem;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SelectionConvertor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SbtStringComboBox extends SBTComboBox<String, StringSelectionItem> {
    public SbtStringComboBox(String [] arr) {
        this();
        this.addElement(
                Arrays.stream(arr).map(StringSelectionItem::new).collect(Collectors.toList()));
    }
    public SbtStringComboBox( ) {
        super(new SelectionConvertor<String, StringSelectionItem>() {

            @Override
            public StringSelectionItem convertToSelectionItem(String keyValue, String display) {
                return (StringSelectionItem)new StringSelectionItem(keyValue).withDisplay(display);
            }

            @Override
            public String convertStringToKey(String keyString) {
                return keyString;
            }

            @Override
            public String convertIntToKey(Integer keyValue) {
                return String.valueOf(keyValue);
            }
        });
    }
    public void addItem(String str) {
        addItem(new StringSelectionItem(str));
    }
    @Override
    public String getValue() {
        return ((StringSelectionItem)super.getValue()).getKeyValue();
    }
    @Override
    public void setValue(Object obj) {
        if ( obj instanceof StringSelectionItem) {
            setSelectedItem(obj);
        } else {
            StringSelectionItem stringSelectionItem = new StringSelectionItem(String.valueOf(obj));
            setSelectedItem(stringSelectionItem);
        }
    }
}
