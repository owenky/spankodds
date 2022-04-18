package com.sia.client.ui.lineseeker;

public abstract class AlertConfigValidator {

    public static String validate(AlertLayout alertLayout) {

        int selectedGameId = alertLayout.getSelectedGameId();
        if ( selectedGameId < 1) {
            return "Please select a game ";
        } else {
            if( null != AlertAttrManager.getAlertAttr(String.valueOf(selectedGameId),alertLayout.getSelectedAlertPeriod())) {
                return "Alert for game id "+selectedGameId+" and period "+alertLayout.getSelectedAlertPeriod()+
                        " already exists. Please change game id or period to create new alert," +
                        " or chose from Alerts drop down box to modify existing one. ";
            }else {
                return null;
            }
        }
    }
}
