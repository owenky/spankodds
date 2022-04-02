package com.sia.client.ui.lineseeker;

import javax.swing.JCheckBox;

public class SectionFieldGroup {

    public final ColumnFieldGroup leftColumn;
    public final ColumnFieldGroup rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;
    public final AlertSectionName sectionName;

    public SectionFieldGroup(AlertSectionName sectionName) {
        this.sectionName = sectionName;
        leftColumn = new ColumnFieldGroup(sectionName.getLeftColTitle()).withShowLineInput(sectionName.toShowLineInput());
        rightColumn = new ColumnFieldGroup(sectionName.getRightColTitle()).withShowLineInput(sectionName.toShowLineInput());
        useEquivalent = new JCheckBox("Use Mathematical Equivalent");
        activateStatus = new JCheckBox("Activate");
        activateStatus.setName(activateStatus.getText());  //name is used for rendered in TitledPanelGenerator, it should be same as its text.
        activateStatus.setSelected(true);
    }
    public AlertSectionName getSectionName() {
        return sectionName;
    }
    public void setLeftColumnTitle(String leftColumnTitle) {
        leftColumn.setTitle(leftColumnTitle);
    }
    public void setRightColumnTitle(String leftColumnTitle) {
        rightColumn.setTitle(leftColumnTitle);
    }
    public String getFieldValues() {
        return activateStatus.isSelected()+"|"+useEquivalent.isSelected()+"|"+leftColumn.getFieldValues()+"|"+rightColumn.getFieldValues()+"|";
    }
}
