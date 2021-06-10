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
}
