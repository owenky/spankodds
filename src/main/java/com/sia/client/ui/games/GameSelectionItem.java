package com.sia.client.ui.games;

import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AppController;

import java.util.Objects;

public class GameSelectionItem extends SelectionItem<Integer> {

	public static final GameSelectionItem promptItem;
	public static final Game dummyGame;
	private final Game game;

	static {

		dummyGame = new Game();
		dummyGame.setGame_id(SELECT_BLANK_KEY);
		AppController.getGames().add(dummyGame);
		promptItem = (GameSelectionItem)new GameSelectionItem(dummyGame).withDisplay("--- Select A Game ---");
	}
	public GameSelectionItem(Game game) {
		super(game.getGame_id());
		this.game = game;
		Comparable<SelectionItem<Integer>> comparator = (o)-> getKeyValue()-o.getKeyValue();
		withComparator(comparator);
	}
	public Game getGame() {
		return game;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		GameSelectionItem that = (GameSelectionItem) o;
		return game.equals(that.game);
	}

	@Override
	public int hashCode() {
		return Objects.hash(game);
	}

	public static String getGameDesc(Game game) {
		String visitor = game.getShortvisitorteam();
		if ( null == visitor) visitor ="";
		String home = game.getShorthometeam();
		if ( null ==home) home ="";
		return String.format("%s/%s",visitor,home);
	}
}