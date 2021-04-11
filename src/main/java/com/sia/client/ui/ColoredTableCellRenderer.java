package com.sia.client.ui;

import com.sia.client.model.ColorData;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

public class ColoredTableCellRenderer extends DefaultTableCellRenderer {
	
	// this doesn't work
	 public Component getTableCellRendererComponent(
               JTable table,
               java.lang.Object value,
               boolean isSelected,
               boolean hasFocus,
               int row, int column) {
      if( !isSelected ) {
         Color c = table.getBackground();
         if( (row%2)==0 &&
                 c.getRed()>10 && c.getGreen()>10 && c.getBlue()>10 )
            setBackground(new Color( c.getRed()-10,
                                     c.getGreen()-10,
                                     c.getBlue()-10));
         else
            setBackground(c);
      }
      return super.getTableCellRendererComponent( table,
                                value,isSelected,hasFocus,row,column);
   }

  public void setValue(Object value) {
    if (value instanceof ColorData) {
      ColorData cvalue = (ColorData)value;
      setForeground(cvalue.m_color);
      setText(cvalue.m_data.toString());
    }
    else if (value instanceof IconData) {
      IconData ivalue = (IconData)value;
      setIcon(ivalue.m_icon);
      setText(ivalue.m_data.toString());
	  setHorizontalTextPosition(SwingConstants.LEADING);
    }
	 else if (value instanceof LineData) {
      LineData ivalue = (LineData)value;
      setIcon(ivalue.m_icon);
      setText(ivalue.m_data.toString());
	  setHorizontalTextPosition(SwingConstants.LEADING);
    }

    else
      super.setValue(value);
  }
}