package com.sia.client.ui.simulator;

import com.sia.client.model.AbstratScreen;
import com.sia.client.ui.ColumnCustomizableTable;

import javax.swing.JPanel;

public class MainScreenTest extends JPanel implements AbstratScreen<TestGame> {

    private final TableProperties tblProp;
    public MainScreenTest(TableProperties tblProp) {
        setName("Main screen of table "+tblProp.table.getName());
        this.tblProp = tblProp;
    }
    public void destroyMe() {

    }
    public void createMe() {
        tblProp.table.clear();
        tblProp.rebuild();
    }
    @Override
    public ColumnCustomizableTable<TestGame> getColumnCustomizableTable() {
        return tblProp.table;
    }
}
