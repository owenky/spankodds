package com.sia.client.ui.comps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sia.client.config.Utils;
import com.sia.client.ui.lineseeker.LineSeekerAlertMethodAttr;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class JComponentBinder {

    private final Object data;
    private final Map<String,JComponent> name2JCompMap = new HashMap<>();
    private final JCompValueChangedListener jCompValueChangedListener;
    private final Function<Object,Void> onValueChangedEvent;

    public JComponentBinder(Object data,Function<Object,Void> onValueChangedEvent) {
        this.data = data;
        this.onValueChangedEvent = onValueChangedEvent;
        this.jCompValueChangedListener = makeDefaultJCompValueChangedListener();
    }
    public JComponentBinder bind(String dataFieldName,JComponent jComponent) {
        name2JCompMap.put(dataFieldName,jComponent);
        if ( null != jCompValueChangedListener) {
            if (jComponent instanceof JToggleButton) {
                ((JToggleButton) jComponent).addItemListener(jCompValueChangedListener);
            } else if (jComponent instanceof JComboBox) {
                ((JComboBox) jComponent).addItemListener(jCompValueChangedListener);
            } else if (jComponent instanceof JTextComponent) {
                ((JTextComponent) jComponent).getDocument().addDocumentListener(jCompValueChangedListener);
            } else if (jComponent instanceof PopupLocationConfig) {
                ((PopupLocationConfig) jComponent).addItemListener(jCompValueChangedListener);
            } else {
                throw new IllegalStateException("Unsupported jcomponent : " + jComponent.getClass().getName());
            }
        }
        return this;
    }
    public void persistJComponentValues() throws IOException {
        ObjectMapper objectMapper = Utils.getObjectMapper();
        Map<String,Object> valueMap = new HashMap<>();
        for(String name: name2JCompMap.keySet()) {
            Object value;
            JComponent jcomp = name2JCompMap.get(name);
            if ( jcomp instanceof JToggleButton) {
                value = ((JToggleButton)jcomp).isSelected();
            } else if ( jcomp  instanceof JComboBox) {
                value = ((JComboBox)jcomp).getSelectedItem();
            } else if ( jcomp instanceof JTextComponent) {
                value = ((JTextComponent)jcomp).getText();
            } else if ( jcomp instanceof PopupLocationConfig) {
                value = ((PopupLocationConfig)jcomp).getSelectedPopupLocation();
            } else {
                throw new IllegalStateException("Unsupported jcomponent : "+jcomp.getClass().getName());
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
        for(String name: name2JCompMap.keySet()) {
            Object value = valuesMap.get(name);
            JComponent jcomp = name2JCompMap.get(name);
            if ( jcomp instanceof JToggleButton) {
                ((JToggleButton)jcomp).setSelected(Boolean.parseBoolean(String.valueOf(value)));
            } else if ( jcomp  instanceof JComboBox) {
               ((JComboBox<?>)jcomp).setSelectedItem(value);
            } else if ( jcomp instanceof JTextComponent) {
                ((JTextComponent)jcomp).setText(String.valueOf(value));
            } else if ( jcomp instanceof PopupLocationConfig) {
                String valStr = String.valueOf(value);
                ((PopupLocationConfig)jcomp).setSelectedPopupLocation(LineSeekerAlertMethodAttr.PopupLocation.valueOf(valStr));
            }  else {
                throw new IllegalStateException("Unsupported jcomponent : "+jcomp.getClass().getName());
            }
        }
    }
    public boolean areValuesChanged() {
        boolean isChanged = false;
        ObjectMapper objectMapper = Utils.getObjectMapper();
        Map<?,?> existingValueMap = objectMapper.convertValue(data,Map.class);
        for(String name: name2JCompMap.keySet()) {
            Object componentValue;
            JComponent jcomp = name2JCompMap.get(name);
            if ( jcomp instanceof JToggleButton) {
                componentValue = ((JToggleButton)jcomp).isSelected();
            } else if ( jcomp  instanceof JComboBox) {
                componentValue = ((JComboBox)jcomp).getSelectedItem();
            } else if ( jcomp instanceof JTextComponent) {
                componentValue = ((JTextComponent)jcomp).getText();
            } else if ( jcomp instanceof PopupLocationConfig) {
                componentValue = ((PopupLocationConfig)jcomp).getSelectedPopupLocation();
            } else {
                throw new IllegalStateException("Unsupported jcomponent : "+jcomp.getClass().getName());
            }
            Object existingValue = existingValueMap.get(name);

            if ( ! Objects.equals(componentValue,existingValue) ) {
                isChanged = true;
                break;
            }
        }

        return isChanged;
    }
    private JCompValueChangedListener makeDefaultJCompValueChangedListener() {
        return new JCompValueChangedListener() {

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
