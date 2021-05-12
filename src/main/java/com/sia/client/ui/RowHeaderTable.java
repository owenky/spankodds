package com.sia.client.ui;

import com.sia.client.model.TableCellRendererProvider;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
//		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setOpaque(false);
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
			headerCellRenderer = new ColumnHeaderCellRenderer(tableCellRendererProvider, mainTable.getColumnHeaderProvider());
		}
		return headerCellRenderer;
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
				cm.addColumn( mainTable.getColumnFromDataModel(tcIndex));
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
//		if ( modelColumnIndex == SBTTableModelDelegator.RowNoColumnIndex ) { //是序号列
//			columnViewIndex = 0;
//		}else if ( modelColumnIndex < 0 ) {
//			columnViewIndex = -1;
//		}else{
//			JTableColumn aColumnIden = mainTable.getColumnFromTableModel(modelColumnIndex);
//			List<JTableColumn> lockedColumns = mainTable.getLockColumns();
//			columnViewIndex = lockedColumns.indexOf(aColumnIden);
//			if ( columnViewIndex >=0 ){
//				columnViewIndex = columnViewIndex + 1; // 第一列是行号，主表锁住的列从第二列开始
//			}
//		}
//
//		return columnViewIndex;
//
//	}
//	@Override
//	public int convertColumnIndexToModel(int viewColumnIndex){
//		int columnModelIndex;
//		if ( viewColumnIndex == 0 ) { //是序号列
//			columnModelIndex =  SBTTableModelDelegator.RowNoColumnIndex;
//		}else{
//			List<JTableColumn> lockedColumns = mainTable.getLockColumns();
//			JTableColumn aColumnIden = lockedColumns.get(viewColumnIndex - 1); // 第一列是行号，主表锁住的列从第二列开始
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
//	@Override
//	public void setCursor(Cursor cursor) {
//		/**
//		 * 对RowHeaderTable直接设置cursor，会导致mainTable的表头（行号、操作列等）消失 -- XFZ@2014-08-17
//		 */
//		mainTable.setCursor(cursor);
//	}
}
