/*
 * @(#)SBTViewport.java	
 * XFZ@2008-02-11
 */

package com.sia.client.ui.sbt;

import com.sia.client.config.SiaConst;

import javax.swing.*;
import java.awt.*;

public class SBTComboBoxInputField extends SBTTextField {

	public SBTComboBoxInputField() {
		init();
	}

	public SBTComboBoxInputField(String s_) {
		super(s_);
		init();
	}

	public SBTComboBoxInputField(int length_) {
		super(length_);
		init();
	}

	public SBTComboBoxInputField(String text, int length) {
		super(text, length);
		init();
	}

	private void init() {
		setFocusable(true);
		underLineColor = SiaConst.Ui.COLOR_UNDERLINE;
		setSelectionColor(SiaConst.Ui.COLOR_TEXT_SELECTION_BCK);
		setSelectedTextColor(SiaConst.Ui.COLOR_TEXT_SELECTION_FORE);
		createUnderLine();
		setOpaque(false); // 默认设置为透明
	}

	private void createUnderLine() {
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, underLineColor));
	}

	@Override
	public String toString() {
		return getText();
	}

	public void setColor(Color theLineColor_) {
		underLineColor = theLineColor_;
		createUnderLine();
	}

	private Color underLineColor = null;
	private static final long serialVersionUID = 20080211L;

}
