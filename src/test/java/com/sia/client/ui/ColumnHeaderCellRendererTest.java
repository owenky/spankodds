package com.sia.client.ui;

import com.sia.client.config.SiaConst;
import com.sia.client.model.ColumnCustomizableDataModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ColumnHeaderCellRendererTest {

    public static void main(String [] argv) {

        Object value = SiaConst.GameGroupHeaderIden+"a";
        assertEquals("a", ColumnCustomizableDataModel.retrieveGameGroupHeader(value));

        value = SiaConst.GameGroupHeaderIden;
        assertNull(ColumnCustomizableDataModel.retrieveGameGroupHeader(value));

        value = "a";
        assertNull(ColumnCustomizableDataModel.retrieveGameGroupHeader(value));

        value = SiaConst.GameGroupHeaderIden+"a"+SiaConst.GameGroupHeaderIden+"a";
        assertNull( ColumnCustomizableDataModel.retrieveGameGroupHeader(value));
    }
}
