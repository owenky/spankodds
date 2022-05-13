package com.sia.client.ui.sbt;

import com.sia.client.model.SelectionItem;

public interface KeyStrokeSelectionItemMatcher {

    boolean match(SelectionItem selectionItem, char keyChar);
}
