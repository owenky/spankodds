package com.sia.client.model;

public class RowHeaderProviderImpl<V extends KeyedObject> implements RowHeaderProvider{

    private final ColumnCustomizableDataModel<V> tableModel;
    public RowHeaderProviderImpl(ColumnCustomizableDataModel<V> tableModel) {
        this.tableModel = tableModel;
    }
    @Override
    public Object get(final int rowModelIndex) {
        return tableModel.getRowModelIndex2GameGroupHeaderMap().get(rowModelIndex);
    }
}
