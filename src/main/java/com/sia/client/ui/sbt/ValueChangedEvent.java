package com.sia.client.ui.sbt;

import com.sia.client.config.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;


public class ValueChangedEvent extends AWTEvent {

    public ValueChangedEvent(Object source_) {
        super(source_, ActionEvent.ACTION_PERFORMED);
    }

    public String getOldValueString() {
        return oldvalueString;
    }

    public void setOldValueString(String oldvalueString_) {
        this.oldvalueString = oldvalueString_;
    }


    public String getNewValueString() {
        return newvalueString;
    }

    public void setNewValueString(String newvalueString_) {
        this.newvalueString = newvalueString_;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object v) {
        value = v;
    }

    public boolean isValueChanged() {
        if (newvalueString == null) {
            if (oldvalueString == null) {
                return false;
            } else {
                return true;
            }
        }


        return !(newvalueString.equalsIgnoreCase(oldvalueString));

    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        if (!Utils.isBlank(property_name)) {
//            property_name = property_name.replaceAll("[\\s:]", "");
        }
        this.property_name = property_name;
    }

    public void copyValues(ValueChangedEvent sourceEvent) {
        Object s_ = sourceEvent.getSource();
        String property_name_;
        if (s_ instanceof Component)
            property_name_ = ((Component) s_).getName();
        else
            property_name_ = sourceEvent.getProperty_name(); //s_.toString();

        this.setNewValueString(sourceEvent.newvalueString);
        this.setOldValueString(sourceEvent.oldvalueString);
        this.setValue(sourceEvent.value);
        this.setSourceTableCellEditor(sourceEvent.isSourceTableCellEditor);
        this.setProperty_name(property_name_);
    }
    public boolean isSourceTableCellEditor() {
        return isSourceTableCellEditor;
    }

    public void setSourceTableCellEditor(boolean isSourceTableCellEditor) {
        this.isSourceTableCellEditor = isSourceTableCellEditor;
    }

    private boolean isSourceTableCellEditor = false;
    private String oldvalueString, newvalueString;
    private Object value;
    private String property_name;
    private static final long serialVersionUID = 20080315;
}
