package com.sia.client.model;

import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public enum GameStatus {
//    the keywords for inprogress is wrong, it is opposite to "NULL" and "", check out Game.StatusSet to find out possible keywords for in progress
    InProgress(SiaConst.InProgresStr,"started.wav", ()->AppController.getUser().getStartedAlert()
            , GameStatus::isInProgress),
    HalfTime(SiaConst.HalfTimeStr,"halftime.wav", ()->AppController.getUser().getHalftimeAlert()
            ,null,"TIME"),
    Final(SiaConst.FinalStr,"final.wav", ()->AppController.getUser().getFinalAlert(),
            null,SiaConst.FinalStr,"WIN","TIE","CNCLD","PONED");

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
    GameStatus(String groupHeader, String defaultSoundFile, Supplier<String> alertPrefSupplier, Function<String,Boolean> rule, String ...keywords) {
        this.alertPrefSupplier = alertPrefSupplier;
        this.defaultSoundFile = defaultSoundFile;
        this.groupHeader = groupHeader;
        this.rule = rule;
        keywordsInUpperCase = new HashSet<>(keywords.length);
        for(String keyword:keywords) {
            keywordsInUpperCase.add(keyword.trim().toUpperCase());
        }
    }
    public boolean isSame(String status) {
        if ( null == status) {
            status = "";
        }
        if ( null != rule) {
            return rule.apply(status);
        } else {
            return keywordsInUpperCase.contains(status.trim().toUpperCase());
        }
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
    private final Function<String,Boolean> rule;
    private static boolean isInProgress(String status) {
        if ( null == status){
            status = "";
        } else {
            status = status.trim();
        }
        return ! HalfTime.isSame(status) && ! Final.isSame(status) &&  ! "NULL".equalsIgnoreCase(status) && ! "".equalsIgnoreCase(status);
    }
}
