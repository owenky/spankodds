package com.sia.client.ui;

import javax.swing.JCheckBox;

public class LineSeekerSectionFieldGroup {

    public final LineSeekerColumnFieldGroup leftColumn;
    public final LineSeekerColumnFieldGroup rightColumn;
    public final JCheckBox useEquivalent;

    public LineSeekerSectionFieldGroup(String input1Name, String input2Name) {
        leftColumn = new LineSeekerColumnFieldGroup(input1Name,input2Name);
        rightColumn = new LineSeekerColumnFieldGroup(input1Name,input2Name);
        useEquivalent = new JCheckBox();
        useEquivalent.setName("Use Equivalent ???? ");
leftColumn.titleLabel.setText("Atlanta Hawks");
rightColumn.titleLabel.setText("Orlando Magic");
    }
}
