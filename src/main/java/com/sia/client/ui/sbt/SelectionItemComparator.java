package com.sia.client.ui.sbt;

import com.sia.client.model.SelectionItem;

import java.text.Collator;
import java.util.Comparator;

public class SelectionItemComparator implements Comparator<SelectionItem> {

	public enum ComparingPart {KEY,DISPLAY};
	private ComparingPart comparingPart;

	public SelectionItemComparator() {
		this(ComparingPart.KEY);
	}
	public SelectionItemComparator(ComparingPart comparingPart) {
		setComparePart(comparingPart);
	}
	public void setComparePart(ComparingPart comparingPart_) {
		this.comparingPart = comparingPart_;
	}

	@Override
	public int compare(SelectionItem obj1,SelectionItem obj2) {

		if (obj1 == null && obj2 == null) {
			return 0;
		}
		if (obj1 == null) {
			return -1;
		}
		if (obj2 == null) {
			return 1;
		}

		if (comparingPart == ComparingPart.KEY) {
			return compareKey(obj1.getKeyValue(), obj2.getKeyValue());
		} else {
			return compareString(obj1.getDisplay(), obj2.getDisplay());
		}

	}

	private int compareKey(Object key1, Object key2) {

		if (key1 == null && key2 == null) {
			return 0;
		}
		if (key1 == null) {
			return -1;
		}
		if (key2 == null) {
			return 1;
		}

		if (key1 == SelectionItem.SELECT_BLANK_KEY) {
			return -1;
		} else if (key2 == SelectionItem.SELECT_BLANK_KEY) {
			return 1;
		}

		if (key1 instanceof Integer && key2 instanceof Integer) {
			if (((Integer) key1).intValue() < ((Integer) key2).intValue()) {
				return -1;
			} else if (((Integer) key1).intValue() == ((Integer) key2)
					.intValue()) {
				return 0;
			} else {
				return 1;
			}

		} else {
			String str1 = key1.toString();
			String str2 = key2.toString();

			return compareString(str1, str2);
		}
	}

	private int compareString(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return 0;
		}
		if (str1 == null) {
			return -1;
		}
		if (str2 == null) {
			return 1;
		}
		str1 = str1.trim();
		str2 = str2.trim();
		Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

		if (cmp.compare(str1, str2) > 0) {
			return 1;
		} else if (cmp.compare(str1, str2) < 0) {
			return -1;
		}
		return 0;
	}
}