package com.sia.client.ui.comps;

import com.sia.client.config.SiaConst;
import com.sia.client.config.Utils;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public interface ValueBindedComponents {

    JLabel getEditStatusLabel();
    UICompValueBinder getJComponentBinder();

    default boolean isEdited() {
        return SiaConst.EditedIndicator.equals(getEditStatusLabel().getText());
    }
    default void setEdited(boolean edited) {
        if ( edited) {
            getEditStatusLabel().setText(SiaConst.EditedIndicator);
        } else {
            getEditStatusLabel().setText("  ");
        }
    }
    default boolean validate() {
        boolean status = true;
        UICompValueBinder uiCompValueBinder = getJComponentBinder();
        List<ActionableOnChanged> bindedUiComp = uiCompValueBinder.getBindedUiComponents();
        String err = bindedUiComp.stream().map(ActionableOnChanged::checkError).filter(Objects::nonNull).findAny().orElse(null);
        if ( null != err) {
            Utils.showErrorMessageDialog(getEditStatusLabel(),err);
            status = false;
        }

        return status;
    }
    default void persist() throws IOException {
        UICompValueBinder binder = getJComponentBinder();
        binder.persistJComponentValues();
        setEdited(false);
    }
    default void updateLayout() {
        UICompValueBinder binder = getJComponentBinder();
        binder.updateJComponentValues();
        setEdited(false);
    }
    default void checkAndSetEditStatus() {
        UICompValueBinder binder = getJComponentBinder();
        boolean isEdited = binder.areValuesChanged();
        setEdited(isEdited);
    }
    default Function<Object,Void> getOnValueChangedEventFunction() {
        return (event)-> {checkAndSetEditStatus(); return null;};
    }
    default void clear() {
        UICompValueBinder.unregister(this);
    }
}
