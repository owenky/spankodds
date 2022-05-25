package com.sia.client.ui.comps;

import com.sia.client.config.SiaConst;

import javax.swing.*;
import java.io.IOException;
import java.util.function.Function;

public interface EditableLayout {

    JComponent getLayoutPane();
    JLabel getEditStatusLabel();
    JComponentBinder getJComponentBinder();

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
    default void persist() throws IOException {
        JComponentBinder binder = getJComponentBinder();
        binder.persistJComponentValues();
        setEdited(false);
    }
    default void updateLayout() {
        JComponentBinder binder = getJComponentBinder();
        binder.updateJComponentValues();
        setEdited(false);
    }
    default void checkAndSetEditStatus() {
        JComponentBinder binder = getJComponentBinder();
        boolean isEdited = binder.areValuesChanged();
        setEdited(isEdited);
    }
    default Function<Object,Void> getOnValueChangedEventFunction() {
        return (event)-> {checkAndSetEditStatus(); return null;};
    }
}
