package com.sia.client.ui.games;

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

	static {
		Game dummyGame = new Game();
		dummyGame.setGame_id(SelectionItem.SELECT_BLANK_KEY);
		promptInput = (GameSelectionItem)(new GameSelectionItem(dummyGame).withDisplay("-- Select Game --"));
	}
	public GameComboBox() {
		super(new GameSelectionConvertor());
		init();
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
			if ( 0 < g.getGame_id()) {
				gameList.add(g);
			}
		}
		gameList.sort(new GameIdSorter());
		List<GameSelectionItem> gameItemList = gameList.stream().map(GameSelectionItem::new).collect(Collectors.toList());
		this.addElement(gameItemList);
	}
}