package com.sia.client.ui.simulator;

import com.sia.client.model.ColumnCustomizableDataModel;
import com.sia.client.model.TableSection;

public class TestDataModel extends ColumnCustomizableDataModel<TestGame> {
    @Override
    protected TableSection<TestGame> findTableSectionByHeaderValue(final String headerValue) {
        return null;
    }
}
