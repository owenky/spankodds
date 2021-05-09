package com.sia.client.ui;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

public class RowHeaderTable extends JTable {

	private final ColumnLockableTable mainTable;
	private boolean toFireChangesInMainTable = true;
	private final boolean hasRowNumber;
	private static final long serialVersionUID = 20091228L;

	public RowHeaderTable(ColumnLockableTable mainTable,boolean hasRowNumber) {
		this.hasRowNumber = hasRowNumber;
		this.mainTable = mainTable;
		((RowHeaderColumnModel)this.getColumnModel()).setMainTable(mainTable);
		this.setModel(mainTable.getModel());
		this.setAutoCreateColumnsFromModel(false);
//		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setOpaque(false);
	}
	public ColumnLockableTable getMainTable(){
		return mainTable;
	}
	@Override
	public int getRowHeight() {
		return mainTable.getRowHeight();
	}
	@Override
	public int getRowHeight(int row_) {
		return mainTable.getRowHeight(row_);
	}
	@Override
	public boolean getShowHorizontalLines() {
		return mainTable.getShowHorizontalLines();
	}
	@Override
	public Dimension getIntercellSpacing() {
		return mainTable.getIntercellSpacing();
	}
	@Override
	public Color getGridColor() {
		if ( null != mainTable) {
			return mainTable.getGridColor();
		} else {
			return super.getGridColor();
		}
	}
	@Override
	public Font getFont() {
		if ( null != mainTable) {
			return mainTable.getFont();
		} else {
			return super.getFont();
		}
	}
	@Override
	public int getRowMargin() {
		return mainTable.getRowMargin();
	}
	@Override
	public boolean getAutoCreateColumnsFromModel() {
		return true;
	}
	@Override
	public void setModel(TableModel dataModel) {
		super.setModel(dataModel);
		dataModel.removeTableModelListener(this);
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
				cm.addColumn( mainTable.getColumnFromModel(tcIndex));
			}
		}
	}
	public void setToFireChangesInMainTable(boolean b) {
		toFireChangesInMainTable = b;
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
		if ( column == 0 ){
			return row;
		}else{
			rtn = super.getValueAt(row,column);
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
//	@Override
//    public int convertRowIndexToModel(int index_) {
//		int rtn_ = mainTable.convertRowIndexToModel(index_);
//		return rtn_;
//    }
//	@Override
//    public int convertRowIndexToView(int index_) {
//		int rtn_ = mainTable.convertRowIndexToView(index_);
//		return rtn_;
//    }
//	@Override
//	public void setCursor(Cursor cursor) {
//		/**
//		 * ��RowHeaderTableֱ������cursor���ᵼ��mainTable�ı�ͷ���кš������еȣ���ʧ -- XFZ@2014-08-17
//		 */
//		mainTable.setCursor(cursor);
//	}
}
