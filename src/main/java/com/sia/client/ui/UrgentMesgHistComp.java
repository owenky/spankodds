package com.sia.client.ui;

import com.sia.client.model.AlertStruct;

import javax.swing.JComponent;

public class UrgentMesgHistComp implements MutableItemContainer<AlertStruct> {

    private final UrgentMesgHistBox histBox;
    public UrgentMesgHistComp() {
        histBox = new UrgentMesgHistBox();
    }
    @Override
    public void addItem(final AlertStruct item) {
        insertItemAt(item,1);
    }

    @Override
    public void insertItemAt(final AlertStruct item, final int index) {
        histBox.insertItemAt(item.convert(),index);
    }
    public JComponent getComponent() {
        return histBox;
    }

}
