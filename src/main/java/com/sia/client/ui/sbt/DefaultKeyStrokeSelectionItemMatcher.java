package com.sia.client.ui.sbt;

import com.sia.client.model.SelectionItem;

public class DefaultKeyStrokeSelectionItemMatcher implements KeyStrokeSelectionItemMatcher{

    @Override
    public boolean match(SelectionItem selectionItem, char keyChar) {

        String s = String.valueOf(keyChar).toUpperCase();
        String display = selectionItem.getDisplay();
        if ( null != display) {
            return display.toUpperCase().startsWith(s);
        } else {
            return false;
        }
    }
}
