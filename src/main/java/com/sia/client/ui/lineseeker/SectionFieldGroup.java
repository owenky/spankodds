package com.sia.client.ui.lineseeker;

import javax.swing.JCheckBox;

public class SectionFieldGroup {

    public final ColumnFieldGroup leftColumn;
    public final ColumnFieldGroup rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;
    public final String sectionName;

    public SectionFieldGroup(String sectionName,String leftColumnTitle, String rightColumnTitle) {
        this.sectionName = sectionName;
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
    public String getSectionName() {
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
