package com.sia.client.ui.sbt;


import com.sia.client.config.Utils;
import com.sia.client.model.SelectionItem;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FilterComboBoxModel<K,T extends SelectionItem<K>> extends DefaultComboBoxModel<T> {

	private static final int NOT_SET = -1, NOT_FOUND = -2;
	private Map<String, T> displayLookupMap;
	private boolean isDataModelSBTSelectionItemType = true; // model的数据是否是SBTSelectionItem
	private int begin_index, end_index;
	private T oldValue;
	private boolean isToFireContentsChangedTogger = true;
	private boolean shouldSetOldValue = true;
	private final SelectionConvertor<K,T> selectionConvertor;

	public FilterComboBoxModel(SelectionConvertor<K,T> selectionConvertor) {
		this.selectionConvertor = selectionConvertor;
		init();
	}

	public FilterComboBoxModel(T[] arg0,SelectionConvertor<K,T> selectionConvertor) {
		super(arg0);
		this.selectionConvertor = selectionConvertor;
		init();
	}

	public FilterComboBoxModel(Vector<T> arg0, SelectionConvertor<K,T> selectionConvertor) {
		super(arg0);
		this.selectionConvertor = selectionConvertor;
		init();
	}

	public void setDataModelSBTSelectionItemType(boolean b) {
		isDataModelSBTSelectionItemType = b;
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
	public void addElement(T anObject) {
		super.addElement(anObject);
	}

	@Override
	public void insertElementAt(T anObject, int index) {

		if (isFilterSet()){
			super.insertElementAt(anObject, index + begin_index);
		} else {
			super.insertElementAt(anObject, index);
		}

		Map<String, T> displayLookupMap = getDisplayLookupMap();
		displayLookupMap.put(anObject.getDisplay(), anObject);
	}

	public boolean isDataModelSBTSelectionItemType() {
		return isDataModelSBTSelectionItemType;
	}

	private void init() {
		end_index = NOT_SET;
		begin_index = NOT_SET;
		isDataModelSBTSelectionItemType = true;
		oldValue = selectionConvertor.makeNewSelectionItem(SelectionItem.SELECT_BLANK_KEY,"");
	}

	public void resetDisplayLookupMap() {
		displayLookupMap = null;
	}

	public T getValueByDisplay(String display_) {
		Map<String, T> displayLookupMap = getDisplayLookupMap();
		return displayLookupMap.get(display_);
	}

	private Map<String, T> getDisplayLookupMap() {
		if (displayLookupMap == null) {
			int size = getSize();
			displayLookupMap = new HashMap<String, T>(size);

			for (int i = 0; i < size; i++) {
				T item = this.getElementAt(i);
				String itemDisplay = item.getDisplay();
				displayLookupMap.put(itemDisplay, item);
			}
		}
		return displayLookupMap;
	}

	@Override
	public int getSize() {
		int rtn_;
		if (isFilterSet()) {
			rtn_ = (end_index - begin_index + 1);
		}else{
			rtn_ = super.getSize();
		}
		return rtn_;
	}

	@Override
	public T getElementAt(int i) {
		T rtn_;
		boolean isFilterSet = isFilterSet();
		if (!isFilterSet)
			rtn_ = super.getElementAt(i);
		else {
			rtn_ = super.getElementAt(begin_index + i);
		}

		return rtn_;
	}

	public Object getElementFromOriginalModel(int i) {
		return super.getElementAt(i);
	}

	public int getOriginalSize() {
		return super.getSize();
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 * 
	 * @param anObject
	 * @return an int representing the index position, where 0 is the first
	 *         position
	 */
	@Override
	public int getIndexOf(Object anObject) {

		int rtn_ = super.getIndexOf(anObject);
		if (isFilterSet()){
			rtn_ = (rtn_ - begin_index);
		}
		return rtn_;
	}

	public int getListIndexOf(Object anObject) {

		int rtn_ = super.getIndexOf(anObject);
		if (isFilterSet()){
			rtn_ = (rtn_ - begin_index);
		}
		return rtn_;
	}

	@Override
	public void removeElementAt(int index) {
		if (isFilterSet()){
			super.removeElementAt(index + begin_index);
		} else {
			super.removeElementAt(index);
		}
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * 
	 * @param anObject
	 *            The combo box value or null for no selection.
	 */
	@Override
	public void setSelectedItem(final Object anObject) {
		if (shouldSetOldValue) {
			oldValue = (T)this.getSelectedItem();
		}
		super.setSelectedItem((T)anObject);
	}


	public T getOldValue() {
		return oldValue;
	}

	public void setOldValue(T oldValue_) {
		oldValue = oldValue_;
	}

	public void reset() {
		begin_index = NOT_SET;
	}

	// ///////////////////////////////////////////////////////////////////////////////

	public int getBegin_index() {
		return begin_index;
	}

	public int getEnd_index() {
		return end_index;
	}

	//see original comments -- 03/20/2022
	public int setRange(T item) {
		if (item != null && item.getKeyValue() != SelectionItem.SELECT_BLANK_KEY) {
			String display = item.getDisplay();
			begin_index = setRange(display);
		} else {
			begin_index = NOT_SET;
			end_index = NOT_SET;
		}
		return begin_index;
	}

	//see original comments -- 03/20/2022
	public int setRange(String display) {
		if (! Utils.isBlank(display)) {
			begin_index = getFirstMatchedItemIndex(display);
			end_index = super.getSize() - 1;
		} else {
			begin_index = NOT_SET;
			end_index = NOT_SET;
		}
		return begin_index;
	}

	private boolean isFilterSet() {
		if (begin_index == NOT_SET){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 返回下拉框数据模型中第一个与s匹配的位置 -- XFZ@2010-12-25
	 * 
	 * @param s
	 * @return
	 */
	public int getFirstMatchedItemIndex(String s) {
		int beginIndex = NOT_FOUND;
		String temp_;

		for (int i = 0; i < super.getSize(); i++) {
			Object o_ = super.getElementAt(i);
			if (o_ != null) {
				temp_ = o_.toString();
				if (temp_ != null) {
					if (temp_.startsWith(s)) {
						beginIndex = i;
						break;
					}
				}
			}
		}
		return beginIndex;
	}

	private int getLastMatchedItemIndex(String s, int beginIndex) {
		if (beginIndex == NOT_FOUND)
			return NOT_FOUND;
		else if (beginIndex == NOT_SET)
			return NOT_SET;

		int endIndex = NOT_FOUND;
		int count = 0;
		String temp_;
		for (int i = 0; i < super.getSize(); i++) {
			Object o_ = super.getElementAt(i);
			if (o_ != null) {
				temp_ = o_.toString();
				if (temp_ != null) {
					if (temp_.startsWith(s)) {
						count++;
					}
				}
			}
		}
		endIndex = beginIndex + count - 1;
		return endIndex;
	}

	@Override
	public void fireContentsChanged(Object source, int index0, int index1) {
		if (isToFireContentsChangedTogger ){
			super.fireContentsChanged(source, index0, index1);
		}
	}

	public boolean getChangeContentsTogger() {
		return isToFireContentsChangedTogger;
	}

	public void setChangeContentsTogger(boolean status) {
		isToFireContentsChangedTogger = status;
	}

	public boolean shouldSetOldValue() {
		return shouldSetOldValue;
	}

	public void setShouldSetOldValue(boolean shouldSetOldValue) {
		this.shouldSetOldValue = shouldSetOldValue;
	}
}
