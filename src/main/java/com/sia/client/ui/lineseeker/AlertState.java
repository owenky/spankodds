package com.sia.client.ui.lineseeker;

public enum AlertState {
    Good("beep1.wav"),
    Bad("beep3.wav"),
    Neutral("beep2.wav");

    AlertState(String defaultSoundFile) {
        this.defaultSoundFile = defaultSoundFile;
    }
    public String getDefaultSoundFile() {
        return defaultSoundFile;
    }
    private final String defaultSoundFile;
}
