package com.sia.client.ui;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.FontMetrics;

public class RowHeaderColumnModel extends DefaultTableColumnModel {
	
	public static final String DEFAULT_ROW_NUMBER_HEADER = "No.";
	private JTable mainTable;
 	private String row_number_header;
	private TableColumn theRowNoColumn;
	private static final long serialVersionUID = 20110510L;

	public RowHeaderColumnModel() {
	}
	public void setMainTable(JTable mainTable_){
		this.mainTable = mainTable_;
		row_number_header = DEFAULT_ROW_NUMBER_HEADER;
	}
	@Override
	public void setColumnMargin(int columnMargin) {
		//don't set column margin, use main table's column margin.
	}
	/**
	 * 设置表格行号列的标题(如序号或设置等,以及二维表的" 颜色 / 尺寸" -- XFZ@2013-10-17
	 */
	public void setRowNumberHeader(String row_number_header_){
		row_number_header = row_number_header_;
		TableColumn rowNuoCol_ = getRowNoColumn();
		rowNuoCol_.setHeaderValue(row_number_header_);
		calculateColumnWidth();
	}
	public TableColumn getRowNoColumn(){
		if ( theRowNoColumn == null){
			theRowNoColumn = new TableColumn(0);
			theRowNoColumn.setHeaderValue(getRowNumberHeader());
			calculateColumnWidth();
		}
		return theRowNoColumn;
	}
	private void calculateColumnWidth(){
		int colwidth = getRowNoColumnWidth();
		theRowNoColumn.setWidth(colwidth);
		theRowNoColumn.setMaxWidth(colwidth);
		theRowNoColumn.setMinWidth(colwidth);
	}
	private int getRowNoColumnWidth() {

		if ( mainTable == null){
			return 0;
		}
		int rowcount = mainTable.getRowCount();
		JLabel sample = new JLabel();
		FontMetrics fm = sample.getFontMetrics(mainTable.getFont());
		int colwidth1 = SwingUtilities.computeStringWidth(fm,String.valueOf(rowcount));
		int colwidth2 = SwingUtilities.computeStringWidth(fm, getRowNumberHeader());

		int colwidth = Math.max(colwidth1, colwidth2);

		return (int) (colwidth * 1.25) + 10;
	}
	public String getRowNumberHeader(){
		return row_number_header;
	}
	@Override
	public TableColumn getColumn(int columnIndex) {
//		return tableColumns.elementAt(columnIndex);
		return super.getColumn(columnIndex);
	}
	@Override
	public void addColumn(TableColumn aColumn) {
        super.addColumn(aColumn);
    }
	@Override
	public int getColumnCount(){
		int rtn = super.getColumnCount();
		return rtn;
	}
	
	/**
     * Returns the index of the column that lies at position <code>x</code>,
     * or -1 if no column covers this point.
     *
     * In keeping with Swing's separable model architecture, a
     * TableColumnModel does not know how the table columns actually appear on
     * screen.  The visual presentation of the columns is the responsibility
     * of the view/controller object using this model (typically JTable).  The
     * view/controller need not display the columns sequentially from left to
     * right.  For example, columns could be displayed from right to left to
     * accomodate a locale preference or some columns might be hidden at the
     * request of the user.  Because the model does not know how the columns
     * are laid out on screen, the given <code>xPosition</code> should not be
     * considered to be a coordinate in 2D graphics space.  Instead, it should
     * be considered to be a width from the start of the first column in the
     * model.  If the column index for a given X coordinate in 2D space is
     * required, <code>JTable.columnAtPoint</code> can be used instead.
     *
     * @param  x  the horizontal location of interest
     * @return	the index of the column or -1 if no column is found
     * @see javax.swing.JTable#columnAtPoint
     */
	@Override
    public int getColumnIndexAtX(int x) {
		return super.getColumnIndexAtX(x);
    }
}