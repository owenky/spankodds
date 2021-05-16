package com.sia.client.ui.simulator;

import java.util.ArrayList;

public class LabeledList extends ArrayList<String> {
    private String header;
    public void setHeader(String header) {
        this.header = header;
    }
    public String getHeader() {
        return this.header;
    }

}
