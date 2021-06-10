package com.sia.client.model;

import com.sia.client.config.Utils;

import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.List;

import static com.sia.client.config.Utils.log;

public class AlertVector {

    private final List<AlertStruct> alertList = new ArrayList<>();
    private JComboBox<AlertStruct> boundComboBox;

    public void addAlert(String hrmin,String mesg) {
        AlertStruct alertStruct = new AlertStruct(hrmin,mesg);
        synchronized ( alertList) {
            alertList.add(0,alertStruct);
            if ( null != boundComboBox) {
                Utils.checkAndRunInEDT(()-> boundComboBox.insertItemAt(alertStruct,0));
            }
        }
    }
    public void bind(JComboBox<AlertStruct> jComboBox) {
        synchronized( alertList) {
            for (int i = alertList.size(); i > 0; i--) {
                jComboBox.addItem(alertList.get(i - 1));
            }
            boundComboBox = jComboBox;
        }
        log("Pre-exist urgent messges: " + alertList.size());
    }
}
