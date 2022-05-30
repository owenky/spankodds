package com.sia.client.ui.lineseeker;

public class ColumnAttributes {

    public static final String defaultJuice = "-110";
    private String lineInput="0";
    private String juiceInput=defaultJuice;
    private AlertState alertState = AlertState.Good;

    public String getLineInput() {
        return lineInput;
    }

    public void setLineInput(String lineInput) {
        this.lineInput = lineInput;
    }

    public String getJuiceInput() {
        return juiceInput;
    }

    public void setJuiceInput(String juiceInput) {
        this.juiceInput = juiceInput;
    }

    public AlertState getAlertState() {
        return alertState;
    }

    public void setAlertState(AlertState alertState) {
        this.alertState = alertState;
    }
}
