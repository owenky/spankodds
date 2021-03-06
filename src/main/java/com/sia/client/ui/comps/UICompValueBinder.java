package com.sia.client.ui.comps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sia.client.config.OmFactory;

import javax.swing.event.DocumentEvent;
import javax.swing.event.TreeSelectionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class UICompValueBinder {

    private Object persistenceObject;
    private static final Map<Integer,UICompValueBinder> identityHashCode2BinderMap = new HashMap<>();
    private final Map<String,ActionableOnChanged> name2UiCompMap = new HashMap<>();
    private final CompValueChangedListener compValueChangedListener;
    private final Function<Object,Void> onValueChangedEvent;

    public static synchronized UICompValueBinder register(Object IdentityObject, Function<Object,Void> onValueChangedEvent) {
        int identityHashCode = System.identityHashCode(IdentityObject);
        return identityHashCode2BinderMap.computeIfAbsent(identityHashCode,(key)->new UICompValueBinder(onValueChangedEvent));
    }
//    public static UICompValueBinder getBinder(Object containingObj) {
//        int identityHashCode = System.identityHashCode(containingObj);
//        return identityHashCode2BinderMap.get(identityHashCode);
//    }
    public static UICompValueBinder unregister(Object containingObj) {
        int identityHashCode = System.identityHashCode(containingObj);
        return identityHashCode2BinderMap.remove(identityHashCode);
    }
    private UICompValueBinder(Function<Object,Void> onValueChangedEvent) {
        this.onValueChangedEvent = onValueChangedEvent;
        this.compValueChangedListener = makeDefaultJCompValueChangedListener();
    }
    public UICompValueBinder withPersistenceObject(Object persistenceObject) {
        this.persistenceObject = persistenceObject;
        return this;
    }
    public UICompValueBinder bindCompProp(String propName, ActionableOnChanged uiComponent) {
        if ( null != compValueChangedListener) {
            if ( name2UiCompMap.containsKey(propName)) {
                ActionableOnChanged oldComp = name2UiCompMap.get(propName);
                oldComp.rmListener(compValueChangedListener);
            }
            uiComponent.addListener(compValueChangedListener);
            name2UiCompMap.put(propName, uiComponent);
        }
        return this;
    }
    public void persistJComponentValues() throws IOException {
        ObjectMapper objectMapper = OmFactory.getObjectMapper();
        Map<String,Object> valueMap = new HashMap<>();
        for(String name: name2UiCompMap.keySet()) {
            ActionableOnChanged comp = name2UiCompMap.get(name);
            Object value = comp.getValue();
            valueMap.put(name,value);
        }
        ObjectReader objectReader = objectMapper.readerForUpdating(persistenceObject);
        String json = objectMapper.writeValueAsString(valueMap);
        objectReader.readValue(json, persistenceObject.getClass());
    }
    public void updateJComponentValues()  {
        ObjectMapper objectMapper = OmFactory.getObjectMapper();
        Map<?,?> valuesMap = objectMapper.convertValue(persistenceObject,Map.class);
        for(String name: name2UiCompMap.keySet()) {
            Object value = valuesMap.get(name);
            ActionableOnChanged comp = name2UiCompMap.get(name);
            comp.setValue(value);
        }
    }
    public boolean areValuesChanged() {
        boolean isChanged = false;
        ObjectMapper objectMapper = OmFactory.getObjectMapper();
        Map<?,?> existingValueMap = objectMapper.convertValue(persistenceObject,Map.class);
        for(String name: name2UiCompMap.keySet()) {
            ActionableOnChanged comp = name2UiCompMap.get(name);
            String componentValue = String.valueOf(comp.getValue());
            String existingValue = String.valueOf(existingValueMap.get(name));
            if ( ! Objects.equals(componentValue,existingValue) ) {
                isChanged = true;
                break;
            }
        }

        return isChanged;
    }
    public java.util.List<ActionableOnChanged> getBindedUiComponents() {
        return Arrays.asList(name2UiCompMap.values().toArray(new ActionableOnChanged[0]));
    }
    private CompValueChangedListener makeDefaultJCompValueChangedListener() {
        return new CompValueChangedListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                processEvent(e);
            }
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
                if ( null != onValueChangedEvent) {
                    onValueChangedEvent.apply(event);
                }
            }
        };
    }
}
