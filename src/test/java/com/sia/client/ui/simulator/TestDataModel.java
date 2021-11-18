package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;

import javax.swing.table.TableColumn;
import java.util.Vector;

public class TestDataModel extends ColumnCustomizableDataModel<TestGame> {

    public TestDataModel( Vector<TableColumn> allColumns) {
        super(allColumns);
    }

}
