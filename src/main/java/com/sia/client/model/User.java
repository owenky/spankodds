package com.sia.client.model;

import com.sia.client.config.SiaConst.SportName;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String bookieTitles;
    private final Map<String,String> sportPreference = new HashMap<>();
    private String email;
    private String password;
    private String username;
    private String address;
    private String city;
    private  String state;
    private String country;
    private String phoneumber;
    private String timezone;
    private String oddstype;
    private String notificationmethod;
    private String subscriptiontype;
    private boolean isloggedin;
    private String bookiecolumnprefs;
    private final StringBuilder bookiecolumnschanged = new StringBuilder();
    private String fixedcolumnprefs;
    private String columncolors;
    private String customtabs;
    private String finalalert;
    private String startedalert;
    private String halftimealert;
    private String lineupalert;
    private String officialalert;
    private String injuryalert;
    private String timechangealert;
    private String limitchangealert;
    private String bigearnalert;
    private long logintime;
    private String chartfilename;
    private int chartminamtnotify;
    private int chartsecsrefresh;

    private String tabsindex;
    private String linealerts;

    public User(String username, String password, String email, String address, String city, String state, String country, String phoneumber,
                String timezone, String oddstype, String notificationmethod, String subscriptiontype, boolean isloggedin,
                String bookiecolumnprefs, String fixedcolumnprefs,
                String columncolors,
                String customtabs,
                String finalalert,
                String startedalert,
                String halftimealert,
                String lineupalert,
                String officialalert,
                String injuryalert,
                String timechangealert,
                String limitchangealert,
                String bigearnalert,
                String footballpref,
                String basketballpref,
                String baseballpref,
                String hockeypref,
                String fightingpref,
                String soccerpref,
                String autoracingpref,
                String golfpref,
                String tennispref,
                String chartfilename,
                int chartminamtnotify,
                int chartsecsrefresh,
                String tabsindex,
                String linealerts
    ) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.phoneumber = phoneumber;
        this.timezone = timezone;
        this.oddstype = oddstype;
        this.notificationmethod = notificationmethod;
        this.subscriptiontype = subscriptiontype;
        this.isloggedin = isloggedin;
        this.bookiecolumnprefs = bookiecolumnprefs;
        this.fixedcolumnprefs = fixedcolumnprefs;


        this.columncolors = columncolors;
        this.customtabs = customtabs;
        this.finalalert = finalalert;
        this.startedalert = startedalert;
        this.halftimealert = halftimealert;
        this.lineupalert = lineupalert;
        this.officialalert = officialalert;
        this.injuryalert = injuryalert;
        this.timechangealert = timechangealert;
        this.limitchangealert = limitchangealert;
        this.bigearnalert = bigearnalert;

        setSportPreference(SportName.Football,footballpref);
        setSportPreference(SportName.Basketball,basketballpref);
        setSportPreference(SportName.Baseball,baseballpref);
        setSportPreference(SportName.Hockey,hockeypref);
        setSportPreference(SportName.Fighting,fightingpref);
        setSportPreference(SportName.Soccer,soccerpref);
        setSportPreference(SportName.Auto_Racing,autoracingpref);
        setSportPreference(SportName.Golf,golfpref);
        setSportPreference(SportName.Tennis,tennispref);

        this.chartfilename = chartfilename;
        this.chartminamtnotify = chartminamtnotify;
        this.chartsecsrefresh = chartsecsrefresh;
        this.tabsindex = tabsindex;
        this.linealerts = linealerts;


        logintime = System.currentTimeMillis();

    }

    public String getUsername() {
        return username;
    }

    public String getTabsIndex() {
        return tabsindex;
    }

    public void setTabsIndex(String s) {
        tabsindex = s;
    }

    public String getLineAlerts() {
        return linealerts;
    }

    public void setLineAlerts(String s) {
        linealerts = s;
    }


    public int getChartMinAmtNotify() {
        return chartminamtnotify;
    }

    public void setChartMinAmtNotify(int s) {
        chartminamtnotify = s;
    }

    public int getChartSecsRefresh() {
        return chartsecsrefresh;
    }

    public void setChartSecsRefresh(int s) {
        chartsecsrefresh = s;
    }

    public String getChartFileName() {
        return chartfilename;
    }

    public void setChartFileName(String s) {
        chartfilename = s;
    }


    public void setSportPreference(String sportName, String pref) {
        sportPreference.put(sportName,pref);
    }
    public String getSportPreference(String sportName) {
        return sportPreference.get(sportName);
    }
    public long getLoginTime() {

        return logintime;
    }

    public String getBookieColumnPrefs() {

        return bookiecolumnprefs;
    }

    public void setBookieColumnPrefs(String s) {

        bookiecolumnprefs = s;
    }
    public void addBookieColumnChanged(String s) {
        if ( bookiecolumnschanged.length() > 0) {
            bookiecolumnschanged.append(",");
        }
        bookiecolumnschanged.append(s);
    }
    public String getBookieColumnsChanged() {

        return null==bookiecolumnschanged?"":bookiecolumnschanged.toString();
    }
    public String getFixedColumnPrefs() {
        return fixedcolumnprefs;
    }

    public void setFixedColumnPrefs(String s) {
        fixedcolumnprefs = s;
    }

    public String getColumnColors() {
        return columncolors;
    }

    public void setColumnColors(String s) {
        columncolors = s;
    }

    public String getCustomTabs() {
        return customtabs;
    }

    public void setCustomTabs(String s) {
        customtabs = s;
    }

    public String getFinalAlert() {
        return finalalert;
    }

    public void setFinalAlert(String s) {
        finalalert = s;
    }

    public String getStartedAlert() {
        return startedalert;
    }

    public void setStartedAlert(String s) {
        startedalert = s;
    }

    public String getHalftimeAlert() {
        return halftimealert;
    }

    public void setHalftimeAlert(String s) {
        halftimealert = s;
    }

    public String getLineupAlert() {
        return lineupalert;
    }

    public void setLineupAlert(String s) {
        lineupalert = s;
    }

    public String getOfficialAlert() {
        return officialalert;
    }

    public void setOfficialAlert(String s) {
        officialalert = s;
    }

    public String getInjuryAlert() {
        return injuryalert;
    }

    public void setInjuryAlert(String s) {
        injuryalert = s;
    }

    public String getTimechangeAlert() {
        return timechangealert;
    }

    public void setTimechangeAlert(String s) {
        timechangealert = s;
    }

    public String getLimitchangeAlert() {
        return limitchangealert;
    }

    public void setLimitchangeAlert(String s) {
        limitchangealert = s;
    }

    public String getBigearnAlert() {
        return bigearnalert;
    }

    public void setBigearnAlert(String s) {
        bigearnalert = s;
    }
    public void setBookieTitles(String bookieTitles) {
        this.bookieTitles = bookieTitles;
    }
    public String getBookieTitles() {
        return bookieTitles;
    }
    // this method will send the login server all of the user preferences for the server
    // to store in its database
    public void notifyServerUserPreferences() {

        /*



         */


    }


}