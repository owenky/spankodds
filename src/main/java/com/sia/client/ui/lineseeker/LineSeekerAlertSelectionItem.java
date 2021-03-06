package com.sia.client.ui.lineseeker;

import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AppController;
import com.sia.client.ui.games.GameSelectionItem;

public class LineSeekerAlertSelectionItem extends SelectionItem<String> {

	private final int gameId;
	private final AlertPeriod alertPeriod;

	public LineSeekerAlertSelectionItem(int gameId, AlertPeriod alertPeriod) {
		super(AlertAttrManager.makeKey(gameId,alertPeriod));
		this.gameId = gameId;
		this.alertPeriod = alertPeriod;
		Comparable<SelectionItem<String>> comparator = (o)-> getKeyValue().compareTo(o.getKeyValue());
		Game game = AppController.getGame(gameId);
		if ( null != game ) {
			withDisplay(game.getGame_id() + " " + GameSelectionItem.getGameDesc(game) + " : " + alertPeriod.toString());
		}
	}
	@Override
	public int compareTo(SelectionItem<String> o) {
		LineSeekerAlertSelectionItem another = (LineSeekerAlertSelectionItem)o;
		int diff = gameId - another.gameId;
		if ( 0 ==  diff) {
			diff = alertPeriod.name().compareTo(another.alertPeriod.name());
		}
		return diff;
	}
	public AlertConfig getAlertConfig() {
		return AlertAttrManager.of(gameId,alertPeriod);
	}
	public static LineSeekerAlertSelectionItem makeBlankAlert() {
		return (LineSeekerAlertSelectionItem)new LineSeekerAlertSelectionItem(AlertConfig.BlankAlert.getGameId(),AlertConfig.BlankAlert.getPeriod()).withDisplay("  New Alert  ");
	}
}