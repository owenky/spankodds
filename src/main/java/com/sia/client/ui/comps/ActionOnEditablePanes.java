package com.sia.client.ui.comps;

import com.sia.client.config.Utils;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public interface ActionOnEditablePanes {

    List<EditablePane> getEditablePanes();
    default boolean validatePanes() {
        boolean status = true;
        for(EditablePane editablePane : getEditablePanes()) {
            if (!editablePane.validate() ) {
                status = false;
                break;
            }
        }
        return status;
    }
    default boolean save(ActionEvent ae)  {
        return persistPanes();
    }
    default boolean persistPanes()  {
        boolean status = validatePanes();
        if ( status) {
            for(EditablePane editablePane : getEditablePanes()) {
                try {
                    editablePane.persist();
                } catch (IOException e) {
                    Utils.log(e);
                }
            }
        }
        return status;
    }
    default void updatePanes(){
        for(EditablePane editablePane : getEditablePanes()) {
            editablePane.updateLayout();
        }
    }
    default void closePanes(){
        for(EditablePane editablePane : getEditablePanes()) {
            editablePane.close();
        }
    }
    default boolean isAnyPaneEdited() {
        return getEditablePanes().stream().map(EditablePane::isEdited).filter(edited->edited).findAny().orElse(false);
    }
}
