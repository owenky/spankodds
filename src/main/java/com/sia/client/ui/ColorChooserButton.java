package com.sia.client.ui;

import com.sia.client.config.OmFactory;
import com.sia.client.ui.comps.ActionableOnChanged;
import com.sia.client.ui.comps.CompValueChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorChooserButton extends JButton implements ActionableOnChanged {

    private Color current;
    private List<ColorChangedListener> listeners = new ArrayList<>();
    private CompValueChangedListener compValueChangedListener;

    public static  ImageIcon createIcon(Color main, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(main);
        graphics.fillRect(0, 0, width, height);
        graphics.setXORMode(Color.DARK_GRAY);
        graphics.drawRect(0, 0, width-1, height-1);
        image.flush();
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }

    public ColorChooserButton(Color c) {
        setSelectedColor(c);
        addActionListener(arg0 -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
            setSelectedColor(newColor);
            if ( null != compValueChangedListener) {
                compValueChangedListener.actionPerformed(arg0);
            }
        });
    }

    public Color getSelectedColor() {
        return current;
    }

    public void setSelectedColor(Color newColor) {
        setSelectedColor(newColor, true);
    }

    public void setSelectedColor(Color newColor, boolean notify) {

        if (newColor == null) return;

        current = newColor;
        setIcon(createIcon(current, 16, 16));
        repaint();

        if (notify) {
            // Notify everybody that may be interested.
            for (ColorChangedListener l : listeners) {
                l.colorChanged(newColor);
            }
        }
    }
    public void addColorChangedListener(ColorChangedListener toAdd) {
        listeners.add(toAdd);
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        this.compValueChangedListener = l;
    }
    @Override
    public void rmListener(CompValueChangedListener l) {
        compValueChangedListener = null;
    }
    @Override
    public void setValue(Object obj) {
        Color color;
        if ( obj instanceof Color ) {
            color = (Color)obj;
        } else {
            color = OmFactory.getObjectMapper().convertValue(obj,Color.class);
        }
        setSelectedColor(color);
    }

    @Override
    public Color getValue() {
        return getSelectedColor();
    }
}