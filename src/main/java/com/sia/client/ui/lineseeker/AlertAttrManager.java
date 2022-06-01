package com.sia.client.ui.lineseeker;

import com.sia.client.config.Config;
import com.sia.client.config.FontConfig;
import com.sia.client.model.SelectionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlertAttrManager {

    public static AlertConfig getAlertAttr(String gameId, AlertPeriod period)  {
        return Config.instance().getAlertAttrMap().get(makeKey(gameId,period));
    }
    public static AlertConfig getAlertAttr(String atttibuteKey)  {
        return Config.instance().getAlertAttrMap().get(atttibuteKey);
    }
    public static AlertConfig of(String atttibuteKey) {
        String [] keyComp = atttibuteKey.split(Config.KeyJointer);
        AlertPeriod alertPeriod = AlertPeriod.valueOf(keyComp[1]);
        int gameId = Integer.parseInt(keyComp[0]);
        return of(gameId,alertPeriod);
    }
    public static AlertConfig of(int gameId,AlertPeriod alertPeriod) {
        String atttibuteKey = makeKey(gameId,alertPeriod);
        AlertConfig rtn = Config.instance().getAlertAttrMap().get(atttibuteKey);
        if ( null == rtn ) {
            if (SelectionItem.SELECT_BLANK_KEY == gameId) {
                rtn = AlertConfig.BlankAlert;
            } else {
                rtn = new AlertConfig(gameId, alertPeriod);
            }
            if ( gameId > 0) {
                Config.instance().getAlertAttrMap().put(atttibuteKey, rtn);
            }
        }
        return rtn;
    }
    public static void removeFromCache(AlertConfig alertConfig) {
        Config.instance().getAlertAttrMap().remove(alertConfig.getKey());
    }
    public static String makeKey(String gameId, AlertPeriod period) {
        return gameId+Config.KeyJointer+period.name();
    }
    public static String makeKey(int gameId, AlertPeriod period) {
        return makeKey(String.valueOf(gameId),period);
    }
    public static List<LineSeekerAlertSelectionItem> getLineSeekerAlertSelectionItem() {
        Set<String> keys = Config.instance().getAlertAttrMap().keySet();
        List<LineSeekerAlertSelectionItem> rtn = new ArrayList<>(keys.size());
        for(String key: keys) {
            String [] parts = key.split(Config.KeyJointer);
            int gameId = Integer.parseInt(parts[0]);
            AlertPeriod alertPeriod = AlertPeriod.valueOf(parts[1]);
            rtn.add(new LineSeekerAlertSelectionItem(gameId,alertPeriod));
        }
        return rtn;
    }
    public static Map<String, LineSeekerAlertMethodAttr> getAlertMethodMap() {
        return Config.instance().getAlertSeekerMethods().getAlertMethodMap();
    }
    public static  LineSeekerAlertMethodAttr getLineSeekerAlertMethodAttr(String alertState) {
        return getAlertMethodMap().computeIfAbsent(alertState, LineSeekerAlertMethodAttr::new);
    }
    public static AlertSeekerMethods getAlertSeekerMethods() {
        return Config.instance().getAlertSeekerMethods();
    }
    public static String getBookies() {
        return Config.instance().getBookies().getValue();
    }
    public static synchronized FontConfig getFontConfig() {
        return Config.instance().getFontConfig();
    }
}
