package com.sia.client.model;

public class ViewValue {
    private String tooltiptext;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }


    public String getTooltiptext() {
        return tooltiptext;
    }

    public void setTooltiptext(final String tooltiptext) {
        this.tooltiptext = tooltiptext;
    }
    public void appendTooltipText(String newText) {
        if ( null == tooltiptext) {
            tooltiptext = "";
        }
        tooltiptext = tooltiptext + newText;
    }
}
