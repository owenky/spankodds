package com.sia.client.ui;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;

public class LineSeekerColumnFieldGroup {

    private static final Dimension DefaultFieldDim = new Dimension(100,25);
    public final JTextField lineInput = new JTextField();
    public final JTextField juiceInput = new JTextField();
    public final JRadioButton good = new JRadioButton("Good");
    public final JRadioButton bad = new JRadioButton("Bad");
    public final JRadioButton neutral = new JRadioButton("Neutral");
    public final JLabel titleLabel;

    public LineSeekerColumnFieldGroup(String columnTitle) {
        lineInput.setName("Line");
        lineInput.setPreferredSize(DefaultFieldDim);
        lineInput.setMinimumSize(DefaultFieldDim);
        juiceInput.setName("Juice");
        juiceInput.setText("-110");
        juiceInput.setPreferredSize(DefaultFieldDim);
        juiceInput.setMinimumSize(DefaultFieldDim);
        titleLabel = new JLabel(columnTitle);
        titleLabel.setPreferredSize(DefaultFieldDim);

        ButtonGroup group = new ButtonGroup();
        group.add(good);
        group.add(bad);
        group.add(neutral);
        good.setSelected(true);
    }
    public String getLineText() {
        return lineInput.getText();
    }
    public String getJuiceText() {
        return juiceInput.getText();
    }
    public String get() {
        return juiceInput.getText();
    }
    public void setTitle(String titleStr) {
        titleLabel.setText(titleStr);
    }
}