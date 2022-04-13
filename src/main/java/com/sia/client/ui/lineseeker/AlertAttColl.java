package com.sia.client.ui.lineseeker;

import java.util.Map;

public class AlertAttColl {

    private Map<String, AlertConfig> alertAttrMap;
    //necessary for ObjectMapper serialization.
    public Map<String, AlertConfig> getAlertAttrMap() {
        return alertAttrMap;
    }

    public void setAlertAttrMap(Map<String, AlertConfig> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
}
