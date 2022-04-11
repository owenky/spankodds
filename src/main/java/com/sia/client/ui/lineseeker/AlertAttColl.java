package com.sia.client.ui.lineseeker;

import java.util.Map;

public class AlertAttColl {

    private Map<String,AlertAttributes> alertAttrMap;
    //necessary for ObjectMapper serialization.
    public Map<String, AlertAttributes> getAlertAttrMap() {
        return alertAttrMap;
    }

    public void setAlertAttrMap(Map<String, AlertAttributes> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
}
