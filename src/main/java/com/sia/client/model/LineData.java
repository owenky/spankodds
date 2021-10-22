package com.sia.client.model;

import java.awt.Color;

public class LineData {


    private String iconPath;
    private String m_data;
    private Color m_color;
    private String border = "";
    private String tooltip = "";

    public LineData(String iconPath, String data, Color c) {
        this.iconPath = iconPath;
        m_data = data;
        m_color = c;
    }

    public LineData(String iconPath, String data, Color c, String border) {
        this(iconPath,data,c);
        this.border = border;
    }
    public String getM_data() {
        return m_data;
    }
    public String toString() {
        return m_data;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String i) {
        iconPath = i;
    }

    public String getData() {
        return m_data;
    }

    public void setData(String s) {
        m_data = s;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String s) {
        border = s;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String s) {
        tooltip = s;
    }

    public Color getBackgroundColor() {
        return m_color;
    }

    public void setBackgroundColor(Color i) {
        m_color = i;
    }


}