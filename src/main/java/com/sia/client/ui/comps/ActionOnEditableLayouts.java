package com.sia.client.ui.comps;

import com.sia.client.config.Utils;

import java.io.IOException;
import java.util.List;

public interface ActionOnEditableLayouts {

    List<EditableLayout> getEditablelayout();
    default boolean validate() {
        boolean status = true;
        for(EditableLayout editableLayout: getEditablelayout()) {
            if (!editableLayout.validate() ) {
                status = false;
                break;
            }
        }
        return status;
    }
    default boolean persist()  {
        boolean status = validate();
        if ( status) {
            for(EditableLayout editableLayout: getEditablelayout()) {
                try {
                    editableLayout.persist();
                } catch (IOException e) {
                    Utils.log(e);
                }
            }
        }
        return status;
    }
    default void updateLayout(){
        for(EditableLayout editableLayout: getEditablelayout()) {
            editableLayout.updateLayout();
        }
    }
    default void close(){
        for(EditableLayout editableLayout: getEditablelayout()) {
            editableLayout.close();
        }
    }
    default boolean isEdited() {
        return getEditablelayout().stream().map(EditableLayout::isEdited).filter(edited->edited).findAny().orElse(false);
    }
}
