package com.sia.client.ui.lineseeker;

public class AlertConfig {

    private AlertAttributes alertAttributes;
    public AlertConfig(AlertAttributes alertAttributes) {
        this.alertAttributes = alertAttributes;
    }
    public AlertAttributes getAlertAttributes() {
        return alertAttributes;
    }
    public void setAlertAttributes(AlertAttributes alertAttributes) {
        this.alertAttributes=alertAttributes;
    }
    public SectionAttribute getSectionAtrribute(AlertSectionName sectionName) {
        return alertAttributes.getSectionAtrribute(sectionName);
    }
    public int getGameId() {
        return alertAttributes.getGameId();
    }
    public AlertPeriod getPeriod() {
        return alertAttributes.getPeriod();
    }
}
