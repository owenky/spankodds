package com.sia.client.ui;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;

public class LineSeekerColumnFieldGroup {

    private static final Dimension DefaultFieldDim = new Dimension(100,25);
    public final JTextField input1 = new JTextField();
    public final JTextField input2 = new JTextField();
    public final JRadioButton good = new JRadioButton("Good");
    public final JRadioButton bad = new JRadioButton("Bad");
    public final JRadioButton neutral = new JRadioButton("Neutral");
    public final JLabel titleLabel;

    public LineSeekerColumnFieldGroup(String input1Name, String input2Name) {
        input1.setName(input1Name);
        input1.setPreferredSize(DefaultFieldDim);
        input1.setMinimumSize(DefaultFieldDim);
        input2.setName(input2Name);
        input2.setPreferredSize(DefaultFieldDim);
        input2.setMinimumSize(DefaultFieldDim);
        titleLabel = new JLabel("");
        titleLabel.setPreferredSize(DefaultFieldDim);

        ButtonGroup group = new ButtonGroup();
        group.add(good);
        group.add(bad);
        group.add(neutral);
    }
    public String getInput1Text() {
        return input1.getText();
    }
    public String getInput2Text() {
        return input2.getText();
    }
    public String get() {
        return input2.getText();
    }
    public void setTitle(String titleStr) {
        titleLabel.setText(titleStr);
    }
}
