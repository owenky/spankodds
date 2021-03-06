package com.sia.client.model;

import java.awt.*;

public class ViewValue {

    public Color LINECHANGERED = new Color(255,0,0);

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
    public static String format(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    protected boolean iswhite(Color color)
    {
        if(color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
