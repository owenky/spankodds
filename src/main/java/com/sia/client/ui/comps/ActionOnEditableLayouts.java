package com.sia.client.ui.comps;

import com.sia.client.config.Utils;

import java.io.IOException;
import java.util.List;

public interface ActionOnEditableLayouts {

    List<EditablePane> getEditablelayout();
    default boolean validate() {
        boolean status = true;
        for(EditablePane editablePane : getEditablelayout()) {
            if (!editablePane.validate() ) {
                status = false;
                break;
            }
        }
        return status;
    }
    default boolean persist()  {
        boolean status = validate();
        if ( status) {
            for(EditablePane editablePane : getEditablelayout()) {
                try {
                    editablePane.persist();
                } catch (IOException e) {
                    Utils.log(e);
                }
            }
        }
        return status;
    }
    default void updateLayout(){
        for(EditablePane editablePane : getEditablelayout()) {
            editablePane.updateLayout();
        }
    }
    default void close(){
        for(EditablePane editablePane : getEditablelayout()) {
            editablePane.close();
        }
    }
    default boolean isEdited() {
        return getEditablelayout().stream().map(EditablePane::isEdited).filter(edited->edited).findAny().orElse(false);
    }
}
