package com.sia.client.ui;

import com.sia.client.model.TableCellRendererProvider;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.List;

public class RowHeaderTable extends JTable {

	private final ColumnCustomizableTable mainTable;
	private boolean toFireChangesInMainTable = true;
	private final boolean hasRowNumber;
	private ColumnHeaderCellRenderer headerCellRenderer;
	private static final long serialVersionUID = 20091228L;

	public RowHeaderTable(ColumnCustomizableTable mainTable, boolean hasRowNumber) {
		this.hasRowNumber = hasRowNumber;
		this.mainTable = mainTable;
		((RowHeaderColumnModel)this.getColumnModel()).setMainTable(mainTable);
		this.setModel(mainTable.getModel());
		this.setAutoCreateColumnsFromModel(false);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	public ColumnCustomizableTable getMainTable(){
		return mainTable;
	}
	@Override
	public final TableCellRenderer getCellRenderer(int rowViewIndex, int columnViewIndex) {
		if ( null == headerCellRenderer) {
			TableCellRendererProvider tableCellRendererProvider = (rViewIndex,cViewIndex)-> {
				int colModelIndex = RowHeaderTable.this.convertColumnIndexToModel(cViewIndex);
				int colDataModelIndex = mainTable.getLockedColumns().get(colModelIndex);
				return mainTable.getUserCellRenderer(rViewIndex,colDataModelIndex);
			};
			headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, mainTable.getColumnHeaderProvider(),mainTable.getMarginProvider());
		}
		return headerCellRenderer;
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
//
//	@Override
//	public int convertColumnIndexToView(int modelColumnIndex){
//		int columnViewIndex;
//		if ( modelColumnIndex == SBTTableModelDelegator.RowNoColumnIndex ) { //�������
//			columnViewIndex = 0;
//		}else if ( modelColumnIndex < 0 ) {
//			columnViewIndex = -1;
//		}else{
//			JTableColumn aColumnIden = mainTable.getColumnFromTableModel(modelColumnIndex);
//			List<JTableColumn> lockedColumns = mainTable.getLockColumns();
//			columnViewIndex = lockedColumns.indexOf(aColumnIden);
//			if ( columnViewIndex >=0 ){
//				columnViewIndex = columnViewIndex + 1; // ��һ�����кţ�������ס���дӵڶ��п�ʼ
//			}
//		}
//
//		return columnViewIndex;
//
//	}
//	@Override
//	public int convertColumnIndexToModel(int viewColumnIndex){
//		int columnModelIndex;
//		if ( viewColumnIndex == 0 ) { //�������
//			columnModelIndex =  SBTTableModelDelegator.RowNoColumnIndex;
//		}else{
//			List<JTableColumn> lockedColumns = mainTable.getLockColumns();
//			JTableColumn aColumnIden = lockedColumns.get(viewColumnIndex - 1); // ��һ�����кţ�������ס���дӵڶ��п�ʼ
//			columnModelIndex = mainTable.getColumnModelIndex(aColumnIden);
//		}
//		return columnModelIndex;
//
//	}
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
}