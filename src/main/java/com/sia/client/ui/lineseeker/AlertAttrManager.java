package com.sia.client.ui.lineseeker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlertAttrManager {

    private static final Map<String,String> alertAtrributeMap = new HashMap<>();

    public static AlertAttributes getAlertAttr(String gameId, String period) throws JsonProcessingException {
        String jsonStr = alertAtrributeMap.get(makeKey(gameId,period));
        return new ObjectMapper().readValue(jsonStr,AlertAttributes.class);
    }
    public static void addSerializationToCache(String gameId,String period, String jsonStr) {
        alertAtrributeMap.put(makeKey(gameId,period),jsonStr);
    }
    public static String serializeAlertAttr(AlertAttributes attr) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(attr);
    }
    public static Map<String,String> getAlertAttrbuteMap() {
        return Collections.unmodifiableMap(alertAtrributeMap);
    }
    private static String makeKey(String gameId, String period) {
        return "LineSeekerAlert_"+gameId+"@"+period;
    }
}
