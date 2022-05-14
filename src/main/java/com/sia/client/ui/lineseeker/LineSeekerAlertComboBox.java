package com.sia.client.ui.lineseeker;

import com.sia.client.ui.sbt.GameIdComboKeyManager;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SBTComboBoxUI;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class LineSeekerAlertComboBox extends SBTComboBox<String, LineSeekerAlertSelectionItem> {

	private static final String myUiClassId = SBTComboBoxUI.uiClassID;

	public LineSeekerAlertComboBox() {
		super(new LinkSeekerAlertSelectionConvertor());
		init();
	}
	@Override
	public String getUIClassID() {
		return myUiClassId;
	}
	private void init() {
		setEditable(false);
		withPromptInput(promptInput);
		setKeySelectionManager(new GameIdComboKeyManager());
	}
	public void addIfAbsent(AlertConfig alertAttr, boolean setSelected) {
		ComboBoxModel<LineSeekerAlertSelectionItem> model = this.getModel();
		int selectedIndex = -1;
		int insertIndex = 1;
		for(int i=0;i<model.getSize();i++) {
			LineSeekerAlertSelectionItem item = model.getElementAt(i);
			int keyOrderDiff = item.getAlertConfig().getKey().compareTo(alertAttr.getKey());
			if ( 0 == keyOrderDiff ) {
				selectedIndex = i;
				insertIndex = -1;
				break;
			} else if ( 0 > keyOrderDiff) {
				insertIndex = i+1;
				selectedIndex = insertIndex;
			}
		}

		if ( 0 <=insertIndex ) {
			this.insertItemAt(new LineSeekerAlertSelectionItem(alertAttr.getGameId(),alertAttr.getPeriod()),insertIndex);
			selectedIndex = insertIndex;
		}

		if ( setSelected ) {
			this.setSelectedIndex(selectedIndex);
		}

	}
	public void loadAlerts() {
		List<LineSeekerAlertSelectionItem> items = AlertAttrManager.getLineSeekerAlertSelectionItem();
		Collections.sort(items);
		items.add(0,LineSeekerAlertSelectionItem.makeBlankAlert());
		this.addElement(items);
	}
}