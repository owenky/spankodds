package com.sia.client.ui.comps;

import com.sia.client.config.Utils;
import com.sia.client.model.NumberSelectionItem;
import com.sia.client.model.StringSelectionItem;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SelectionConvertor;

import java.text.NumberFormat;
import java.text.ParseException;

public class SbtNumberComboBox extends SBTComboBox<Number, NumberSelectionItem> {

    public SbtNumberComboBox() {
        super(new SelectionConvertor<Number, NumberSelectionItem>() {

            @Override
            public NumberSelectionItem convertToSelectionItem(Number keyValue, String display) {
                return (NumberSelectionItem)new NumberSelectionItem(keyValue).withDisplay(display);
            }

            @Override
            public Number convertStringToKey(String keyString) {
                try {
                    return NumberFormat.getInstance().parse(keyString);
                } catch (ParseException e) {
                    Utils.log(e);
                    return null;
                }
            }

            @Override
            public Number convertIntToKey(Integer keyValue) {
                return convertStringToKey(String.valueOf(keyValue));
            }
        });
    }
    @Override
    public Number getValue() {
        Object selectedvalue = super.getValue();
        if ( null != selectedvalue ) {
            selectedvalue = ((NumberSelectionItem) selectedvalue).getKeyValue();
        }
        return (Number)selectedvalue;
    }
    @Override
    public void setValue(Object obj) {
        if ( obj instanceof NumberSelectionItem) {
            setSelectedItem(obj);
        } else {
            Number number = getSelectionConvertor().convertStringToKey(String.valueOf(obj));
            NumberSelectionItem numberSelectionItem = new NumberSelectionItem(number);
            setSelectedItem(numberSelectionItem);
        }
    }
}
