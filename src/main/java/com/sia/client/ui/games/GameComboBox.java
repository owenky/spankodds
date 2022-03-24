package com.sia.client.ui.games;

import com.sia.client.config.GameUtils;
import com.sia.client.model.Game;
import com.sia.client.model.GameIdSorter;
import com.sia.client.model.Games;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AppController;
import com.sia.client.ui.sbt.SBTComboBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

// 特征是编辑框为下划线
public class GameComboBox extends SBTComboBox<Integer,GameSelectionItem> {

	public static final GameSelectionItem promptInput;
	private static final String myUiClassId = "ComboBoxUI";
	//use default ComboBoxUI instead of SBT style UI -- 03/23/2022
//	private static final String myUiClassId = SBTComboBoxUI.uiClassID;

	static {
		Game dummyGame = new Game();
		dummyGame.setGame_id(SelectionItem.SELECT_BLANK_KEY);
		promptInput = (GameSelectionItem)(new GameSelectionItem(dummyGame).withDisplay("-- Select Game --"));
	}
	public GameComboBox() {
		super(new GameSelectionConvertor());
		init();
	}
	@Override
	public String getUIClassID() {
//		return myUiClassId;
		return super.getUIClassID();
	}
	private void init() {
		withPromptInput(promptInput);
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
		int largestGameId = gameItemList.get(gameItemList.size()-1).getGame().getGame_id();
		String largestGameIdStr = String.valueOf(largestGameId);
		int maxGameIdLength = largestGameIdStr.length();

		int maxDisplayLen = 0;
		GameSelectionItem prototypeDisplayValue = null;
		for(GameSelectionItem gItem: gameItemList) {
			Game game = gItem.getGame();
			int gameId = game.getGame_id();
			String display = String.format("%"+maxGameIdLength+"d  %s/%s",gameId,game.getShortvisitorteam(),game.getShorthometeam());
			gItem.withDisplay(display);
			if ( display.length() > maxDisplayLen) {
				maxDisplayLen = display.length();
				prototypeDisplayValue = gItem;
			}

		}
		setPrototypeDisplayValue(prototypeDisplayValue);
		this.addElement(gameItemList);
	}
}