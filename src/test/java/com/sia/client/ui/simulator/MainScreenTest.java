package com.sia.client.ui.simulator;

import javax.swing.JPanel;

public class MainScreenTest extends JPanel {

    private final TableProperties tblProp;
    public MainScreenTest(TableProperties tblProp) {
        this.tblProp = tblProp;
    }
    public void destroyMe() {

    }
    public void createMe() {
        tblProp.table.clear();
        tblProp.rebuild();
    }
}
