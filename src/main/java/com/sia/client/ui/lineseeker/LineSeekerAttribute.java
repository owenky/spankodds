package com.sia.client.ui.lineseeker;

public class LineSeekerAttribute {

    private ColumnAttributes homeColumn;
    private ColumnAttributes visitorColumn;
    private boolean useEquivalent = true;
    private boolean activateStatus = true;
    private AlertSectionName sectionName;

    public synchronized ColumnAttributes getHomeColumn() {
        if ( null == homeColumn) {
            homeColumn = new ColumnAttributes();
        }
        return homeColumn;
    }

    public void setHomeColumn(ColumnAttributes homeColumn) {
        this.homeColumn = homeColumn;
    }

    public synchronized ColumnAttributes getVisitorColumn() {
        if ( null == visitorColumn) {
            visitorColumn = new ColumnAttributes();
        }
        return visitorColumn;
    }

    public void setVisitorColumn(ColumnAttributes visitorColumn) {
        this.visitorColumn = visitorColumn;
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
