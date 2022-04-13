package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class AlertAttrManager {

    private static final String KeyJointer = "@";
    private static AlertAttColl alertAttColl;


    public static AlertConfig getAlertAttr(String gameId, AlertPeriod period)  {
        return getAlertAttColl().getAlertAttrMap().get(makeKey(gameId,period));
    }
    public static AlertConfig getAlertAttr(String atttibuteKey)  {
        return getAlertAttColl().getAlertAttrMap().get(atttibuteKey);
    }
    public static AlertConfig of(String atttibuteKey) {
        AlertConfig rtn = getAlertAttColl().getAlertAttrMap().get(atttibuteKey);
        if ( null == rtn ) {
            String [] keyComp = atttibuteKey.split(KeyJointer);
            AlertPeriod period = AlertPeriod.valueOf(keyComp[1]);
            rtn = new AlertConfig(Integer.parseInt(keyComp[0]),period);
            alertAttColl.getAlertAttrMap().put(atttibuteKey,rtn);
        }
        return rtn;
    }
    public static AlertConfig of(int gameId, AlertPeriod period) {
       return of(makeKey(String.valueOf(gameId),period) );
    }
    public static void addToCache(AlertConfig alertAttributes) {
        getAlertAttColl().getAlertAttrMap().put(alertAttributes.getKey(),alertAttributes);
    }
    public static String serializeAlertAttr(AlertConfig attr) throws JsonProcessingException {
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
    public static String makeKey(String gameId, AlertPeriod period) {
        return gameId+KeyJointer+period.name();
    }
}
