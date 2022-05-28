package com.sia.client.ui.sbt;

import com.sia.client.config.Utils;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.comps.ActionableOnChanged;
import com.sia.client.ui.comps.CompValueChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SBTComboBox<K,T extends SelectionItem<K>> extends JComboBox<T> implements ActionableOnChanged {

	public static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor";
	public static final String CONTAINING_TABLE = "CONTAINING_TABLE";
	protected int maximumRowCount = 15;
	protected T promptInput;
	protected T selectAll;
	private boolean isRequired = false;
	private boolean isNewItemAllowed = false;
	private boolean shouldFireItemStateChanged = true;
	private boolean toFireValueChangedEvent = true; // toFireValueChangedEvent==trueʱ��fireValueChangedEvent()�Żᱻ����
	private final List<ValueChangedListener> valueChangedListeners = new ArrayList<>();
	private DefaultComboBoxListener defaultListener;
	private boolean shouldHaveBlankItem = true;
	private Map<? extends Serializable, ? extends Serializable> itemMap;
	private boolean allowRightClick = false;
	private SelectionItemComparator.ComparingPart selectionItemComparingPart;
	private final SelectionConvertor<K,T> selectionConvertor;
	private static final long serialVersionUID = 20070128L;

	static {
		UIManager.put(SBTComboBoxUI.uiClassID, SBTComboBoxUI.class.getName());
	}

	public SBTComboBox(SelectionConvertor<K,T> selectionConvertor) {
		this.selectionConvertor = selectionConvertor;
		init();
		FilterComboBoxModel<K,T> model = new FilterComboBoxModel<>(this.selectionConvertor);
		setModel(model);
	}
	public SelectionConvertor<K,T> getSelectionConvertor() {
		return selectionConvertor;
	}
	public void addElement(List<T> elements) {
		FilterComboBoxModel<K,T> model = (FilterComboBoxModel<K,T>)getModel();
		for(T ele: elements) {
			model.addElement(ele);
		}
	}
	/**
	 * 
	 * This method is public as an implementation side effect. do not call or
	 * override.
	 * -- XFZ@2013-11-27
	 */
	@Override
	public final void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	public Object getHighLightValue() {
		SBTComboBoxUI theUI_ = (SBTComboBoxUI) this.getUI();
		return theUI_.getHighLightValue();
	}
	public boolean isAllowRightClick() {
		return allowRightClick;
	}

	public void setAllowRightClick(boolean allowRightClick) {
		this.allowRightClick = allowRightClick;
	}
	public boolean isNewItemAllowed() {
		return isNewItemAllowed;
	}
	public T getBlankItem() {
		if (this.isRequired) {
			return promptInput;
		} else {
			return selectionConvertor.makeNewSelectionItem(SelectionItem.SELECT_BLANK_KEY,"");
		}
	}
	public T getSelectAllItem() {
		if ( null == selectAll) {
			selectAll = selectionConvertor.makeNewSelectionItem(SelectionItem.SELECT_ALL_KEY,"Select All");
		}
		return selectAll;
	}
	public void setIsNewItemAllowed(boolean isNewItemAllowed) {
		this.isNewItemAllowed = isNewItemAllowed;
	}
	public void setHorizontalAlignment(int alignment) {

		Component editComponent = this.getEditor().getEditorComponent();
		if (editComponent instanceof JTextField) {
			JTextField textField = (JTextField) editComponent;
			textField.setHorizontalAlignment(alignment);
		}
	}

	@Override
	public void setFont(Font f) {
		super.setFont(f);
		ComboBoxEditor editor = getEditor();
		if (editor != null) {
			Component ec = editor.getEditorComponent();
			if (ec != null) {
				ec.setFont(f);
			}
		}
	}

	@Override
	public void setSelectedItem(Object anObject) {
		setSelectedItem(anObject, true);
	}
	protected void setSelectedItem(Object anObject, boolean shouldFireItemStateChanged) {
		final T selectionItem;
		if (anObject instanceof SelectionItem) {
			if (((SelectionItem<?>) anObject).isValueBlank()) {
				selectionItem = getBlankItem();
			} else {
				selectionItem = (T)anObject;
			}
		} else if (Utils.isBlank(anObject)) {
			T selectionAllItem = getSelectAllItem();
			if (this.containsSelectAllItem() && Utils.isBlank(selectionAllItem.getDisplay())) {
				selectionItem = selectionAllItem;
			} else {
				selectionItem = getBlankItem();
			}
		} else {
			throw new IllegalStateException("Can't convert object "+anObject+" to SelectionItem object");
		}
		this.shouldFireItemStateChanged = shouldFireItemStateChanged;
		super.setSelectedItem(selectionItem);
		String editorComponentToolTipText = getEditorComponentToolTipText(anObject);
		setEditorComponentToolTipText(editorComponentToolTipText);
	}
	//see original comments -- 03/20/2022
	protected String getEditorComponentToolTipText(Object selectedObject) {
		return null;
	}

	private void setEditorComponentToolTipText(String toolTipText) {
		Component editorComp = this.getEditor().getEditorComponent();
		if (editorComp instanceof JTextField) {
			((JTextField) editorComp).setToolTipText(toolTipText);
		}
	}

	@Override
	protected void fireItemStateChanged(ItemEvent e) {

		if (this.shouldFireItemStateChanged) {
			super.fireItemStateChanged(e);
		}
	}

	public void setWidth(int length) {
		this.setSize(length, this.getHeight());
	}

	private void init() {
		selectionItemComparingPart = SelectionItemComparator.ComparingPart.KEY;
		setRenderer(new SBTComboboxRender(this));
		setHorizontalAlignment(JTextField.LEFT);
		installAncestorListener();
		setDefaultStatus();
		setAlignmentX(0.5f);
		installListeners(getDefaultListener());
	}

	protected void setDefaultStatus() {
		setEnabled(true);
		setDefaultInputStatus();
	}
	public SBTComboBox<K,T> withComparingPart(SelectionItemComparator.ComparingPart comparingPart) {
		selectionItemComparingPart = comparingPart;
		return this;
	}
	public SBTComboBox<K,T> withPromptInput(T promptInput) {
		this.promptInput = promptInput;
		return this;
	}
	public void installListeners(DefaultComboBoxListener cbl) {
		if (defaultListener != null) {
			removeFocusListener(defaultListener);
			removeKeyListener(defaultListener);
			removeMouseListener(defaultListener);
			removeItemListener(defaultListener);
		}

		addFocusListener(cbl);
		addKeyListener(cbl);
		addMouseListener(cbl);
		addItemListener(cbl);

		defaultListener = cbl;
	}
	protected void setDefaultInputStatus() {
		setEditable(false);// �����������༭
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.removeMouseListener(l);
			editorComp_.addMouseListener(l);
		}
		super.removeMouseListener(l);
		super.addMouseListener(l);
	}

	@Override
	public synchronized void removeMouseListener(MouseListener l) {
		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.removeMouseListener(l);
		}
		super.removeMouseListener(l);
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {

		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.removeKeyListener(l);
			editorComp_.addKeyListener(l);
		}
		super.removeKeyListener(l);
		super.addKeyListener(l);
	}

	@Override
	public synchronized void removeKeyListener(KeyListener l) {

		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.removeKeyListener(l);
		}
		super.removeKeyListener(l);
	}

	@Override
	public synchronized void addFocusListener(FocusListener l_) {

		removeFocusListener(l_);
		//see original comments -- 03/20/2022
		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.addFocusListener(l_);
		}

		super.addFocusListener(l_);
	}

	@Override
	public synchronized void removeFocusListener(FocusListener l_) {
		if (editor != null && editor.getEditorComponent() != null) {
			Component editorComp_ = editor.getEditorComponent();
			editorComp_.removeFocusListener(l_);
		}
		super.removeFocusListener(l_);
	}
	public void fireValueChangedEvent() {

		if (!toFireValueChangedEvent) {// see original comments -- 03/21/2022
			return;
		}else if (null == valueChangedListeners || 0 == valueChangedListeners.size()){
			return;
		}

		boolean togger = ((FilterComboBoxModel<K,T>) dataModel).getChangeContentsTogger();
		if (!togger){
			return;
		}

		T newValue_;
		T selectedItem_ = (T)getSelectedItem();

		if (selectedItem_ == null) {
			newValue_ = selectionConvertor.makeNewSelectionItem(SelectionItem.SELECT_BLANK_KEY, "");
		} else {
			newValue_ = selectedItem_;
		}

		T oldValue = getOldValue();
		if (null == oldValue || newValue_.getKeyValue() != oldValue.getKeyValue()) {
			ValueChangedEvent e_ = new ValueChangedEvent(this);

			e_.setValue(newValue_);
			if (newValue_ == null){
				e_.setNewValueString("");
			}else{
				e_.setNewValueString(String.valueOf(newValue_.getKeyValue()));
			}

			if (oldValue == null){
				e_.setOldValueString("");
			}else{
				e_.setOldValueString(String.valueOf(oldValue.getKeyValue()));
			}
			//commented out on 03/21/2022
//			e_.setSourceTableCellEditor(this.isTableCellEditor());
			e_.setProperty_name(this.getName());
			for (ValueChangedListener valueChangedListener : valueChangedListeners) {
				valueChangedListener.processValueChangedEvent(e_);
			}
		}
	}
	public void addValueChangeListener(ValueChangedListener valueChangedListener) {
		valueChangedListeners.add(valueChangedListener);
	}
	public T getOldValue() {
		FilterComboBoxModel<K,T> theModel = (FilterComboBoxModel<K,T>) this.getModel();
		Object rtn = theModel.getOldValue();
		return convertObjectToSBTSelectionItem(rtn);
	}
	private T convertObjectToSBTSelectionItem(Object o_) {

		T rtn;
		if (o_ == null) {
			rtn = getBlankItem();
		} else if (o_ instanceof SelectionItem) {
			rtn = (T) o_;
		} else {
			String str_value_ = o_.toString();
			String caseConvertedText_ = convertCase(str_value_);
			rtn = selectionConvertor.makeNewSelectionItem(caseConvertedText_, caseConvertedText_);
		}

		return rtn;
	}
	protected String convertCase(String str) {
		return str;
	}
	public int resetRange(Object item) {

		FilterComboBoxModel<K,T> dm_ = (FilterComboBoxModel<K,T>) dataModel;
		//see original comments -- 03-21/2022
		dm_.setChangeContentsTogger(false);
		dm_.setSelectedItem(item);
		dm_.setChangeContentsTogger(true);

		int firstMatchedSelection; // ��һ��������ƥ����к�
		if (item instanceof SelectionItem) {
			firstMatchedSelection = dm_.getFirstMatchedItemIndex(((SelectionItem<?>) item).getDisplay());
		} else {
			firstMatchedSelection = dm_.getFirstMatchedItemIndex(String.valueOf(item));
		}

		hidePopup();
		showPopup();

		if (firstMatchedSelection >= 0) {
			this.setHighLightIndex(firstMatchedSelection);
		} else {
			this.clearHighLightIndex();
		}

		return firstMatchedSelection;

	}
	public List<T> convertToSelectItems( Map<?, ?> items) {

		List<T> rtn_;
		if (items == null || items.size() == 0) {
			rtn_ = new ArrayList<>(0);
		} else {
			rtn_ = new ArrayList<>(items.size());
			Set<?> keySet_ = items.keySet();
			for (Object key_ : keySet_) {
				if (key_ instanceof SelectionItem) {
					rtn_.add((T) key_);
					continue;
				}
				Object value_ = items.get(key_);
				if (value_ instanceof SelectionItem) {
					rtn_.add((T) value_);
					continue;
				}

				T item_ = selectionConvertor.makeNewSelectionItem(key_.toString(), String.valueOf(value_));
				rtn_.add(item_);

			}
			SelectionItemComparator comparator = new SelectionItemComparator(selectionItemComparingPart);
			rtn_.sort(comparator);
		}
		return rtn_;
	}
	//see original comments -- 03/20/2022
	public List<T> convertToSelectItems( Serializable[][] items) {

		List<T> vec = new ArrayList<>();
		if (items != null) {
			for (Serializable[] item : items) {
				T selectItem;
				Object value = item[1];
				if (value instanceof SelectionItem) {
					selectItem = (T) value;
				} else {
					Serializable key = item[0];
					if (key == null) {
						Exception e = new Exception("Blank key value found.");
						Utils.log(e);
						continue;
					}
					String display;
					if (value == null) {
						display = getBlankItem().getDisplay();
					} else {
						display = value.toString();
					}
					selectItem = selectionConvertor.makeNewSelectionItem(key.toString(), display);
				}
				vec.add(selectItem);
			}
		}
		return vec;
	}
	//see original comments -- 03/20/2022
	public void InsertItemAt(Object key, String display) {
		if (display == null) {
			display = "";
		}

		T select = selectionConvertor.makeNewSelectionItem(key.toString(), display);
		if (key == SelectionItem.SELECT_ALL_KEY) {
			selectAll = select;
		}
		((DefaultComboBoxModel<T>) this.getModel()).insertElementAt(select, 0);
	}
	public void insertItemAt(Object anObject, String display, int index) {
		T select = selectionConvertor.makeNewSelectionItem(String.valueOf(anObject), display);
		super.insertItemAt(select, index);
	}
	public boolean containsSelectAllItem() {
		return this.containsKey(SelectionItem.SELECT_ALL_KEY);
	}
	public boolean containsKey(Object key) {
		boolean rtn = false;

		FilterComboBoxModel<K,T> myModel = (FilterComboBoxModel<K,T>) this.getModel();
		int size = myModel.getOriginalSize();
		for (int i = 0; i < size; i++) {
			Object elementAtI = myModel.getElementFromOriginalModel(i);
			if (elementAtI == null) {
			} else if (elementAtI instanceof SelectionItem) {
				T item = (T) elementAtI;
				if (item.getKeyValue() != null && item.getKeyValue().equals(key)) {
					rtn = true;
					break;
				}
			} else {
				rtn = elementAtI.equals(key);
				if (rtn) {
					break;
				}
			}
		}
		return rtn;
	}
	protected DefaultComboBoxListener getDefaultListener() {
		if (defaultListener == null) {
			defaultListener = new DefaultComboBoxListener(this);
		}

		return defaultListener;
	}
	//see original comments -- 03/21/2022
	public void setHighLightIndex(int index) {
		SBTComboBoxUI theUI_ = (SBTComboBoxUI) this.getUI();
		theUI_.setHighLightIndex(index);
	}
	//clear selected hightlight
	public void clearHighLightIndex() {
		SBTComboBoxUI theUI_ = (SBTComboBoxUI) this.getUI();
		theUI_.clearHighLightIndex();
	}
	@Override
	public void addListener(CompValueChangedListener l) {
		this.addItemListener(l);
	}
	@Override
	public void setValue(Object obj) {
		T value = convertObjectToSBTSelectionItem(obj);
		setSelectedItem(value);
	}
	@Override
	public Object getValue() {
		return getSelectedItem();
	}
}