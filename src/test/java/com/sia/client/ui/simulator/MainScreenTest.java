package com.sia.client.ui.simulator;

import com.sia.client.model.AbstractScreen;
import com.sia.client.ui.ColumnCustomizableTable;

import javax.swing.JPanel;

public class MainScreenTest extends JPanel implements AbstractScreen<TestGame> {

    private final TableProperties tblProp;
    public MainScreenTest(TableProperties tblProp) {
        setName("Main screen of table "+tblProp.getName());
        this.tblProp = tblProp;
    }
    @Override
    public void destroyMe() {
        new Exception("need implementation....").printStackTrace();
    }

    @Override
    public boolean shouldAddToScreen(final TestGame game) {
        new Exception("need implementation....").printStackTrace();
        return false;
    }

    public void createMe() {
        tblProp.rebuild();
    }
    @Override
    public ColumnCustomizableTable<TestGame> getColumnCustomizableTable() {
        return tblProp.table;
    }

    @Override
    public void addGame(final TestGame game) {
        new Exception("need implementation....").printStackTrace();
    }
}
