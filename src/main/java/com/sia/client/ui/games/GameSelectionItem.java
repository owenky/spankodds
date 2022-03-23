package com.sia.client.ui.games;

import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;

public class GameSelectionItem extends SelectionItem<Integer> {

	public static final GameSelectionItem promptItem;
	private final Game game;

	static {

		Game dummyGame = new Game();
		dummyGame.setGame_id(SELECT_BLANK_KEY);
		promptItem = (GameSelectionItem)new GameSelectionItem(dummyGame).withDisplay("--- Select A Game ---");
	}
	public GameSelectionItem(Game game) {
		super(game.getGame_id());
		this.game = game;
		Comparable<SelectionItem<Integer>> comparator = (o)-> getKeyValue()-o.getKeyValue();
		int gameId = game.getGame_id();
		String display;

		display = gameId+ "  "+this.game.getShortvisitorteam()+"/"+this.game.getShorthometeam();
		withDisplay(display).withComparator(comparator);
	}
}