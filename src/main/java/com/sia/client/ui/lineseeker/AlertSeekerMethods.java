package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class AlertSeekerMethods {
    private String renotifyInMinutes = "0.5";
    private Map<String,LineSeekerAlertMethodAttr> alertMethodMap = new HashMap<>();
    @JsonProperty
    public Map<String, LineSeekerAlertMethodAttr> getAlertMethodMap() {
        return alertMethodMap;
    }
    @JsonProperty
    public void setAlertMethodMap(Map<String, LineSeekerAlertMethodAttr> alertMethodMap) {
        this.alertMethodMap = alertMethodMap;
    }
    @JsonProperty
    public String getRenotifyInMinutes() {
        return renotifyInMinutes;
    }
    @JsonProperty
    public void setRenotifyInMinutes(String renotifyInMinutes) {
        this.renotifyInMinutes = renotifyInMinutes;
    }
}
