package com.sia.client.ui.lineseeker;

import com.sia.client.ui.comps.SimpleValueWraper;

import java.util.HashMap;
import java.util.Map;

public class AlertAttColl {

    private Map<String, AlertConfig> alertAttrMap = new HashMap<>();
    private AlertSeekerMethods alertSeekerMethods = new AlertSeekerMethods();
    private SimpleValueWraper<String> bookies;

    //necessary for ObjectMapper serialization.
    public Map<String, AlertConfig> getAlertAttrMap() {
        return alertAttrMap;
    }
    public void setAlertAttrMap(Map<String, AlertConfig> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
    public AlertSeekerMethods getAlertSeekerMethods() {
        return alertSeekerMethods;
    }

    public void setAlertSeekerMethods(AlertSeekerMethods alertSeekerMethods) {
        this.alertSeekerMethods = alertSeekerMethods;
    }
    public SimpleValueWraper<String> getBookies() {
        return bookies;
    }

    public void setBookies(SimpleValueWraper<String> bookies) {
        this.bookies = bookies;
    }
}
