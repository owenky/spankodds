package com.sia.client.ui.comps;

import javax.swing.*;

public class SbtTextField extends JTextField implements ActionableOnChanged{

    public SbtTextField() {

    }
    public SbtTextField(String text) {
        super(text);
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        this.getDocument().addDocumentListener(l);
    }
    @Override
    public void rmListener(CompValueChangedListener l) {
        this.getDocument().removeDocumentListener(l);
    }
    @Override
    public void setValue(Object obj) {
        setText(String.valueOf(obj));
    }

    @Override
    public String getValue() {
        return getText();
    }
}
