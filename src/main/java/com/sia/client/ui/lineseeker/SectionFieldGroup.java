package com.sia.client.ui.lineseeker;

import javax.swing.JCheckBox;

public class SectionFieldGroup {

    public final ColumnComponents leftColumn;
    public final ColumnComponents rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;
    public final AlertSectionName sectionName;
    private SectionAttribute sectionAtrribute;

    public SectionFieldGroup(AlertSectionName sectionName) {
        this.sectionName = sectionName;
        leftColumn = new ColumnComponents(sectionName.getLeftColTitle()).withShowLineInput(sectionName.toShowLineInput());
        rightColumn = new ColumnComponents(sectionName.getRightColTitle()).withShowLineInput(sectionName.toShowLineInput());
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
    public void setSectionAtrribute(SectionAttribute sectionAtrribute) {
        this.sectionAtrribute = sectionAtrribute;
        this.leftColumn.setColumnAttributes(sectionAtrribute.getLeftColumn());
        this.rightColumn.setColumnAttributes(sectionAtrribute.getRightColumn());
        this.useEquivalent.setSelected(sectionAtrribute.isUseEquivalent());
        this.activateStatus.setSelected(sectionAtrribute.isActivateStatus());
    }
    public synchronized SectionAttribute getSectionAtrribute() {
        if ( null == sectionAtrribute) {
            sectionAtrribute = new SectionAttribute();
            sectionAtrribute.setSectionName(sectionName);
            this.leftColumn.setColumnAttributes(sectionAtrribute.getLeftColumn());
            this.rightColumn.setColumnAttributes(sectionAtrribute.getRightColumn());
        }
        return this.sectionAtrribute;
    }
    public void updateSectionAttribute() {
        this.leftColumn.updateColumnAttributes();
        this.rightColumn.updateColumnAttributes();
        getSectionAtrribute().setUseEquivalent( this.useEquivalent.isSelected());
        getSectionAtrribute().setActivateStatus(activateStatus.isSelected());
        getSectionAtrribute().setSectionName(sectionName);
    }
}
