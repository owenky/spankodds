package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ScreenProperty;
import com.sia.client.model.SpankyWindowConfig;

import javax.swing.table.TableColumn;
import java.util.Vector;

public class TestDataModel extends ColumnCustomizableDataModel<TestGame> {

    public TestDataModel( Vector<TableColumn> allColumns) {
        super(new ScreenProperty("TestSport",new SpankyWindowConfig(0,false, false)),allColumns);
    }

}
