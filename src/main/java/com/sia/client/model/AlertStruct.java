package com.sia.client.model;

public class AlertStruct {

    private final String time;
    private final String mesg;


    public AlertStruct(String time, String mesg) {
        this.time = time;
        this.mesg = mesg;
    }
    public String getTime() {
        return time;
    }

    public String getMesg() {
        return mesg;
    }
    @Override
    public String toString() {
        return time+" "+mesg;
    }
    public Object [] convert() {
        Object [] rowData = new Object [2];
        rowData[0] = getMesg();
        rowData[1] = getTime();
        return rowData;
    }
}
