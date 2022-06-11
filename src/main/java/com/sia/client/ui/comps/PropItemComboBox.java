package com.sia.client.ui.comps;

import com.sia.client.model.PropItem;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SelectionConvertor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PropItemComboBox extends SBTComboBox<String, PropItem> {
    public PropItemComboBox(PropItem [] arr) {
        this();
        this.addElement(Arrays.stream(arr).collect(Collectors.toList()));
    }
    public PropItemComboBox( ) {
        super(new SelectionConvertor<String, PropItem>() {

            @Override
            public PropItem convertToSelectionItem(String keyValue, String display) {
                return new PropItem(keyValue,display);
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
    public void addItem(String key,String display) {
        addItem(this.getSelectionConvertor().convertToSelectionItem(key,display));
    }
    @Override
    public String getValue() {
        return ((PropItem)super.getValue()).getKeyValue();
    }
    @Override
    public void setValue(Object obj) {
        if ( obj instanceof PropItem) {
            setSelectedItem(obj);
        } else if ( obj instanceof SelectionItem) {
            SelectionItem<String> si = (SelectionItem<String>)obj;
            PropItem item = new PropItem(si.getKeyValue(),si.getDisplay());
            setSelectedItem(item);
        }  else {
            PropItem item = new PropItem(String.valueOf(obj),String.valueOf(obj));
            setSelectedItem(item);
        }
    }
}
