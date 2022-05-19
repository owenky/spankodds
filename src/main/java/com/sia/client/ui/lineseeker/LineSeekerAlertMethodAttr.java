package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LineSeekerAlertMethodAttr {

    public static final String DefaultSoundFile="Default";
    private boolean isAudioEnabled = true;
    private boolean isPopupEnabled = true;
    private String soundFile = DefaultSoundFile;
    private String popupSeconds = "3";
    private String renotifyInMinutes = "0.5";
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
}
