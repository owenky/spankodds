package com.sia.client.ui.lineseeker;

import java.util.HashMap;
import java.util.Map;

public class AlertSeekerMethods {
    private String renotifyInMinutes = "0.5";
    private Map<String,LineSeekerAlertMethodAttr> alertMethodMap = new HashMap<>();

    public Map<String, LineSeekerAlertMethodAttr> getAlertMethodMap() {
        return alertMethodMap;
    }
    public void setAlertMethodMap(Map<String, LineSeekerAlertMethodAttr> alertMethodMap) {
        this.alertMethodMap = alertMethodMap;
    }
    public String getRenotifyInMinutes() {
        return renotifyInMinutes;
    }

    public void setRenotifyInMinutes(String renotifyInMinutes) {
        this.renotifyInMinutes = renotifyInMinutes;
    }
}
