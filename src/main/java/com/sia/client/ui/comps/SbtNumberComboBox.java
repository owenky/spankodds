package com.sia.client.ui.comps;

import com.sia.client.config.Utils;
import com.sia.client.model.NumberSelectionItem;
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
}
