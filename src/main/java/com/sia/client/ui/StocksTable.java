package com.sia.client.ui; /**
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */ 

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class StocksTable extends JFrame {

  public JTable m_table;
  public StockTableData m_data;
  public JLabel m_title;

JScrollPane ps = new JScrollPane();
	public JTable getTable()
	{
		return m_table;
	}
	public void setTable(JTable t)
	{
		m_table = t;
	}
	public StockTableData getData()
	{
		return m_data;
	}
	public void setData(StockTableData t)
	{
		m_data = t;
	}	
	
	public JScrollPane getScrollPane()
	{
		return ps;
	}
	public void setScrollPane(JScrollPane sp)
	{
		ps = sp;
	}

  public StocksTable() {
    super("Stocks Table");
    setSize(600, 300);

	UIManager.put("Table.focusCellHighlightBorder",
		new LineBorder(Color.black, 0));

    m_data = new StockTableData();

    m_title = new JLabel(m_data.getTitle(),
      new ImageIcon("money.gif"), SwingConstants.CENTER);
    m_title.setFont(new Font("Helvetica",Font.PLAIN,24));
    getContentPane().add(m_title, BorderLayout.NORTH);

    m_table = new JTable();
    m_table.setAutoCreateColumnsFromModel(false);
	m_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    m_table.setModel(m_data);

    for (int k = 0; k < m_data.getColumnCount(); k++) {
      DefaultTableCellRenderer renderer = new
              ColoredTableCellRenderer();
      renderer.setHorizontalAlignment(
        StockTableData.m_columns[k].m_alignment);
      TableColumn column = new TableColumn(k,
      StockTableData.m_columns[k].m_width, renderer, null);
      m_table.addColumn(column);
    }

    JTableHeader header = m_table.getTableHeader();
    header.setUpdateTableInRealTime(false);

	
	//	FrozenTablePane ps = new FrozenTablePane(m_table,1);
    
    ps.getViewport().setBackground(m_table.getBackground());
    ps.getViewport().add(m_table);
    getContentPane().add(ps, BorderLayout.CENTER);
	
	
	FixedColumnTable fct = new FixedColumnTable(2, ps);
	m_table = fct.getFixedTable();
	
  }

  public static void main(String argv[]) {
	StocksTable frame = new StocksTable();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
  }
}










