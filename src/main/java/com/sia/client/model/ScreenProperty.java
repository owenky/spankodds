package com.sia.client.model;

public class ScreenProperty {

    private final SpankyWindowConfig spankyWindowConfig;
    private final String name;
    private int currentmaxlength = 0;
    private long cleartime = System.currentTimeMillis();
    private boolean showheaders = true;

    public ScreenProperty(String name,SpankyWindowConfig spankyWindowConfig) {
        this.spankyWindowConfig = spankyWindowConfig;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getCurrentmaxlength() {
        return currentmaxlength;
    }

    public void setCurrentmaxlength(final int currentmaxlength) {
        this.currentmaxlength = currentmaxlength;
    }

    public long getCleartime() {
        return cleartime;
    }

    public void setCleartime(final long cleartime) {
        this.cleartime = cleartime;
    }

    public boolean isShowheaders() {
        return showheaders;
    }

    public void setShowheaders(final boolean showheaders) {
        this.showheaders = showheaders;
    }

    public SpankyWindowConfig getSpankyWindowConfig() {
        return spankyWindowConfig;
    }
}
