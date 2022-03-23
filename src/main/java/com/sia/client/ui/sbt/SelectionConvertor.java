package com.sia.client.ui.sbt;

public interface SelectionConvertor<K,T> {

    T convertToSelectionItem(K keyValue, String display);
    K convertStringToKey(String keyString);
    K convertIntToKey(Integer keyValue);
    default T makeNewSelectionItem(int key,String display) {
        return convertToSelectionItem(convertIntToKey(key),display);
    }
    default T makeNewSelectionItem(String key,String display) {
        return convertToSelectionItem(convertStringToKey(key),display);
    }
}
