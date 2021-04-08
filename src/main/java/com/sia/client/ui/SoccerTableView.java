package com.sia.client.ui;

import javax.swing.*;
import javax.swing.table.*;

public class SoccerTableView extends JTable
  {
	    public TableCellRenderer getCellRenderer(int row, int column)  
			
			{
			return new LineRenderer("soccer");
			}
	 
  
	
  }