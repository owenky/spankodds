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
    private ColumnAttributes columnAttributes;

    public ColumnComponents(String columnTitle) {
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
    public void setColumnAttributes(ColumnAttributes columnAttributes) {
        this.columnAttributes = columnAttributes;
        this.lineInput.setText(columnAttributes.getLineInput());
        this.juiceInput.setText(columnAttributes.getJuiceInput());
        this.getAlertStateButton(columnAttributes.getAlertState()).setSelected(true);
    }
    public ColumnAttributes getColumnAttributes() {
        return this.columnAttributes;
    }
    /**
     *
     * @return error message
     */
    public String updateColumnAttributes() {
        String alertStateName = alertStateButtons.getSelection().getActionCommand();
        columnAttributes.setLineInput(lineInput.getText());
        columnAttributes.setJuiceInput(juiceInput.getText());
        AlertState alertState = Enum.valueOf(AlertState.class,alertStateName);
        columnAttributes.setAlertState(alertState);
        return null;
    }

}
