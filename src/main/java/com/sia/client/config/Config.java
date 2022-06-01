package com.sia.client.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sia.client.model.Games;
import com.sia.client.ui.comps.ActionableOnChanged;
import com.sia.client.ui.comps.SimpleValueWraper;
import com.sia.client.ui.lineseeker.AlertConfig;
import com.sia.client.ui.lineseeker.AlertSeekerMethods;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    public static final String KeyJointer = "@";
    private Map<String, AlertConfig> alertAttrMap = new HashMap<>();
    private AlertSeekerMethods alertSeekerMethods = new AlertSeekerMethods();
    private SimpleValueWraper<String> bookies;
    private FontConfig fontConfig;
    private static final Config instance;

    static {
        instance = new Config();
        instance.setAlertAttrMap(new HashMap<>());
    }
    public static Config instance() {
        return instance;
    }
    //necessary for ObjectMapper serialization.
    public Map<String, AlertConfig> getAlertAttrMap() {
        return alertAttrMap;
    }
    public void setAlertAttrMap(Map<String, AlertConfig> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
    public AlertSeekerMethods getAlertSeekerMethods() {
        return alertSeekerMethods;
    }

    public void setAlertSeekerMethods(AlertSeekerMethods alertSeekerMethods) {
        this.alertSeekerMethods = alertSeekerMethods;
    }
    public SimpleValueWraper<String> getBookies() {
        if ( null == bookies) {
            bookies = new SimpleValueWraper<>("");
        }
        return bookies;
    }

    public void setBookies(SimpleValueWraper<String> bookies) {
        this.bookies = bookies;
    }
    public FontConfig getFontConfig() {
        if ( null == fontConfig ){
            fontConfig = new FontConfig();
        }
        return fontConfig;
    }

    public void setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
    }
    public void syncWithGames() {
        List<String> obsoleteGameIds = alertAttrMap.keySet().stream()
                .filter(key-> ! Games.instance().containsGameId(key.split(KeyJointer)[0]))
                .collect(Collectors.toList());

        obsoleteGameIds.forEach(key-> {
            alertAttrMap.remove(key);
        });

    }
    public static String serialize() throws JsonProcessingException {
        return Utils.getObjectMapper().writeValueAsString(instance);
    }
    public static void deSerialize(String str) throws IOException {
        if ( null != str && 0 < str.length()) {
            ObjectMapper objectMapper = Utils.getObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(instance);
            objectReader.readValue(str, instance.getClass());
        }
    }
}
