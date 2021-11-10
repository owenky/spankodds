package com.sia.client.ui;

import com.sia.client.config.Utils;
import com.sia.client.model.KeyedObject;
import com.sia.client.model.TableCellRendererProvider;

import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.util.List;

public class RowHeaderTable<V extends KeyedObject> extends JTable implements ColumnAdjuster {

	private final ColumnCustomizableTable<V> mainTable;
	private final boolean hasRowNumber;
	private TableCellRenderer headerCellRenderer;
	private static final long serialVersionUID = 20091228L;

	public RowHeaderTable(ColumnCustomizableTable<V> mainTable, boolean hasRowNumber) {
		super(mainTable.getModel());
		this.hasRowNumber = hasRowNumber;
		this.mainTable = mainTable;
		((RowHeaderColumnModel)this.getColumnModel()).setMainTable(mainTable);
		this.setAutoCreateColumnsFromModel(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ToolTipManager.sharedInstance().registerComponent(this);
	}
	public void optimizeSize() {
		//set jviewport (row header of jscroll pane of mainTable) preferred size
		getParent().setPreferredSize(getPreferredSize());
		//flip policy makes main table takes up space that RowHeaderTable releases. --06/25/2021
		mainTable.getTableScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mainTable.getTableScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	public ColumnCustomizableTable<V> getMainTable(){
		return mainTable;
	}
	@Override
	public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
		if ( null == headerCellRenderer) {
			TableCellRendererProvider tableCellRendererProvider = (rViewIndex, cViewIndex)-> {
				int colModelIndex = RowHeaderTable.this.convertColumnIndexToModel(cViewIndex);
				int colDataModelIndex = mainTable.getLockedColumns().get(colModelIndex);
				return mainTable.getUserCellRenderer(rViewIndex,colDataModelIndex);
			};
			headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, mainTable.getModel().getColumnHeaderProperty());
		}
		return headerCellRenderer;
	}
	@Override
	public String getToolTipText(MouseEvent e) {
		return Utils.getTableCellToolTipText(this,e);
	}
	@Override
	public void setRowMargin(int rowMargin) {
		//don't set row margin, use main table's row margin
	}
	@Override
	public boolean getAutoCreateColumnsFromModel() {
		return true;
	}
	@Override
	public final void removeRowSelectionInterval(int index0, int index1){
		super.removeRowSelectionInterval(index0, index1);
	}
	@Override
	public final void addRowSelectionInterval(int index0, int index1){
		super.addRowSelectionInterval(index0, index1);
	}
	@Override
	public boolean isRowSelected(int row) {
		return mainTable.isRowSelected(row);
	}
	@Override
	public void createDefaultColumnsFromModel() {

		// Remove any current columns
		RowHeaderColumnModel cm = (RowHeaderColumnModel)getColumnModel();

		while (cm.getColumnCount() > 0) {
			cm.removeColumn(cm.getColumn(0));
		}

		if ( mainTable == null){
			return;
		}

		if ( hasRowNumber) {
			TableColumn theColumn_ = cm.getRowNoColumn();
			addColumn(theColumn_);
		}

		List<Integer> lockColumns = mainTable.getLockedColumns();
		if ( null != lockColumns) {
			for (Integer tcIndex : lockColumns) {
				cm.addColumn( mainTable.getColumnFromDataModel(tcIndex));
			}
		}
	}
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	@Override
    public TableColumnModel createDefaultColumnModel() {
		return new RowHeaderColumnModel();
    }
	@Override
	public Object getValueAt(int row, int column) {
		
		Object rtn;
		if ( hasRowNumber) {
			if (column == 0) {
				rtn = row+1;
			} else {
				rtn = super.getValueAt(row, column-1);
			}
		} else {
			rtn = super.getValueAt(row, column);
		}
		return rtn;

	}
	@Override
	public int getRowCount() {
		if ( mainTable == null){
			return 0;
		} else {
			return mainTable.getRowCount();
		}
	}
	@Override
	public void adjustColumn(int column) {
		mainTable.adjustRowHeaderColumn(column);
		optimizeSize();
	}
	@Override
	public JTable table() {
		return this;
	}
	@Override
    public int convertRowIndexToModel(int index_) {
		return mainTable.convertRowIndexToModel(index_);
    }
	@Override
    public int convertRowIndexToView(int index_) {
		return mainTable.convertRowIndexToView(index_);
    }
	@Override
	public String getName() {
		return "RowHeaderTable of "+mainTable.getName();
	}
	@Override
	public String toString() {
		return getName();
	}
//	@Override
//	public void tableChanged(TableModelEvent e) {
//		super.tableChanged(e);
//		if ( (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW) &&  null != mainTable  ) {
//			//super method discard row model, need to re-config row height
//			mainTable.configHeaderRow();
//		}
//	}
}
