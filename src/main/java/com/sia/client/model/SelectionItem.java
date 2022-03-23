package com.sia.client.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class SelectionItem<T> implements Comparable<SelectionItem<T>> {
	public static final Integer SELECT_ALL_KEY = Integer.MAX_VALUE;
	public static final Integer SELECT_BLANK_KEY = -1;
	public static final Integer SELECT_REFRESH = -2;
	public static final Integer READING = -3;
	public static final Integer UPDATE_FAILURE = -4;
	private String display;
	private final T keyValue;
	private Comparable<SelectionItem<T>> comparator;
	private static final Set<Integer> specialKeys;

	static {
		specialKeys = new HashSet<>();
		specialKeys.add(SELECT_ALL_KEY);
		specialKeys.add(SELECT_BLANK_KEY);
		specialKeys.add(SELECT_REFRESH);
		specialKeys.add(UPDATE_FAILURE);
		specialKeys.add(READING);
	}

	public SelectionItem(T keyValue) {
		this.keyValue = keyValue;
		this.comparator = (o)-> String.valueOf(this.keyValue).compareTo(String.valueOf(o.keyValue));
	}
	@Override
	public int compareTo(SelectionItem o) {
		return comparator.compareTo(o);
	}
	public T getKeyValue(){
		return keyValue;
	}

	public SelectionItem<T> withDisplay(String display) {
		this.display = display;
		return this;
	}
	public SelectionItem<T> withComparator(Comparable<SelectionItem<T>> comparator) {
		this.comparator = comparator;
		return this;
	}
	public boolean isSpecialItem(){
		return isObjectSpecialItemKey(getKeyValue());
	}
	public static boolean isObjectSpecialItem(Object obj_){
		if  (!(obj_ instanceof SelectionItem)){
			return false;
		}else{
			return ((SelectionItem<?>)obj_).isSpecialItem();
		}
	}
	public static boolean isObjectSpecialItemKey(Object obj_){
		return specialKeys.contains(obj_);
		
	}

	public boolean isValueBlank() {
		return SelectionItem.SELECT_BLANK_KEY.equals(getKeyValue());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SelectionItem<?> that = (SelectionItem<?>) o;
		return keyValue.equals(that.keyValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(keyValue);
	}

	@Override
	public String toString(){
		return display;
	}

	public String getDisplay(){
		return display;
	}
	public String valueOf(String text){ // 在ComboBoxInputLineEditor 的getItem 方法中被反射使用
		return String.valueOf(text);
	}
}