package com.sia.client.ui.lineseeker;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class ColumnComponents {

    private static final Dimension DefaultFieldDim = new Dimension(100,25);
    public final JTextField lineInput = new JTextField();
    public final JTextField juiceInput = new JTextField();
    private final ButtonGroup alertStateButtons = new ButtonGroup();
    public final JLabel titleLabel;
    public final JButton clearBtn = new JButton("Clear");
    public final AlertSectionName sectionName;

    public ColumnComponents(String columnTitle,AlertSectionName sectionName) {
        this.sectionName = sectionName;
        lineInput.setName("Line");
        lineInput.setPreferredSize(DefaultFieldDim);
        lineInput.setMinimumSize(DefaultFieldDim);
        juiceInput.setName("Juice");
        juiceInput.setPreferredSize(DefaultFieldDim);
        juiceInput.setMinimumSize(DefaultFieldDim);
        titleLabel = new JLabel(columnTitle);
        titleLabel.setPreferredSize(DefaultFieldDim);

        AlertState [] alertStates = AlertState.values();
        for(AlertState state: alertStates) {
            JRadioButton rb = new JRadioButton(state.name());
            rb.setActionCommand(state.name()); //necessary for alertStateButtons.getSelection().getActionCommand() -- 20220-04-03
            alertStateButtons.add(rb);
        }
        alertStateButtons.setSelected(getAlertStateButton(AlertState.Good).getModel(),true);

        clearBtn.addActionListener((event)-> {
            lineInput.setText("");
            juiceInput.setText(ColumnAttributes.defaultJuice);
        });
    }
    public void addInputListener(AlertComponentListener l) {
        lineInput.getDocument().addDocumentListener(l);
        juiceInput.getDocument().addDocumentListener(l);
        Enumeration<AbstractButton> radioBtnEnum = alertStateButtons.getElements();
        while ( radioBtnEnum.hasMoreElements()) {
            radioBtnEnum.nextElement().addActionListener(l);
        }
    }
    public JRadioButton getAlertStateButton(AlertState alertState) {
        Enumeration<AbstractButton> btnEnum = alertStateButtons.getElements();
        JRadioButton rtn = null;
        while ( btnEnum.hasMoreElements()) {
            JRadioButton btn = (JRadioButton)btnEnum.nextElement();
            if ( alertState.name().equals(btn.getText())) {
                rtn = btn;
                break;
            }
        }
        return rtn;
    }
    public ColumnComponents withShowLineInput(boolean toShowLineInput) {
        lineInput.setVisible(toShowLineInput);
        return this;
    }
    public String getFieldValues() {
        return "line="+lineInput.getText()+";juice="+juiceInput.getText();
    }
    public String getLineText() {
        return lineInput.getText();
    }
    public String getJuiceText() {
        return juiceInput.getText();
    }
    public String get() {
        return juiceInput.getText();
    }
    public void setTitle(String titleStr) {
        titleLabel.setText(titleStr);
    }
    public void setColumnCompValues(ColumnAttributes columnAttributes) {
        this.lineInput.setText(columnAttributes.getLineInput());
        this.juiceInput.setText(columnAttributes.getJuiceInput());
        this.getAlertStateButton(columnAttributes.getAlertState()).setSelected(true);
    }
    public void updateColumnCompAttr(ColumnAttributes columnAttributes) {
        columnAttributes.setLineInput(this.lineInput.getText());
        columnAttributes.setJuiceInput(this.juiceInput.getText());
        columnAttributes.setAlertState(AlertState.valueOf(getAlertState()));
    }
    public String getAlertState() {
        return alertStateButtons.getSelection().getActionCommand();
    }
}
