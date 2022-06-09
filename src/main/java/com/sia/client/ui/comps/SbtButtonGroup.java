package com.sia.client.ui.comps;

import javax.swing.*;
import java.util.Enumeration;

public class SbtButtonGroup extends ButtonGroup implements ActionableOnChanged{

    /**
     * btn's text and name and action_command should be same.
     * and button's name should be uniq in this ButtonGroup
     */
    @Override
    public void add(AbstractButton btn) {
        if ( null != btn.getActionCommand() && 0 < btn.getActionCommand().trim().length()) {
            String ac = btn.getActionCommand().trim();
            btn.setName(ac);
            btn.setText(ac);
            super.add(btn);
        } else {
            throw new IllegalArgumentException("action command of button is not set");
        }
    }
    @Override
    public void addListener(CompValueChangedListener l) {
        Enumeration<AbstractButton> radioBtnEnum = getElements();
        while ( radioBtnEnum.hasMoreElements()) {
            radioBtnEnum.nextElement().addActionListener(l);
        }
    }
    @Override
    public void rmListener(CompValueChangedListener l) {
        Enumeration<AbstractButton> radioBtnEnum = getElements();
        while ( radioBtnEnum.hasMoreElements()) {
            radioBtnEnum.nextElement().removeActionListener(l);
        }
    }
    @Override
    public void setValue(Object obj) {
        this.getSelectedButton(String.valueOf(obj)).setSelected(true);
    }

    @Override
    public String getValue() {
        return getSelection().getActionCommand();
    }
    public AbstractButton getSelectedButton(String actionCommand) {
        Enumeration<AbstractButton> btnEnum = getElements();
        JRadioButton rtn = null;
        while ( btnEnum.hasMoreElements()) {
            JRadioButton btn = (JRadioButton)btnEnum.nextElement();
            if ( actionCommand.equals(btn.getActionCommand())) {
                rtn = btn;
                break;
            }
        }
        return rtn;
    }
}
