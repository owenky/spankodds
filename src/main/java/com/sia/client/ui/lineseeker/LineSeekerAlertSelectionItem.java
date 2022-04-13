package com.sia.client.ui.lineseeker;

import com.sia.client.model.Game;
import com.sia.client.model.SelectionItem;
import com.sia.client.ui.AppController;
import com.sia.client.ui.games.GameSelectionItem;

public class LineSeekerAlertSelectionItem extends SelectionItem<String> {

	private final AlertConfig alertAttributes;
	public LineSeekerAlertSelectionItem(AlertConfig alertAttributes) {
		super(alertAttributes.getKey());
		this.alertAttributes = alertAttributes;
		Comparable<SelectionItem<String>> comparator = (o)-> getKeyValue().compareTo(o.getKeyValue());
		Game game = AppController.getGame(alertAttributes.getGameId());
		withComparator(comparator);
		if ( null != game ) {
			withDisplay(game.getGame_id() + " " + GameSelectionItem.getGameDesc(game) + " : " + alertAttributes.getPeriod().toString());
		}
	}
	public AlertConfig getAlertAttributes() {
		return alertAttributes;
	}
	public static LineSeekerAlertSelectionItem makeBlankAlert() {
		AlertConfig dummyAttribute = new AlertConfig(SELECT_BLANK_KEY, AlertPeriod.Full);
		return (LineSeekerAlertSelectionItem)new LineSeekerAlertSelectionItem(dummyAttribute).withDisplay("  New Alert  ");
	}
}