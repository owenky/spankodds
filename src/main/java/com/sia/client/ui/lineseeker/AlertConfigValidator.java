package com.sia.client.ui.lineseeker;

public abstract class AlertConfigValidator {

    public static String validate(AlertPane alertPane) {

        int selectedGameId = alertPane.getSelectedGameId();
        if ( selectedGameId < 1) {
            return "Please select a game ";
        } else {
            if( ! alertPane.isEdited() && null != AlertAttrManager.getAlertAttr(String.valueOf(selectedGameId), alertPane.getSelectedAlertPeriod())) {
                return "Alert for game id "+selectedGameId+" and period "+ alertPane.getSelectedAlertPeriod()+
                        " already exists. Please change game id or period to create new alert," +
                        " or chose from Alerts drop down box to modify existing one. ";
            }else {
                return null;
            }
        }
    }
}
