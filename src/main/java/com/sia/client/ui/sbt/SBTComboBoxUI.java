package com.sia.client.ui.sbt;

import com.sia.client.config.SiaConst;
import com.sia.client.config.UiFunc;
import com.sia.client.config.Utils;
import com.sia.client.model.SelectionItem;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class SBTComboBoxUI extends BasicComboBoxUI {
	private JComboBox combo;

	public SBTComboBoxUI(JComboBox combo_) {
		super();
		combo = combo_;
		combo.setPreferredSize(new Dimension(100, 20));

	}

	/**
	 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) -- XFZ@2010-12-15
	 * 
	 * @param index
	 */
	public void setHighLightIndex(int index) {
		((MyComboPopup) popup).setHighLightIndex(index);
	}

	@Override
	protected ComboBoxEditor createEditor() {
		ComboBoxInputLineEditor editor = new ComboBoxInputLineEditor();
		return editor;
	}

	/**
	 * 清除选中行！XFZ@2010-12-２５
	 */
	public void clearHighLightIndex() {
		((MyComboPopup) popup).clearHighLightIndex();
	}

	/**
	 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) -- XFZ@2010-12-15
	 */
	public int getHighLightIndex() {
		return ((MyComboPopup) popup).getHighLightIndex();
	}

	/**
	 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) -- XFZ@2010-12-15
	 * 
	 */
	public Object getHighLightValue() {
		return ((MyComboPopup) popup).getHighLightValue();
	}

	/**
	 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) -- XFZ@2010-12-15
	 * 
	 */
	public void setHighLightValue(Object anObject) {
		((MyComboPopup) popup).setHighLightValue(anObject);
	}

	public ComboPopup getPopup() {
		return popup;
	}

	@Override
	protected ComboPopup createPopup() {
		MyComboPopup popup = new MyComboPopup(comboBox);
		popup.getAccessibleContext().setAccessibleParent(comboBox);
		return popup;
	}

	/**
	 * 模拟点击下拉框的按钮以显示下拉框内容，它与JComboBox.showPopup()类似，但某些情形下不知道何原因
	 * JComboBox.showPopup()不工作，故用此代替-- XFZ@2011-01-03
	 */
	public void triggerPopup() {
		arrowButton.doClick();
	}

	@Override
	protected SBTButton createArrowButton() {
		SBTButton theButton = SBTButton.createComboboxDropDownButton();
		theButton.setVerticalAlignment(SwingConstants.BOTTOM);
		return theButton;
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		listBox.setSelectionForeground(Color.WHITE);
		listBox.setSelectionBackground(new Color(49, 106, 197, 255));
		if (c instanceof SBTComboBox) {
			SBTComboBox comboBox = (SBTComboBox) c;
			if (comboBox.isAllowRightClick()) {
//				listBox.addMouseListener(getMouseEventListener());
			}
		}
	}



	public JList getListBox() {
		return listBox;
	}

	public static ComponentUI createUI(JComponent c) {
		return new SBTComboBoxUI((JComboBox) c);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	private class MyComboPopup extends BasicComboPopup {

		public MyComboPopup(final JComboBox combo) {
			super(combo);
		}

		/**
		 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) --
		 * XFZ@2010-12-15
		 * 
		 * @param index
		 */
		public void setHighLightIndex(int index) {
			list.setSelectedIndex(index);
		}

		/**
		 * hightLight序号为index的行，但没有选中！(setSelectedIndex()是选中某行) --
		 * XFZ@2010-12-15
		 */
		public void clearHighLightIndex() {
			list.clearSelection();
		}

		/**
		 * 返回被hightLight的行数
		 * 
		 */
		public int getHighLightIndex() {
			return list.getSelectedIndex();
		}

		/**
		 * 返回被hightLight的选项 -- XFZ@2010-12-17
		 * 
		 */
		public Object getHighLightValue() {
			return list.getSelectedValue();
		}

		/**
		 * 设置被hightLight的选项 -- PFW@2010-12-17
		 * 
		 */
		public void setHighLightValue(Object anObject) {
			list.setSelectedValue(anObject, true);
		}

		/**
		 * Appends the specified menu item to the end of this menu.
		 * 
		 * @param menuItem
		 *            the <code>JMenuItem</code> to add
		 * @return the <code>JMenuItem</code> added
		 */
		@Override
		public JMenuItem add(JMenuItem menuItem) {
			super.add(menuItem);
			return menuItem;
		}

		@Override
		public void show() {

			boolean isDisplayable_ = comboBox.isDisplayable();
			boolean isShowing_ = comboBox.isShowing();
			// 主要是将滚动条置顶
			// if(SBTUIManager.getComponentScreenLocation(comboBox)!= null ){
			// //这里需要判断,否则会找不到comboBox的screenLocation
			if (isDisplayable_ && isShowing_) {
				// java.awt.IllegalComponentStateException: component must be
				// showing on the screen to determine its location
				super.show();
				int index = getListIndex();
				if (index < 0) {
					index = 0;
				}
				Rectangle cellBounds = list.getCellBounds(index, index);
				if (scroller != null && scroller.getVerticalScrollBar() != null
						&& cellBounds != null) {
					scroller.getVerticalScrollBar().setValue(cellBounds.y);
				}

				int screenHeight = UiFunc.getScreenHeight();
				Point comboBoxPoint = comboBox.getLocationOnScreen();
				if ((screenHeight - comboBoxPoint.y) < this.getHeight()
						+ comboBox.getHeight()) {
					this.setLocation(new Point(comboBoxPoint.x, comboBoxPoint.y
							- this.getHeight()));// scroller.getHeight()-3*comboBox.getHeight()));
				} else {
					this.setLocation(new Point(comboBoxPoint.x, comboBoxPoint.y
							+ comboBox.getHeight()));
				}

			}
		}

		@Override
		protected void configureList() {
			super.configureList();
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			list.setSelectionForeground(SiaConst.Ui.COLOR_TEXT_SELECTION_FORE);
			list.setSelectionBackground(SiaConst.Ui.COLOR_TEXT_SELECTION_BCK);
		}

		private int getListIndex() {
			int i, c;
			Object obj;

			JTextField jtf = ((JTextField) comboBox.getEditor()
					.getEditorComponent());
			String sObject = jtf.getText();
			if (Utils.isBlank(sObject)) {
				return -1;
			}

			ComboBoxModel dataModel = comboBox.getModel();
//			sObject = ((SBTComboBox) this.comboBox).convertCase(sObject);
			for (i = 0, c = dataModel.getSize(); i < c; i++) {
				obj = dataModel.getElementAt(i);
				if (obj != null) {
					String txt_;
					if (obj instanceof SelectionItem) {
						txt_ = ((SelectionItem) obj).getDisplay();
					} else {
						txt_ = obj.toString();
					}
					if (txt_.startsWith(sObject)) {
						return i;
					}
				}
			}
			return -1;
		}

		@Override
		protected JList createList() {
			JList list = new JList(comboBox.getModel()) {
				private static final long serialVersionUID = 20090422L;

				@Override
				public void processMouseEvent(MouseEvent e) {
					if (e.isControlDown()) {
						// Fix for 4234053. Filter out the Control
						// Key from the list.
						// ie., don't allow CTRL key deselection.
						e = new MouseEvent((Component) e.getSource(),
								e.getID(), e.getWhen(), e.getModifiers()
										^ InputEvent.CTRL_MASK, e.getX(),
								e.getY(), e.getClickCount(), e.isPopupTrigger());
					}
					super.processMouseEvent(e);
				}

				@Override
				public String getToolTipText(MouseEvent event) {
					int index = locationToIndex(event.getPoint());
					if (index != -1) {
						Object value = getModel().getElementAt(index);
						ListCellRenderer renderer = getCellRenderer();
						Component rendererComp = renderer.getListCellRendererComponent(this, value,
										index, true, false);
						if (rendererComp.getPreferredSize().width > getVisibleRect().width) {
							return Utils.isBlank(value) ? null : value.toString();
						} else {
							return null;
						}
					}
					return null;
				}
			};
			return list;
		}

		private static final long serialVersionUID = 20080229L;
	}

	public void addComponentToList(Component c) {
		BasicComboPopup popup = (BasicComboPopup) getPopup();
		popup.add(c);
	}

}
