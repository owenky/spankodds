package com.sia.client.ui.comps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sia.client.config.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class UICompValueBinder {

    private final Object data;
    private final Map<String,Object> name2UiCompMap = new HashMap<>();
    private final CompValueChangedListener compValueChangedListener;
    private final Function<Object,Void> onValueChangedEvent;

    public UICompValueBinder(Object data, Function<Object,Void> onValueChangedEvent) {
        this.data = data;
        this.onValueChangedEvent = onValueChangedEvent;
        this.compValueChangedListener = makeDefaultJCompValueChangedListener();
    }
    public UICompValueBinder bind(String dataFieldName, Object uiComponent) {
        if ( null != compValueChangedListener) {
            if (uiComponent instanceof ItemSelectable) {
                ((ItemSelectable) uiComponent).addItemListener(compValueChangedListener);
            }  else if (uiComponent instanceof JTextComponent) {
                ((JTextComponent) uiComponent).getDocument().addDocumentListener(compValueChangedListener);
            } else if (uiComponent instanceof ActionableOnChanged) {
                ((ActionableOnChanged) uiComponent).addListener(compValueChangedListener);
            }  else {
                throw new IllegalStateException("Unsupported jcomponent : " + uiComponent.getClass().getName());
            }
            name2UiCompMap.put(dataFieldName,uiComponent);
        }
        return this;
    }
    public void persistJComponentValues() throws IOException {
        ObjectMapper objectMapper = Utils.getObjectMapper();
        Map<String,Object> valueMap = new HashMap<>();
        for(String name: name2UiCompMap.keySet()) {
            Object value;
            Object comp = name2UiCompMap.get(name);
            if ( comp instanceof JToggleButton) {
                value = ((JToggleButton)comp).isSelected();
            } else if ( comp  instanceof JComboBox) {
                value = ((JComboBox)comp).getSelectedItem();
            } else if ( comp instanceof JTextComponent) {
                value = ((JTextComponent)comp).getText();
            } else if ( comp instanceof ActionableOnChanged) {
                value = ((ActionableOnChanged)comp).getValue();
            } else {
                throw new IllegalStateException("Unsupported jcomponent : "+comp.getClass().getName());
            }

            valueMap.put(name,value);
        }
        ObjectReader objectReader = objectMapper.readerForUpdating(data);
        String json = objectMapper.writeValueAsString(valueMap);
        objectReader.readValue(json,data.getClass());
    }
    public void updateJComponentValues()  {
        ObjectMapper objectMapper = Utils.getObjectMapper();
        Map<?,?> valuesMap = objectMapper.convertValue(data,Map.class);
        for(String name: name2UiCompMap.keySet()) {
            Object value = valuesMap.get(name);
            Object comp = name2UiCompMap.get(name);
            if ( comp instanceof JToggleButton) {
                ((JToggleButton)comp).setSelected(Boolean.parseBoolean(String.valueOf(value)));
            } else if ( comp  instanceof JComboBox) {
               ((JComboBox<?>)comp).setSelectedItem(value);
            } else if ( comp instanceof JTextComponent) {
                ((JTextComponent)comp).setText(String.valueOf(value));
            } else if ( comp instanceof ActionableOnChanged) {
                String valStr = String.valueOf(value);
                ((ActionableOnChanged)comp).setValue(valStr);
            }  else {
                throw new IllegalStateException("Unsupported jcomponent : "+comp.getClass().getName());
            }
        }
    }
    public boolean areValuesChanged() {
        boolean isChanged = false;
        ObjectMapper objectMapper = Utils.getObjectMapper();
        Map<?,?> existingValueMap = objectMapper.convertValue(data,Map.class);
        for(String name: name2UiCompMap.keySet()) {
            Object componentValue;
            Object comp = name2UiCompMap.get(name);
            if ( comp instanceof JToggleButton) {
                componentValue = ((JToggleButton)comp).isSelected();
            } else if ( comp  instanceof JComboBox) {
                componentValue = ((JComboBox)comp).getSelectedItem();
            } else if ( comp instanceof JTextComponent) {
                componentValue = ((JTextComponent)comp).getText();
            } else if ( comp instanceof ActionableOnChanged) {
                componentValue = ((ActionableOnChanged)comp).getValue();
            } else {
                throw new IllegalStateException("Unsupported component : "+comp.getClass().getName());
            }
            Object existingValue = existingValueMap.get(name);

            if ( ! Objects.equals(componentValue,existingValue) ) {
                isChanged = true;
                break;
            }
        }

        return isChanged;
    }
    private CompValueChangedListener makeDefaultJCompValueChangedListener() {
        return new CompValueChangedListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                processEvent(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                processEvent(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                processEvent(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processEvent(e);
            }

            @Override
            public void itemStateChanged(ItemEvent e) {
                processEvent(e);
            }
            private void processEvent(Object event) {
                if ( null == onValueChangedEvent) {
                    onValueChangedEvent.apply(event);
                }
            }
        };
    }
}
