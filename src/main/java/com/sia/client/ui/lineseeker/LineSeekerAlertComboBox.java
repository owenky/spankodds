package com.sia.client.ui.lineseeker;

import com.sia.client.model.SelectionItem;
import com.sia.client.ui.sbt.GameIdComboKeyManager;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SBTComboBoxUI;

import javax.swing.*;

public class LineSeekerAlertComboBox extends SBTComboBox<String, LineSeekerAlertSelectionItem> {

	public static final LineSeekerAlertSelectionItem promptInput;
	private static final int maxGameIdLen = 6;
	private static final String myUiClassId = SBTComboBoxUI.uiClassID;

	static {
		AlertAttributes dummyAttr = new AlertAttributes(SelectionItem.SELECT_BLANK_KEY,AlertPeriod.Full);
		promptInput = (LineSeekerAlertSelectionItem)(new LineSeekerAlertSelectionItem(dummyAttr).withDisplay(" "));
	}
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
	public void addIfAbsent(AlertAttributes alertAttr,boolean setSelected) {
		ComboBoxModel<LineSeekerAlertSelectionItem> model = this.getModel();
		int selectedIndex = -1;
		int insertIndex = 1;
		for(int i=0;i<model.getSize();i++) {
			LineSeekerAlertSelectionItem item = model.getElementAt(i);
			int keyOrderDiff = item.getAlertAttributes().getKey().compareTo(alertAttr.getKey());
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
			this.insertItemAt(new LineSeekerAlertSelectionItem(alertAttr),insertIndex);
			selectedIndex = insertIndex;
		}

		if ( setSelected ) {
			this.setSelectedIndex(selectedIndex);
		}

	}
	public void loadAlerts() {
		this.addItem(LineSeekerAlertSelectionItem.makeBlankAlert());
	}
//	public void loadGames() {
//		Games games = AppController.getGames();
//		Iterator<Game> ite = games.iterator();
//		List<Game> gameList = new ArrayList<>(games.size()+2);
//		while(ite.hasNext()) {
//			Game g = ite.next();
//			if (GameUtils.isRealGame(g)) {
//				gameList.add(g);
//			}
//		}
//		gameList.sort(new GameIdSorter());
//		List<GameSelectionItem> gameItemList = gameList.stream().map(GameSelectionItem::new).collect(Collectors.toList());
//
//		int maxDisplayLen = 0;
//		GameSelectionItem prototypeDisplayValue = null;
//		for(GameSelectionItem gItem: gameItemList) {
//			Game game = gItem.getGame();
//			int gameId = game.getGame_id();
//			String gameIdStr = formatGameId(gameId);
//			String display = String.format("%s  %s",gameIdStr,GameSelectionItem.getGameDesc(game));
//			gItem.withDisplay(display);
//			if ( display.length() > maxDisplayLen) {
//				maxDisplayLen = display.length();
//				prototypeDisplayValue = gItem;
//			}
//
//		}
//		setPrototypeDisplayValue(prototypeDisplayValue);
//		gameItemList.add(0,promptInput);
//		this.addElement(gameItemList);
//	}
//
//	private static String formatGameId(int gameId) {
//		StringBuilder sb = new StringBuilder(String.valueOf(gameId));
//		for(int i=sb.length();i<maxGameIdLen;i++) {
//			sb.append(" ");
//		}
//		return sb.toString();
//	}
}