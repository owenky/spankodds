package com.sia.client.ui.sbt;

import com.sia.client.config.SiaConst;
import com.sia.client.config.UiFunc;
import com.sia.client.config.Utils;
import com.sia.client.model.PatternValidator;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.EventListener;

public class SBTTextField extends JFormattedTextField{

	public SBTTextField() {
		this(null, 0);
	}

	public SBTTextField(String s_) {
		this(s_, 0);
	}

	public SBTTextField(int length_) {
		this(null, length_);
	}

	public SBTTextField(String text, int length) {
		super(text);
		this.setColumns(length);
		init();
	}

	private void init() {
		this.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		this.setOpaque(false);
	}

	public void setPatternValidator(PatternValidator pv) {
		this.validator = pv;
	}

	@Override
	public void paste() {
		String oldv = getText();
		super.paste();
		String newv = getText();
		String clean_text_ = newv.replaceAll("[\n\r]", " ");// see original comments -- 03/20/2022
		if (! Utils.isSameObject(newv, clean_text_)) {
			setText(clean_text_);
			newv = clean_text_;
		}

		if (!validatePaste(newv)) {
			UiFunc.beep();
			UiFunc.showDialogAndWait("Error", newv + " is not digit.");
			setText(oldv);
		}
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	private boolean validatePaste(String str) {
		if (validator == null) {
			return true;
		}
		return validator.validate(str);
	}
	// see original comments -- 03/20/2022
	public void setInputErrorPrompt(String inputErrorPrompt) {
		this.inputErrorPrompt = inputErrorPrompt;
	}

	public String getErrorMessage() {
		if (this.isEditValid()) {
			return "";
		} else {
			return this.getText()+" is not valid.<br>" + inputErrorPrompt;
		}
	}

	@Override
	public void setEnabled(boolean b) {
		if (b) {
			super.setEnabled(b);
		}
	}

	@Override
	protected void invalidEdit() {
		UiFunc.showDialogAndWait("Error", getErrorMessage());
		super.invalidEdit();
	}
	protected boolean containsListener(EventListener[] listeners,
			EventListener l) {
		boolean rtn = false;
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				if (listeners[i].equals(l)) {
					rtn = true;
					break;
				}
			}
		}
		return rtn;
	}

	private static final int string_length_adjusted = 4;

	@Override
	public void setText(String text) {
		if (!adjustedDisplayMode) {
			super.setText(text);
		} else {
			StringBuffer sb = new StringBuffer(text);
			Font textFont = this.getFont();
			int lineWidth = this.getWidth();
			int textWidth = UiFunc.getStringWidth(textFont, text);
			if (textWidth > lineWidth) {
				sb.append("...");
				int length = sb.length() - string_length_adjusted;
				while (textWidth > lineWidth && length >= 0) {
					sb.deleteCharAt(length);
					textWidth = UiFunc.getStringWidth(textFont, sb.toString());
					length--;
				}
			}
			this.setHorizontalAlignment(SwingConstants.CENTER);
			super.setText(sb.toString());

		}
	}

	@Override
	public synchronized void addActionListener(ActionListener listener) {

		if (!containsListener(getActionListeners(), listener)) {
			super.addActionListener(listener);
		}
	}

	@Override
	public synchronized void addKeyListener(KeyListener listener) {
		if (!containsListener(getKeyListeners(), listener)) {
			super.addKeyListener(listener);
		}
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		if (!containsListener(getMouseListeners(), l)) {
			super.addMouseListener(l);
		}
	}

	@Override
	public void addCaretListener(CaretListener listener) {
		if (!containsListener(getCaretListeners(), listener)) {
			super.addCaretListener(listener);
		}
	}

	@Override
	public synchronized void addFocusListener(FocusListener listener) {
		FocusListener[] focus_listeners_ = getFocusListeners();
		if (focus_listeners_ == null
				|| !containsListener(focus_listeners_, listener)) {
			super.addFocusListener(listener);
		}
	}


	public Point getErrorMessagePopupLocation() {
		return error_message_pop_up_location;
	}

	// see original comments -- 03/20/2022
	public void setErrorMessagePopupLocation(Point error_message_pop_up_location) {
		this.error_message_pop_up_location = error_message_pop_up_location;
	}

	public void setAdjustedDisplayMode(boolean b) {
		adjustedDisplayMode = b;
	}

	private boolean adjustedDisplayMode = false;
	private String inputErrorPrompt = "Invalid format";
	private PatternValidator validator;
	private Point error_message_pop_up_location;
	private FocusListener defaultFocusEventListener;
	private MouseListener defaultMouseEventListener;
	private static final long serialVersionUID = 20090625L;
	private static final String uiClassID = "MetalTextFieldUI";
	static { // see original comments -- 03/20/2022
		UIManager.put(uiClassID, MetalTextFieldUI.class.getName());
	}

	// see original comments -- 03/20/2022
	public FocusListener getDefaultFocusEventListener() {
		if (null == defaultFocusEventListener) {
			defaultFocusEventListener = new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {

					JTextComponent theComponent = SBTTextField.this;
					if (theComponent.isEditable()) {
						theComponent.selectAll();
					}
				}
				@Override
				public void focusLost(FocusEvent e) {

					JTextComponent theComponent = SBTTextField.this;
					theComponent.setBackground(SiaConst.Ui.COLOR_WINDOW_BCK);

					if (theComponent.isEditable()) {
						try {
							commitEdit();
						} catch (ParseException pe) {
							invalidEdit();
						}
					}
				}
			};
		}
		return defaultFocusEventListener;
	}

	private MouseListener getDefaultMouseEventListener() {
		if (null == defaultMouseEventListener) {
			defaultMouseEventListener = new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if ( UiFunc.isSpecialKeyDown(e)) {
						if (e.getClickCount() > 1) {
							// 双击就全选
							selectAll();
						} else {
							// 单击不选
							requestFocus();
							int caretposition_ = getCaretPosition();
							if (caretposition_ >= 0) {
								select(caretposition_, caretposition_);
								setCaretPosition(caretposition_);
							}
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			};
		}
		return defaultMouseEventListener;

	}
}