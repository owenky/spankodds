package com.sia.client.ui.simulator;

import com.sia.client.model.BookieColumnModel;
import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.ScreenProperty;
import com.sia.client.model.SpankyWindowConfig;

import javax.swing.table.TableColumn;
import java.util.List;

public class TestDataModel extends ColumnCustomizableDataModel<TestGame> {

    public TestDataModel( List<TableColumn> allColumns) {
        super(new ScreenProperty("TestSport",new SpankyWindowConfig(0,false, false)),createTestBookieColumnModel(allColumns));
    }
    public static BookieColumnModel createTestBookieColumnModel(final List<TableColumn> allColumns) {
        return new BookieColumnModel() {

            @Override
            public int size() {
                return allColumns.size();
            }

            @Override
            public TableColumn get(int index) {
                return allColumns.get(index);
            }
        };
    }
}
