package com.sia.client.ui.lineseeker;

import com.sia.client.model.SelectionItem;
import com.sia.client.ui.sbt.SelectionConvertor;

public class LinkSeekerAlertSelectionConvertor implements SelectionConvertor<String, LineSeekerAlertSelectionItem> {
    @Override
    public LineSeekerAlertSelectionItem convertToSelectionItem(String keyValue, String display) {
        AlertAttributes alertAttributes = AlertAttrManager.of(keyValue);
        return (LineSeekerAlertSelectionItem) new LineSeekerAlertSelectionItem(alertAttributes).withDisplay(display);
    }

    @Override
    public String convertStringToKey(String keyString) {
        return keyString;
    }

    @Override
    public String convertIntToKey(Integer keyValue) {
        if (SelectionItem.isObjectSpecialItemKey(keyValue)) {
            return AlertAttrManager.makeKey(String.valueOf(keyValue), AlertPeriod.Full);
        }
        throw new IllegalArgumentException("Does not support Integer key");
    }
}
