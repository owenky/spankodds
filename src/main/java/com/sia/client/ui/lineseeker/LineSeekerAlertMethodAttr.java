package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineSeekerAlertMethodAttr {

    public static final String DefaultSoundFilePath = "openers.wav";
    private boolean isAudioEnabled = true;
    private boolean isPopupEnabled = true;
    private String soundFile = DefaultSoundFilePath;
    private String popupSeconds = "3";
    private String renotifyInMinutes = "0.5";

    private PopupLocation popupLocation;
    private final String alertState;

    public LineSeekerAlertMethodAttr (@JsonProperty("alertState") String alertState) {
        this.alertState = alertState;
    }
    public boolean getAudioEnabled() {
        return isAudioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        isAudioEnabled = audioEnabled;
    }

    public boolean getPopupEnabled() {
        return isPopupEnabled;
    }

    public void setPopupEnabled(boolean popupEnabled) {
        isPopupEnabled = popupEnabled;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public void setSoundFile(String soundFile) {
        this.soundFile = soundFile;
    }

    public String getPopupSeconds() {
        return popupSeconds;
    }

    public void setPopupSeconds(String popupSeconds) {
        this.popupSeconds = popupSeconds;
    }

    public String getRenotifyInMinutes() {
        return renotifyInMinutes;
    }

    public void setRenotifyInMinutes(String renotifyInMinutes) {
        this.renotifyInMinutes = renotifyInMinutes;
    }

    public String getAlertState() {
        return alertState;
    }

    public PopupLocation getPopupLocation() {
        if ( null == popupLocation) {
            popupLocation =  PopupLocation.CENTER;
        }
        return popupLocation;
    }

    public void setPopupLocation(PopupLocation popupLocation) {
        this.popupLocation = popupLocation;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public enum PopupLocation {
        CENTER(0),
        TOP_CENTER(1),
        TOP_RIGHT(2),
        MIDDLE_RIGHT(3),
        BOTTOM_RIGHT(4),
        BOTTOM_CENTER(5),
        BOTTOM_LEFT(6),
        MIDDLE_LEFT(7),
        TOP_LEFT(8);


        PopupLocation(int location) {
            this.location = location;
        }
        public int getLocation() {
            return location;
        }
        private final int location;
    }
}
