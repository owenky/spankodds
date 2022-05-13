package com.sia.client.ui.sbt;


import com.sia.client.model.SelectionItem;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class SBTComboboxRender extends BasicComboBoxRenderer {
	// private final SBTComboBox parentComboBox;
	private static final long serialVersionUID = 20111221L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		if ( value instanceof SelectionItem)
			value = ((SelectionItem<?>)value).getDisplay();
		
		return super.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
	}

	public SBTComboboxRender(SBTComboBox parentComboBox_) {
		// parentComboBox =parentComboBox_;
		//commented out on 03/21/2022
//		addMouseListener(getDefaultMouseListener());
	}
//
//	public AsynMouseEventListener getDefaultMouseListener() {
//		return new AsynMouseEventListener() {
//
//			@Override
//			protected void mousePressedNormal(MouseEvent e) {
//				super.mousePressedNormal(e);
//			}
//
//		};
//	}
//
//

}