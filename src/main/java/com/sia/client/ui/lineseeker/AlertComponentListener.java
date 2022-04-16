package com.sia.client.ui.lineseeker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AlertComponentListener implements KeyListener, ActionListener {

    private final AlertLayout alertLayout;
    public AlertComponentListener(AlertLayout alertLayout) {
        this.alertLayout = alertLayout;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void keyTyped(KeyEvent e) {
//        checkAndSetEditStatus();
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        checkAndSetEditStatus();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        checkAndSetEditStatus();
    }
    private void checkAndSetEditStatus() {
        alertLayout.setEditStatus(isConfigurationChanged());
    }
    private boolean isConfigurationChanged() {
        AlertConfig currentConfig = alertLayout.getAlertConfig();
        boolean changed = false;
        for(AlertSectionName alertSectionName: AlertSectionName.values()) {
            SectionAttribute sectionAttr = currentConfig.getSectionAtrribute(alertSectionName);
            SectionComponents sc = alertLayout.getSectionComponents(alertSectionName);
            if ( (changed=valueChanged(sectionAttr,sc))) {
                break;
            }
        }
        return changed;
    }
    private boolean valueChanged(SectionAttribute sectionAttr, SectionComponents sc) {

        boolean changed = sectionAttr.isActivateStatus() != sc.activateStatus.isSelected();
        if ( changed) {
            return true;
        }
        changed = sectionAttr.isUseEquivalent() != sc.useEquivalent.isSelected();
        if ( changed ) {
            return true;
        }
        changed = columnValueChanged(sectionAttr.getLeftColumn(),sc.getLeftColumn());
        if ( changed ) {
            return true;
        }

        return columnValueChanged(sectionAttr.getRightColumn(),sc.getRightColumn());
    }
    private boolean columnValueChanged(ColumnAttributes columnAttributes, ColumnComponents columnComponents) {
        boolean changed = ! columnComponents.getLineText().equals(columnAttributes.getLineInput());
        if ( changed ) {
            return true;
        }
        changed = ! columnComponents.getJuiceText().equals(columnAttributes.getJuiceInput());
        if ( changed ) {
            return true;
        }

        String alertStateName = columnComponents.getAlertState();
        return ! columnAttributes.getAlertState().name().equals(alertStateName);
    }
}
