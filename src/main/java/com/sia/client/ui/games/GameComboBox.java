package com.sia.client.ui.games;

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
import com.sia.client.model.GameIdSorter;
import com.sia.client.model.Games;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AppController;
import com.sia.client.ui.sbt.GameIdComboKeyManager;
import com.sia.client.ui.sbt.SBTComboBox;
import com.sia.client.ui.sbt.SBTComboBoxUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GameComboBox extends SBTComboBox<Integer,GameSelectionItem> {

	public static final GameSelectionItem promptInput;
	private static final int maxGameIdLen = 6;
	//use default ComboBoxUI instead of SBT style UI -- 03/23/2022
//	private static final String myUiClassId = "ComboBoxUI";
	private static final String myUiClassId = SBTComboBoxUI.uiClassID;

	static {
		Game dummyGame = new Game();
		dummyGame.setGame_id(SelectionItem.SELECT_BLANK_KEY);
		promptInput = (GameSelectionItem)(new GameSelectionItem(dummyGame).withDisplay(" "));
	}
	public GameComboBox() {
		super(new GameSelectionConvertor());
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
	public void loadGames() {
		Games games = AppController.getGames();
		Iterator<Game> ite = games.iterator();
		List<Game> gameList = new ArrayList<>(games.size()+2);
		while(ite.hasNext()) {
			Game g = ite.next();
			if (GameUtils.isRealGame(g)) {
				gameList.add(g);
			}
		}
		gameList.sort(new GameIdSorter());
		List<GameSelectionItem> gameItemList = gameList.stream().map(GameSelectionItem::new).collect(Collectors.toList());

		int maxDisplayLen = 0;
		GameSelectionItem prototypeDisplayValue = null;
		for(GameSelectionItem gItem: gameItemList) {
			Game game = gItem.getGame();
			int gameId = game.getGame_id();
			String gameIdStr = formatGameId(gameId);
			String display = String.format("%s  %s",gameIdStr,GameSelectionItem.getGameDesc(game));
			gItem.withDisplay(display);
			if ( display.length() > maxDisplayLen) {
				maxDisplayLen = display.length();
				prototypeDisplayValue = gItem;
			}

		}
		setPrototypeDisplayValue(prototypeDisplayValue);
		gameItemList.add(0,promptInput);
		this.addElement(gameItemList);
	}

	private static String formatGameId(int gameId) {
		StringBuilder sb = new StringBuilder(String.valueOf(gameId));
		for(int i=sb.length();i<maxGameIdLen;i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}