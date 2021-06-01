package com.sia.client.model;

import javax.swing.ImageIcon;
import java.awt.Color;

public class LineData {


    public ImageIcon m_icon;
    public String m_data;
    public Color m_color;
    public String border = "";
    public String tooltip = "";

    public LineData(ImageIcon icon, String data, Color c) {
        m_icon = icon;
        m_data = data;
        m_color = c;
    }

    public LineData(ImageIcon icon, String data, Color c, String border) {
        this(icon,data,c);
        this.border = border;
    }

    public String toString() {
        return m_data.toString();
    }

    public ImageIcon getIcon() {
        return m_icon;
    }

    public void setIcon(ImageIcon i) {
        m_icon = i;
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