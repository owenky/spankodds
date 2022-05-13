package com.sia.client.ui.lineseeker;

public class SectionAttribute {

    private ColumnAttributes leftColumn;
    private ColumnAttributes rightColumn;
    private boolean useEquivalent = true;
    private boolean activateStatus = true;
    private AlertSectionName sectionName;

    public synchronized ColumnAttributes getLeftColumn() {
        if ( null == leftColumn) {
            leftColumn = new ColumnAttributes();
        }
        return leftColumn;
    }

    public void setLeftColumn(ColumnAttributes leftColumn) {
        this.leftColumn = leftColumn;
    }

    public synchronized ColumnAttributes getRightColumn() {
        if ( null == rightColumn) {
            rightColumn = new ColumnAttributes();
        }
        return rightColumn;
    }

    public void setRightColumn(ColumnAttributes rightColumn) {
        this.rightColumn = rightColumn;
    }

    public boolean isUseEquivalent() {
        return useEquivalent;
    }

    public void setUseEquivalent(boolean useEquivalent) {
        this.useEquivalent = useEquivalent;
    }

    public boolean isActivateStatus() {
        return activateStatus;
    }

    public void setActivateStatus(boolean activateStatus) {
        this.activateStatus = activateStatus;
    }

    public AlertSectionName getSectionName() {
        return sectionName;
    }

    public void setSectionName(AlertSectionName sectionName) {
        this.sectionName = sectionName;
    }
}
