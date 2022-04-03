package com.sia.client.ui.lineseeker;

public abstract class AlertConfigValidator {

    public static String validate(AlertConfig alertConfig) {

        if ( alertConfig.getGameId() < 1) {
            return "Please select a game ";
        }
        return null;
    }
}
