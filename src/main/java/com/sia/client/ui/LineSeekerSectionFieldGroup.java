package com.sia.client.ui;

import javax.swing.JCheckBox;

public class LineSeekerSectionFieldGroup {

    public final LineSeekerColumnFieldGroup leftColumn;
    public final LineSeekerColumnFieldGroup rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;

    public LineSeekerSectionFieldGroup(String leftColumnTitle,String rightColumnTitle) {
        leftColumn = new LineSeekerColumnFieldGroup(leftColumnTitle);
        rightColumn = new LineSeekerColumnFieldGroup(rightColumnTitle);
        useEquivalent = new JCheckBox("Use Mathematical Equivalent");
        activateStatus = new JCheckBox("Activate");
        activateStatus.setName(activateStatus.getText());  //name is used for rendered in TitledPanelGenerator, it should be same as its text.
        activateStatus.setSelected(true);
    }
}
