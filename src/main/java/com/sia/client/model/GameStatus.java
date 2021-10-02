package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public enum GameStatus {
    InProgress(SiaConst.InProgresStr,"started.wav", ()->AppController.getUser().getStartedAlert()
            ,"NULL",""),
    HalfTime(SiaConst.HalfTimeStr,"halftime.wav", ()->AppController.getUser().getHalftimeAlert()
            ,"TIME"),
    Final(SiaConst.FinalStr,"final.wav", ()->AppController.getUser().getFinalAlert(),
            SiaConst.FinalStr,"WIN");

    public static GameStatus find(String status) {
        GameStatus rtn = null;
        for(GameStatus gs: GameStatus.values()) {
            if ( gs.isSame(status)) {
                rtn = gs;
                break;
            }
        }
        return rtn;
    }
    GameStatus(String groupHeader, String defaultSoundFile, Supplier<String> alertPrefSupplier, String ...keywords) {
        this.alertPrefSupplier = alertPrefSupplier;
        this.defaultSoundFile = defaultSoundFile;
        this.groupHeader = groupHeader;
        keywordsInUpperCase = new HashSet<>(keywords.length);
        for(String keyword:keywords) {
            keywordsInUpperCase.add(keyword.trim().toUpperCase());
        }
    }
    public boolean isSame(String status) {
        if ( null == status) {
            status = "NULL";
        }
        return keywordsInUpperCase.contains(status.trim().toUpperCase());
    }
    public String getSoundFile() {
        return defaultSoundFile;
    }
    public String getGroupHeader() {
        return groupHeader;
    }
    public Supplier<String> getAlertPrefSupplier() {
        return alertPrefSupplier;
    }
    private final Supplier<String> alertPrefSupplier;
    private final String defaultSoundFile;
    private final String groupHeader;
    private final Set<String> keywordsInUpperCase;
}
