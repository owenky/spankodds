package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.Map;

public class AlertAttColl {

    private Map<String, AlertConfig> alertAttrMap = new HashMap<>();
    private Map<String,LineSeekerAlertMethodAttr> alertMethodMap = new HashMap<>();
    //necessary for ObjectMapper serialization.
    public Map<String, AlertConfig> getAlertAttrMap() {
        return alertAttrMap;
    }
    public void setAlertAttrMap(Map<String, AlertConfig> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
    public Map<String, LineSeekerAlertMethodAttr> getAlertMethodMap() {
        return alertMethodMap;
    }
    public void setAlertMethodMap(Map<String, LineSeekerAlertMethodAttr> alertMethodMap) {
        this.alertMethodMap = alertMethodMap;
    }
}
