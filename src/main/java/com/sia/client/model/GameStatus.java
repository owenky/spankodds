package com.sia.client.model;

import com.sia.client.config.GameUtils;
import com.sia.client.config.SiaConst;
import com.sia.client.ui.AppController;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.sia.client.config.SiaConst.StageGroupAnchorOffset;

public enum GameStatus {
    HalfTime(GameGroupHeader.createStageGroupHeader(SiaConst.HalfTimeStr,-1000000),"halftime.wav", ()->AppController.getUser().getHalftimeAlert()
            ,null,"TIME"),
    SeriesPrice(GameGroupHeader.createStageGroupHeader(SiaConst.SeriesPricesStr,StageGroupAnchorOffset+100),null, null
            , null,SiaConst.SeriesPricesStr,SiaConst.SoccerSeriesPricesStr),
    InGamePrices(GameGroupHeader.createStageGroupHeader(SiaConst.InGamePricesStr,StageGroupAnchorOffset+110),null, null
            , null,SiaConst.InGamePricesStr,SiaConst.SoccerInGamePricesStr),
    //    the keywords for inprogress is wrong, it is opposite to "NULL" and "", check out Game.StatusSet to find out possible keywords for in progress
    InProgress(GameGroupHeader.createStageGroupHeader(SiaConst.InProgresStr,StageGroupAnchorOffset+130),"started.wav", ()->AppController.getUser().getStartedAlert()
            , GameStatus::isInProgress),
    Final(GameGroupHeader.createStageGroupHeader(SiaConst.FinalStr,StageGroupAnchorOffset+200),"final.wav", ()->AppController.getUser().getFinalAlert(),
            null,SiaConst.FinalStr,"WIN","TIE","CNCLD","PONED");

    public static GameStatus getGameStatus(Game game) {
        if ( game.isIngame() && GameUtils.isGameStarted(game)) {
            //A good rule to check Frank is a game should never be under in game if it hasn’t started yet -- 12/19/2021
            return InGamePrices;
        } else if ( game.isSeriesprice()) {
            return SeriesPrice;
        }
        return find(game.getStatus());
    }
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
    GameStatus(GameGroupHeader groupHeader, String defaultSoundFile, Supplier<String> alertPrefSupplier, Function<String,Boolean> rule, String ...keywords) {
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
    public GameGroupHeader getGroupHeader() {
        return groupHeader;
    }
    public Supplier<String> getAlertPrefSupplier() {
        return alertPrefSupplier;
    }
    private final Supplier<String> alertPrefSupplier;
    private final String defaultSoundFile;
    private final GameGroupHeader groupHeader;
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
