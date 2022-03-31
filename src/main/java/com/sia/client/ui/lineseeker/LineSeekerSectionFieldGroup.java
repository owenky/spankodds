package com.sia.client.ui.lineseeker;

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
    public LineSeekerSectionFieldGroup withShowLineInput(boolean toShowLineInput) {
        leftColumn.withShowLineInput(toShowLineInput);
        rightColumn.withShowLineInput(toShowLineInput);
        return this;
    }
    public void setLeftColumnTitle(String leftColumnTitle) {
        leftColumn.setTitle(leftColumnTitle);
    }
    public void setRightColumnTitle(String leftColumnTitle) {
        rightColumn.setTitle(leftColumnTitle);
    }
}
