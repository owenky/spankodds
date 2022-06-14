package com.sia.client.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.sia.client.model.Games;
import com.sia.client.model.UserDisplaySettings;
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
    private UserDisplaySettings userDisplaySettings;
    private ColumnSettings columnSettings;
    @JsonIgnore
    private boolean syncStatus = true;
    private static final Config instance;

    static {
        instance = new Config();
        instance.setAlertAttrMap(new HashMap<>());
    }
    public static Config instance() {
        return instance;
    }
    @JsonProperty
    public synchronized Map<String, AlertConfig> getAlertAttrMap() {
        if ( !syncStatus) {
            syncWithGames();
            syncStatus = true;
        }
        return alertAttrMap;
    }
    @JsonProperty
    public void setAlertAttrMap(Map<String, AlertConfig> alertAttrMap) {
        this.alertAttrMap = alertAttrMap;
    }
    @JsonProperty
    public AlertSeekerMethods getAlertSeekerMethods() {
        return alertSeekerMethods;
    }
    @JsonProperty
    public void setAlertSeekerMethods(AlertSeekerMethods alertSeekerMethods) {
        this.alertSeekerMethods = alertSeekerMethods;
    }
    @JsonProperty
    public SimpleValueWraper<String> getBookies() {
        if ( null == bookies) {
            bookies = new SimpleValueWraper<>("");
        }
        return bookies;
    }
    @JsonProperty
    public void setBookies(SimpleValueWraper<String> bookies) {
        this.bookies = bookies;
    }
    @JsonProperty
    public FontConfig getFontConfig() {
        if ( null == fontConfig ){
            fontConfig = new FontConfig();
        }
        return fontConfig;
    }
    @JsonProperty
    public void setFontConfig(FontConfig fontConfig) {
        this.fontConfig = fontConfig;
    }
    private void syncWithGames() {
        List<String> obsoleteGameIds = alertAttrMap.keySet().stream()
                .filter(key-> ! Games.instance().containsGameId(key.split(KeyJointer)[0]))
                .collect(Collectors.toList());

        obsoleteGameIds.forEach(key-> {
            alertAttrMap.remove(key);
        });

    }
    @JsonProperty
    public UserDisplaySettings getUserDisplaySettings() {
        if ( null == userDisplaySettings) {
            userDisplaySettings = new UserDisplaySettings();
        }
        return userDisplaySettings;
    }
    @JsonProperty
    public void setUserDisplaySettings(UserDisplaySettings userDisplaySettings) {
        this.userDisplaySettings = userDisplaySettings;
    }
    @JsonProperty
    public ColumnSettings getColumnSettings() {
        if ( null == columnSettings) {
            columnSettings = new ColumnSettings();
        }
        return columnSettings;
    }
    @JsonProperty
    public void setColumnSettings(ColumnSettings columnSettings) {
        this.columnSettings = columnSettings;
    }
    @JsonIgnore
    public void setOutOfSync() {
        this.syncStatus = false;
    }
    public static String serialize() throws JsonProcessingException {
        return OmFactory.getObjectMapper().writeValueAsString(instance);
    }

    public static void deSerialize(String str) throws IOException {
        if ( null != str && 0 < str.length()) {
            ObjectMapper objectMapper = OmFactory.getObjectMapper();
            ObjectReader objectReader = objectMapper.readerForUpdating(instance);
            objectReader.readValue(str, instance.getClass());
        }
    }
}
