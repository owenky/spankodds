package com.sia.client.ui.comps;

import java.util.List;

public interface OnEditableLayout {

    List<EditableLayout> getEditablelayout();
    default boolean validate() {
        boolean status = true;
//        for(EditableLayout editableLayout: getEditablelayout()) {
//            UICompValueBinder uiCompValueBinder = editableLayout.getJComponentBinder();
//            using uiCompValueBinder to find all ActionableOnChanged, and check each ActionableOnChanged.checkError
//            String err = editableLayout.checkError();
//        }
        return status;
    }
}
