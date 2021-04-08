package com.sia.client.ui;

import javax.swing.*;
 import javafx.scene.paint.Color;
 import java.awt.Component;
 //public class LineListCellRenderer implements ListCellRenderer 
 public class LineListCellRenderer extends DefaultListCellRenderer 
 {
     public LineListCellRenderer() {
         
     }

	@Override
     public Component getListCellRendererComponent(JList list,
                                                   Object value,
                                                   int index,
                                                   boolean isSelected,
                                                   boolean cellHasFocus) 
	   {
		//setHorizontalAlignment(SwingConstants.RIGHT);
		JLabel component = (JLabel) value;
		//component.setHorizontalTextPosition(SwingConstants.RIGHT);
		
		
         //component.setText(value.toString());
		 //component.setIcon(value.getIcon());
		 
		return component;
/*
         Color background;
         Color foreground;

         // check if this cell represents the current DnD drop location
         JList.DropLocation dropLocation = list.getDropLocation();
         if (dropLocation != null
                 && !dropLocation.isInsert()
                 && dropLocation.getIndex() == index) {

             background = Color.BLUE;
             foreground = Color.WHITE;

         // check if this cell is selected
         } else if (isSelected) {
             background = Color.RED;
             foreground = Color.WHITE;

         // unselected, and not the DnD drop location
         } else {
             background = Color.WHITE;
             foreground = Color.BLACK;
         };

         setBackground(background);
         setForeground(foreground);
*/
         
     }
 }