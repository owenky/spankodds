package com.sia.client.ui.lineseeker;

import javax.swing.*;

public class SectionComponents {

    public final ColumnComponents leftColumn;
    public final ColumnComponents rightColumn;
    public final JCheckBox useEquivalent;
    public final JCheckBox activateStatus;
    public final AlertSectionName sectionName;

    public SectionComponents(AlertSectionName sectionName) {
        this.sectionName = sectionName;
        leftColumn = new ColumnComponents(sectionName.getLeftColTitle(),sectionName).withShowLineInput(sectionName.toShowLineInput());
        rightColumn = new ColumnComponents(sectionName.getRightColTitle(),sectionName).withShowLineInput(sectionName.toShowLineInput());
        useEquivalent = new JCheckBox("Use Mathematical Equivalent");
        activateStatus = new JCheckBox("Activate");
        activateStatus.setName(activateStatus.getText());  //name is used for rendered in TitledPanelGenerator, it should be same as its text.
        activateStatus.setSelected(true);
    }
    public void addActionListener(AlertComponentListener l) {
        leftColumn.addInputListener(l);
        rightColumn.addInputListener(l);
        useEquivalent.addActionListener(l);
        activateStatus.addActionListener(l);
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
    public void setSectionCompValues(SectionAttribute sectionAtrribute) {
        this.leftColumn.setColumnCompValues(sectionAtrribute.getLeftColumn());
        this.rightColumn.setColumnCompValues(sectionAtrribute.getRightColumn());
        this.useEquivalent.setSelected(sectionAtrribute.isUseEquivalent());
        this.activateStatus.setSelected(sectionAtrribute.isActivateStatus());
    }
    public void updateSectionAttribute(SectionAttribute sectionAtrribute) {
        this.leftColumn.updateColumnCompAttr(sectionAtrribute.getLeftColumn());
        this.rightColumn.updateColumnCompAttr(sectionAtrribute.getRightColumn());
        sectionAtrribute.setUseEquivalent( this.useEquivalent.isSelected());
        sectionAtrribute.setActivateStatus(this.activateStatus.isSelected());
    }
    public ColumnComponents getLeftColumn() {
        return leftColumn;
    }
    public ColumnComponents getRightColumn() {
        return rightColumn;
    }
}
