package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class LineSeekerAlertMethodAttr {

    private boolean audioEnabled = true;
    private boolean popupEnabled = true;
    private String soundFile;
    private String popupSeconds = "3";
    private String renotifyInMinutes = "0.5";

    private PopupLocation popupLocation;
    private final String alertState;

    public LineSeekerAlertMethodAttr (@JsonProperty("alertState") String alertState) {
        this.alertState = alertState;
        this.soundFile = AlertState.valueOf(alertState).getDefaultSoundFile();
    }
    public boolean getAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    public boolean getPopupEnabled() {
        return popupEnabled;
    }

    public void setPopupEnabled(boolean popupEnabled) {
        this.popupEnabled = popupEnabled;
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

    public void setRenotifyInMinutes(String renotifyInMinutes) {
        this.renotifyInMinutes = renotifyInMinutes;
    }

    public String getAlertState() {
        return alertState;
    }

    public PopupLocation getPopupLocation() {
        return popupLocation;
    }

    public void setPopupLocation(PopupLocation popupLocation) {
        if ( popupLocation == PopupLocation.CENTER) {
            popupLocation = PopupLocation.TOP_RIGHT;
        }
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

        public static PopupLocation findPopupLocation(int location) {
            return Arrays.stream(PopupLocation.values()).filter(popupLocation -> popupLocation.getLocation() == location).findAny().get();
        }
        PopupLocation(int location) {
            this.location = location;
        }
        public int getLocation() {
            return location;
        }
        private final int location;
    }
}
