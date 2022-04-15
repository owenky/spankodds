package com.sia.client.ui.lineseeker;

public abstract class AlertConfigValidator {

    public static String validate(AlertLayout alertLayout) {

        if ( alertLayout.getSelectedGameId() < 1) {
            return "Please select a game ";
        } else {
            return null;
        }
    }
}
