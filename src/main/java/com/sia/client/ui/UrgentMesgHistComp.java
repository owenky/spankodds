package com.sia.client.ui;

import com.sia.client.model.AlertStruct;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import java.awt.Component;

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
    @Override
    public JComponent getComponent() {
        return histBox;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class JComboBoxWrapper implements MutableItemContainer<AlertStruct> {

        private final JComboBox<AlertStruct> delegator;
        public JComboBoxWrapper() {
            delegator = new JComboBox<>();
        }

        @Override
        public void addItem(final AlertStruct item) {
            delegator.addItem(item);
        }

        @Override
        public void insertItemAt(final AlertStruct item, final int index) {
            delegator.insertItemAt(item,index);
        }

        @Override
        public Component getComponent() {
            return delegator;
        }
    }
}
