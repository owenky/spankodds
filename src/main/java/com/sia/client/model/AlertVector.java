package com.sia.client.model;

import com.sia.client.config.Utils;
import com.sia.client.ui.MutableItemContainer;

import java.util.ArrayList;
import java.util.List;

import static com.sia.client.config.Utils.log;

public class AlertVector {

    private final List<AlertStruct> alertList = new ArrayList<>();
    private final List<MutableItemContainer<AlertStruct>> boundComboBoxList = new ArrayList<>();

    public void addAlert(String hrmin,String mesg) {
        AlertStruct alertStruct = new AlertStruct(hrmin,mesg);
        synchronized ( alertList) {
            alertList.add(0,alertStruct);
            Utils.checkAndRunInEDT(() -> {
                for(MutableItemContainer<AlertStruct> boundComboBox: boundComboBoxList) {
                    boundComboBox.insertItemAt(alertStruct,1);
                }
            });
        }
    }
    public void bind(MutableItemContainer<AlertStruct> comboBox) {
        synchronized( alertList) {
            for (int i = alertList.size(); i > 0; i--) {
                comboBox.addItem(alertList.get(i - 1));
            }
            boundComboBoxList.add(comboBox);
        }
        log("Pre-exist urgent messges: " + alertList.size());
    }
    public void unBind(MutableItemContainer<AlertStruct> comboBox) {
        synchronized( alertList) {
            boundComboBoxList.remove(comboBox);
        }
        log("un bind message box ");
    }
}
