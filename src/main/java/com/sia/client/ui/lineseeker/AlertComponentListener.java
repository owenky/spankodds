package com.sia.client.ui.lineseeker;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AlertComponentListener implements KeyListener, ActionListener, DocumentListener {

    private final AlertPane alertPane;
    public AlertComponentListener(AlertPane alertPane) {
        this.alertPane = alertPane;
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
    @Override
    public void insertUpdate(DocumentEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkAndSetEditStatus();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkAndSetEditStatus();
    }
    private void checkAndSetEditStatus() {
        alertPane.setEditStatus(isConfigurationChanged());
    }
    private boolean isConfigurationChanged() {
        AlertConfig currentConfig = alertPane.getAlertConfig();
        boolean changed = false;
        for(AlertSectionName alertSectionName: AlertSectionName.values()) {
            LineSeekerAttribute sectionAttr = currentConfig.getSectionAtrribute(alertSectionName);
            SectionComponents sc = alertPane.getSectionComponents(alertSectionName);
            if ( (changed=valueChanged(sectionAttr,sc))) {
                break;
            }
        }
        return changed;
    }
    private boolean valueChanged(LineSeekerAttribute sectionAttr, SectionComponents sc) {

        boolean changed = sectionAttr.isActivateStatus() != sc.activateStatus.isSelected();
        if ( changed) {
            return true;
        }
        changed = sectionAttr.isUseEquivalent() != sc.useEquivalent.isSelected();
        if ( changed ) {
            return true;
        }
        changed = columnValueChanged(sectionAttr.getHomeColumn(),sc.getLeftColumn());
        if ( changed ) {
            return true;
        }

        return columnValueChanged(sectionAttr.getVisitorColumn(),sc.getRightColumn());
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
