package com.sia.client.model;

public class ViewValue {
    private String tooltiptext;

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
