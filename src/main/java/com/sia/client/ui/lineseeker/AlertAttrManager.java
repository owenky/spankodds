package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class AlertAttrManager {

    private static AlertAttColl alertAttColl;


    public static AlertAttributes getAlertAttr(String gameId, String period) throws JsonProcessingException {
        return getAlertAttColl().getAlertAttrMap().get(makeKey(gameId,period));
//        String jsonStr = alertAtrributeMap.get();
//        return new ObjectMapper().readValue(jsonStr,AlertAttributes.class);
    }
    public static void addSerializationToCache(AlertAttributes alertAttributes) {
        getAlertAttColl().getAlertAttrMap().put(makeKey(String.valueOf(alertAttributes.getGameId()),alertAttributes.getPeriod()),alertAttributes);
    }
    public static String serializeAlertAttr(AlertAttributes attr) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(attr);
    }
    public static String serializeAlertAlertAttColl() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(getAlertAttColl());
    }
    public static void deSerializeAlertAlertAttColl(String alertAttCollJsonStr) throws JsonProcessingException {
        alertAttColl = new ObjectMapper().readValue(alertAttCollJsonStr,AlertAttColl.class);
    }
    private static synchronized AlertAttColl getAlertAttColl() {
        if ( null == alertAttColl) {
            initAlertAttColl();
        }
        return alertAttColl;
    }
    private static synchronized void initAlertAttColl() {
        alertAttColl = new AlertAttColl();
        alertAttColl.setAlertAttrMap(new HashMap<>());
    }
    private static String makeKey(String gameId, String period) {
        return gameId+"@"+period;
    }
}
