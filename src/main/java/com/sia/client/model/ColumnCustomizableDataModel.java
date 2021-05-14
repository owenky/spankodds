package com.sia.client.model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public abstract class ColumnCustomizableDataModel implements TableModel {

    private final DefaultTableModel delegator = new DefaultTableModel();

    @Override
    abstract public int getRowCount();

    @Override
    public int getColumnCount() {
        return delegator.getColumnCount();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return delegator.getColumnName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return delegator.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return delegator.isCellEditable(rowIndex,columnIndex);
    }
    @Override
    public void addTableModelListener(final TableModelListener l) {
        delegator.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(final TableModelListener l) {
        delegator.removeTableModelListener(l);
    }
    public void fireTableChanged(TableModelEvent e) {
        delegator.fireTableChanged(e);
    }
}
