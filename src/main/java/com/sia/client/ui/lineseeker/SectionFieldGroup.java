package com.sia.client.ui.lineseeker;

import javax.swing.JCheckBox;

public class SectionFieldGroup {

    public final ColumnFieldGroup leftColumn;
    public final ColumnFieldGroup rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;

    public SectionFieldGroup(String leftColumnTitle, String rightColumnTitle) {
        leftColumn = new ColumnFieldGroup(leftColumnTitle);
        rightColumn = new ColumnFieldGroup(rightColumnTitle);
        useEquivalent = new JCheckBox("Use Mathematical Equivalent");
        activateStatus = new JCheckBox("Activate");
        activateStatus.setName(activateStatus.getText());  //name is used for rendered in TitledPanelGenerator, it should be same as its text.
        activateStatus.setSelected(true);
    }
    public SectionFieldGroup withShowLineInput(boolean toShowLineInput) {
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
